package com.bingo.learn.cglib;

import com.bingo.learn.proxy.Enhancements;
import com.bingo.learn.proxy.Seller;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by ing on 2021/12/7 09:57
 * 消费者
 */
public class Client {
    public static void main(String[] args) {
        Seller seller = new Seller();
        Enhancements enhancements = new Enhancements();
        /**
         * 动态代理 cglib
         * 特点：字节码随用随创建，随用随加载
         * 作用：不修改源码的基础上对方法增强
         * 分类：基于接口的动态代理 和 基于子类的动态代理
         * 基于子类的动态代理
         *      涉及的类：Enhancer
         *      提供者：cglib
         * 如果创建代理对象
         *      使用Enhancer类中的create方法
         * 创建代理对象的要求
         *      被代理类不能是最终类(final)，也就是必须有子类
         *  create方法的参数：
         *  Class: 字节码
         *      用于指定被代理对象的字节码 想代理谁就写谁的class
         *
         *  Callback:
         *         用于写增强的代码，这里实现具体的代理内容，一般都是写该接口的实现类，通常是匿名内部类，不必须
         *          一般写的都是该接口的子接口实现类：MethodInterceptor
         */
        Seller proxySeller = (Seller) Enhancer.create(seller.getClass(), new MethodInterceptor() {
            /**
             * 执行被代理对象的任何方法都会经过该方法
             *
             * @param  proxy 代理对象的引用
             * @param method 当前执行的方法
             * @param args 当前执行方法所需参数
             * @param methodProxy 当前执行方法的代理对象
             * @return
             * @throws Throwable
             */
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                //提供增强代码
                Object ret = null;
                // 1.获取方法执行的参数
                Float money = (Float) args[0];
                enhancements.before(money);
                /**
                 * invoke方法参数
                 * Object:指的是谁的方法，是被代理对象的方法。所以这里是被代理对象
                 * args ：方法参数
                 */
                ret = method.invoke(seller, money * 0.9f);
                enhancements.after();
                return ret;
            }
        });
        proxySeller.sellCars(100000f);
    }
}
