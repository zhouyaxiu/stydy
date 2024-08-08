package com.shensu.mybatis.entity.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName ReplantVaccinumStatBean.java
 * @Description 补种疫苗统计
 * @createTime 2023年04月13日 16:11
 */
@Data
@ApiModel("补种疫苗统计类")
public class ReplantVacStatBean {

    //------------STRING
    private String rowKey;
    //------------INT
    protected int  virtualStudentCount;
    protected int  studentCount;
    protected int  hasNotCard;
    protected int  hasCard;
    protected int  needReplant;
    protected int  hasReplant;
    // 需要补种的数量
    protected int  B063NeedReplant;
    protected int  B064NeedReplant;
    protected int  B065NeedReplant;
    protected int  B001NeedReplant;
    protected int  B009NeedReplant;
    protected int  B010NeedReplant;
    protected int  B011NeedReplant;
    protected int  B012NeedReplant;
    protected int  B015NeedReplant;
    protected int  B016NeedReplant;
    protected int  B017NeedReplant;
    protected int  B018NeedReplant;
    protected int  B037NeedReplant;

    protected int  B038NeedReplant;                   //白破2
    protected int  B039NeedReplant;                   //白破3

    protected int  B059NeedReplant;
    protected int  B060NeedReplant;
    protected int  B040NeedReplant;
    protected int  B041NeedReplant;
    protected int  B045NeedReplant;
    protected int  B046NeedReplant;
    protected int  B032NeedReplant;
    protected int  B033NeedReplant;
    protected int  B073NeedReplant;
    protected int  B074NeedReplant;

    // 已经补种的数量
    protected int  B063HasReplant;
    protected int  B064HasReplant;
    protected int  B065HasReplant;
    protected int  B001HasReplant;
    protected int  B009HasReplant;
    protected int  B010HasReplant;
    protected int  B011HasReplant;
    protected int  B012HasReplant;
    protected int  B015HasReplant;
    protected int  B016HasReplant;
    protected int  B017HasReplant;
    protected int  B018HasReplant;
    protected int  B037HasReplant;

    protected int  B038HasReplant;                    //白破2
    protected int  B039HasReplant;                    //白破3

    protected int  B059HasReplant;
    protected int  B060HasReplant;
    protected int  B040HasReplant;
    protected int  B041HasReplant;
    protected int  B045HasReplant;
    protected int  B046HasReplant;
    protected int  B032HasReplant;
    protected int  B033HasReplant;
    protected int  B073HasReplant;
    protected int  B074HasReplant;
    // 小计已经补种
    @ApiModelProperty(value = "卡介苗已补种")
    protected int KaJieHasReplantSubtotal;
    @ApiModelProperty(value = "白破已补种")
    protected int BaiPoHasReplantSubtotal;
    @ApiModelProperty(value = "甲肝减毒已补种")
    protected int JiaGanJianDuHuoHasReplantSubtotal;
    @ApiModelProperty(value = "乙肝已补种")
    protected int YiGanHasReplantSubtotal;
    @ApiModelProperty(value = "脊灰已补种")
    protected int JiHuiHasReplantSubtotal;
    @ApiModelProperty(value = "百白破已补种")
    protected int BaiBaiPoHasReplantSubtotal;
    @ApiModelProperty(value = "麻疹已补种")
    protected int MaFengHasReplantSubtotal;
    @ApiModelProperty(value = "A群流脑已补种")
    protected int AQunLiuNaoHasReplantSubtotal;
    @ApiModelProperty(value = "A+C群流脑已补种")
    protected int ACQunLiuNaoHasReplantSubtotal;
    @ApiModelProperty(value = "乙脑已补种")
    protected int YiNaoJianDuHuoHasReplantSubtotal;
    // 小计需要补种
    @ApiModelProperty(value = "卡介苗需补种")
    protected int KaJieNeedReplantSubtotal;
    @ApiModelProperty(value = "白破需补种")
    protected int BaiPoNeedReplantSubtotal;
    @ApiModelProperty(value = "甲肝减毒需补种")
    protected int JiaGanJianDuHuoNeedReplantSubtotal;
    @ApiModelProperty(value = "乙肝需补种")
    protected int YiGanNeedReplantSubtotal;
    @ApiModelProperty(value = "脊灰需补种")
    protected int JiHuiNeedReplantSubtotal;
    @ApiModelProperty(value = "百白破需补种")
    protected int BaiBaiPoNeedReplantSubtotal;
    @ApiModelProperty(value = "麻疹需补种")
    protected int MaFengNeedReplantSubtotal;
    @ApiModelProperty(value = "A群流脑需补种")
    protected int AQunLiuNaoNeedReplantSubtotal;
    @ApiModelProperty(value = "A+C群流脑需补种")
    protected int ACQunLiuNaoNeedReplantSubtotal;
    @ApiModelProperty(value = "乙脑需补种")
    protected int YiNaoJianDuHuoNeedReplantSubtotal;
    //补种率
    @ApiModelProperty(value = "卡介苗补种率")
    protected String KaJieHasReplantRate;
    @ApiModelProperty(value = "白破补种率")
    protected String BaiPoHasReplantRate;
    @ApiModelProperty(value = "甲肝减毒补种率")
    protected String JiaGanJianDuHuoHasReplantRate;
    @ApiModelProperty(value = "乙肝补种率")
    protected String YiGanHasReplantRate;
    @ApiModelProperty(value = "脊灰补种率")
    protected String JiHuiHasReplantRate;
    @ApiModelProperty(value = "百白破补种率")
    protected String BaiBaiPoHasReplantRate;
    @ApiModelProperty(value = "麻疹补种率")
    protected String MaFengHasReplantRate;
    @ApiModelProperty(value = "A群流脑补种率")
    protected String AQunLiuNaoHasReplantRate;
    @ApiModelProperty(value = "A+C群流脑补种率")
    protected String ACQunLiuNaoHasReplantRate;
    @ApiModelProperty(value = "乙脑补种率")
    protected String YiNaoJianDuHuoHasReplantRate;
    //合计
    @ApiModelProperty(value = "已补种合计")
    protected int hasReplantTotal;
    @ApiModelProperty(value = "需补种合计")
    protected int NeedReplantTotal;
    @ApiModelProperty(value = "已补种率合计")
    protected String HasReplantTotalRate;

}
