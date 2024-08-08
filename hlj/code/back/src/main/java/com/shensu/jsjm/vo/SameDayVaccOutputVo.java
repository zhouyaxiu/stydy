package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.SameDayVacc;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class SameDayVaccOutputVo {

    @ApiModelProperty(value = "地区下剂次集合")
    private List<SameDayVacc> sameDayVaccs;

    public List<SameDayVacc> getSameDayVaccs() {
        return sameDayVaccs;
    }

    public void setSameDayVaccs(List<SameDayVacc> sameDayVaccs) {
        this.sameDayVaccs = sameDayVaccs;
    }
}
