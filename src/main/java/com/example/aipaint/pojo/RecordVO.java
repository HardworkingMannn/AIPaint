package com.example.aipaint.pojo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecordVO {
    private Integer userId;
    private Integer recordId;
    private String description;
    private LocalDateTime localDateTime;
    private List<String> links;
}
