package com.bingo.learn.tx.service;

/**
 * Created by ing on 2021/11/25 11:01
 */
public interface AccountService {
    /**
     * 转账
     * @param outMan 转账人
     * @param inMan  收款人
     * @param money  转账金额
     */
    void transfer(String outMan, String inMan, double money);
}
