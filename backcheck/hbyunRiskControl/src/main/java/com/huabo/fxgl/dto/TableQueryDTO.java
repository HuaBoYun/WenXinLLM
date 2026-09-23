package com.huabo.fxgl.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 表查询参数DTO
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(name="TableQueryDTO", description="表查询参数")
public class TableQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(name = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(name = "数据源ID")
    private String dataSourceId;

    @Schema(name = "表名(模糊查询)")
    private String tableName;

    @Schema(name = "表注释(模糊查询)")
    private String tableComment;

    @Schema(name = "同步状态(Y/N)")
    private String syncStatus;
}
