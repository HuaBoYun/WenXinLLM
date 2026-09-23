package com.hbfk.util;

import java.util.Arrays;
import java.util.List;

/**
 * 转换Word、Pdf 扩展工具
 *
 * @author lee
 * @version 1.0.0
 * @description
 * @date 2022/8/12 8:59 下午
 **/
public class AsposeWordExtUtil {

    /**
     * 转换Word、Pdf 前，处理表格超出显示边界、表格变现不显示问题
     *
     * @param str 待处理html数据
     * @return 返回处理后结果
     */
    public static String regexRep(String str) {
        //规则： 替换规则 @  替换内容（当替换内容为$时，则替换为空）
        List<String> regexList = Arrays.asList(new String[]{
                //处理表格越界
                "<table width=([^>]*)>@<table>",
                //增加表格线条
                "valign=\"center\" style=\"@valign=\"center\" style=\"border-style: solid;",
                "valign=\"top\" style=\"@valign=\"top\" style=\"border-style: solid;",
                "valign=\"middle\" style=\"@valign=\"middle\" style=\"border-style: solid;",
                "valign=\"bottom\" style=\"@valign=\"bottom\" style=\"border-style: solid;",
                "valign=\"baseline\" style=\"@valign=\"baseline\" style=\"border-style: solid;",
                "<td style=\"@<td style=\"border-style: solid;",
                //替换 标注-->"删除"
                "&nbsp;<\\s*a class=\"mark-delete\".*?/a\\s*>&nbsp;@$",
                "&nbsp;<a class=\"mark-delete\" [^>]*>([^<]*)</a>&nbsp;@$",
                " <a class=\"mark-delete\" [^>]*>([^<]*)</a>&nbsp;@$",
                //替换 标注-->"标注"
                "&nbsp;<a class=\"mark-content\" [^>]*> @$",
                " <a class=\"mark-content\" [^>]*> @$",
                " </a>&nbsp;@$"
        });


        for (String item : regexList) {
            String[] itemArr = item.split("@");
            String regex = itemArr[0];
            String replaceStr = itemArr[1];

            str = str.replaceAll(regex, replaceStr.equals("$") ? "" : replaceStr);
        }
        return str;
    }

}
