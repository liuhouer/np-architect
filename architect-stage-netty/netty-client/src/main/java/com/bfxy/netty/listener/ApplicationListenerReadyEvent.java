package com.bfxy.netty.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import com.bfxy.netty.client.Client;

public class ApplicationListenerReadyEvent implements ApplicationListener<ApplicationReadyEvent>{

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.err.println("@@@@@@@@----应用服务已经启动成功----@@@@@@@@");
		Client.getInstance().init();
	}

}
