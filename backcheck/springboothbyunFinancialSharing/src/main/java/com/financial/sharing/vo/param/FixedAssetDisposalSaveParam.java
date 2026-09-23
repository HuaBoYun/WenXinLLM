package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 固定资产处置保存参数
 * @author system
 * @since 2026-01-23
 */
@Data
@ApiModel(value = "FixedAssetDisposalSaveParam", description = "固定资产处置保存参数")
public class FixedAssetDisposalSaveParam {

    @ApiModelProperty(value = "处置ID（更新时必填）")
    private String disposalId;

    @ApiModelProperty(value = "处置单号（新增时自动生成）")
    private String disposalNo;

    @ApiModelProperty(value = "处置类型(SCRAP-报废,SALE-出售,TRANSFER-转让,DONATION-捐赠,DAMAGE-毁损)", required = true)
    @NotBlank(message = "处置类型不能为空")
    private String disposalType;

    @ApiModelProperty(value = "资产ID", required = true)
    @NotBlank(message = "资产ID不能为空")
    private String assetId;

    @ApiModelProperty(value = "处置收入")
    private BigDecimal disposalIncome;

    @ApiModelProperty(value = "处置日期", required = true)
    @NotNull(message = "处置日期不能为空")
    private LocalDate disposalDate;

    @ApiModelProperty(value = "处置原因")
    private String reason;

    @ApiModelProperty(value = "接收方（转让或捐赠时填写）")
    private String receiver;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "操作人ID")
    private String operatorId;
}

