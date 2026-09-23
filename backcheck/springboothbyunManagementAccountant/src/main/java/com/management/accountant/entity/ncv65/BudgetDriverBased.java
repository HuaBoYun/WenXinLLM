package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 驱动因子预算实体类
 * 
 * @description 驱动因子预算管理实体，支持基于驱动因子的预算编制和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_DRIVER_BASED")
public class BudgetDriverBased implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 驱动因子预算编码
     */
    @TableField("DRIVER_BUDGET_CODE")
    private String driverBudgetCode;

    /**
     * 驱动因子预算名称
     */
    @TableField("DRIVER_BUDGET_NAME")
    private String driverBudgetName;

    /**
     * 预算年度
     */
    @TableField("FISCAL_YEAR")
    private Integer fiscalYear;

    /**
     * 组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 预算科目ID
     */
    @TableField("BUDGET_ACCOUNT_ID")
    private String budgetAccountId;

    /**
     * 驱动因子ID
     */
    @TableField("DRIVER_ID")
    private String driverId;

    /**
     * 驱动因子编码
     */
    @TableField("DRIVER_CODE")
    private String driverCode;

    /**
     * 驱动因子名称
     */
    @TableField("DRIVER_NAME")
    private String driverName;

    /**
     * 驱动因子类型：VOLUME-数量型，VALUE-价值型，RATE-比率型，TIME-时间型
     */
    @TableField("DRIVER_TYPE")
    private String driverType;

    /**
     * 驱动因子分类：REVENUE-收入驱动，COST-成本驱动，ACTIVITY-活动驱动，RESOURCE-资源驱动
     */
    @TableField("DRIVER_CATEGORY")
    private String driverCategory;

    /**
     * 计量单位
     */
    @TableField("UNIT_OF_MEASURE")
    private String unitOfMeasure;

    /**
     * 驱动因子数量
     */
    @TableField("DRIVER_QUANTITY")
    private BigDecimal driverQuantity;

    /**
     * 单位成本/价格
     */
    @TableField("UNIT_COST")
    private BigDecimal unitCost;

    /**
     * 预算金额
     */
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    /**
     * 驱动关系类型：LINEAR-线性关系，STEP-阶梯关系，EXPONENTIAL-指数关系，CUSTOM-自定义关系
     */
    @TableField("DRIVER_RELATIONSHIP_TYPE")
    private String driverRelationshipType;

    /**
     * 驱动公式
     */
    @TableField("DRIVER_FORMULA")
    private String driverFormula;

    /**
     * 驱动参数（JSON格式）
     */
    @TableField("DRIVER_PARAMETERS")
    private String driverParameters;

    /**
     * 历史数据期间数
     */
    @TableField("HISTORICAL_PERIODS")
    private Integer historicalPeriods;

    /**
     * 历史平均值
     */
    @TableField("HISTORICAL_AVERAGE")
    private BigDecimal historicalAverage;

    /**
     * 趋势系数
     */
    @TableField("TREND_COEFFICIENT")
    private BigDecimal trendCoefficient;

    /**
     * 季节性系数（JSON格式）
     */
    @TableField("SEASONAL_COEFFICIENTS")
    private String seasonalCoefficients;

    /**
     * 弹性系数
     */
    @TableField("ELASTICITY_COEFFICIENT")
    private BigDecimal elasticityCoefficient;

    /**
     * 相关性系数
     */
    @TableField("CORRELATION_COEFFICIENT")
    private BigDecimal correlationCoefficient;

    /**
     * 置信度
     */
    @TableField("CONFIDENCE_LEVEL")
    private BigDecimal confidenceLevel;

    /**
     * 预测精度
     */
    @TableField("FORECAST_ACCURACY")
    private BigDecimal forecastAccuracy;

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
     * 基准情景值
     */
    @TableField("BASE_SCENARIO_VALUE")
    private BigDecimal baseScenarioValue;

    /**
     * 乐观情景值
     */
    @TableField("OPTIMISTIC_SCENARIO_VALUE")
    private BigDecimal optimisticScenarioValue;

    /**
     * 悲观情景值
     */
    @TableField("PESSIMISTIC_SCENARIO_VALUE")
    private BigDecimal pessimisticScenarioValue;

    /**
     * 风险调整系数
     */
    @TableField("RISK_ADJUSTMENT_FACTOR")
    private BigDecimal riskAdjustmentFactor;

    /**
     * 预算状态：DRAFT-草稿，ACTIVE-激活，COMPLETED-完成，CANCELLED-取消
     */
    @TableField("BUDGET_STATUS")
    private String budgetStatus;

    /**
     * 审批状态：PENDING-待审批，APPROVED-已批准，REJECTED-已拒绝
     */
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    /**
     * 数据来源：MANUAL-手工录入，SYSTEM-系统计算，IMPORT-导入，API-接口
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 数据质量等级：HIGH-高，MEDIUM-中，LOW-低
     */
    @TableField("DATA_QUALITY_LEVEL")
    private String dataQualityLevel;

    /**
     * 最后更新数据时间
     */
    @TableField("LAST_DATA_UPDATE_TIME")
    private LocalDateTime lastDataUpdateTime;

    /**
     * 自动更新
     */
    @TableField("IS_AUTO_UPDATE")
    private Boolean isAutoUpdate;

    /**
     * 更新频率：DAILY-每日，WEEKLY-每周，MONTHLY-每月，QUARTERLY-每季
     */
    @TableField("UPDATE_FREQUENCY")
    private String updateFrequency;

    /**
     * 下次更新时间
     */
    @TableField("NEXT_UPDATE_TIME")
    private LocalDateTime nextUpdateTime;

    /**
     * 验证规则（JSON格式）
     */
    @TableField("VALIDATION_RULES")
    private String validationRules;

    /**
     * 预警阈值
     */
    @TableField("WARNING_THRESHOLD")
    private BigDecimal warningThreshold;

    /**
     * 控制阈值
     */
    @TableField("CONTROL_THRESHOLD")
    private BigDecimal controlThreshold;

    /**
     * 驱动因子描述
     */
    @TableField("DRIVER_DESCRIPTION")
    private String driverDescription;

    /**
     * 业务逻辑说明
     */
    @TableField("BUSINESS_LOGIC")
    private String businessLogic;

    /**
     * 假设条件
     */
    @TableField("ASSUMPTIONS")
    private String assumptions;

    /**
     * 限制条件
     */
    @TableField("CONSTRAINTS")
    private String constraints;

    /**
     * 负责人ID
     */
    @TableField("RESPONSIBLE_PERSON_ID")
    private String responsiblePersonId;

    /**
     * 负责人姓名
     */
    @TableField("RESPONSIBLE_PERSON_NAME")
    private String responsiblePersonName;

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
     * 审批意见
     */
    @TableField("APPROVAL_COMMENTS")
    private String approvalComments;

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
     * 驱动因子类型常量
     */
    public static final String DRIVER_TYPE_VOLUME = "VOLUME";
    public static final String DRIVER_TYPE_VALUE = "VALUE";
    public static final String DRIVER_TYPE_RATE = "RATE";
    public static final String DRIVER_TYPE_TIME = "TIME";

    /**
     * 驱动因子分类常量
     */
    public static final String DRIVER_CATEGORY_REVENUE = "REVENUE";
    public static final String DRIVER_CATEGORY_COST = "COST";
    public static final String DRIVER_CATEGORY_ACTIVITY = "ACTIVITY";
    public static final String DRIVER_CATEGORY_RESOURCE = "RESOURCE";

    /**
     * 驱动关系类型常量
     */
    public static final String DRIVER_RELATIONSHIP_TYPE_LINEAR = "LINEAR";
    public static final String DRIVER_RELATIONSHIP_TYPE_STEP = "STEP";
    public static final String DRIVER_RELATIONSHIP_TYPE_EXPONENTIAL = "EXPONENTIAL";
    public static final String DRIVER_RELATIONSHIP_TYPE_CUSTOM = "CUSTOM";

    /**
     * 预算状态常量
     */
    public static final String BUDGET_STATUS_DRAFT = "DRAFT";
    public static final String BUDGET_STATUS_ACTIVE = "ACTIVE";
    public static final String BUDGET_STATUS_COMPLETED = "COMPLETED";
    public static final String BUDGET_STATUS_CANCELLED = "CANCELLED";

    /**
     * 审批状态常量
     */
    public static final String APPROVAL_STATUS_PENDING = "PENDING";
    public static final String APPROVAL_STATUS_APPROVED = "APPROVED";
    public static final String APPROVAL_STATUS_REJECTED = "REJECTED";

    /**
     * 数据来源常量
     */
    public static final String DATA_SOURCE_MANUAL = "MANUAL";
    public static final String DATA_SOURCE_SYSTEM = "SYSTEM";
    public static final String DATA_SOURCE_IMPORT = "IMPORT";
    public static final String DATA_SOURCE_API = "API";

    /**
     * 数据质量等级常量
     */
    public static final String DATA_QUALITY_LEVEL_HIGH = "HIGH";
    public static final String DATA_QUALITY_LEVEL_MEDIUM = "MEDIUM";
    public static final String DATA_QUALITY_LEVEL_LOW = "LOW";

    /**
     * 更新频率常量
     */
    public static final String UPDATE_FREQUENCY_DAILY = "DAILY";
    public static final String UPDATE_FREQUENCY_WEEKLY = "WEEKLY";
    public static final String UPDATE_FREQUENCY_MONTHLY = "MONTHLY";
    public static final String UPDATE_FREQUENCY_QUARTERLY = "QUARTERLY";
}
