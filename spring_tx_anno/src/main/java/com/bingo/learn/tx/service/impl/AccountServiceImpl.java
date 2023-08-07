package com.bingo.learn.tx.service.impl;


import com.bingo.learn.tx.dao.AccountDao;
import com.bingo.learn.tx.service.AccountService;
import org.aspectj.lang.annotation.AdviceName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ing on 2021/11/25 11:01
 */
@Service("accountService")
@Transactional(isolation = Isolation.REPEATABLE_READ) // 这里加上表示该类下的方法都采用这一事务控制属性 和方法上单独定义的属性 优先级采用就近原则 方法自己的优先级高
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    // public void setAccountDao(AccountDao accountDao) {
    //     this.accountDao = accountDao;
    // }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED) //事务增强
    public void transfer(String outMan, String inMan, double money) {
        accountDao.out(outMan, money);
        int i = 1/0;
        accountDao.in(inMan, money);
    }
}
