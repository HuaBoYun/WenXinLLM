package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据报送任务实体类
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("DATA_SUBMISSION_TASK")
@Schema(name="DataSubmissionTask", description="数据报送任务实体")
public class DataSubmissionTask {

    @Schema(name = "报送任务ID")
    @TableId(value = "TASK_ID", type = IdType.ASSIGN_UUID)
    private String taskId;

    @Schema(name = "企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @Schema(name = "任务名称")
    @TableField("TASK_NAME")
    private String taskName;

    @Schema(name = "任务描述")
    @TableField("TASK_DESCRIPTION")
    private String taskDescription;

    @Schema(name = "任务类型")
    @TableField("TASK_TYPE")
    private String taskType;

    @Schema(name = "报送类型")
    @TableField("SUBMISSION_TYPE")
    private String submissionType;

    @Schema(name = "监管部门")
    @TableField("REGULATORY_DEPARTMENT")
    private String regulatoryDepartment;

    @Schema(name = "任务来源")
    @TableField("TASK_SOURCE")
    private String taskSource;

    @Schema(name = "优先级")
    @TableField("PRIORITY")
    private String priority;

    @Schema(name = "任务状态")
    @TableField("TASK_STATUS")
    private String taskStatus;

    @Schema(name = "计划开始时间")
    @TableField("PLANNED_START_TIME")
    private LocalDateTime plannedStartTime;

    @Schema(name = "计划结束时间")
    @TableField("PLANNED_END_TIME")
    private LocalDateTime plannedEndTime;

    @Schema(name = "实际开始时间")
    @TableField("ACTUAL_START_TIME")
    private LocalDateTime actualStartTime;

    @Schema(name = "实际结束时间")
    @TableField("ACTUAL_END_TIME")
    private LocalDateTime actualEndTime;

    @Schema(name = "截止时间")
    @TableField("DEADLINE")
    private LocalDateTime deadline;

    @Schema(name = "完成进度")
    @TableField("COMPLETION_PROGRESS")
    private BigDecimal completionProgress;

    @Schema(name = "质量评分")
    @TableField("QUALITY_SCORE")
    private BigDecimal qualityScore;

    @Schema(name = "负责人")
    @TableField("RESPONSIBLE_PERSON")
    private String responsiblePerson;

    @Schema(name = "负责部门")
    @TableField("RESPONSIBLE_DEPARTMENT")
    private String responsibleDepartment;

    @Schema(name = "参与人员")
    @TableField("PARTICIPANTS")
    private String participants;

    @Schema(name = "数据范围")
    @TableField("DATA_SCOPE")
    private String dataScope;

    @Schema(name = "数据格式要求")
    @TableField("DATA_FORMAT_REQUIREMENTS")
    private String dataFormatRequirements;

    @Schema(name = "质量要求")
    @TableField("QUALITY_REQUIREMENTS")
    private String qualityRequirements;

    @Schema(name = "提交方式")
    @TableField("SUBMISSION_METHOD")
    private String submissionMethod;

    @Schema(name = "提交地址")
    @TableField("SUBMISSION_ADDRESS")
    private String submissionAddress;

    @Schema(name = "联系人")
    @TableField("CONTACT_PERSON")
    private String contactPerson;

    @Schema(name = "联系电话")
    @TableField("CONTACT_PHONE")
    private String contactPhone;

    @Schema(name = "联系邮箱")
    @TableField("CONTACT_EMAIL")
    private String contactEmail;

    @Schema(name = "任务附件")
    @TableField("TASK_ATTACHMENTS")
    private String taskAttachments;

    @Schema(name = "执行计划")
    @TableField("EXECUTION_PLAN")
    private String executionPlan;

    @Schema(name = "风险评估")
    @TableField("RISK_ASSESSMENT")
    private String riskAssessment;

    @Schema(name = "应急预案")
    @TableField("CONTINGENCY_PLAN")
    private String contingencyPlan;

    @Schema(name = "完成情况说明")
    @TableField("COMPLETION_DESCRIPTION")
    private String completionDescription;

    @Schema(name = "问题记录")
    @TableField("ISSUE_RECORDS")
    private String issueRecords;

    @Schema(name = "解决方案")
    @TableField("SOLUTIONS")
    private String solutions;

    @Schema(name = "经验总结")
    @TableField("LESSONS_LEARNED")
    private String lessonsLearned;

    @Schema(name = "反馈信息")
    @TableField("FEEDBACK_INFO")
    private String feedbackInfo;

    @Schema(name = "反馈时间")
    @TableField("FEEDBACK_TIME")
    private LocalDateTime feedbackTime;

    @Schema(name = "反馈处理状态")
    @TableField("FEEDBACK_STATUS")
    private String feedbackStatus;

    @Schema(name = "整改要求")
    @TableField("RECTIFICATION_REQUIREMENTS")
    private String rectificationRequirements;

    @Schema(name = "整改计划")
    @TableField("RECTIFICATION_PLAN")
    private String rectificationPlan;

    @Schema(name = "整改完成时间")
    @TableField("RECTIFICATION_COMPLETION_TIME")
    private LocalDateTime rectificationCompletionTime;

    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks;

    @Schema(name = "扩展字段1")
    @TableField("EXT_FIELD1")
    private String extField1;

    @Schema(name = "扩展字段2")
    @TableField("EXT_FIELD2")
    private String extField2;

    @Schema(name = "扩展字段3")
    @TableField("EXT_FIELD3")
    private String extField3;

    @Schema(name = "创建人")
    @TableField("CREATE_BY")
    private String createBy;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_BY")
    private String updateBy;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @Schema(name = "删除标志")
    @TableField("DEL_FLAG")
    private String delFlag;
}
