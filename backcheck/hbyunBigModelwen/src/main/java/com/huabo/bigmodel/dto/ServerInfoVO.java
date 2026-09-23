package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器基础信息（静态信息）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "服务器基础信息")
public class ServerInfoVO {

    @Schema(description = "操作系统名称")
    private String osName;

    @Schema(description = "操作系统版本")
    private String osVersion;

    @Schema(description = "操作系统架构")
    private String osArch;

    @Schema(description = "主机名")
    private String hostName;

    @Schema(description = "CPU型号")
    private String cpuModel;

    @Schema(description = "CPU核心数(物理)")
    private Integer cpuPhysicalCores;

    @Schema(description = "CPU逻辑核数")
    private Integer cpuLogicalCores;

    @Schema(description = "总内存(字节)")
    private Long totalMemory;

    @Schema(description = "总内存(可读)")
    private String totalMemoryStr;

    @Schema(description = "JVM名称")
    private String jvmName;

    @Schema(description = "JVM版本")
    private String jvmVersion;

    @Schema(description = "JVM供应商")
    private String jvmVendor;

    @Schema(description = "JVM最大堆内存(字节)")
    private Long jvmMaxMemory;

    @Schema(description = "JVM最大堆内存(可读)")
    private String jvmMaxMemoryStr;

    @Schema(description = "服务器启动时间")
    private String bootTime;

    @Schema(description = "运行时长(可读)")
    private String uptime;
}
