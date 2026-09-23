package com.huabo.financialdata.config.log;


import com.huabo.financialdata.config.exception.BizException;

/**
 * 日志接口
 *
 * @author lee
 * @version 1.0.0
 **/
public interface ILogService {

    /**
     * info: 日出输出
     *
     * @param message 日志输出内容
     */
    void info(String message);

    /**
     * info: 日出输出
     *
     * @param message 日志输出内容
     * @param e       BizException 异常内容
     */
    void info(String message, BizException e);

    /**
     * info: 日出输出
     *
     * @param message 日志输出内容
     * @param e       BizException 异常内容
     * @param objects message中占位符对应值
     */
    void info(String message, BizException e, Object... objects);

    /**
     * info: 日出输出
     *
     * @param message 日志输出内容
     * @param objects message中占位符对应值
     */
    void info(String message, Object... objects);

    /**
     * ERROR: 日出输出
     *
     * @param message 日志输出内容
     * @return
     */
    void error(String message);

    /**
     * ERROR: 日出输出
     *
     * @param message 日志输出内容
     * @param e       BizException 异常内容
     */
    void error(String message, BizException e);

    /**
     * ERROR: 日出输出
     *
     * @param message 日志输出内容
     * @param objects message中占位符对应值
     */
    void error(String message, Object... objects);

    /**
     * ERROR: 日出输出
     *
     * @param message 日志输出内容
     * @param e       BizException 异常内容
     * @param objects message中占位符对应值
     */
    void error(String message, BizException e, Object... objects);

}
