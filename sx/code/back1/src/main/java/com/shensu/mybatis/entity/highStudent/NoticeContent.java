package com.shensu.mybatis.entity.highStudent;

import lombok.Data;

@Data
public class NoticeContent {
    private String stuParentname ;//父母清明
    private String schName ;//学校
    private String claName ;//班级
    private String claShortname;//班级名字
    private String claLevel ;//年级
    private String schType ;//学校类型
    private String stuName ;//学生名字
    private String stuBirth ;//生日
    private String stuFathername ;//父亲姓名
    private String stuMothername ;//母亲姓名
    private String stationName ;//地区名称
    private String stationAddress ;
    private String linkPhone;
    private String firstDose;//第一针（0 已种   1：到期未中  ）
    private String secondDose;//第二剂次（0：已种  1到期未中）


}
