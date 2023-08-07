package com.bingo.learn.springboot_profile;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by ing on 2021/12/16 18:25
 */
@Component
@ConfigurationProperties("person")
public class DataSource {
    private String driverClassName;
    private String url;
    private String userName;
    private String password;
}
