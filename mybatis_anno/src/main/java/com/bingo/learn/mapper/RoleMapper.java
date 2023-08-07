package com.bingo.learn.mapper;

import com.bingo.learn.domain.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by ing on 2021/11/30 11:28
 */
public interface RoleMapper {
    @Select("SELECT * from sys_role sr LEFT JOIN sys_user_role ur ON sr.id = ur.roleId WHERE ur.userId = #{uid} ")
    List<Role> findByUid(String uid);
}
