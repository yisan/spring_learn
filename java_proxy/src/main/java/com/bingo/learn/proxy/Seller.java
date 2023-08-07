package com.bingo.learn.proxy;

/**
 * Created by ing on 2021/12/7 09:53
 * 生产者
 */
public class Seller implements ISeller {
    /**
     * 卖车
     * @param money
     */
    public void sellCars(float money){
        System.out.println("卖车，拿到钱："+money);
    }
}
