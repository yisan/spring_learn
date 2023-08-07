package com.bingo.learn.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by ing on 2021/11/24 15:20
 */
public class ProxyTest {
    public static void main(String[] args) {
        //创建目标对象
        final Target target = new Target();
        // 获得增强对象
        final Enhance enhance = new Enhance();
        // 返回值就是动态生成的代理对象
        // 代理对象和目标对象之间的关联是有同样的接口，而不是能互相转换，可以认为是兄弟。
        // 所以这里用他们共同的接口来接收
        TargetInterface proxy = (TargetInterface) Proxy.newProxyInstance(
                target.getClass().getClassLoader(), // 目标对象类加载
                target.getClass().getInterfaces(), // 目标对象相同的接口字节码对象数组
                new InvocationHandler() {
                    // 调用代理对象的任何方法，实质执行的都是invoke方法
                    // method 你要执行的目标对象的方法
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        enhance.before();//前置增强

                        Object invoke = method.invoke(target, args);// 执行目标方法
                        enhance.after();//后置增强
                        return invoke;
                    }
                }
        );
        proxy.save();
    }
}
