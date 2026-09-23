package com.huabo.fxgl.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 风险数据库一览表 DTO
 *
 * @author AI Assistant
 * @since 2025-01-07
 */
@Data
public class RiskDatabaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 风险类型名称
     */
    private String riskcatname;

    /**
     * 指定措施数量
     */
    private Integer totalCount;

    /**
     * 完成措施数量
     */
    private Integer completedCount;

    /**
     * 未完成措施数量
     */
    private Integer uncompletedCount;
}

