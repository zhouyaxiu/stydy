package com.shensu.service.check.util.model;

import com.shensu.mybatis.entity.check.JzjlBean;

import java.util.List;

/**
 *
 */
public class CheckPassChildInfo {
    private String name;

    private String birth;
    private List<JzjlBean> jzjlList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public List<JzjlBean> getJzjlList() {
        return jzjlList;
    }

    public void setJzjlList(List<JzjlBean> jzjlList) {
        this.jzjlList = jzjlList;
    }
}
