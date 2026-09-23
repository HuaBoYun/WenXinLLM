package com.financial.sharing.budgetPlanning.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 预算调整查询参数
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
public class BudgetAdjustmentQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调整单号
     */
    private String adjustmentNo;

    /**
     * 预算模型ID
     */
    private String modelId;

    /**
     * 预算期间
     */
    private String period;

    /**
     * 预算版本
     */
    private String version;

    /**
     * 调整类型(FULL-整版调整/PARTIAL-零星调整)
     */
    private String adjustmentType;

    /**
     * 状态(DRAFT-草稿/SUBMITTED-已提交/APPROVED-已审批/REJECTED-已驳回/EXECUTED-已执行)
     */
    private String status;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 租户ID
     */
    private String orgId;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;
}

