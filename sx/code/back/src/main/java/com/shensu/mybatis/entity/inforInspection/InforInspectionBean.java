package com.shensu.mybatis.entity.inforInspection;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("海南省接种信息查询实体类")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class InforInspectionBean {
    private String areaName;//单位名称
    private String areaCode;//单位编码
    private int  shcNum;//应查验学校数
    private int checkSchNum;//查验学校数
    private int allStuNum;//入学入托总人数
    private int  checkStuNum;//实际查验人数
    private int  hasCard; // 有证人数
    private int  needCard; // 补证人数
    private int qcjzCount; //完成全程接种人数
    private int needReplant;//需补种疫苗人数
    private int hasReplant;//完成全程补种人数
    //卡介
    private int  B001NeedReplant;
    private int  B001HasReplant;

   //乙肝
    private int  B063NeedReplant;
    private int  B064NeedReplant;
    private int  B065NeedReplant;
    private int  B063HasReplant;
    private int  B064HasReplant;
    private int  B065HasReplant;

    //脊灰
    private int  B009NeedReplant;
    private int  B010NeedReplant;
    private int  B011NeedReplant;
    private int  B012NeedReplant;
    private int  B009HasReplant;
    private int  B010HasReplant;
    private int  B011HasReplant;
    private int  B012HasReplant;
    //百白破
    private int  B015NeedReplant;
    private int  B016NeedReplant;
    private int  B017NeedReplant;
    private int  B018NeedReplant;
    private int  B015HasReplant;
    private int  B016HasReplant;
    private int  B017HasReplant;
    private int  B018HasReplant;
    //白破
    private int  B037NeedReplant;
    private int  B037HasReplant;
    //麻疹
    private int  B059NeedReplant;
    private int  B060NeedReplant;
    private int  B059HasReplant;
    private int  B060HasReplant;
    //A 群流脑
    private int  B040NeedReplant;
    private int  B041NeedReplant;
    private int  B040HasReplant;
    private int  B041HasReplant;
    //A+C 群流脑
    private int  B045NeedReplant;
    private int  B046NeedReplant;
    private int  B045HasReplant;
    private int  B046HasReplant;
    //乙脑
    private int  B032NeedReplant;
    private int  B033NeedReplant;
    private int  B032HasReplant;
    private int  B033HasReplant;
    //甲肝1
    private int  B073NeedReplant;
    private int  B073HasReplant;


    //甲肝2
    private int  B074NeedReplant;
    private int  B074HasReplant;
    //水痘
    private int  B050NeedReplant;
    private int  B132NeedReplant;
    private int  B050HasReplant;
    private int  B132HasReplant;
    //private int hasReplantTotal;//已补种总剂次数
    //private int needReplantTotal;//需补充总剂次数
   //private String  replantPercent;//补种率
}
