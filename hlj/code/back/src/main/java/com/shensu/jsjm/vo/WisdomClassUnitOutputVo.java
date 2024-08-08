package com.shensu.jsjm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:todo
 * @author：GJ
 * @date：2023/11/20 10:20
 */
@Data
public class WisdomClassUnitOutputVo {

    @ApiModelProperty(value = "常规接种单位数量")
    private int commonUnitNum;
    @ApiModelProperty(value = "产科接种单位数量")
    private int hosUnitNum;
    @ApiModelProperty(value = "成人接种单位数量")
    private int adultUnitNum;
    @ApiModelProperty(value = "狂犬病暴露处置门诊数量")
    private int rabiesUnitNum;
    @ApiModelProperty(value = "新冠疫苗接种点数量")
    private int COVIDUnitNum;
    @ApiModelProperty(value = "接种单位总数")
    private int total;
    @ApiModelProperty(value = "当日开诊的常规接种单位数量")
    private int nowCommonUnitNum;
    @ApiModelProperty(value = "当日开诊的产科接种单位数量")
    private int nowHosUnitNum;
    @ApiModelProperty(value = "当日开诊的成人接种单位数量")
    private int nowAdultUnitNum;
    @ApiModelProperty(value = "当日开诊的狂犬病暴露处置门诊数量")
    private int nowRabiesUnitNum;
    @ApiModelProperty(value = "当日开诊的新冠疫苗接种点数量")
    private int nowCOVIDUnitNum;
    @ApiModelProperty(value = "当日开诊的接种单位总数")
    private int nowTotal;

}
