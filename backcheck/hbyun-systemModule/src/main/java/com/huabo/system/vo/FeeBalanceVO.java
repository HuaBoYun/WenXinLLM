package com.huabo.system.vo;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "FeeBalanceVO", description = "余额信息")
public class FeeBalanceVO {

    @Schema(name = "公司ID")
    private BigDecimal companyOrgId;

    @Schema(name = "公司名称")
    private String companyName;

    @Schema(name = "当前余额（解密后）")
    private BigDecimal balance;

    @Schema(name = "累计消费")
    private BigDecimal totalConsumed;

    @Schema(name = "累计充值")
    private BigDecimal totalRecharged;

    @Schema(name = "初始赠送额度")
    private BigDecimal initAmount;

    @Schema(name = "最后充值时间")
    private Date lastRechargeTime;
}
