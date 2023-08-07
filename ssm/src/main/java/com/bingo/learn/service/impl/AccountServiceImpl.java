package com.bingo.learn.service.impl;

import com.bingo.learn.domain.Account;
import com.bingo.learn.mapper.AccountMapper;
import com.bingo.learn.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ing on 2021/11/30 15:20
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper mapper;
    @Override
    public void save(Account account) {
        int i = 10/0;
        mapper.save(account);
    }

    @Override
    public List<Account> findAll() {
        return mapper.findAll();
    }
}
