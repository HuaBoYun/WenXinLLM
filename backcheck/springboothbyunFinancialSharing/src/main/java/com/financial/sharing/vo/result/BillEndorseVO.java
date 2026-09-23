package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 票据背书VO
 * @author system
 * @since 2025-01-13
 */
@Data
@ApiModel("票据背书VO")
public class BillEndorseVO {

    @ApiModelProperty("背书ID")
    private String endorseId;

    @ApiModelProperty("背书编号")
    private String endorseNo;

    @ApiModelProperty("票据ID")
    private String billId;

    @ApiModelProperty("票据号")
    private String billNo;

    @ApiModelProperty("背书人ID")
    private String endorserId;

    @ApiModelProperty("背书人名称")
    private String endorserName;

    @ApiModelProperty("被背书人ID")
    private String endorseeId;

    @ApiModelProperty("被背书人名称")
    private String endorseeName;

    @ApiModelProperty("背书金额")
    private BigDecimal endorseAmount;

    @ApiModelProperty("背书日期")
    private LocalDate endorseDate;

    @ApiModelProperty("背书类型")
    private String endorseType;

    @ApiModelProperty("背书类型名称")
    private String endorseTypeName;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("状态名称")
    private String statusName;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("审批人")
    private String approveBy;

    @ApiModelProperty("审批时间")
    private LocalDateTime approveTime;

    @ApiModelProperty("审批意见")
    private String approveComments;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String createBy;
}
