package com.example.aipaint.pojo;

import lombok.Data;

import java.util.List;

@Data
public class KeywordDTO {
    private String type;
    private List<String> keywords;  //可能有多个关键词，比如混合类型
    private Double weight;   //普通类型下可以代表权重，在渐变类型下可以代表比例
}
