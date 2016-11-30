package com.yhsjedu.datacloud.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 日期处理函数
 * 
 * @author Administrator
 */
public class DateUtility {
    /** 年-月-日(yyyy-MM-dd) */
    public static final String DATE_PATTERN_SLASH_YMD = "yyyy-MM-dd";
    /** 年-月-日(yyyy-MM-dd) */
    public static final String DATE_PATTERN_SLASH_MD = "MM-dd";
    /** 年-月-日(yyyy-MM-dd HH:mm:ss) */
    public static final String DATE_PATTERN_SLASH_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_SLASH_YMDHM = "yyyy-MM-dd HH:mm";
    /** 年月日(yyyyMMdd) */
    public static final String DATE_PATTERN_NO_SLASH_YMD = "yyyyMMdd";
    /** 年月(yyyyMM) */
    public static final String DATE_PATTERN_NO_SLASH_YM = "yyyyMM";
    /** 年(yyyy) */
    public static final String DATE_PATTERN_YYYY = "yyyy";
    /** 月(MM) */
    public static final String DATE_PATTERN_MM = "MM";
    /** 年月日时分秒(yyyyMMddHHmmss) */
    public static final String DATE_PATTERN_YMDHMS = "yyyyMMddHHmmss";
    /** 年月日时分秒 毫秒(yyyyMMddHHmmssSSS) */
    public static final String DATE_PATTERN_YMDHMS2S = "yyyyMMddHHmmssSSS";
    /** yyyy年MM月dd日(yyyy/MM/dd) */
    public static final String DATE_PATTERN_REPORT = "yyyy年MM月dd日";
    /** yyyy/MM/dd */
    public static final String DATE_PATTERN_REPORT_YMD = "yyyy/MM/dd";
    /** yyyy年MM月(yyyy/MM) */
    public static final String DATE_PATTERN_REPORT_YM = "yyyy年MM月";
    /** 当月第一天 */
    public static final String MONTH_FIRST_DAY = "01";

    /**
     * 取得系统日期
     * 
     * @return Date
     */
    public static Date getSystemDateTime() {
        Date sysDate = new java.util.Date(System.currentTimeMillis());
        return sysDate;
    }

    public static Timestamp getSystemDateTimeTimestamp() {
        return new Timestamp(getSystemDateTime().getTime());
    }

    /**
     * 取得系统日期(不带时间)
     * 
     * @return
     */
    public static Date getSystemDate() {
        return formatStringToDate(toString(getSystemDateTime(), DATE_PATTERN_SLASH_YMD), DATE_PATTERN_SLASH_YMD);
    }

    public static Timestamp getSystemDateTimestamp() {
        return new Timestamp(getSystemDate().getTime());
    }

    /**
     * 字符串转换成日期类型
     * 
     * @param strDate 转换字符
     * @param formatType 日期格式
     * @return
     */
    public static Date formatStringToDate(String strDate, String formatType) {
        Date date = null;
        try {
            DateFormat format = new SimpleDateFormat(formatType);

            date = format.parse(strDate);

            return date;
        } catch (Exception ex) {
            return date;
        }
    }

    /**
     * 日期转换成字符串类型
     * 
     * @param date 转换日期
     * @param formatType 日期格式
     * @return
     */
    public static String formatDateToString(Date date, String formatType) {
        String strDate = null;
        try {
            DateFormat format = new SimpleDateFormat(formatType);

            strDate = format.format(date);

            return strDate;
        } catch (Exception ex) {
            return strDate;
        }
    }

    /**
     * 日期计算方法
     * 
     * @param date 计算对象
     * @param filed 计算域 Calendar.YEAR:年 Calendar.MONTH:月 Calendar.DAY_OF_MONTH:日
     * @param val 日期加减数量
     * @return
     */
    public static Date calcDate(Date date, int filed, int val) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(filed, val);
        cl.set(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH), cl.get(Calendar.DAY_OF_MONTH));

        return cl.getTime();
    }

    /**
     * 判断一个日期是否在两个日期之间
     * 
     * @param descDate 待判断日期
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return
     */
    public static boolean DateInPeriod(Date descDate, Date startDate, Date endDate) {
        boolean bRet = true;
        if (descDate == null)
            return false;

        if (startDate != null) {
            bRet = startDate.before(descDate);
        }
        if (endDate != null && bRet) {
            bRet = endDate.after(descDate);
        }
        return bRet;
    }

    /**
     * 日期有效性
     * 
     * @author yanghf
     * @param descDate
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean periodOfValidity(Date nowDate, Date startDate, Date endDate) {
        boolean yes = false;
        if (nowDate == null && startDate == null && endDate == null) {
            yes = false;
        } else if (startDate.before(nowDate) && endDate.after(nowDate)) {
            yes = true;
        }
        return yes;
    }

    /**
     * 把字符串格式化成日期格式
     * 
     * @param value 日期字符串
     * @param datePattern (SimpleDateFormat)
     * @return java.util.Date Date
     * @see java.text.SimpleDateFormat
     */
    public static Date formatDate(String value, String datePattern) {
        if (datePattern == null)
            throw new NullPointerException(
                    "\u30D1\u30E9\u30E1\u30FC\u30BF datePattern \u306B\u306F null \u3092\u6307\u5B9A\u3067\u304D\u307E\u305B\u3093\u3002");
        if (value == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        sdf.setLenient(false);
        ParsePosition pos = new ParsePosition(0);
        Date date = sdf.parse(value, pos);
        if (pos.getIndex() != value.length())
            return null;
        else
            return date;
    }

    /**
     * 判定输入值是否为日期
     * 
     * @param value
     * @param datePattern (SimpleDateFormat )
     * @return
     * @see java.text.SimpleDateFormat
     */
    public static boolean isDate(String value, String datePattern) {
        if (value.length() != datePattern.length()) {
            return false;
        }
        if (datePattern == null) {
            throw new NullPointerException(
                    "\u30D1\u30E9\u30E1\u30FC\u30BF datePattern \u306B\u306F null \u3092\u6307\u5B9A\u3067\u304D\u307E\u305B\u3093\u3002");
        }
        if (value == null) {
            return false;
        }
        if (datePattern.indexOf('y') == -1) {
            return formatDate("2004" + value, "yyyy" + datePattern) != null;
        } else {
            return formatDate(value, datePattern) != null;
        }
    }

    /**
     * <p>
     * 把日期转化为字符串
     * </p>
     * 
     * @param value
     * @param datePattern
     * @return String
     */
    public static String toString(Date value, String datePattern) {
        if (value == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);

        return sdf.format(value);
    }

    /**
     * <p>
     * 日期加年
     * </p>
     * 
     * @param value 日期(yyyyMMdd)
     * @param amount 年数
     * @return String 返回yyyyMMdd的日期字符串
     */
    public static String addYear(String value, int amount, String format) {

        if (!isDate(value, format)) {
            return null;
        }
        return calculateCalendar(value, amount, Calendar.YEAR, format);
    }

    /**
     * <p>
     * 日期加月
     * </p>
     * 
     * @param value 日期
     * @param amount 天数
     * @return String 返回yyyyMMdd的日期字符串
     */
    public static String addMonth(String value, int amount, String format) {

        if (!isDate(value, format)) {
            return null;
        }

        return calculateCalendar(value, amount, Calendar.MONTH, format);

    }

    /**
     * <p>
     * 日期加天数
     * </p>
     * 
     * @param value 日期
     * @param amount 天数
     * @return String 返回yyyyMMdd的日期字符串
     */
    public static String addDay(String value, int amount, String format) {

        if (!isDate(value, format)) {
            return null;
        }

        return calculateCalendar(value, amount, Calendar.DAY_OF_MONTH, format);
    }

    /**
     * <p>
     * 得到日期当月的最后一天(yyyyMMdd)
     * </p>
     * 
     * @param value 日期
     * @return String 当月最后一天
     */
    public static String getLastDayOfMonth(String value) {

        String formatDate = value;
        if (isDate(formatDate, DATE_PATTERN_NO_SLASH_YM)) {
            formatDate = formatDate + MONTH_FIRST_DAY;
        } else if (!isDate(formatDate, DATE_PATTERN_NO_SLASH_YMD)) {
            return null;
        }

        Calendar calendar = getCalendar(formatDate);

        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.DAY_OF_MONTH, lastDay);

        String date = toString(calendar.getTime(), DATE_PATTERN_NO_SLASH_YMD);

        return date;
    }

    /**
     * <p>
     * 得到日期当月的第一天(yyyyMMdd)
     * </p>
     * 
     * @param value 日期
     * @return String 当月第一天
     */
    public static String getFirstDayOfMonth(String value) {

        if (!isDate(value, DATE_PATTERN_NO_SLASH_YMD)) {
            return null;
        }

        Calendar calendar = getCalendar(value);

        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.DAY_OF_MONTH, firstDay);

        String date = toString(calendar.getTime(), DATE_PATTERN_NO_SLASH_YMD);

        return date;
    }

    /**
     * <p>
     * 星期的第一天(yyyyMMdd)
     * </p>
     * 
     * @param value 日期
     * @return String 当周第一天
     */
    public static String getFirstDayOfWeek(String value) {

        if (!isDate(value, DATE_PATTERN_NO_SLASH_YMD)) {
            return null;
        }

        Calendar calendar = getCalendar(value);

        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_WEEK);

        calendar.set(Calendar.DAY_OF_WEEK, firstDay);

        String date = toString(calendar.getTime(), DATE_PATTERN_NO_SLASH_YMD);

        return date;
    }

    /**
     * <p>
     * 计算日期(yyyyMMdd)
     * </p>
     * 
     * @param value 日期
     * @param amount
     * @param type
     * @return String
     */
    private static String calculateCalendar(String value, int amount, int type, String formatType) {
        Calendar calendar = getCalendar(value, formatType);
        calendar.add(type, amount);
        String date = toString(calendar.getTime(), formatType);

        return date;
    }

    public static Date calculateCalendar(Date date, int amount, int type) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(type, amount);
        return calendar.getTime();
    }

    /**
     * 获取日历
     * 
     * @param value 日期
     * @return 日历
     */
    private static Calendar getCalendar(String value) {
        Date date = formatDate(value, DATE_PATTERN_NO_SLASH_YMD);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        return calendar;
    }

    private static Calendar getCalendar(String value, String format) {
        Date date = formatDate(value, format);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        return calendar;
    }

    /**
     * 取得当前时间
     * 
     * @return 当前时间
     */
    public static Date getDate() {
        Date nowDate = Calendar.getInstance().getTime();
        return nowDate;
    }

    /**
     * 取得当前时间
     * 
     * @param datePattern
     * @return 当前时间(带格式)
     */
    public static String getDate(String datePattern) {
        // 当前时间
        Date nowDate = getDate();
        return toString(nowDate, datePattern);
    }

    /**
     * 取得XMLGregorianCalendar格式的日期
     * 
     * @param date
     * @return
     * @throws DatatypeConfigurationException
     */
    public static XMLGregorianCalendar getXMLGregorianCalendar(Date date) throws DatatypeConfigurationException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    }

    /**
     * 取得XMLGregorianCalendar格式的日期（当前日期）
     * 
     * @return
     * @throws DatatypeConfigurationException
     */
    public static XMLGregorianCalendar getXMLGregorianCalendar() throws DatatypeConfigurationException {
        return getXMLGregorianCalendar(getSystemDateTime());
    }

    /**
     * 判断当前日期是那个季度的
     * 
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getQuarByDate(Date date) {
        String quar = "";
        int month = Integer.valueOf(getDate(DATE_PATTERN_MM));
        if (month == 1 || month == 2 || month == 3) {
            quar = "1";
        } else if (month == 4 || month == 5 || month == 6) {
            quar = "2";
        } else if (month == 7 || month == 8 || month == 9) {
            quar = "3";
        } else if (month == 10 || month == 11 || month == 12) {
            quar = "4";
        }
        return quar;
    }

    /**
     * 得到本季度第一天的日期
     * 
     * @Methods Name getFirstDayOfQuarter
     * @return Date
     */
    public static Date getFirstDayOfQuarter(Date date) {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        int curMonth = cDay.get(Calendar.MONTH);
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
            cDay.set(Calendar.MONTH, Calendar.JANUARY);
        }
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
            cDay.set(Calendar.MONTH, Calendar.APRIL);
        }
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
            cDay.set(Calendar.MONTH, Calendar.JULY);
        }
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
            cDay.set(Calendar.MONTH, Calendar.OCTOBER);
        }
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMinimum(Calendar.DAY_OF_MONTH));
        System.out.println(cDay.getTime());
        return cDay.getTime();
    }

    /**
     * 得到本季度最后一天的日期
     * 
     * @Methods Name getLastDayOfQuarter
     * @return Date
     */
    public static Date getLastDayOfQuarter(Date date) {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        int curMonth = cDay.get(Calendar.MONTH);
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
            cDay.set(Calendar.MONTH, Calendar.MARCH);
        }
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
            cDay.set(Calendar.MONTH, Calendar.JUNE);
        }
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
            cDay.set(Calendar.MONTH, Calendar.AUGUST);
        }
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
            cDay.set(Calendar.MONTH, Calendar.DECEMBER);
        }
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println(cDay.getTime());
        return cDay.getTime();
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     * 
     * @param beginDate
     * @param endDate
     * @return List
     * @throws ParseException
     */
    public static String[] getDatesBetweenTwoDate(String startDate, String endDate, String formatStr)
            throws ParseException {
        Date start = formatDate(startDate, formatStr);
        Date end = formatDate(endDate, formatStr);

        List<String> rtnVal = new ArrayList<String>();
        while (true) {
            rtnVal.add(toString(start, formatStr));

            start = calculateCalendar(start, 1, Calendar.DAY_OF_MONTH);
            if (start.after(end)) {
                break;
            }
        }

        return (String[]) rtnVal.toArray(new String[] {});
    }

    /**
     * 查询当前日期前(后)x天的日期
     * 
     * @param millis 当前日期毫秒数
     * @param day 天数（如果day数为负数,说明是此日期前的天数）
     * @return yyyy-MM-dd
     */
    public static String beforLongDate(long millis, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(c.getTimeInMillis());
        return sdf.format(date);
    }

    /**
     * @param date 当前日期
     * @param day 当前日期的前后几天,正数表示向后几天，负数表示前几天
     * @return
     */
    public static Date getBeforOrAfterDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();

    }

    public static long dayDiff(String from, String end, String format) {
        return dayDiff(formatDate(from, format), formatDate(end, format));
    }

    public static long dayDiff(Date from, String end, String format) {
        return dayDiff(from, formatDate(end, format));
    }

    public static long dayDiff(String from, Date end, String format) {
        return dayDiff(formatDate(from, format), end);
    }

    public static long dayDiff(Date from, Date end) {
        Calendar f = Calendar.getInstance();
        f.setTime(from);

        Calendar t = Calendar.getInstance();
        t.setTime(end);

        long time1 = f.getTimeInMillis();
        long time2 = t.getTimeInMillis();
        return (time2 - time1) / (1000 * 3600 * 24);
    }

}
