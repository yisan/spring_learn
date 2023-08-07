package com.bingo.learn.dao.impl;

import com.bingo.learn.dao.UserDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ing on 2021/12/3 12:30
 */
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    //模拟持久层操作
    @Override
    public void save() {
        System.out.println("【UserDaoImpl】 保存用户成功");
    }
}
