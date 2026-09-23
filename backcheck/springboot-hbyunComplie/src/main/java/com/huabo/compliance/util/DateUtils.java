package com.huabo.compliance.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    public static final String DATE_JFP_STR = "yyyyMM";
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SMALL_STR = "yyyy-MM-dd";
    public static final String DATE_KEY_STR = "yyMMddHHmmss";
    public static final String DATE_KEY_ALLSTR = "YYYYMMddHHmmss";

    public DateUtils() {
    }

    public static Date parse(String strDate) {
        return parse(strDate, "yyyy-MM-dd");
    }

    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);

        try {
            return strDate != null && !"".equals(strDate) ? df.parse(strDate) : null;
        } catch (ParseException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static int compareDateWithNow(Date date1) {
        Date date2 = new Date();
        int rnum = date1.compareTo(date2);
        return rnum;
    }

    public static int compareDateWithNow(long date1) {
        long date2 = dateToUnixTimestamp();
        if (date1 > date2) {
            return 1;
        } else {
            return date1 < date2 ? -1 : 0;
        }
    }

    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }

    public static String getJFPTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        return df.format(new Date());
    }

    public static long dateToUnixTimestamp(String date) {
        long timestamp = 0L;

        try {
            timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(date).getTime();
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return timestamp;
    }

    public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0L;

        try {
            timestamp = (new SimpleDateFormat(dateFormat)).parse(date).getTime();
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return timestamp;
    }

    public static long dateToUnixTimestamp() {
        long timestamp = (new Date()).getTime();
        return timestamp;
    }

    public static String unixTimestampToDate(long timestamp) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }

    public static int getMonthSpace(Date date1, Date date2) {
        boolean result = false;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int result1 = c2.get(2) - c1.get(2);
        return result1 == 0 ? 1 : Math.abs(result1);
    }

    public static int getMonth(Date date) {
        boolean result = false;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int result2 = c.get(2);
        return result2 == 0 ? 1 : Math.abs(result2);
    }

    public static int compare_date(String DATE1, String DATE2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else {
                return dt1.getTime() < dt2.getTime() ? -1 : 0;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            return 0;
        }
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        return year + "/" + month + "/" + day;
    }

    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int)((date2.getTime() - date1.getTime()) / 86400000L);
        return days;
    }

    public static void main(String[] args) {
        System.out.println(compare_date("2019-03-129", "2019-03-12"));
    }

    public static String getIntDateStr(Date date, String pattern) {
        if (date == null) {
            return "";
        } else {
            DateFormat dft = new SimpleDateFormat(pattern);
            String str = dft.format(date);
            return str;
        }
    }

    public static String parseDate(Date date, String type) {
        String dateStr = "";
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(type);
            dateStr = sdf.format(date);
        }

        return dateStr;
    }
}
