package com.bingo.learn.service.impl;

import com.bingo.learn.service.UserService;

/**
 * Created by ing on 2021/12/2 15:09
 */
public class UserServiceImpl implements UserService {
    public UserServiceImpl() {
        System.out.println("UserServiceImpl 无参构造函数...");
    }

    public void save() {
        System.out.println("UserServiceImpl 的 save方法执行...");
    }
    public void init(){
        System.out.println("对象初始化...");
    }
    public void destory(){
        System.out.println("对象销毁...");
    }
}