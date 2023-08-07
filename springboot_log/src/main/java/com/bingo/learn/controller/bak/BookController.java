package com.bingo.learn.controller.bak;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ing on 2022/1/11 15:56
 */
// @RestController
@RequestMapping("/books")
public class BookController extends BaseClass {

    @GetMapping
    public String getById(){
        System.out.println("springboot is running ...");
        log.debug("debug...");
        log.info("info...");
        log.warn("warn...");
        log.error("error");
        return "springboot is running...";
    }
}
