package com.bingo.test;

import com.bingo.learn.domain.Order;
import com.bingo.learn.domain.User;
import com.bingo.learn.mapper.OrderMapper;
import com.bingo.learn.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * Created by ing on 2021/11/29 01:03
 */
public class MybatisTest {
    // 测试多对多
    @Test
    public void test3() throws IOException {
        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(rs);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.findUserAndRoleAll();
        for (User user : userList) {
            System.out.println(user);
        }
        sqlSession.close();
    }
    // 测试一对多
    @Test
    public void test2() throws IOException {
        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(rs);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList =   mapper.findAll();
        for (User user : userList) {
            System.out.println(user);
        }
        sqlSession.close();
    }
    //测试一对一
    @Test
    public void test1() throws IOException {
        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(rs);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        List<Order> orderList =   mapper.findAll();
        for (Order order : orderList) {
            System.out.println(order);
        }
        sqlSession.close();
    }
}
