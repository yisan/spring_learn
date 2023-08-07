package com.bingo.learn.resolver;

import com.bingo.learn.exception.MyException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * 自定义异常处理器
 */
public class MyExceptionResolver implements HandlerExceptionResolver {
    /**
     * @param request
     * @param response
     * @param handler
     * @param ex       异常对象
     * @return ModelView 跳转到错误视图
     */
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        if (ex instanceof MyException) {
            modelAndView.addObject("info", "自定义异常111");
        }else if (ex instanceof  ClassCastException){
            modelAndView.addObject("info","类转换异常");
        }else if (ex instanceof ParseException){
            modelAndView.addObject("info","解析异常");
        }
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
