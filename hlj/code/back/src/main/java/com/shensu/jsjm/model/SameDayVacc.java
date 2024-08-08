package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;

public class SameDayVacc {

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @ApiModelProperty(value = "免规疫苗剂次数")
    private int freeVaccNum;

    @ApiModelProperty(value = "非免规疫苗剂次数")
    private int chargeVaccNum;

    private String countryCode;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getFreeVaccNum() {
        return freeVaccNum;
    }

    public void setFreeVaccNum(int freeVaccNum) {
        this.freeVaccNum = freeVaccNum;
    }

    public int getChargeVaccNum() {
        return chargeVaccNum;
    }

    public void setChargeVaccNum(int chargeVaccNum) {
        this.chargeVaccNum = chargeVaccNum;
    }
}
