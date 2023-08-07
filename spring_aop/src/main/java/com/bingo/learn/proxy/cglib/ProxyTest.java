package com.bingo.learn.proxy.cglib;


import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by ing on 2021/11/24 15:20
 */
public class ProxyTest {
    public static void main(String[] args) {
        final Target target = new Target(); // 目标类对象
        final Enhance enhance = new Enhance(); // 增强类对象
        // 基于cglib 动态代理
        // 1.创建增强器
        Enhancer enhancer = new Enhancer();
        // 2.设置父类
        enhancer.setSuperclass(Target.class);
        // 3.设置回调
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                enhance.before();//执行前置增强方法
                Object invoke = method.invoke(target, args); //执行目标方法
                enhance.after();//执行后置增强方法
                return invoke;
            }
        });
        // 4.生成代理对象
        Target proxy = (Target) enhancer.create();
        proxy.save();
    }
}
