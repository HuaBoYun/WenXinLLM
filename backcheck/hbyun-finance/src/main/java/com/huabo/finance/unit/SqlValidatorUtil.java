package com.huabo.finance.unit;

import java.util.regex.Pattern;

public class SqlValidatorUtil {
	// 定义危险操作的正则表达式模式
    private static final Pattern[] DANGEROUS_PATTERNS = {
        Pattern.compile("\\bCREATE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bDROP\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bDELETE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bUPDATE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bALTER\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bTRUNCATE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bINSERT\\b", Pattern.CASE_INSENSITIVE),
        //Pattern.compile("\\bREPLACE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bGRANT\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bREVOKE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bEXEC\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bEXECUTE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bMERGE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bCALL\\b", Pattern.CASE_INSENSITIVE)
    };
    
    // 允许的操作模式（SELECT查询）
    private static final Pattern SELECT_PATTERN = 
        Pattern.compile("^\\s*SELECT\\b", Pattern.CASE_INSENSITIVE);
    
    public static boolean validateSql(String sql) {
        
        // 检查是否包含危险操作
        for (Pattern pattern : DANGEROUS_PATTERNS) {
            if (pattern.matcher(sql).find()) {
                //String operation = pattern.toString().split("\\\\b")[1];
                return false;
            }
        }
        
        // 检查是否以SELECT开头
        if (!SELECT_PATTERN.matcher(sql).find()) {
            return false;
        }
        
        return true;
    }	
}
