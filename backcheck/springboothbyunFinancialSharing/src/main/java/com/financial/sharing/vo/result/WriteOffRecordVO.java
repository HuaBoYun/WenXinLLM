package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 核销记录VO
 * @author system
 * @since 2025-01-13
 */
@Data
@ApiModel("核销记录VO")
public class WriteOffRecordVO {

    @ApiModelProperty("核销ID")
    private String writeOffId;

    @ApiModelProperty("核销单号")
    private String writeOffNo;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("付款单号")
    private String paymentNo;

    @ApiModelProperty("应付单号")
    private String payableNo;

    @ApiModelProperty("核销金额")
    private BigDecimal writeOffAmount;

    @ApiModelProperty("核销状态(0:待核销,1:已核销,2:已撤销)")
    private Integer writeOffStatus;

    @ApiModelProperty("核销日期")
    private LocalDate writeOffDate;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String createBy;
}
