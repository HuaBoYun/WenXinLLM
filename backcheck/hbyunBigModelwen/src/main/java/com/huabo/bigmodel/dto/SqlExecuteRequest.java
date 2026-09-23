package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * SQL执行请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "SQL执行请求")
public class SqlExecuteRequest {

    /**
     * SQL语句
     */
    @NotBlank(message = "SQL语句不能为空")
    @Schema(description = "SQL语句", example = "SELECT * FROM users WHERE id = 1")
    private String sql;

    /**
     * 数据库类型: dm(达梦), mysql
     */
    @Schema(description = "数据库类型", example = "dm", allowableValues = {"dm", "mysql"})
    @Builder.Default
    private String dbType = "dm";

    /**
     * SQL类型: DDL(建表), DML(增删改), QUERY(查询)
     */
    @Schema(description = "SQL类型", example = "QUERY", allowableValues = {"DDL", "DML", "QUERY"})
    private String sqlType;

    /**
     * 是否自动检测SQL类型
     */
    @Schema(description = "是否自动检测SQL类型", example = "true")
    @Builder.Default
    private Boolean autoDetect = true;
}

