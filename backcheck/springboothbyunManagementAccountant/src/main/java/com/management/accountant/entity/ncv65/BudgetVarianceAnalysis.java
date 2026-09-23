package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算差异分析实体类
 * 
 * @description 预算差异分析实体，支持预算与实际的差异分析和原因分析
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_VARIANCE_ANALYSIS")
public class BudgetVarianceAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 分析编码
     */
    @TableField("ANALYSIS_CODE")
    private String analysisCode;

    /**
     * 分析名称
     */
    @TableField("ANALYSIS_NAME")
    private String analysisName;

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
     * 分析期间（格式：YYYY-MM）
     */
    @TableField("ANALYSIS_PERIOD")
    private String analysisPeriod;

    /**
     * 期间类型：annual-年度，quarterly-季度，monthly-月度，weekly-周，daily-日
     */
    @TableField("PERIOD_TYPE")
    private String periodType;

    /**
     * 分析类型：budget_actual-预算与实际，budget_forecast-预算与预测，period_comparison-期间对比，version_comparison-版本对比
     */
    @TableField("ANALYSIS_TYPE")
    private String analysisType;

    /**
     * 分析维度：amount-金额差异，ratio-比率差异，trend-趋势差异，structure-结构差异
     */
    @TableField("ANALYSIS_DIMENSION")
    private String analysisDimension;

    /**
     * 预算金额
     */
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    /**
     * 实际金额
     */
    @TableField("ACTUAL_AMOUNT")
    private BigDecimal actualAmount;

    /**
     * 预测金额
     */
    @TableField("FORECAST_AMOUNT")
    private BigDecimal forecastAmount;

    /**
     * 差异金额
     */
    @TableField("VARIANCE_AMOUNT")
    private BigDecimal varianceAmount;

    /**
     * 差异率（百分比）
     */
    @TableField("VARIANCE_RATE")
    private BigDecimal varianceRate;

    /**
     * 绝对差异金额
     */
    @TableField("ABSOLUTE_VARIANCE")
    private BigDecimal absoluteVariance;

    /**
     * 相对差异率（百分比）
     */
    @TableField("RELATIVE_VARIANCE")
    private BigDecimal relativeVariance;

    /**
     * 累计差异金额
     */
    @TableField("CUMULATIVE_VARIANCE")
    private BigDecimal cumulativeVariance;

    /**
     * 累计差异率（百分比）
     */
    @TableField("CUMULATIVE_VARIANCE_RATE")
    private BigDecimal cumulativeVarianceRate;

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
     * 差异等级：normal-正常，warning-预警，critical-严重，emergency-紧急
     */
    @TableField("VARIANCE_LEVEL")
    private String varianceLevel;

    /**
     * 差异状态：favorable-有利差异，unfavorable-不利差异，neutral-中性差异
     */
    @TableField("VARIANCE_STATUS")
    private String varianceStatus;

    /**
     * 差异趋势：improving-改善，deteriorating-恶化，stable-稳定，volatile-波动
     */
    @TableField("VARIANCE_TREND")
    private String varianceTrend;

    /**
     * 差异原因分类：internal-内部原因，external-外部原因，policy-政策原因，market-市场原因
     */
    @TableField("VARIANCE_CAUSE_TYPE")
    private String varianceCauseType;

    /**
     * 差异原因分析
     */
    @TableField("VARIANCE_CAUSE_ANALYSIS")
    private String varianceCauseAnalysis;

    /**
     * 主要影响因素（JSON格式）
     */
    @TableField("MAIN_FACTORS")
    private String mainFactors;

    /**
     * 次要影响因素（JSON格式）
     */
    @TableField("SECONDARY_FACTORS")
    private String secondaryFactors;

    /**
     * 根本原因分析
     */
    @TableField("ROOT_CAUSE_ANALYSIS")
    private String rootCauseAnalysis;

    /**
     * 改进建议
     */
    @TableField("IMPROVEMENT_SUGGESTIONS")
    private String improvementSuggestions;

    /**
     * 纠正措施（JSON格式）
     */
    @TableField("CORRECTIVE_ACTIONS")
    private String correctiveActions;

    /**
     * 预防措施（JSON格式）
     */
    @TableField("PREVENTIVE_ACTIONS")
    private String preventiveActions;

    /**
     * 责任部门ID
     */
    @TableField("RESPONSIBLE_DEPT_ID")
    private String responsibleDeptId;

    /**
     * 责任部门名称
     */
    @TableField("RESPONSIBLE_DEPT_NAME")
    private String responsibleDeptName;

    /**
     * 责任人ID
     */
    @TableField("RESPONSIBLE_PERSON_ID")
    private String responsiblePersonId;

    /**
     * 责任人姓名
     */
    @TableField("RESPONSIBLE_PERSON_NAME")
    private String responsiblePersonName;

    /**
     * 分析人ID
     */
    @TableField("ANALYST_ID")
    private String analystId;

    /**
     * 分析人姓名
     */
    @TableField("ANALYST_NAME")
    private String analystName;

    /**
     * 分析时间
     */
    @TableField("ANALYSIS_TIME")
    private LocalDateTime analysisTime;

    /**
     * 分析方法：manual-手工分析，automatic-自动分析，ai-AI分析
     */
    @TableField("ANALYSIS_METHOD")
    private String analysisMethod;

    /**
     * 分析模型
     */
    @TableField("ANALYSIS_MODEL")
    private String analysisModel;

    /**
     * 分析参数（JSON格式）
     */
    @TableField("ANALYSIS_PARAMETERS")
    private String analysisParameters;

    /**
     * 分析结果（JSON格式）
     */
    @TableField("ANALYSIS_RESULT")
    private String analysisResult;

    /**
     * 分析报告
     */
    @TableField("ANALYSIS_REPORT")
    private String analysisReport;

    /**
     * 关键发现（JSON格式）
     */
    @TableField("KEY_FINDINGS")
    private String keyFindings;

    /**
     * 风险评估（JSON格式）
     */
    @TableField("RISK_ASSESSMENT")
    private String riskAssessment;

    /**
     * 历史对比数据（JSON格式）
     */
    @TableField("HISTORICAL_COMPARISON")
    private String historicalComparison;

    /**
     * 同期对比数据（JSON格式）
     */
    @TableField("PEER_COMPARISON")
    private String peerComparison;

    /**
     * 基准对比数据（JSON格式）
     */
    @TableField("BENCHMARK_COMPARISON")
    private String benchmarkComparison;

    /**
     * 敏感性分析（JSON格式）
     */
    @TableField("SENSITIVITY_ANALYSIS")
    private String sensitivityAnalysis;

    /**
     * 情景分析（JSON格式）
     */
    @TableField("SCENARIO_ANALYSIS")
    private String scenarioAnalysis;

    /**
     * 附件信息（JSON格式）
     */
    @TableField("ATTACHMENTS")
    private String attachments;

    /**
     * 是否自动分析
     */
    @TableField("IS_AUTO_ANALYSIS")
    private Boolean isAutoAnalysis;

    /**
     * 是否定期分析
     */
    @TableField("IS_PERIODIC_ANALYSIS")
    private Boolean isPeriodicAnalysis;

    /**
     * 分析频率：daily-每日，weekly-每周，monthly-每月，quarterly-每季度
     */
    @TableField("ANALYSIS_FREQUENCY")
    private String analysisFrequency;

    /**
     * 下次分析时间
     */
    @TableField("NEXT_ANALYSIS_TIME")
    private LocalDateTime nextAnalysisTime;

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
    public static final String PERIOD_TYPE_ANNUAL = "annual";
    public static final String PERIOD_TYPE_QUARTERLY = "quarterly";
    public static final String PERIOD_TYPE_MONTHLY = "monthly";
    public static final String PERIOD_TYPE_WEEKLY = "weekly";
    public static final String PERIOD_TYPE_DAILY = "daily";

    public static final String ANALYSIS_TYPE_BUDGET_ACTUAL = "budget_actual";
    public static final String ANALYSIS_TYPE_BUDGET_FORECAST = "budget_forecast";
    public static final String ANALYSIS_TYPE_PERIOD_COMPARISON = "period_comparison";
    public static final String ANALYSIS_TYPE_VERSION_COMPARISON = "version_comparison";

    public static final String ANALYSIS_DIMENSION_AMOUNT = "amount";
    public static final String ANALYSIS_DIMENSION_RATIO = "ratio";
    public static final String ANALYSIS_DIMENSION_TREND = "trend";
    public static final String ANALYSIS_DIMENSION_STRUCTURE = "structure";

    public static final String VARIANCE_LEVEL_NORMAL = "normal";
    public static final String VARIANCE_LEVEL_WARNING = "warning";
    public static final String VARIANCE_LEVEL_CRITICAL = "critical";
    public static final String VARIANCE_LEVEL_EMERGENCY = "emergency";

    public static final String VARIANCE_STATUS_FAVORABLE = "favorable";
    public static final String VARIANCE_STATUS_UNFAVORABLE = "unfavorable";
    public static final String VARIANCE_STATUS_NEUTRAL = "neutral";

    public static final String VARIANCE_TREND_IMPROVING = "improving";
    public static final String VARIANCE_TREND_DETERIORATING = "deteriorating";
    public static final String VARIANCE_TREND_STABLE = "stable";
    public static final String VARIANCE_TREND_VOLATILE = "volatile";

    public static final String VARIANCE_CAUSE_TYPE_INTERNAL = "internal";
    public static final String VARIANCE_CAUSE_TYPE_EXTERNAL = "external";
    public static final String VARIANCE_CAUSE_TYPE_POLICY = "policy";
    public static final String VARIANCE_CAUSE_TYPE_MARKET = "market";

    public static final String ANALYSIS_METHOD_MANUAL = "manual";
    public static final String ANALYSIS_METHOD_AUTOMATIC = "automatic";
    public static final String ANALYSIS_METHOD_AI = "ai";

    public static final String ANALYSIS_FREQUENCY_DAILY = "daily";
    public static final String ANALYSIS_FREQUENCY_WEEKLY = "weekly";
    public static final String ANALYSIS_FREQUENCY_MONTHLY = "monthly";
    public static final String ANALYSIS_FREQUENCY_QUARTERLY = "quarterly";

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_ARCHIVED = "archived";
}
