package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.LabelValueBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class AgesOutputVo {

    @ApiModelProperty("年龄组")
    List<LabelValueBean> ages;

}
