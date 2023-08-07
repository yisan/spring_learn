package com.bingo.learn.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ing on 2021/11/24 01:04
 */
public class MyInterceptor implements HandlerInterceptor {
    // 在目标方法执行前执行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle...");
        String allow = request.getParameter("allow");
        if ("yes".equals(allow)){
            return  true;
        }else{
            request.getRequestDispatcher("/error.jsp").forward(request,response);
            return false;
        }
        // return false; // 不放行
        // return true; //放行
    }

    // 在目标方法执行之后，视图对象返回之前执行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle...");
        modelAndView.addObject("name","李时珍");

    }

    // 在流程都执行完毕后执行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion...");
    }
}
