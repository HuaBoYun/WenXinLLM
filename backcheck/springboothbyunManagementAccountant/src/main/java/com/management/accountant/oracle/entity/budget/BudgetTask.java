package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算任务实体类
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_TASK")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID (主键)
     */
    @TableId(value = "TASK_ID", type = IdType.ASSIGN_UUID)
    private String taskId;

    /**
     * 任务编码 (唯一)
     */
    @TableField("TASK_CODE")
    private String taskCode;

    /**
     * 任务名称
     */
    @TableField("TASK_NAME")
    private String taskName;

    /**
     * 任务类型
     * ANNUAL_BUDGET: 年度预算
     * QUARTERLY_BUDGET: 季度预算
     * MONTHLY_BUDGET: 月度预算
     * ADJUSTMENT: 预算调整
     * FORECAST: 预算预测
     */
    @TableField("TASK_TYPE")
    private String taskType;

    /**
     * 任务状态
     * PENDING: 待开始
     * IN_PROGRESS: 进行中
     * COMPLETED: 已完成
     * CANCELLED: 已取消
     * PAUSED: 已暂停
     */
    @TableField("TASK_STATUS")
    private String taskStatus;

    /**
     * 优先级
     * HIGH: 高
     * MEDIUM: 中
     * LOW: 低
     */
    @TableField("PRIORITY")
    private String priority;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    /**
     * 预算期间
     */
    @TableField("BUDGET_PERIOD")
    private String budgetPeriod;

    /**
     * 组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 组织名称
     */
    @TableField("ORGANIZATION_NAME")
    private String organizationName;

    /**
     * 创建人ID
     */
    @TableField("CREATOR_ID")
    private String creatorId;

    /**
     * 创建人姓名
     */
    @TableField("CREATOR_NAME")
    private String creatorName;

    /**
     * 分配人ID
     */
    @TableField("ASSIGNEE_ID")
    private String assigneeId;

    /**
     * 分配人姓名
     */
    @TableField("ASSIGNEE_NAME")
    private String assigneeName;

    /**
     * 开始日期
     */
    @TableField("START_DATE")
    private Date startDate;

    /**
     * 截止日期
     */
    @TableField("DUE_DATE")
    private Date dueDate;

    /**
     * 实际完成日期
     */
    @TableField("ACTUAL_COMPLETION_DATE")
    private Date actualCompletionDate;

    /**
     * 完成进度 (0-100)
     */
    @TableField("PROGRESS")
    private BigDecimal progress;

    /**
     * 任务描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 任务要求
     */
    @TableField("REQUIREMENTS")
    private String requirements;

    /**
     * 附件URL
     */
    @TableField("ATTACHMENTS")
    private String attachments;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 自定义 setter，兼容前端传入 Boolean 值
     */
    public void setIsEnabled(Object value) {
        if (value == null) {
            this.isEnabled = null;
        } else if (value instanceof Integer) {
            this.isEnabled = (Integer) value;
        } else if (value instanceof Boolean) {
            this.isEnabled = (Boolean) value ? 1 : 0;
        } else if (value instanceof String) {
            String str = (String) value;
            if ("true".equalsIgnoreCase(str) || "1".equals(str)) {
                this.isEnabled = 1;
            } else if ("false".equalsIgnoreCase(str) || "0".equals(str)) {
                this.isEnabled = 0;
            } else {
                this.isEnabled = Integer.valueOf(str);
            }
        } else if (value instanceof Number) {
            this.isEnabled = ((Number) value).intValue();
        } else {
            this.isEnabled = Integer.parseInt(value.toString());
        }
    }

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

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
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 删除标志 (0:未删除 1:已删除)
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 版本号 (乐观锁)
     */
    @TableField("VERSION")
    private Integer version;
}

