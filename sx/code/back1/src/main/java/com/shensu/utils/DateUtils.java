package com.shensu.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date getEndTimeOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }
    
    private static final String default_date_pattern = "yyyy-MM-dd";
    private static final String default_time_pattern = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat sdf = new SimpleDateFormat(default_date_pattern);
    private static SimpleDateFormat stf = new SimpleDateFormat(default_time_pattern);
    
    /**
     * 两个类之间的日期
     * @param date1
     * @param date2
     * @return
     */
    public static int monthBetween(Date date1, Date date2) {
        DateTime dt1 = new DateTime(date1);
        DateTime dt2 = new DateTime(date2);
        int year1 = dt1.getYear();
        int year2 = dt2.getYear();
        int month1 = dt1.getMonthOfYear();
        int month2 = dt2.getMonthOfYear();
        int day1 = dt1.getDayOfMonth();
        int day2 = dt2.getDayOfMonth();
        int mi1 = dt1.getMillisOfDay();
        int mi2 = dt2.getMillisOfDay();

        if (!dt1.isBefore(dt2)) {
            if ((day1 > day2) || (day1 == day2 && mi1 >= mi2)) {
                return (year1 - year2) * 12 + (month1 - month2);
            } else {
                return (year1 - year2) * 12 + (month1 - month2) - 1;
            }
        } else {
            return -1;
        }
    }
    
    public static  Date parse(String s,String pattern) {
        try {
            if(StringUtils.isEmpty(pattern)) {
                if(s.length()==default_date_pattern.length()) {
                    return sdf.parse(s);
                }else if(s.length()==default_time_pattern.length()) {
                    return stf.parse(s);
                }else {
                    return null;
                }
            }else {
                SimpleDateFormat sf = new SimpleDateFormat(pattern);
                return sf.parse(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Date d = new Date();
        long t = 92l * 24l * 3600l * 1000l - 1;
        System.out.println(monthBetween(d, new Date(d.getTime() - t)));
    }
}
