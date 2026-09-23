package com.management.accountant.entity.pm;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 绩效校准管理实体类
 * 
 * @author AI Assistant
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_pm_performance_calibration")
public class PmPerformanceCalibration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 校准ID - 主键
     */
    @TableId(value = "calibration_id", type = IdType.AUTO)
    private Long calibrationId;

    /**
     * 校准编码 - 唯一标识
     */
    @TableField("calibration_code")
    private String calibrationCode;

    /**
     * 校准标题
     */
    @TableField("calibration_title")
    private String calibrationTitle;

    /**
     * 校准类型 - ANNUAL(年度校准), QUARTERLY(季度校准), MONTHLY(月度校准), PROJECT(项目校准), SPECIAL(专项校准)
     */
    @TableField("calibration_type")
    private String calibrationType;

    /**
     * 校准状态 - PLANNED(已计划), ONGOING(进行中), COMPLETED(已完成), CANCELLED(已取消)
     */
    @TableField("calibration_status")
    private String calibrationStatus;

    /**
     * 校准年度
     */
    @TableField("calibration_year")
    private Integer calibrationYear;

    /**
     * 校准季度 (1-4)
     */
    @TableField("calibration_quarter")
    private Integer calibrationQuarter;

    /**
     * 校准月份 (1-12)
     */
    @TableField("calibration_month")
    private Integer calibrationMonth;

    /**
     * 校准范围 - COMPANY(全公司), DEPARTMENT(部门), TEAM(团队), INDIVIDUAL(个人)
     */
    @TableField("calibration_scope")
    private String calibrationScope;

    /**
     * 目标部门ID
     */
    @TableField("target_dept_id")
    private Long targetDeptId;

    /**
     * 目标部门名称
     */
    @TableField("target_dept_name")
    private String targetDeptName;

    /**
     * 校准负责人ID
     */
    @TableField("calibration_owner_id")
    private Long calibrationOwnerId;

    /**
     * 校准负责人姓名
     */
    @TableField("calibration_owner_name")
    private String calibrationOwnerName;

    /**
     * 校准委员会成员IDs (JSON数组格式)
     */
    @TableField("committee_member_ids")
    private String committeeMemberIds;

    /**
     * 校准委员会成员姓名 (JSON数组格式)
     */
    @TableField("committee_member_names")
    private String committeeMemberNames;

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
     * 校准目标
     */
    @TableField("calibration_objective")
    private String calibrationObjective;

    /**
     * 校准原则
     */
    @TableField("calibration_principles")
    private String calibrationPrinciples;

    /**
     * 校准标准
     */
    @TableField("calibration_standards")
    private String calibrationStandards;

    /**
     * 校准规则 (JSON格式)
     */
    @TableField("calibration_rules")
    private String calibrationRules;

    /**
     * 校准方法 - MEETING(会议校准), ONLINE(在线校准), HYBRID(混合校准)
     */
    @TableField("calibration_method")
    private String calibrationMethod;

    /**
     * 校准地点
     */
    @TableField("calibration_location")
    private String calibrationLocation;

    /**
     * 参与人数
     */
    @TableField("participant_count")
    private Integer participantCount;

    /**
     * 校准对象数量
     */
    @TableField("calibration_object_count")
    private Integer calibrationObjectCount;

    /**
     * 校准前平均分
     */
    @TableField("pre_calibration_avg_score")
    private BigDecimal preCalibrationAvgScore;

    /**
     * 校准后平均分
     */
    @TableField("post_calibration_avg_score")
    private BigDecimal postCalibrationAvgScore;

    /**
     * 分数调整幅度
     */
    @TableField("score_adjustment_range")
    private BigDecimal scoreAdjustmentRange;

    /**
     * 校准完成率 (%)
     */
    @TableField("completion_rate")
    private BigDecimal completionRate;

    /**
     * 一致性指数 (0-1)
     */
    @TableField("consistency_index")
    private BigDecimal consistencyIndex;

    /**
     * 校准效果评分 (1-5)
     */
    @TableField("effectiveness_rating")
    private Integer effectivenessRating;

    /**
     * 校准记录
     */
    @TableField("calibration_notes")
    private String calibrationNotes;

    /**
     * 校准总结
     */
    @TableField("calibration_summary")
    private String calibrationSummary;

    /**
     * 主要调整内容
     */
    @TableField("major_adjustments")
    private String majorAdjustments;

    /**
     * 争议处理记录
     */
    @TableField("dispute_resolution")
    private String disputeResolution;

    /**
     * 改进建议
     */
    @TableField("improvement_suggestions")
    private String improvementSuggestions;

    /**
     * 下次校准建议
     */
    @TableField("next_calibration_suggestions")
    private String nextCalibrationSuggestions;

    /**
     * 校准文档路径
     */
    @TableField("calibration_documents")
    private String calibrationDocuments;

    /**
     * 校准报告路径
     */
    @TableField("calibration_report")
    private String calibrationReport;

    /**
     * 优先级 - HIGH(高), MEDIUM(中), LOW(低)
     */
    @TableField("priority_level")
    private String priorityLevel;

    /**
     * 是否需要跟进 - 0:否, 1:是
     */
    @TableField("need_follow_up")
    private Integer needFollowUp;

    /**
     * 跟进状态 - PENDING(待跟进), IN_PROGRESS(跟进中), COMPLETED(已完成)
     */
    @TableField("follow_up_status")
    private String followUpStatus;

    /**
     * 跟进计划
     */
    @TableField("follow_up_plan")
    private String followUpPlan;

    /**
     * 跟进截止时间
     */
    @TableField("follow_up_deadline")
    private LocalDateTime followUpDeadline;

    /**
     * 通知状态 - PENDING(待通知), SENT(已发送), FAILED(发送失败)
     */
    @TableField("notification_status")
    private String notificationStatus;

    /**
     * 通知时间
     */
    @TableField("notification_time")
    private LocalDateTime notificationTime;

    /**
     * 提醒时间
     */
    @TableField("reminder_time")
    private LocalDateTime reminderTime;

    /**
     * 是否已提醒 - 0:否, 1:是
     */
    @TableField("is_reminded")
    private Integer isReminded;

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
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 是否删除 - 0:否, 1:是
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

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
     * 数据版本号
     */
    @Version
    @TableField("version")
    private Integer version;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;
}
