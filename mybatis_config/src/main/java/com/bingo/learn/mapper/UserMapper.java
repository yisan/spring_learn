package com.bingo.learn.mapper;

import com.bingo.learn.domain.User;

import java.util.List;

/**
 * Dao接口
 */
public interface UserMapper {
    void save(User user);
    User findById(int id);
    List<User> findAll();
}
