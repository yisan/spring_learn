package com.bingo.other.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by ing on 2021/12/15 15:57
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UserConfig.class)
public @interface EnableUser {
}
