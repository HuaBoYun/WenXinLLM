package com.financial.sharing.budgetPlanning.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 预算数据审批查询参数
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
public class BudgetApprovalQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 审批人ID
     */
    private String approverId;

    /**
     * 审批状态(PENDING-待审批/APPROVED-已通过/REJECTED-已驳回/CANCELLED-已撤销)
     */
    private String approvalStatus;

    /**
     * 预算期间
     */
    private String period;

    /**
     * 预算版本
     */
    private String version;

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 模型名称（模糊查询）
     */
    private String modelName;

    /**
     * 提交人ID
     */
    private String submitUserId;

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

