package com.bingo.test;

import com.bingo.learn.domain.User;
import com.bingo.learn.mapper.UserMapper;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ing on 2021/11/26 16:07
 */
public class MapperTest {
    @Test
    public void test() throws IOException {
        InputStream rs = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(rs);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        User user1 = new User();
        user1.setId(8);
        user1.setName("Alice");
        user1.setAddress("shanghai");
        List<User> userList1 = mapper.findByCondition(user1);
        System.out.println(userList1);

        User user2 = new User();
        user2.setId(8);
        user2.setName("Alice");
        List<User> userList2 = mapper.findByCondition(user2);
        System.out.println(userList2);


        // 模拟ids
        List<Integer> ids= new ArrayList<Integer>();
        ids.add(1);
        ids.add(2);
        List<User> list = mapper.findByIds(ids);
        System.out.println(list);
        sqlSession.close();
    }
}
