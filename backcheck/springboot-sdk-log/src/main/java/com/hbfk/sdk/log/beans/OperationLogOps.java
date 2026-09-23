package com.hbfk.sdk.log.beans;

import com.hbfk.sdk.log.enums.OperationType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
* @Description: 业务操作实体
* @Author: 61
*/
@Data
@Builder
public class OperationLogOps {
    private String successTemplate;
    private String failTemplate;
    private String busType;
    private OperationType operationType;
    private String subType;
    private String busNo;
    private String extra;
    private Date startTime;
}
