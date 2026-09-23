package com.financial.sharing.budgetPlanning.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 预算数据查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class BudgetDataQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 表单ID
     */
    private String formId;

    /**
     * 预算期间
     */
    private String period;

    /**
     * 预算版本
     */
    private String version;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 主体编码
     */
    private String organizationCode;

    /**
     * 状态
     */
    private String status;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}

