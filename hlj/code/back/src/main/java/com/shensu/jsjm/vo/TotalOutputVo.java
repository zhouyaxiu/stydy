package com.shensu.jsjm.vo;

import com.shensu.jsjm.model.WisdomTodayData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class TotalOutputVo {

    @ApiModelProperty(value = "接种 建档人数统计")
    private WisdomTodayData wisdomTodayData;

}
