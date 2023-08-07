package com.bingo.learn.service;

import com.bingo.learn.domain.Account;

import java.util.List;

/**
 * Created by ing on 2021/11/30 15:18
 */
public interface AccountService {
    void save(Account account);
    List<Account> findAll();
}
