package com.hbfk.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	public static final String DATE_JFP_STR="yyyyMM";
	public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_SMALL_STR = "yyyy-MM-dd";
	public static final String DATE_KEY_STR = "yyMMddHHmmss";
	public static final String DATE_KEY_ALLSTR = "YYYYMMddHHmmss"; 
	    
	public static Timestamp testTimestampToString(String date) {  
		 Timestamp ts = Timestamp.valueOf(date);  
		 return ts;
	}  

	/**
     * 将指定的日期转换成Unix时间戳
     * @param String date 需要转换的日期 yyyy-MM-dd HH:mm:ss
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(DATE_FULL_STR).parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }
    
    /**
     * 将指定的日期转换成Unix时间戳
     * @param String date 需要转换的日期 yyyy-MM-dd
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
     
    /**
     * 将当前日期转换成Unix时间戳
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp() {
        long timestamp = new Date().getTime();
        return timestamp;
    }
    
    /**
	 * 获取时间的简写
	 * 传入参数 
	 * date  Date 类型	转换时间
	 * pattern  String 类型		转换格式
	 */
	public static String getIntDateStr(Date date,String pattern){
		if(date == null){
			return "";
		}
		DateFormat dft = new SimpleDateFormat(pattern);
		String str = dft.format(date);
		return str;
	}
	
	 /**
     * 获取系统当前时间
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        return df.format(new Date());
    }
     
    /**
     * 获取系统当前时间
     * @return
     */
    public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }
     
    /**
     * 获取系统当前计费期
     * @return
     */
    public static String getJFPTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_JFP_STR);
        return df.format(new Date());
    }
     
    /**
     * 将Unix时间戳转换成日期
     * @param long timestamp 时间戳
     * @return String 日期字符串
     */
    public static String unixTimestampToDate(long timestamp) {
        SimpleDateFormat sd = new SimpleDateFormat(DATE_FULL_STR);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }
    	/**
    	 * 月份差
    	 * <p>Title:</p>
    	 * <p>Description:</p>
    	 * @author SongXiangYing
    	 * @date 2016年9月12日 下午3:20:58
    	 */
        public static int getMonthSpace(Date date1, Date date2){
            int result = 0;
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(date1);
            c2.setTime(date2);
            result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
            return result == 0 ? 1 : Math.abs(result);

        }
        
        
        public static int getMonth(Date date){
            int result = 0;
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            result = c.get(Calendar.MONTH) ;
            return result == 0 ? 1 : Math.abs(result);

        }
    public static int compare_date(String DATE1, String DATE2) {
        
        
        DateFormat df = new SimpleDateFormat(DATE_SMALL_STR);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
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
    
	public static String getCurrentDate(){
		Calendar calendar=Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH)+1;
		int day=calendar.get(Calendar.DAY_OF_MONTH);
		return year+"/"+month+"/"+day;
	}
	
	/**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }
	public static void main(String[] args) {
		System.out.println(parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
	}
	
	
	public static String parseDate(Date date, String type) {
		String dateStr = "";
    	if(date != null){
    		SimpleDateFormat sdf = new SimpleDateFormat(type);
    		dateStr = sdf.format(date);
    	}
    	return dateStr;
	}
	
	public static Date formatDate(String dateStr,String type) throws Exception {
		Date date = null;
		if(dateStr != null && !"".equals(dateStr)) {
			SimpleDateFormat sdf = new SimpleDateFormat(type);
			date = sdf.parse(dateStr);
		}
		
		return date;
	}
	
    public static Date getYearStartDate(Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear(); // 清除所有字段
        calendar.set(Calendar.YEAR, year); // 设置年份
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);
        // 将月份设置为0（即1月），日期设置为1（即1日）
        // 时、分、秒和毫秒都设置为0
        return calendar.getTime();
    }
    
    public static Date getYearEndDate(Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear(); // 清除所有字段
        calendar.set(Calendar.YEAR, year); // 设置年份
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        // 将月份设置为0（即1月），日期设置为1（即1日）
        // 时、分、秒和毫秒都设置为0
        return calendar.getTime();
    }
	
}
