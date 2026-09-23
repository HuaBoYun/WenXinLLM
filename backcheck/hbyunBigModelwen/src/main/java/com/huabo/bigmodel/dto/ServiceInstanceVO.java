package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微服务实例信息VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "微服务实例信息")
public class ServiceInstanceVO {

    @Schema(description = "服务ID")
    private String serviceId;

    @Schema(description = "服务名称")
    private String serviceName;

    @Schema(description = "实例ID")
    private String instanceId;

    @Schema(description = "IP地址")
    private String host;

    @Schema(description = "端口号")
    private Integer port;

    @Schema(description = "状态: UP/DOWN/UNKNOWN")
    private String status;

    @Schema(description = "服务地址URL")
    private String uri;

    @Schema(description = "是否安全连接")
    private Boolean secure;

    @Schema(description = "元数据(JSON字符串)")
    private String metadata;
}
