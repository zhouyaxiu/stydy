package com.shensu.mybatis.entity.check;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName Student.java
 * @Description 学生信息
 * @createTime 2023年04月6日 10:25
 */
@Data
@ApiModel("儿童简略信息实体类")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class StudentBrief implements Cloneable, java.io.Serializable {

    private static final long serialVersionUID = 7822322971593117510L;

    /**
     * 来源: saas信息系统
     */
    public static final String SOURCE_SAAS = "0";

    /**
     * 来源: excel导入
     */
    public static final String SOURCE_EXCEL = "1";

    /**
     * 来源: 校医录入
     */
    public static final String SOURCE_DOCTOR = "2";

    /**
     * 未建档状态
     */
    public static final String STATE_NO_ARCHIVE = "2";

    @ApiModelProperty(value = "主键")
    protected Long rowkey;                                     //受种者20位编码
    @ApiModelProperty(value = "受种者预防接种档案编号")
    protected String stuNo;                                      //受种者预防接种档案编号
    @ApiModelProperty(value = "免疫库身份证件号")
    protected transient String cid;                              //免疫库身份证件号
    @ApiModelProperty(value = "免疫库个案编码")
    protected transient String fchildno;                         //免疫库个案编码
    @ApiModelProperty(value = "受种者条码")
    protected String stuImuno;                                   //受种者条码
    @ApiModelProperty(value = "学生姓名")
    protected String stuName;                                    //学生姓名
    @ApiModelProperty(value = "学生生日")
    protected String stuBirth;                                   //学生生日
    @ApiModelProperty(value = "学生性别")
    protected String stuSex;                                     //学生性别
    @ApiModelProperty(value = "学生户籍属性")
    protected String stuType;                                    //学生户籍属性
    @ApiModelProperty(value = "报到时是否有接种证", example = "0有1无")
    protected String stuIsjzz;                                   //报到时是否有接种证	0有1无
    @ApiModelProperty(value = "是否补接种证", example = "0有1无")
    protected String stuIsbjzz;                                  //是否补接种证	0有1无
    @ApiModelProperty(value = "父母姓名")
    protected String stuParentname;                              //父母姓名
    @ApiModelProperty(value = "手机")
    protected String stuPhone;                                   //手机

    @ApiModelProperty(value = "学年")
    private String academicYear;                                 //学年
    @ApiModelProperty(value = "轮次", example = "1春季0秋季")
    protected String rounds;                                     //'1':'春季','0':'秋季'
    @ApiModelProperty(value = "年级")
    protected String grade;                                      //年级
    @ApiModelProperty(value = "班级")
    protected String clazzName;                                  //班级

    @ApiModelProperty(value = "地级市编码")
    protected String stuCitycode;                                //地级市编码
    @ApiModelProperty(value = "县区编码")
    protected String stuCountycode;                              //县区编码
    @ApiModelProperty(value = "接种单位编码")
    protected String stuStationcode;                             //接种单位编码

    @ApiModelProperty(value = "接种单位名称")
    protected String stuStationName;                             //接种单位名称

    @ApiModelProperty(value = "归属接种单位编码")
    protected String stuBelongStationCode;

    @ApiModelProperty(value = "归属接种单位名称")
    protected String stuBelongStationName;

    @ApiModelProperty(value = "乡镇编码")
    protected String stuStreetcode;                              //乡镇编码
    @ApiModelProperty(value = "接种单位名称")
    protected String stuStationname;                             //接种单位名称
    @ApiModelProperty(hidden = true)
    protected String stuStatus;
    @ApiModelProperty(value = "父亲姓名")
    protected String stuFathername;                              //父亲姓名
    @ApiModelProperty(value = "父亲号码")
    protected String stuFatherPhone;                             //父亲号码
    @ApiModelProperty(value = "母亲姓名")
    protected String stuMothername;                              //母亲姓名
    @ApiModelProperty(value = "母亲号码")
    protected String stuMotherPhone;                              //母亲号码
    @ApiModelProperty(value = "其它电话、固定电话")
    protected String stuOtherphone;                              //其它电话、固定电话

    @ApiModelProperty(value = "其它联系人")
    protected String stuOtherLinkMan;

    @ApiModelProperty(value = "所属社区医院", hidden = true)
    private String belongPalceStation;
    @ApiModelProperty(value = "所属社区医院地址", hidden = true)
    private String belongPlaceAddress;
    @ApiModelProperty(value = "所属社区医院电话", hidden = true)
    private String linkPhone;
    @ApiModelProperty(value = "即将转入的学校对应的社区医院", hidden = true)
    private String belongSchoolStation;

    @ApiModelProperty(value = "家庭住址", hidden = true)
    private String fResideAddr;
    @ApiModelProperty(value = "居委会", hidden = true)
    private String iRc;


    @Override
    public Object clone() {
        StudentBrief result = null;
        try {
            result = (StudentBrief) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String getClaLevelName(String l) {
        if ("0".equals(l)) {
            return "幼儿园托班";
        } else if ("1".equals(l)) {
            return "幼儿园小班";
        } else if ("2".equals(l)) {
            return "幼儿园中班";
        } else if ("3".equals(l)) {
            return "幼儿园大班";
        } else if ("4".equals(l)) {
            return "一年级";
        } else if ("5".equals(l)) {
            return "二年级";
        } else if ("6".equals(l)) {
            return "三年级";
        } else if ("7".equals(l)) {
            return "四年级";
        } else if ("8".equals(l)) {
            return "五年级";
        } else if ("9".equals(l)) {
            return "六年级";
        }
        return l;
    }

    public static String getClaLevelNum(String l) {
        if ("幼儿园托班".equals(l)) {
            return "0";
        } else if ("幼儿园小班".equals(l)) {
            return "1";
        } else if ("幼儿园中班".equals(l)) {
            return "2";
        } else if ("幼儿园大班".equals(l)) {
            return "3";
        } else if ("一年级".equals(l)) {
            return "4";
        } else if ("二年级".equals(l)) {
            return "5";
        } else if ("三年级".equals(l)) {
            return "6";
        } else if ("四年级".equals(l)) {
            return "7";
        } else if ("五年级".equals(l)) {
            return "8";
        } else if ("六年级".equals(l)) {
            return "9";
        }
        return l;
    }

    //转换户籍类型
    public static String changeStuType(String typeCode){
        String str = null;
        if("1".equals(typeCode)) {
            str="县区内流动";
        } else if("3".equals(typeCode)) {
            str="市地内流动";
        } else if("4".equals(typeCode)) {
            str="本省内流动";
        } else if("5".equals(typeCode)) {
            str="省际内流动";
        } else if("6".equals(typeCode)) {
            str="国际流动";
        }
        return str;
    }

}
