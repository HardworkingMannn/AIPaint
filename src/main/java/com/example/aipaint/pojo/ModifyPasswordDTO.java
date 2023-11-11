package com.example.aipaint.pojo;

import lombok.Data;

@Data
public class ModifyPasswordDTO {  //修改密码
    private String username;
    private String oldPassword;
    private String newPassword;
}
