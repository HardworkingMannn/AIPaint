package com.example.aipaint.service;

import com.example.aipaint.entity.Record;
import com.example.aipaint.exception.NotPositiveException;
import com.example.aipaint.exception.OverRecordSizeException;
import com.example.aipaint.mapper.UserMapper;
import com.example.aipaint.pojo.Result;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RecordService {//处理文字转图片历史记录
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpSession httpSession;  //session，用于记录UserID
    public List<Record> getRecord(Integer pageNum, Integer pageSize){//分页获取记录
        checkPositive(pageNum,"页数");
        checkPositive(pageSize,"页大小");
        pageNum=(pageNum-1)*pageSize;
        checkOverMax(pageNum);   //检验最大页数是否超出
        return userMapper.getRecordByPage((Integer) httpSession.getAttribute("userId"), pageNum, pageSize);
    }
    public void checkPositive(int num,String type){//检验页数和页大小为正数
        if(num<=0){
            log.error("{}不能为负数或0",type);
            throw new NotPositiveException(type);
        }
    }
    public void checkOverMax(Integer pageNum){//验证页数是否超出最大值
        Integer total = getTotal();
        if(pageNum>=total){
            throw new OverRecordSizeException();
        }
    }
    public Integer getTotal(){
        return userMapper.getTotal((Integer) httpSession.getAttribute("userId"));
    }
}
