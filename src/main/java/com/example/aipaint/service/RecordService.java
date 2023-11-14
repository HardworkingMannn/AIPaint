package com.example.aipaint.service;

import com.example.aipaint.entity.Record;
import com.example.aipaint.exception.NotPositiveException;
import com.example.aipaint.exception.OverRecordSizeException;
import com.example.aipaint.mapper.RecordLinkMapper;
import com.example.aipaint.mapper.UserMapper;
import com.example.aipaint.pojo.RecordVO;
import com.example.aipaint.pojo.Result;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RecordService {//处理文字转图片历史记录
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpSession httpSession;  //session，用于记录UserID
    @Autowired
    private RecordLinkMapper recordLinkMapper;
    public List<RecordVO> getRecord(Integer pageNum, Integer pageSize){//分页获取记录
        pageNum = handlePage(pageNum, pageSize);
        List<Record> records = userMapper.getRecordByPage((Integer) httpSession.getAttribute("userId"), pageNum, pageSize);
        List<RecordVO> recordVOS=new ArrayList<>();
        fillRecordVOs(records, recordVOS);
        return recordVOS;
    }

    private void fillRecordVOs(List<Record> records, List<RecordVO> recordVOS) {//填充VO数组
        for (Record record : records) {
            RecordVO recordVO = new RecordVO();
            recordVO.setLocalDateTime(record.getLocalDateTime());
            recordVO.setDescription(record.getDescription());
            recordVO.setLinks(recordLinkMapper.getLinks(record.getRecordId()));
            recordVO.setUserId(record.getUserId());
            recordVO.setRecordId(record.getRecordId());
            recordVOS.add(recordVO);
        }
    }

    private Integer handlePage(Integer pageNum, Integer pageSize) {//处理页面，检验是否有错误，并且把页数改成limit的开始位置
        checkPositive(pageNum,"页数");
        checkPositive(pageSize,"页大小");
        pageNum =(pageNum -1)* pageSize;
        checkOverMax(pageNum);   //检验最大页数是否超出
        return pageNum;
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
    public List<RecordVO> getShared(Integer pageNum,Integer pageSize){
        pageNum=handlePage(pageNum,pageSize);
        List<Record> records = userMapper.getSharedRecord(pageNum, pageSize);
        log.info("分页查询record表，获取最新数据记录:起始位置：{}，页大小:{}",pageNum,pageSize);
        List<RecordVO> recordVOS=new ArrayList<>();
        fillRecordVOs(records, recordVOS);
        log.info("查询到的最新结果：{}",recordVOS);
        return recordVOS;
    }
}
