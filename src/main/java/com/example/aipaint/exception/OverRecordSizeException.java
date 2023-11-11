package com.example.aipaint.exception;

public class OverRecordSizeException extends  RuntimeException{
    public OverRecordSizeException(String message) {
        super(message);
    }

    public OverRecordSizeException() {
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
