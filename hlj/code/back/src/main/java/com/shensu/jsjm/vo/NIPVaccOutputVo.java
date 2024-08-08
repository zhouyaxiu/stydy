package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.NIPVacc;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class NIPVaccOutputVo {

    @ApiModelProperty(value = "地区下剂次集合")
    private List<NIPVacc> NIPVaccs;

    public List<NIPVacc> getNIPVaccs() {
        return NIPVaccs;
    }

    public void setNIPVaccs(List<NIPVacc> NIPVaccs) {
        this.NIPVaccs = NIPVaccs;
    }
}
