package com.bfxy.netty.transfer;

import java.util.List;

import com.bfxy.netty.serial.BaseSerializer;

public class User extends BaseSerializer {

	private String userId;
	private String userName;
	private int age;
	private List<String> favorite;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String userId, String userName, int age, List<String> favorite) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.age = age;
		this.favorite = favorite;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<String> getFavorite() {
		return favorite;
	}
	public void setFavorite(List<String> favorite) {
		this.favorite = favorite;
	}
	
	@Override
	protected void read() {
		this.userId = readString();
		this.userName = readString();
		this.age = readInt();
		this.favorite = readList(String.class);
	}
	@Override
	protected void write() {
		//userId
		writeString(this.userId);
		//userName
		writeString(this.userName);
		//age
		writeInt(this.age);
		//favorite
		writeList(this.favorite);
		
	}
	
}
