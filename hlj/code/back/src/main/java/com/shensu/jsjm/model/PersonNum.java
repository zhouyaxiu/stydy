package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;

public class PersonNum {

    @ApiModelProperty(value = "疾控人数")
    private int cdcNumber;

    @ApiModelProperty(value = "门诊人数")
    private int opcNumber;

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    public int getCdcNumber() {
        return cdcNumber;
    }

    public void setCdcNumber(int cdcNumber) {
        this.cdcNumber = cdcNumber;
    }

    public int getOpcNumber() {
        return opcNumber;
    }

    public void setOpcNumber(int opcNumber) {
        this.opcNumber = opcNumber;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
