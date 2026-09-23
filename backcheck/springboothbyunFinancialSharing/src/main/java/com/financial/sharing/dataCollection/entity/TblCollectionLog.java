package com.financial.sharing.dataCollection.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 归集日志实体类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@TableName("TBL_COLLECTION_LOG")
public class TblCollectionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId("LOG_ID")
    private String logId;

    /**
     * 任务ID
     */
    @TableField("TASK_ID")
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
     * 执行类型：MANUAL(手动)/SCHEDULED(定时)
     */
    @TableField("EXECUTE_TYPE")
    private String executeType;

    /**
     * 执行状态：SUCCESS(成功)/FAILED(失败)/STOPPED(已停止)
     */
    @TableField("EXECUTE_STATUS")
    private String executeStatus;

    /**
     * 开始时间
     */
    @TableField("START_TIME")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("END_TIME")
    private Date endTime;

    /**
     * 执行时长（毫秒）
     */
    @TableField("EXECUTE_DURATION")
    private Long executeDuration;

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
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 规则执行详情（JSON格式）
     */
    @TableField("RULE_DETAILS")
    private String ruleDetails;

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
}

