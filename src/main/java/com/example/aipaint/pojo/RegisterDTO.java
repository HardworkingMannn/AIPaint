package com.example.aipaint.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "注册接口使用的dto，username用户名邮箱，password密码，code验证码")
public class RegisterDTO {
    private String username;
    private String name;
    private String password;
    private Integer code;
}
