package com.bingo.learn;

import com.bingo.learn.domain.User;
import com.bingo.learn.mapper.UserMapper;
import com.bingo.learn.mapper.UserXmlMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootMybatisApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserXmlMapper userXmlMapper;
    @Test
    void testFindAll() {
        List<User> userList = userMapper.findAll();
        for (User user : userList) {
            System.out.println(user);
        }
        List<User> userList1 = userXmlMapper.findAll();
        for (User user : userList1) {
            System.out.println(user);
        }
    }
}
