package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

/**
 * 合并范围配置查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class ConsolidationScopeQueryParam {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 合并方法
     */
    private String consolidationMethod;

    /**
     * 是否启用
     */
    private String isActive;

    /**
     * 当前页
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;
}

