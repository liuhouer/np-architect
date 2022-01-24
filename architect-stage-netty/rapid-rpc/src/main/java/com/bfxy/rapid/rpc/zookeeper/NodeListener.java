package com.bfxy.rapid.rpc.zookeeper;

/**
 *	NodeListener
 *	节点变更的监听器	
 */
public interface NodeListener {
    void nodeChanged(ZookeeperClient client, ChangedEvent event) throws Exception;
}
