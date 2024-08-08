package com.shensu.jsjm.vo;

import io.swagger.annotations.ApiModelProperty;

public class VaccinationRateInputVo {

    @ApiModelProperty(value = "地区编码")
    private String areaCode;

    @ApiModelProperty(value = "层级：/common/getArea接口返回")
    private int level;

    @ApiModelProperty(value = "年龄:最小年龄")
    private int age;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
