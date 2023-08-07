package com.bingo.learn.service;

import com.bingo.learn.domain.Role;

import java.util.List;

/**
 * Created by ing on 2021/11/23 13:39
 */
public interface RoleService {
    List<Role> list();

    void save(Role role);
}
