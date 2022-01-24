package com.bfxy.rapid.rpc.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections4.CollectionUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;


/**
 * 	$RpcConnectManager
 * @author 17475
 *
 */
@Slf4j
public class RpcConnectManager {

//	private static volatile RpcConnectManager  RPC_CONNECT_MANAGER = new RpcConnectManager();
	
    public RpcConnectManager() {
    }
	
	/*	一个连接的地址，对应一个实际的业务处理器(client)	*/
	private Map<InetSocketAddress, RpcClientHandler> connectedHandlerMap = new ConcurrentHashMap<InetSocketAddress, RpcClientHandler>();
	
	/*	所有连接成功的地址 所对应的 任务执行器列表 connectedHandlerList */
	private CopyOnWriteArrayList<RpcClientHandler> connectedHandlerList = new CopyOnWriteArrayList<RpcClientHandler>();
	
	/*	用于异步的提交连接请求的线程池	*/
	private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
	
	private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
	
	private ReentrantLock connectedLock = new ReentrantLock();
	
	private Condition connectedCondition = connectedLock.newCondition();
	
	private long connectTimeoutMills = 6000;
	
	private volatile boolean isRunning = true;
	
	private volatile AtomicInteger handlerIdx = new AtomicInteger(0);
	
	//1. 异步连接 线程池 真正的发起连接，连接失败监听，连接成功监听
	//2. 对于连接进来的资源做一个缓存（做一个管理）updateConnectedServer
	/**
	 * 	$connect 发起连接方法
	 * @param serverAddress
	 */
	public void connect(final String serverAddress) {
		List<String> allServerAddress = Arrays.asList(serverAddress.split(","));
		updateConnectedServer(allServerAddress);
	}
	
	/** 
	 * 	add connect List<String> serverAddress
	 * @param serverAddress
	 */
    public void connect(List<String> serverAddress) {
        updateConnectedServer(serverAddress);
    }
	
	/**
	 * 	$更新缓存信息 并 异步发起连接
	 * 	192.168.11.111:8765,192.168.11.112:8765
	 * @param allServerAddress
	 */
	public void updateConnectedServer(List<String> allServerAddress) {
		if(CollectionUtils.isNotEmpty(allServerAddress)) {
			
			//	1.解析allServerAddress地址 并且临时存储到我们的newAllServerNodeSet HashSet集合中
			HashSet<InetSocketAddress> newAllServerNodeSet = new HashSet<InetSocketAddress>();
			for(int i =0; i < allServerAddress.size(); i++) {
				String[] array = allServerAddress.get(i).split(":");
				if(array.length == 2) {
					String host = array[0];
					int port = Integer.parseInt(array[1]);
					final InetSocketAddress remotePeer = new InetSocketAddress(host, port);
					newAllServerNodeSet.add(remotePeer);
				}
			}
			
			//	2.调用建立连接方法 发起远程连接操作
			for(InetSocketAddress serverNodeAddress : newAllServerNodeSet) {
				if(!connectedHandlerMap.keySet().contains(serverNodeAddress)) {
					connectAsync(serverNodeAddress);
				}
			}
			
			//	3. 如果allServerAddress列表里不存在的连接地址，那么我需要从缓存中进行移除
			for(int i = 0; i< connectedHandlerList.size(); i++) {
				RpcClientHandler rpcClientHandler = connectedHandlerList.get(i);
				SocketAddress remotePeer = rpcClientHandler.getRemotePeer();
				if(!newAllServerNodeSet.contains(remotePeer)) {
					log.info(" remove invalid server node " + remotePeer);
					RpcClientHandler handler = connectedHandlerMap.get(remotePeer);
					if(handler != null) {
						handler.close();
						connectedHandlerMap.remove(remotePeer);
					}
					connectedHandlerList.remove(rpcClientHandler);
				}
			}
			
		} else {
			// 添加告警
			log.error(" no available server address! ");
			// 清除所有的缓存信息
			clearConnected();
		}
	}

	/**
	 * 	$connectAsync 异步发起连接的方法
	 * @param serverNodeAddress
	 */
	private void connectAsync(InetSocketAddress remotePeer) {
		threadPoolExecutor.submit(new Runnable() {
			@Override
			public void run() {
				Bootstrap b = new Bootstrap();
				b
				.group(eventLoopGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new RpcClientInitializer());
				connect(b, remotePeer);
			}
		});
	}
	
	private void connect(final Bootstrap b, InetSocketAddress remotePeer) {
		
		//	1.真正的建立连接
		final ChannelFuture channelFuture = b.connect(remotePeer);
		
		//	2.连接失败的时候添加监听 清除资源后进行发起重连操作
		channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				log.info("channelFuture.channel close operationComplete, remote peer =" + remotePeer);
				future.channel().eventLoop().schedule(new Runnable() {
					
					@Override
					public void run() {
						log.warn(" connect fail, to reconnect! ");
						clearConnected();
						connect(b, remotePeer);
					}
		
				}, 3, TimeUnit.SECONDS);
			}
		});
		
		//	3.连接成功的时候添加监听 把我们的新连接放入缓存中
		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()) {
					log.info("successfully connect to remote server, remote peer = " + remotePeer);
					RpcClientHandler handler = future.channel().pipeline().get(RpcClientHandler.class);
					addHandler(handler);
				}
			}
		});
	}
	
	/**
	 * 	$clearConnected
	 * 	连接失败时，及时的释放资源，清空缓存
	 * 	先删除所有的connectedHandlerMap中的数据
	 * 	然后再清空connectedHandlerList中的数据
	 */
	private void clearConnected() {
		for(final RpcClientHandler rpcClientHandler : connectedHandlerList) {
			// 通过RpcClientHandler 找到具体的remotePeer, 从connectedHandlerMap进行移除指定的 RpcClientHandler
			SocketAddress remotePeer = rpcClientHandler.getRemotePeer();
			RpcClientHandler handler = connectedHandlerMap.get(remotePeer);
			if(handler != null) {
				handler.close();
				connectedHandlerMap.remove(remotePeer);
			}
		}
		connectedHandlerList.clear();
	}
	
	
	/**
	 * 	$addHandler 添加RpcClientHandler到指定的缓存中
	 * 	connectedHandlerMap & connectedHandlerList
	 * 	
	 * @param handler
	 */
	private void addHandler(RpcClientHandler handler) {
		connectedHandlerList.add(handler);
		InetSocketAddress remoteAddress = //(InetSocketAddress) handler.getRemotePeer();
				(InetSocketAddress) handler.getChannel().remoteAddress();

		connectedHandlerMap.put(remoteAddress, handler);
		//signalAvailableHandler 唤醒可用的业务执行器
		signalAvailableHandler();
	}

	/**
	 * 	唤醒另外一端的线程(阻塞的状态中) 告知有新连接接入
	 */
	private void signalAvailableHandler() {
		connectedLock.lock();
		try {
			connectedCondition.signalAll();
		} finally {
			connectedLock.unlock();
		}
	}
	
	/**
	 * 	$waitingForAvailableHandler 等待新连接接入通知方法
	 * @return
	 * @throws InterruptedException
	 */
	private boolean waitingForAvailableHandler() throws InterruptedException {
		connectedLock.lock();
		try {
			return connectedCondition.await(this.connectTimeoutMills, TimeUnit.MICROSECONDS);
		} finally {
			connectedLock.unlock();
		}
	}
	
	
	/**
	 * $chooseHandler 选择一个实际的业务处理器
	 * @return RpcClientHandler
	 */
	public RpcClientHandler chooseHandler() {
		CopyOnWriteArrayList<RpcClientHandler> handlers = (CopyOnWriteArrayList<RpcClientHandler>)this.connectedHandlerList.clone();
		
		int size = handlers.size();
		
		while(isRunning && size <= 0) {
			try {
				boolean available = waitingForAvailableHandler();
				if(available) {
					handlers = (CopyOnWriteArrayList<RpcClientHandler>)this.connectedHandlerList.clone();
					size = handlers.size();
				}
			} catch (InterruptedException e) {
				log.error(" wating for available node is interrupted !");
				throw new RuntimeException("no connect any servers!", e);
			}
		}
		if(!isRunning) {
			return null;
		}
		// 	最终使用取模方式取得其中一个业务处理器进行实际的业务处理
		return handlers.get(((handlerIdx.getAndAdd(1) + size) % size));
	}
	
	/**
	 * 	$stop 关闭的方法
	 */
	public void stop() {
		isRunning = false;
		for(int i = 0; i< connectedHandlerList.size(); i++) {
			RpcClientHandler rpcClientHandler = connectedHandlerList.get(i);
			rpcClientHandler.close();
		}
		// 在这里要调用一下唤醒操作
		signalAvailableHandler();
		threadPoolExecutor.shutdown();
		eventLoopGroup.shutdownGracefully();
	}
	
	/**
	 * $reconnect 发起重连方法 需要把对应的资源进行释放
	 * @param handler
	 * @param remotePeer
	 */
	public void reconnect(final RpcClientHandler handler , final SocketAddress remotePeer) {
		if(handler != null) {
			handler.close();
			connectedHandlerList.remove(handler);
			connectedHandlerMap.remove(remotePeer);
		}
		connectAsync((InetSocketAddress) remotePeer);
	}
	
}
