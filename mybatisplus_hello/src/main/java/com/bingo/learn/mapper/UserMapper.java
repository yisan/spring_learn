package com.bingo.learn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bingo.learn.domain.User;

import java.util.List;

/**
 * Created by ing on 2021/12/10 23:45
 */
public interface UserMapper extends BaseMapper<User> {
    List<User> findAll();
}
