package com.bingo.learn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by ing on 2021/12/8 15:34
 */
@Controller
public class UserController {
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String save() {
        System.out.println("user saved...");
        return "success";
    }

    @RequestMapping("/test")
    public ModelAndView test() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("success");
        return modelAndView;
    }
}
