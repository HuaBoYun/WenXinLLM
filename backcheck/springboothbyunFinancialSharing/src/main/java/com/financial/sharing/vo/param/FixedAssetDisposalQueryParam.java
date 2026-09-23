package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 固定资产处置查询参数
 * @author system
 * @since 2026-01-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FixedAssetDisposalQueryParam", description = "固定资产处置查询参数")
public class FixedAssetDisposalQueryParam extends PageableParam {

    @ApiModelProperty(value = "处置单号")
    private String disposalNo;

    @ApiModelProperty(value = "资产ID")
    private String assetId;

    @ApiModelProperty(value = "资产编码")
    private String assetCode;

    @ApiModelProperty(value = "资产名称")
    private String assetName;

    @ApiModelProperty(value = "处置类型(SCRAP-报废,SALE-出售,TRANSFER-转让,DONATION-捐赠,DAMAGE-毁损)")
    private String disposalType;

    @ApiModelProperty(value = "状态(PENDING-待审批,APPROVED-已审批,REJECTED-已拒绝,COMPLETED-已完成)")
    private String status;

    @ApiModelProperty(value = "处置日期开始")
    private LocalDate disposalDateStart;

    @ApiModelProperty(value = "处置日期结束")
    private LocalDate disposalDateEnd;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
}

