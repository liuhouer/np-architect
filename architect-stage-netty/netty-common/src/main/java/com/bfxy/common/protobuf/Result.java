package com.bfxy.common.protobuf;

import com.google.protobuf.GeneratedMessageV3;

import lombok.Data;

/**
 * 	$Result
 * 	通用的返回结果包装类
 */
@Data
public class Result <T extends GeneratedMessageV3> {

	private MessageModule.ResultType resultType;
	
	private T content;
	
	public static <T extends GeneratedMessageV3> Result<T> SUCCESS(){
		Result<T> result = new Result<T>();
		result.resultType = MessageModule.ResultType.SUCCESS;
		return result;
	}
	
	public static <T extends GeneratedMessageV3> Result<T> SUCCESS(T content){
		Result<T> result = new Result<T>();
		result.resultType = MessageModule.ResultType.SUCCESS;
		result.content = content;
		return result;
	}
	
	public static <T extends GeneratedMessageV3> Result<T> FAILURE(){
		Result<T> result = new Result<T>();
		result.resultType = MessageModule.ResultType.FAILURE;
		return result;
	}
	
	public static <T extends GeneratedMessageV3> Result<T> FAILURE(T content){
		Result<T> result = new Result<T>();
		result.resultType = MessageModule.ResultType.FAILURE;
		result.content = content;
		return result;
	}
	
}
