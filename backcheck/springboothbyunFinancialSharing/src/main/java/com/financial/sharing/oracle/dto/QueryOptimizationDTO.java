package com.financial.sharing.oracle.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 查询优化DTO
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class QueryOptimizationDTO {

    /**
     * 优化类型(ADD_INDEX/UPDATE_STATS/REBUILD_INDEX)
     */
    @NotBlank(message = "优化类型不能为空")
    private String type;

    /**
     * SQL ID
     */
    private String sqlId;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 索引名
     */
    private String indexName;

    /**
     * 估算百分比
     */
    @NotNull(message = "估算百分比不能为空")
    private Integer estimatePercent;

    /**
     * 优化原因
     */
    private String reason;

    /**
     * 预期收益
     */
    private Double expectedBenefit;
}