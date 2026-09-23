package com.financial.sharing.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预付款VO
 * @author system
 * @since 2025-01-13
 */
@Data
@ApiModel("预付款VO")
public class PrepaymentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("预付款ID")
    private String prepaymentId;

    @ApiModelProperty("预付款单号")
    private String prepaymentNo;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("预付金额")
    private BigDecimal prepaymentAmount;

    @ApiModelProperty("已冲销金额")
    private BigDecimal offsetAmount;

    @ApiModelProperty("剩余金额")
    private BigDecimal remainingAmount;

    @ApiModelProperty("预付状态(0:未冲销,1:部分冲销,2:全部冲销)")
    private Integer prepaymentStatus;

    @ApiModelProperty("预付日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate prepaymentDate;

    @ApiModelProperty("付款方式(1:现金,2:银行转账,3:支票,4:承兑汇票)")
    private Integer paymentMethod;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String createBy;
}
