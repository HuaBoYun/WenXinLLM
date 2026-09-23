package com.financial.sharing.dataCollection.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 映射规则查询参数DTO
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
public class MappingRuleQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则编码
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 数据源ID
     */
    private String sourceId;

    /**
     * 目标表
     */
    private String targetTable;

    /**
     * 是否启用：Y/N
     */
    private String isEnabled;

    /**
     * 是否增量：Y/N
     */
    private String isIncremental;

    /**
     * 冲突策略
     */
    private String conflictStrategy;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 每页记录数
     */
    private Integer pageSize;
}

