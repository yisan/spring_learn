package com.bingo.learn.service.impl;

import com.bingo.learn.mapper.RoleDao;
import com.bingo.learn.mapper.UserDao;
import com.bingo.learn.domain.Role;
import com.bingo.learn.domain.User;
import com.bingo.learn.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

/**
 * Created by ing on 2021/11/23 16:25
 */
public class UserServiceImpl implements UserService {
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    private UserDao userDao;
    private RoleDao roleDao;

    public List<User> list() {
        List<User> userList = userDao.findAll();
        // 封装userList中的每一个User的role数据
        for (User user : userList) {
            // 获得user的id
            List<Role> roles = roleDao.findRoleByUserId(user.getId());
            user.setRoles(roles);
        }
        return userList;
    }

    public void save(User user, Long[] roleIds) {
        // 向sys_user表存储数据

        Long userId = userDao.save(user);
        // 向sys_user_role关系表中存储多条数据
        userDao.saveUserRoleRel(userId,roleIds);
    }

    public void del(Long userId) {
        // 1.删除sys_user_role关系表
        userDao.delUserRoleRel(userId);
        // 2.删除sys_user表
        userDao.del(userId);
    }

    public User login(String username, String password) {
        try {
           User user = userDao.findByUsernameAndPassword(username, password);
            return user;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
