package com.imooc.live.seata.tcc;

import com.imooc.live.seata.tcc.service.TccConsumerService;
import com.imooc.live.seata.tcc.starter.TccServiceConsumer;
import lombok.Cleanup;
import org.apache.curator.test.TestingServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ImportResource("classpath:provider/*.xml")
@MapperScan("com.imooc.live.seata.tcc.dao")
public class TccServiceProvider {

    // 启动dubbo服务
    public static void main(String[] args) throws Exception {
        // TestingServer是假的zk数据源，不用单独设置一套zk环境，方便同学们本地启动
        // 放在test目录结构里启动
        new TestingServer(2181, true).start();
        ApplicationContext context = SpringApplication.run(TccServiceProvider.class, args);

        // 在H2 DB里创建表，可以改为自动创建，懒得写entity了
        DataSource dataSource = context.getBean(DataSource.class);
        System.out.println("Prepareing data");
        prepareTable(dataSource);
    }

    protected static void prepareTable(DataSource dataSource){
        try {
            @Cleanup Connection conn = null;
            conn = dataSource.getConnection();
            Statement s = conn.createStatement();
            s.execute("drop table zoo if exists ");
            s.execute("CREATE TABLE  zoo  ( animal_type varchar(10) NOT NULL, available int(11),locked int(11),frozen int(11) ) ");
            System.out.println("Table Zoo created");
            String sql = "insert into zoo(animal_type, available, locked, frozen) values(?, ?, ?, ?)";
            @Cleanup PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "1");
            ps.setInt(2, 10);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.executeUpdate();

            s.execute("drop table fridge if exists ");
            s.execute("CREATE TABLE  fridge  ( id bigint(11) NOT NULL, available int(11),reserved int(11),occupied int(11) ) ");
            System.out.println("Table Fridge created");
            sql = "insert into fridge(id, available, reserved, occupied) values(?, ?, ?, ?)";
            @Cleanup PreparedStatement ps2 = conn.prepareStatement(sql);
            ps2.setLong(1, 1L);
            ps2.setInt(2, 10);
            ps2.setInt(3, 0);
            ps2.setInt(4, 0);
            ps2.executeUpdate();

        } catch (Exception e) {
            System.out.println("EEEEEEEEEEERROR " + e);
        }
    }
}
