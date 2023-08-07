package com.bingo.learn.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * Created by ing on 2021/12/15 10:30
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ClassCondition.class)
public @interface ConditionOnClass {
    String[] value();
}
