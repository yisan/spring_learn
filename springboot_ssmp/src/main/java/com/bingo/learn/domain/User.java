package com.bingo.learn.domain;

import lombok.Data;

/**
 * Created by ing on 2022/1/6 16:47
 */
@Data
public class User {
    private Long id;
    private String userName;
    private String name;
    private Integer age;
    private String password;
    private String email;
}
