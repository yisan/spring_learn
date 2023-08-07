package com.bingo.learn.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;

/**
 * Created by ing on 2021/12/6 13:11
 * 和Spring连接数据库相关的配置类
 */
// @Configuration
@PropertySource("classpath:jdbc.properties")
public class JdbcConfiguration {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    @Bean(name = "dataSource")
    public DataSource createDataSource() {
        DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName(driver);
        dds.setUsername(username);
        dds.setPassword(password);
        dds.setUrl(url);
        return dds;
    }

    @Bean(name = "runner")
    @Scope("prototype")
    public QueryRunner createQueryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }
}
