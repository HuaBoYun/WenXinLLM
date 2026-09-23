package com.huabo.finance.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 采集任务日志实体类
 * 
 * @author 华博云开发团队
 * @since 2025-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_COLLECTION_TASK_LOG")
@Schema(name = "CollectionTaskLog对象", description = "采集任务日志")
public class CollectionTaskLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "日志ID")
    @TableId("LOG_ID")
    private String logId;

    @Schema(name =  "任务ID")
    @TableField("TASK_ID")
    private String taskId;

    @Schema(name =  "详情ID")
    @TableField("DETAIL_ID")
    private String detailId;

    @Schema(name =  "日志级别(INFO, WARN, ERROR, DEBUG)")
    @TableField("LOG_LEVEL")
    private String logLevel;

    @Schema(name =  "日志类型(CONNECT-连接, CREATE_TABLE-建表, INSERT-插入, UPDATE-更新, ERROR-错误)")
    @TableField("LOG_TYPE")
    private String logType;

    @Schema(name =  "日志消息")
    @TableField("LOG_MESSAGE")
    private String logMessage;

    @Schema(name =  "日志详情")
    @TableField("LOG_DETAIL")
    private String logDetail;

    @Schema(name =  "创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // 日志级别常量
    public static final String LEVEL_INFO = "INFO";
    public static final String LEVEL_WARN = "WARN";
    public static final String LEVEL_ERROR = "ERROR";
    public static final String LEVEL_DEBUG = "DEBUG";

    // 日志类型常量
    public static final String TYPE_CONNECT = "CONNECT";
    public static final String TYPE_CREATE_TABLE = "CREATE_TABLE";
    public static final String TYPE_INSERT = "INSERT";
    public static final String TYPE_UPDATE = "UPDATE";
    public static final String TYPE_ERROR = "ERROR";
}

