package com.huabo.fxgl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

/**
 * 数据源保存参数DTO
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(name="DataSourceSaveDTO", description="数据源保存参数")
public class DataSourceSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "数据源ID(更新时必填)")
    private String sourceId;

    @Schema(name = "数据源名称", required = true)
    private String sourceName;

    @Schema(name = "数据源类型(DM/ORACLE/MYSQL)", required = true)
    private String sourceType;

    @Schema(name = "主机IP", required = true)
    private String hostIp;

    @Schema(name = "端口", required = true)
    private Integer port;

    @Schema(name = "数据库名", required = true)
    private String databaseName;

    @Schema(name = "用户名", required = true)
    private String username;

    @Schema(name = "密码", required = true)
    private String password;

    @Schema(name = "连接URL")
    private String connectionUrl;

    @Schema(name = "状态(ACTIVE/INACTIVE)")
    private String status = "ACTIVE";

    @Schema(name = "描述")
    private String description;
}
