package com.shensu.mybatis.entity.highStudent;

import lombok.Data;

@Data
public class HighStuInfoAndCheck {
    private String  roundId;//轮次id
    private String  stuNo;//学号
    private String  roundName; //轮次名称
    private String claNameyear;//学年
    private String schName;//学校名称
    private String schId;//学校id
    private String claLevel;//年级
    private String claName;//班级
    private String stuName;//姓名
    private String cid;//身份证号码
    private String fResideAddr;//现住地址
    private String stuType;//学生类型
    private String firstDose;//第一剂次（麻腮风只有1个剂次）
    private String secondDose;//第二剂次
    private String mark;


}
