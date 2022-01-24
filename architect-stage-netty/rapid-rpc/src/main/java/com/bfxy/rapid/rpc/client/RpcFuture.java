package com.bfxy.rapid.rpc.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

import com.bfxy.rapid.rpc.codec.RpcRequest;
import com.bfxy.rapid.rpc.codec.RpcResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcFuture implements Future<Object> {

	private RpcRequest request;
	
	private RpcResponse response;
	
	private long startTime;
	
	private static final long TIME_THRESHOLD = 5000;
	
	private List<RpcCallback> pendingCallbacks = new ArrayList<RpcCallback>();
	
	private Sync sync ;
	
	private ReentrantLock lock = new ReentrantLock();
	
	private ThreadPoolExecutor executor = new ThreadPoolExecutor(16, 16, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
	
	public RpcFuture(RpcRequest request) {
		this.request = request;
		this.startTime = System.currentTimeMillis();
		this.sync = new Sync();
	}

	/**
	 * 	$done 实际的回调处理
	 * @param response
	 */
	public void done(RpcResponse response) {
		this.response = response;
		boolean success = sync.release(1);
		if(success) {
			invokeCallbacks();
		}
		// 整体rpc调用的耗时
		long costTime = System.currentTimeMillis() - startTime;
		if(TIME_THRESHOLD < costTime) {
			log.warn("the rpc response time is too slow, request id = " + this.request.getRequestId() + " cost time: " + costTime);
		}
	}
	
	/**
	 * 	依次执行回调函数处理
	 */
	private void invokeCallbacks() {
		lock.lock();
		try {
			for(final RpcCallback callback : pendingCallbacks) {
				runCallback(callback);
			}			
		} finally {
			lock.unlock();
		}
	}

	private void runCallback(RpcCallback callback) {
		final RpcResponse response = this.response;
		executor.submit(new Runnable() {
			@Override
			public void run() {
				if(response.getThrowable() == null) {
					callback.success(response.getResult());
				} else {
					callback.failure(response.getThrowable());
				}
			}
		});
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCancelled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDone() {
		return sync.isDone();
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		sync.acquire(-1);
		if(this.response != null) {
			return this.response.getResult();
		} else {
			return null;
		}
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
		if(success) {
			if(this.response != null) {
				return this.response.getResult();
			} else {
				return null;
			}
		} else {
			throw new RuntimeException("timeout excetion requestId: " 
					+ this.request.getRequestId() 
					+ ",className: " + this.request.getClassName()
					+ ",methodName: " + this.request.getMethodName());
		}
	}
	
	class Sync extends AbstractQueuedSynchronizer {

		private static final long serialVersionUID = -3989844522545731058L;
		
		private final int done = 1;
		
		private final int pending = 0;
		
		protected boolean tryAcquire(int acquires) {
			return getState() == done ? true : false;
		}
		
		protected boolean tryRelease(int releases) {
			if(getState() == pending) {
				if(compareAndSetState(pending, done)) {
					return true;
				}
			}
			return false;
		}
		
		public boolean isDone() {
			return getState() == done;
		}
	}
	
	/**
	 * 	可以在应用执行的过程中添加回调处理函数
	 * @param callback
	 * @return
	 */
	public RpcFuture addCallback(RpcCallback callback) {
		lock.lock();
		try {
			if(isDone()) {
				runCallback(callback);
			} else {
				this.pendingCallbacks.add(callback);
			}
		} finally {
			lock.unlock();
		}
		return this;
	}
	
}
