package com.example.aipaint.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="baidu")
@Component
@Data
public class BaiduRequestConfig {//配置类，接收百度翻译api的相关配置参数
    private String from;
    private String to;
    private String appid;
    private String key;
}
