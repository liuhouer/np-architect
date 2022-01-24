package com.imooc;

import com.imooc.jvm.objectpool.datasource.DMDataSource;
import com.imooc.jvm.objectpool.datasource.DataSourceEnpoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@EnableAsync
@SpringBootApplication
// 扫描 mybatis 通用 mapper 所在的包
@MapperScan(basePackages = "com.imooc.mapper")
// 扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.imooc", "org.n3r.idworker"})
//@EnableTransactionManagement
@EnableScheduling       // 开启定时任务
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    ServletWebServerFactory servletWebServerFactory() {
//        return new TomcatServletWebServerFactory();
//    }

//    @Bean
//    @Primary
//    public DataSource dataSource(){
//        return new DMDataSource();
//    }
//
//    @Bean
//    public DataSourceEnpoint dataSourceEnpoint() {
//        DataSource dataSource = this.dataSource();
//        return new DataSourceEnpoint((DMDataSource) dataSource);
//    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate(){
        return new AsyncRestTemplate();
    }

    @Bean
    public WebClient webClient(){
        return WebClient.create();
    }
}