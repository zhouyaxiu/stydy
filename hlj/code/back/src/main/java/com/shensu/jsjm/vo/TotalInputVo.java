package com.shensu.jsjm.vo;

import io.swagger.annotations.ApiModelProperty;

public class TotalInputVo {
    @ApiModelProperty(value = "地区编码")
    private String areaCode;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
