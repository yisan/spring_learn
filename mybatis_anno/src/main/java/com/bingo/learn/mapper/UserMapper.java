package com.bingo.learn.mapper;

import com.bingo.learn.domain.Order;
import com.bingo.learn.domain.User;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by ing on 2021/11/29 17:19
 */
public interface UserMapper {
    @Insert("insert into user values(#{id},#{name},#{password},#{address},#{birthday})")
    void save(User user);
    @Update("update user set name=#{name},password=#{password},address=#{address} where id=#{id}\n")
    void update(User user);
    @Delete("delete from user where id = #{id}\n")
    void delete(int id);
    @Select("select * from user where id = #{id}")
    User findById(int id);
    @Select("select * from user")
    List<User> findAll();
    //多对多查询
    @Select("select * from user")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "address",property = "address"),
            @Result(column = "birthday",property = "birthday"),
            //
            @Result(property = "orderList",javaType = List.class,column = "id",many = @Many(select = "com.bingo.learn.mapper.OrderMapper.findByUid"))
    })
    List<User> findUserAndOrdersAll();
    @Select("select * from user")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "password",property = "password"),
            @Result(column = "address",property = "address"),
            @Result(column = "birthday",property = "birthday"),
            @Result(property = "roleList",column = "id",javaType = List.class,many = @Many(select = "com.bingo.learn.mapper.RoleMapper.findByUid"))
    })
    List<User> findUserAndRoleAll();
}
