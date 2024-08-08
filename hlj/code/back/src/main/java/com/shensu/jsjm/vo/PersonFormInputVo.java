package com.shensu.jsjm.vo;

import io.swagger.annotations.ApiModelProperty;

public class PersonFormInputVo {

    @ApiModelProperty(value = "地区编码，默认省级编码")
    private String areaCode;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
