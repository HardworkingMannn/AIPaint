package com.example.aipaint.utils;

import com.example.aipaint.pojo.BaiduRequestConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class TransApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private String appid;
    private String securityKey;
    private String from;
    private String to;
    @Autowired
    private BaiduRequestConfig baiduRequestConfig;

    @PostConstruct
    public void init(){
        this.appid=baiduRequestConfig.getAppid();
        this.securityKey=baiduRequestConfig.getKey();
        this.from=baiduRequestConfig.getFrom();
        this.to=baiduRequestConfig.getTo();
    }

    public String getTransResult(String query) {
        Map<String, String> params = buildParams(query, from, to);
        String s = HttpGet.get(TRANS_API_HOST, params);
        return s;
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        String md = MD5.md5(src);
        params.put("sign", md);
        return params;
    }

}
