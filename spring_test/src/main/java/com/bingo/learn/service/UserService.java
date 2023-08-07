package com.bingo.learn.service;

import com.bingo.learn.domain.User;

import java.util.List;

/**
 * Created by ing on 2021/11/23 16:25
 */
public interface UserService {
    List<User> list();

    void save(User user, Long[] roleIds);

    void del(Long userId);

    User login(String username, String password);
}
