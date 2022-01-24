package com.bfxy.rapid.rpc.zookeeper;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

/**
 * 	ZookeeperClient
 * 	zookeeper客户端接口
 */
public interface ZookeeperClient {

	/**
	 * 	addPersistentNode 持久化节点API
	 * @param path
	 * @param data
	 * @throws Exception
	 */
	void addPersistentNode(String path, String data) throws Exception ;
	
	/**
	 * 	addEphemeralNode 临时节点API
	 * @param path	/aaa
	 * @param data	123
	 * @return
	 * @throws Exception
	 */
	String addEphemeralNode(String path, String data) throws Exception;
	
	/**
	 * setData 更新节点数据
	 * @param path	/aaa
	 * @param data	456
	 * @return
	 * @throws Exception
	 */
	Stat setData(String path, String data) throws Exception;
	
	/**
	 * getData 通过path(key) 找到对应的value值
	 * @param path
	 * @return
	 * @throws Exception
	 */
	String getData(String path) throws Exception;
	
	/**
	 * deletePath 删除节点
	 * @param path
	 * @throws Exception
	 */
	void deletePath(String path) throws Exception;
	
	/**
	 * isConnected 是否连接成功
	 * @return
	 */
	boolean isConnected();
	
	/**
	 * getNodes 获取当前节点下的直接子节点
	 * @param path ===>	/parent
	 * @return =>	/aaa, /bbb
	 * 
	 *  	/parent   ==> 1
	 *  		/aaa	==> 2
	 *  			/ccc	==> 3
	 *  		/bbb	==> 2
	 */
	List<String> getNodes(String path);
	
	/**
	 * checkExists	判断指定节点是否存在
	 * @param path
	 * @return
	 * @throws Exception
	 */
	boolean checkExists(String path) throws Exception;
	
	/**
	 * listener4ChildrenPath 监听给定节点下的直接子节点
	 * @param parent   		/parent
	 * @param listener		/aaa, /bbb
	 * @throws Exception
	 * 
	 *  	/parent   ==> 1
	 *  		/aaa	==> 2
	 *  			/ccc	==> 3
	 *  		/bbb	==> 2
	 */
	void listener4ChildrenPath(final String parent, final NodeListener listener) throws Exception;

	void close();
	
	//	获取当前客户端实例对象
	CuratorFramework getClient();

}
