package com.example.aipaint.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "关键词集合keywords，count生成张数，shared是否分享")
public class Keyword {
    private List<KeywordDTO> keywords;
    private Integer count;
    private Boolean shared;
}
