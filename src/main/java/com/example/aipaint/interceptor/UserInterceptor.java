package com.example.aipaint.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //TODO 之后再实现登录功能使用，现在先搞个userId
        if(request.getSession().getAttribute("userId")!=null) {
            request.getSession().setAttribute("userId", 1);
            log.info("userId初始化完成");
        }
        return true;
    }
}
