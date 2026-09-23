package com.financial.sharing.consolidationReport.dto;

import lombok.Data;

/**
 * 抵消凭证模板查询参数DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class EliminationTemplateQueryParam {

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 抵消类型
     */
    private String eliminationType;

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

