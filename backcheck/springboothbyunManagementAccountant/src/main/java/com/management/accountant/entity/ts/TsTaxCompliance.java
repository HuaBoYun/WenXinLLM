package com.management.accountant.entity.ts;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 税务合规检查实体类
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_ts_tax_compliance")
public class TsTaxCompliance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合规检查ID
     */
    @TableId(value = "compliance_id", type = IdType.AUTO)
    private Long complianceId;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 合规检查编号
     */
    @TableField("compliance_code")
    private String complianceCode;

    /**
     * 合规检查名称
     */
    @TableField("compliance_name")
    private String complianceName;

    /**
     * 合规检查类型
     */
    @TableField("compliance_type")
    private String complianceType;

    /**
     * 检查范围
     */
    @TableField("check_scope")
    private String checkScope;

    /**
     * 检查对象
     */
    @TableField("check_object")
    private String checkObject;

    /**
     * 检查状态
     */
    @TableField("check_status")
    private String checkStatus;

    /**
     * 合规状态
     */
    @TableField("compliance_status")
    private String complianceStatus;

    /**
     * 风险等级
     */
    @TableField("risk_level")
    private String riskLevel;

    /**
     * 优先级
     */
    @TableField("priority")
    private String priority;

    /**
     * 检查规则ID
     */
    @TableField("rule_id")
    private Long ruleId;

    /**
     * 检查规则名称
     */
    @TableField("rule_name")
    private String ruleName;

    /**
     * 检查规则描述
     */
    @TableField("rule_description")
    private String ruleDescription;

    /**
     * 检查方法
     */
    @TableField("check_method")
    private String checkMethod;

    /**
     * 检查频率
     */
    @TableField("check_frequency")
    private String checkFrequency;

    /**
     * 检查周期
     */
    @TableField("check_cycle")
    private String checkCycle;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 计划检查时间
     */
    @TableField("planned_check_time")
    private LocalDateTime plannedCheckTime;

    /**
     * 实际检查时间
     */
    @TableField("actual_check_time")
    private LocalDateTime actualCheckTime;

    /**
     * 检查完成时间
     */
    @TableField("completion_time")
    private LocalDateTime completionTime;

    /**
     * 检查进度
     */
    @TableField("check_progress")
    private BigDecimal checkProgress;

    /**
     * 检查结果
     */
    @TableField("check_result")
    private String checkResult;

    /**
     * 合规评分
     */
    @TableField("compliance_score")
    private BigDecimal complianceScore;

    /**
     * 风险评分
     */
    @TableField("risk_score")
    private BigDecimal riskScore;

    /**
     * 问题数量
     */
    @TableField("issue_count")
    private Integer issueCount;

    /**
     * 严重问题数量
     */
    @TableField("critical_issue_count")
    private Integer criticalIssueCount;

    /**
     * 一般问题数量
     */
    @TableField("normal_issue_count")
    private Integer normalIssueCount;

    /**
     * 轻微问题数量
     */
    @TableField("minor_issue_count")
    private Integer minorIssueCount;

    /**
     * 问题描述
     */
    @TableField("issue_description")
    private String issueDescription;

    /**
     * 整改建议
     */
    @TableField("rectification_suggestion")
    private String rectificationSuggestion;

    /**
     * 整改状态
     */
    @TableField("rectification_status")
    private String rectificationStatus;

    /**
     * 整改负责人
     */
    @TableField("rectification_responsible")
    private String rectificationResponsible;

    /**
     * 整改截止时间
     */
    @TableField("rectification_deadline")
    private LocalDateTime rectificationDeadline;

    /**
     * 整改完成时间
     */
    @TableField("rectification_completion_time")
    private LocalDateTime rectificationCompletionTime;

    /**
     * 整改进度
     */
    @TableField("rectification_progress")
    private BigDecimal rectificationProgress;

    /**
     * 复查状态
     */
    @TableField("recheck_status")
    private String recheckStatus;

    /**
     * 复查时间
     */
    @TableField("recheck_time")
    private LocalDateTime recheckTime;

    /**
     * 复查结果
     */
    @TableField("recheck_result")
    private String recheckResult;

    /**
     * 检查人员
     */
    @TableField("checker")
    private String checker;

    /**
     * 检查部门
     */
    @TableField("check_department")
    private String checkDepartment;

    /**
     * 审核人员
     */
    @TableField("reviewer")
    private String reviewer;

    /**
     * 审核时间
     */
    @TableField("review_time")
    private LocalDateTime reviewTime;

    /**
     * 审核意见
     */
    @TableField("review_comment")
    private String reviewComment;

    /**
     * 报告路径
     */
    @TableField("report_path")
    private String reportPath;

    /**
     * 报告生成时间
     */
    @TableField("report_generation_time")
    private LocalDateTime reportGenerationTime;

    /**
     * 通知状态
     */
    @TableField("notification_status")
    private String notificationStatus;

    /**
     * 通知时间
     */
    @TableField("notification_time")
    private LocalDateTime notificationTime;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 扩展字段1
     */
    @TableField("ext_field1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("ext_field2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("ext_field3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("ext_field4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("ext_field5")
    private String extField5;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;
}
