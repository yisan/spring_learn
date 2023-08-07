package com.bingo.learn.test;

import com.bingo.learn.config.SpringConfiguration;
import com.bingo.learn.domain.Account;
import com.bingo.learn.service.AccountService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 */
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = "classpath:bean.xml")
public class AccountServiceTest {

    @Test
    public void testFindAll() {
        ApplicationContext app = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        AccountService accountService = app.getBean(AccountService.class);
        //3.执行方法
        List<Account> accounts = accountService.findAllAccount();
        for(Account account : accounts){
            System.out.println(account);
        }
    }

    // @Test
    // public void testFindOne() {
    //     //3.执行方法
    //     Account account = as.findAccountById(1);
    //     System.out.println(xaccount);
    // }
    //
    @Test
    public void testSave() {
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        AccountService accountService = app.getBean(AccountService.class);
        Account account = new Account();
        account.setName("Alice");
        account.setBalance(2000f);
        //3.执行方法
        accountService.saveAccount(account);

    }
    //
    // @Test
    // public void testUpdate() {
    //     //3.执行方法
    //     Account account = as.findAccountById(4);
    //     account.setBalance(23456f);
    //     as.updateAccount(account);
    // }
    //
    // @Test
    // public void testDelete() {
    //     //3.执行方法
    //     as.deleteAccount(4);
    // }
}
