package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * SQL执行响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "SQL执行响应")
public class SqlExecuteResponse {

    /**
     * 是否成功
     */
    @Schema(description = "是否成功")
    private Boolean success;

    /**
     * 消息
     */
    @Schema(description = "消息")
    private String message;

    /**
     * SQL类型
     */
    @Schema(description = "SQL类型")
    private String sqlType;

    /**
     * 影响行数(DDL/DML)
     */
    @Schema(description = "影响行数")
    private Integer affectedRows;

    /**
     * 查询结果(QUERY)
     */
    @Schema(description = "查询结果")
    private List<Map<String, Object>> data;

    /**
     * 列信息
     */
    @Schema(description = "列信息")
    private List<ColumnInfo> columns;

    /**
     * 执行时间(毫秒)
     */
    @Schema(description = "执行时间(毫秒)")
    private Long executionTime;

    /**
     * 列信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColumnInfo {
        /**
         * 列名
         */
        private String name;

        /**
         * 列类型
         */
        private String type;

        /**
         * 列标签
         */
        private String label;
    }

    /**
     * 成功响应
     */
    public static SqlExecuteResponse success(String message) {
        return SqlExecuteResponse.builder()
                .success(true)
                .message(message)
                .build();
    }

    /**
     * 失败响应
     */
    public static SqlExecuteResponse error(String message) {
        return SqlExecuteResponse.builder()
                .success(false)
                .message(message)
                .build();
    }
}

