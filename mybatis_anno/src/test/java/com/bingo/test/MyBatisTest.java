package com.bingo.test;

import com.bingo.learn.domain.Order;
import com.bingo.learn.domain.User;
import com.bingo.learn.mapper.OrderMapper;
import com.bingo.learn.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by ing on 2021/11/29 17:22
 */
public class MyBatisTest {
    private UserMapper mapper;
    private OrderMapper orderMapper;
    private SqlSession sqlSession;

    // 初始化方法，对于每一个要测试的方法都执行一次
    @Before
    public void before() throws IOException {
        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(rs);
        sqlSession = sqlSessionFactory.openSession(true);
        mapper = sqlSession.getMapper(UserMapper.class);
        orderMapper = sqlSession.getMapper(OrderMapper.class);
    }

    @After
    public void after() {
        sqlSession.close();
    }
    @Test
    public void testFindUserAndRoleAll(){
        List<User> userList = mapper.findUserAndRoleAll();
        for (User user : userList) {
            System.out.println(user);
        }
    }
    @Test
    public void testFindAllOrders(){
        List<Order> orderList =  orderMapper.findAll();
        for (Order order : orderList) {
            System.out.println(order);
        }
    }
    @Test
    public void testFindUserAndOrdersAll(){
        List<User> userList = mapper.findUserAndOrdersAll();
        for (User user : userList) {
            System.out.println(user);
        }
    }
    @Test
    public void testSave() {
        User user = new User();
        user.setName("韩信");
        user.setPassword("123456");
        user.setAddress("海南");
        mapper.save(user);

    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(11);
        user.setName("韩信");
        user.setPassword("123abc");
        user.setAddress("河南");
        mapper.update(user);
    }

    @Test
    public void testDelete() {
        mapper.delete(11);
    }

    @Test
    public void testFindById() {
        User user = mapper.findById(1);
        System.out.println(user);
    }

    @Test
    public void testFindAll() {
        List<User> userList = mapper.findAll();
        System.out.println(userList);
    }
}
