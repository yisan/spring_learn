package com.bingo.test;

import com.bingo.learn.domain.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by ing on 2021/11/23 12:32
 */
@RunWith(SpringJUnit4ClassRunner.class) //交给spring去帮我们测试
@ContextConfiguration("classpath:applicationContext.xml") //配置文件
public class JdbcTemplateCRUDTest {
    @Autowired //测试什么，就注入什么
    private JdbcTemplate jdbcTemplate;
    @Test
    public void testQueryCount(){
        Long count = jdbcTemplate.queryForObject("select count(*) from account", Long.class);
        System.out.println(count);
    }
    @Test
    public void testQuery(){
        Account account = jdbcTemplate.queryForObject("select * from account where id = ?;", new BeanPropertyRowMapper<Account>(Account.class), 4);
        System.out.println(account);
    }
    @Test
    public void testQueryAll(){
        List<Account> accountList = jdbcTemplate.query("select * from account;", new BeanPropertyRowMapper<Account>(Account.class));
        System.out.println(accountList);
    }
    @Test
    public void testUpdate(){
        int row = jdbcTemplate.update("update account set balance = ? where username = ?;", 40000, "alice");
        System.out.println(row);
    }
    @Test
    public void testDelete(){
        int result = jdbcTemplate.update("delete from account where username =?;", "tom");
        System.out.println(result);


    }
}
