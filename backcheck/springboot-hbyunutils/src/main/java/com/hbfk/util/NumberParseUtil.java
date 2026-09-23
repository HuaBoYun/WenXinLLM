package com.hbfk.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberParseUtil {

	/**
	 * 将字符串返回为 金额类型的格式 3,565,656,560,600,451,000,000
	 * @param str
	 * @return
	 */
	public static String getFormatter(String str) {
		NumberFormat n = NumberFormat.getNumberInstance();
		double d;
		String outStr = null;
		d = Double.parseDouble(str);
		outStr = n.format(d);
		return outStr;
	}
	
	/**
	 * 将字符串返回为 金额类型的格式 ￥3,565,656,560,600,451,000,000.00
	 * @param str
	 * @return
	 */
	public static String getDecimalFormat(String str) {
		DecimalFormat fmt = new DecimalFormat("##,###,###,###,##0.00");
		String outStr = null;
		double d;
		d = Double.parseDouble(str);
		outStr = fmt.format(d);
		return outStr;
	}
	
	/**
	 * 将字符串返回为 金额类型的格式3,565,656,560,600,451,000,000.00
	 * @param str
	 * @return
	 */
	public static String getCurrency(String str){
		NumberFormat n = NumberFormat.getCurrencyInstance();
		double d;
		String outStr = null;
		d = Double.parseDouble(str);
		outStr = n.format(d);
		return outStr;
	}
}
