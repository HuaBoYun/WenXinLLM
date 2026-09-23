package com.huabo.financialdata.config.log;


import com.huabo.financialdata.config.exception.BizException;
import com.huabo.financialdata.config.exception.ExceptionStackTraceBuilder;
import com.huabo.financialdata.util.DateNewUtils;

/**
 * 异常返回信息处理类
 *
 * @author lee
 * @version 1.0.0
 **/
public class CalorieLogHandler {
    /**
     * 通过BizException生成固定格式的异常信息
     *
     * @param e BizException
     */
    public static CalorieLogInfo hander(BizException e) {
        return buidLogInfo(e);
    }

    public static CalorieLogInfo hander(String message, BizException e) {
        CalorieLogInfo calorieLogInfo = buidLogInfo(e);
        calorieLogInfo.setInfoTitle(message);
        return calorieLogInfo;
    }

    public static CalorieLogInfo hander(String message) {
        CalorieLogInfo calorieLogInfo = new CalorieLogInfo();
        calorieLogInfo.setAccDate(DateNewUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        calorieLogInfo.setInfoTitle(message);
        return calorieLogInfo;
    }

    /**
     * 根据异常类生成LogInfo
     *
     * @param e BizException 异常信息
     * @return calorieLogInfo CalorieLogInfo 日志类
     */

    private static CalorieLogInfo buidLogInfo(BizException e) {
        CalorieLogInfo calorieLogInfo = new CalorieLogInfo();
        calorieLogInfo.setAccDate(DateNewUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        calorieLogInfo.setInfoCode(e.getBizCode().getCode());
        calorieLogInfo.setInfoName(e.getBizCode().getName());
        calorieLogInfo.setInfoTitle(e.getLocalizedMessage());
        calorieLogInfo.setInfoDesc(ExceptionStackTraceBuilder.getValue(e));
        calorieLogInfo.setInfoUsrDesc(e.getCustomMessage());
        return calorieLogInfo;
    }

}
