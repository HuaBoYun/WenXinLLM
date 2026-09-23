package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

/**
 * 股权信息查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class EquityInfoQueryParam {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 母公司ID
     */
    private String parentOrgId;

    /**
     * 母公司名称
     */
    private String parentOrgName;

    /**
     * 子公司ID
     */
    private String subsidiaryOrgId;

    /**
     * 子公司名称
     */
    private String subsidiaryOrgName;

    /**
     * 股权类型
     */
    private String equityType;

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

