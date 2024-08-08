package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VaccineType {

    @ApiModelProperty("疫苗种类名称")
    private String label;
    @ApiModelProperty("疫苗种类ID")
    private String value;
    @ApiModelProperty("疫苗种类code")
    private String code;

}
