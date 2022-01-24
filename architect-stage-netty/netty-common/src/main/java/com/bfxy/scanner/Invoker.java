package com.bfxy.scanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.Data;

@Data
public class Invoker {

	private Method method;
	
	private Object target;
	
	/**
	 * 	$createInvoker
	 * 	创建invoker对象
	 * @param method
	 * @param target
	 * @return
	 */
	public static Invoker createInvoker(Method method, Object target) {
		Invoker invoker = new Invoker();
		invoker.setMethod(method);
		invoker.setTarget(target);
		return invoker;
	}
	
	/**
	 * 	$invoke 反射调用
	 * @param params
	 * @return 返回方法的执行结果
	 */
	public Object invoke(Object... params) {
		try {
			return method.invoke(target, params);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
