package com.shensu.jsjm.model;

import java.util.List;

public class SimpleAreaBean {

    private String          code;
    private String          name;
    private List<SimpleAreaBean> children;
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SimpleAreaBean> getChildren() {
        return children;
    }

    public void setChildren(List<SimpleAreaBean> children) {
        this.children = children;
    }
}
