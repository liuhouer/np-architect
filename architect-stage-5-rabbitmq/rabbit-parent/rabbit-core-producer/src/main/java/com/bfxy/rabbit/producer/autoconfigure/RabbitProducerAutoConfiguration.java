package com.bfxy.rabbit.producer.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bfxy.rabbit.task.annotation.EnableElasticJob;

/**
 * 	$RabbitProducerAutoConfiguration 自动装配 
 * @author Alienware
 *
 */
@EnableElasticJob
@Configuration
@ComponentScan({"com.bfxy.rabbit.producer.*"})
public class RabbitProducerAutoConfiguration {


}
