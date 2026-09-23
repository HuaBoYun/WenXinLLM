package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库连接池状态
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "数据库连接池状态")
public class DatabasePoolVO {

    @Schema(description = "数据库连接URL")
    private String url;

    @Schema(description = "驱动类名")
    private String driverClass;

    @Schema(description = "当前活跃连接数")
    private Integer activeCount;

    @Schema(description = "最大活跃连接数")
    private Integer maxActive;

    @Schema(description = "初始连接数")
    private Integer initialSize;

    @Schema(description = "池中连接数")
    private Integer poolingCount;

    @Schema(description = "等待获取连接的线程数")
    private Integer waitThreadCount;

    @Schema(description = "累计等待次数")
    private Long notEmptyWaitCount;

    @Schema(description = "累计建立连接次数")
    private Long connectCount;

    @Schema(description = "累计关闭连接次数")
    private Long closeCount;

    @Schema(description = "累计错误次数")
    private Long errorCount;

    @Schema(description = "连接使用百分比", example = "35.5")
    private Double connectPercent;
}
