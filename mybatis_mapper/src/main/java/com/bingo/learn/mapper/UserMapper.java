package com.bingo.learn.mapper;

import com.bingo.learn.domain.User;

import java.util.List;

/**
 * Created by ing on 2021/11/26 16:01
 */
public interface UserMapper {
    List<User> findByCondition(User user);
    List<User> findByIds(List<Integer> ids);
}
