package com.huabo.fxgl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 数据模型测试请求DTO
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(description="数据模型测试请求参数")
public class DataModelTestDTO {

    @Schema(name = "SQL内容", required = true)
    private String sqlContent;

    @Schema(name = "数据源ID", required = true)
    private String dataSourceId;

    @Schema(name = "参数列表")
    private Map<String, Object> parameters;
}
