package com.example.aipaint.exception;

public class NotPositiveException extends RuntimeException{
    public NotPositiveException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
