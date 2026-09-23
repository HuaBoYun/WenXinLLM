package com.hbfk.sdk.log.context;

import com.hbfk.sdk.log.beans.OperationLog;

/**
* @Description: OperationLog 上下文
* @Author: 61
*/
public class OperationLogContext {

    private static final ThreadLocal<OperationLog> logThreadLocal = new ThreadLocal<>();

    public static final String REQUEST_ID = "Request-Id";


    public static void setOperationLog(OperationLog operationLog){
        logThreadLocal.set(operationLog);
    }
    public static OperationLog getOperationLog(){
        return logThreadLocal.get();
    }

    public static void removeOperationLog(){
        logThreadLocal.remove();
    }

}
