package com.example.aipaint.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@ConfigurationProperties(prefix = "py")
@Component
@Data
@Slf4j
public class PyRequestConfig {//封装和请求py后端接口相关的参数
    private String host;
    private String port;
    private String text_request_method;

    public URI getText_request_methodURI() throws URISyntaxException {
        String uri=null;
        try {
             uri = combineURI(this.text_request_method);
            return new URI(uri);
        }catch(Exception e){
            log.error("uri转换错误:{}",uri);
            throw new RuntimeException("uri转换错误");
        }
    }
    public String combineURI(String method){
        return "http://"+host+":"+port+"/"+text_request_method;
    }
}
