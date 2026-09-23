package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器实时资源指标
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "服务器实时资源指标")
public class ServerMetricsVO {

    @Schema(description = "CPU使用率(%)")
    private Double cpuUsage;

    @Schema(description = "CPU系统使用率(%)")
    private Double cpuSystemUsage;

    @Schema(description = "CPU用户使用率(%)")
    private Double cpuUserUsage;

    @Schema(description = "CPU等待IO(%)")
    private Double cpuIoWait;

    @Schema(description = "总内存(字节)")
    private Long totalMemory;

    @Schema(description = "已用内存(字节)")
    private Long usedMemory;

    @Schema(description = "可用内存(字节)")
    private Long availableMemory;

    @Schema(description = "内存使用率(%)")
    private Double memoryUsage;

    @Schema(description = "JVM总堆内存(字节)")
    private Long jvmHeapTotal;

    @Schema(description = "JVM已用堆内存(字节)")
    private Long jvmHeapUsed;

    @Schema(description = "JVM堆使用率(%)")
    private Double jvmHeapUsage;

    @Schema(description = "JVM非堆内存已使用(字节)")
    private Long jvmNonHeapUsed;

    @Schema(description = "线程总数")
    private Integer threadCount;

    @Schema(description = "守护线程数")
    private Integer daemonThreadCount;

    @Schema(description = "采集时间戳")
    private Long timestamp;
}
