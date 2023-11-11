package com.example.aipaint.controller;

import com.example.aipaint.exception.KeywordTypeException;
import com.example.aipaint.pojo.KeywordDTO;
import com.example.aipaint.pojo.Result;
import com.example.aipaint.service.RemoteService;
import com.example.aipaint.service.Services;
import io.swagger.v3.oas.annotations.Operation;
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
    public Result textToPic(String text) {//普通文本转图，返回图片链接
        log.info("调用textToPic接口:{}",text);
        String translated=null;
        try {//可能会有翻译错误
            translated = services.translate(text);
        }catch(Exception exception){
            log.warn("翻译失败");
            return Result.fail( "翻译失败");
        }
        String link=null;
        try {
            link=remoteService.requestForPicWithText(translated);
            if(!link.startsWith("http")){
                log.info("py后端返回链接有误");
                return Result.fail("py后端返回链接有误");
            }
        } catch (Exception e) {
            return Result.fail("请求py后端的ai生成接口时出错");
        }
        return Result.success(link);
    }

    @PostMapping("/keywordToPic")
    @Operation(summary = "关键词换图片")
    public Result keywordToPic(@RequestBody List<KeywordDTO> keywordDTOs){
        log.info("调用keywordToPic接口："+keywordDTOs.toString());
        String format=null;
        try {
            format = services.format(keywordDTOs);
        }catch(KeywordTypeException e){
            return Result.fail(e.getMessage());
        }  catch(NullPointerException e){
            return Result.fail(e.getMessage());
        }
        log.info("格式化关键词完成:{}",format);
        String link=null;
        try {
             link= remoteService.requestForPicWithText(format);
        } catch (Exception e) {
            return Result.fail("请求py后端出错");
        }
        return Result.success(link);
    }

}
