package com.huabo.financialdata.config.log;


import com.huabo.financialdata.config.exception.BizException;

/**
 * 日志工厂
 *
 * @author lee
 * @version 1.0.0
 **/
public class LogFactory {

    public static ILogService logService = new LogDefaultServiceImpl();

    public static void info(String message) {
        logService.info(message);
    }

    public static void info(String message, BizException e) {
        logService.info(message, e);
    }

    public static void info(String message, BizException e, Object... objects) {
        logService.info(message, e, objects);
    }

    public static void info(String message, Object... objects) {
        logService.info(message, objects);
    }

    public static void error(String message) {
        logService.error(message);
    }

    public static void error(String message, BizException e) {
        logService.error(message, e);
    }

    public static void error(String message, BizException e, Object... objects) {
        logService.error(message, e, objects);
    }

    public static void error(String message, Object... objects) {
        logService.error(message, objects);
    }


}
