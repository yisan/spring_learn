package com.bingo.learn.test;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.bingo.learn.domain.User;
import com.bingo.learn.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by ing on 2021/12/11 00:47
 */
public class MybatisPlusTest {
    @Test
    public void testFindAll() throws IOException {
        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new MybatisSqlSessionFactoryBuilder().build(rs);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        // List<User> userList = mapper.findAll();
        List<User> userList = mapper.selectList(null);
        for (User user : userList) {
            System.out.println(user);
        }
    }

}
