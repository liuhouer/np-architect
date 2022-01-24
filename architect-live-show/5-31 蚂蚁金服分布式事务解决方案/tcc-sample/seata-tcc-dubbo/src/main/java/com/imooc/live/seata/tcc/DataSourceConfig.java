package com.imooc.live.seata.tcc;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.inject.Qualifier;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource zooDatasource(){
        return new org.apache.commons.dbcp.BasicDataSource();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource zooDatasource)throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfigLocation(
                new PathMatchingResourcePatternResolver().getResource("classpath:sqlMapConfig.xml"));
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath*:/mapper/*.xml")
        );
        sqlSessionFactoryBean.setDataSource(zooDatasource);
        sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());

        return sqlSessionFactoryBean.getObject();
    }


}
