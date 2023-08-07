package com.bingo.learn.service.impl;

import com.bingo.learn.dao.UserDao;
import com.bingo.learn.service.UserService;

/**
 * Created by ing on 2021/12/8 09:56
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void save() {
        userDao.save();
    }
}
