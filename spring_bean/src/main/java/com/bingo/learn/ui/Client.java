package com.bingo.learn.ui;

import com.bingo.learn.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 模拟一个表现层，用来调用业务层
 */
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) app.getBean("userService");
        userService.save();
        app.close();
    }
}
