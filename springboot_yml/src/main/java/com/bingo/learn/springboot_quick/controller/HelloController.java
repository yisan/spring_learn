package com.bingo.learn.springboot_quick.controller;

import com.bingo.learn.springboot_quick.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ing on 2021/12/9 10:19
 */
@RestController
public class HelloController {
    @Value("${name}")
    private String name;
    @Value("${person.name}")
    private String personName;
    @Value("${person.age}")
    private int personAge;
    @Value("${address[0]}")
    private String address1;
    @Value("${msg1}")
    private String msg1;
    @Value("${msg2}")
    private String msg2;
    // 第二种方式
    @Autowired
    private Environment env;//在spring容器启动时就会初始化该对象实例，所以直接注入即可
    @Autowired
    private Person person;
    @RequestMapping("/hello")
    public String hello() {
        System.out.println(name);
        System.out.println(personName);
        System.out.println(personAge);
        System.out.println(address1);
        System.out.println(msg1);
        System.out.println(msg2);
        // 第二种方式
        System.out.println("------------------------");
        System.out.println(env.getProperty("person.name"));
        System.out.println(env.getProperty("person.age"));
        System.out.println(env.getProperty("address[0]"));
        //第三种方式
        System.out.println("------------------------");
        System.out.println(person.getName());
        System.out.println(person.getAge());
        String[] address = person.getAddress();
        for (String s : address) {
            System.out.println(s);
        }
        return "Hello SpringBoot!!!";
    }
}
