package com.bingo.learn;

import com.bingo.learn.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by ing on 2021/11/25 17:22
 */
public class MyBatisTest {
    @Test
    public void testSelectOne() throws IOException {
        User user = new User();
        user.setName("Tom");
        user.setPassword("123000");
        user.setAddress("beijing");
        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(rs);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        User ret = sqlSession.selectOne("userMapper.findById", 9);
        System.out.println(ret);
        // mybatis执行更新操作，需提交事务
        // sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void testInsert() throws IOException {
        // 模拟User对象
        User user = new User();
        user.setName("Tom");
        user.setPassword("123000");
        user.setAddress("beijing");
        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(rs);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        sqlSession.insert("userMapper.save", user);
        // mybatis执行更新操作，需提交事务
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void testUpdate() throws IOException {
        // 模拟User对象
        User user = new User();
        user.setId(1);
        user.setName("刘诺英");
        user.setPassword("123000");
        user.setAddress("beijing");

        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(rs);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        sqlSession.update("userMapper.update",user);
        // mybatis执行更新操作，需提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testDelete() throws IOException {
        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(rs);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int ret = sqlSession.delete("userMapper.delete", 5);
        System.out.println(ret);
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void testQuery() throws IOException {
        // 加载配置文件
        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        // 获取session工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(rs);
        // 获取session会话对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 执行操作 参数：UserMapper中的namespace+id
        List<User> userList = sqlSession.selectList("userMapper.findAll");
        System.out.println(userList);
        // 释放资源
        sqlSession.close();

    }
}
