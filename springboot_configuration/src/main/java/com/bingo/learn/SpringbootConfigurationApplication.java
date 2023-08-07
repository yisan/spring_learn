package com.bingo.learn;

import com.alibaba.druid.pool.DruidDataSource;
import com.bingo.learn.config.ServerConfig;
import com.bingo.learn.config.UserConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author ing
 */
@SpringBootApplication
@EnableConfigurationProperties(value = {ServerConfig.class, UserConfig.class})
public class SpringbootConfigurationApplication {
    /**
     * spring boot2.x中对配置文件中的命名规范进行了强制约束，需要kebab格式，不能使用驼峰格式了
     */
    @Bean
    @ConfigurationProperties(prefix = "data-source")
    public DruidDataSource dataSource(){
        return new DruidDataSource();
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(SpringbootConfigurationApplication.class, args);
        ServerConfig serverConfig = ctx.getBean(ServerConfig.class);
        System.out.println(serverConfig.getIp());
        System.out.println(serverConfig.getPort());
        UserConfig user = ctx.getBean(UserConfig.class);
        System.out.println(user.getUname());
        System.out.println(user.getEmail());
        DruidDataSource dataSource = ctx.getBean(DruidDataSource.class);
        System.out.println(dataSource.getDriverClassName());
    }

}
