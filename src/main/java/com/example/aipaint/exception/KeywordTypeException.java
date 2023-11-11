package com.example.aipaint.exception;

public class KeywordTypeException extends RuntimeException{  //当关键词的类型出错的抛出异常
    public KeywordTypeException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
