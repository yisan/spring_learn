package com.bingo.learn.dao.impl;

import com.bingo.learn.dao.UserDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ing on 2021/12/3 16:24
 */
@Repository("userDao1")
public class UserDaoImpl1 implements UserDao {
    @Override
    public void save() {
        System.out.println("【UserDaoImpl-1】 保存用户成功");
    }
}
