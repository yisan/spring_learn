package com.bingo.learn.proxy;

/**
 * Created by ing on 2021/12/7 09:56
 * 代理商的要求，也就是需要实现的接口
 */
public interface ISeller {
    /**
     * 销售
     * @param money
     */
    void sellCars(float money);

}
