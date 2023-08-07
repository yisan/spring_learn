package com.bingo.learn.config;

import com.bingo.learn.condition.ClassCondition;
import com.bingo.learn.condition.ConditionOnClass;
import com.bingo.learn.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ing on 2021/12/14 17:10
 */
@Configuration
public class UserConfig {
    @Bean
    // @Conditional(ClassCondition.class)
    @ConditionOnClass({"com.alibaba.druid.pool.DruidDataSource","redis.clients.jedis.Jedis"})
    public User user(){
        return new User("法外狂徒",20);
    }
}
