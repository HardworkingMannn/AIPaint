package com.example.aipaint.config;

import com.example.aipaint.pojo.PyRequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.http.HttpClient;


@Configuration
public class Config {
    @Bean
    @Scope("prototype")   //每一次请求都创建新的client对象
    public CloseableHttpClient httpClient(){
        return HttpClients.createDefault();
    }
}
