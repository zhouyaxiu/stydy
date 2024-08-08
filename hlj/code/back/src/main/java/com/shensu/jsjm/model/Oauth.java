package com.shensu.jsjm.model;

public class Oauth {

    private String code;
    private String pwd;
    private String smKey;
    private String askTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSmKey() {
        return smKey;
    }

    public void setSmKey(String smKey) {
        this.smKey = smKey;
    }

    public String getAskTime() {
        return askTime;
    }

    public void setAskTime(String askTime) {
        this.askTime = askTime;
    }
}
