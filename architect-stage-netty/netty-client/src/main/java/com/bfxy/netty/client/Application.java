package com.bfxy.netty.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bfxy.netty.listener.ApplicationListenerReadyEvent;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		
        SpringApplication application = new SpringApplication(Application.class);
        application.addListeners(new ApplicationListenerReadyEvent());
        application.run(args);
        
	}

}

