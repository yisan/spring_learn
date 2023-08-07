package com.bingo.learn.proxy.cglib;

/**
 * Created by ing on 2021/11/24 15:18
 * 增强类
 */
public class Enhance {
    public void before(){
        System.out.println("前置增强");
    }
    public void after(){
        System.out.println("后置增强");
    }
}
