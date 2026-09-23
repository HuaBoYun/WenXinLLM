package com.huabo.fxgl.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 表信息VO
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(name="TableInfoVO", description="表信息")
public class TableInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "表名")
    private String tableName;

    @Schema(name = "表注释")
    private String tableComment;

    @Schema(name = "数据源ID")
    private String dataSourceId;

    @Schema(name = "数据源名称")
    private String dataSourceName;

    @Schema(name = "列数量")
    private Integer columnCount;

    @Schema(name = "主键列数量")
    private Integer primaryKeyCount;

    @Schema(name = "同步时间")
    private LocalDateTime syncTime;

    @Schema(name = "同步时间字符串")
    private String syncTimeStr;

    @Schema(name = "是否已同步")
    private Boolean isSynced;

    @Schema(name = "同步状态")
    private String syncStatus;

    @Schema(name = "同步状态名称")
    private String syncStatusName;

    @Schema(name = "是否可编辑")
    private Boolean canEdit = true;

    @Schema(name = "是否可删除")
    private Boolean canDelete = true;
}
