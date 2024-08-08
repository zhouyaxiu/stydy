package com.shensu.service.check.util.model;

/**
 * 金苗宝查验用Dto
 */
public class CheckPassSearchDto {
    //儿童预防接种档案编号
    private String childCode;

    private CheckPassChildInfo childinfo;

    public String getChildCode() {
        return childCode;
    }

    public void setChildCode(String childCode) {
        this.childCode = childCode;
    }

    public CheckPassChildInfo getChildinfo() {
        return childinfo;
    }

    public void setChildinfo(CheckPassChildInfo childinfo) {
        this.childinfo = childinfo;
    }
}
