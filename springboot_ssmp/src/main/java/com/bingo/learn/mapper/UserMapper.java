package com.bingo.learn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bingo.learn.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by ing on 2022/1/6 17:05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
