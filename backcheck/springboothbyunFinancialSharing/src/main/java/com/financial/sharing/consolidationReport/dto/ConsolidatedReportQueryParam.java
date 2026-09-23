package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

/**
 * 合并报表查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class ConsolidatedReportQueryParam {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 期间
     */
    private String period;

    /**
     * 报表类型
     */
    private String reportType;

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

