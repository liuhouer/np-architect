package com.bfxy.netty.protobuf;

import java.util.Arrays;

import com.bfxy.netty.protobuf.UserModule.User;
import com.bfxy.netty.protobuf.UserModule.User.Builder;
import com.google.protobuf.InvalidProtocolBufferException;

public class TestProtobuf {

	/**
	 * serialObject2Bytes 序列化
	 * @return
	 */
	public static byte[] serialObject2Bytes() {
		Builder userBuilder = UserModule.User.newBuilder();
		
		userBuilder
		.setUserId("1001")
		.setAge(30)
		.setUserName("张三")
		.addFavorite("足球")
		.addFavorite("撸码");
		
		User user = userBuilder.build();
		/**
		 *  序列化机制：
		 *  1. java序列化 比如一个int类型(4个字节长度)
		 *  // int a = 2   &  int a = 110000000
		 *  java的序列化无论真是的int类型数值大小实际占用多少个字节，在内存中都是以4个长度(32位)
		 *  
		 *  protobuf序列化机制：
		 *  是按照实际的数据大小去动态伸缩的, 因此很多时候我们的int数据并没有实际占用到4个字节
		 *  所以protobuf序列化后一般都会比int类型(java序列化机制)的占用长度要小很多！
		 */
		byte[] data = user.toByteArray();
		//[10, 4, 49, 48, 48, 49, 16, 30, 26, 6, -27, -68, -96, -28, -72, -119, 34, 6, -24, -74, -77, -25, -112, -125, 34, 6, -26, -110, -72, -25, -96, -127]

		System.err.println(Arrays.toString(data));
		
		return data;
	}
	
	/**
	 * $serialBytes2Object 反序列化
	 * @param data
	 * @return
	 */
	public static User serialBytes2Object(byte[] data) {
		try {
			return UserModule.User.parseFrom(data);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		byte[] data = serialObject2Bytes();
		User user = serialBytes2Object(data);
		
		System.err.println("userId: " + user.getUserId());
		System.err.println("age: " + user.getAge());
		System.err.println("userName: " + user.getUserName());
		System.err.println("favorite: " + user.getFavoriteList());
	}
	
	
}
