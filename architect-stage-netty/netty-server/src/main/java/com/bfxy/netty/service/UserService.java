package com.bfxy.netty.service;

import org.springframework.stereotype.Service;

import com.bfxy.annotation.Cmd;
import com.bfxy.annotation.Module;
import com.bfxy.common.protobuf.Result;
import com.bfxy.common.protobuf.UserModule;
import com.bfxy.common.protobuf.UserModule.User;
import com.google.protobuf.InvalidProtocolBufferException;

@Service
@Module(module = "user")
public class UserService {

	//	自动注入相关的spring bean (Service)
	
	@Cmd(cmd = "save")
	public Result<?> save(byte[] data) {
		User user = null;
		try {
			user = UserModule.User.parseFrom(data);
			System.err.println(" save ok , userId: " + user.getUserId() + " ,userName: " + user.getUserName());
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
			return Result.FAILURE();
		}
		return Result.SUCCESS(user);
	}
	
	@Cmd(cmd = "update")
	public Result<?> update(byte[] data) {
		User user = null;
		try {
			user = UserModule.User.parseFrom(data);
			System.err.println(" update ok , userId: " + user.getUserId() + " ,userName: " + user.getUserName());
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
			return Result.FAILURE();
		}
		return Result.SUCCESS(user);
	}
	
}
