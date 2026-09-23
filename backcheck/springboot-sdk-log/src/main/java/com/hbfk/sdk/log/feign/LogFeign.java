package com.hbfk.sdk.log.feign;

import com.hbfk.sdk.log.beans.OperationLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Author: 61
 * @Description: 日志服务feign客户端
 */
@FeignClient(name = "hbyunlog",configuration = FeignConfig.class)
public interface LogFeign {
    @PostMapping("/log/bus/save")
    void sendLogBusInfo(OperationLog operationLog);
}
