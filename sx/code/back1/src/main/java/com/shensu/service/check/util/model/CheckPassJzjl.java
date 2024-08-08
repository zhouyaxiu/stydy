package com.shensu.service.check.util.model;

/**
 * 查验接种记录
 */
public class CheckPassJzjl {
    //疫苗编码
    private String fbactid;
    //接种日期
    private String shotdate;
    //
    private String colspan;
    //疫苗名称
    private String ymname;
    //剂次
    private String zc;

    public String getFbactid() {
        return fbactid;
    }

    public void setFbactid(String fbactid) {
        this.fbactid = fbactid;
    }

    public String getShotdate() {
        return shotdate;
    }

    public void setShotdate(String shotdate) {
        this.shotdate = shotdate;
    }

    public String getColspan() {
        return colspan;
    }

    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    public String getYmname() {
        return ymname;
    }

    public void setYmname(String ymname) {
        this.ymname = ymname;
    }

    public String getZc() {
        return zc;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }
}
