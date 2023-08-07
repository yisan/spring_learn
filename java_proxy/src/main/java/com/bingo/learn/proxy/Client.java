package com.bingo.learn.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by ing on 2021/12/7 09:57
 * 消费者
 */
public class Client {
    public static void main(String[] args) {
        // 原先的方式，直接找生产者购买
        Seller seller = new Seller();
        Enhancements enhancements = new Enhancements();
        //现在的方式 ,通过代理商
        /*
         * 动态代理
         * 特点：字节码随用随创建，随用随加载
         * 作用：不修改源码的基础上对方法增强
         * 分类：基于接口的动态代理
         *      基于子类的动态代理
         * 基于接口的动态代理
         *      涉及的类：Proxy
         *      提供者：JDK官方
         * 如果创建代理对象
         *      使用Proxy类中的newProxyInstance方法
         * 创建代理对象的要求
         *      被代理类至少实现一个接口，没有则不能使用
         *  newProxyInstance方法的参数：
         *  Classloader: 用于加载代理对象字节码的，和被代理对象使用的类加载器相同，所以这里你代理谁就写谁的类加载器：producer.getClass().getClassLoader() -固定写法
         *  Class[]: 字节码数组
         *          用于然代理对象和被代理对象有相同的方法，怎么有相同的方法：就是两者都实现相同的接口 ，所以你代理谁就写谁实现的接口字节码数组 - 固定写法
         *  InvocationHandler:
         *         用于写增强的代码，这里实现具体的代理内容，一般都是写该接口的实现类，通常是匿名内部类，不必须
         *
         */
        ISeller iSeller = (ISeller) Proxy.newProxyInstance(seller.getClass().getClassLoader(), seller.getClass().getInterfaces(), new InvocationHandler() {
            /**
             * 作用：执行被代理对象的任何接口方法都会经过该方法
             * @param proxy 代理对象的引用
             * @param method 当前执行的方法
             * @param args 当前执行方法所需参数
             * @return 和被代理对象方法有相同的返回值
             * @throws Throwable
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //提供增强代码
                Object ret = null;
                // 1.获取方法执行的参数
                Float money = (Float) args[0];
                money = enhancements.before(money);
                /**
                 * invoke方法参数
                 * Object:指的是谁的方法，是被代理对象的方法。所以这里是被代理对象
                 * args ：方法参数
                 */
                ret = method.invoke(seller, money);
                enhancements.after();
                return ret;
            }
        });
        iSeller.sellCars(100000f);

    }
}
