package com.bingo.learn.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by ing on 2021/12/17 10:16
 */
@Data
public class User {
    private Long id;
    private String userName;
    private String name;
    private Integer age;
    private String email;
}
