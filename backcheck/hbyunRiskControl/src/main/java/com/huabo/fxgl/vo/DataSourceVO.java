package com.huabo.fxgl.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据源返回VO
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(name="DataSourceVO", description="数据源返回对象")
public class DataSourceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "数据源ID")
    private String sourceId;

    @Schema(name = "数据源名称")
    private String sourceName;

    @Schema(name = "数据源类型")
    private String sourceType;

    @Schema(name = "数据源类型名称")
    private String sourceTypeName;

    @Schema(name = "主机IP")
    private String hostIp;

    @Schema(name = "端口")
    private Integer port;

    @Schema(name = "数据库名")
    private String databaseName;

    @Schema(name = "用户名")
    private String username;

    @Schema(name = "连接URL")
    private String connectionUrl;

    @Schema(name = "状态")
    private String status;

    @Schema(name = "状态名称")
    private String statusName;

    @Schema(name = "描述")
    private String description;

    @Schema(name = "最后同步时间")
    private LocalDateTime lastSyncTime;

    @Schema(name = "表数量")
    private Integer tableCount;

    @Schema(name = "连接测试结果")
    private String connectionTestResult;

    @Schema(name = "连接测试结果名称")
    private String connectionTestResultName;

    @Schema(name = "创建人")
    private String createUser;

    @Schema(name = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    private String updateUser;

    @Schema(name = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "是否可编辑")
    private Boolean canEdit;

    @Schema(name = "是否可删除")
    private Boolean canDelete;
}
