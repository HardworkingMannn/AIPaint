package com.example.aipaint.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "想要生成的ai图的文本（text），想要生成的张数count，是否分享shared")
public class TextDTO {//用于接收textToPic接口的数据
    private String text;
    private Integer count;
    private Boolean shared;
}
