package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.VaccineType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class VaccineTypeOutputVo {

    @ApiModelProperty("疫苗种类集合")
    private List<VaccineType> vaccineTypeList;

}
