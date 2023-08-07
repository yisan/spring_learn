package com.bingo.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by ing on 2021/11/23 10:38
 */
public class JdbcTemplateTest {
    @Test
    public void test2(){
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        JdbcTemplate jdbcTemplate = app.getBean(JdbcTemplate.class);
        int row = jdbcTemplate.update("insert into account(username,balance) values(?,?);", "alice", 3000);
        System.out.println(row);

    }
    @Test
    public void test1() {
        DruidDataSource dataSource = new DruidDataSource(); //设置数据库连接参数
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("Bingoing");
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        // 设置数据源
        jdbcTemplate.setDataSource(dataSource);
        // 执行操作
        int row = jdbcTemplate.update("insert into account(username,balance) values(?,?);", "tom", 3000);
        System.out.println(row);

    }
}
