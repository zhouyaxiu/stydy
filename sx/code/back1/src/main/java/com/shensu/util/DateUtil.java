package com.shensu.util;

import cn.hutool.core.date.DateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 使用ThreadLocal以空间换时间解决SimpleDateFormat线程安全问题。
 *
 */
public class DateUtil {

	private static Logger logger = LogManager.getLogger(DateUtil.class);

	/**
	 * parsePatterns
	 */
	private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy年MM月dd日",
			"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd",
			"yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S", "yyyy-MM-dd HH:mm:ss.SSS",
			"yyyy/MM/dd HH:mm", "yyyyMMdd"};

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_FORMAT_SHOT = "yyyy-MM-dd";

    private static final String DATE_FORMAT_THIN = "yyyyMMdd";

    private static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSS";

    @SuppressWarnings("rawtypes")
    private static ThreadLocal threadLocal = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(DATE_FORMAT);
        }
    };

    public static String getCurTime(){
    	SimpleDateFormat dateformat1 = new SimpleDateFormat(DATE_FORMAT);
		Calendar  calendar = new GregorianCalendar();
		return dateformat1.format(calendar.getTime());
	}

    public static DateFormat getDateFormat() {
        return (DateFormat) threadLocal.get();
    }

    public static Date parse(String textDate) throws ParseException {
        return getDateFormat().parse(textDate);
    }
    public static String format(Date date) throws ParseException {
        return getDateFormat().format(date);
    }

    public static String format(String date) {//将日期字符串转换成指定格式的日期字符串
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	String formatDateString = "";
		if (StringUtils.isNotBlank(date)) {
			try {
				formatDateString = dateFormat.format(dateFormat.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			formatDateString = "";
		}
		return formatDateString;
    }

	public static String formatCommon(Date date) {
		String dateStr = cn.hutool.core.date.DateUtil.format(date, DATE_FORMAT);
		return dateStr;
	}

	public static String formatShort(Date date) {
		String dateStr = cn.hutool.core.date.DateUtil.format(date, DATE_FORMAT_SHOT);
		return dateStr;
	}

	public static String formatThin(Date date) {
		String dateStr = cn.hutool.core.date.DateUtil.format(date, DATE_FORMAT_SHOT);
		return dateStr;
	}

	public static String formatFull(Date date) {
		String dateStr = cn.hutool.core.date.DateUtil.format(date, DATE_FORMAT_FULL);
		return dateStr;
	}

	public static String formatShort(String dateStr) {
		Date date = parseDate(dateStr);
		try {
			dateStr = cn.hutool.core.date.DateUtil.format(date, DATE_FORMAT_SHOT);
		} catch (Exception e) {
			dateStr = null;
		}
		return dateStr;
	}

	public static String formatThin(String dateStr) {
		Date date = parseDate(dateStr);
		try {
			dateStr = cn.hutool.core.date.DateUtil.format(date, DATE_FORMAT_THIN);
		} catch (Exception e) {
			dateStr = null;
		}
		return dateStr;
	}

    public static String format(String date , String parten) {//将日期字符串转换成指定格式的日期字符串
    	if(StringUtils.isBlank(parten)) {
    		parten = DATE_FORMAT;
    	}
    	SimpleDateFormat dateFormat = new SimpleDateFormat(parten);
    	String formatDateString = "";
		if (StringUtils.isNotBlank(date)) {
			Date parsedDate = null;
			try {
				if(date.length() == 14) {
					dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					parsedDate = dateFormat.parse(date);
				} else {
					dateFormat = new SimpleDateFormat(parten);
					parsedDate = dateFormat.parse(date);
				}
				dateFormat = new SimpleDateFormat(parten);

				formatDateString = dateFormat.format(parsedDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			formatDateString = "";
		}
		return formatDateString;
    }

	public static Date parseDate(String dateStr) {
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}
		try {
			return cn.hutool.core.date.DateUtil.parse(dateStr, parsePatterns);
		} catch (DateException e) {
			//logger.error("Unable to parse the date");
			//System.out.println("Unable to parse the date");
			return null;
		}
	}

	/**
	 * 是否为合法的日期
	 * @param dateStr
	 * @return
	 */
	public static boolean isDate(String dateStr) {
		return parseDate(dateStr) != null;
	}

	/**
	 * 日期排序
	 * @param list
	 */
	public static void sort(List<Date> list) {
		Collections.sort(list, new Comparator<Date>() {
			@Override
			public int compare(Date dt1, Date dt2) {
				return dt1.compareTo(dt2);
			}
		});
	}

	/**
	 * 日期排序
	 * @param dateArray
	 */
	public static Date[] sortWithNull(Date[] dateArray) {
		List<Date> list = new ArrayList<>();
		for (Date date: dateArray) {
			if (date != null && !list.contains(date)) {
				list.add(date);
			}
		}
		if (CollectionUtils.isEmpty(list)) {
			return dateArray;
		}
		Collections.sort(list, new Comparator<Date>() {
			@Override
			public int compare(Date dt1, Date dt2) {
				return dt1.compareTo(dt2);
			}
		});

		if (list.size() < dateArray.length) {
			int size = dateArray.length - list.size();
			for (int i = 0; i < size; i++) {
				list.add(null);
			}
		}
		return list.toArray(new Date[dateArray.length]);
	}

	/**
	 * 日期排序
	 * @param dateArray
	 */
	public static void sort(Date[] dateArray) {
		List<Date> list = Arrays.asList(dateArray);
		//List<Date> list = new ArrayList<>(Arrays.asList(dateArray));
		Collections.sort(list, new Comparator<Date>() {
			@Override
			public int compare(Date dt1, Date dt2) {
				return dt1.compareTo(dt2);
			}
		});
	}
}
