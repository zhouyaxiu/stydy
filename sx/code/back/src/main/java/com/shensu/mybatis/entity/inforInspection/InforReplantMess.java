package com.shensu.mybatis.entity.inforInspection;

import com.shensu.mybatis.entity.check.VaccinInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("补种信息查询细节")
public class InforReplantMess {
    @ApiModelProperty(value = "学校id")
    private String schId;
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

    @ApiModelProperty(value = "父亲姓名")
    protected String stuFathername;
    @ApiModelProperty(value = "父亲号码")
    protected String stuFatherPhone;
    @ApiModelProperty(value = "母亲姓名")
    protected String stuMothername;
    @ApiModelProperty(value = "母亲号码")
    protected String stuMotherPhone;

    @ApiModelProperty(value = "接种疫苗名称以及剂次信息")
    private Map<String,String>  replantMap;
    @ApiModelProperty(value = "接种疫苗名称以及剂次信息集合")
    private List<VaccinInfo> replantList;

    //补种补证通知单需要字段
    @ApiModelProperty(value = "所属社区医院")
    private String belongPalceStation;
    @ApiModelProperty(value = "所属社区医院地址")
    private String belongPlaceAddress;
    @ApiModelProperty(value = "所属社区医院电话")
    private String belongPhone;
    @ApiModelProperty(value = "即将转入的学校对应的社区医院")
    private String belongSchoolStation;

    /*
    @ApiModelProperty(hidden = true)
    private String b001State;
    @ApiModelProperty(hidden = true)
    private String b063State;
    @ApiModelProperty(hidden = true)
    private String b064State;
    @ApiModelProperty(hidden = true)
    private String b065State;
    @ApiModelProperty(hidden = true)
    private String b009State;
    @ApiModelProperty(hidden = true)
    private String b010State;
    @ApiModelProperty(hidden = true)
    private String b011State;
    @ApiModelProperty(hidden = true)
    private String b012State;
    @ApiModelProperty(hidden = true)
    private String b015State;
    @ApiModelProperty(hidden = true)
    private String b016State;
    @ApiModelProperty(hidden = true)
    private String b017State;
    @ApiModelProperty(hidden = true)
    private String b018State;
    @ApiModelProperty(hidden = true)
    private String b037State;
    @ApiModelProperty(hidden = true)
    private String b059State;
    @ApiModelProperty(hidden = true)
    private String b060State;
    @ApiModelProperty(hidden = true)
    private String b040State;
    @ApiModelProperty(hidden = true)
    private String b041State;
    @ApiModelProperty(hidden = true)
    private String b045State;
    @ApiModelProperty(hidden = true)
    private String b046State;
    @ApiModelProperty(hidden = true)
    private String b032State;
    @ApiModelProperty(hidden = true)
    private String b033State;
    @ApiModelProperty(hidden = true)
    private String b069State;

    @ApiModelProperty(hidden = true)
    private String b063StateAfter;
    @ApiModelProperty(hidden = true)
    private String b064StateAfter;
    @ApiModelProperty(hidden = true)
    private String b065StateAfter;
    @ApiModelProperty(hidden = true)
    private String b001StateAfter;
    @ApiModelProperty(hidden = true)
    private String b009StateAfter;
    @ApiModelProperty(hidden = true)
    private String b010StateAfter;
    @ApiModelProperty(hidden = true)
    private String b011StateAfter;
    @ApiModelProperty(hidden = true)
    private String b012StateAfter;
    @ApiModelProperty(hidden = true)
    private String b015StateAfter;
    @ApiModelProperty(hidden = true)
    private String b016StateAfter;
    @ApiModelProperty(hidden = true)
    private String b017StateAfter;
    @ApiModelProperty(hidden = true)
    private String b018StateAfter;
    @ApiModelProperty(hidden = true)
    private String b037StateAfter;
    @ApiModelProperty(hidden = true)
    private String b059StateAfter;
    @ApiModelProperty(hidden = true)
    private String b060StateAfter;
    @ApiModelProperty(hidden = true)
    private String b040StateAfter;
    @ApiModelProperty(hidden = true)
    private String b041StateAfter;
    @ApiModelProperty(hidden = true)
    private String b045StateAfter;
    @ApiModelProperty(hidden = true)
    private String b046StateAfter;
    @ApiModelProperty(hidden = true)
    private String b032StateAfter;
    @ApiModelProperty(hidden = true)
    private String b033StateAfter;
    @ApiModelProperty(hidden = true)
    private String b069StateAfter;
    */

}
