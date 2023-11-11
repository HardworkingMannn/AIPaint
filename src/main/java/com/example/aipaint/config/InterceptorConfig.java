package com.example.aipaint.config;

import com.example.aipaint.interceptor.UserInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptorConfig implements ApplicationContextAware,WebMvcConfigurer {
    private ApplicationContext applicationContext;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        UserInterceptor bean = applicationContext.getBean(UserInterceptor.class);
        registry.addInterceptor(bean).addPathPatterns("/**").excludePathPatterns("/text/test","/login/**")
                .excludePathPatterns("/doc.html","/v3/api-docs","/v3/api-docs/swagger-config","/v3/api-docs/**","/swagger-ui/index.index");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {//注入容器，通过容器查找到拦截器对象
        this.applicationContext=applicationContext;
    }
}
