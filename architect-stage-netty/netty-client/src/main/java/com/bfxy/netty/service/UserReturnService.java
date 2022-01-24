package com.bfxy.netty.service;

import org.springframework.stereotype.Service;

import com.bfxy.annotation.Cmd;
import com.bfxy.annotation.Module;
import com.bfxy.common.protobuf.MessageModule.ResultType;

@Service
@Module(module = "user-return")
public class UserReturnService {
	
	@Cmd(cmd = "save-return")
	public void saveReturn(ResultType resultType, byte[] data) {
		if(ResultType.SUCCESS.equals(resultType)) {
			System.err.println("处理 user save 方法成功!");
		} else {
			System.err.println("处理 user save 方法失败!");
		}
	}
	
	@Cmd(cmd = "update-return")
	public void updateReturn(ResultType resultType, byte[] data) {
		if(ResultType.SUCCESS.equals(resultType)) {
			System.err.println("处理 user update 方法成功!");
		} else {
			System.err.println("处理 user update 方法失败!");
		}
	}
}
