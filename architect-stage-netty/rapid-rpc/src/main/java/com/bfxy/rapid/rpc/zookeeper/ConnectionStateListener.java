package com.bfxy.rapid.rpc.zookeeper;

/**
 *	ConnectionStateListener
 *	连接状态的事件监听器
 */
public interface ConnectionStateListener {
    void stateChanged(ZookeeperClient client, ConnectionState state);
}

