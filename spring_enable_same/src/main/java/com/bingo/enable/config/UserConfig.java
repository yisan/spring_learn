package com.bingo.enable.config;

import com.bingo.enable.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ing on 2021/12/15 15:20
 */
@Configuration
public class UserConfig {
    @Bean
    public User user(){
        return new User();
    }
}
