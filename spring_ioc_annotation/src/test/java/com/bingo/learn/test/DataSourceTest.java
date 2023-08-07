package com.bingo.learn.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ing on 2021/11/19 17:53
 */
public class DataSourceTest {
    @Test
    public void test(){
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

}
