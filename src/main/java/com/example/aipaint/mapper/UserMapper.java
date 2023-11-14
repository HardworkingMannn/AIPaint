package com.example.aipaint.mapper;

import com.example.aipaint.entity.Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {//写入记录的mapper
    //TODO 转换成xml形式的，避免sql注入攻击
    public Integer insertRecord(Record record);
    @Select("select * from record where user_id=#{userId} limit #{pageNum},#{pageSize}")
    public List<Record> getRecordByPage(Integer userId,Integer pageNum,Integer pageSize);
    @Select("select count(*) from record where user_id=#{userId}")
    public Integer getTotal(Integer userId);
    List<Record> getSharedRecord(Integer pageNum,Integer pageSize);
}
