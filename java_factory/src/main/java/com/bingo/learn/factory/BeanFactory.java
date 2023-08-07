package com.bingo.learn.factory;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 创建Bean对象的工厂
 * Bean :在计算机英语中，可重用组建的含义
 * JavaBean:用java编写的可重用组件
 * 可以理解为就是创建service和dao对象的。
 */
public class BeanFactory{
    // 定义一个Properties对象
    private static Properties props;
    // 定义一个Map,存放要创建的对象，也就是容器的概念
    private static Map<String ,Object> beans;
    // 使用静态代码块为Properties对象赋值
    static {
        try {
            props = new Properties();
            // 利用反射拿到类加载器，返回文件的输入流对象
            InputStream rs = BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties");
            props.load(rs);
            // 实例化容器
            beans = new HashMap<>();
            // 取出配置文件中所有的key
            Enumeration keys = props.keys();
            // 遍历
            while (keys.hasMoreElements()){
                // 取出每个key
                String key = keys.nextElement().toString();
                // 根据key获取value
                String beanPath = props.getProperty(key);
                // 反射创建对象
                Object value = Class.forName(beanPath).newInstance();
                // 将key和value存入容器
                beans.put(key,value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据beanName获取bean对象
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return  beans.get(beanName);
    }
}

