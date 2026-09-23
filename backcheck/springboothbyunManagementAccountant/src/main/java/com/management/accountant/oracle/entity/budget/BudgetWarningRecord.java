package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算预警记录实体类
 * 
 * @author AI Agent
 * @date 2025-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_WARNING_RECORD")
public class BudgetWarningRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID (主键)
     */
    @TableId(value = "RECORD_ID", type = IdType.ASSIGN_UUID)
    private String recordId;

    /**
     * 规则ID
     */
    @TableField("RULE_ID")
    private String ruleId;

    /**
     * 预算ID
     */
    @TableField("BUDGET_ID")
    private String budgetId;

    /**
     * 预警级别
     * HIGH: 高
     * MEDIUM: 中
     * LOW: 低
     */
    @TableField("WARNING_LEVEL")
    private String warningLevel;

    /**
     * 预警消息
     */
    @TableField("WARNING_MESSAGE")
    private String warningMessage;

    /**
     * 预警数据 (JSON格式)
     */
    @TableField("WARNING_DATA")
    private String warningData;

    /**
     * 处理状态
     * PENDING: 待处理
     * PROCESSING: 处理中
     * HANDLED: 已处理
     * IGNORED: 已忽略
     */
    @TableField("HANDLE_STATUS")
    private String handleStatus;

    /**
     * 处理说明
     */
    @TableField("HANDLE_NOTE")
    private String handleNote;

    /**
     * 处理人ID
     */
    @TableField("HANDLE_BY")
    private String handleBy;

    /**
     * 处理时间
     */
    @TableField("HANDLE_TIME")
    private Date handleTime;

    /**
     * 是否已通知 (1:是, 0:否)
     */
    @TableField("IS_NOTIFIED")
    private Integer isNotified;

    /**
     * 通知时间
     */
    @TableField("NOTIFICATION_TIME")
    private Date notificationTime;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 删除标志 (0:未删除, 1:已删除)
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;
}

