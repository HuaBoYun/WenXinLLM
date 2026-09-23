package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

/**
 * 对账数据查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class ReconciliationDataQueryParam {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 期间
     */
    private String period;

    /**
     * 甲方公司ID
     */
    private String companyAId;

    /**
     * 甲方公司名称
     */
    private String companyAName;

    /**
     * 乙方公司ID
     */
    private String companyBId;

    /**
     * 乙方公司名称
     */
    private String companyBName;

    /**
     * 交易类型
     */
    private String transactionType;

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

