package com.bingo.learn.mapper;

import com.bingo.learn.domain.User;


/**
 * Dao接口
 */
public interface UserMapper {
     User findById(int id);
     void update(int id, String name, String password, String address);
}
