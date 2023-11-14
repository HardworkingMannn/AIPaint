package com.example.aipaint.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Record {
    private Integer userId;
    private Integer recordId;
    private String description;
    private Boolean shared;   //是否分享
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime localDateTime;
}
