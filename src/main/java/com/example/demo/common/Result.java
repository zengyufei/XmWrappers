package com.example.demo.common;

public class Result<T> {
    private Integer code;
    private String msg;
    private String message;
    private T data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result<T> ok(T t) {
        this.setCode(1000);
        this.setMsg("成功");
        this.setMessage("成功");
        this.setData(t);
        return this;
    }

    public Result<T> fail(String msg) {
        this.setCode(1001);
        this.setMsg(msg);
        this.setMessage(msg);
        return this;
    }


    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.setMessage(msg);
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.setMessage(msg);
    }

}
