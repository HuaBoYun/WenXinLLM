package com.huabo.fxgl.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 表统计信息VO
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@Schema(name="TableStatisticsVO", description="表统计信息")
public class TableStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "数据源ID")
    private String dataSourceId;

    @Schema(name = "数据源名称")
    private String dataSourceName;

    @Schema(name = "总表数量")
    private Integer totalTables;

    @Schema(name = "已同步表数量")
    private Integer syncedTables;

    @Schema(name = "未同步表数量")
    private Integer unsyncedTables;

    @Schema(name = "总列数量")
    private Integer totalColumns;

    @Schema(name = "最后同步时间")
    private String lastSyncTime;

    @Schema(name = "同步状态")
    private String syncStatus;

    @Schema(name = "同步状态名称")
    private String syncStatusName;
}
