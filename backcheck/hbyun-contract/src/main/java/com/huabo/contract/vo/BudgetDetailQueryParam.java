package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算明细查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetDetailQueryParam extends BaseQueryParam {

    /**
     * 项目预算ID
     */
    private Long budgetId;

    /**
     * 明细编号
     */
    private String detailNo;

    /**
     * 明细名称
     */
    private String detailName;

    /**
     * 成本类别(1:人工成本,2:材料成本,3:设备成本,4:管理费用,5:其他费用)
     */
    private Integer costCategory;

    /**
     * 成本子类别
     */
    private String costSubcategory;

    /**
     * 明细状态(1:计划中,2:执行中,3:已完成,4:已取消,5:已调整)
     */
    private Integer detailStatus;

    /**
     * 审批状态(1:待审批,2:已审批,3:已驳回)
     */
    private Integer approvalStatus;

    /**
     * 是否关键项(0:否,1:是)
     */
    private Integer isCritical;

    /**
     * 风险等级(1:低,2:中,3:高,4:极高)
     */
    private Integer riskLevel;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 负责人ID
     */
    private Long managerId;

    /**
     * 负责人姓名
     */
    private String managerName;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 预算金额最小值
     */
    private BigDecimal budgetedAmountMin;

    /**
     * 预算金额最大值
     */
    private BigDecimal budgetedAmountMax;

    /**
     * 实际金额最小值
     */
    private BigDecimal actualAmountMin;

    /**
     * 实际金额最大值
     */
    private BigDecimal actualAmountMax;

    /**
     * 差异金额最小值
     */
    private BigDecimal varianceAmountMin;

    /**
     * 差异金额最大值
     */
    private BigDecimal varianceAmountMax;

    /**
     * 差异率最小值（%）
     */
    private BigDecimal varianceRateMin;

    /**
     * 差异率最大值（%）
     */
    private BigDecimal varianceRateMax;

    /**
     * 预算期间开始时间开始
     */
    private Date budgetPeriodStartBegin;

    /**
     * 预算期间开始时间结束
     */
    private Date budgetPeriodStartEnd;

    /**
     * 预算期间结束时间开始
     */
    private Date budgetPeriodEndBegin;

    /**
     * 预算期间结束时间结束
     */
    private Date budgetPeriodEndEnd;

    /**
     * 实际发生时间开始
     */
    private Date actualOccurrenceTimeStart;

    /**
     * 实际发生时间结束
     */
    private Date actualOccurrenceTimeEnd;

    /**
     * 审批时间开始
     */
    private Date approvalTimeStart;

    /**
     * 审批时间结束
     */
    private Date approvalTimeEnd;

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
     * 关键词搜索（明细名称、成本子类别、调整原因）
     */
    private String keyword;

    /**
     * 是否超预算
     */
    private Boolean overBudget;

    /**
     * 是否关键项
     */
    private Boolean criticalItem;

    /**
     * 是否高风险
     */
    private Boolean highRisk;
}
