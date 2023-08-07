package com.bingo.learn.tx.service.impl;

import com.bingo.learn.tx.dao.AccountDao;
import com.bingo.learn.tx.service.AccountService;

/**
 * Created by ing on 2021/11/25 11:01
 */
public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void transfer(String outMan, String inMan, double money) {
        accountDao.out(outMan, money);
        int i = 1/0;
        accountDao.in(inMan, money);
    }
}
