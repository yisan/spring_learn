package com.bingo.learn.mapper;

import com.bingo.learn.domain.Account;

import java.util.List;

/**
 * Created by ing on 2021/11/30 14:48
 */
public interface AccountMapper {
    /**
     * 保存
     * @param account 金额
     */
    void save(Account account);

    /**
     * 查询所有账户信息
     * @return
     */
    List<Account> findAll();
}
