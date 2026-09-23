package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 票据背书参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("票据背书参数")
public class BillEndorseParam {

    @NotBlank(message = "票据ID不能为空")
    @ApiModelProperty(value = "票据ID", required = true)
    private String billId;

    @ApiModelProperty("背书日期")
    private LocalDate endorseDate;

    @NotNull(message = "背书金额不能为空")
    @ApiModelProperty(value = "背书金额", required = true)
    private BigDecimal endorseAmount;

    @NotBlank(message = "被背书人不能为空")
    @ApiModelProperty(value = "被背书人", required = true)
    private String endorseeName;

    @ApiModelProperty("被背书人开户行")
    private String endorseeBank;

    @ApiModelProperty("被背书人账号")
    private String endorseeAccount;

    @ApiModelProperty("背书类型(1:转让,2:质押)")
    private Integer endorseType = 1;

    @ApiModelProperty("背书说明")
    private String description;

    @ApiModelProperty("备注")
    private String remarks;
}

