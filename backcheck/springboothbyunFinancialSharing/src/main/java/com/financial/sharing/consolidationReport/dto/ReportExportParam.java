package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

/**
 * 报表导出参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class ReportExportParam {

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
     * 导出格式：EXCEL/PDF
     */
    private String exportFormat;
}

