package com.shensu.util;

import com.shensu.mybatis.entity.inforInspection.UpdateStudentProperties;
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

    /**
     * 1表示不更新的字段  0表示要更新且非日期的字段 2表示要更新且日期的字段
     * 晚上定点更新的字段
     */
    public final static List<UpdateStudentProperties> AtNightUpdateStudent = new ArrayList<UpdateStudentProperties>() {
        {
            add(new UpdateStudentProperties("stuName", "受种者姓名", 0));
            add(new UpdateStudentProperties("stuSex", "性别", 0));
            add(new UpdateStudentProperties("stuIsbjzz", "接种证", 0));
            add(new UpdateStudentProperties("stuBirth", "出生日期", 2));
            add(new UpdateStudentProperties("stuType", "户籍类型", 0));
            add(new UpdateStudentProperties("stuParentname", "父亲/母亲/监护人姓名", 0));
            add(new UpdateStudentProperties("stuBelongStationcode", "归属接种单位编码", 0));
            add(new UpdateStudentProperties("stuFathername", "父亲姓名", 0));
            add(new UpdateStudentProperties("stuFatherPhone", "父亲号码", 0));
            add(new UpdateStudentProperties("stuMothername", "父亲姓名", 0));
            add(new UpdateStudentProperties("stuMotherPhone", "母亲号码", 0));
            //			add(new UpdateStudentProperties("stu_phone","移动电话",0));
            //			add(new UpdateStudentProperties("stu_otherphone","其他电话",0));
            add(new UpdateStudentProperties("b001", "卡介苗", 2));
            add(new UpdateStudentProperties("b063", "乙肝1", 2));
            add(new UpdateStudentProperties("b064", "乙肝2", 2));
            add(new UpdateStudentProperties("b065", "乙肝3", 2));
            add(new UpdateStudentProperties("b009", "脊灰减毒1", 2));
            add(new UpdateStudentProperties("b010", "脊灰减毒2", 2));
            add(new UpdateStudentProperties("b011", "脊灰减毒3", 2));
            add(new UpdateStudentProperties("b012", "脊灰减毒4", 2));
            add(new UpdateStudentProperties("b015", "百白破1", 2));
            add(new UpdateStudentProperties("b016", "百白破2", 2));
            add(new UpdateStudentProperties("b017", "百白破3", 2));
            add(new UpdateStudentProperties("b018", "百白破4", 2));
            add(new UpdateStudentProperties("b037", "白破1", 2));
            add(new UpdateStudentProperties("b038", "白破2", 2));
            add(new UpdateStudentProperties("b039", "白破3", 2));
            add(new UpdateStudentProperties("b059", "麻疹1", 2));
            add(new UpdateStudentProperties("b060", "麻疹2", 2));
            add(new UpdateStudentProperties("b040", "A群流脑1", 2));
            add(new UpdateStudentProperties("b041", "A群流脑2", 2));
            add(new UpdateStudentProperties("b045", "A+C群流脑1", 2));
            add(new UpdateStudentProperties("b046", "A+C群流脑2", 2));
            add(new UpdateStudentProperties("b032", "乙脑减毒1", 2));
            add(new UpdateStudentProperties("b033", "乙脑减毒2", 2));
            add(new UpdateStudentProperties("b034", "乙脑减毒3", 2));
            add(new UpdateStudentProperties("b035", "乙脑减毒4", 2));
            add(new UpdateStudentProperties("b036", "乙脑减毒5", 2));
            add(new UpdateStudentProperties("b069", "甲肝减毒1", 2));
            add(new UpdateStudentProperties("b070", "甲肝减毒2", 2));
            add(new UpdateStudentProperties("b071", "甲肝减毒3", 2));
            add(new UpdateStudentProperties("b072", "甲肝减毒4", 2));
            add(new UpdateStudentProperties("b050", "水痘1", 2));
            add(new UpdateStudentProperties("b132", "水痘2", 2));
            add(new UpdateStudentProperties("b169", "脊灰灭活1", 2));
            add(new UpdateStudentProperties("b170", "脊灰灭活2", 2));
            add(new UpdateStudentProperties("b171", "脊灰灭活3", 2));
            add(new UpdateStudentProperties("b172", "脊灰灭活4", 2));
            add(new UpdateStudentProperties("b163", "五联苗1", 2));
            add(new UpdateStudentProperties("b164", "五联苗2", 2));
            add(new UpdateStudentProperties("b165", "五联苗3", 2));
            add(new UpdateStudentProperties("b166", "五联苗4", 2));
            add(new UpdateStudentProperties("b157", "四联苗1", 2));
            add(new UpdateStudentProperties("b158", "四联苗2", 2));
            add(new UpdateStudentProperties("b159", "四联苗3", 2));
            add(new UpdateStudentProperties("b160", "四联苗4", 2));
            add(new UpdateStudentProperties("b027", "乙脑灭活1", 2));
            add(new UpdateStudentProperties("b028", "乙脑灭活2", 2));
            add(new UpdateStudentProperties("b029", "乙脑灭活3", 2));
            add(new UpdateStudentProperties("b030", "乙脑灭活4", 2));
            add(new UpdateStudentProperties("b031", "乙脑灭活5", 2));
            add(new UpdateStudentProperties("b138", "A+C结合流脑1", 2));
            add(new UpdateStudentProperties("b139", "A+C结合流脑2", 2));
            add(new UpdateStudentProperties("b140", "A+C结合流脑3", 2));
            add(new UpdateStudentProperties("b141", "A+C结合流脑4", 2));
            add(new UpdateStudentProperties("b147", "ACYW流脑1", 2));
            add(new UpdateStudentProperties("b148", "ACYW流脑2", 2));
            add(new UpdateStudentProperties("b073", "甲肝灭活1", 2));
            add(new UpdateStudentProperties("b074", "甲肝灭活2", 2));
            add(new UpdateStudentProperties("b075", "甲肝灭活3", 2));
            add(new UpdateStudentProperties("b076", "甲肝灭活4", 2));
            add(new UpdateStudentProperties("b133", "甲乙肝1", 2));
            add(new UpdateStudentProperties("b327", "甲乙肝2", 2));
            add(new UpdateStudentProperties("b093", "麻风1", 2));
            add(new UpdateStudentProperties("b094", "麻风2", 2));
            add(new UpdateStudentProperties("b089", "麻腮风1", 2));
            add(new UpdateStudentProperties("b090", "麻腮风2", 2));
            add(new UpdateStudentProperties("b096", "麻腮1", 2));
            add(new UpdateStudentProperties("b097", "麻腮2", 2));
            add(new UpdateStudentProperties("b155", "青少年百白破1", 2));
        }
    };

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
