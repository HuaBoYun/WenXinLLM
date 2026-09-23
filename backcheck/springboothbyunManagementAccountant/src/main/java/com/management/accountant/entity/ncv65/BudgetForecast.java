package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算预测实体类
 * 
 * @description 预算预测管理实体，支持滚动预测、智能预测和多场景预测
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_FORECAST")
public class BudgetForecast implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 预测编码
     */
    @TableField("FORECAST_CODE")
    private String forecastCode;

    /**
     * 预测名称
     */
    @TableField("FORECAST_NAME")
    private String forecastName;

    /**
     * 预测简称
     */
    @TableField("FORECAST_SHORT_NAME")
    private String forecastShortName;

    /**
     * 预算年度
     */
    @TableField("FISCAL_YEAR")
    private Integer fiscalYear;

    /**
     * 预算模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 组织体系ID
     */
    @TableField("STRUCTURE_ID")
    private String structureId;

    /**
     * 组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 组织名称
     */
    @TableField("ORGANIZATION_NAME")
    private String organizationName;

    /**
     * 指标ID
     */
    @TableField("INDICATOR_ID")
    private String indicatorId;

    /**
     * 指标编码
     */
    @TableField("INDICATOR_CODE")
    private String indicatorCode;

    /**
     * 指标名称
     */
    @TableField("INDICATOR_NAME")
    private String indicatorName;

    /**
     * 维度组合（JSON格式）
     */
    @TableField("DIMENSION_COMBINATION")
    private String dimensionCombination;

    /**
     * 预测类型：rolling-滚动预测，annual-年度预测，quarterly-季度预测，monthly-月度预测
     */
    @TableField("FORECAST_TYPE")
    private String forecastType;

    /**
     * 预测方法：historical-历史趋势，regression-回归分析，ai-AI预测，expert-专家判断，hybrid-混合方法
     */
    @TableField("FORECAST_METHOD")
    private String forecastMethod;

    /**
     * 预测模型
     */
    @TableField("FORECAST_MODEL")
    private String forecastModel;

    /**
     * 预测算法
     */
    @TableField("FORECAST_ALGORITHM")
    private String forecastAlgorithm;

    /**
     * 预测期间（格式：YYYY-MM）
     */
    @TableField("FORECAST_PERIOD")
    private String forecastPeriod;

    /**
     * 预测开始期间
     */
    @TableField("FORECAST_START_PERIOD")
    private String forecastStartPeriod;

    /**
     * 预测结束期间
     */
    @TableField("FORECAST_END_PERIOD")
    private String forecastEndPeriod;

    /**
     * 期间类型：annual-年度，quarterly-季度，monthly-月度，weekly-周，daily-日
     */
    @TableField("PERIOD_TYPE")
    private String periodType;

    /**
     * 预测值
     */
    @TableField("FORECAST_VALUE")
    private BigDecimal forecastValue;

    /**
     * 预测上限
     */
    @TableField("FORECAST_UPPER_LIMIT")
    private BigDecimal forecastUpperLimit;

    /**
     * 预测下限
     */
    @TableField("FORECAST_LOWER_LIMIT")
    private BigDecimal forecastLowerLimit;

    /**
     * 置信区间上限
     */
    @TableField("CONFIDENCE_UPPER_LIMIT")
    private BigDecimal confidenceUpperLimit;

    /**
     * 置信区间下限
     */
    @TableField("CONFIDENCE_LOWER_LIMIT")
    private BigDecimal confidenceLowerLimit;

    /**
     * 置信度（百分比）
     */
    @TableField("CONFIDENCE_LEVEL")
    private BigDecimal confidenceLevel;

    /**
     * 预测准确度（百分比）
     */
    @TableField("FORECAST_ACCURACY")
    private BigDecimal forecastAccuracy;

    /**
     * 预测误差
     */
    @TableField("FORECAST_ERROR")
    private BigDecimal forecastError;

    /**
     * 币种
     */
    @TableField("CURRENCY")
    private String currency;

    /**
     * 汇率
     */
    @TableField("EXCHANGE_RATE")
    private BigDecimal exchangeRate;

    /**
     * 基准值
     */
    @TableField("BASELINE_VALUE")
    private BigDecimal baselineValue;

    /**
     * 历史数据期间数
     */
    @TableField("HISTORICAL_PERIODS")
    private Integer historicalPeriods;

    /**
     * 预测期间数
     */
    @TableField("FORECAST_PERIODS")
    private Integer forecastPeriods;

    /**
     * 季节性调整
     */
    @TableField("SEASONAL_ADJUSTMENT")
    private Boolean seasonalAdjustment;

    /**
     * 趋势调整
     */
    @TableField("TREND_ADJUSTMENT")
    private Boolean trendAdjustment;

    /**
     * 周期性调整
     */
    @TableField("CYCLICAL_ADJUSTMENT")
    private Boolean cyclicalAdjustment;

    /**
     * 预测参数（JSON格式）
     */
    @TableField("FORECAST_PARAMETERS")
    private String forecastParameters;

    /**
     * 预测假设（JSON格式）
     */
    @TableField("FORECAST_ASSUMPTIONS")
    private String forecastAssumptions;

    /**
     * 影响因素（JSON格式）
     */
    @TableField("INFLUENCE_FACTORS")
    private String influenceFactors;

    /**
     * 外部变量（JSON格式）
     */
    @TableField("EXTERNAL_VARIABLES")
    private String externalVariables;

    /**
     * 预测场景ID
     */
    @TableField("SCENARIO_ID")
    private String scenarioId;

    /**
     * 场景名称
     */
    @TableField("SCENARIO_NAME")
    private String scenarioName;

    /**
     * 场景概率（百分比）
     */
    @TableField("SCENARIO_PROBABILITY")
    private BigDecimal scenarioProbability;

    /**
     * 预测人ID
     */
    @TableField("FORECASTER_ID")
    private String forecasterId;

    /**
     * 预测人姓名
     */
    @TableField("FORECASTER_NAME")
    private String forecasterName;

    /**
     * 预测时间
     */
    @TableField("FORECAST_TIME")
    private LocalDateTime forecastTime;

    /**
     * 预测部门ID
     */
    @TableField("FORECAST_DEPT_ID")
    private String forecastDeptId;

    /**
     * 预测部门名称
     */
    @TableField("FORECAST_DEPT_NAME")
    private String forecastDeptName;

    /**
     * 审批状态：draft-草稿，submitted-已提交，approved-已审批，rejected-已拒绝
     */
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    /**
     * 审批人ID
     */
    @TableField("APPROVER_ID")
    private String approverId;

    /**
     * 审批人姓名
     */
    @TableField("APPROVER_NAME")
    private String approverName;

    /**
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    @TableField("APPROVAL_COMMENTS")
    private String approvalComments;

    /**
     * 发布状态：unpublished-未发布，published-已发布，archived-已归档
     */
    @TableField("PUBLISH_STATUS")
    private String publishStatus;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

    /**
     * 发布人ID
     */
    @TableField("PUBLISHER_ID")
    private String publisherId;

    /**
     * 发布人姓名
     */
    @TableField("PUBLISHER_NAME")
    private String publisherName;

    /**
     * 预测结果（JSON格式）
     */
    @TableField("FORECAST_RESULT")
    private String forecastResult;

    /**
     * 预测报告
     */
    @TableField("FORECAST_REPORT")
    private String forecastReport;

    /**
     * 关键洞察（JSON格式）
     */
    @TableField("KEY_INSIGHTS")
    private String keyInsights;

    /**
     * 风险提示（JSON格式）
     */
    @TableField("RISK_ALERTS")
    private String riskAlerts;

    /**
     * 建议措施（JSON格式）
     */
    @TableField("RECOMMENDATIONS")
    private String recommendations;

    /**
     * 模型评估（JSON格式）
     */
    @TableField("MODEL_EVALUATION")
    private String modelEvaluation;

    /**
     * 敏感性分析（JSON格式）
     */
    @TableField("SENSITIVITY_ANALYSIS")
    private String sensitivityAnalysis;

    /**
     * 蒙特卡洛分析（JSON格式）
     */
    @TableField("MONTE_CARLO_ANALYSIS")
    private String monteCarloAnalysis;

    /**
     * 附件信息（JSON格式）
     */
    @TableField("ATTACHMENTS")
    private String attachments;

    /**
     * 是否自动预测
     */
    @TableField("IS_AUTO_FORECAST")
    private Boolean isAutoForecast;

    /**
     * 是否滚动预测
     */
    @TableField("IS_ROLLING_FORECAST")
    private Boolean isRollingForecast;

    /**
     * 预测频率：daily-每日，weekly-每周，monthly-每月，quarterly-每季度
     */
    @TableField("FORECAST_FREQUENCY")
    private String forecastFrequency;

    /**
     * 下次预测时间
     */
    @TableField("NEXT_FORECAST_TIME")
    private LocalDateTime nextForecastTime;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用，archived-已归档
     */
    @TableField("STATUS")
    private String status;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号（乐观锁）
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 扩展字段1
     */
    @TableField("EXT_FIELD1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("EXT_FIELD2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("EXT_FIELD3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("EXT_FIELD4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("EXT_FIELD5")
    private String extField5;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // 常量定义
    public static final String FORECAST_TYPE_ROLLING = "rolling";
    public static final String FORECAST_TYPE_ANNUAL = "annual";
    public static final String FORECAST_TYPE_QUARTERLY = "quarterly";
    public static final String FORECAST_TYPE_MONTHLY = "monthly";

    public static final String FORECAST_METHOD_HISTORICAL = "historical";
    public static final String FORECAST_METHOD_REGRESSION = "regression";
    public static final String FORECAST_METHOD_AI = "ai";
    public static final String FORECAST_METHOD_EXPERT = "expert";
    public static final String FORECAST_METHOD_HYBRID = "hybrid";

    public static final String PERIOD_TYPE_ANNUAL = "annual";
    public static final String PERIOD_TYPE_QUARTERLY = "quarterly";
    public static final String PERIOD_TYPE_MONTHLY = "monthly";
    public static final String PERIOD_TYPE_WEEKLY = "weekly";
    public static final String PERIOD_TYPE_DAILY = "daily";

    public static final String APPROVAL_STATUS_DRAFT = "draft";
    public static final String APPROVAL_STATUS_SUBMITTED = "submitted";
    public static final String APPROVAL_STATUS_APPROVED = "approved";
    public static final String APPROVAL_STATUS_REJECTED = "rejected";

    public static final String PUBLISH_STATUS_UNPUBLISHED = "unpublished";
    public static final String PUBLISH_STATUS_PUBLISHED = "published";
    public static final String PUBLISH_STATUS_ARCHIVED = "archived";

    public static final String FORECAST_FREQUENCY_DAILY = "daily";
    public static final String FORECAST_FREQUENCY_WEEKLY = "weekly";
    public static final String FORECAST_FREQUENCY_MONTHLY = "monthly";
    public static final String FORECAST_FREQUENCY_QUARTERLY = "quarterly";

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_ARCHIVED = "archived";
}
