package com.bingo.learn.factory;

import com.bingo.learn.service.UserService;
import com.bingo.learn.service.impl.UserServiceImpl;

/**
 * Created by ing on 2021/12/2 23:03
 */
public class StaticFactory {
    public static UserService getUserService(){
        return new UserServiceImpl();
    }
}
