package com.bingo.learn.proxy.cglib;

import com.bingo.learn.proxy.jdk.TargetInterface;

/**
 * Created by ing on 2021/11/24 15:15
 * 目标类
 */
public class Target implements TargetInterface {
    public void save() {
        System.out.println("save running ...");
    }
}
