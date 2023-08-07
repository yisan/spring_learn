package com.bingo.learn.controller;

import com.bingo.learn.controller.bak.BaseClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ing on 2022/1/11 15:56
 */
@Slf4j
@RestController
@RequestMapping("/books")
public class BookController  {

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
