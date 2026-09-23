package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目策划表实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_planning")
public class ProjectPlanning implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 策划编号
     */
    @TableField("planning_no")
    private String planningNo;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 策划名称
     */
    @TableField("planning_name")
    private String planningName;

    /**
     * 策划类型(1:可行性研究,2:初步设计,3:施工图设计,4:专项方案,5:其他)
     */
    @TableField("planning_type")
    private Integer planningType;

    /**
     * 策划状态(1:草稿,2:待审核,3:已审核,4:执行中,5:已完成,6:已暂停,7:已取消)
     */
    @TableField("planning_status")
    private Integer planningStatus;

    /**
     * 策划人ID
     */
    @TableField("planner_id")
    private Long plannerId;

    /**
     * 策划日期
     */
    @TableField("planning_date")
    private Date planningDate;

    /**
     * 策划版本 (数据库中暂不存在此字段)
     */
    @TableField(value = "planning_version", exist = false)
    private String planningVersion;

    /**
     * 项目开始日期 (数据库中暂不存在此字段)
     */
    @TableField(value = "project_start_date", exist = false)
    private Date projectStartDate;

    /**
     * 项目结束日期 (数据库中暂不存在此字段)
     */
    @TableField(value = "project_end_date", exist = false)
    private Date projectEndDate;

    /**
     * 总工期(天) (数据库中暂不存在此字段)
     */
    @TableField(value = "total_duration", exist = false)
    private Integer totalDuration;

    /**
     * 总预算 (数据库中暂不存在此字段)
     */
    @TableField(value = "total_budget", exist = false)
    private BigDecimal totalBudget;

    /**
     * 预算金额 (数据库中暂不存在此字段)
     */
    @TableField(value = "budget_amount", exist = false)
    private BigDecimal budgetAmount;

    /**
     * 完成度(百分比) (数据库中暂不存在此字段)
     */
    @TableField(value = "completion_rate", exist = false)
    private BigDecimal completionRate;

    /**
     * 计划开始时间 (数据库中暂不存在此字段)
     */
    @TableField(value = "planned_start_date", exist = false)
    private Date plannedStartDate;

    /**
     * 计划结束时间 (数据库中暂不存在此字段)
     */
    @TableField(value = "planned_end_date", exist = false)
    private Date plannedEndDate;

    /**
     * 实际开始时间 (数据库中暂不存在此字段)
     */
    @TableField(value = "actual_start_date", exist = false)
    private Date actualStartDate;

    /**
     * 实际结束时间 (数据库中暂不存在此字段)
     */
    @TableField(value = "actual_end_date", exist = false)
    private Date actualEndDate;

    /**
     * 策划说明 (数据库中暂不存在此字段)
     */
    @TableField(value = "planning_description", exist = false)
    private String planningDescription;

    /**
     * 关键里程碑 (数据库中暂不存在此字段)
     */
    @TableField(value = "key_milestones", exist = false)
    private String keyMilestones;

    /**
     * 风险分析 (数据库中暂不存在此字段)
     */
    @TableField(value = "risk_analysis", exist = false)
    private String riskAnalysis;

    /**
     * 质量目标 (数据库中暂不存在此字段)
     */
    @TableField(value = "quality_objectives", exist = false)
    private String qualityObjectives;

    /**
     * 安全措施 (数据库中暂不存在此字段)
     */
    @TableField(value = "safety_measures", exist = false)
    private String safetyMeasures;

    /**
     * 审核人ID (数据库中暂不存在此字段)
     */
    @TableField(value = "reviewer_id", exist = false)
    private Long reviewerId;

    /**
     * 审核人姓名 (数据库中暂不存在此字段)
     */
    @TableField(value = "reviewer_name", exist = false)
    private String reviewerName;

    /**
     * 审核日期 (数据库中暂不存在此字段)
     */
    @TableField(value = "review_date", exist = false)
    private Date reviewDate;

    /**
     * 审核时间 (兼容旧字段名，数据库中暂不存在此字段)
     */
    @TableField(value = "review_time", exist = false)
    private Date reviewTime;

    /**
     * 审核意见 (数据库中暂不存在此字段)
     */
    @TableField(value = "review_comments", exist = false)
    private String reviewComments;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 获取策划类型名称
     */
    public String getPlanningTypeName() {
        if (planningType == null) {
            return "";
        }
        switch (planningType) {
            case 1:
                return "可行性研究";
            case 2:
                return "初步设计";
            case 3:
                return "施工图设计";
            case 4:
                return "专项方案";
            case 5:
                return "其他";
            default:
                return "未知";
        }
    }



    /**
     * 获取策划状态名称
     */
    public String getPlanningStatusName() {
        if (planningStatus == null) {
            return "";
        }
        switch (planningStatus) {
            case 1:
                return "草稿";
            case 2:
                return "待审核";
            case 3:
                return "已审核";
            case 4:
                return "执行中";
            case 5:
                return "已完成";
            case 6:
                return "已暂停";
            case 7:
                return "已取消";
            default:
                return "未知";
        }
    }

    /**
     * 获取策划状态颜色
     */
    public String getPlanningStatusColor() {
        if (planningStatus == null) {
            return "#909399";
        }
        switch (planningStatus) {
            case 1:
                return "#909399"; // 灰色
            case 2:
                return "#E6A23C"; // 橙色
            case 3:
                return "#409EFF"; // 蓝色
            case 4:
                return "#67C23A"; // 绿色
            case 5:
                return "#67C23A"; // 绿色
            case 6:
                return "#F56C6C"; // 红色
            case 7:
                return "#909399"; // 灰色
            default:
                return "#909399";
        }
    }

    /**
     * 判断是否可以编辑
     */
    public boolean canEdit() {
        return planningStatus != null && (planningStatus == 1 || planningStatus == 3);
    }

    /**
     * 判断是否可以审核
     */
    public boolean canReview() {
        return planningStatus != null && planningStatus == 2;
    }

    /**
     * 判断是否可以执行
     */
    public boolean canExecute() {
        return planningStatus != null && planningStatus == 3;
    }

    /**
     * 判断是否已完成
     */
    public boolean isCompleted() {
        return planningStatus != null && planningStatus == 5;
    }

    /**
     * 判断是否已取消
     */
    public boolean isCancelled() {
        return planningStatus != null && planningStatus == 7;
    }

    /**
     * 获取预算金额显示文本
     */
    public String getBudgetAmountText() {
        if (budgetAmount == null) {
            return "未设定";
        }
        if (budgetAmount.compareTo(new BigDecimal("10000")) >= 0) {
            return budgetAmount.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP) + "万元";
        } else {
            return budgetAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "元";
        }
    }

    /**
     * 获取完成度显示文本
     */
    public String getCompletionRateText() {
        if (completionRate == null) {
            return "0%";
        }
        return completionRate.setScale(1, BigDecimal.ROUND_HALF_UP) + "%";
    }

    /**
     * 计算计划工期（天）
     */
    public long getPlannedDuration() {
        if (plannedStartDate == null || plannedEndDate == null) {
            return 0;
        }
        long diff = plannedEndDate.getTime() - plannedStartDate.getTime();
        return diff / (24 * 60 * 60 * 1000) + 1;
    }

    /**
     * 计算实际工期（天）
     */
    public long getActualDuration() {
        if (actualStartDate == null) {
            return 0;
        }
        Date endDate = actualEndDate != null ? actualEndDate : new Date();
        long diff = endDate.getTime() - actualStartDate.getTime();
        return diff / (24 * 60 * 60 * 1000) + 1;
    }

    /**
     * 判断是否延期
     */
    public boolean isDelayed() {
        if (plannedEndDate == null) {
            return false;
        }
        Date compareDate = actualEndDate != null ? actualEndDate : new Date();
        return compareDate.after(plannedEndDate);
    }

    /**
     * 计算延期天数
     */
    public long getDelayDays() {
        if (!isDelayed()) {
            return 0;
        }
        Date compareDate = actualEndDate != null ? actualEndDate : new Date();
        long diff = compareDate.getTime() - plannedEndDate.getTime();
        return diff / (24 * 60 * 60 * 1000);
    }

    /**
     * 判断是否即将到期（7天内）
     */
    public boolean isExpiringSoon() {
        if (plannedEndDate == null || isCompleted() || isCancelled()) {
            return false;
        }
        long diff = plannedEndDate.getTime() - new Date().getTime();
        long days = diff / (24 * 60 * 60 * 1000);
        return days >= 0 && days <= 7;
    }

    /**
     * 判断是否为重点策划（预算金额>=500万）
     */
    public boolean isKeyPlanning() {
        return budgetAmount != null && budgetAmount.compareTo(new BigDecimal("5000000")) >= 0;
    }
}
