package com.financial.sharing.budgetPlanning.dto;

import lombok.Data;

/**
 * 预算表单配置查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class BudgetFormQueryParam {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 表单编码
     */
    private String formCode;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 表单类型
     */
    private String formType;

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

