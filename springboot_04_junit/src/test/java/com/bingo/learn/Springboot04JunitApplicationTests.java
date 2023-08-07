package com.bingo.learn;

import com.bingo.learn.dao.BookDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Springboot04JunitApplicationTests {
    @Autowired
    private BookDao bookDao;
    @Test
    void contextLoads() {
        bookDao.save();
    }

}
