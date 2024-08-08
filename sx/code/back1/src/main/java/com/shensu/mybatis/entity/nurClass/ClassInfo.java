package com.shensu.mybatis.entity.nurClass;

import lombok.Data;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName ClassInfo.java
 * @Description 班级信息
 * @createTime 2022年08月17日 14:25
 */
@Data
public class ClassInfo implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = -5744412829132912889L;
    private String rowkey;         //班级编码 主键、唯一(10位接种单位编码+3位学校流水号+3位流班级流水号)

    private String claName;        //班级名称(包括年份 春节/秋季)
    private String claTotal;       //班级总人数
    private String claLinkman;     //联系人
    private String claPhone;       //电话
    private String claSchid;       //所属学校
    private String claAddtime;     //新增学校时间
    private String claUpdatetime;  //修改时间
    private String claNameYear;    //年份
    private String claLevel;       // 年级
    private String claNameSeason;  //季节
    private String claShortName;   //班级名称
    private String claDelete;
    private String remark;         //验证错误信息
    private String remarkLocation; //验证错误信息

    private String schName;

    private String schType;

    private String supName;

    private String stationName;

    private String stationCode;




    public static String getClaLevelName(String l) {
        if ("-1".equals(l)) {
            return "学前班";
        } else if ("0".equals(l)) {
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
        if ("学前班".equals(l)) {
            return "-1";
        }
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
}
