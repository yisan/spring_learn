package com.bingo.learn.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ing on 2021/12/16 11:35
 */
@Configuration
@EnableConfigurationProperties(UserProperties.class)
public class UserAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = "user")
    public User user(UserProperties userProperties){
        System.out.println("UserAutoConfiguration -- user");
        String name = userProperties.getUserName()!=null? userProperties.getUserName():"法外狂徒";
        int age = userProperties.getAge()!=null?userProperties.getAge():30;
        return new User(name,age);
    }
}
