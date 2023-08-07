package com.bingo.learn.interceptor;

import com.bingo.learn.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by ing on 2021/11/24 10:05
 */
public class PrivilegeInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 逻辑：判断session中有没有user,进一步判断用户是否登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return false;
        }
        return true;//放行 ，访问目标资源
    }
}
