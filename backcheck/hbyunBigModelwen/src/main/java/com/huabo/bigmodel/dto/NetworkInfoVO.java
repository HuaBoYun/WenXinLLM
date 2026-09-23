package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网络接口信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "网络接口信息")
public class NetworkInfoVO {

    @Schema(description = "接口名称", example = "eth0")
    private String name;

    @Schema(description = "IPv4地址", example = "192.0.2.200")
    private String ipv4;

    @Schema(description = "IPv6地址")
    private String ipv6;

    @Schema(description = "MAC地址", example = "00:1B:63:84:45:E6")
    private String mac;

    @Schema(description = "MTU大小(字节)")
    private Long mtu;

    @Schema(description = "累计接收字节数")
    private Long bytesRecv;

    @Schema(description = "累计发送字节数")
    private Long bytesSent;

    @Schema(description = "累计接收包数")
    private Long packetsRecv;

    @Schema(description = "累计发送包数")
    private Long packetsSent;

    @Schema(description = "接收速度(可读)", example = "2.5 MB/s")
    private String speedRecv;

    @Schema(description = "发送速度(可读)", example = "1.8 MB/s")
    private String speedSent;

    @Schema(description = "接收错误数")
    private Long errorsRecv;

    @Schema(description = "发送错误数")
    private Long errorsSent;
}
