package com.financial.sharing.budgetPlanning.dto;

import lombok.Data;

/**
 * 业务规则查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class BudgetRuleQueryParam {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 规则编码
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型
     */
    private String ruleType;

    /**
     * 状态
     */
    private String status;

    /**
     * 当前页
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;
}

