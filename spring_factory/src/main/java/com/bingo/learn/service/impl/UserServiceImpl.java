package com.bingo.learn.service.impl;

import com.bingo.learn.dao.UserDao;
import com.bingo.learn.dao.impl.UserDaoImpl;
import com.bingo.learn.service.UserService;

/**
 * 模拟业务层实现类
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();

    public void save() {
        userDao.save();
    }
}
