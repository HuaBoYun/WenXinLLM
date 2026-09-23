package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目变更管理表
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_change")
public class ChangeManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 变更编号
     */
    @TableField("change_no")
    private String changeNo;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private String projectId;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 变更标题
     */
    @TableField("change_title")
    private String changeTitle;

    /**
     * 变更类型(1:设计变更,2:工程变更,3:合同变更,4:进度变更,5:成本变更,6:其他变更)
     */
    @TableField("change_type")
    private Integer changeType;

    /**
     * 变更等级(1:一级,2:二级,3:三级,4:四级,5:五级)
     */
    @TableField("change_level")
    private Integer changeLevel;

    /**
     * 变更原因
     */
    @TableField("change_reason")
    private String changeReason;

    /**
     * 变更内容
     */
    @TableField("change_content")
    private String changeContent;

    /**
     * 变更前状态
     */
    @TableField("before_change")
    private String beforeChange;

    /**
     * 变更后状态
     */
    @TableField("after_change")
    private String afterChange;

    /**
     * 变更影响分析
     */
    @TableField("impact_analysis")
    private String impactAnalysis;

    /**
     * 成本影响
     */
    @TableField("cost_impact")
    private BigDecimal costImpact;

    /**
     * 进度影响(天)
     */
    @TableField("schedule_impact")
    private Integer scheduleImpact;

    /**
     * 质量影响
     */
    @TableField("quality_impact")
    private String qualityImpact;

    /**
     * 风险影响
     */
    @TableField("risk_impact")
    private String riskImpact;

    /**
     * 申请人ID
     */
    @TableField("applicant_id")
    private Long applicantId;

    /**
     * 申请人姓名
     */
    @TableField("applicant_name")
    private String applicantName;

    /**
     * 申请时间
     */
    @TableField("apply_time")
    private Date applyTime;

    /**
     * 申请部门ID
     */
    @TableField("apply_department_id")
    private Long applyDepartmentId;

    /**
     * 申请部门名称
     */
    @TableField("apply_department_name")
    private String applyDepartmentName;

    /**
     * 变更状态(1:草稿,2:待审核,3:审核中,4:已批准,5:已拒绝,6:实施中,7:已完成,8:已取消)
     */
    @TableField("change_status")
    private Integer changeStatus;

    /**
     * 审核人ID
     */
    @TableField("reviewer_id")
    private Long reviewerId;

    /**
     * 审核人姓名
     */
    @TableField("reviewer_name")
    private String reviewerName;

    /**
     * 审核时间
     */
    @TableField("review_time")
    private Date reviewTime;

    /**
     * 审核意见
     */
    @TableField("review_comments")
    private String reviewComments;

    /**
     * 批准人ID
     */
    @TableField("approver_id")
    private Long approverId;

    /**
     * 批准人姓名
     */
    @TableField("approver_name")
    private String approverName;

    /**
     * 批准时间
     */
    @TableField("approve_time")
    private Date approveTime;

    /**
     * 批准意见
     */
    @TableField("approve_comments")
    private String approveComments;

    /**
     * 实施人ID
     */
    @TableField("implementer_id")
    private Long implementerId;

    /**
     * 实施人姓名
     */
    @TableField("implementer_name")
    private String implementerName;

    /**
     * 实施开始时间
     */
    @TableField("implement_start_time")
    private Date implementStartTime;

    /**
     * 实施结束时间
     */
    @TableField("implement_end_time")
    private Date implementEndTime;

    /**
     * 实施情况
     */
    @TableField("implement_status")
    private String implementStatus;

    /**
     * 完成度(%)
     */
    @TableField("completion_rate")
    private BigDecimal completionRate;

    /**
     * 验收人ID
     */
    @TableField("acceptor_id")
    private Long acceptorId;

    /**
     * 验收人姓名
     */
    @TableField("acceptor_name")
    private String acceptorName;

    /**
     * 验收时间
     */
    @TableField("acceptance_time")
    private Date acceptanceTime;

    /**
     * 验收结果
     */
    @TableField("acceptance_result")
    private String acceptanceResult;

    /**
     * 实际成本影响
     */
    @TableField("actual_cost_impact")
    private BigDecimal actualCostImpact;

    /**
     * 实际进度影响(天)
     */
    @TableField("actual_schedule_impact")
    private Integer actualScheduleImpact;

    /**
     * 变更效果评估
     */
    @TableField("effectiveness_evaluation")
    private String effectivenessEvaluation;

    /**
     * 经验教训
     */
    @TableField("lessons_learned")
    private String lessonsLearned;

    /**
     * 附件路径
     */
    @TableField("attachment_path")
    private String attachmentPath;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 优先级(1:高,2:中,3:低)
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 是否紧急(0:否,1:是)
     */
    @TableField("is_urgent")
    private Integer isUrgent;

    /**
     * 是否重要(0:否,1:是)
     */
    @TableField("is_important")
    private Integer isImportant;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
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
     * 删除标志(0:未删除,1:已删除)
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    /**
     * 版本号
     */
    @TableField("version")
    private Integer version;

    /**
     * 部门ID
     */
    @TableField("department_id")
    private Long departmentId;

    /**
     * 部门名称
     */
    @TableField("department_name")
    private String departmentName;

    /**
     * 组织ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @TableField("organization_name")
    private String organizationName;

    // ==================== 业务方法 ====================

    /**
     * 获取变更类型名称
     *
     * @return 变更类型名称
     */
    public String getChangeTypeName() {
        if (changeType == null) {
            return "";
        }
        switch (changeType) {
            case 1:
                return "设计变更";
            case 2:
                return "工程变更";
            case 3:
                return "合同变更";
            case 4:
                return "进度变更";
            case 5:
                return "成本变更";
            case 6:
                return "其他变更";
            default:
                return "未知类型";
        }
    }

    /**
     * 获取变更等级名称
     *
     * @return 变更等级名称
     */
    public String getChangeLevelName() {
        if (changeLevel == null) {
            return "";
        }
        switch (changeLevel) {
            case 1:
                return "一级";
            case 2:
                return "二级";
            case 3:
                return "三级";
            case 4:
                return "四级";
            case 5:
                return "五级";
            default:
                return "未知等级";
        }
    }

    /**
     * 获取变更状态名称
     *
     * @return 变更状态名称
     */
    public String getChangeStatusName() {
        if (changeStatus == null) {
            return "";
        }
        switch (changeStatus) {
            case 1:
                return "草稿";
            case 2:
                return "待审核";
            case 3:
                return "审核中";
            case 4:
                return "已批准";
            case 5:
                return "已拒绝";
            case 6:
                return "实施中";
            case 7:
                return "已完成";
            case 8:
                return "已取消";
            default:
                return "未知状态";
        }
    }

    /**
     * 获取优先级名称
     *
     * @return 优先级名称
     */
    public String getPriorityName() {
        if (priority == null) {
            return "";
        }
        switch (priority) {
            case 1:
                return "高";
            case 2:
                return "中";
            case 3:
                return "低";
            default:
                return "未知优先级";
        }
    }

    /**
     * 是否可以编辑
     *
     * @return 是否可以编辑
     */
    public boolean canEdit() {
        return changeStatus != null && (changeStatus == 1 || changeStatus == 2);
    }

    /**
     * 是否可以审核
     *
     * @return 是否可以审核
     */
    public boolean canReview() {
        return changeStatus != null && changeStatus == 2;
    }

    /**
     * 是否可以批准
     *
     * @return 是否可以批准
     */
    public boolean canApprove() {
        return changeStatus != null && changeStatus == 3;
    }

    /**
     * 是否可以实施
     *
     * @return 是否可以实施
     */
    public boolean canImplement() {
        return changeStatus != null && changeStatus == 4;
    }

    /**
     * 是否可以验收
     *
     * @return 是否可以验收
     */
    public boolean canAccept() {
        return changeStatus != null && changeStatus == 6;
    }

    /**
     * 是否已完成
     *
     * @return 是否已完成
     */
    public boolean isCompleted() {
        return changeStatus != null && changeStatus == 7;
    }

    /**
     * 是否已拒绝
     *
     * @return 是否已拒绝
     */
    public boolean isRejected() {
        return changeStatus != null && changeStatus == 5;
    }

    /**
     * 是否已取消
     *
     * @return 是否已取消
     */
    public boolean isCancelled() {
        return changeStatus != null && changeStatus == 8;
    }

    /**
     * 是否紧急变更
     *
     * @return 是否紧急变更
     */
    public boolean isUrgentChange() {
        return isUrgent != null && isUrgent == 1;
    }

    /**
     * 是否重要变更
     *
     * @return 是否重要变更
     */
    public boolean isImportantChange() {
        return isImportant != null && isImportant == 1;
    }

    /**
     * 是否高优先级
     *
     * @return 是否高优先级
     */
    public boolean isHighPriority() {
        return priority != null && priority == 1;
    }

    /**
     * 是否有成本影响
     *
     * @return 是否有成本影响
     */
    public boolean hasCostImpact() {
        return costImpact != null && costImpact.compareTo(BigDecimal.ZERO) != 0;
    }

    /**
     * 是否有进度影响
     *
     * @return 是否有进度影响
     */
    public boolean hasScheduleImpact() {
        return scheduleImpact != null && scheduleImpact != 0;
    }

    /**
     * 获取变更影响摘要
     *
     * @return 变更影响摘要
     */
    public String getImpactSummary() {
        StringBuilder summary = new StringBuilder();
        
        if (hasCostImpact()) {
            summary.append("成本影响：").append(costImpact);
        }
        
        if (hasScheduleImpact()) {
            if (summary.length() > 0) summary.append("，");
            summary.append("进度影响：").append(scheduleImpact).append("天");
        }
        
        if (summary.length() == 0) {
            summary.append("无重大影响");
        }
        
        return summary.toString();
    }

    /**
     * 获取变更进度描述
     *
     * @return 变更进度描述
     */
    public String getProgressDescription() {
        StringBuilder progress = new StringBuilder();
        progress.append(getChangeStatusName());
        
        if (completionRate != null && completionRate.compareTo(BigDecimal.ZERO) > 0) {
            progress.append("（完成度：").append(completionRate).append("%）");
        }
        
        return progress.toString();
    }

    /**
     * 是否需要关注
     *
     * @return 是否需要关注
     */
    public boolean needsAttention() {
        return isUrgentChange() || isImportantChange() || isHighPriority() || hasCostImpact() || hasScheduleImpact();
    }

    /**
     * 获取变更摘要信息
     *
     * @return 变更摘要信息
     */
    public String getChangeSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("【").append(getChangeTypeName()).append("】");
        summary.append(changeTitle);
        if (needsAttention()) {
            summary.append("（重要）");
        }
        return summary.toString();
    }
}
