package com.bingo.learn.proxy;


/**
 * 增强功能
 */
public class Enhancements {
    public float before(float money){
        System.out.println("找客户,过户手续等");
        System.out.println("收取中介费："+money*0.1f);
    return money*0.9f;
    }
    public void after(){
        System.out.println("售后服务等");
    }
}