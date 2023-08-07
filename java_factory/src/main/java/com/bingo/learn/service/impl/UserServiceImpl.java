package com.bingo.learn.service.impl;

import com.bingo.learn.dao.UserDao;
import com.bingo.learn.factory.BeanFactory;
import com.bingo.learn.service.UserService;

/**
 * Created by ing on 2021/12/2 15:09
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
    public void save() {
        userDao.save();
    }
}