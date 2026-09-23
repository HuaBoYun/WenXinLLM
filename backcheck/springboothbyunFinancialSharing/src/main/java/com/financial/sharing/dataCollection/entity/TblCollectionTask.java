package com.financial.sharing.dataCollection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 归集任务实体类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@TableName("TBL_FS_COLLECTION_TASK")
public class TblCollectionTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(value = "TASK_ID", type = IdType.ASSIGN_ID)
    private String taskId;

    /**
     * 任务编码
     */
    @TableField("TASK_CODE")
    private String taskCode;

    /**
     * 任务名称
     */
    @TableField("TASK_NAME")
    private String taskName;

    /**
     * 任务类型：MANUAL(手动)/SCHEDULED(定时)/REALTIME(实时)
     */
    @TableField("TASK_TYPE")
    private String taskType;

    /**
     * 数据源ID
     */
    @TableField("SOURCE_ID")
    private String sourceId;

    /**
     * 规则ID列表（JSON数组）
     */
    @TableField("RULE_IDS")
    private String ruleIds;

    /**
     * 调度类型：ONCE(一次)/DAILY(每日)/WEEKLY(每周)/MONTHLY(每月)/CRON(表达式)
     */
    @TableField("SCHEDULE_TYPE")
    private String scheduleType;

    /**
     * 调度表达式（CRON）
     */
    @TableField("SCHEDULE_EXPRESSION")
    private String scheduleExpression;

    /**
     * 下次执行时间
     */
    @TableField("NEXT_EXECUTE_TIME")
    private Date nextExecuteTime;

    /**
     * 最后执行时间
     */
    @TableField("LAST_EXECUTE_TIME")
    private Date lastExecuteTime;

    /**
     * 执行状态：PENDING(待执行)/RUNNING(运行中)/SUCCESS(成功)/FAILED(失败)
     */
    @TableField("EXECUTE_STATUS")
    private String executeStatus;

    /**
     * 总记录数
     */
    @TableField("TOTAL_COUNT")
    private Integer totalCount;

    /**
     * 成功数
     */
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    /**
     * 失败数
     */
    @TableField("FAILED_COUNT")
    private Integer failedCount;

    /**
     * 跳过数
     */
    @TableField("SKIP_COUNT")
    private Integer skipCount;

    /**
     * 执行时长（秒）
     */
    @TableField("EXECUTE_DURATION")
    private Integer executeDuration;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 日志文件路径
     */
    @TableField("LOG_FILE_PATH")
    private String logFilePath;

    /**
     * 是否启用：Y/N
     */
    @TableField("IS_ENABLED")
    private String isEnabled;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private String orgId;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}

