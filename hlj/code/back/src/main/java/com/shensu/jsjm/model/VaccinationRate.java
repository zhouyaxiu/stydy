package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;

public class VaccinationRate {

    @ApiModelProperty(value = "疫苗种类")
    private String vaccinationName;

    @ApiModelProperty(value = "接种率")
    private String rate;

    public String getVaccinationName() {
        return vaccinationName;
    }

    public void setVaccinationName(String vaccinationName) {
        this.vaccinationName = vaccinationName;
    }

    public String getRate() {
        return rate == null ? "0.00" : rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
