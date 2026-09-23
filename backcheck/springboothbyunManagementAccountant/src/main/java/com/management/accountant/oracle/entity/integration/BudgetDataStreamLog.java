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
 * 数据流运行日志实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_DATASTREAM_LOG")
public class BudgetDataStreamLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "LOG_ID", type = IdType.ASSIGN_UUID)
    private String logId;

    /**
     * 数据流ID
     */
    @TableField("STREAM_ID")
    private String streamId;

    /**
     * 日志时间
     */
    @TableField("LOG_TIME")
    private Date logTime;

    /**
     * 日志级别 (INFO/WARN/ERROR/DEBUG)
     */
    @TableField("LOG_LEVEL")
    private String logLevel;

    /**
     * 组件名称 (Source/Processor/Sink)
     */
    @TableField("COMPONENT")
    private String component;

    /**
     * 日志消息
     */
    @TableField("MESSAGE")
    private String message;

    /**
     * 处理记录数
     */
    @TableField("RECORD_COUNT")
    private Long recordCount;

    /**
     * 错误详情
     */
    @TableField("ERROR_DETAIL")
    private String errorDetail;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;
}
