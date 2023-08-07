package com.bingo.learn.mapper;

import com.bingo.learn.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by ing on 2021/12/14 12:55
 */
@Mapper
public interface UserXmlMapper {
    List<User> findAll();
}
