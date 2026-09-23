package com.hbfk.sdk.log.support.listener;

import cn.hutool.json.JSONUtil;
import com.hbfk.sdk.log.beans.OperationLog;
import com.hbfk.sdk.log.feign.LogFeign;
import com.hbfk.sdk.log.support.listener.event.OperationLogEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;

/**
 * @Description: log 应用事件监听
 * @Author: 61
 */
@Slf4j
@NoArgsConstructor
public class LogListener {
    @Resource
    LogFeign logFeign;

    @Async(value = "taskExecutorLog")
    @EventListener(OperationLogEvent.class)
    public void saveOperationLog(OperationLogEvent operationLogEvent) {
        OperationLog operationLog = (OperationLog) operationLogEvent.getSource();
        log.info("开始发送日志: {}", JSONUtil.toJsonStr(operationLog));
        logFeign.sendLogBusInfo(operationLog);
        log.info("日志发送结束");
    }

}
