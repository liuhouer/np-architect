package com.bfxy.scanner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvokerTable {

	private static ConcurrentHashMap<String /*module */, Map<String /*cmd*/, Invoker>> invokerTable = 
			new ConcurrentHashMap<String /*module */, Map<String /*cmd*/, Invoker>>();
	
	/**
	 * 	$addInvoker
	 * @param module
	 * @param cmd
	 * @param invoker
	 */
	public static void addInvoker(String module, String cmd, Invoker invoker) {
		Map<String /*cmd*/, Invoker> map = invokerTable.get(module);
		if(map == null) {
			map = new HashMap<String /*cmd*/, Invoker>();
			invokerTable.put(module, map);
		}
		map.put(cmd, invoker);
	}
	
	/**
	 * 	$getInvoker
	 * @param module
	 * @param cmd
	 * @return
	 */
	public static Invoker getInvoker(String module, String cmd) {
		Map<String /*cmd*/, Invoker> map = invokerTable.get(module);
		if(map != null) {
			return map.get(cmd);
		}
		return null;
	}
	
}
