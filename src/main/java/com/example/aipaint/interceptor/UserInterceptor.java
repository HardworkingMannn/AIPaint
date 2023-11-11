package com.example.aipaint.interceptor;

import com.example.aipaint.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //TODO 之后再实现登录功能使用，现在先搞个userId
        String token = request.getHeader("token");
         Integer userId = jwtUtil.verifyToken(token);
        if(userId!=null&&request.getSession().getAttribute("userId")==null){
            request.getSession().setAttribute("userId",userId);
            log.info("userId初始化完成");
        }
        if(userId==null){
            log.error("token错误:{}",token);
            response.getWriter().print("拒绝访问，token错误");
        }
        return userId!=null;
    }
}
