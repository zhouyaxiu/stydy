package com.shensu.util;


import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * @Fields LOGGER : 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm"};

    public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate(String updateDate, String s) {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }


    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm）
     */
    public static String formatDateMinute(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd）
     */
    public static String formatDate(Date date) {
        if (date != null) {
            return formatDate(date, "yyyy-MM-dd");
        }
        return "";
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当天字符串 格式（yyyyMMdd）
     */
    public static String getYmd() {
        return formatDate(new Date(), "yyyyMMdd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDateToFormat(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return parseDate(obj.toString(), "yyyy-MM-dd");
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    public static Date getDateStart(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateEnd(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getMonthStartStr(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return formatDate(calendar.getTime(), pattern);
    }

    public static Date getMonthStartDate(Date date) {
        String monthStartStr = getMonthStartStr(date, null);
        return parseDate(monthStartStr);
    }

    public static String getMonthEndStr(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);     //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        return formatDate(calendar.getTime(), pattern);
    }

    public static Date getMonthEndDate(Date date) {
        String monthEndStr = getMonthEndStr(date, null);
        return parseDate(monthEndStr);
    }


    /**
     * 获取日期所在年的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayDateOfYear(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = cal.getActualMinimum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, last);
        return cal.getTime();
    }

    public static String getFirstDayDateOfYear(Date date, String pattern) {
        Date firstDay = getFirstDayDateOfYear(date);
        return formatDate(firstDay, pattern);
    }

    /**
     * 获取年的日期所在年的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfYear(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, last);
        return cal.getTime();
    }

    public static String getLastDayOfYear(Date date, String pattern) {
        Date lastDay = getLastDayOfYear(date);
        return formatDate(lastDay, pattern);
    }

    /**
     * 判断字符串是否是日期
     *
     * @param timeString
     * @return
     */
    public static boolean isDate(String timeString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        try {
            format.parse(timeString);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 格式化时间
     *
     * @param timestamp
     * @return
     */
    public static String dateFormat(Date timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }

    /**
     * 格式化时间
     *
     * @param timestamp
     * @return
     */
    public static String dateFormat(Date timestamp, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(timestamp);
    }

    /**
     * 获取系统时间Timestamp
     *
     * @return
     */
    public static Timestamp getSysTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * 获取系统时间Date
     *
     * @return
     */
    public static Date getSysDate() {
        return new Date();
    }

    /**
     * 生成时间随机数
     *
     * @return
     */
    public static String getDateRandom() {
        String s = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        return s;
    }

    /**
     * 〈简述〉转日期
     * 〈详细描述〉
     *
     * @param dateStr String
     * @return Date
     * @author gelongyu
     */
    public static Date toDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 〈简述〉转日期
     * 〈详细描述〉
     *
     * @param dateStr String
     * @return Date
     * @author gelongyu
     */
    public static Date toDateByRule(String dateStr, String rule) {
        SimpleDateFormat sdf = new SimpleDateFormat(rule);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static Date toDateFull(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date toDateHalf(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    //格式化
    public static String formatDateFullStr(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatTime;
        try {
            Date date = sdf.parse(dateStr);
            formatTime = sdf.format(date);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
            formatTime = null;
        }
        return formatTime;
    }

    //格式化
    public static String formatDateFullStr(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatTime;
        try {
            Date date = sdf.parse(dateStr);
            formatTime = sdf.format(date);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
            formatTime = null;
        }
        return formatTime;
    }

    public static String formatDateFullStr(String dateStr, String pattern,String pattern2) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        SimpleDateFormat sdf2 = new SimpleDateFormat(pattern2);
        String formatTime;
        try {
            Date date = sdf.parse(dateStr);
            formatTime = sdf2.format(date);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
            formatTime = null;
        }
        return formatTime;
    }

    public static Date toDateFull1(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当前时间 格式yyyy-MM-dd
     *
     * @return
     */
    public static Date getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(sdf.format(getSysDate()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String getPastDate(String oldDateStr, String intervalDay) {
        if (!StringUtil.isEmpty(intervalDay) && !StringUtil.isEmpty(oldDateStr)) {
            Calendar calendar = new GregorianCalendar();
            try {
                calendar.setTime(dateformat.parse(oldDateStr));
                calendar.add(GregorianCalendar.DATE, (-1) * Integer.parseInt(intervalDay));
                return dateformat.format(calendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 日期比较
     * s1 : 日期1，默认格式yyyy-mm-dd
     * s2 : 日期2，默认格式yyyy-mm-dd
     * 返回值 : true表示s1>s2 , false表示s1<=s2
     * */
    public static boolean compareDate(String s1, String s2) throws ParseException {
        boolean flag = false;
        Date d1 = dateformat.parse(s1);
        Date d2 = dateformat.parse(s2);

        if (d1.compareTo(d2) > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 〈简述〉两个日期相差几个月(可跨年)
     * 〈详细描述〉
     *
     * @param startDate 时间1
     * @param endDate   时间2
     * @return 相差几个月份
     * @author gelongyu
     */
    public static long MonthCompare(String startDate, String endDate) {
        Temporal temporal1 = LocalDate.parse(startDate);
        Temporal temporal2 = LocalDate.parse(endDate);
        // 方法返回为相差月份
        long month = ChronoUnit.MONTHS.between(temporal1, temporal2);
        return month;
    }

    /**
     * 〈简述〉两个日期相差几天(可跨年)
     * 〈详细描述〉
     *
     * @param startDate 时间1
     * @param endDate   时间2
     * @return 相差几天
     * @author gelongyu
     */
    public static long dayCompare(String startDate, String endDate) {
        Temporal temporal1 = LocalDate.parse(startDate);
        Temporal temporal2 = LocalDate.parse(endDate);
        // 方法返回为相差月份
        long day = ChronoUnit.DAYS.between(temporal1, temporal2);
        return day;
    }

    /**
     * 〈简述〉在给定的日期上添加月份
     * 〈详细描述〉
     *
     * @param sourceDate 时间
     * @param month   增大的月份
     * @return 相差几天
     * @author gelongyu
     */
    public static String stepMonth(String sourceDate, int month) {
        Calendar c = Calendar.getInstance();
        Date toDate = toDate(sourceDate);
        c.setTime(toDate);
        c.add(Calendar.MONTH, month);
        return formatDate(c.getTime());
    }
}
