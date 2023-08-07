package com.bingo.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

/**
 * @author ing
 */
@SpringBootApplication
public class SSMPApplication {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        // SpringApplication.run(SSMPApplication.class,args);
        // 在启动boot程序时去掉读取外部参数的形参，可以断开读取外部临时配置参数的入口。
        SpringApplication.run(SSMPApplication.class);
    }
}
