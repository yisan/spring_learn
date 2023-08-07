package com.bingo.learn.service.impl;

import com.bingo.learn.dao.UserDao;
import com.bingo.learn.service.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;


/**
 * Created by ing on 2021/12/3 10:20
 */
@Component("userService")
@Scope
public class UserServiceImpl implements UserService {

    @Resource(name = "userDao1")
    private UserDao userDao2;

    @PostConstruct
    public void init() {
        System.out.println("UserServiceImpl 初始化方法执行");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("UserServiceImpl 销毁方法执行");
    }

    @Override
    public void save() {
        userDao2.save();
    }
}
