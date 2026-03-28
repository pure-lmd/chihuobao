package com.example.exception;

public class ServiceException extends RuntimeException {
    private String msg;

    public ServiceException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

