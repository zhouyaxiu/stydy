package com.shensu.service.check.util.model;

import java.util.List;

/**
 * 金苗宝查验返回对象
 */
public class CheckPassVO {

    private String name;

    private Integer code;

    private String  isPassCheck;

    private List<CheckPassJzjl> jzjlList;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsPassCheck() {
        return isPassCheck;
    }

    public void setIsPassCheck(String isPassCheck) {
        this.isPassCheck = isPassCheck;
    }

    public List<CheckPassJzjl> getJzjlList() {
        return jzjlList;
    }

    public void setJzjlList(List<CheckPassJzjl> jzjlList) {
        this.jzjlList = jzjlList;
    }
}
