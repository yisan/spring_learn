package com.bingo.learn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bingo.learn.domain.User;
import org.springframework.stereotype.Repository;

/**
 * Created by ing on 2021/12/11 23:58
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    void findAll();
}
