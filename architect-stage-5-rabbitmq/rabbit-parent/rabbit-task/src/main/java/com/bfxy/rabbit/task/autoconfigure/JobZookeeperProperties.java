package com.bfxy.rabbit.task.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "elastic.job.zk")
@Data
public class JobZookeeperProperties {

	private String namespace;
	
	private String serverLists;
	
	private int maxRetries = 3;

	private int connectionTimeoutMilliseconds = 15000;
	
	private int sessionTimeoutMilliseconds = 60000;
	
	private int baseSleepTimeMilliseconds = 1000;
	
	private int maxSleepTimeMilliseconds = 3000;
	
	private String digest = "";
	
}
