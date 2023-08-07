package com.bingo.learn.factory;

import com.bingo.learn.service.UserService;
import com.bingo.learn.service.impl.UserServiceImpl;

/**
 * Created by ing on 2021/12/2 22:45
 */
public class BeanFactory {
    // 提供UserService对象
    public UserService getUserService() {
        return new UserServiceImpl();
    }
}
