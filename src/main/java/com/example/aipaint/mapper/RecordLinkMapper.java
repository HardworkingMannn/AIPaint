package com.example.aipaint.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecordLinkMapper {
    @Insert("insert into record_link(record_id,link) values(#{recordId},#{link})")
    public void insertRecordLink(Integer recordId,String link);
    @Select("select link from record_link where record_id=#{recordId}")
    List<String> getLinks(Integer recordId);
}
