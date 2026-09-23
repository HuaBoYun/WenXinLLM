package com.management.accountant.entity.pm;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 绩效面谈管理实体类
 * 
 * @author AI Assistant
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_pm_performance_interview")
public class PmPerformanceInterview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 面谈ID
     */
    @TableId(value = "interview_id", type = IdType.AUTO)
    private Long interviewId;

    /**
     * 面谈编码
     */
    @TableField("interview_code")
    private String interviewCode;

    /**
     * 面谈标题
     */
    @TableField("interview_title")
    private String interviewTitle;

    /**
     * 面谈类型 (ANNUAL-年度面谈, QUARTERLY-季度面谈, MONTHLY-月度面谈, PROJECT-项目面谈, SPECIAL-专项面谈)
     */
    @TableField("interview_type")
    private String interviewType;

    /**
     * 面谈状态 (PLANNED-已计划, SCHEDULED-已安排, ONGOING-进行中, COMPLETED-已完成, CANCELLED-已取消, POSTPONED-已延期)
     */
    @TableField("interview_status")
    private String interviewStatus;

    /**
     * 被面谈人ID
     */
    @TableField("interviewee_id")
    private Long intervieweeId;

    /**
     * 被面谈人姓名
     */
    @TableField("interviewee_name")
    private String intervieweeName;

    /**
     * 被面谈人部门ID
     */
    @TableField("interviewee_dept_id")
    private Long intervieweeDeptId;

    /**
     * 被面谈人部门名称
     */
    @TableField("interviewee_dept_name")
    private String intervieweeDeptName;

    /**
     * 被面谈人职位
     */
    @TableField("interviewee_position")
    private String intervieweePosition;

    /**
     * 面谈官ID
     */
    @TableField("interviewer_id")
    private Long interviewerId;

    /**
     * 面谈官姓名
     */
    @TableField("interviewer_name")
    private String interviewerName;

    /**
     * 面谈官部门ID
     */
    @TableField("interviewer_dept_id")
    private Long interviewerDeptId;

    /**
     * 面谈官部门名称
     */
    @TableField("interviewer_dept_name")
    private String interviewerDeptName;

    /**
     * 面谈官职位
     */
    @TableField("interviewer_position")
    private String interviewerPosition;

    /**
     * 面谈年度
     */
    @TableField("interview_year")
    private Integer interviewYear;

    /**
     * 面谈季度 (1-4)
     */
    @TableField("interview_quarter")
    private Integer interviewQuarter;

    /**
     * 面谈月份 (1-12)
     */
    @TableField("interview_month")
    private Integer interviewMonth;

    /**
     * 计划开始时间
     */
    @TableField("planned_start_time")
    private LocalDateTime plannedStartTime;

    /**
     * 计划结束时间
     */
    @TableField("planned_end_time")
    private LocalDateTime plannedEndTime;

    /**
     * 实际开始时间
     */
    @TableField("actual_start_time")
    private LocalDateTime actualStartTime;

    /**
     * 实际结束时间
     */
    @TableField("actual_end_time")
    private LocalDateTime actualEndTime;

    /**
     * 面谈地点
     */
    @TableField("interview_location")
    private String interviewLocation;

    /**
     * 面谈方式 (FACE_TO_FACE-面对面, VIDEO-视频, PHONE-电话, ONLINE-在线)
     */
    @TableField("interview_method")
    private String interviewMethod;

    /**
     * 面谈目的
     */
    @TableField("interview_purpose")
    private String interviewPurpose;

    /**
     * 面谈议程
     */
    @TableField("interview_agenda")
    private String interviewAgenda;

    /**
     * 面谈准备事项
     */
    @TableField("interview_preparation")
    private String interviewPreparation;

    /**
     * 绩效评分
     */
    @TableField("performance_score")
    private BigDecimal performanceScore;

    /**
     * 绩效等级 (EXCELLENT-优秀, GOOD-良好, FAIR-一般, POOR-较差)
     */
    @TableField("performance_grade")
    private String performanceGrade;

    /**
     * 工作表现总结
     */
    @TableField("performance_summary")
    private String performanceSummary;

    /**
     * 主要成就
     */
    @TableField("key_achievements")
    private String keyAchievements;

    /**
     * 改进领域
     */
    @TableField("improvement_areas")
    private String improvementAreas;

    /**
     * 发展目标
     */
    @TableField("development_goals")
    private String developmentGoals;

    /**
     * 培训需求
     */
    @TableField("training_needs")
    private String trainingNeeds;

    /**
     * 职业规划
     */
    @TableField("career_planning")
    private String careerPlanning;

    /**
     * 员工反馈
     */
    @TableField("employee_feedback")
    private String employeeFeedback;

    /**
     * 管理者反馈
     */
    @TableField("manager_feedback")
    private String managerFeedback;

    /**
     * 改进计划
     */
    @TableField("improvement_plan")
    private String improvementPlan;

    /**
     * 下次面谈计划
     */
    @TableField("next_interview_plan")
    private String nextInterviewPlan;

    /**
     * 面谈记录
     */
    @TableField("interview_notes")
    private String interviewNotes;

    /**
     * 面谈总结
     */
    @TableField("interview_summary")
    private String interviewSummary;

    /**
     * 行动计划
     */
    @TableField("action_plan")
    private String actionPlan;

    /**
     * 跟踪计划
     */
    @TableField("follow_up_plan")
    private String followUpPlan;

    /**
     * 面谈满意度 (1-5分)
     */
    @TableField("satisfaction_rating")
    private Integer satisfactionRating;

    /**
     * 面谈效果评价
     */
    @TableField("effectiveness_evaluation")
    private String effectivenessEvaluation;

    /**
     * 是否需要跟进 (0-否, 1-是)
     */
    @TableField("need_follow_up")
    private Integer needFollowUp;

    /**
     * 跟进状态 (PENDING-待跟进, IN_PROGRESS-跟进中, COMPLETED-已完成)
     */
    @TableField("follow_up_status")
    private String followUpStatus;

    /**
     * 跟进截止时间
     */
    @TableField("follow_up_deadline")
    private LocalDateTime followUpDeadline;

    /**
     * 面谈模板ID
     */
    @TableField("template_id")
    private Long templateId;

    /**
     * 面谈模板名称
     */
    @TableField("template_name")
    private String templateName;

    /**
     * 关联评估ID
     */
    @TableField("assessment_id")
    private Long assessmentId;

    /**
     * 关联目标ID
     */
    @TableField("target_id")
    private Long targetId;

    /**
     * 面谈权重
     */
    @TableField("interview_weight")
    private BigDecimal interviewWeight;

    /**
     * 面谈得分
     */
    @TableField("interview_score")
    private BigDecimal interviewScore;

    /**
     * 是否公开 (0-否, 1-是)
     */
    @TableField("is_public")
    private Integer isPublic;

    /**
     * 是否匿名 (0-否, 1-是)
     */
    @TableField("is_anonymous")
    private Integer isAnonymous;

    /**
     * 面谈优先级 (HIGH-高, MEDIUM-中, LOW-低)
     */
    @TableField("priority_level")
    private String priorityLevel;

    /**
     * 面谈标签
     */
    @TableField("interview_tags")
    private String interviewTags;

    /**
     * 附件路径
     */
    @TableField("attachment_path")
    private String attachmentPath;

    /**
     * 备注信息
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
     * 创建人ID
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 创建人姓名
     */
    @TableField(value = "created_name", fill = FieldFill.INSERT)
    private String createdName;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 更新人姓名
     */
    @TableField(value = "updated_name", fill = FieldFill.INSERT_UPDATE)
    private String updatedName;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志 (0-未删除, 1-已删除)
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    /**
     * 版本号
     */
    @TableField("version")
    @Version
    private Integer version;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;
}
