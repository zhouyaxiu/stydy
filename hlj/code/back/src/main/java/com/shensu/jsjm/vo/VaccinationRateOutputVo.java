package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.VaccinationRate;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class VaccinationRateOutputVo {

    @ApiModelProperty(value = "疫苗接种率集合")
    private List<VaccinationRate> vaccinationRates;

    public List<VaccinationRate> getVaccinationRates() {
        return vaccinationRates;
    }

    public void setVaccinationRates(List<VaccinationRate> vaccinationRates) {
        this.vaccinationRates = vaccinationRates;
    }
}
