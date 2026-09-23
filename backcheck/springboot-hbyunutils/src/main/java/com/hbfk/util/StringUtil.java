package com.hbfk.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类
 * @author wangfeng
 * @date 2010-8-24上午10:33:49
 * @Copyright(c) Beijing Seeyon Software Co.,LTD
 */
public class StringUtil {

    /** 分隔符，用户无法输入的空格 */
    public static final String COMMON_STRING_DELIMITER = "\u001d";

    /** 字母的宽度 */
    private static final int FONT_WIDTHHALF = 6;

    /** 汉字的宽度 */
    private static final int FONT_WIDTH = 12;

    /** 字符的最大数值 */
    private static final int MAX_VALUE = 127;
    
    
    public static String getGBK(String s){
    	String str = null;
    	try{
    		str = new String(s.getBytes("utf-8"),"gbk");
    	}catch(Exception e){e.printStackTrace();}
    	return str;
    }

    
    /**根据字符串返回Properties对象
     * @param str 符合Properties格式的字符串
     * @return Properties对象
     * @throws IOException
     */
    public static java.util.Properties getProperties(String str)throws IOException{
    	java.util.Properties p = new java.util.Properties();
    	p.load(new StringReader(str));
    	return p;
    }
    
    /**
     * 根据前台页面显示宽度来截取字符串
     * @param str 字符串
     * @param width 显示宽度
     * @return 截取后的字符串
     */
    public static String splitByWidth(String str, int width) {
        // 宽度
        int w = 0;
        int index = 0;
        // 得到字符串宽度 如果整体宽度较小 不用截取
        if(FONT_WIDTH * str.length() <= width) {
            return str;
        }
        // 遍历,如果宽度大于指定,跳出循环.
        for(; index < str.length(); index++) {
            if(str.charAt(index) < MAX_VALUE) {
                w += FONT_WIDTHHALF;
            } else {
                w += FONT_WIDTH;
            }
            if(w > width) {
                break;
            }
        }
        // 裁剪字符
        return str.substring(0, index - 1) + "...";
    }

    /**
     * 检查字符串是否为空(null,空格)
     * @param str 字符串
     * @return boolean 是否为空
     */
    public static boolean isEmpty(String str) {
        boolean rs = true;
        if(str != null) {
            rs = "".equals(str.trim());
        }
        return rs;
    }

    /**
     * 检查字符串是否非空(null,空格)
     * @param str 字符串
     * @return boolean 是否为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否是int型
     * @param str 字符串
     * @return 是否为int型
     */
    public static boolean isInteger(String str) {
        if(isEmpty(str)) {
            return false;
        }
        Pattern pat = Pattern.compile("^([0-9]*|-)[0-9]+$");
        Matcher m = pat.matcher(str);
        return m.find();
    }

    /**
     * 判断字符串是否是double
     * @param str 字符串
     * @return 是否是double
     */
    public static boolean isDouble(String str) {
        if(isEmpty(str)) {
            return false;
        }
        if(isInteger(str)) {
            return true;
        }
        Pattern pat = Pattern.compile("^([0-9]*|-)[0-9]+\\.[0-9]+$");
        Matcher m = pat.matcher(str);
        return m.find();
    }

    /**
     * 将字符串转换为boolean类型，并返回。其中只有当字符串为“true”时才返回true，否则返回false。
     * @param str 字符串
     * @return 布尔型
     */
    public static boolean stringToboolean(String str) {
        if(isNotEmpty(str)) {
            if("true".equals(str)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 对String中的' "转义(String作为js内容使用时需要用到)
     * @param str 字符串
     * @return 转义后的字符串
     */
    public static String formatJavaScriptContent(String str) {
        if(isEmpty(str)) {
            return str;
        }
        StringBuffer buffer = new StringBuffer(str.length() * 2);
        int size = str.length();
        for(int i = 0; i < size; i++) {
            char c = str.charAt(i);
            switch(c) {
                case '\'' :
                    buffer.append('\\').append(c);
                    break;
                case '\"' :
                    buffer.append('\\').append(c);
                    break;
                default :
                    buffer.append(c);
                    break;
            }
        }
        return buffer.toString();
    }

    /**
     * @param sep 需要用到的分隔符
     * @param count 重复的个数
     * @return 返回 seq,seq,seq,格式的字符串
     */
    public static String getString(String sep, int count) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < count; i++) {
            sb.append("?" + (i == count - 1 ? "" : ","));
        }
        return sb.toString();
    }

    /**
     * @param sep 需要用到的分隔符
     * @param str 需要连接的字符串数组
     * @return 返回str[1],str[2],str[3],str[4] 格式的字符串
     */
    public static String getString(String sep, String[] str) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < str.length; i++) {
            sb.append(str[i] + (i == str.length - 1 ? "" : sep));
        }
        return sb.toString();
    }
    
    /**
     * 将一个字符串中所有的换行符替换为html的换行标签
     * @param str 要替换的字符串
     * @return 替换后的字符串
     */
    public static String replaceEnter(String str){
        return replaceEnter(str, "<br/>");
    }
    
    /**
     * 将一个字符串中所有的换行符替换为html的换行标签
     * @param str 要替换的字符串
     * @param str2 换行符将被改字符串替换
     * @return 替换后的字符串
     */
    public static String replaceEnter(String str, String str2){
        String temp = "";
        if(str!=null){
            String str3 = str2!=null? str2 : "<br/>";
            temp = str.replaceAll("\r\n",str3);
            temp = temp.replaceAll("\n",str3);
        }
        return temp;
    }
    
    /**
     * 替换一个字符串中所有的尖括号，将他们替换为指定的html转义字符
     * @param str 将要替换的字符串
     * @return 替换好的字符串
     */
    public static String replaceTag(String str){
        String temp = "";
        if(str!=null){
            temp = str.replaceAll("&", "&gt;");
            temp = temp.replaceAll("<","&lt;");
            temp = temp.replaceAll(">","&gt;");
            temp = temp.replaceAll(" ", "&nbsp;");
            temp = temp.replaceAll("\'", "&#39;");
            temp = temp.replaceAll("\"", "&quot;");
            temp = temp.replaceAll("\r\n", "<br/>");
            temp = temp.replaceAll("\n", "<br/>");
        }
        return temp;
    }
    
    /**
     * @param str 原始字符串
     * @param width 截取长度
     * @return 截取后的字符串
     */
    public static String getSubString(String str, int width) {
        int fontWidth = FONT_WIDTH;
        int fontWidthHalf = FONT_WIDTH / 2;
        int w = 0;
        int index = 0;
        for(int i = 0; i < str.length(); i++) {
            w += str.charAt(i) <= MAX_VALUE ? fontWidthHalf : fontWidth;
            if(w <= width) {
                index = i;
            }
        }
        return w < width ? str : str.substring(0, index) + "...";
    }
}
