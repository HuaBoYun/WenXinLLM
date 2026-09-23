package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预算分析表
 * 对应表：tbl_eps_budget_analysis
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_eps_budget_analysis")
public class EpsBudgetAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分析ID，主键
     */
    @TableId(value = "analysis_id", type = IdType.AUTO)
    private Long analysisId;

    /**
     * 所属预算体系ID
     */
    @TableField("system_id")
    private Long systemId;

    /**
     * 预算版本ID
     */
    @TableField("version_id")
    private Long versionId;

    /**
     * 分析名称
     */
    @TableField("analysis_name")
    private String analysisName;

    /**
     * 分析类型：VARIANCE-差异分析/TREND-趋势分析/RATIO-比率分析/SCENARIO-情景分析
     */
    @TableField("analysis_type")
    private String analysisType;

    /**
     * 分析维度：ORGANIZATION-组织/SUBJECT-科目/PERIOD-期间/PRODUCT-产品
     */
    @TableField("analysis_dimension")
    private String analysisDimension;

    /**
     * 分析期间
     */
    @TableField("analysis_period")
    private String analysisPeriod;

    /**
     * 期间开始日期
     */
    @TableField("period_start_date")
    private LocalDate periodStartDate;

    /**
     * 期间结束日期
     */
    @TableField("period_end_date")
    private LocalDate periodEndDate;

    /**
     * 组织ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 预算科目ID
     */
    @TableField("subject_id")
    private Long subjectId;

    /**
     * 预算金额
     */
    @TableField("budget_amount")
    private BigDecimal budgetAmount;

    /**
     * 实际金额
     */
    @TableField("actual_amount")
    private BigDecimal actualAmount;

    /**
     * 差异金额
     */
    @TableField("variance_amount")
    private BigDecimal varianceAmount;

    /**
     * 差异率（百分比）
     */
    @TableField("variance_rate")
    private BigDecimal varianceRate;

    /**
     * 执行率（百分比）
     */
    @TableField("execution_rate")
    private BigDecimal executionRate;

    /**
     * 完成率（百分比）
     */
    @TableField("completion_rate")
    private BigDecimal completionRate;

    /**
     * 同比增长率（百分比）
     */
    @TableField("yoy_growth_rate")
    private BigDecimal yoyGrowthRate;

    /**
     * 环比增长率（百分比）
     */
    @TableField("mom_growth_rate")
    private BigDecimal momGrowthRate;

    /**
     * 趋势指标
     */
    @TableField("trend_indicator")
    private String trendIndicator;

    /**
     * 风险等级：LOW-低风险/MEDIUM-中风险/HIGH-高风险/CRITICAL-严重风险
     */
    @TableField("risk_level")
    private String riskLevel;

    /**
     * 风险描述
     */
    @TableField("risk_description")
    private String riskDescription;

    /**
     * 预警状态：NORMAL-正常/WARNING-预警/ALERT-告警/CRITICAL-严重
     */
    @TableField("alert_status")
    private String alertStatus;

    /**
     * 预警消息
     */
    @TableField("alert_message")
    private String alertMessage;

    /**
     * 分析结论
     */
    @TableField("analysis_conclusion")
    private String analysisConclusion;

    /**
     * 改进建议
     */
    @TableField("improvement_suggestions")
    private String improvementSuggestions;

    /**
     * 分析方法：STATISTICAL-统计分析/ML-机器学习/AI-人工智能
     */
    @TableField("analysis_method")
    private String analysisMethod;

    /**
     * 分析模型
     */
    @TableField("analysis_model")
    private String analysisModel;

    /**
     * 模型参数，JSON格式存储
     */
    @TableField("model_parameters")
    private String modelParameters;

    /**
     * 置信度（百分比）
     */
    @TableField("confidence_level")
    private BigDecimal confidenceLevel;

    /**
     * 准确度（百分比）
     */
    @TableField("accuracy_rate")
    private BigDecimal accuracyRate;

    /**
     * 分析数据，JSON格式存储
     */
    @TableField("analysis_data")
    private String analysisData;

    /**
     * 图表配置，JSON格式存储
     */
    @TableField("chart_config")
    private String chartConfig;

    /**
     * 报告模板ID
     */
    @TableField("report_template_id")
    private Long reportTemplateId;

    /**
     * 是否自动分析：0-否/1-是
     */
    @TableField("auto_analysis")
    private Integer autoAnalysis;

    /**
     * 分析频率：DAILY-每日/WEEKLY-每周/MONTHLY-每月/QUARTERLY-每季度
     */
    @TableField("analysis_frequency")
    private String analysisFrequency;

    /**
     * 下次分析时间
     */
    @TableField("next_analysis_time")
    private LocalDateTime nextAnalysisTime;

    /**
     * 状态：DRAFT-草稿/RUNNING-运行中/COMPLETED-已完成/FAILED-失败
     */
    @TableField("status")
    private String status;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志：0-未删除/1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
