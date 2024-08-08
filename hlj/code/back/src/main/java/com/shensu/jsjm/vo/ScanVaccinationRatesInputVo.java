package com.shensu.jsjm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ScanVaccinationRatesInputVo {

    @ApiModelProperty(value = "地区编码")
    private String areaCode;

    @ApiModelProperty(value = "层级：/common/getArea接口返回")
    private int level;


    @ApiModelProperty(value = "月份")
    private String month;
}

