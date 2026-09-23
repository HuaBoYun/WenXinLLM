package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

/**
 * 报表生成参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class ReportGenerateParam {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 期间
     */
    private String period;

    /**
     * 报表类型：BALANCE_SHEET(资产负债表)/INCOME_STATEMENT(利润表)/
     * CASH_FLOW(现金流量表)/ALL(全部)
     */
    private String reportType;

    /**
     * 是否重新生成
     */
    private Boolean regenerate;
}

