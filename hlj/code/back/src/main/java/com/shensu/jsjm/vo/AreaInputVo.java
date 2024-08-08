package com.shensu.jsjm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AreaInputVo {

    @ApiModelProperty(value = "地区编码")
    private String areaCode;

}
