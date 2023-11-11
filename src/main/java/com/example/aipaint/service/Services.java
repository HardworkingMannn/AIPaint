package com.example.aipaint.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.aipaint.constant.KeywordType;
import com.example.aipaint.exception.DoubleNotExistException;
import com.example.aipaint.exception.KeywordTypeException;
import com.example.aipaint.pojo.BaiduRequestConfig;
import com.example.aipaint.pojo.KeywordDTO;
import com.example.aipaint.utils.TransApi;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.compiler.ast.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class Services {
    @Autowired
    private TransApi transApi;
    public String translate(String zh)  {//调用百度翻译api翻译字符串
        //TODO 验证字符串是不是含有中文，没有就直接返回
        String json= transApi.getTransResult(zh);
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray array = jsonObject.getJSONArray("trans_result");
            String dst = (String) array.getJSONObject(0).get("dst");
            log.info("调用翻译接口：{}->{}",zh,dst);
            return dst;
        }catch (NullPointerException e){
            log.info("获取不到翻译结果:{}",json);
            throw new NullPointerException("获取不到翻译结果");
        }
    }

    /**
     *用于格式化关键词
     * @param keywordDTOs 关键词数组
     * @return 格式化关键词
     */
    public String format(List<KeywordDTO> keywordDTOs)   {//把输入的关键词格式化为给大模型的输入
        StringBuilder builder = new StringBuilder();
        for (KeywordDTO keywordDTO : keywordDTOs) {
            formatSingle(keywordDTO,builder);
        }
        return builder.substring(1);   //清除第一个多余的','
    }
    public void formatSingle(KeywordDTO keywordDTO,StringBuilder result) {  //格式化的结果都放在result中了，直接拼接
        ArrayList<String> translatedList = new ArrayList<>();
        for (String keyword : keywordDTO.getKeywords()) {//逐个翻译，放入list中
            //TODO 不能访问频率太快，所以只能一秒访问一次百度翻译API，其实可以整个文档都丢上去，但是这样太耗费字符了
            try {
                Thread.sleep(1000);
            }catch(Exception e){}
            translatedList.add(translate(keyword));
        }
        switch (keywordDTO.getType()){//根据不同的类型确立如何格式化
            case KeywordType.NONE:
                result.append(",");
                result.append(translatedList.get(0));
            case KeywordType.NORM:
                result.append(",");
                result.append("(");
                checkDouble(keywordDTO.getWeight());    //检验是否有赋值double
                result.append(translatedList.get(0)+":"+keywordDTO.getWeight());
                result.append(")");
                break;
            case KeywordType.GRADIENT:
                result.append(",");
                result.append(generateGradientString(translatedList,keywordDTO.getWeight()));
                break;
            case KeywordType.MIX:
                result.append(",");
                result.append(generateMixedString(translatedList));
                break;
            case KeywordType.TURN:
                result.append(",");
                result.append("[");
                result.append(generateMixedString(translatedList));  //都是被|分割，可以复用这个方法,但是轮流模式外面有[]裹着
                result.append("]");
                break;
            default:
                log.error("关键词异常出错:{}",keywordDTO.getType());
                throw new KeywordTypeException("关键词异常出错:"+keywordDTO.getType());   //抛出异常给controller处理
        }
    }
    public String generateGradientString(ArrayList<String> translatedList,double degree){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (String keyword : translatedList) {
            builder.append(keyword);
            builder.append(":");
        }
        builder.append(degree);
        builder.append("]");
        return builder.toString();
    }
    public String generateMixedString(ArrayList<String> translatedList){
        StringBuilder builder = new StringBuilder();
        for (String s : translatedList) {
            builder.append(s);
            builder.append("|");
        }
        return builder.substring(0,builder.length()-1);  //去除最后一位多余的|
    }
    public void checkDouble(Double d){
        if(d==null){
            throw new DoubleNotExistException();
        }
    }
}

