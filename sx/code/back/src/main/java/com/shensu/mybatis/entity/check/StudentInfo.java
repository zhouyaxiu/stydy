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
 * @createTime 2023年09月06日 10:25
 */
@Data
@ApiModel("儿童信息实体类")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class StudentInfo implements Cloneable, java.io.Serializable {

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
    protected Long rowkey;                                       //受种者20位编码
    @ApiModelProperty(value = "受种者预防接种档案编号")
    protected String stuNo;                                      //受种者预防接种档案编号
    @ApiModelProperty(value = "免疫库身份证件号")
    protected transient String cid;                              //免疫库身份证件号
    @ApiModelProperty(value = "免疫库个案编码")
    protected String fchildno;                                   //免疫库个案编码
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
    @ApiModelProperty(value = "班级名称")
    protected String className;                                  //班级
    @ApiModelProperty(value = "班级id")
    protected String classId;                                    //班级id

    @ApiModelProperty(value = "地级市编码")
    protected String stuCitycode;                                //地级市编码
    @ApiModelProperty(value = "县区编码")
    protected String stuCountycode;                              //县区编码
    @ApiModelProperty(value = "乡镇编码")
    protected String stuStreetcode;                              //乡镇编码
    @ApiModelProperty(value = "接种单位编码")
    protected String stuStationcode;                             //接种单位编码

    @ApiModelProperty(value = "归属接种单位编码")
    protected String stuBelongStationCode;

    @ApiModelProperty(value = "归属接种单位名称")
    protected String stuBelongStationName;

    @ApiModelProperty(value = "增加学生时间")
    protected String stuAddtime;                                 //增加学生时间
    @ApiModelProperty(value = "记录更新时间")
    protected String stuUpdatetime;                              //记录更新时间

    @ApiModelProperty(value = "接种单位名称")
    protected String stuStationName;                             //接种单位名称
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

    @ApiModelProperty(value = "家庭住址", hidden = true)
    private String fResideAddr;

    @ApiModelProperty(value = "学校主键")
    private String schid;                                      //学校rowkey
    @ApiModelProperty(value = "学校名称")
    private String schName;                                    //学校名称
    @ApiModelProperty(value = "扫码日期")
    private String stuSmrq;                                   //扫码日期
    @ApiModelProperty(value = "上次扫码日期")
    private String lastStuSmrq;                              //上次扫码日期
    @ApiModelProperty(value = "初次扫码日期")
    private String firstStuSmrq;                              //初次扫码日期
    @ApiModelProperty(value = "上次扫码学校")
    private String lastStuSchname;                           //上次扫码学校

    //用于展示的字段
    @ApiModelProperty(value = "流感1")
    private String fluFir;

    @ApiModelProperty(value = "流感1剂次")
    private String fluFirId;

    @ApiModelProperty(value = "流感1剂次名称")
    private String fluFirName;

    @ApiModelProperty(value = "流感2")
    private String fluSec;

    @ApiModelProperty(value = "流感2剂次")
    private String fluSecId;

    @ApiModelProperty(value = "流感2剂次名称")
    private String fluSecName;

    @ApiModelProperty(value = "水痘1")
    private String varFir;

    @ApiModelProperty(value = "水痘1剂次")
    private String varFirId;

    @ApiModelProperty(value = "水痘1剂次名称")
    private String varFirName;

    @ApiModelProperty(value = "水痘2")
    private String varSec;

    @ApiModelProperty(value = "水痘2剂次")
    private String varSecId;

    @ApiModelProperty(value = "水痘2剂次名称")
    private String varSecName;

    @ApiModelProperty(value = "麻疹")
    private String measles;

    @ApiModelProperty(value = "麻疹剂次")
    private String measlesId;

    @ApiModelProperty(value = "麻疹剂次名称")
    private String measlesName;

    @Override
    public Object clone() {
        StudentInfo result = null;
        try {
            result = (StudentInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return result;
    }

}
