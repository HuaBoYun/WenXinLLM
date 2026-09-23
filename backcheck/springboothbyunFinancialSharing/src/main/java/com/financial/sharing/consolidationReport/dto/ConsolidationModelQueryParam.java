package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

/**
 * 合并模型查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class ConsolidationModelQueryParam {

    /**
     * 模型编码
     */
    private String modelCode;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 合并类型
     */
    private String consolidationType;

    /**
     * 母公司ID
     */
    private String parentOrgId;

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

