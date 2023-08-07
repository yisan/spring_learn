package com.bingo.learn;

import com.bingo.learn.domain.User;
import com.bingo.learn.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringbootMybatisPlusApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void testGetById() {
        User user = userMapper.selectById(2L);
        System.out.println(user);
    }

    @Test
    void testGetAll(){
        List<User> userList = userMapper.selectList(null);
        System.out.println(userList);
    }
}
