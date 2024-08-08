package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;

public class VaccinationCount {

    @ApiModelProperty(value = "疫苗种类")
    private String vaccinationName;

    @ApiModelProperty(value = "接种量")
    private long count;

    public String getVaccinationName() {
        return vaccinationName;
    }

    public void setVaccinationName(String vaccinationName) {
        this.vaccinationName = vaccinationName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
