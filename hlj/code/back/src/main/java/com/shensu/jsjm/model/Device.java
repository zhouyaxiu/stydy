package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;

public class Device {

    @ApiModelProperty("地区行政编码")
    private String GBCODE;
    @ApiModelProperty("地区行政名称")
    private String GBNAME;
    @ApiModelProperty("设备类型")
    private String DEVICETYPE;
    @ApiModelProperty("设备数量")
    private String DEVICENUM;

    public String getGBCODE() {
        return GBCODE;
    }

    public void setGBCODE(String GBCODE) {
        this.GBCODE = GBCODE;
    }

    public String getGBNAME() {
        return GBNAME;
    }

    public void setGBNAME(String GBNAME) {
        this.GBNAME = GBNAME;
    }

    public String getDEVICETYPE() {
        return DEVICETYPE;
    }

    public void setDEVICETYPE(String DEVICETYPE) {
        this.DEVICETYPE = DEVICETYPE;
    }

    public String getDEVICENUM() {
        return DEVICENUM;
    }

    public void setDEVICENUM(String DEVICENUM) {
        this.DEVICENUM = DEVICENUM;
    }
}
