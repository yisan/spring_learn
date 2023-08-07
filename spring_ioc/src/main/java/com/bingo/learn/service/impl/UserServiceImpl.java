package com.bingo.learn.service.impl;

import com.bingo.learn.dao.UserDao;
import com.bingo.learn.dao.impl.UserDaoImpl;
import com.bingo.learn.service.UserService;

/**
 * Created by ing on 2021/12/2 15:09
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();

    public UserServiceImpl() {
        System.out.println("UserServiceImpl对象创建了...");
    }

    public void save() {
        userDao.save();
    }
}