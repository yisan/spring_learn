package com.bingo.learn.config;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * Created by ing on 2022/1/12 15:22
 * @author ing
 */
@Data
@ConfigurationProperties(prefix = "user")
@Validated
public class UserConfig {
    @NotNull
    @Size(min = 4,max = 10,message = "名字不符合要求")
    private String uname;

    @NotNull
    @Range(min = 0,max = 150)
    private Integer age;

    @Email(message = "请输入正确的邮箱")
    private String email;
}
