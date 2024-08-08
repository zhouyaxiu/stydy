package com.shensu.jsjm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InventoryInputVo {

    @ApiModelProperty(value = "地区编码")
    private String areaCode;

    @ApiModelProperty(value = "疫苗种类id")
    private String vaccineId;
}
