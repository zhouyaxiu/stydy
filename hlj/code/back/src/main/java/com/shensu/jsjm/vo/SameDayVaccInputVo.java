package com.shensu.jsjm.vo;

import io.swagger.annotations.ApiModelProperty;

public class SameDayVaccInputVo {

    @ApiModelProperty(value = "地区编码")
    private String areaCode;

    @ApiModelProperty(value = "层级：/common/getArea接口返回")
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
