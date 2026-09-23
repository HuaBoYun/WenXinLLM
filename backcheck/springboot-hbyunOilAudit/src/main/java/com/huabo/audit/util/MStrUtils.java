package com.huabo.audit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @description  string工具类
* @author   lyz
* @date 2022/4/14 10:57
*/
public class MStrUtils {

    private static final String UNDERSCORE = "_";

    /**
    * @description 下划线转驼峰
    * @author   lyz
    * @date 2022/4/14 11:00
    */
    public static String underscoreToCamelCase(String str) {
        if (str == null || "".equals(str.trim())) {
            return str;
        }
        String[] strary = str.split(UNDERSCORE);
        if (strary.length == 1) {
            return str;
        }
        StringBuffer strb = new StringBuffer();
        strb.append(strary[0]);
        for (int i = 1; i < strary.length; i++) {
            strb.append(upperFirstCase(strary[i]));
        }
        return strb.toString();
    }

    /**
    * @description 驼峰转下划线
    * @author   lyz
    * @date 2022/4/14 10:59
    */
    public static String camelCasetoUnderscore(String str) {
        if (str == null || "".equals(str.trim())) {
            return str;
        }
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer strb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(strb, UNDERSCORE + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(strb);
        return strb.toString();
    }


    /**
    * @description  首字母转大写
    * @author   lyz
    * @date 2022/4/14 10:59
    */
    public static String upperFirstCase(String str) {
        char[] chars = str.toCharArray();
//        chars[0] -= 32;
        chars[0] = Character.toUpperCase(chars[0]);
        return String.valueOf(chars);
    }
}
