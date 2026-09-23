package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

/**
 * 抵消凭证查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class EliminationVoucherQueryParam {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 期间
     */
    private String period;

    /**
     * 凭证号
     */
    private String voucherNo;

    /**
     * 模板ID
     */
    private String templateId;

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

