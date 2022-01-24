package com.bfxy.rapid.rpc.zookeeper;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ThreadUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 	CuratorImpl
 */
public class CuratorImpl implements ZookeeperClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuratorImpl.class);

	//	zookeeper address地址
	private String address;
	
	//	会话超时时间
	private int sessionTimeout;
	
	//	连接超时时间
	private int connectionTimeout;
	
	//	?1 单线程的线程池监听数据缓存变更
    private final ExecutorService EVENT_THREAD_POOL = Executors.newFixedThreadPool(1, ThreadUtils.newThreadFactory("PathChildrenCache"));
    
    //	?2 单线程的线程池数据变更的事件监听
    private final ExecutorService DIRECT_EXECUTOR = Executors.newFixedThreadPool(1, ThreadUtils.newThreadFactory("PathChildrenCache"));
    
    //	客户端实例对象
	private CuratorFramework client;
	
	public CuratorImpl(String address, int connectionTimeout) {
		creator(address, connectionTimeout);
	}
	
	/**
	 * creator
	 * @param address	服务地址
	 * @param connectionTimeout	 连接超时时间
	 */
	private void creator(String address, int connectionTimeout){
        client = CuratorFrameworkFactory.builder()
        		//	192.168.11.221:2181,192.168.11.222:2181,192.168.11.223:2181
                .connectString(address)
                //	RetryNTimes 连接次数, 连接时间间隔
                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 3000))
                //	连接超时时间
                .connectionTimeoutMs(connectionTimeout)
                //	会话超时时间
                .sessionTimeoutMs(connectionTimeout).build();
        
        //	因为client.start是异步化的建立连接
        CountDownLatch latch = new CountDownLatch(1);
        addConnectionChangeListenter(new ConnectionWatcher(latch));
        
        //	启动客户端实例对象 建立连接
        client.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
        	LOGGER.error("start zk latch.await() error", e);
            Thread.currentThread().interrupt();
        }  
	}
    
	/**
	 * 	/parent/aaa/bbb/ccc   1234
	 * 
	 * 	/parent
	 * 		/aaa
	 * 			/bbb
	 * 				/ccc
	 * 
	 * 	/parent/aaa/bbb/ccc
	 * 
	 */
    public void addPersistentNode(String path, String data) throws Exception {
        try {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, data.getBytes(Charset.defaultCharset()));
        } catch (KeeperException.NodeExistsException e) {
            LOGGER.warn("Node already exists: {}", path);
        } catch (Exception e) {
            throw new Exception("addPersistentNode error", e);
        }
    }
    
    public String addEphemeralNode(String path, String data) throws Exception {
        return client.create()
        		.withMode(CreateMode.EPHEMERAL)
        		.forPath(path, data.getBytes(Charset.defaultCharset()));
    }
    
    public Stat setData(String path, String data) throws Exception {
        return client.setData().forPath(path, data.getBytes(Charset.defaultCharset()));
    }

    public String getData(String path) throws Exception {
        return new String(client.getData().forPath(path), Charset.defaultCharset());
    }
    
    public void deletePath(String path) throws Exception {
        client.delete().forPath(path);
    }
    
    public boolean isConnected() {
        return client.getZookeeperClient().isConnected();
    }
    
    public boolean checkExists(String path) throws Exception {
        return (null == client.checkExists().forPath(path) ? false : true);
    }
    
    public List<String> getNodes(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    
    @SuppressWarnings({"resource", "incomplete-switch"})
    public void listener4ChildrenPath(final String parent, final NodeListener listener) throws Exception {
    	
    	//	建立一个本地缓存
		PathChildrenCache cache = new PathChildrenCache(client,	//	客户端对象实例
				parent,	//	要监听的父节点
				true, 	//	必须设置为true
				false,	//	不需要压缩数据	 
				EVENT_THREAD_POOL	//	单线程的线程池监听数据缓存变更
				);
		//	启动我们的缓存监听机制
		cache.start(StartMode.POST_INITIALIZED_EVENT);
		LOGGER.info("add listener parent path start, path : {}", parent);
		
        cache.getListenable().addListener(new PathChildrenCacheListener() {
        	
        	//	直接子节点数据(包括path和data)发生变化
            public void childEvent(CuratorFramework curator, PathChildrenCacheEvent event) throws Exception {
                if (event.getData() == null) return;
                switch (event.getType()) {
                    case CHILD_ADDED:
                        listener.nodeChanged(CuratorImpl.this,
                        		new ChangedEvent(event.getData().getPath(),
                        				new String(event.getData().getData()),
                        				ChangedEvent.Type.CHILD_ADDED));
                        break;
                    case CHILD_REMOVED:
                        listener.nodeChanged(CuratorImpl.this, 
                        		new ChangedEvent(event.getData().getPath(),
                        				new String(event.getData().getData()),
                        				ChangedEvent.Type.CHILD_REMOVED));
                        break;
                    case CHILD_UPDATED:
                        listener.nodeChanged(CuratorImpl.this,
                        		new ChangedEvent(event.getData().getPath(),
                        				new String(event.getData().getData()),
                        				ChangedEvent.Type.CHILD_UPDATED));
                        break;
                }
            }
        }, DIRECT_EXECUTOR);
        
    }
    
    
    @PreDestroy
    public void close() {
		if (client != null) {
			try {
				this.client.close();
				LOGGER.info("zookeeper client is closed");
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("zookeeper client is closed is error: {}", e);
			}
		}
    }
    
    /**
     * ConnectionWatcher
     *
     */
    @SuppressWarnings("static-access")
    private static class ConnectionWatcher implements ConnectionStateListener {
        CountDownLatch latch;
        ConnectionWatcher(CountDownLatch latch) {
            this.latch = latch;
        }
		public void stateChanged(ZookeeperClient sender, ConnectionState state) {
			//	当连接状态为CONNECTED的时候
            if (state == state.CONNECTED) {
            	//	释放阻塞线程
                latch.countDown();
            }			
		}
    }
    
    /**
     * addConnectionChangeListenter
     * 添加连接时间监听的方法
     * @param listener
     */
    private void addConnectionChangeListenter(final ConnectionStateListener listener) {
        if (listener != null) {
            client.getConnectionStateListenable().addListener(new org.apache.curator.framework.state.ConnectionStateListener() {
                public void stateChanged(CuratorFramework sender, org.apache.curator.framework.state.ConnectionState state) {
                    listener.stateChanged(CuratorImpl.this, convertTo(state));
                }
            });
        }
    }
    
    private ConnectionState convertTo(org.apache.curator.framework.state.ConnectionState state) {
        switch (state) {
            case CONNECTED:
                return ConnectionState.CONNECTED;
            case SUSPENDED:
                return ConnectionState.SUSPENDED;
            case RECONNECTED:
                return ConnectionState.RECONNECTED;
            case LOST:
                return ConnectionState.LOST;
            default:
                return ConnectionState.READ_ONLY;
        }
    }
	
    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public CuratorFramework getClient() {
		return client;
	}

}
