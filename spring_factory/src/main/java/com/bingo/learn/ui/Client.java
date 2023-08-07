package com.bingo.learn.ui;

import com.bingo.learn.service.UserService;
import com.bingo.learn.service.impl.UserServiceImpl;

/**
 * 模拟一个表现层，用来调用业务层
 */
public class Client {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();
        userService.save();
    }
}
