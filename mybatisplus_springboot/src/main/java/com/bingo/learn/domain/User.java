package com.bingo.learn.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ing on 2021/12/11 23:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("mp_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userName;
    private String name;
    private Integer age;
    private String email;
    private String password;
}

