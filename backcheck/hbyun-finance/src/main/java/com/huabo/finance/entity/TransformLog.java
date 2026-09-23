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
 * 转化日志实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_TRANSFORM_LOG")
@Schema(name = "TransformLog对象", description = "转化日志")
public class TransformLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "日志ID")
    @TableId("LOG_ID")
    private String logId;

    @Schema(name =  "任务ID")
    @TableField("TASK_ID")
    private String taskId;

    @Schema(name =  "日志级别(INFO-信息,WARN-警告,ERROR-错误,DEBUG-调试)")
    @TableField("LOG_LEVEL")
    private String logLevel;

    @Schema(name =  "日志类型(FIELD_MAPPING-字段映射,ALGORITHM-算法执行,VALIDATION-数据校验,ERROR-错误)")
    @TableField("LOG_TYPE")
    private String logType;

    @Schema(name =  "日志消息")
    @TableField("LOG_MESSAGE")
    private String logMessage;

    @Schema(name =  "日志详情")
    @TableField("LOG_DETAIL")
    private String logDetail;

    @Schema(name =  "源表名")
    @TableField("SOURCE_TABLE")
    private String sourceTable;

    @Schema(name =  "目标表名")
    @TableField("TARGET_TABLE")
    private String targetTable;

    @Schema(name =  "记录数")
    @TableField("RECORD_COUNT")
    private Integer recordCount;

    @Schema(name =  "创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}

