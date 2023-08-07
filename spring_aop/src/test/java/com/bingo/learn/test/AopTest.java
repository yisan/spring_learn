package com.bingo.learn.test;

import com.bingo.learn.aop.Target;
import com.bingo.learn.aop.TargetInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ing on 2021/11/24 17:47
 */
@RunWith(SpringJUnit4ClassRunner.class) // 指定测试引擎
@ContextConfiguration("classpath:applicationContext.xml") //指定配置文件
public class AopTest {
    @Autowired //测试谁就注入谁
    private TargetInterface target;
    @Test
    public void test1(){
        target.save();
    }
}
