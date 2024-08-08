package com.shensu.jsjm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class ClassUnitOutputVo {

    @ApiModelProperty(value = "常规接种单位数量")
    private int commonUnitNum;
    @ApiModelProperty(value = "产科接种单位数量")
    private int hosUnitNum;
    @ApiModelProperty(value = "成人接种单位数量")
    private int adultUnitNum;
    @ApiModelProperty(value = "狂犬病暴露处置门诊数量")
    private int rabiesUnitNum;

    @ApiModelProperty(value = "接种单位总数")
    private int total;

}
