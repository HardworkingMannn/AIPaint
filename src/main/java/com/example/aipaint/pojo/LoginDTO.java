package com.example.aipaint.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "密码登录")
public class LoginDTO {
    private String username;
    private String password;
}
