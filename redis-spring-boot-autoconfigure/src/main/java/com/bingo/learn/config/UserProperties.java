package com.bingo.learn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by ing on 2021/12/16 12:27
 */
@ConfigurationProperties(prefix = "user")
public class UserProperties {
    private String userName;
    private Integer age;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
