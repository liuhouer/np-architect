package com.bfxy.rapid.rpc.codec;

import java.io.Serializable;

import lombok.Data;

/**
 * $RpcResponse
 * @author 17475
 *
 */
@Data
public class RpcResponse implements Serializable {

	private static final long serialVersionUID = -7989400623370901861L;

	private String requestId;
	
	private Object result;
	
	private Throwable throwable;
	
}
