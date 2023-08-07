package com.bingo.learn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by ing on 2021/11/24 00:46
 */
// @RequestMapping("/target")
@Controller
public class TargetController {
    @RequestMapping("/show")
    public ModelAndView show(){
        System.out.println("目标资源执行...");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","李银河");
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
