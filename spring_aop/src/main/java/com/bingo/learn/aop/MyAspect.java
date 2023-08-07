package com.bingo.learn.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by ing on 2021/11/24 17:34
 * 切面类
 */
public class MyAspect {
    // 前置通知
    public void before(){
        System.out.println("前置增强...");
    }
    // 后置通知
    public void afterReturning(){
        System.out.println("后置增强...");
    }
    // 环绕通知
    // Proceeding JoinPoint 正在执行的连接点 == 切点
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕前置增强...");
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("环绕后置增强...");
        return proceed;
    }
    // 异常通知
    public void afterThrowing(){
        System.out.println("异常抛出增强...");
    }
    // 最终通知
    public void after(){
        System.out.println("最终增强...");
    }
}
