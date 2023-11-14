package com.example.aipaint.controller;

import com.example.aipaint.exception.DoubleNotExistException;
import com.example.aipaint.exception.DoubleNotInRangeException;
import com.example.aipaint.exception.KeywordTypeException;
import com.example.aipaint.pojo.Keyword;
import com.example.aipaint.pojo.KeywordDTO;
import com.example.aipaint.pojo.Result;
import com.example.aipaint.pojo.TextDTO;
import com.example.aipaint.service.RemoteService;
import com.example.aipaint.service.Services;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "文字转图片")
@RequestMapping("/text")
public class TextController {
    @Autowired
    private Services services;
    @Autowired
    private RemoteService remoteService;

    @Operation(summary = "测试接口")
    @GetMapping("/test")
    public String test(){
        log.info("测试成功");
        return "测试成功";
    }
    @GetMapping("/textToPic")
    @Operation(summary = "普通文本转图")
    public Result<List<String>> textToPic(TextDTO textDTO) {//普通文本转图，返回图片链接
        String text=textDTO.getText();
        log.info("调用textToPic接口:{}",text);
        String translated=null;
        try {//可能会有翻译错误
            translated = services.translate(text);
            textDTO.setText(translated);
        }catch(Exception exception){
            log.warn("翻译失败");
            return Result.fail( "翻译失败");
        }
        List<String> links=null;
        try {
            links=remoteService.requestForPicWithText(textDTO);
            for (String link : links) {
                if(!link.startsWith("http")){
                    log.info("py后端返回链接有误");
                    return Result.fail("py后端返回链接有误");
                }
            }
        } catch (Exception e) {
            return Result.fail("请求py后端的ai生成接口时出错");
        }
        return Result.success(links);
    }

    @PostMapping("/keywordToPic")
    @Operation(summary = "关键词换图片")
    @Parameters({
            @Parameter(name="type",description = "类型，分为none（无权重值）,norm(普通类型，带权重值)，mix（混合类型），gradient（渐变类型），turn（交换类型），各个类型的含义参考群里的文档"),
            @Parameter(name="keywords",description = "可能会有多个关键词，也可能只有一个，比如none和norm，只会有一个，gradient会有两个，mix和turn可以有多个"),
            @Parameter(name="weight",description = "在norm类型下代表权重，gradient类型下代表比例（0-1），前多少比例是什么，后多少比例转换成什么")
    })
    public Result<List<String>> keywordToPic(@RequestBody Keyword keyword){
        List<KeywordDTO> keywordDTOs = keyword.getKeywords();
        log.info("调用keywordToPic接口："+keywordDTOs.toString());
        String format=null;
        try {
            format = services.format(keywordDTOs);
        }catch(KeywordTypeException e){
            return Result.fail(e.getMessage());
        }  catch(NullPointerException e){
            return Result.fail(e.getMessage());
        }catch(DoubleNotExistException e){
            return Result.fail("数值必须存在");
        }catch(DoubleNotInRangeException e){
            return Result.fail("数值必须在0-1之间");
        }
        log.info("格式化关键词完成:{}",format);
        List<String> links;

        TextDTO textDTO = new TextDTO();
        textDTO.setCount(keyword.getCount());
        textDTO.setShared(keyword.getShared());
        textDTO.setText(format);
        try {
             links= remoteService.requestForPicWithText(textDTO);
        } catch (Exception e) {
            return Result.fail("请求py后端出错");
        }
        return Result.success(links);
    }

}
