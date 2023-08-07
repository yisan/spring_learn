package com.bingo.learn.controller.bak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ing on 2022/1/11 18:02
 */
public class BaseClass {
    private Class clazz;
    public static Logger log;
    public BaseClass(){
        clazz = this.getClass();
        log = LoggerFactory.getLogger(clazz);
    }
}
