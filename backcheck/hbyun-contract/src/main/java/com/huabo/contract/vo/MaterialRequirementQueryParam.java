package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 材料需求查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialRequirementQueryParam extends BaseQueryParam {

    /**
     * 项目策划ID
     */
    private Long planningId;

    /**
     * 材料编号
     */
    private String materialNo;

    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 材料类型(1:原材料,2:辅助材料,3:工具,4:消耗品,5:其他)
     */
    private Integer materialType;

    /**
     * 材料规格
     */
    private String materialSpec;

    /**
     * 材料型号
     */
    private String materialModel;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 采购状态(1:待采购,2:采购中,3:已到货,4:已验收,5:已入库,6:已取消)
     */
    private Integer procurementStatus;

    /**
     * 优先级(1:低,2:中,3:高,4:紧急)
     */
    private Integer priority;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 负责人ID
     */
    private Long managerId;

    /**
     * 负责人姓名
     */
    private String managerName;

    /**
     * 使用地点
     */
    private String usageLocation;

    /**
     * 预算成本最小值
     */
    private BigDecimal budgetedCostMin;

    /**
     * 预算成本最大值
     */
    private BigDecimal budgetedCostMax;

    /**
     * 预算单价最小值
     */
    private BigDecimal budgetedUnitPriceMin;

    /**
     * 预算单价最大值
     */
    private BigDecimal budgetedUnitPriceMax;

    /**
     * 需求数量最小值
     */
    private BigDecimal requiredQuantityMin;

    /**
     * 需求数量最大值
     */
    private BigDecimal requiredQuantityMax;

    /**
     * 需求时间开始
     */
    private Date requiredTimeStart;

    /**
     * 需求时间结束
     */
    private Date requiredTimeEnd;

    /**
     * 采购时间开始
     */
    private Date purchaseTimeStart;

    /**
     * 采购时间结束
     */
    private Date purchaseTimeEnd;

    /**
     * 到货时间开始
     */
    private Date deliveryTimeStart;

    /**
     * 到货时间结束
     */
    private Date deliveryTimeEnd;

    /**
     * 创建时间开始
     */
    private Date createTimeStart;

    /**
     * 创建时间结束
     */
    private Date createTimeEnd;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 采购人员
     */
    private String purchaser;

    /**
     * 验收人员
     */
    private String inspector;

    /**
     * 关键词搜索（材料名称、规格、型号、品牌）
     */
    private String keyword;

    /**
     * 是否超预算
     */
    private Boolean overBudget;

    /**
     * 是否需求紧急
     */
    private Boolean urgent;

    /**
     * 采购完成率最小值
     */
    private BigDecimal procurementRateMin;

    /**
     * 采购完成率最大值
     */
    private BigDecimal procurementRateMax;

    /**
     * 材料编码（兼容Mapper XML）
     */
    private String materialCode;

    /**
     * 预算成本最小值（兼容Mapper XML）
     */
    private BigDecimal minBudgetedCost;

    /**
     * 预算成本最大值（兼容Mapper XML）
     */
    private BigDecimal maxBudgetedCost;

    /**
     * 实际成本最小值
     */
    private BigDecimal minActualCost;

    /**
     * 实际成本最大值
     */
    private BigDecimal maxActualCost;

    /**
     * 需求开始日期
     */
    private Date requiredStartDate;

    /**
     * 需求结束日期
     */
    private Date requiredEndDate;

    /**
     * 采购开始日期
     */
    private Date purchaseStartDate;

    /**
     * 采购结束日期
     */
    private Date purchaseEndDate;

    /**
     * 交付开始日期
     */
    private Date deliveryStartDate;

    /**
     * 交付结束日期
     */
    private Date deliveryEndDate;

    /**
     * 创建开始时间
     */
    private Date createStartTime;

    /**
     * 创建结束时间
     */
    private Date createEndTime;

    /**
     * 材料类别
     */
    private String materialCategory;

    /**
     * 材料规格
     */
    private String materialSpecification;

    /**
     * 需求状态
     */
    private Integer requirementStatus;

}
