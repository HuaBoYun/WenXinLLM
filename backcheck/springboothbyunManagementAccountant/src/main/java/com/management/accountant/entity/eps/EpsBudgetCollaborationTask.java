package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 预算协同编制任务实体
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eps_budget_collaboration_task")
@ApiModel(value = "EpsBudgetCollaborationTask对象", description = "预算协同编制任务")
public class EpsBudgetCollaborationTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID")
    @TableId(value = "task_id", type = IdType.ASSIGN_ID)
    private Long taskId;

    @ApiModelProperty(value = "预算体系ID")
    @TableField("system_id")
    @NotNull(message = "预算体系ID不能为空")
    private Long systemId;

    @ApiModelProperty(value = "预算版本ID")
    @TableField("version_id")
    private Long versionId;

    @ApiModelProperty(value = "任务编码")
    @TableField("task_code")
    @NotBlank(message = "任务编码不能为空")
    private String taskCode;

    @ApiModelProperty(value = "任务名称")
    @TableField("task_name")
    @NotBlank(message = "任务名称不能为空")
    private String taskName;

    @ApiModelProperty(value = "任务描述")
    @TableField("task_description")
    private String taskDescription;

    @ApiModelProperty(value = "任务类型")
    @TableField("task_type")
    private String taskType;

    @ApiModelProperty(value = "任务状态")
    @TableField("task_status")
    private String taskStatus;

    @ApiModelProperty(value = "优先级")
    @TableField("priority")
    private String priority;

    @ApiModelProperty(value = "负责人ID")
    @TableField("assignee_id")
    private Long assigneeId;

    @ApiModelProperty(value = "负责人姓名")
    @TableField("assignee_name")
    private String assigneeName;

    @ApiModelProperty(value = "创建人ID")
    @TableField("creator_id")
    private Long creatorId;

    @ApiModelProperty(value = "创建人姓名")
    @TableField("creator_name")
    private String creatorName;

    @ApiModelProperty(value = "组织ID")
    @TableField("org_id")
    private Long orgId;

    @ApiModelProperty(value = "组织名称")
    @TableField("org_name")
    private String orgName;

    @ApiModelProperty(value = "部门ID")
    @TableField("dept_id")
    private Long deptId;

    @ApiModelProperty(value = "部门名称")
    @TableField("dept_name")
    private String deptName;

    @ApiModelProperty(value = "预算科目ID")
    @TableField("subject_id")
    private Long subjectId;

    @ApiModelProperty(value = "预算科目编码")
    @TableField("subject_code")
    private String subjectCode;

    @ApiModelProperty(value = "预算科目名称")
    @TableField("subject_name")
    private String subjectName;

    @ApiModelProperty(value = "预算期间")
    @TableField("budget_period")
    private String budgetPeriod;

    @ApiModelProperty(value = "计划开始时间")
    @TableField("planned_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedStartTime;

    @ApiModelProperty(value = "计划结束时间")
    @TableField("planned_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedEndTime;

    @ApiModelProperty(value = "实际开始时间")
    @TableField("actual_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualStartTime;

    @ApiModelProperty(value = "实际结束时间")
    @TableField("actual_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualEndTime;

    @ApiModelProperty(value = "预计工时")
    @TableField("estimated_hours")
    private Integer estimatedHours;

    @ApiModelProperty(value = "实际工时")
    @TableField("actual_hours")
    private Integer actualHours;

    @ApiModelProperty(value = "完成进度")
    @TableField("progress_percentage")
    private Integer progressPercentage;

    @ApiModelProperty(value = "任务配置")
    @TableField("task_configuration")
    private String taskConfiguration;

    @ApiModelProperty(value = "协同规则")
    @TableField("collaboration_rules")
    private String collaborationRules;

    @ApiModelProperty(value = "权限配置")
    @TableField("permission_config")
    private String permissionConfig;

    @ApiModelProperty(value = "通知配置")
    @TableField("notification_config")
    private String notificationConfig;

    @ApiModelProperty(value = "模板ID")
    @TableField("template_id")
    private Long templateId;

    @ApiModelProperty(value = "模板名称")
    @TableField("template_name")
    private String templateName;

    @ApiModelProperty(value = "工作流实例ID")
    @TableField("workflow_instance_id")
    private String workflowInstanceId;

    @ApiModelProperty(value = "工作流状态")
    @TableField("workflow_status")
    private String workflowStatus;

    @ApiModelProperty(value = "审批状态")
    @TableField("approval_status")
    private String approvalStatus;

    @ApiModelProperty(value = "当前审批人ID")
    @TableField("current_approver_id")
    private Long currentApproverId;

    @ApiModelProperty(value = "当前审批人姓名")
    @TableField("current_approver_name")
    private String currentApproverName;

    @ApiModelProperty(value = "参与人数")
    @TableField("participant_count")
    private Integer participantCount;

    @ApiModelProperty(value = "完成人数")
    @TableField("completed_count")
    private Integer completedCount;

    @ApiModelProperty(value = "任务标签")
    @TableField("task_tags")
    private String taskTags;

    @ApiModelProperty(value = "任务分类")
    @TableField("task_category")
    private String taskCategory;

    @ApiModelProperty(value = "是否紧急")
    @TableField("is_urgent")
    private Boolean isUrgent;

    @ApiModelProperty(value = "是否重要")
    @TableField("is_important")
    private Boolean isImportant;

    @ApiModelProperty(value = "是否可见")
    @TableField("is_visible")
    private Boolean isVisible;

    @ApiModelProperty(value = "是否可编辑")
    @TableField("is_editable")
    private Boolean isEditable;

    @ApiModelProperty(value = "是否允许协作")
    @TableField("allow_collaboration")
    private Boolean allowCollaboration;

    @ApiModelProperty(value = "是否自动分配")
    @TableField("auto_assign")
    private Boolean autoAssign;

    @ApiModelProperty(value = "自动分配规则")
    @TableField("auto_assign_rules")
    private String autoAssignRules;

    @ApiModelProperty(value = "提醒设置")
    @TableField("reminder_settings")
    private String reminderSettings;

    @ApiModelProperty(value = "截止日期提醒")
    @TableField("deadline_reminder")
    private Integer deadlineReminder;

    @ApiModelProperty(value = "逾期处理")
    @TableField("overdue_handling")
    private String overdueHandling;

    @ApiModelProperty(value = "质量要求")
    @TableField("quality_requirements")
    private String qualityRequirements;

    @ApiModelProperty(value = "验收标准")
    @TableField("acceptance_criteria")
    private String acceptanceCriteria;

    @ApiModelProperty(value = "风险等级")
    @TableField("risk_level")
    private String riskLevel;

    @ApiModelProperty(value = "风险描述")
    @TableField("risk_description")
    private String riskDescription;

    @ApiModelProperty(value = "依赖任务")
    @TableField("dependent_tasks")
    private String dependentTasks;

    @ApiModelProperty(value = "前置条件")
    @TableField("prerequisites")
    private String prerequisites;

    @ApiModelProperty(value = "交付物")
    @TableField("deliverables")
    private String deliverables;

    @ApiModelProperty(value = "资源需求")
    @TableField("resource_requirements")
    private String resourceRequirements;

    @ApiModelProperty(value = "成本预算")
    @TableField("cost_budget")
    private Double costBudget;

    @ApiModelProperty(value = "实际成本")
    @TableField("actual_cost")
    private Double actualCost;

    @ApiModelProperty(value = "评分")
    @TableField("rating")
    private Double rating;

    @ApiModelProperty(value = "评价")
    @TableField("evaluation")
    private String evaluation;

    @ApiModelProperty(value = "备注")
    @TableField("remarks")
    private String remarks;

    @ApiModelProperty(value = "扩展字段1")
    @TableField("ext_field1")
    private String extField1;

    @ApiModelProperty(value = "扩展字段2")
    @TableField("ext_field2")
    private String extField2;

    @ApiModelProperty(value = "扩展字段3")
    @TableField("ext_field3")
    private String extField3;

    @ApiModelProperty(value = "扩展字段4")
    @TableField("ext_field4")
    private String extField4;

    @ApiModelProperty(value = "扩展字段5")
    @TableField("ext_field5")
    private String extField5;

    @ApiModelProperty(value = "排序号")
    @TableField("sort_order")
    private Integer sortOrder;

    @ApiModelProperty(value = "版本号")
    @TableField("version")
    @Version
    private Integer version;

    @ApiModelProperty(value = "是否删除")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "更新人")
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;
}
