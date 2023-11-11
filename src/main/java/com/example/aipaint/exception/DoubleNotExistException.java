package com.example.aipaint.exception;

public class DoubleNotExistException extends RuntimeException{//权重或者渐变比例的分数没有填写
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public DoubleNotExistException(String message) {
        super(message);
    }

    public DoubleNotExistException() {
    }
}
