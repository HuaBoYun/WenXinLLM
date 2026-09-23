package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 滚动预测实体类
 * 
 * @description 滚动预测管理实体，支持滚动预测的配置和执行管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_ROLLING_FORECAST")
public class BudgetRollingForecast implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 滚动预测编码
     */
    @TableField("FORECAST_CODE")
    private String forecastCode;

    /**
     * 滚动预测名称
     */
    @TableField("FORECAST_NAME")
    private String forecastName;

    /**
     * 预测类型：REVENUE-收入预测，EXPENSE-支出预测，CASH_FLOW-现金流预测，COMPREHENSIVE-综合预测
     */
    @TableField("FORECAST_TYPE")
    private String forecastType;

    /**
     * 预测方法：TREND-趋势预测，REGRESSION-回归预测，SEASONAL-季节性预测，COMBINED-组合预测
     */
    @TableField("FORECAST_METHOD")
    private String forecastMethod;

    /**
     * 预测模型：LINEAR-线性模型，EXPONENTIAL-指数模型，ARIMA-ARIMA模型，NEURAL-神经网络
     */
    @TableField("FORECAST_MODEL")
    private String forecastModel;

    /**
     * 预算年度
     */
    @TableField("FISCAL_YEAR")
    private Integer fiscalYear;

    /**
     * 预测期间数
     */
    @TableField("FORECAST_PERIODS")
    private Integer forecastPeriods;

    /**
     * 滚动频率：MONTHLY-月度，QUARTERLY-季度，SEMI_ANNUAL-半年度，ANNUAL-年度
     */
    @TableField("ROLLING_FREQUENCY")
    private String rollingFrequency;

    /**
     * 滚动窗口大小
     */
    @TableField("ROLLING_WINDOW_SIZE")
    private Integer rollingWindowSize;

    /**
     * 组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 预测范围：ORGANIZATION-组织，DEPARTMENT-部门，PROJECT-项目，ACCOUNT-科目
     */
    @TableField("FORECAST_SCOPE")
    private String forecastScope;

    /**
     * 预测维度（JSON格式）
     */
    @TableField("FORECAST_DIMENSIONS")
    private String forecastDimensions;

    /**
     * 预测状态：DRAFT-草稿，ACTIVE-激活，COMPLETED-完成，CANCELLED-取消
     */
    @TableField("FORECAST_STATUS")
    private String forecastStatus;

    /**
     * 执行状态：PENDING-待执行，RUNNING-执行中，COMPLETED-已完成，FAILED-失败
     */
    @TableField("EXECUTION_STATUS")
    private String executionStatus;

    /**
     * 数据源配置（JSON格式）
     */
    @TableField("DATA_SOURCE_CONFIG")
    private String dataSourceConfig;

    /**
     * 历史数据期间数
     */
    @TableField("HISTORICAL_PERIODS")
    private Integer historicalPeriods;

    /**
     * 预测算法参数（JSON格式）
     */
    @TableField("ALGORITHM_PARAMETERS")
    private String algorithmParameters;

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
     * 异常值处理
     */
    @TableField("OUTLIER_HANDLING")
    private Boolean outlierHandling;

    /**
     * 置信区间
     */
    @TableField("CONFIDENCE_INTERVAL")
    private BigDecimal confidenceInterval;

    /**
     * 预测精度要求
     */
    @TableField("ACCURACY_REQUIREMENT")
    private BigDecimal accuracyRequirement;

    /**
     * 自动执行
     */
    @TableField("IS_AUTO_EXECUTION")
    private Boolean isAutoExecution;

    /**
     * 执行计划（JSON格式）
     */
    @TableField("EXECUTION_SCHEDULE")
    private String executionSchedule;

    /**
     * 下次执行时间
     */
    @TableField("NEXT_EXECUTION_TIME")
    private LocalDateTime nextExecutionTime;

    /**
     * 最后执行时间
     */
    @TableField("LAST_EXECUTION_TIME")
    private LocalDateTime lastExecutionTime;

    /**
     * 执行次数
     */
    @TableField("EXECUTION_COUNT")
    private Integer executionCount;

    /**
     * 成功次数
     */
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    /**
     * 失败次数
     */
    @TableField("FAILURE_COUNT")
    private Integer failureCount;

    /**
     * 平均执行时间（毫秒）
     */
    @TableField("AVERAGE_EXECUTION_TIME")
    private Long averageExecutionTime;

    /**
     * 最后预测精度
     */
    @TableField("LAST_FORECAST_ACCURACY")
    private BigDecimal lastForecastAccuracy;

    /**
     * 平均预测精度
     */
    @TableField("AVERAGE_FORECAST_ACCURACY")
    private BigDecimal averageForecastAccuracy;

    /**
     * 预测结果存储路径
     */
    @TableField("RESULT_STORAGE_PATH")
    private String resultStoragePath;

    /**
     * 预测报告模板ID
     */
    @TableField("REPORT_TEMPLATE_ID")
    private String reportTemplateId;

    /**
     * 通知设置（JSON格式）
     */
    @TableField("NOTIFICATION_SETTINGS")
    private String notificationSettings;

    /**
     * 预测描述
     */
    @TableField("FORECAST_DESCRIPTION")
    private String forecastDescription;

    /**
     * 预测备注
     */
    @TableField("FORECAST_NOTES")
    private String forecastNotes;

    /**
     * 创建人
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 审批人
     */
    @TableField("APPROVED_BY")
    private String approvedBy;

    /**
     * 审批时间
     */
    @TableField("APPROVED_TIME")
    private LocalDateTime approvedTime;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private LocalDateTime effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRY_DATE")
    private LocalDateTime expiryDate;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
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

    // ==================== 常量定义 ====================

    /**
     * 预测类型常量
     */
    public static final String FORECAST_TYPE_REVENUE = "REVENUE";
    public static final String FORECAST_TYPE_EXPENSE = "EXPENSE";
    public static final String FORECAST_TYPE_CASH_FLOW = "CASH_FLOW";
    public static final String FORECAST_TYPE_COMPREHENSIVE = "COMPREHENSIVE";

    /**
     * 预测方法常量
     */
    public static final String FORECAST_METHOD_TREND = "TREND";
    public static final String FORECAST_METHOD_REGRESSION = "REGRESSION";
    public static final String FORECAST_METHOD_SEASONAL = "SEASONAL";
    public static final String FORECAST_METHOD_COMBINED = "COMBINED";

    /**
     * 预测模型常量
     */
    public static final String FORECAST_MODEL_LINEAR = "LINEAR";
    public static final String FORECAST_MODEL_EXPONENTIAL = "EXPONENTIAL";
    public static final String FORECAST_MODEL_ARIMA = "ARIMA";
    public static final String FORECAST_MODEL_NEURAL = "NEURAL";

    /**
     * 滚动频率常量
     */
    public static final String ROLLING_FREQUENCY_MONTHLY = "MONTHLY";
    public static final String ROLLING_FREQUENCY_QUARTERLY = "QUARTERLY";
    public static final String ROLLING_FREQUENCY_SEMI_ANNUAL = "SEMI_ANNUAL";
    public static final String ROLLING_FREQUENCY_ANNUAL = "ANNUAL";

    /**
     * 预测范围常量
     */
    public static final String FORECAST_SCOPE_ORGANIZATION = "ORGANIZATION";
    public static final String FORECAST_SCOPE_DEPARTMENT = "DEPARTMENT";
    public static final String FORECAST_SCOPE_PROJECT = "PROJECT";
    public static final String FORECAST_SCOPE_ACCOUNT = "ACCOUNT";

    /**
     * 预测状态常量
     */
    public static final String FORECAST_STATUS_DRAFT = "DRAFT";
    public static final String FORECAST_STATUS_ACTIVE = "ACTIVE";
    public static final String FORECAST_STATUS_COMPLETED = "COMPLETED";
    public static final String FORECAST_STATUS_CANCELLED = "CANCELLED";

    /**
     * 执行状态常量
     */
    public static final String EXECUTION_STATUS_PENDING = "PENDING";
    public static final String EXECUTION_STATUS_RUNNING = "RUNNING";
    public static final String EXECUTION_STATUS_COMPLETED = "COMPLETED";
    public static final String EXECUTION_STATUS_FAILED = "FAILED";
}
