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
@ApiModel("儿童信息实体类")
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class Student implements Cloneable, java.io.Serializable {

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

    @ApiModelProperty(value = "乡镇编码")
    protected String stuStreetcode;                              //乡镇编码

    @ApiModelProperty(value = "接种单位编码")
    protected String stuStationcode;                             //接种单位编码

    @ApiModelProperty(value = "归属接种单位编码")
    protected String stuBelongStationcode;


    @ApiModelProperty(value = "增加学生时间")
    protected String stuAddtime;                                 //增加学生时间
    @ApiModelProperty(value = "记录更新时间")
    protected String stuUpdatetime;                              //记录更新时间
    @ApiModelProperty(value = "数据来源", example = "0信息扫码、1excel导入、2校医编辑")
    protected String stuSourceid;                                //数据来源	0信息扫码、1excel导入、2校医编辑、3学籍系统
    @ApiModelProperty(value = "接种是否完成", example = "0完成1未完成 用于统计")
    protected String stuIsjz;                                    //接种是否完成	0完成1未完成 用于统计
    @ApiModelProperty(value = "补种是否完成", example = "0完成1未完成")
    protected String stuIsfull;                                  //补种是否完成	0完成1未完成 用于统计
    @ApiModelProperty(value = "接种是否完成(初次或年度查验的状态)", example = "0完成1未完成")
    protected String stuIsjzOrigin;                            //接种是否完成(初次或年度查验的状态)	0完成1未完成 用于统计
    @ApiModelProperty(value = "补种是否完成(初次或年度查验的状态)", example = "0完成1未完成")
    protected String stuIsfullOrigin;                          //补种是否完成(初次或年度查验的状态)	0完成1未完成 用于统计

    //疫苗
    @ApiModelProperty(value = "卡介苗")
    protected String b001;                                       //卡介苗
    @ApiModelProperty(value = "乙肝1")
    protected String b063;                                       //乙肝1
    @ApiModelProperty(value = "乙肝2")
    protected String b064;                                       //乙肝2
    @ApiModelProperty(value = "乙肝3")
    protected String b065;                                       //乙肝3
    @ApiModelProperty(value = "脊灰减毒1")
    protected String b009;                                       //脊灰减毒1
    @ApiModelProperty(value = "脊灰减毒2")
    protected String b010;                                       //脊灰减毒2
    @ApiModelProperty(value = "脊灰减毒3")
    protected String b011;                                       //脊灰减毒3
    @ApiModelProperty(value = "脊灰减毒4")
    protected String b012;                                       //脊灰减毒4
    @ApiModelProperty(value = "百白破1")
    protected String b015;                                       //百白破1
    @ApiModelProperty(value = "百白破2")
    protected String b016;                                       //百白破2
    @ApiModelProperty(value = "百白破3")
    protected String b017;                                       //百白破3
    @ApiModelProperty(value = "百白破4")
    protected String b018;                                       //百白破4
    @ApiModelProperty(value = "白破1")
    protected String b037;                                       //白破1
    @ApiModelProperty(value = "白破2")
    protected String b038;                                       //白破2
    @ApiModelProperty(value = "白破3")
    protected String b039;                                       //白破3
    @ApiModelProperty(value = "麻风1")
    protected String b093;                                       //麻风1
    @ApiModelProperty(value = "麻风2")
    protected String b094;                                       //麻风2
    @ApiModelProperty(value = "A群流脑1")
    protected String b040;                                       //A群流脑1
    @ApiModelProperty(value = "A群流脑2")
    protected String b041;                                       //A群流脑2
    @ApiModelProperty(value = "A+C群流脑1")
    protected String b045;                                       //A+C群流脑1
    @ApiModelProperty(value = "A+C群流脑2")
    protected String b046;                                       //A+C群流脑2
    @ApiModelProperty(value = "乙脑减毒1")
    protected String b032;                                       //乙脑减毒1
    @ApiModelProperty(value = "乙脑减毒2")
    protected String b033;                                       //乙脑减毒2
    @ApiModelProperty(value = "乙脑减毒3")
    protected String b034;                                       //乙脑减毒3
    @ApiModelProperty(value = "乙脑减毒4")
    protected String b035;                                       //乙脑减毒4
    @ApiModelProperty(value = "乙脑减毒5")
    protected String b036;                                       //乙脑减毒5
    @ApiModelProperty(value = "甲肝减毒1")
    protected String b069;                                       //甲肝减毒1
    @ApiModelProperty(value = "甲肝减毒2")
    protected String b070;                                       //甲肝减毒2
    @ApiModelProperty(value = "甲肝减毒3")
    protected String b071;                                       //甲肝减毒3
    @ApiModelProperty(value = "甲肝减毒4")
    protected String b072;                                       //甲肝减毒4

    @ApiModelProperty(value = "水痘1")
    protected String b050;                                       //水痘1
    @ApiModelProperty(value = "水痘2")
    protected String b132;                                       //水痘2

    @ApiModelProperty(value = "脊灰灭活1")
    protected String b169;                                       //脊灰灭活1
    @ApiModelProperty(value = "脊灰灭活2")
    protected String b170;                                       //脊灰灭活2
    @ApiModelProperty(value = "脊灰灭活3")
    protected String b171;                                       //脊灰灭活3
    @ApiModelProperty(value = "脊灰灭活4")
    protected String b172;                                       //脊灰灭活4
    @ApiModelProperty(value = "五联1")
    protected String b163;                                       //五联1
    @ApiModelProperty(value = "五联2")
    protected String b164;                                       //五联2
    @ApiModelProperty(value = "五联3")
    protected String b165;                                       //五联3
    @ApiModelProperty(value = "五联4")
    protected String b166;                                       //五联4
    @ApiModelProperty(value = "四联苗1")
    protected String b157;                                       //四联苗1
    @ApiModelProperty(value = "四联苗2")
    protected String b158;                                       //四联苗2
    @ApiModelProperty(value = "四联苗3")
    protected String b159;                                       //四联苗3
    @ApiModelProperty(value = "四联苗4")
    protected String b160;                                       //四联苗4
    @ApiModelProperty(value = "乙脑灭活1")
    protected String b027;                                       //乙脑灭活1
    @ApiModelProperty(value = "乙脑灭活2")
    protected String b028;                                       //乙脑灭活2
    @ApiModelProperty(value = "乙脑灭活3")
    protected String b029;                                       //乙脑灭活3
    @ApiModelProperty(value = "乙脑灭活4")
    protected String b030;                                       //乙脑灭活4
    @ApiModelProperty(value = "乙脑灭活5")
    protected String b031;                                       //乙脑灭活5

    @ApiModelProperty(value = "A+C结合流脑1")
    protected String b138;                                       //A+C结合流脑1
    @ApiModelProperty(value = "A+C结合流脑2")
    protected String b139;                                       //A+C结合流脑2
    @ApiModelProperty(value = "A+C结合流脑3")
    protected String b140;                                       //A+C结合流脑3
    @ApiModelProperty(value = "A+C结合流脑4")
    protected String b141;                                       //A+C结合流脑4
    @ApiModelProperty(value = "ACYW流脑1")
    protected String b147;                                       //ACYW流脑1
    @ApiModelProperty(value = "ACYW流脑2")
    protected String b148;                                       //ACYW流脑2
    @ApiModelProperty(value = "甲肝灭活1")
    protected String b073;                                       //甲肝灭活1
    @ApiModelProperty(value = "甲肝灭活2")
    protected String b074;                                       //甲肝灭活2
    @ApiModelProperty(value = "甲肝灭活3")
    protected String b075;                                       //甲肝灭活3
    @ApiModelProperty(value = "甲肝灭活4")
    protected String b076;                                       //甲肝灭活4

    @ApiModelProperty(value = "甲乙肝1")
    protected String b133;                                       //甲乙肝1
    @ApiModelProperty(value = "甲乙肝2")
    protected String b327;                                       //甲乙肝2
    @ApiModelProperty(value = "青少年百白破1")
    protected String b155;                                       //青少年百白破1
    @ApiModelProperty(value = "麻疹1")
    protected String b059;                                       //麻疹1
    @ApiModelProperty(value = "麻疹2")
    protected String b060;                                       //麻疹2
    @ApiModelProperty(value = "麻腮风1")
    protected String b089;                                       //麻腮风1
    @ApiModelProperty(value = "麻腮风2")
    protected String b090;                                       //麻腮风2
    @ApiModelProperty(value = "麻腮1")
    protected String b096;                                       //麻腮1
    @ApiModelProperty(value = "麻腮2")
    protected String b097;                                       //麻腮2


    @ApiModelProperty(hidden = true)
    protected String FPPDW;                                      //FPPDW
    @ApiModelProperty(hidden = true)
    protected String FPPDH;                                      //FPPDH

    @ApiModelProperty(hidden = true)
    protected String rowKeyFiled      = "rowkey";                //主键字段名
    @ApiModelProperty(hidden = true)
    protected String tablename        = "NUR_STUINFO";           //表名
    @ApiModelProperty(hidden = true)
    protected String childtablename   = "EPI_CHILDINFO";

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
    protected String stuMotherPhone;//母亲号码

    @ApiModelProperty(value = "其它电话、固定电话")
    protected String stuOtherphone;                              //其它电话、固定电话

    @ApiModelProperty(value = "其它联系人")
    protected String stuOtherLinkMan;


    @ApiModelProperty(value = "是否有补种操作", example = "0有1无")
    private String isbzoper;                                   //是否有补种操作 0有1无
    @ApiModelProperty(hidden = true)
    private String messageStu;
    @ApiModelProperty(hidden = true)
    private String stuindexrowkey;                             //保存学生索引表，rowkey字段
    @ApiModelProperty(value = "学校主键")
    private String schid;                                      //学校rowkey
    @ApiModelProperty(value = "学校名称")
    private String schName;                                    //学校名称
    @ApiModelProperty(hidden = true)
    private int stuexcelindex;                              //供导入列表使用，显示导入学生序号，仅页面使用
    @ApiModelProperty(value = "扫码日期")
    private String stuSmrq;                                   //扫码日期
    @ApiModelProperty(value = "上次扫码日期")
    private String lastStuSmrq;                              //上次扫码日期
    @ApiModelProperty(value = "初次扫码日期")
    private String firstStuSmrq;                              //初次扫码日期
    @ApiModelProperty(value = "上次扫码学校")
    private String lastStuSchname;                           //上次扫码学校

    //用于展示的字段
    //乙肝疫苗
    @ApiModelProperty(hidden = true)
    private String d063;
    @ApiModelProperty(hidden = true)
    private String d064;
    @ApiModelProperty(hidden = true)
    private String d065;

    //卡介苗
    @ApiModelProperty(hidden = true)
    private String d001;

    //脊灰疫苗
    @ApiModelProperty(hidden = true)
    private String d009;
    @ApiModelProperty(hidden = true)
    private String d010;
    @ApiModelProperty(hidden = true)
    private String d011;
    @ApiModelProperty(hidden = true)
    private String d012;

    //百白破疫苗
    @ApiModelProperty(hidden = true)
    private String d015;
    @ApiModelProperty(hidden = true)
    private String d016;
    @ApiModelProperty(hidden = true)
    private String d017;
    @ApiModelProperty(hidden = true)
    private String d018;

    //白破疫苗
    @ApiModelProperty(hidden = true)
    private String d037;

    //麻疹类疫苗
    @ApiModelProperty(hidden = true)
    private String d059;
    @ApiModelProperty(hidden = true)
    private String d060;

    //[流脑疫苗]
    //A群流脑疫苗
    @ApiModelProperty(hidden = true)
    private String d040;
    @ApiModelProperty(hidden = true)
    private String d041;

    //A+C群流脑疫苗
    @ApiModelProperty(hidden = true)
    private String d045;
    @ApiModelProperty(hidden = true)
    private String d046;

    //乙脑疫苗
    @ApiModelProperty(hidden = true)
    private String d032;
    @ApiModelProperty(hidden = true)
    private String d033;

    //甲肝疫苗
    @ApiModelProperty(hidden = true)
    private String d073;
    @ApiModelProperty(hidden = true)
    private String d074;
    //水痘疫苗
    @ApiModelProperty(hidden = true)
    private String d050;
    @ApiModelProperty(hidden = true)
    private String d132;
    //暂时没用到的
    private String d069;
    private String B069State;
    private String B034State;
    private String B031State;
    private String B035State;
    private String B036State;
    private String B070State;
    private String B072State;
    private String B075State;
    private String B076State;
    private String B069StateAfter;
    private String B034StateAfter;
    private String B031StateAfter;
    private String B035StateAfter;
    private String B036StateAfter;
    private String B070StateAfter;
    private String B071StateAfter;
    private String B076StateAfter;
    //标记每个剂次的接种状态
    @ApiModelProperty(hidden = true)
    protected String b063State;
    @ApiModelProperty(hidden = true)
    protected String b064State;
    @ApiModelProperty(hidden = true)
    protected String b065State;
    @ApiModelProperty(hidden = true)
    protected String b001State;
    @ApiModelProperty(hidden = true)
    protected String b009State;
    @ApiModelProperty(hidden = true)
    protected String b010State;
    @ApiModelProperty(hidden = true)
    protected String b011State;
    @ApiModelProperty(hidden = true)
    protected String b012State;
    @ApiModelProperty(hidden = true)
    protected String b015State;
    @ApiModelProperty(hidden = true)
    protected String b016State;
    @ApiModelProperty(hidden = true)
    protected String b017State;
    @ApiModelProperty(hidden = true)
    protected String b018State;
    @ApiModelProperty(hidden = true)
    protected String b037State;
    @ApiModelProperty(hidden = true)
    protected String b038State;
    @ApiModelProperty(hidden = true)
    protected String b039State;
    @ApiModelProperty(hidden = true)
    protected String b059State;
    @ApiModelProperty(hidden = true)
    protected String b060State;
    @ApiModelProperty(hidden = true)
    protected String b040State;
    @ApiModelProperty(hidden = true)
    protected String b041State;
    @ApiModelProperty(hidden = true)
    protected String b045State;
    @ApiModelProperty(hidden = true)
    protected String b046State;
    @ApiModelProperty(hidden = true)
    protected String b032State;
    @ApiModelProperty(hidden = true)
    protected String b033State;
    @ApiModelProperty(hidden = true)
    protected String b073State;
    @ApiModelProperty(hidden = true)
    protected String b074State;

    @ApiModelProperty(hidden = true)
    protected String b050State;
    @ApiModelProperty(hidden = true)
    protected String b132State;

    //标记每个剂次的接种状态 ,此为修改数据后的各个剂次状态。
    @ApiModelProperty(hidden = true)
    protected String b063StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b064StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b065StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b001StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b009StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b010StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b011StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b012StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b015StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b016StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b017StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b018StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b037StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b038StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b039StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b059StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b060StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b040StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b041StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b045StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b046StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b032StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b033StateAfter;

    @ApiModelProperty(hidden = true)
    protected String b073StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b074StateAfter;

    @ApiModelProperty(hidden = true)
    protected String b050StateAfter;
    @ApiModelProperty(hidden = true)
    protected String b132StateAfter;

    @ApiModelProperty(value = "补种疫苗名称")
    private String needReplantRoster;

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

    @ApiModelProperty(value = "本次流程是否被查验过", hidden = true)
    private transient Boolean isChecked = false; //1是, 0否


    @Override
    public Object clone() {
        Student result = null;
        try {
            result = (Student) super.clone();
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
    public  static  String  changeStuType(String typeCode){
        String str =null;
        if("1".equals(typeCode)){
            str="县区内流动";
        }else if("3".equals(typeCode)){
            str="市地内流动";
        }else if("4".equals(typeCode)){
            str="本省内流动";
        }else if("5".equals(typeCode)){
            str="省际内流动";
        }else if("6".equals(typeCode)){
            str="国际流动";
        }
        return str;
    }
}
