package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Inventory {
    @ApiModelProperty("地区行政编码")
    private String GBCODE;
    @ApiModelProperty("地区行政名称")
    private String GBNAME;
    @ApiModelProperty("疫苗种类")
    private String GOODSTYPE;
    @ApiModelProperty("疫苗大类编码")
    private String GOODSSORTCODE;
    @ApiModelProperty("效期区间")
    private String EXCEEDTYPE;
    @ApiModelProperty("当前库存")
    private int STORENUM;
    @ApiModelProperty("效期小于1个月的实时库存数")
    private int lessOneMonthNum = 0;
    @ApiModelProperty("效期为1-3个月的实时库存数")
    private int lessThreeMonthNum = 0;
    @ApiModelProperty("效期大于3个月的实时库存数")
    private int moreThreeMonthNum = 0;

}
