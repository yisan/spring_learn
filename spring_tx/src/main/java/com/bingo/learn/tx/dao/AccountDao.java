package com.bingo.learn.tx.dao;

/**
 * Created by ing on 2021/11/25 10:56
 */
public interface AccountDao {
    /**
     *
     * @param outMan
     * @param money
     */
    void out(String outMan,double money);

    /**
     *
     * @param inMan
     * @param money
     */
    void in(String inMan,double money);
}
