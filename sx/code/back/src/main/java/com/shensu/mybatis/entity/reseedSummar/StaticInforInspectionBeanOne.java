package com.shensu.mybatis.entity.reseedSummar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StaticInforInspectionBeanOne {
	private String name; // 名称列(根据情况不同，分别暂时学校名/班级名/地区名)
    private  String grade;//选择学校时显示年级+班级，不选择学校显示学校
	private String code; //

	protected int virtualSchoolCount; // 应查验学校数

	protected int schoolCount;  // 实查验学校数

    private String rowKey;
    //------------INT
    protected int  virtualStudentCount; // 应查验学生数
    protected int  studentCount; // 查验人数
    protected int  hasNotCard; // 无证人数
    protected int  hasCard; // 有证人数

    protected int  needCard; // 补证人数

    protected int  needReplant; // 需补种人数

    protected int  needReplantFull; // 需补种人数中已补种完成人数
    protected int  hasReplant; // 完成全程接种

    protected int  vaccineNeedReplant; // 疫苗需补种剂次数

    protected int  vaccineHasReplant; // 疫苗已补种剂次数

    protected String  replantPercent; // 补种率


    @JsonProperty("B063NeedReplant")
    protected int  B063NeedReplant;
    @JsonProperty("B064NeedReplant")
    protected int  B064NeedReplant;
    @JsonProperty("B065NeedReplant")
    protected int  B065NeedReplant;
    @JsonProperty("B001NeedReplant")
    protected int  B001NeedReplant;
    @JsonProperty("B009NeedReplant")
    protected int  B009NeedReplant;
    @JsonProperty("B010NeedReplant")
    protected int  B010NeedReplant;
    @JsonProperty("B011NeedReplant")
    protected int  B011NeedReplant;
    @JsonProperty("B012NeedReplant")
    protected int  B012NeedReplant;
    @JsonProperty("B015NeedReplant")
    protected int  B015NeedReplant;
    @JsonProperty( "B016NeedReplant")
    protected int  B016NeedReplant;
    @JsonProperty("B017NeedReplant")
    protected int  B017NeedReplant;
    @JsonProperty("B018NeedReplant")
    protected int  B018NeedReplant;
    @JsonProperty("B037NeedReplant")
    protected int  B037NeedReplant;
    @JsonProperty("B038NeedReplant")
    protected int  B038NeedReplant;
    @JsonProperty("B039NeedReplant")
    protected int  B039NeedReplant;
    @JsonProperty("B059NeedReplant")
    protected int  B059NeedReplant;
    @JsonProperty("B060NeedReplant")
    protected int  B060NeedReplant;
    @JsonProperty("B040NeedReplant")
    protected int  B040NeedReplant;
    @JsonProperty("B041NeedReplant")
    protected int  B041NeedReplant;
    @JsonProperty("B045NeedReplant")
    protected int  B045NeedReplant;
    @JsonProperty("B046NeedReplant")
    protected int  B046NeedReplant;
    @JsonProperty("B032NeedReplant")
    protected int  B032NeedReplant;
    @JsonProperty("B033NeedReplant")
    protected int  B033NeedReplant;
    @JsonProperty("B073NeedReplant")
    protected int  B073NeedReplant;
    @JsonProperty("B074NeedReplant")
    protected int  B074NeedReplant;
    //
    @JsonProperty("B063HasReplant")
    protected int  B063HasReplant;
    @JsonProperty("B064HasReplant")
    protected int  B064HasReplant;
    @JsonProperty("B065HasReplant")
    protected int  B065HasReplant;
    @JsonProperty("B001HasReplant")
    protected int  B001HasReplant;
    @JsonProperty("B009HasReplant")
    protected int  B009HasReplant;
    @JsonProperty("B010HasReplant")
    protected int  B010HasReplant;
    @JsonProperty("B011HasReplant")
    protected int  B011HasReplant;
    @JsonProperty("B012HasReplant")
    protected int  B012HasReplant;
    @JsonProperty("B015HasReplant")
    protected int  B015HasReplant;
    @JsonProperty("B016HasReplant")
    protected int  B016HasReplant;
    @JsonProperty("B017HasReplant")
    protected int  B017HasReplant;
    @JsonProperty("B018HasReplant")
    protected int  B018HasReplant;
    @JsonProperty("B037HasReplant")
    protected int  B037HasReplant;

    @JsonProperty("B038HasReplant")
    protected int  B038HasReplant;
    @JsonProperty("B039HasReplant")
    protected int  B039HasReplant;
    @JsonProperty("B059HasReplant")
    protected int  B059HasReplant;
    @JsonProperty("B060HasReplant")
    protected int  B060HasReplant;
    @JsonProperty("B040HasReplant")
    protected int  B040HasReplant;
    @JsonProperty("B041HasReplant")
    protected int  B041HasReplant;
    @JsonProperty("B045HasReplant")
    protected int  B045HasReplant;
    @JsonProperty("B046HasReplant")
    protected int  B046HasReplant;
    @JsonProperty("B032HasReplant")
    protected int  B032HasReplant;
    @JsonProperty("B033HasReplant")
    protected int  B033HasReplant;
    @JsonProperty( "B073HasReplant")
    protected int  B073HasReplant;
    @JsonProperty( "B074HasReplant")
    protected int  B074HasReplant;

    @JsonProperty( "B050HasReplant")
    protected int  B050HasReplant;
    @JsonProperty( "B050NeedReplant")
    protected int  B050NeedReplant;
    @JsonProperty( "B132HasReplant")
    protected int  B132HasReplant;
    @JsonProperty( "B132NeedReplant")
    protected int  B132NeedReplant;
    //
    @JsonProperty("KaJieHasReplantSubtotal")
    protected int  KaJieHasReplantSubtotal;
    @JsonProperty("BaiPoHasReplantSubtotal")
    protected int  BaiPoHasReplantSubtotal;
    @JsonProperty("JiaGanJianDuHuoHasReplantSubtotal")
    protected int  JiaGanJianDuHuoHasReplantSubtotal;
    @JsonProperty("YiGanHasReplantSubtotal")
    protected int  YiGanHasReplantSubtotal;
    @JsonProperty("JiHuiHasReplantSubtotal")
    protected int  JiHuiHasReplantSubtotal;
    @JsonProperty("BaiBaiPoHasReplantSubtotal")
    protected int  BaiBaiPoHasReplantSubtotal;
    @JsonProperty("MaFengHasReplantSubtotal")
    protected int  MaFengHasReplantSubtotal;
    @JsonProperty(  "AQunLiuNaoHasReplantSubtotal")
    protected int  AQunLiuNaoHasReplantSubtotal;
    @JsonProperty(  "ACQunLiuNaoHasReplantSubtotal")
    protected int  ACQunLiuNaoHasReplantSubtotal;
    @JsonProperty(  "YiNaoJianDuHuoHasReplantSubtotal")
    protected int  YiNaoJianDuHuoHasReplantSubtotal;
    @JsonProperty(  "ShuiDouHasReplantSubtotal")
    protected int  ShuiDouHasReplantSubtotal;

    @JsonProperty(  "JiaGanMieHasReplantSubtotal")
    protected int  JiaGanMieHasReplantSubtotal;
    //
    @JsonProperty(  "KaJieNeedReplantSubtotal")
    protected int  KaJieNeedReplantSubtotal;
    @JsonProperty(  "BaiPoNeedReplantSubtotal")
    protected int  BaiPoNeedReplantSubtotal;
    @JsonProperty(  "JiaGanJianDuHuoNeedReplantSubtotal")
    protected int  JiaGanJianDuHuoNeedReplantSubtotal;
    @JsonProperty(  "YiGanNeedReplantSubtotal")
    protected int  YiGanNeedReplantSubtotal;
    @JsonProperty(  "JiHuiNeedReplantSubtotal")
    protected int  JiHuiNeedReplantSubtotal;
    @JsonProperty(  "BaiBaiPoNeedReplantSubtotal")
    protected int  BaiBaiPoNeedReplantSubtotal;
    @JsonProperty(  "MaFengNeedReplantSubtotal")
    protected int  MaFengNeedReplantSubtotal;
    @JsonProperty(  "AQunLiuNaoNeedReplantSubtotal")
    protected int  AQunLiuNaoNeedReplantSubtotal;
    @JsonProperty(  "ACQunLiuNaoNeedReplantSubtotal")
    protected int  ACQunLiuNaoNeedReplantSubtotal;
    @JsonProperty(  "YiNaoJianDuHuoNeedReplantSubtotal")
    protected int  YiNaoJianDuHuoNeedReplantSubtotal;
    @JsonProperty(  "ShuiDouNeedReplantSubtotal")
    protected int  ShuiDouNeedReplantSubtotal;

    @JsonProperty(  "JiaGanMieNeedReplantSubtotal")
    protected int  JiaGanMieNeedReplantSubtotal;
}
