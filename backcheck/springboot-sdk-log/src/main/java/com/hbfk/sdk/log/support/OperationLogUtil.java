package com.hbfk.sdk.log.support;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.beans.OperationLogOps;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @Description: 业务log Util
* @Author: 61
*/
@NoArgsConstructor
public class OperationLogUtil {

    /**
    * @Description: 获取方法上的 operationLog 注解信
    * @Param: [method]
    * @return: java.util.Collection<com.hbfk.sdk.log.beans.OperationLogOps>
    * @Author: 61
    */
    public Collection<OperationLogOps> operationLogAnnotations(Method method) {
        OperationLog[] operationLogs = method.getAnnotationsByType(OperationLog.class);
        return Stream.of(operationLogs).map(x -> operationLogToOps(x)).collect(Collectors.toList());
    }

    private OperationLogOps operationLogToOps(OperationLog record) {
        return OperationLogOps.builder()
                .successTemplate(record.success())
                .failTemplate(record.fail())
                .busType(record.busType())
                .operationType(record.operationType())
                .subType(record.subType())
                .busNo(record.busNo())
                .extra(record.extra())
                .startTime(new Date())
                .build();
    }

}
