package com.bfxy.netty.http;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyHttpServerTest {

	static final int PORT = 8888;
	 
    public static void main(String[] args) throws Exception {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
      
		DefaultEventExecutorGroup defaultEventExecutorGroup = new DefaultEventExecutorGroup(
                4,
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyServerWorkerThread_" + this.threadIndex.incrementAndGet());
                    }
                });
		
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.childOption(ChannelOption.TCP_NODELAY,true);
            b.childOption(ChannelOption.SO_KEEPALIVE,true);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(defaultEventExecutorGroup,
                            		new HttpServerCodec(),
                            		new HttpObjectAggregator(1024*1024),
                            		new HttpServerExpectContinueHandler(),
                            		new NettyHttpServerHandler());
                        }
                    });

            Channel ch = b.bind(PORT).sync().channel();
            System.err.println("Netty http server listening on port "+ PORT);
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    public static class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, HttpObject message) throws Exception {
			
			//	判断是否为HttpRequest对象
			if (message instanceof HttpRequest){
				HttpRequest request = (HttpRequest)message;
				HttpMethod method = request.method();
				HttpHeaders headers = request.headers();
				
				//	判断请求类型 get/post做不同的逻辑处理
				if(HttpMethod.GET.equals(method)) {
					System.err.println("doGet ..");
				} 
				else if (HttpMethod.POST.equals(method)) {
					System.err.println("doPost ..");
					//	post请求得到fullRequest可以进行解析处理
					FullHttpRequest fullRequest = (FullHttpRequest)message;
			        String contentType = headers.get(HttpHeaderNames.CONTENT_TYPE);
			        contentTypeConverter(contentType);
				}
				
				sendResponse(ctx, request);
			} else {
				log.error("#NettyHttpServerHandler.channelRead0# message type is not HttpRequest: {}", message);
			}
		}
		
		/**
		 * 	$contentTypeConverter 根据不同的contentType 可以做不同的数据转换
		 * @param contentType
		 */
		private void contentTypeConverter(String contentType) {
			if(StringUtils.isNotBlank(contentType)) {
				//	这里我们可以根据不同类型的contentType做响应的处理
				if(HttpHeaderValues.APPLICATION_JSON.toString().equals(contentType)){
		        	System.err.println("contentType is application/json");
		        } else if(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString().equals(contentType)) {
		        	System.err.println("contentType is application/x-www-form-urlencoded");
		        } 
				// else if ....
			} else {
				log.warn("#NettyHttpServerHandler.channelRead0# message contentType is null, contentType: {}", contentType);
			}
		}
		
		/**
		 * 	$sendResponse 写出响应信息
		 * @param ctx
		 * @param request
		 */
		private void sendResponse(ChannelHandlerContext ctx, HttpRequest request) {
			String responseBody = "hello client";
            DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(
            		HttpVersion.HTTP_1_1,
            		HttpResponseStatus.OK, 
            		Unpooled.wrappedBuffer(responseBody.getBytes()));
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
            //	如果keepAlive是true则保持连接，否则关闭连接
            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (!keepAlive) {
                ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
            } else {
            	httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.writeAndFlush(httpResponse);
            }
		}
    	
    }
}
