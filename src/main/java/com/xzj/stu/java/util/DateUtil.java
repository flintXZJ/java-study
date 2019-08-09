package com.xzj.stu.java.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String FORMAT_HHMMSS = "HHmmss";

	/**
	 * 得到日期+i天后的日期
	 * 把日期往后增加一天.整数往后推,负数往前移动
	 * @param d
	 * @param i
	 * @return
	 */
	public static Date addDay(Date d, int i) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(d);
		calendar.add(Calendar.DATE, i);// 把日期往后增加一天.整数往后推,负数往前移动
		return calendar.getTime();// 这个时间就是日期往后推一天的结果
	}
	
	/**
	 * 时间加减 Date -> String
	 * @param date
	 * @param day
	 * @param format
     * @return
     */
	public static String addDay(Date date, int day, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return formatDate2Str(calendar.getTime(), format);
	}

	/**
	 * 时间加减 String -> String
	 * @param date 
	 * @param formatstr
	 *          参数date的格式，返回的格式
	 * @param day
	 *          正数n天后，负数n天前
	 * @return
	 * @throws ParseException
	 */
	public static String addDay(String date, int day, String formatstr) {
		SimpleDateFormat format = new SimpleDateFormat(formatstr);
		//严格解析  如：33/12/2011 会报错
		format.setLenient(false);
		Date paramDate = null;
		try {
			paramDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(paramDate);
		calendar.add(Calendar.DATE, day);
		return new SimpleDateFormat(formatstr).format(calendar.getTime());
	}
	
	/**
	 * 小时 加减
	 *
	 * @param date
	 * @param hour
	 * @param format
     * @return
     */
	public static String addHours(Date date, int hour, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hour);
		return formatDate2Str(calendar.getTime(), format);
	}
	
	/**
	 * 小时加减  Date -> Date
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date addHours(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		return calendar.getTime();
	}
	
	/**
	 * 分钟 加减
	 *
	 * @param date
	 * @param minute
     * @return 返回 yyyyMMddHHmmss
     */
	public static String addMinutes2Str(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return formatDate2Str(calendar.getTime(), FORMAT_YYYYMMDDHHMMSS);
	}
	
	/**
	 * 分钟加减
	 *
	 * @param minute
	 * @return
     */
	public static Date addMinutes2Date(Date date,int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}
	
	/**
	 * 
	 * @param date
	 * @param minute
	 * @param format
	 * @return
	 */
	public static String addMinutes(Date date, int minute,String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return formatDate2Str(calendar.getTime(), format);
	}
	
	/**
	 * 分钟 加减
	 *
	 * @param dateStr
	 * @param minute
	 * @param format
     * @return Timestamp
     */
	public static Timestamp addMinutes(String dateStr, int minute, String format) {
		Date date = formatStr2Date(dateStr, format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 分钟 加减
	 *
	 * @param dateStr
	 * @param minute
     * @return Timestamp
     */
	public static Timestamp addMinutes(String dateStr, int minute) {
		return addMinutes(dateStr, minute, FORMAT_YYYY_MM_DD_HH_MM_SS);
	}
	
	/**
	 * 计算两个日期相差分钟数 如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one
	 *          第一个日期数，作为基准
	 * @param two
	 *          第二个日期数，作为比较
	 * @return 两个日期相差分钟数
	 */
	public static long diffMinutes(Date one, Date two) {
		Calendar calendar = Calendar.getInstance();
		// 得到第一个日期的年分和月份数
		calendar.setTime(one);
		long onel = calendar.getTimeInMillis();
		// 得到第二个日期的年份和月份
		calendar.setTime(two);
		long towl = calendar.getTimeInMillis();

		return (towl - onel) / 1000 / 60;
	}
	
	/**
	 * 当前时间格式化
	 * @param format
	 * @return
	 */
	public static String getCurrentDateStr(String format) {
		if ((format == null) || format.equals("")) {
			format = FORMAT_YYYY_MM_DD_HH_MM_SS;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		return sdf.format(new Date());
	}

	/**
	 * 获取当前时间的时间戳
	 * @return
	 */
	public static Timestamp getCurrentTime() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static Timestamp getYear() {
		Calendar calendar = new GregorianCalendar();
		return new Timestamp(formatStr2Date(String.valueOf(calendar.get(Calendar.YEAR)), "yyyy").getTime());
	}
	
	/**
	 * 获取当前时间的秒数
	 * 
	 * @return
	 */
	public static long getCurrentSecond() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * @param src
	 *          字符串
	 * @param srcFormat
	 *          字符串日期的格式
	 * @param tranfFomat
	 *          日期要转成的格式。
	 * @return 字符串  如yyyyMMddHHmmss ->  yyyy-MM-dd HH:mm:ss
	 */
	public static String translateFormatStr(String src, String srcFormat, String tranfFomat) {
		try {
			Date date = new SimpleDateFormat(srcFormat).parse(src);
			return new SimpleDateFormat(tranfFomat).format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @param src
	 *          字符串
	 * @param srcFormat
	 *          字符串日期的格式
	 * @param tranfFomat
	 *          日期要转成的格式。
	 * @return Date  如yyyyMMddHHmmss ->  yyyy-MM-dd HH:mm:ss
	 */
	public static Date translateFormatDate(String src, String srcFormat, String tranfFomat) {
		String translateFormatStr = translateFormatStr(src, srcFormat, tranfFomat);
		return formatStr2Date(translateFormatStr, tranfFomat);
	}

	public static Timestamp getNowTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * 格式化后进行String类型时间比较
	 * 
	 * @param date1
	 * @param date2
	 * @param format
	 * @return DATE1>DATE2返回1，DATE1<DATE2返回-1,等于返回0
	 */
	public static int compareDate(String date1, String date2, String format) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * String类型时间比较
	 * 
	 * @param date1
	 * @param date2
	 * @return DATE1>DATE2返回1，DATE1<DATE2返回-1,等于返回0
	 */
	public static int compareDate(String date1, String date2) {
		DateFormat df = new SimpleDateFormat();
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Date类型时间比较
	 * 
	 * @param date1
	 * @param date2
	 * @return DATE1>DATE2返回1，DATE1<DATE2返回-1,等于返回0
	 */
	public static int compareDate(Date date1, Date date2) {
		if (date1.getTime() > date2.getTime()) {
			return 1;
		} else if (date2.getTime() < date2.getTime()) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String formatDate2Str(Date date) {
		return formatDate2Str(date, FORMAT_YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 时间转换  Date -> String
	 * 
	 * @param date
	 * @param format 默认返回  yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatDate2Str(Date date, String format) {
		if ((format == null) || format.equals("")) {
			format = FORMAT_YYYY_MM_DD_HH_MM_SS;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (date != null) {
			return sdf.format(date);
		}
		return "";
	}
	
	/**
	 * 时间转换  String -> Date，如果转化失败，返回null
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date formatStr2Date(String dateStr, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		try {
			return sf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 时间转换  String -> String，如果转化失败，返回null
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static String formatStr2Str(String dateStr, String format) {
		SimpleDateFormat sf = new SimpleDateFormat();
		try {
			Date date = sf.parse(dateStr);
			return formatDate2Str(date,format);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 时间格式转换
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date formatDate2date(Date date, String format) {
		String dateStr = formatDate2Str(date, format);
		if (dateStr == null) {
			return null;
		}
		return formatStr2Date(dateStr, format);
	}

	/**
	 * 日期格式转换
	 * 
	 * @param str YYYYMMDD
	 * @return YYYY-MM-DD
     */
	public static String formatStr(String str) {
		if ((str == null) || (str.length() != 8)) {
			return str;
		}
		return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8);
	}

	/**
	 * 格式化sql Timestamp
	 * 
	 * @param tm
	 * @param format
	 * @return
	 */
	public static String formatSqlTimestamp(Timestamp tm, String format) {
		if (null == tm) {
			return "";
		}
		return new SimpleDateFormat(format).format(tm);
	}

	/**
	 * 格式化sql Timestamp
	 * 
	 * @param tm
	 * @return yyyyMMddHHmmss
	 */
	public static String formatSqlTimestamp(Timestamp tm) {
		return null == tm ? "" : new SimpleDateFormat(FORMAT_YYYYMMDDHHMMSS).format(tm);
	}

	/**
	 * 获取当前时间，格式为 yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String getCurrentTimeStr() {
		Date now = new Date();
		return formatDate2Str(now, FORMAT_YYYYMMDDHHMMSS);
	}

	/**
	 * 获取当前时间，默认格式为 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTimeStr(String format) {
		format = StringUtils.isEmpty(format) ? FORMAT_YYYY_MM_DD_HH_MM_SS : format;
		Date now = new Date();
		return formatDate2Str(now, format);
	}

	/**
	 * 功能描述：返回分
	 *
	 * @param date
	 *          日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 功能描述：返回小时(24小时)
	 *
	 * @param date
	 *          日期
	 * @return 返回小时
	 */
	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}


	/**
	 * 验证日期和格式
	 * 
	 * @param str
	 * @param format
	 * @return
	 */
	public static boolean isValidDate(String str, String format) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		boolean convertSuccess = true;
		SimpleDateFormat sf = new SimpleDateFormat(format);
		try {
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			sf.setLenient(false);
			sf.parse(StringUtils.trim(str));
		} catch (ParseException e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one
	 *          第一个日期数，作为基准
	 * @param two
	 *          第二个日期数，作为比较
	 * @return 两个日期相差天数
	 */
	public static long diffDays(Date one, Date two) {
		return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
	}
	
	

    /**
     * 计算相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int dateDayDiff(Date startDate, Date endDate) {
        if (endDate.compareTo(startDate) >= 0) {
            String diff = DurationFormatUtils.formatPeriod(startDate.getTime(), endDate.getTime(), "d");
            return Integer.parseInt(diff);
        } else {
            String diff = DurationFormatUtils.formatPeriod(endDate.getTime(), startDate.getTime(), "d");
            return -1 * Integer.parseInt(diff);
        }
    }


    public static int secondDiff(Date startDate, Date endDate) {
        if (endDate.compareTo(startDate) >= 0) {
            String diff = DurationFormatUtils.formatPeriod(startDate.getTime(), endDate.getTime(), "s");
            return Integer.parseInt(diff);
        } else {
            String diff = DurationFormatUtils.formatPeriod(endDate.getTime(), startDate.getTime(), "s");
            return -1 * Integer.parseInt(diff);
        }
    }
	
}
