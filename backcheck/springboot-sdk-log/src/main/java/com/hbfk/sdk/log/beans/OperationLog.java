package com.hbfk.sdk.log.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author: 61
 * @Description: 业务日志请求对象
 */
@Data
@Builder
public class OperationLog {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * ip
     */
    private String ip;
    /**
     * 链路ID
     */
    private String traceId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 项目模块
     */
    private String module;
    /**
     * 业务模块
     */
    private String businessModule;
    /**
     * 业务子模块
     */
    private String subBusinessModule;
    /**
     * 请求报文
     */
    private String requestPayload;
    /**
     * 响应报文
     */
    private String responsePayload;
    /**
     * 执行的SQL语句集合
     */
    private String executedSql;
    /**
     * 请求类和方法名
     */
    private String requestClassMethod;
    /**
     * 操作动作说明
     */
    private String actionDescription;
    /**
     * 请求时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date requestTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 请求耗时（毫秒）
     */
    private Long requestDurationMs;

    /**
     * 异常信息
     */
    private String error;

    /**
     * 异常标志 0：无异常 1：有异常
     */
    private Integer errorFlag;

    public void buildOperationLog(String traceId, String ip,String module,String requestPayload,String responsePayload,
                                  String requestClassMethod,String actionDescription,Date requestTime,long requestDurationMs,
                                  String executedSql,String businessModule,String subBusinessModule,String error,Integer errorFlag) {
        this.traceId = traceId;
        this.ip = ip;
        this.module = module;
        this.requestPayload = requestPayload;
        this.responsePayload = responsePayload;
        this.requestClassMethod = requestClassMethod;
        this.actionDescription = actionDescription;
        this.requestTime = requestTime;
        this.endTime = new Date();
        this.requestDurationMs = requestDurationMs;
        this.executedSql = executedSql;
        this.businessModule = businessModule;
        this.subBusinessModule = subBusinessModule;
        this.error = error;
        this.errorFlag = errorFlag;
    }
}
