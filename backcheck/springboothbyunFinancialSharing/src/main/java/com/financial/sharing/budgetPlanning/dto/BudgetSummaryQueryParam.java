package com.financial.sharing.budgetPlanning.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 预算数据汇总查询参数
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
public class BudgetSummaryQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 汇总类型(SUBJECT-科目/ORGANIZATION-组织/PERIOD-期间/CUSTOM-自定义)
     */
    private String summaryType;

    /**
     * 维度编码
     */
    private String dimensionCode;

    /**
     * 状态(PROCESSING-处理中/COMPLETED-已完成/FAILED-失败)
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

