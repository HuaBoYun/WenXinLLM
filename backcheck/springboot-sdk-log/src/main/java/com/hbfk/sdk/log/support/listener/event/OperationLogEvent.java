package com.hbfk.sdk.log.support.listener.event;

import com.hbfk.sdk.log.beans.OperationLog;
import org.springframework.context.ApplicationEvent;

/**
* @Description: 业务日志事件类
* @Author: 61
*/
public class OperationLogEvent extends ApplicationEvent {

    public OperationLogEvent(OperationLog operationLog) {
        super(operationLog);
    }
}
