package com.huabo.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "LicenseRechargeDTO", description = "密钥充值请求参数")
public class LicenseRechargeDTO {

    @Schema(name = "密钥字符串")
    private String licenseKey;
}
