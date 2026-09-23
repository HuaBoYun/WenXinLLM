package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备资源查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EquipmentResourceQueryParam extends BaseQueryParam {

    /**
     * 项目策划ID
     */
    private Long planningId;

    /**
     * 设备编号
     */
    private String equipmentNo;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备类型(1:施工设备,2:检测设备,3:办公设备,4:运输设备,5:其他)
     */
    private Integer equipmentType;

    /**
     * 设备规格
     */
    private String equipmentSpec;

    /**
     * 设备型号
     */
    private String equipmentModel;

    /**
     * 制造商
     */
    private String manufacturer;

    /**
     * 租赁方式(1:购买,2:租赁,3:自有,4:借用)
     */
    private Integer leaseType;

    /**
     * 分配状态(1:待分配,2:已分配,3:使用中,4:已归还,5:已损坏,6:已报废)
     */
    private Integer allocationStatus;

    /**
     * 设备状态(1:正常,2:维修中,3:故障,4:报废)
     */
    private Integer equipmentStatus;

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
     * 单价最小值
     */
    private BigDecimal unitPriceMin;

    /**
     * 单价最大值
     */
    private BigDecimal unitPriceMax;

    /**
     * 开始使用时间开始
     */
    private Date startTimeStart;

    /**
     * 开始使用时间结束
     */
    private Date startTimeEnd;

    /**
     * 结束使用时间开始
     */
    private Date endTimeStart;

    /**
     * 结束使用时间结束
     */
    private Date endTimeEnd;

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
     * 关键词搜索（设备名称、规格、型号、制造商）
     */
    private String keyword;

    /**
     * 是否超预算
     */
    private Boolean overBudget;

    /**
     * 分配完成率最小值
     */
    private BigDecimal allocationRateMin;

    /**
     * 分配完成率最大值
     */
    private BigDecimal allocationRateMax;

    /**
     * 租赁期限最小值（天）
     */
    private Integer leaseDurationMin;

    /**
     * 租赁期限最大值（天）
     */
    private Integer leaseDurationMax;

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


}
