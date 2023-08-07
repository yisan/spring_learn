package com.bingo.learn.service.impl;

import com.bingo.learn.mapper.RoleDao;
import com.bingo.learn.mapper.impl.RoleDaoImpl;
import com.bingo.learn.domain.Role;
import com.bingo.learn.service.RoleService;

import java.util.List;

/**
 * Created by ing on 2021/11/23 13:48
 */
public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;
    public void setRoleDao(RoleDaoImpl roleDao) {
        this.roleDao = roleDao;
    }
    public List<Role> list() {
        return roleDao.findAll();
    }

    public void save(Role role) {
        roleDao.save(role);
    }


}
