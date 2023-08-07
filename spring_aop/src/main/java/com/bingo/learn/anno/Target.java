package com.bingo.learn.anno;

import org.springframework.stereotype.Component;

/**
 * Created by ing on 2021/11/24 15:15
 * 目标类
 */
@Component("target") //交给spring容器
public class Target implements TargetInterface {
    public void save() {
        System.out.println("save running ...");
    }
}
