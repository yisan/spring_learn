package com.bingo.test;

import com.bingo.learn.domain.User;
import com.bingo.learn.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by ing on 2021/12/12 00:17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( "classpath:applicationContext.xml")
public class MpStringTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void testSelectList(){
        List<User> userList = userMapper.selectList(null);
        for (User user : userList) {
            System.out.println(user);
        }
    }
}
