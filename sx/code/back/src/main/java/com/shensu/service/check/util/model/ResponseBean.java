package com.shensu.service.check.util.model;

import java.io.Serializable;

public class ResponseBean implements Serializable {
    public static final int SUCCESS_CODE = 0;
    public static final String SUCCESS_MSG = "请求成功";
    public static final String SYS_ERR = "系统异常";
    public static final int FAIL_CODE = 1;
    private int code;
    private String message;
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void success(Object data) {
        this.setCode(SUCCESS_CODE);
        this.setMessage(SUCCESS_MSG);
        this.setData(data);
    }

    public void success() {
        this.setCode(SUCCESS_CODE);
        this.setMessage(SUCCESS_MSG);
    }

    public void successMsg(String msg) {
        this.setCode(SUCCESS_CODE);
        this.setMessage(msg);
    }

    public void fail(String msg) {
        this.setCode(FAIL_CODE);
        this.setData("");
        this.setMessage(msg);
    }

    public void failMsg(String msg) {
        this.setCode(FAIL_CODE);
        this.setData("");
        this.setMessage(msg);
    }

    public void failError() {
        this.setCode(FAIL_CODE);
        this.setData(null);
        this.setMessage(SYS_ERR);
    }

    public void fail(Object data, String msg) {
        this.setCode(FAIL_CODE);
        this.setData(data);
        this.setMessage(msg);
    }

    public ResponseBean() {

    }

    public ResponseBean(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        if (this.code == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static ResponseBean failObject(String message){
        return new ResponseBean(FAIL_CODE,message,null);
    }

    public static ResponseBean successObject(String message,Object data){
        return new ResponseBean(SUCCESS_CODE,message,data);
    }
}
