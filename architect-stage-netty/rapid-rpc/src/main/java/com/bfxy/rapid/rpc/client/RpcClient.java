package com.bfxy.rapid.rpc.client;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bfxy.rapid.rpc.client.proxy.RpcAsyncProxy;
import com.bfxy.rapid.rpc.client.proxy.RpcProxyImpl;

/**
 * 	$RpcClient
 * @author hezhuo.bai-JiFeng
 * @since 2019年10月17日 下午3:00:21
 */
public class RpcClient {

    private final Map<Class<?>, Object> syncProxyInstanceMap = new ConcurrentHashMap<Class<?>, Object>();

    private final Map<Class<?>, Object> asyncProxyInstanceMap = new ConcurrentHashMap<Class<?>, Object>();

    private String serverAddress;
    
    private List<String> serverAddressList;
    
    private long timeout;
    
    private RpcConnectManager rpcConnectManager;
    
    public void initClient(String serverAddress, long timeout) {
        this.serverAddress = serverAddress;
        this.timeout = timeout;
        this.rpcConnectManager = new RpcConnectManager();
        connect();
    }
    
    private void connect() {
        this.rpcConnectManager.connect(this.serverAddress);
    }
    
    /**
     * 	initClient: 直接返回对应的代理对象，把RpcConnectManager透传到代理对象中
     * @param <T>
     * @param serverAddress
     * @param timeout
     * @param interfaceClass
     * @return RpcProxyImpl
     */
    @SuppressWarnings("unchecked")
	public <T> T initClient(List<String> serverAddress, long timeout, Class<T> interfaceClass) {
        this.serverAddressList = serverAddress;
        this.timeout = timeout;
        this.rpcConnectManager = new RpcConnectManager();
        this.rpcConnectManager.connect(this.serverAddressList);
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RpcProxyImpl<>(rpcConnectManager, interfaceClass, timeout)
        );
    }
    
	public void updateConnectedServer(List<String> serverAddress) {
        this.serverAddressList = serverAddress;
        this.rpcConnectManager.updateConnectedServer(serverAddress);
    }
    
    /**
     * <B>方法名称：</B>invokeSync<BR>
     * <B>概要说明：</B>同步调用<BR>
     * @author hezhuo.bai-JiFeng
     * @since 2019年10月17日 下午4:10:04
     * @param <T>
     * @param interfaceClass
     * @return
     */
    @SuppressWarnings("unchecked")
	public <T> T invokeSync(Class<T> interfaceClass) {
        if (syncProxyInstanceMap.containsKey(interfaceClass)) {
            return (T) syncProxyInstanceMap.get(interfaceClass);
        } else {
            Object proxy = Proxy.newProxyInstance(
                    interfaceClass.getClassLoader(),
                    new Class<?>[]{interfaceClass},
                    new RpcProxyImpl<>(rpcConnectManager, interfaceClass, timeout)
            );
            syncProxyInstanceMap.put(interfaceClass, proxy);
            return (T) proxy;
        }
    }
    
    /**
     * <B>方法名称：</B>InvokeAsync<BR>
     * <B>概要说明：</B>异步调用<BR>
     * @author hezhuo.bai-JiFeng
     * @since 2019年10月17日 下午4:12:14
     * @param <T>
     * @param interfaceClass
     * @return
     */
    @SuppressWarnings("unchecked")
	public <T> RpcAsyncProxy invokeAsync(Class<T> interfaceClass) {
        if (asyncProxyInstanceMap.containsKey(interfaceClass)) {
            return ((RpcProxyImpl<T>) asyncProxyInstanceMap.get(interfaceClass));
        } else {
        	RpcProxyImpl<T> rpcProxyImpl = new RpcProxyImpl<T>(rpcConnectManager, interfaceClass, timeout);
        	asyncProxyInstanceMap.put(interfaceClass, rpcProxyImpl);
            return rpcProxyImpl;
        }
    }
    
    public void stop() {
    	this.rpcConnectManager.stop();
    }
}
