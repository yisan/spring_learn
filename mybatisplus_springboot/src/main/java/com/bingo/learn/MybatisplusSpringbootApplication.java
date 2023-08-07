package com.bingo.learn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.bingo.learn.mapper")
@SpringBootApplication
public class MybatisplusSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisplusSpringbootApplication.class, args);
    }

}
