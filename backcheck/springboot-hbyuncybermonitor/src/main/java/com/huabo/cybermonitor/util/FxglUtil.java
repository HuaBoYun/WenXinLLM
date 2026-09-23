package com.huabo.cybermonitor.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

public class FxglUtil {
	private static String webRootPath = "";
	public static String getselect(Object s, int type){
		if(s.equals("闭")&&type==1)
			{
			
			return "selected='selected'";
			}
		if(s.equals("开")&&type==0)
			{
			
			  return "selected='selected'";
			}
		return "";
		
	}
	
	public static String strCheck(String s){
		if(s != null) {
			return s;
		}
		else{
			return "";
		}
	}
	
	
	public static String strPar(Integer s){
		if(s==1) {
			return "弃用";
		}
		else{
			return "使用";
		}
	}
	
	public static Object strCheck(Object s){
		if(s != null) {
			return s;
		}
		else{
			return "";
		}
	}
	public static String checkDC(Object s){
		if(s.equals("D"))
			return "借";
		else  if(s.equals("C")){
			return "贷";
		}
		else return "";
	}
	
	public static String getTimeString(){
		DecimalFormat timeFormat4 = new DecimalFormat("0000;0000");
        Calendar cal = Calendar.getInstance();
        String val = String.valueOf(cal.get(Calendar.YEAR));
        val += timeFormat4.format(cal.get(Calendar.DAY_OF_YEAR));
        val += UUID.randomUUID().toString().replaceAll("-", "");
        
        return val;
	}
	
	public static String getStrFromObject(Object s){
	
		if(s == null) return "";
		else return s.toString();
	}
	
	/**
	 * 账套密码规则
	 * HYG
	 * @param name
	 * @return
	 */
	public static String getPwdFromName(String name){
		if(name == null){
			name = "abc123321";
		}
		if(name.equals("25982FA6")){
			return "1";
		}else if(name.equals("68C21TG8")){
			return "1";
		}else if(name.equals("HBAUDIT2")){
			return "1";
		}
		
		String result = new StringBuilder(name).reverse().toString();
		result = result.toLowerCase();
		
		return result;
	}

	public static String getWebRootPath() {
		return webRootPath;
	}

	public static void setWebRootPath(String webRootPath) {
		FxglUtil.webRootPath = webRootPath;
	}
	
//	public static String getDataResultDir(){
//		
//	}
}
