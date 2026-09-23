package com.financial.sharing.dataCollection.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据质量规则查询参数DTO
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
public class DataQualityRuleQueryParam implements Serializable {

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
     * 规则类型
     */
    private String ruleType;

    /**
     * 检查级别
     */
    private String checkLevel;

    /**
     * 是否启用
     */
    private String isEnabled;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 每页大小
     */
    private Integer pageSize;
}

