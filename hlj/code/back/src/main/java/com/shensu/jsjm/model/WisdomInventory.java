package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:todo
 * @author：GJ
 * @date：2023/11/29 9:56
 */
@Data
public class WisdomInventory {

    @ApiModelProperty("疫苗种类")
    private String GOODSTYPE;
    @ApiModelProperty("疫苗大类编码")
    private String GOODSSORTCODE;
    @ApiModelProperty("疫苗大类名称")
    private String GOODSSORTNAME;
    @ApiModelProperty("效期区间")
    private String EXCEEDTYPE;
    @ApiModelProperty("当前库存")
    private int provinceSTORENUM;
    @ApiModelProperty("效期小于1个月的实时库存数")
    private int provinceLessOneMonthNum = 0;
    @ApiModelProperty("效期为1-3个月的实时库存数")
    private int provinceLessThreeMonthNum = 0;
    @ApiModelProperty("效期大于3个月的实时库存数")
    private int provinceMoreThreeMonthNum = 0;
    @ApiModelProperty("当前库存")

    private int citySTORENUM;
    @ApiModelProperty("效期小于1个月的实时库存数")
    private int cityLessOneMonthNum = 0;
    @ApiModelProperty("效期为1-3个月的实时库存数")
    private int cityLessThreeMonthNum = 0;
    @ApiModelProperty("效期大于3个月的实时库存数")
    private int cityMoreThreeMonthNum = 0;

    private int couSTORENUM;
    @ApiModelProperty("效期小于1个月的实时库存数")
    private int couLessOneMonthNum = 0;
    @ApiModelProperty("效期为1-3个月的实时库存数")
    private int couLessThreeMonthNum = 0;
    @ApiModelProperty("效期大于3个月的实时库存数")
    private int couMoreThreeMonthNum = 0;


    private int unitSTORENUM;
    @ApiModelProperty("效期小于1个月的实时库存数")
    private int unitLessOneMonthNum = 0;
    @ApiModelProperty("效期为1-3个月的实时库存数")
    private int unitLessThreeMonthNum = 0;
    @ApiModelProperty("效期大于3个月的实时库存数")
    private int unitMoreThreeMonthNum = 0;

}
