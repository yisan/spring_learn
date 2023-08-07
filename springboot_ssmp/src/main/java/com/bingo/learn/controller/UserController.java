package com.bingo.learn.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.learn.domain.R;
import com.bingo.learn.domain.User;
import com.bingo.learn.mapper.UserMapper;
import com.bingo.learn.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public R getAll() {
        return new R(userService.list(),true);
    }
    @PostMapping
    public  R save(@RequestBody User user) {
        return new R(userService.save(user));
    }

    @PutMapping
    public R update(@RequestBody User user) {

        return  new R(userService.updateById(user));
    }

    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id) {
        return new R(userService.removeById(id));
    }

    @GetMapping("{id}")
    public R getById(@PathVariable Integer id) {
        return new R(userService.getById(id),true);
    }

    @GetMapping("/{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize) {
        IPage<User> page = new Page<>(currentPage, pageSize);
        return new R( userService.page(page, null),true);
    }
}
