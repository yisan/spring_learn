package com.bingo.learn.mapper;

import com.bingo.learn.domain.User;

import java.util.List;

/**
 * Created by ing on 2021/11/23 16:26
 */
public interface UserDao {

    List<User> findAll();

    Long save(User user);

    void saveUserRoleRel(Long id, Long[] roles);

    void del(Long userId);

    void delUserRoleRel(Long userId);

    User findByUsernameAndPassword(String username, String password);
}
