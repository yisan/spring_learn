package com.bingo.learn.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.learn.domain.User;
import com.bingo.learn.mapper.UserMapper;
import com.bingo.learn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ing on 2022/1/6 18:07
 */
// @Service
public class UserServiceImpl1 implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean save(User user) {
        return userMapper.insert(user) > 0;
    }

    @Override
    public Boolean update(User user) {
        return userMapper.updateById(user) > 0;
    }

    @Override
    public Boolean delete(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id) ;
    }

    @Override
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @Override
    public IPage<User> getPage(int current, int pageSize) {
        IPage<User> page = new Page<>(current,pageSize);
        userMapper.selectPage(page,null);
        return page;
    }
}
