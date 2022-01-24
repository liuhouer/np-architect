package com.imooc.architect;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 姚半仙
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.imooc.architect.dao")
@EnableDiscoveryClient
@EnableFeignClients
public class HousekeeperApplication {

	public static void main(String[] args) {
		SpringApplication.run(HousekeeperApplication.class, args);
	}

}
