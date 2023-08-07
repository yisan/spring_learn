package com.bingo.learn.ui;

import com.bingo.learn.factory.BeanFactory;
import com.bingo.learn.service.UserService;

/**
 * 模拟一个表现层，用来调用业务层
 */
public class Client {
    public static void main(String[] args) {
        UserService userService = (UserService) BeanFactory.getBean("userService");
        userService.save();
    }
}
