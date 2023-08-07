package com.bingo.learn.anno;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created by ing on 2021/11/24 17:34
 * 切面类
 */
@Component("myAspect")
@Aspect //告知spring 该类是一个切面类
public class MyAspect {
    // 配置前置通知
    @Before("MyAspect.pointcut()")
    public void before() {
        System.out.println("前置通知...");
    }

    // 后置通知
    @After("execution(* com.bingo.learn.anno.*.*(..))")
    public void after() {
        System.out.println("后置通知...");
    }

    // 正常返回通知
    @AfterReturning("execution(* com.bingo.learn.anno.*.*(..))")
    public void afterReturning() {
        System.out.println("后置返回通知...");
    }

    // 环绕通知
    // Proceeding JoinPoint 正在执行的连接点 == 切点
    @Around("execution(* com.bingo.learn.anno.*.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕前置通知...");
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("环绕后置通知...");
        return proceed;
    }

    // 异常返回通知
    @AfterThrowing("execution(* com.bingo.learn.anno.*.*(..))")
    public void afterThrowing() {
        System.out.println("异常通知...");
    }
    //定义切点表达式
    @Pointcut("execution(* com.bingo.learn.anno.*.*(..))")
    public void pointcut() {

    }

}
