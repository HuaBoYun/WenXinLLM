package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * API调用日志实体类
 *
 * @author AI Agent
 * @date 2026-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_API_CALL_LOG")
public class BudgetApiCallLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 日志ID (主键) */
    @TableId(value = "LOG_ID", type = IdType.ASSIGN_UUID)
    private String logId;

    /** 关联API ID */
    @TableField("API_ID")
    private String apiId;

    /** 请求ID */
    @TableField("REQUEST_ID")
    private String requestId;

    /** 调用时间 */
    @TableField("CALL_TIME")
    private Date callTime;

    /** 请求URL */
    @TableField("REQUEST_URL")
    private String requestUrl;

    /** 请求方法 */
    @TableField("REQUEST_METHOD")
    private String requestMethod;

    /** 请求头 */
    @TableField("REQUEST_HEADERS")
    private String requestHeaders;

    /** 请求体 */
    @TableField("REQUEST_BODY")
    private String requestBody;

    /** 响应状态码 */
    @TableField("RESPONSE_STATUS")
    private Integer responseStatus;

    /** 响应头 */
    @TableField("RESPONSE_HEADERS")
    private String responseHeaders;

    /** 响应体 */
    @TableField("RESPONSE_BODY")
    private String responseBody;

    /** 响应时间(毫秒) */
    @TableField("RESPONSE_TIME")
    private Integer responseTime;

    /** 数据大小(字节) */
    @TableField("DATA_SIZE")
    private Integer dataSize;

    /** 错误信息 */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /** 调用状态 (SUCCESS/FAILED/TIMEOUT) */
    @TableField("CALL_STATUS")
    private String callStatus;

    /** 调用者IP */
    @TableField("CALLER_IP")
    private String callerIp;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;
}
