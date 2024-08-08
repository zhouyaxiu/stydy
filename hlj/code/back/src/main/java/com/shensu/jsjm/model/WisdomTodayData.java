package com.shensu.jsjm.model;

import lombok.Data;

/**
 * @description:todo
 * @author：GJ
 * @date：2023/11/22 15:11
 */
@Data
public class WisdomTodayData {
//    当日接种人数
    private long todayShotPeopleNumber;
//    当日接种剂次数
    private long todayShotJCNumber;
//    当日建档人数
    private long todayChildBookNumber;
//    总建档数
    private long childBookNumber;
//    0-6岁建档数
    private long childBook06Number;
//    7-18岁建档数
    private long childBook718Number;
//    18岁以上建档数
    private long childBook18Number;
//    区域编码
    private String areaCode;
//    区域编码
    private String areaName;

    public void merge(WisdomTodayData other) {
        this.todayShotPeopleNumber += other.todayShotPeopleNumber;
        this.todayShotJCNumber += other.todayShotJCNumber;
        this.todayChildBookNumber += other.todayChildBookNumber;
        this.childBookNumber += other.childBookNumber;
        this.childBook06Number += other.childBook06Number;
        this.childBook718Number += other.childBook718Number;
        this.childBook18Number += other.childBook18Number;
        this.areaCode = other.areaCode;
        this.areaName = other.areaName;
    }
}
