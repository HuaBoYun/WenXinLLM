package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 核销参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("核销参数")
public class WriteOffParam {

    @NotBlank(message = "付款ID不能为空")
    @ApiModelProperty(value = "付款ID", required = true)
    private String paymentId;

    @ApiModelProperty("核销明细列表")
    private List<WriteOffDetailParam> details;

    @ApiModelProperty("核销日期")
    private LocalDate writeOffDate;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("备注")
    private String remarks;

    /**
     * 核销明细参数
     */
    @Data
    @ApiModel("核销明细参数")
    public static class WriteOffDetailParam {

        @NotBlank(message = "应付单据ID不能为空")
        @ApiModelProperty(value = "应付单据ID", required = true)
        private String documentId;

        @NotNull(message = "核销金额不能为空")
        @ApiModelProperty(value = "核销金额", required = true)
        private BigDecimal writeOffAmount;
    }
}

