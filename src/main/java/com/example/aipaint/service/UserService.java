package com.example.aipaint.service;

import com.example.aipaint.entity.User;
import com.example.aipaint.mapper.LoginMapper;
import com.example.aipaint.pojo.UserInfo;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    public HttpSession session;
    @Autowired
    public LoginMapper loginMapper;
    public UserInfo getUserInfo(){
        Integer id = (Integer) session.getAttribute("userId");
        User info = loginMapper.getUserInfo(id);
        UserInfo userInfo = new UserInfo();
        userInfo.setName(info.getName());
        userInfo.setEmail(info.getUsername());
        return userInfo;
    }
}
