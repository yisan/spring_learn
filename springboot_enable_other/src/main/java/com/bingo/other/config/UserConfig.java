package com.bingo.other.config;

import com.bingo.other.domain.Role;
import com.bingo.other.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ing on 2021/12/15 14:45
 */
@Configuration
public class UserConfig {
    @Bean
    public User user() {
        return new User();
    }
    @Bean
    public Role role(){
        return new Role();
    }
}
