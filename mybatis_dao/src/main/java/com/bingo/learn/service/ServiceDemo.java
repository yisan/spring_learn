package com.bingo.learn.service;


import com.bingo.learn.mapper.UserMapper;
import com.bingo.learn.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;


/**
 * Dao调用
 */
public class ServiceDemo {
    public static void main(String[] args) throws IOException {
        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(rs);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.findById(8);
        mapper.update(8,"Alice","123456","shanghai");
        System.out.println(user);
        sqlSession.close();
    }
}
