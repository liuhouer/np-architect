package com.bfxy.esjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.bfxy.rabbit.task.annotation.EnableElasticJob;

@EnableElasticJob
@SpringBootApplication
@ComponentScan(basePackages = {"com.bfxy.esjob.*","com.bfxy.esjob.service.*", "com.bfxy.esjob.annotation.*","com.bfxy.esjob.task.*"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
