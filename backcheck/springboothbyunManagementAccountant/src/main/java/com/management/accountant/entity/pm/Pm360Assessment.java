package com.management.accountant.entity.pm;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 360度评估实体类
 * 支持多维度评估：自评、上级评价、同级评价、下级评价、客户评价
 * 
 * @author 华博云AI助手
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_pm_360_assessment")
public class Pm360Assessment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评估ID
     */
    @TableId(value = "assessment_id", type = IdType.AUTO)
    private Long assessmentId;

    /**
     * 评估编码
     */
    @TableField("assessment_code")
    private String assessmentCode;

    /**
     * 评估名称
     */
    @TableField("assessment_name")
    private String assessmentName;

    /**
     * 被评估人ID
     */
    @TableField("assessed_user_id")
    private Long assessedUserId;

    /**
     * 被评估人姓名
     */
    @TableField("assessed_user_name")
    private String assessedUserName;

    /**
     * 评估周期
     */
    @TableField("assessment_period")
    private String assessmentPeriod;

    /**
     * 评估年度
     */
    @TableField("assessment_year")
    private Integer assessmentYear;

    /**
     * 评估季度
     */
    @TableField("assessment_quarter")
    private Integer assessmentQuarter;

    /**
     * 评估月份
     */
    @TableField("assessment_month")
    private Integer assessmentMonth;

    /**
     * 评估类型：ANNUAL-年度评估，QUARTERLY-季度评估，MONTHLY-月度评估，PROJECT-项目评估
     */
    @TableField("assessment_type")
    private String assessmentType;

    /**
     * 评估状态：DRAFT-草稿，ONGOING-进行中，COMPLETED-已完成，CANCELLED-已取消
     */
    @TableField("assessment_status")
    private String assessmentStatus;

    /**
     * 评估开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 评估结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

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
     * 自评分数
     */
    @TableField("self_score")
    private BigDecimal selfScore;

    /**
     * 自评权重
     */
    @TableField("self_weight")
    private BigDecimal selfWeight;

    /**
     * 上级评分
     */
    @TableField("superior_score")
    private BigDecimal superiorScore;

    /**
     * 上级评分权重
     */
    @TableField("superior_weight")
    private BigDecimal superiorWeight;

    /**
     * 同级评分
     */
    @TableField("peer_score")
    private BigDecimal peerScore;

    /**
     * 同级评分权重
     */
    @TableField("peer_weight")
    private BigDecimal peerWeight;

    /**
     * 下级评分
     */
    @TableField("subordinate_score")
    private BigDecimal subordinateScore;

    /**
     * 下级评分权重
     */
    @TableField("subordinate_weight")
    private BigDecimal subordinateWeight;

    /**
     * 客户评分
     */
    @TableField("customer_score")
    private BigDecimal customerScore;

    /**
     * 客户评分权重
     */
    @TableField("customer_weight")
    private BigDecimal customerWeight;

    /**
     * 综合得分
     */
    @TableField("total_score")
    private BigDecimal totalScore;

    /**
     * 加权平均分
     */
    @TableField("weighted_average_score")
    private BigDecimal weightedAverageScore;

    /**
     * 评估等级：EXCELLENT-优秀，GOOD-良好，FAIR-一般，POOR-较差
     */
    @TableField("assessment_grade")
    private String assessmentGrade;

    /**
     * 评估排名
     */
    @TableField("assessment_rank")
    private Integer assessmentRank;

    /**
     * 参与评估人数
     */
    @TableField("participant_count")
    private Integer participantCount;

    /**
     * 已完成评估人数
     */
    @TableField("completed_count")
    private Integer completedCount;

    /**
     * 评估完成率
     */
    @TableField("completion_rate")
    private BigDecimal completionRate;

    /**
     * 评估描述
     */
    @TableField("assessment_description")
    private String assessmentDescription;

    /**
     * 评估目标
     */
    @TableField("assessment_objectives")
    private String assessmentObjectives;

    /**
     * 评估标准
     */
    @TableField("assessment_criteria")
    private String assessmentCriteria;

    /**
     * 评估维度配置
     */
    @TableField("dimension_config")
    private String dimensionConfig;

    /**
     * 权重配置
     */
    @TableField("weight_config")
    private String weightConfig;

    /**
     * 评估结果
     */
    @TableField("assessment_result")
    private String assessmentResult;

    /**
     * 改进建议
     */
    @TableField("improvement_suggestions")
    private String improvementSuggestions;

    /**
     * 发展计划
     */
    @TableField("development_plan")
    private String developmentPlan;

    /**
     * 评估反馈
     */
    @TableField("assessment_feedback")
    private String assessmentFeedback;

    /**
     * 评估报告
     */
    @TableField("assessment_report")
    private String assessmentReport;

    /**
     * 评估数据
     */
    @TableField("assessment_data")
    private String assessmentData;

    /**
     * 评估分析
     */
    @TableField("assessment_analysis")
    private String assessmentAnalysis;

    /**
     * 评估洞察
     */
    @TableField("assessment_insights")
    private String assessmentInsights;

    /**
     * 评估趋势
     */
    @TableField("assessment_trends")
    private String assessmentTrends;

    /**
     * 对比分析
     */
    @TableField("comparison_analysis")
    private String comparisonAnalysis;

    /**
     * 基准对比
     */
    @TableField("benchmark_comparison")
    private String benchmarkComparison;

    /**
     * 历史对比
     */
    @TableField("historical_comparison")
    private String historicalComparison;

    /**
     * 同级对比
     */
    @TableField("peer_comparison")
    private String peerComparison;

    /**
     * 评估模板ID
     */
    @TableField("template_id")
    private Long templateId;

    /**
     * 评估模板名称
     */
    @TableField("template_name")
    private String templateName;

    /**
     * 评估方案ID
     */
    @TableField("plan_id")
    private Long planId;

    /**
     * 评估方案名称
     */
    @TableField("plan_name")
    private String planName;

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
     * 职位ID
     */
    @TableField("position_id")
    private Long positionId;

    /**
     * 职位名称
     */
    @TableField("position_name")
    private String positionName;

    /**
     * 评估负责人ID
     */
    @TableField("assessment_manager_id")
    private Long assessmentManagerId;

    /**
     * 评估负责人姓名
     */
    @TableField("assessment_manager_name")
    private String assessmentManagerName;

    /**
     * 评估审核人ID
     */
    @TableField("assessment_reviewer_id")
    private Long assessmentReviewerId;

    /**
     * 评估审核人姓名
     */
    @TableField("assessment_reviewer_name")
    private String assessmentReviewerName;

    /**
     * 是否启用
     */
    @TableField("is_enabled")
    private Integer isEnabled;

    /**
     * 是否可见
     */
    @TableField("is_visible")
    private Integer isVisible;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 版本号
     */
    @TableField("version")
    private Integer version;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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
    @TableField(value = "created_by_name", fill = FieldFill.INSERT)
    private String createdByName;

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
    @TableField(value = "updated_by_name", fill = FieldFill.INSERT_UPDATE)
    private String updatedByName;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
