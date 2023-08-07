package com.bingo.learn.controller;

import com.bingo.learn.domain.Account;
import com.bingo.learn.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by ing on 2021/11/30 15:35
 */
@Controller
//设置响应数据编码  也可以在web.xml通过forceEncoding来指定
// @RequestMapping(value = "/account",produces = "text/html;charset=UTF-8")
@RequestMapping(value = "/account")
public class AccountController  {
    @Autowired
    private AccountService accountService;

    @RequestMapping("/save")
    @ResponseBody
    //表单中的字段名和实体类的属性字段一一对应，spring会帮我们自动封装
    public String save(Account account){
        accountService.save(account);
        return "保存成功";
    }
    @RequestMapping("/findAll")
    public ModelAndView findAll(){
        List<Account> accountList = accountService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("accountList",accountList);
        modelAndView.setViewName("accountList");
        return modelAndView;
    }
}
