package com.financial.sharing.budgetPlanning.dto;

import lombok.Data;

/**
 * 预算模型查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class BudgetModelQueryParam {

    /**
     * 模型编码
     */
    private String modelCode;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 预算年度
     */
    private String budgetYear;

    /**
     * 预算类型
     */
    private String budgetType;

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

