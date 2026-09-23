package com.huabo.system.dto;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "LicenseGenerateDTO", description = "密钥生成请求参数")
public class LicenseGenerateDTO {

    @Schema(name = "授权公司ID")
    private BigDecimal companyOrgId;

    @Schema(name = "充值额度")
    private BigDecimal rechargeAmount;

    @Schema(name = "密钥有效期")
    private Date expireTime;
}
