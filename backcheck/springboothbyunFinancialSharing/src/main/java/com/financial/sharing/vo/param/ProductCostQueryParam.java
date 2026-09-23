package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品成本查询参数类
 * 
 * @author AI Agent
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductCostQueryParam extends PageableParam {

    // ========== 基础查询字段 ==========

    /**
     * 成本核算ID
     */
    private Long costingId;

    /**
     * 成本核算ID列表
     */
    private List<Long> costingIds;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品ID列表
     */
    private List<Long> productIds;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 产品编码列表
     */
    private List<String> productCodes;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品名称模糊查询
     */
    private String productNameLike;

    /**
     * 产品类型
     */
    private Integer productType;

    /**
     * 产品类型列表
     */
    private List<Integer> productTypes;

    /**
     * 核算期间
     */
    private String costingPeriod;

    /**
     * 核算期间列表
     */
    private List<String> costingPeriods;

    /**
     * 核算期间开始
     */
    private String costingPeriodStart;

    /**
     * 核算期间结束
     */
    private String costingPeriodEnd;

    /**
     * 核算方法
     */
    private Integer costingMethod;

    /**
     * 核算方法列表
     */
    private List<Integer> costingMethods;

    /**
     * 核算状态
     */
    private Integer costingStatus;

    /**
     * 核算状态列表
     */
    private List<Integer> costingStatuses;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 账簿ID列表
     */
    private List<Long> bookIds;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    // ========== 成本金额查询字段 ==========

    /**
     * 总成本最小值
     */
    private BigDecimal totalCostMin;

    /**
     * 总成本最大值
     */
    private BigDecimal totalCostMax;

    /**
     * 单位成本最小值
     */
    private BigDecimal unitCostMin;

    /**
     * 单位成本最大值
     */
    private BigDecimal unitCostMax;

    /**
     * 直接材料成本最小值
     */
    private BigDecimal directMaterialMin;

    /**
     * 直接材料成本最大值
     */
    private BigDecimal directMaterialMax;

    /**
     * 直接人工成本最小值
     */
    private BigDecimal directLaborMin;

    /**
     * 直接人工成本最大值
     */
    private BigDecimal directLaborMax;

    /**
     * 制造费用最小值
     */
    private BigDecimal manufacturingOverheadMin;

    /**
     * 制造费用最大值
     */
    private BigDecimal manufacturingOverheadMax;

    /**
     * 标准成本最小值
     */
    private BigDecimal standardCostMin;

    /**
     * 标准成本最大值
     */
    private BigDecimal standardCostMax;

    /**
     * 预算成本最小值
     */
    private BigDecimal budgetCostMin;

    /**
     * 预算成本最大值
     */
    private BigDecimal budgetCostMax;

    /**
     * 成本差异最小值
     */
    private BigDecimal costVarianceMin;

    /**
     * 成本差异最大值
     */
    private BigDecimal costVarianceMax;

    // ========== 生产相关查询字段 ==========

    /**
     * 生产数量最小值
     */
    private BigDecimal productionQuantityMin;

    /**
     * 生产数量最大值
     */
    private BigDecimal productionQuantityMax;

    /**
     * 成本中心ID
     */
    private Long costCenterId;

    /**
     * 成本中心ID列表
     */
    private List<Long> costCenterIds;

    /**
     * 成本中心名称
     */
    private String costCenterName;

    /**
     * 工艺路线ID
     */
    private Long processRouteId;

    /**
     * BOM版本
     */
    private String bomVersion;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 工单号
     */
    private String workOrderNo;

    /**
     * 开工日期开始
     */
    private LocalDateTime startDateBegin;

    /**
     * 开工日期结束
     */
    private LocalDateTime startDateEnd;

    /**
     * 完工日期开始
     */
    private LocalDateTime finishDateBegin;

    /**
     * 完工日期结束
     */
    private LocalDateTime finishDateEnd;

    // ========== 审核相关查询字段 ==========

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 审核状态列表
     */
    private List<Integer> auditStatuses;

    /**
     * 审核人
     */
    private Long auditor;

    /**
     * 审核人列表
     */
    private List<Long> auditors;

    /**
     * 审核时间开始
     */
    private LocalDateTime auditTimeBegin;

    /**
     * 审核时间结束
     */
    private LocalDateTime auditTimeEnd;

    // ========== 凭证相关查询字段 ==========

    /**
     * 凭证ID
     */
    private Long voucherId;

    /**
     * 凭证号
     */
    private String voucherNo;

    /**
     * 是否生成凭证
     */
    private Integer isVoucherGenerated;

    // ========== 时间查询字段 ==========

    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeBegin;

    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;

    /**
     * 更新时间开始
     */
    private LocalDateTime updateTimeBegin;

    /**
     * 更新时间结束
     */
    private LocalDateTime updateTimeEnd;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 创建人列表
     */
    private List<Long> creators;

    /**
     * 更新人
     */
    private Long updater;

    /**
     * 更新人列表
     */
    private List<Long> updaters;

    // ========== 成本分析查询字段 ==========

    /**
     * 分析类型
     */
    private String analysisType;

    /**
     * 分析期间开始
     */
    private String analysisPeriodStart;

    /**
     * 分析期间结束
     */
    private String analysisPeriodEnd;

    /**
     * 对比基准期间
     */
    private String baselinePeriod;

    /**
     * 对比目标期间
     */
    private String targetPeriod;

    /**
     * 差异阈值
     */
    private BigDecimal varianceThreshold;

    /**
     * 差异率阈值
     */
    private BigDecimal varianceRateThreshold;

    // ========== 成本控制查询字段 ==========

    /**
     * 成本控制状态
     */
    private Integer costControlStatus;

    /**
     * 成本控制状态列表
     */
    private List<Integer> costControlStatuses;

    /**
     * 成本预警阈值
     */
    private BigDecimal costAlertThreshold;

    /**
     * 成本风险等级
     */
    private Integer costRiskLevel;

    /**
     * 成本风险等级列表
     */
    private List<Integer> costRiskLevels;

    // ========== 成本性态查询字段 ==========

    /**
     * 成本性态
     */
    private Integer costBehavior;

    /**
     * 成本性态列表
     */
    private List<Integer> costBehaviors;

    /**
     * 可控性
     */
    private Integer controllability;

    /**
     * 可控性列表
     */
    private List<Integer> controllabilities;

    /**
     * 相关性
     */
    private Integer relevance;

    /**
     * 相关性列表
     */
    private List<Integer> relevances;

    /**
     * 可追溯性
     */
    private Integer traceability;

    /**
     * 可追溯性列表
     */
    private List<Integer> traceabilities;

    // ========== 成本分摊查询字段 ==========

    /**
     * 分摊基础
     */
    private String allocationBasis;

    /**
     * 分摊基础列表
     */
    private List<String> allocationBases;

    /**
     * 成本归集方式
     */
    private String costCollectionMethod;

    /**
     * 成本分配方式
     */
    private String costAllocationMethod;

    /**
     * 成本驱动因子
     */
    private String costDriver;

    /**
     * 成本对象
     */
    private String costObject;

    /**
     * 成本库
     */
    private String costPool;

    /**
     * 成本层次
     */
    private Integer costLevel;

    // ========== 报告查询字段 ==========

    /**
     * 报告类型
     */
    private String reportType;

    /**
     * 报告期间
     */
    private String reportPeriod;

    /**
     * 报告期间开始
     */
    private String reportPeriodStart;

    /**
     * 报告期间结束
     */
    private String reportPeriodEnd;

    /**
     * 是否包含图表
     */
    private Boolean includeCharts;

    /**
     * 报告格式
     */
    private String reportFormat;

    /**
     * 报告格式列表
     */
    private List<String> reportFormats;

    // ========== 导入导出查询字段 ==========

    /**
     * 导出类型
     */
    private String exportType;

    /**
     * 导出格式
     */
    private String exportFormat;

    /**
     * 是否包含明细
     */
    private Boolean includeDetails;

    /**
     * 是否包含汇总
     */
    private Boolean includeSummary;

    // ========== 排序字段 ==========

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向
     */
    private String orderDirection;

    /**
     * 多字段排序
     */
    private List<String> orderFields;

    // ========== 分组字段 ==========

    /**
     * 分组字段
     */
    private String groupBy;

    /**
     * 分组字段列表
     */
    private List<String> groupFields;

    // ========== 聚合字段 ==========

    /**
     * 聚合函数
     */
    private String aggregateFunction;

    /**
     * 聚合字段
     */
    private String aggregateField;

    /**
     * 聚合字段列表
     */
    private List<String> aggregateFields;

    // ========== 扩展查询字段 ==========

    /**
     * 扩展字段1
     */
    private String extField1;

    /**
     * 扩展字段2
     */
    private String extField2;

    /**
     * 扩展字段3
     */
    private String extField3;

    /**
     * 扩展字段4
     */
    private String extField4;

    /**
     * 扩展字段5
     */
    private String extField5;

    /**
     * 自定义查询条件
     */
    private String customCondition;

    /**
     * 自定义查询参数
     */
    private Object customParams;

    /**
     * 是否启用缓存
     */
    private Boolean enableCache;

    /**
     * 缓存过期时间（秒）
     */
    private Integer cacheExpireTime;

    /**
     * 查询超时时间（秒）
     */
    private Integer queryTimeout;

    /**
     * 是否异步查询
     */
    private Boolean asyncQuery;

    /**
     * 查询优先级
     */
    private Integer queryPriority;

    /**
     * 数据权限过滤
     */
    private Boolean dataPermissionFilter;

    /**
     * 用户ID（用于数据权限）
     */
    private Long userId;

    /**
     * 部门ID（用于数据权限）
     */
    private Long deptId;

    /**
     * 角色ID（用于数据权限）
     */
    private Long roleId;
}
