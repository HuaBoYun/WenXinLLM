package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

/**
 * 对账差异分析查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class ReconciliationAnalysisQueryParam {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 期间
     */
    private String period;

    /**
     * 差异原因
     */
    private String diffReason;

    /**
     * 状态
     */
    private String status;

    /**
     * 处理人
     */
    private String handler;

    /**
     * 甲方公司名称
     */
    private String companyAName;

    /**
     * 乙方公司名称
     */
    private String companyBName;

    /**
     * 当前页
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;
}

