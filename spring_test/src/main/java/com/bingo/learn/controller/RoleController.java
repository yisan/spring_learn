package com.bingo.learn.controller;

import com.bingo.learn.domain.Role;
import com.bingo.learn.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by ing on 2021/11/23 13:33
 */
@RequestMapping(value = "/role")
@Controller //利用注解产生bean，就需要springmvc里配置
public class RoleController {
    @Autowired
    private  RoleService roleService;

    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView();
        List<Role> roleList = roleService.list();
        System.out.println(roleList);
        modelAndView.addObject("roleList", roleList);
        modelAndView.setViewName("role-list");
        return modelAndView;
    }
    @RequestMapping("/save")
    public String save(Role role){
        roleService.save(role);
        return  "redirect:/role/list";
    }
}
