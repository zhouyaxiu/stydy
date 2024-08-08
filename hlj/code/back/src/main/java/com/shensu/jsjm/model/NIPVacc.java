package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @description:todo
 * @author：GJ
 * @date：2023/11/20 15:45
 */
public class NIPVacc {

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @ApiModelProperty(value = "免规疫苗剂次数")
    private BigDecimal SZPercent;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public BigDecimal getSZPercent() {
        return SZPercent;
    }

    public void setSZPercent(BigDecimal SZPercent) {
        this.SZPercent = SZPercent;
    }
}
