package com.bfxy.rapid.rpc.config.consumer;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bfxy.rapid.rpc.client.RpcClient;
import com.bfxy.rapid.rpc.config.RpcConfigAbstract;

/**
 * 	$ConsumerConfig
 * @author hezhuo.bai-JiFeng
 * @since 2019年10月17日 下午2:57:15
 */
public class ConsumerConfig<T> extends RpcConfigAbstract {

    /**
     * 	直连调用地址
     */
    protected volatile List<String> url;

    /**
     * 	连接超时时间
     */
    protected int connectTimeout = 3000;

    /**
     * 	代理实例对象
     */
    private volatile transient T proxyInstance;
    
    private RpcClient client ;

    @SuppressWarnings("unchecked")
	public void initRpcClient() {
        this.client = new RpcClient();
        this.proxyInstance = (T) this.client.initClient(url, connectTimeout, getProxyClass());
    }

    protected Class<?> getProxyClass() {
        if (proxyClass != null) {
            return proxyClass;
        }
        try {
            if (StringUtils.isNotBlank(interfaceClass)) {
                this.proxyClass = Class.forName(interfaceClass);
            } else {
                throw new Exception("consumer.interfaceId, null, interfaceId must be not null");
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return proxyClass;
    }

    public List<String> getUrl() {
		return url;
	}

	public void setUrl(List<String> url) {
		this.url = url;
	}

	public int getTimeout() {
        return connectTimeout;
    }
    
    public void setTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

	public T getProxyInstance() {
		return proxyInstance;
	}


	public RpcClient getClient() {
		return client;
	}


	public void setClient(RpcClient client) {
		this.client = client;
	}

}
