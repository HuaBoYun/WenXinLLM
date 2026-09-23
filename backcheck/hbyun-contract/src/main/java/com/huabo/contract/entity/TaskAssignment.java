package com.huabo.contract.entity;

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
 * 任务书管理表实体类
 * 对应数据库表：task_assignment
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("task_assignment")
public class TaskAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 任务书编号
     */
    @TableField("task_no")
    private String taskNo;

    /**
     * 任务书名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 任务类型(1:主要任务,2:辅助任务,3:专项任务)
     */
    @TableField("task_type")
    private Integer taskType;

    /**
     * 任务状态(1:待审核,2:已审核,3:已下达,4:执行中,5:已完成)
     */
    @TableField("task_status")
    private Integer taskStatus;

    /**
     * 任务描述
     */
    @TableField("task_description")
    private String taskDescription;

    /**
     * 任务目标
     */
    @TableField("task_objectives")
    private String taskObjectives;

    /**
     * 交付成果
     */
    @TableField("deliverables")
    private String deliverables;

    /**
     * 质量要求
     */
    @TableField("quality_requirements")
    private String qualityRequirements;

    /**
     * 时间要求
     */
    @TableField("time_requirements")
    private String timeRequirements;

    /**
     * 资源要求
     */
    @TableField("resource_requirements")
    private String resourceRequirements;

    /**
     * 技术要求
     */
    @TableField("technical_requirements")
    private String technicalRequirements;

    /**
     * 安全要求
     */
    @TableField("safety_requirements")
    private String safetyRequirements;

    /**
     * 环保要求
     */
    @TableField("environmental_requirements")
    private String environmentalRequirements;

    /**
     * 任务负责人ID
     */
    @TableField("assigned_person_id")
    private Long assignedPersonId;

    /**
     * 任务团队成员
     */
    @TableField("assigned_team_members")
    private String assignedTeamMembers;

    /**
     * 计划开始日期
     */
    @TableField("planned_start_date")
    private Date plannedStartDate;

    /**
     * 计划结束日期
     */
    @TableField("planned_end_date")
    private Date plannedEndDate;

    /**
     * 实际开始日期
     */
    @TableField("actual_start_date")
    private Date actualStartDate;

    /**
     * 实际结束日期
     */
    @TableField("actual_end_date")
    private Date actualEndDate;

    /**
     * 计划工作量(人天)
     */
    @TableField("planned_workload")
    private BigDecimal plannedWorkload;

    /**
     * 实际工作量(人天)
     */
    @TableField("actual_workload")
    private BigDecimal actualWorkload;

    /**
     * 预算金额
     */
    @TableField("budget_amount")
    private BigDecimal budgetAmount;

    /**
     * 实际费用
     */
    @TableField("actual_cost")
    private BigDecimal actualCost;

    /**
     * 完成进度(%)
     */
    @TableField("progress_percentage")
    private BigDecimal progressPercentage;

    /**
     * 下达人ID
     */
    @TableField("issuer_id")
    private Long issuerId;

    /**
     * 下达日期
     */
    @TableField("issue_date")
    private Date issueDate;

    /**
     * 审核人ID
     */
    @TableField("reviewer_id")
    private Long reviewerId;

    /**
     * 审核日期
     */
    @TableField("review_date")
    private Date reviewDate;

    /**
     * 审核意见
     */
    @TableField("review_comments")
    private String reviewComments;

    /**
     * 完成报告
     */
    @TableField("completion_report")
    private String completionReport;

    /**
     * 验收人ID
     */
    @TableField("acceptance_person_id")
    private Long acceptancePersonId;

    /**
     * 验收日期
     */
    @TableField("acceptance_date")
    private Date acceptanceDate;

    /**
     * 验收结果(1:合格,2:不合格,3:需整改)
     */
    @TableField("acceptance_result")
    private Integer acceptanceResult;

    /**
     * 验收意见
     */
    @TableField("acceptance_comments")
    private String acceptanceComments;

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
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;
}
