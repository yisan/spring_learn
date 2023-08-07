package com.bingo.learn.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.learn.domain.Books;
import com.bingo.learn.domain.R;
import com.bingo.learn.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * Created by ing on 2022/1/7 15:45
 *
 * @author ing
 */
@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private IBookService bookService;

    @GetMapping
    public R getAll() {
        return new R(bookService.list(), true);
    }

    @PostMapping
    public R save(@RequestBody Books book) throws IOException {
        // 模拟抛出异常
        if ("123".equals(book.getName())) {
            throw new IOException();
        }
        boolean flag = bookService.save(book);
        return new R(flag, flag ? "添加成功" : "添加失败");
    }

    @PutMapping
    public R update(@RequestBody Books book) {
        return new R(bookService.updateById(book));
    }

    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id) {
        return new R(bookService.removeById(id));
    }

    @GetMapping("{id}")
    public R getById(@PathVariable Integer id) {
        return new R(bookService.getById(id), true);
    }

    // @GetMapping("/{current}/{pageSize}")
    // public R getPage(@PathVariable int current, @PathVariable int pageSize) {
    //     IPage<Books> page = new Page<>(current, pageSize);
    //     // 解决最后一页删除干净后的bug:当前页码值已经大于总页码值。
    //     page = bookService.page(page, null);
    //     if (current > page.getPages()) {
    //         page = new Page<>(page.getPages(), pageSize);
    //         page = bookService.page(page, null);
    //     }
    //     return new R(page, true);
    // }
    @GetMapping("/{current}/{pageSize}")
    public R getPage(@PathVariable int current, @PathVariable int pageSize,Books book) {
        System.out.println("springboot restart1...");
        System.out.println("springboot restart2...");
        System.out.println("springboot restart3...");


        IPage<Books> page = bookService.getPage(current,pageSize,book);
        if (current > page.getPages()) {
            page = bookService.getPage((int) page.getPages(),pageSize,book);
        }
        return new R(page, true);
    }
}
