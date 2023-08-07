package com.bingo.learn.ui;

import com.bingo.learn.dao.UserDao;
import com.bingo.learn.dao.impl.UserDaoImpl;
import com.bingo.learn.service.UserService;
import com.bingo.learn.service.impl.UserServiceImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.Resources;

/**
 * 模拟一个表现层，用来调用业务层
 */
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 2.根据id来获取对象
        // ① 一种是通过字节码进行强转得到UserDao的对象
        UserDao userDao = app.getBean("userDao", UserDao.class);
        // ② 一种是获取Object，进行强转得到UserService对象
        UserService userService = (UserService) app.getBean("userService");
        System.out.println("userService: " + userService);
        System.out.println("userDao: " + userDao);

        Resource resource = new ClassPathResource("applicationContext.xml");
        BeanFactory factory = new XmlBeanFactory(resource);
        UserService userService1 = (UserService) factory.getBean("userService");
        System.out.println(userService1);

    }
}
