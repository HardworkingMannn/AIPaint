package com.example.aipaint.service;
import com.alibaba.fastjson.JSON;
import com.example.aipaint.entity.Record;
import com.example.aipaint.mapper.RecordLinkMapper;
import com.example.aipaint.mapper.UserMapper;
import com.example.aipaint.pojo.PyRequestConfig;
import com.example.aipaint.pojo.TextDTO;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class RemoteService {//用于远程调用py接口的service
    @Autowired
    private PyRequestConfig pyRequestConfig;
    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecordLinkMapper recordLinkMapper;
    @Autowired
    private HttpSession httpSession;
    public List<String> requestForPicWithText( TextDTO textDTO) throws Exception {//请求py后端生成ai图片，返回图片链接
        URI methodURI = pyRequestConfig.getText_request_methodURI();
        HttpPost httpPost = new HttpPost(methodURI);
        //设置参数
        HashMap<String, String> map = new HashMap<>();
        map.put("text",textDTO.getText());
        map.put("count",""+textDTO.getCount());
        String jsonString = JSON.toJSONString(map);
        StringEntity stringEntity = new StringEntity(jsonString);
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Content-Type","application/json;charset=utf-8");

       //可能访问错误导致异常
        log.info("调用py后端接口:{}",methodURI.toString());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {//如果成功
                HttpEntity entity = response.getEntity();
                String json = EntityUtils.toString(entity);
                //TODO 改成返回多个链接
                List<String> parse = (List<String>) JSON.parse(json);
                log.info("插入记录到数据库:{}"+ json);
                insertRecord(textDTO, parse);   //插入数据库
                return parse;
            } else {
                log.error("请求py后端出错，状态码为：{}", response.getStatusLine().getStatusCode());
            }
        return null;
    }

    private void insertRecord(TextDTO textDTO, List<String> parse) {
        //设置注入对象
        Record record = new Record();
        record.setUserId((Integer) httpSession.getAttribute("userId"));
        //可以把描述设置成中文，因为这个是翻译过的
        record.setDescription(textDTO.getText());
        record.setShared(textDTO.getShared());
        userMapper.insertRecord(record);
        for (String s : parse) {  //插入记录
            recordLinkMapper.insertRecordLink(record.getRecordId(),s);
        }
    }
}
