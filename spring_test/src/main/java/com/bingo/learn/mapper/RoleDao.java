package com.bingo.learn.mapper;

import com.bingo.learn.domain.Role;

import java.util.List;

/**
 * Created by ing on 2021/11/23 13:50
 */
public interface RoleDao {
    List<Role> findAll();

    void save(Role role);

    List<Role> findRoleByUserId(Long id);
}
