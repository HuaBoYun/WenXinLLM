package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 核销记录保存参数
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel(value = "ArWriteOffSaveParam", description = "核销记录保存参数")
public class ArWriteOffSaveParam {

    @ApiModelProperty(value = "收款单ID", required = true)
    @NotBlank(message = "收款单ID不能为空")
    private String receiptId;

    @ApiModelProperty(value = "核销明细列表", required = true)
    @NotNull(message = "核销明细不能为空")
    private List<WriteOffDetailParam> details;

    @ApiModelProperty(value = "核销日期")
    private LocalDate writeOffDate;

    @ApiModelProperty(value = "备注")
    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    private String remarks;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "操作人ID")
    private String operatorId;

    @ApiModelProperty(value = "应收单ID（单笔核销时使用）")
    private String receivableId;

    @ApiModelProperty(value = "核销金额（单笔核销时使用）")
    private BigDecimal writeOffAmount;

    @ApiModelProperty(value = "核销类型(1手工核销 2自动核销)")
    private Integer writeOffType;

    /**
     * 核销明细参数
     */
    @Data
    @ApiModel(value = "WriteOffDetailParam", description = "核销明细参数")
    public static class WriteOffDetailParam {

        @ApiModelProperty(value = "应收单ID", required = true)
        @NotBlank(message = "应收单ID不能为空")
        private String receivableId;

        @ApiModelProperty(value = "核销金额", required = true)
        @NotNull(message = "核销金额不能为空")
        private BigDecimal writeOffAmount;
    }
}

