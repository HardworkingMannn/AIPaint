package com.example.aipaint.controller;

import com.example.aipaint.exception.NotPositiveException;
import com.example.aipaint.exception.OverRecordSizeException;
import com.example.aipaint.pojo.RecordVO;
import com.example.aipaint.pojo.Result;
import com.example.aipaint.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/record")
@Tag(name="用于查找历史记录的接口")
@RestController
@Slf4j
public class RecordController {
    @Autowired
    private RecordService recordService;
    /**
     * 分页查询历史记录
     * @param pageNum  页数，从1开始
     * @param pageSize  页面大小
     * @return  返回一个记录数组
     */
    @GetMapping("/getRecord")
    @Operation(summary = "根据分页,获取历史记录")
    @Parameters({
            @Parameter(name="pageNum",description = "第几页，从1开始"),
            @Parameter(name="pageSize",description = "页面大小")
    })
    public Result<RecordVO> getRecord(Integer pageNum, Integer pageSize){
        try {
            log.info("分页查找记录，页数:{},页面大小:{}",pageNum,pageSize);
            return Result.success(recordService.getRecord(pageNum, pageSize));
        }catch(NotPositiveException e){
            log.warn("页数或页码不为正数");
            return Result.fail(e.getMessage()+"必须为正数");
        }catch(OverRecordSizeException e){
            log.warn("超出记录最大数");
            return Result.fail("超出记录最大数");
        }
    }
    @GetMapping("/getTotal")
    @Operation(summary="获取记录的数量")
    public Result<Integer> getTotal(){
        log.info("查找记录的接口被调用");
        return Result.success(recordService.getTotal());
    }
    @GetMapping("/getShared")
    @Operation(summary = "获取分享的记录")
    public Result<RecordVO> getShared(Integer pageNum, Integer pageSize){
        return Result.success(recordService.getShared(pageNum,pageSize));
    }
}
