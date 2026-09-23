package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 坏账准备保存参数
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel("坏账准备保存参数")
public class ArBadDebtProvisionSaveParam {

    @ApiModelProperty("坏账准备ID（更新时必填）")
    private String provisionId;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("应收单ID")
    private String receivableId;

    @ApiModelProperty("客户ID")
    private String customerId;

    @ApiModelProperty("计提日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate provisionDate;

    @ApiModelProperty("计提金额")
    private BigDecimal provisionAmount;

    @ApiModelProperty("计提比例")
    private BigDecimal provisionRate;

    @ApiModelProperty("计提原因")
    private String provisionReason;

    @ApiModelProperty("计提方式：1-按比例计提, 2-按金额计提, 3-全额计提")
    private Integer provisionMethod;

    @ApiModelProperty("账龄区间")
    private String agingRange;

    @ApiModelProperty("风险等级：1-低风险, 2-中风险, 3-高风险")
    private Integer riskLevel;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人ID")
    private String creatorId;

    @ApiModelProperty("操作人ID")
    private String operatorId;

    @ApiModelProperty("应收金额")
    private BigDecimal receivableAmount;
}

