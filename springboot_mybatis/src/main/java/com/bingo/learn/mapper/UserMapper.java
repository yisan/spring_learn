package com.bingo.learn.mapper;

import com.bingo.learn.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by ing on 2021/12/14 12:27
 */
@Mapper
public interface UserMapper {
    @Select("select * from mp_user")
    List<User> findAll();
}
