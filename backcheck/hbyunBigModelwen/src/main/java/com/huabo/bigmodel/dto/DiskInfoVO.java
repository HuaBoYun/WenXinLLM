package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 磁盘分区信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "磁盘分区信息")
public class DiskInfoVO {

    @Schema(description = "挂载点/盘符")
    private String mountPoint;

    @Schema(description = "文件系统类型")
    private String fsType;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "总容量(字节)")
    private Long totalSpace;

    @Schema(description = "总容量(可读)")
    private String totalSpaceStr;

    @Schema(description = "可用空间(字节)")
    private Long freeSpace;

    @Schema(description = "可用空间(可读)")
    private String freeSpaceStr;

    @Schema(description = "已用空间(字节)")
    private Long usedSpace;

    @Schema(description = "已用空间(可读)")
    private String usedSpaceStr;

    @Schema(description = "使用率(%)")
    private Double usagePercent;
}
