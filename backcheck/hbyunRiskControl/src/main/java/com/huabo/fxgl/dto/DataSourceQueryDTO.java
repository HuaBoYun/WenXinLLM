package com.huabo.fxgl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据源查询参数DTO
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(name="DataSourceQueryDTO", description="数据源查询参数")
public class DataSourceQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(name = "每页大小", example = "20")
    private Integer pageSize = 20;

    @Schema(name = "数据源名称(模糊查询)")
    private String sourceName;

    @Schema(name = "数据源类型(DM/ORACLE/MYSQL)")
    private String sourceType;

    @Schema(name = "状态(ACTIVE/INACTIVE)")
    private String status;

    @Schema(name = "主机IP")
    private String hostIp;

    @Schema(name = "是否启用(Y/N)")
    private String isEnabled;

    @Schema(name = "创建人")
    private String createUser;

    @Schema(name = "开始时间")
    private String startTime;

    @Schema(name = "结束时间")
    private String endTime;

    @Schema(name = "排序字段")
    private String sortField;

    @Schema(name = "排序方向(ASC/DESC)")
    private String sortOrder;
}
