package com.bingo.learn.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * Created by ing on 2021/12/14 17:49
 */
public class ClassCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // try {
        //     Class<?> cls = Class.forName("com.alibaba.druid.pool.DruidDataSource");
        // } catch (ClassNotFoundException e) {
        //     e.printStackTrace();
        //     return false;
        // }
        Map<String, Object> map = metadata.getAnnotationAttributes(ConditionOnClass.class.getName());
        String[] value = (String[]) map.get("value");

        try {
            for (String s : value) {
                Class<?> cls = Class.forName(s);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
