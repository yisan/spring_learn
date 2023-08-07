package com.bingo.learn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bingo.learn.domain.Books;
import org.apache.ibatis.annotations.Mapper;


/**
 * Created by ing on 2022/1/7 15:42
 * @author ing
 */
@Mapper
public interface BookMapper extends BaseMapper<Books> {
}
