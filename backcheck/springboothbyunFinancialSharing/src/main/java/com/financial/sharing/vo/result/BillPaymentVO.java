package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 票据兑付VO
 * @author system
 * @since 2025-01-13
 */
@Data
@ApiModel("票据兑付VO")
public class BillPaymentVO {

    @ApiModelProperty("兑付ID")
    private String paymentId;

    @ApiModelProperty("兑付编号")
    private String paymentNo;

    @ApiModelProperty("票据ID")
    private String billId;

    @ApiModelProperty("票据号")
    private String billNo;

    @ApiModelProperty("兑付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("实际到账金额")
    private BigDecimal actualAmount;

    @ApiModelProperty("兑付日期")
    private LocalDate paymentDate;

    @ApiModelProperty("兑付银行")
    private String bankName;

    @ApiModelProperty("银行账号")
    private String bankAccount;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("状态名称")
    private String statusName;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("确认人")
    private String confirmBy;

    @ApiModelProperty("确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String createBy;
}
