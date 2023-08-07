package com.bingo.learn.controller;

import com.bingo.learn.domain.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by ing on 2022/1/10 10:40
 *
 * @author ing
 * 作为SpringMVC的异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {
    /**
     * 拦截所有的Exception类型的异常信息
     */
    @ExceptionHandler
    public R doException(Exception e) {
        e.printStackTrace();
        // 记录日志
        // 通知运维
        // 通知开发
        return new R("服务器故障,请稍后再试！");
    }
}
