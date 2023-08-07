package com.bingo.auto;

import com.bingo.learn.config.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootTestAutoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootTestAutoApplication.class, args);
        User user = context.getBean(User.class);
        System.out.println(user);
    }

    @Bean
    public User user() {
        return new User("wangwu", 22);
    }

}
