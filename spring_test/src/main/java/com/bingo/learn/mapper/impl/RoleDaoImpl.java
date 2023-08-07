package com.bingo.learn.mapper.impl;

import com.bingo.learn.mapper.RoleDao;
import com.bingo.learn.domain.Role;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by ing on 2021/11/23 13:50
 */
public class RoleDaoImpl implements RoleDao {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Role> findAll() {
        return jdbcTemplate.query("select * from sys_role", new BeanPropertyRowMapper<Role>(Role.class));
    }

    public void save(Role role) {
        jdbcTemplate.update("insert into sys_role values(?,?,?);", null, role.getRoleName(), role.getRoleDesc());
    }

    public List<Role> findRoleByUserId(Long id) {
        List<Role> roles = jdbcTemplate.query("select * from sys_user_role ur , sys_role r where ur.roleId = r.id and ur.userId =?;", new BeanPropertyRowMapper<Role>(Role.class), id);
        return  roles;
    }
}

