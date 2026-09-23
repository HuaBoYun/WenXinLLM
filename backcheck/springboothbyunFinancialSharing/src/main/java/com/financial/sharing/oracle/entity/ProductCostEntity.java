package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品成本实体类 - Oracle/达梦版本
 * 
 * @author AI Agent
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_PRODUCT_COST")
public class ProductCostEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成本核算ID
     */
    @TableId(value = "COSTING_ID", type = IdType.AUTO)
    private Long costingId;

    /**
     * 产品ID
     */
    @TableField("PRODUCT_ID")
    private Long productId;

    /**
     * 产品编码
     */
    @TableField("PRODUCT_CODE")
    private String productCode;

    /**
     * 产品名称
     */
    @TableField("PRODUCT_NAME")
    private String productName;

    /**
     * 核算期间
     */
    @TableField("COSTING_PERIOD")
    private String costingPeriod;

    /**
     * 核算方法(1品种法2分批法3分步法)
     */
    @TableField("COSTING_METHOD")
    private Integer costingMethod;

    /**
     * 直接材料成本
     */
    @TableField("DIRECT_MATERIAL")
    private BigDecimal directMaterial;

    /**
     * 直接人工成本
     */
    @TableField("DIRECT_LABOR")
    private BigDecimal directLabor;

    /**
     * 制造费用
     */
    @TableField("MANUFACTURING_OVERHEAD")
    private BigDecimal manufacturingOverhead;

    /**
     * 总成本
     */
    @TableField("TOTAL_COST")
    private BigDecimal totalCost;

    /**
     * 单位成本
     */
    @TableField("UNIT_COST")
    private BigDecimal unitCost;

    /**
     * 生产数量
     */
    @TableField("PRODUCTION_QUANTITY")
    private BigDecimal productionQuantity;

    /**
     * 核算状态(1核算中2已完成)
     */
    @TableField("COSTING_STATUS")
    private Integer costingStatus;

    /**
     * 账簿ID
     */
    @TableField("BOOK_ID")
    private Long bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 版本号
     */
    @TableField("VERSION")
    private Integer version;

    /**
     * 删除标识(0否1是)
     */
    @TableField("IS_DELETED")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("CREATOR")
    private Long creator;

    /**
     * 更新人
     */
    @TableField("UPDATER")
    private Long updater;

    // ========== 扩展字段 ==========

    /**
     * 产品类型
     */
    @TableField("PRODUCT_TYPE")
    private Integer productType;

    /**
     * 产品规格
     */
    @TableField("PRODUCT_SPECIFICATION")
    private String productSpecification;

    /**
     * 计量单位
     */
    @TableField("UNIT")
    private String unit;

    /**
     * 标准成本
     */
    @TableField("STANDARD_COST")
    private BigDecimal standardCost;

    /**
     * 预算成本
     */
    @TableField("BUDGET_COST")
    private BigDecimal budgetCost;

    /**
     * 成本差异
     */
    @TableField("COST_VARIANCE")
    private BigDecimal costVariance;

    /**
     * 差异率
     */
    @TableField("VARIANCE_RATE")
    private BigDecimal varianceRate;

    /**
     * 材料成本占比
     */
    @TableField("MATERIAL_RATIO")
    private BigDecimal materialRatio;

    /**
     * 人工成本占比
     */
    @TableField("LABOR_RATIO")
    private BigDecimal laborRatio;

    /**
     * 费用成本占比
     */
    @TableField("OVERHEAD_RATIO")
    private BigDecimal overheadRatio;

    /**
     * 成本中心ID
     */
    @TableField("COST_CENTER_ID")
    private Long costCenterId;

    /**
     * 成本中心名称
     */
    @TableField("COST_CENTER_NAME")
    private String costCenterName;

    /**
     * 工艺路线ID
     */
    @TableField("PROCESS_ROUTE_ID")
    private Long processRouteId;

    /**
     * BOM版本
     */
    @TableField("BOM_VERSION")
    private String bomVersion;

    /**
     * 批次号
     */
    @TableField("BATCH_NO")
    private String batchNo;

    /**
     * 工单号
     */
    @TableField("WORK_ORDER_NO")
    private String workOrderNo;

    /**
     * 开工日期
     */
    @TableField("START_DATE")
    private LocalDateTime startDate;

    /**
     * 完工日期
     */
    @TableField("FINISH_DATE")
    private LocalDateTime finishDate;

    /**
     * 在制品成本
     */
    @TableField("WIP_COST")
    private BigDecimal wipCost;

    /**
     * 完工产品成本
     */
    @TableField("FINISHED_COST")
    private BigDecimal finishedCost;

    /**
     * 废品损失
     */
    @TableField("SCRAP_LOSS")
    private BigDecimal scrapLoss;

    /**
     * 返工成本
     */
    @TableField("REWORK_COST")
    private BigDecimal reworkCost;

    /**
     * 质量成本
     */
    @TableField("QUALITY_COST")
    private BigDecimal qualityCost;

    /**
     * 环保成本
     */
    @TableField("ENVIRONMENTAL_COST")
    private BigDecimal environmentalCost;

    /**
     * 安全成本
     */
    @TableField("SAFETY_COST")
    private BigDecimal safetyCost;

    /**
     * 研发分摊成本
     */
    @TableField("RD_ALLOCATED_COST")
    private BigDecimal rdAllocatedCost;

    /**
     * 管理费用分摊
     */
    @TableField("ADMIN_ALLOCATED_COST")
    private BigDecimal adminAllocatedCost;

    /**
     * 销售费用分摊
     */
    @TableField("SALES_ALLOCATED_COST")
    private BigDecimal salesAllocatedCost;

    /**
     * 财务费用分摊
     */
    @TableField("FINANCE_ALLOCATED_COST")
    private BigDecimal financeAllocatedCost;

    /**
     * 税费成本
     */
    @TableField("TAX_COST")
    private BigDecimal taxCost;

    /**
     * 运输成本
     */
    @TableField("TRANSPORT_COST")
    private BigDecimal transportCost;

    /**
     * 仓储成本
     */
    @TableField("STORAGE_COST")
    private BigDecimal storageCost;

    /**
     * 包装成本
     */
    @TableField("PACKAGING_COST")
    private BigDecimal packagingCost;

    /**
     * 能源成本
     */
    @TableField("ENERGY_COST")
    private BigDecimal energyCost;

    /**
     * 折旧成本
     */
    @TableField("DEPRECIATION_COST")
    private BigDecimal depreciationCost;

    /**
     * 维修成本
     */
    @TableField("MAINTENANCE_COST")
    private BigDecimal maintenanceCost;

    /**
     * 保险成本
     */
    @TableField("INSURANCE_COST")
    private BigDecimal insuranceCost;

    /**
     * 其他成本
     */
    @TableField("OTHER_COST")
    private BigDecimal otherCost;

    /**
     * 成本备注
     */
    @TableField("COST_REMARK")
    private String costRemark;

    /**
     * 审核状态(0待审核1已审核2已驳回)
     */
    @TableField("AUDIT_STATUS")
    private Integer auditStatus;

    /**
     * 审核人
     */
    @TableField("AUDITOR")
    private Long auditor;

    /**
     * 审核时间
     */
    @TableField("AUDIT_TIME")
    private LocalDateTime auditTime;

    /**
     * 审核意见
     */
    @TableField("AUDIT_OPINION")
    private String auditOpinion;

    /**
     * 凭证ID
     */
    @TableField("VOUCHER_ID")
    private Long voucherId;

    /**
     * 凭证号
     */
    @TableField("VOUCHER_NO")
    private String voucherNo;

    /**
     * 是否生成凭证
     */
    @TableField("IS_VOUCHER_GENERATED")
    private Integer isVoucherGenerated;

    /**
     * 成本计算公式
     */
    @TableField("COST_FORMULA")
    private String costFormula;

    /**
     * 分摊基础
     */
    @TableField("ALLOCATION_BASIS")
    private String allocationBasis;

    /**
     * 分摊系数
     */
    @TableField("ALLOCATION_COEFFICIENT")
    private BigDecimal allocationCoefficient;

    /**
     * 成本驱动因子
     */
    @TableField("COST_DRIVER")
    private String costDriver;

    /**
     * 作业成本
     */
    @TableField("ACTIVITY_COST")
    private BigDecimal activityCost;

    /**
     * 资源成本
     */
    @TableField("RESOURCE_COST")
    private BigDecimal resourceCost;

    /**
     * 成本对象
     */
    @TableField("COST_OBJECT")
    private String costObject;

    /**
     * 成本库
     */
    @TableField("COST_POOL")
    private String costPool;

    /**
     * 成本层次
     */
    @TableField("COST_LEVEL")
    private Integer costLevel;

    /**
     * 成本性态(1固定成本2变动成本3混合成本)
     */
    @TableField("COST_BEHAVIOR")
    private Integer costBehavior;

    /**
     * 可控性(1可控2不可控3部分可控)
     */
    @TableField("CONTROLLABILITY")
    private Integer controllability;

    /**
     * 相关性(1相关2不相关)
     */
    @TableField("RELEVANCE")
    private Integer relevance;

    /**
     * 可追溯性(1直接2间接)
     */
    @TableField("TRACEABILITY")
    private Integer traceability;

    /**
     * 成本归集方式
     */
    @TableField("COST_COLLECTION_METHOD")
    private String costCollectionMethod;

    /**
     * 成本分配方式
     */
    @TableField("COST_ALLOCATION_METHOD")
    private String costAllocationMethod;

    /**
     * 成本核算精度
     */
    @TableField("COSTING_PRECISION")
    private Integer costingPrecision;

    /**
     * 成本核算频率
     */
    @TableField("COSTING_FREQUENCY")
    private String costingFrequency;

    /**
     * 成本基准
     */
    @TableField("COST_BASELINE")
    private BigDecimal costBaseline;

    /**
     * 成本目标
     */
    @TableField("COST_TARGET")
    private BigDecimal costTarget;

    /**
     * 成本上限
     */
    @TableField("COST_CEILING")
    private BigDecimal costCeiling;

    /**
     * 成本下限
     */
    @TableField("COST_FLOOR")
    private BigDecimal costFloor;

    /**
     * 成本预警阈值
     */
    @TableField("COST_ALERT_THRESHOLD")
    private BigDecimal costAlertThreshold;

    /**
     * 成本控制状态
     */
    @TableField("COST_CONTROL_STATUS")
    private Integer costControlStatus;

    /**
     * 成本改进措施
     */
    @TableField("COST_IMPROVEMENT_MEASURES")
    private String costImprovementMeasures;

    /**
     * 成本节约金额
     */
    @TableField("COST_SAVING_AMOUNT")
    private BigDecimal costSavingAmount;

    /**
     * 成本效率指标
     */
    @TableField("COST_EFFICIENCY_INDICATOR")
    private BigDecimal costEfficiencyIndicator;

    /**
     * 成本质量指标
     */
    @TableField("COST_QUALITY_INDICATOR")
    private BigDecimal costQualityIndicator;

    /**
     * 成本风险等级
     */
    @TableField("COST_RISK_LEVEL")
    private Integer costRiskLevel;

    /**
     * 成本风险描述
     */
    @TableField("COST_RISK_DESCRIPTION")
    private String costRiskDescription;

    /**
     * 成本优化建议
     */
    @TableField("COST_OPTIMIZATION_SUGGESTION")
    private String costOptimizationSuggestion;

    /**
     * 成本分析报告
     */
    @TableField("COST_ANALYSIS_REPORT")
    private String costAnalysisReport;

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
}
