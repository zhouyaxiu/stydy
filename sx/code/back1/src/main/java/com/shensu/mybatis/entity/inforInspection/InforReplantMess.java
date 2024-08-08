package com.shensu.mybatis.entity.inforInspection;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("补种信息查询细节")
public class InforReplantMess {
    @ApiModelProperty(value = "学校id")
    private  String  schId;
    @ApiModelProperty(value = "学校名称")
    private String schName;
    @ApiModelProperty(value = "年级")
    private String claLevel;
    @ApiModelProperty(value = "班级主键")
    private String claCode;
    @ApiModelProperty(value = "班级名称")
    private String claName;
    @ApiModelProperty(value = "学校主键")
    private Long stuRowkey;
    @ApiModelProperty(value = "学生姓名")
    private String stuName;
    @ApiModelProperty(value = "个案编码")
    private String stuNo;
    @ApiModelProperty(value = "身份证")
    private String stuCid;
    @ApiModelProperty(value = "性别")
    private String stuSex;
    @ApiModelProperty(value = "生日")
    private String stuBirth;
    @ApiModelProperty(value = "家庭住址")

    private String stuAddr;
    @ApiModelProperty(value = "补种的疫苗剂次")
    private String replantName;
    @ApiModelProperty(value = "补种疫苗剂次的时间")
    private String replateDate;
    @ApiModelProperty(value = "是否种全")
    private String ifFull;
    @ApiModelProperty(value = "父母姓名")
    private String parentName;
    @ApiModelProperty(value = "接种疫苗名称以及剂次信息")
    private Map<String,String>  replantMap;

    //补种补证通知单需要字段
    @ApiModelProperty(value = "所属社区医院")
    private String belongPalceStation;
    @ApiModelProperty(value = "所属社区医院地址")
    private String belongPlaceAddress;
    @ApiModelProperty(value = "所属社区医院电话")
    private String belongPhone;
    @ApiModelProperty(value = "即将转入的学校对应的社区医院")
    private String belongSchoolStation;


}
