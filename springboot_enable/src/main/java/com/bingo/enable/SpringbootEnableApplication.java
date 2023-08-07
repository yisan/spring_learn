package com.bingo.enable;

import com.bingo.other.config.EnableUser;
import com.bingo.other.config.UserConfig;
import com.bingo.other.domain.Role;
import com.bingo.other.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.Map;

@SpringBootApplication
// @ComponentScan("com.bingo.other")
// @Import(UserConfig.class)
// @EnableUser
// @Import(User.class)
// @Import(UserConfig.class)
// @Import(MyImportSelector.class)
@Import(MyImportBeanDefinitionRegistrar.class)
public class SpringbootEnableApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootEnableApplication.class, args);

        // Object user = context.getBean("user");
        // System.out.println(user);
        // Map<String, User> map = context.getBeansOfType(User.class);
        // System.out.println(map);

        User user = context.getBean(User.class);
        Role role = context.getBean(Role.class);
        System.out.println(user);
        System.out.println(role);
    }
}
