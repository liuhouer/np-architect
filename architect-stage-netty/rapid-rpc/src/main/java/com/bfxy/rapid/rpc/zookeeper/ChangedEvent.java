package com.bfxy.rapid.rpc.zookeeper;

/**
 * 	ChangedEvent
 *	节点数据发送变更的事件
 */
public class ChangedEvent {

	public static enum Type {
        CHILD_ADDED,
        CHILD_UPDATED,
        CHILD_REMOVED;
    }

    private String path;		//	数据的路径 key
    private String data;		//	数据的值 value
    private Type type;			//	数据的变化类型

    public ChangedEvent(String path, String data, Type type) {
        this.path = path;
        this.data = data;
        this.type = type;
    }

    public String getData() {
        return this.data;
    }
    
    public String getPath() {
        return this.path;
    }

    public Type getType() {
        return type;
    }
    
    
    
}
