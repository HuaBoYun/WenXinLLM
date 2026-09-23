package com.huabo.financialdata.config.exception;

/**
 * 异常信息构建通用类
 *
 * @author lee
 * @version 1.0.0
 **/
public class ExceptionStackTraceBuilder {

    /**
     * 异常信息输出方法
     * 将异常信息转成字符串输出
     */
    public static String getValue(Throwable e) {
        StringBuilder sb = new StringBuilder(e.getClass() + ":" + e.getLocalizedMessage() + "\n");
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append("\t " + element.getClassName()).append(".")
                    .append(element.getMethodName()).append("(")
                    .append(element.getFileName()).append(":")
                    .append(element.getLineNumber()).append(")\n");
        }
        return sb.toString();
    }
}
