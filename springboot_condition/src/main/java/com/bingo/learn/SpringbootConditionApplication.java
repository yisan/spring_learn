package com.bingo.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringbootConditionApplication {

    public static void main(String[] args) {
        // 启动SpringBoot 的应用，返回Spring 的IOC容器上下文
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootConditionApplication.class, args);
        Object user = context.getBean("user");
        System.out.println("user: "+user);
    }

}
