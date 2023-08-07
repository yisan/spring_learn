package com.bingo.learn.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by ing on 2021/12/6 11:11
 */
// @Configuration
@ComponentScan("com.bingo.learn")
@Import({JdbcConfiguration.class})
public class SpringConfiguration {

}
