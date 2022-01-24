package com.bfxy.netty.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.ErrorDataEncoderException;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * 	$NettyHttpClientTest
 */
public class NettyHttpClientTest {

	public static void main(String[] args) throws InterruptedException, URISyntaxException, UnsupportedEncodingException, ErrorDataEncoderException {
		
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		
		//	创建线程组，这个是Netty除去BossGroup 和  workerGroup 以外，还可以对handler去使用异步线程池去执行
		DefaultEventExecutorGroup defaultEventExecutorGroup = new DefaultEventExecutorGroup(
				4,
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyClientWorkerThread_" + this.threadIndex.incrementAndGet());
                    }
                });
		
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            
                            //	小伙伴们注意看这里，就是使用了上面的线程组去处理一个个加入到pipeline中的handler
                            p.addLast(defaultEventExecutorGroup, 
                            		//	接收Http请求，必须要使用下面三个对应的codec、ObjectAggregator、ExpectContinueHandler处理器
                            		new HttpClientCodec(),
                            		new HttpObjectAggregator(1024*1024),
                            		new HttpServerExpectContinueHandler(),
                            		new HttpClientHandler());
                        }
                    });

            Channel channel = bootstrap.connect("127.0.0.1", 8888).sync().channel();

            // 	在客户端与服务器端建立连接之后，我们可以发送http get请求 和 http post请求
            //	http get
            sendHttpGet(channel);
            
            //	http post
//            sendHttpPost(channel);
            channel.closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
        }
	}
	
	/**
	 * 	$sendHttpGet
	 * @param channel
	 * @throws URISyntaxException
	 * @throws UnsupportedEncodingException
	 * @throws InterruptedException
	 */
	private static void sendHttpGet(Channel channel) throws URISyntaxException, UnsupportedEncodingException, InterruptedException {
        //	请求地址
		URI uri = new URI("http://127.0.0.1:8888");
		//	使用DefaultFullHttpRequest类包装请求信息，比如协议版本、协议类型、请求路径和请求内容
        DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(
        		HttpVersion.HTTP_1_1, 
        		HttpMethod.GET, 
        		uri.toASCIIString(),
        		Unpooled.wrappedBuffer("hello server".getBytes("UTF-8")));	
        
        // 	可以在httpRequest.headers中设置各种需要的信息。
        httpRequest.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
        httpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpRequest.content().readableBytes());
        httpRequest.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
        
        // 	使用channel发送httpRequest到Server端
        channel.writeAndFlush(httpRequest).sync();	
	}
	
	/**
	 * 	$sendHttpPost
	 * @param channel
	 * @throws ErrorDataEncoderException
	 * @throws URISyntaxException
	 * @throws InterruptedException
	 */
	private static void sendHttpPost(Channel channel) throws ErrorDataEncoderException, URISyntaxException, InterruptedException {
	
		//	请求地址
		URI uri = new URI("http://127.0.0.1:8888");
		
		// 	这里我们也可以使用HttpDataFactory工厂类去创建请求，Disk if MINSIZE exceed
        HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); 
        // 	预编译（组织）Http请求对象 Prepare the HTTP request.
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(
        		HttpVersion.HTTP_1_1,
        		HttpMethod.POST, 
        		uri.toASCIIString());

        // 	对请求内容进行编码，使用到HttpDataFactory, Use the PostBody encoder
        HttpPostRequestEncoder bodyRequestEncoder =
        		// 	注意最后一个参数为false则代表	 => not multipart
                new HttpPostRequestEncoder(factory, request, false);  
        
        //	添加请求体内容
        bodyRequestEncoder.addBodyAttribute("param1", "val1");
        bodyRequestEncoder.addBodyAttribute("param2", "val2");

        //	可以给body添加文件等
        //	bodyRequestEncoder.addBodyFileUpload("myfile", file, "application/x-zip-compressed", false);
        // 	it is legal to add directly header or cookie into the request until finalize
        
        
        //	这部分是对请求头的设置headers，比如我们可以设置常见的请求头信息
        request.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);

        // 	发送请求，send request
        channel.writeAndFlush(request);
	}
	
	
	/**
	 * 	$HttpClientHandler 内置的HttpClientHandler，主要接受Server端会送的响应
	 * 	小伙伴们注意：
	 * 	在这里我们的HttpClientHandler 继承自 SimpleChannelInboundHandler，我们点击该类进入源码。
	 * 	public abstract class SimpleChannelInboundHandler<I> extends ChannelInboundHandlerAdapter {
	 * 	发现ChannelInboundHandlerAdapter是SimpleChannelInboundHandler的父类。
	 * 	这里希望小伙伴们仔细阅读源码，你会发现它们的区别就是一个对buffer对象没有回收释放资源处理，另一个帮你做了回收释放资源处理。
	 */
	private static class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {
		
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, HttpObject message) throws Exception {
            //	判断是否为HttpResponse对象
			if(message instanceof HttpResponse){
            	HttpResponse dfhr = (HttpResponse)message;
            	//	打印服务器返回的httpResponse
                System.err.println(dfhr.toString());	
            }
			//	判断是否包含请求体内容处理
            if(message instanceof HttpContent) {
            	HttpContent hc = (HttpContent)message;
    			ByteBuf buf = (ByteBuf) hc.content();
    			byte[] req = new byte[buf.readableBytes()];
    			buf.readBytes(req);
    			String body = new String(req, "utf-8");
    			//	打印服务器返回的body
    			System.err.println("Server Receive Content :" + body );
            }
			
		}
    }
}
