package com.bingo.learn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.learn.domain.User;
import com.bingo.learn.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SSMPApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    void testGetPage() {
        IPage<User> page = new Page<>(1,3);
        userMapper.selectPage(page, null);
    }

}
