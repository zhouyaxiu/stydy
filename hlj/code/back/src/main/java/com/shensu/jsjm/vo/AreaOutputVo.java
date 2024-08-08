package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.SimpleAreaBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AreaOutputVo {

    @ApiModelProperty(value = "地区编码集合")
    private SimpleAreaBean areaMapCode;

}
