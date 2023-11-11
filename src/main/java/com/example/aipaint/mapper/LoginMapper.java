package com.example.aipaint.mapper;

import com.example.aipaint.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginMapper {//用于注册登录之类的功能
    void register(String username,String password);
    User userExist(String username);
    User checkPassword(String username,String password);  //检验密码是否正确
    void setPassword(String username,String newPassword);
    String findPassword(String username);

}
