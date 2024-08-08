package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.VaccinationCount;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class VaccinationCountOutputVo {

    @ApiModelProperty(value = "疫苗接量集合")
    private List<VaccinationCount> vaccinationCounts;

    public List<VaccinationCount> getVaccinationCounts() {
        return vaccinationCounts;
    }

    public void setVaccinationCounts(List<VaccinationCount> vaccinationCounts) {
        this.vaccinationCounts = vaccinationCounts;
    }
}
