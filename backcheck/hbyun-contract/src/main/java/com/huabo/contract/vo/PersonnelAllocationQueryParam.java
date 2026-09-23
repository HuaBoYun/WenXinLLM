package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 人员配置查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PersonnelAllocationQueryParam extends BaseQueryParam {

    /**
     * 项目策划ID
     */
    private Long planningId;

    /**
     * 配置编号
     */
    private String allocationNo;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 岗位类型(1:项目经理,2:技术负责人,3:质量负责人,4:安全负责人,5:普通员工,6:其他)
     */
    private Integer positionType;

    /**
     * 配置状态(1:待分配,2:已分配,3:执行中,4:已完成,5:已取消)
     */
    private Integer allocationStatus;

    /**
     * 优先级(1:低,2:中,3:高,4:紧急)
     */
    private Integer priority;

    /**
     * 负责人ID
     */
    private Long managerId;

    /**
     * 负责人姓名
     */
    private String managerName;

    /**
     * 需求人数最小值
     */
    private Integer requiredCountMin;

    /**
     * 需求人数最大值
     */
    private Integer requiredCountMax;

    /**
     * 预算成本最小值
     */
    private BigDecimal budgetedCostMin;

    /**
     * 预算成本最大值
     */
    private BigDecimal budgetedCostMax;

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
     * 开始时间开始
     */
    private Date startTimeStart;

    /**
     * 开始时间结束
     */
    private Date startTimeEnd;

    /**
     * 结束时间开始
     */
    private Date endTimeStart;

    /**
     * 结束时间结束
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
     * 工作地点
     */
    private String workLocation;

    /**
     * 技能要求关键词
     */
    private String skillKeyword;

    /**
     * 关键词搜索（岗位名称、技能要求、工作内容）
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
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 创建开始时间
     */
    private Date createStartTime;

    /**
     * 创建结束时间
     */
    private Date createEndTime;
}
