package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.management.accountant.util.excel.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算警报实体类
 *
 * @description 预算警报管理，用于记录和处理预算相关的警报信息
 * @author AI Agent
 * @date 2026-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_ALERT")
public class BudgetAlert implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 警报ID (主键)
     */
    @TableId(value = "ALERT_ID", type = IdType.ASSIGN_UUID)
    private String alertId;

    /**
     * 警报编码
     */
    @TableField("ALERT_CODE")
    @ExcelField(title = "警报编码", sort = 10, width = 4000)
    private String alertCode;

    /**
     * 警报名称
     */
    @TableField("ALERT_NAME")
    @ExcelField(title = "警报名称", sort = 20, width = 6000)
    private String alertName;

    /**
     * 警报类型
     * BUDGET_EXCEED: 预算超支
     * LIMIT_WARNING: 限额预警
     * EXECUTION_DELAY: 执行延迟
     * APPROVAL_TIMEOUT: 审批超时
     * FREEZE_ALERT: 冻结警报
     * SYSTEM_ALERT: 系统警报
     */
    @TableField("ALERT_TYPE")
    @ExcelField(title = "警报类型", sort = 30, width = 3000)
    private String alertType;

    /**
     * 警报级别
     * CRITICAL: 严重
     * HIGH: 高
     * MEDIUM: 中
     * LOW: 低
     * INFO: 信息
     */
    @TableField("ALERT_LEVEL")
    @ExcelField(title = "警报级别", sort = 40, width = 3000)
    private String alertLevel;

    /**
     * 关联预算ID
     */
    @TableField("BUDGET_ID")
    private String budgetId;

    /**
     * 关联组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 关联规则ID
     */
    @TableField("RULE_ID")
    private String ruleId;

    /**
     * 警报消息
     */
    @TableField("ALERT_MESSAGE")
    @ExcelField(title = "警报消息", sort = 50, width = 8000)
    private String alertMessage;

    /**
     * 警报详情 (JSON格式)
     */
    @TableField("ALERT_DETAIL")
    private String alertDetail;

    /**
     * 触发值
     */
    @TableField("TRIGGER_VALUE")
    @ExcelField(title = "触发值", sort = 60, width = 3000, dataFormat = "#,##0.00")
    private BigDecimal triggerValue;

    /**
     * 阈值
     */
    @TableField("THRESHOLD_VALUE")
    @ExcelField(title = "阈值", sort = 70, width = 3000, dataFormat = "#,##0.00")
    private BigDecimal thresholdValue;

    /**
     * 警报状态
     * PENDING: 待处理
     * ACKNOWLEDGED: 已确认
     * PROCESSING: 处理中
     * RESOLVED: 已解决
     * ESCALATED: 已升级
     * CLOSED: 已关闭
     */
    @TableField("ALERT_STATUS")
    @ExcelField(title = "警报状态", sort = 80, width = 3000)
    private String alertStatus;

    /**
     * 确认人ID
     */
    @TableField("ACKNOWLEDGED_BY")
    private String acknowledgedBy;

    /**
     * 确认时间
     */
    @TableField("ACKNOWLEDGED_TIME")
    @ExcelField(title = "确认时间", sort = 90, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date acknowledgedTime;

    /**
     * 确认备注
     */
    @TableField("ACKNOWLEDGED_NOTE")
    private String acknowledgedNote;

    /**
     * 处理人ID
     */
    @TableField("RESOLVED_BY")
    private String resolvedBy;

    /**
     * 处理时间
     */
    @TableField("RESOLVED_TIME")
    @ExcelField(title = "处理时间", sort = 100, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date resolvedTime;

    /**
     * 处理备注
     */
    @TableField("RESOLVED_NOTE")
    private String resolvedNote;

    /**
     * 升级人ID
     */
    @TableField("ESCALATED_BY")
    private String escalatedBy;

    /**
     * 升级时间
     */
    @TableField("ESCALATED_TIME")
    private Date escalatedTime;

    /**
     * 升级原因
     */
    @TableField("ESCALATED_REASON")
    private String escalatedReason;

    /**
     * 升级目标 (用户ID或角色ID)
     */
    @TableField("ESCALATED_TO")
    private String escalatedTo;

    /**
     * 通知方式
     * SYSTEM: 系统通知
     * EMAIL: 邮件
     * SMS: 短信
     * ALL: 全部
     */
    @TableField("NOTIFICATION_METHOD")
    @ExcelField(title = "通知方式", sort = 110, width = 3000)
    private String notificationMethod;

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
     * 通知接收人 (JSON数组)
     */
    @TableField("NOTIFICATION_RECEIVERS")
    private String notificationReceivers;

    /**
     * 触发时间
     */
    @TableField("TRIGGER_TIME")
    @ExcelField(title = "触发时间", sort = 120, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date triggerTime;

    /**
     * 过期时间
     */
    @TableField("EXPIRE_TIME")
    private Date expireTime;

    /**
     * 是否自动关闭 (1:是, 0:否)
     */
    @TableField("AUTO_CLOSE")
    private Integer autoClose;

    /**
     * 自动关闭时间（小时）
     */
    @TableField("AUTO_CLOSE_HOURS")
    private Integer autoCloseHours;

    /**
     * 优先级 (1-10, 数字越大优先级越高)
     */
    @TableField("PRIORITY")
    @ExcelField(title = "优先级", sort = 130, width = 2500)
    private Integer priority;

    /**
     * 公司ID
     */
    @TableField("COMPANY_ID")
    private String companyId;

    /**
     * 创建人ID
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ExcelField(title = "创建时间", sort = 140, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 删除标志 (0:未删除, 1:已删除)
     */
    @TableLogic
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

