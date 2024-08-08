package com.shensu.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zcy
 * @ClassName Util
 * @Description
 * @createTime 2022/12/7
 */
public class Util {
    /*
     * 根据行政区划编码获取行政区划级别
     * zoneCode : 行政区划编码
     * 返回值 :　行政区划级别．（０－省，１－市，２－县，３－乡，４－点，5 - 村，-1 - 国家）
     *
     * */
    public static String   getZoneLevelByCode(String zoneCode) {
        String zoneLevel = "-1";

        if (zoneCode.substring(2).equals("0000000000")) //省
        {
            zoneLevel = "0";
        } else if (zoneCode.substring(4).equals("00000000")) //市
        {
            zoneLevel = "1";
        } else if (zoneCode.substring(6).equals("000000")) //县
        {
            zoneLevel = "2";
        } else if (zoneCode.substring(8).equals("0000")) //乡
        {
            zoneLevel = "3";
        } else if (zoneCode.substring(10).equals("00")) //点
        {
            zoneLevel = "4";
        }
        return zoneLevel;
    }

    /**
     * 将日期转换到指定格式的字符串
     * @param date
     * @param string
     * @return
     */
    public static String getFixedDate(Date date, String string) {

        SimpleDateFormat sdf = new SimpleDateFormat(string);
        return sdf.format(date);
    }

    /**
     * 将日期转换到指定格式的字符串
     * @param src
     * @param formatString
     * @return
     * @throws ParseException
     */
    public static Date getDateByString(String src, String formatString) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(formatString);

        return sdf.parse(src);
    }

    /**
     * 计算百分比
     * @param number
     * @param totalNum
     * @param scale
     * @return
     */
    public static String accuracyInt(Integer number, Integer totalNum, int scale){
        if (number == null || totalNum == null || totalNum == 0 || number == 0) {
            return "0.00%";
        }
        Double num = number.doubleValue();
        Double total = totalNum.doubleValue();

        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 （四舍五入）
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracyNum = num / total * 100;
        return df.format(accuracyNum) + "%";
    }

    /**
     * 返回对象中 值为空的字段名
     *
     * */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 对象转集合
     * @param obj
     * @param  clazz 对象类型
     * @return ArrayList
     * */
    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
    public static int getMonthByADate(String birthDate,String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(sdf.parse(birthDate));
        c2.setTime(sdf.parse(time));
        if (c1.getTimeInMillis() <= c2.getTimeInMillis()) {
            int agemonth = 0;
            int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
            if (year > 0) {
                c1.add(Calendar.YEAR, year);
                if (c1.getTimeInMillis() > c2.getTimeInMillis()) {
                    year = year - 1;
                    c1.setTime(sdf.parse(birthDate));
                    c1.add(Calendar.YEAR, year);
                }
                if (year > 0) {
                    agemonth = year * 12;
                }
            }
            int month = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
            if (month <= 0) {
                if (month == 0) {
                    if (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR) > 0) {
                        month = (12 - c1.get(Calendar.MONTH)) + c2.get(Calendar.MONTH);
                    }
                } else {
                    month = (12 - c1.get(Calendar.MONTH)) + c2.get(Calendar.MONTH);
                }
            }
            c1.add(Calendar.MONTH, month);
            if (c1.getTimeInMillis() > c2.getTimeInMillis()) {
                month = month - 1;
                c1.setTime(sdf.parse(birthDate));
                c1.add(Calendar.MONTH, month);
            }
            if (month > 0) {
                agemonth += month;
            }
            return agemonth;
        }
        return 0;
    }

}
