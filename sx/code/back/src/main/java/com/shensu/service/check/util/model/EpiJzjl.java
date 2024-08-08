package com.shensu.service.check.util.model;


public class EpiJzjl {

    private String fchildno;
    private String fbactid;
    private String shotdate;
    private String zc;
    private String colspan;
    private String ymname;
    private String bactid;
    private String fproductno;
    private String fproductName;
    private Integer orderBy;


    public EpiJzjl(String ymname, String fbactid, String zc, String shotdate, String colspan) {
        this.ymname = ymname;
        this.fbactid = fbactid;
        this.zc = zc;
        this.shotdate = shotdate;
        this.colspan = colspan;
    }

    public EpiJzjl(String ymname, String bactid, String zc, String shotdate, String colspan, String fproductno) {
        this.ymname = ymname;
        this.bactid = bactid;
        this.zc = zc;
        this.shotdate = shotdate;
        this.colspan = colspan;
        this.fproductno=fproductno;
    }

    public String getFchildno() {
        return fchildno;
    }

    public void setFchildno(String fchildno) {
        this.fchildno = fchildno;
    }

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

    public String getZc() {
        return zc;
    }

    public void setZc(String zc) {
        this.zc = zc;
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

    public String getBactid() {
        return bactid;
    }

    public void setBactid(String bactid) {
        this.bactid = bactid;
    }

    public String getFproductno() {
        return fproductno;
    }

    public void setFproductno(String fproductno) {
        this.fproductno = fproductno;
    }

    public String getFproductName() {
        return fproductName;
    }

    public void setFproductName(String fproductName) {
        this.fproductName = fproductName;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }
}
