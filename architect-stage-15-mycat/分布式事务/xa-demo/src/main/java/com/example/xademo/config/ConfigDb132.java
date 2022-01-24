package com.example.xademo.config;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(value = "com.example.xademo.db132.dao",sqlSessionFactoryRef = "sqlSessionFactoryBean132")
public class ConfigDb132 {

    @Bean("db132")
    public DataSource db132(){
        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setUser("imooc");
        xaDataSource.setPassword("Imooc@123456");
        xaDataSource.setUrl("jdbc:mysql://192.168.73.132:3306/xa_132");

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(xaDataSource);


        return atomikosDataSourceBean;
    }

    @Bean("sqlSessionFactoryBean132")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("db132") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resourceResolver.getResources("mybatis/db132/*.xml"));
        return sqlSessionFactoryBean;
    }



}
