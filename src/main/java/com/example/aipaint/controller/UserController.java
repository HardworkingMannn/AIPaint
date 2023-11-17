package com.example.aipaint.controller;

import com.example.aipaint.pojo.Result;
import com.example.aipaint.pojo.UserInfo;
import com.example.aipaint.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="用户相关的接口")
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    public UserService userService;
    @GetMapping("/userinfo")
    @Operation(summary = "获取用户信息，个人中心")
    public Result<UserInfo> getUserInfo(){
        return Result.success(userService.getUserInfo());
    }
}
