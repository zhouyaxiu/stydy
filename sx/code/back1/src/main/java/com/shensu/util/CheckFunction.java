package com.shensu.util;

import com.shensu.mybatis.entity.highStudent.HighStudCheck;
import com.shensu.mybatis.entity.highStudent.HighStudent;

import java.util.Calendar;
import java.util.Date;

public class CheckFunction {
    /**
     * 查验算法：当前年份接种过麻疹就是已种，当前年份没有接种麻疹就是未中
     * */
    public static  void  checkMaZhen(HighStudent student, HighStudCheck checkStu , String startTime , String endTime){
        String maZhen  = student.getMmr();
        if(StringUtils.isEmpty(maZhen)){
            checkStu.setFirstDose("1");
        }else{
            if(DateUtils.isDate(maZhen)){
                //  boolean reslut =  DateUtils.isEffectiveDate(DateUtils.toDate(maZhen),DateUtils.toDate(startTime),DateUtils.toDate(endTime));
                String nowYear = DateUtils.getYear();
                String jieZhong = maZhen.split("-")[0];
                if(nowYear.equals(jieZhong)){
                    checkStu.setFirstDose("0");
                }else{
                    checkStu.setFirstDose("1");
                }
            }}
    }
    /**
     * 到目前为止：1针也没接种 第一针到期未中 第二针未到接种时间
     *           1针接种，第二针没接种，那么第二针分为两种情况，如果是当前时间和第一针时间间隔小于三个月，第二针未中接种时间  间隔大于三个月，第二针到期未中
     *
     *           0 已种   -1 未到接种时间   1:到期未中
     * */
    public static  void  shuiDou(HighStudent stu,HighStudCheck highStudent){
        String shuiDou1 =stu.getVarOne();
        String shuiDou2 = stu.getVarTwo();
        if(StringUtils.isBlank(shuiDou1)&&StringUtils.isBlank(shuiDou2)){
            highStudent.setFirstDose("1");
            highStudent.setSecondDose("-1");
        }else if(StringUtils.isNotBlank(shuiDou1)){
            if(DateUtils.isDate(shuiDou1)){
                if(StringUtils.isNotBlank(shuiDou2) && DateUtils.isDate(shuiDou2)){
                    highStudent.setFirstDose("0");
                    highStudent.setSecondDose("0");
                }else{
                    highStudent.setFirstDose("0");
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.MONTH, -3);
                    if(DateUtils.dateFormat(now.getTime()).compareTo(shuiDou1) <= 0){
                        highStudent.setSecondDose("-1");
                    }else{
                        highStudent.setSecondDose("1");
                    }
                }
            }
        }
    }
    /**
     * 满3周岁在校、在园儿童，在今年流感季（流感季：去年8月到今年7月是上一个流感季，今年8月到明年7月是这个流感季）未接种过流感疫苗，
     * */
    public  static   void  liuGan(HighStudent stu,HighStudCheck highStudent) throws Exception {
        String nowYear  = DateUtils.getMonth();
        String nowDate = DateUtils.getDate();
        String liuGan1 = stu.getFluOne();

        if(StringUtils.isEmpty(liuGan1)){
            highStudent.setFirstDose("1");
        }else {

            Date liuGan1Date  = DateUtils.toDate(liuGan1);

            Date beforeDate=null;
            Date endDate=null;
            //上一个流感季节
            if("7".compareTo(nowYear)<0){
                String[] dates = nowDate.split("-");
                int year  = Integer.parseInt(dates[0]);
                String beforeTime  = year-1+"-"+"08-01";
                String endTime =year+"-07-31";
                beforeDate=DateUtils.toDate(beforeTime);
                endDate = DateUtils.toDate(endTime);
            }else{
                //当前流感季
                String[] dates = nowDate.split("-");
                int year  = Integer.parseInt(dates[0]);
                String beforeTime  = year+"-"+"08-01";
                String endTime =year+1+"-07-31";
                beforeDate  =DateUtils.toDate(beforeTime);
                endDate = DateUtils.toDate(endTime);
            }
            if (DateUtils.isEffectiveDate(liuGan1Date, beforeDate, endDate)) {
                highStudent.setFirstDose("0");
            } else {
                highStudent.setFirstDose("1");
            }
        }
    }
}
