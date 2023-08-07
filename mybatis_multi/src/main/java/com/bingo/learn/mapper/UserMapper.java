package com.bingo.learn.mapper;

import com.bingo.learn.domain.User;

import java.util.List;

/**
 * Dao接口
 */
public interface UserMapper {
    List<User> findAll();
    List<User> findUserAndRoleAll();
}
