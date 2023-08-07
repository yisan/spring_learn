package com.bingo.learn.aop;

/**
 * Created by ing on 2021/11/24 15:15
 * 目标类
 */
public class Target implements TargetInterface {
    public void save() {
        System.out.println("save running ...");
        int  a =  1 / 0; //测试异常增强
    }
}
