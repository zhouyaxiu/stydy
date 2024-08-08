package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;

public class ScanVaccinationRate {

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @ApiModelProperty(value = "扫描率")
    private String rate;

    private int scanCount;
    private int szCount;

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public int getSzCount() {
        return szCount;
    }

    public void setSzCount(int szCount) {
        this.szCount = szCount;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
