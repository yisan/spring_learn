package com.bingo.learn.tx.dao.impl;

import com.bingo.learn.tx.dao.AccountDao;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by ing on 2021/11/25 10:57
 */
public class AccountDaoImpl implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void out(String outMan, double money) {
        jdbcTemplate.update("update account set balance = balance - ? where username = ?;", money, outMan);
    }


    public void in(String inMan, double money) {
        jdbcTemplate.update("update account set balance = balance + ? where username = ?;",money,inMan);
    }
}
