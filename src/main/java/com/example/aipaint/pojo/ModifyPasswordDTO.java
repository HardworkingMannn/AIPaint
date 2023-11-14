package com.example.aipaint.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改密码，username邮箱，oldPassword旧密码，newPassword新密码")
public class ModifyPasswordDTO {  //修改密码
    private String username;
    private String oldPassword;
    private String newPassword;
}
