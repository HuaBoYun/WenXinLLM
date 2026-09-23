package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应付单据VO
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("应付单据VO")
public class PayableDocumentVO {

    @ApiModelProperty("单据ID")
    private String documentId;

    @ApiModelProperty("单据编号")
    private String documentNo;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("应付金额")
    private BigDecimal payableAmount;

    @ApiModelProperty("已付金额")
    private BigDecimal paidAmount;

    @ApiModelProperty("未付金额")
    private BigDecimal remainingAmount;

    @ApiModelProperty("单据状态(0:草稿,1:待审核,2:已审核,3:已拒绝)")
    private Integer documentStatus;

    @ApiModelProperty("单据状态名称")
    private String documentStatusName;

    @ApiModelProperty("到期日期")
    private LocalDate dueDate;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty("业务类型")
    private Integer businessType;

    @ApiModelProperty("业务类型名称")
    private String businessTypeName;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("审核人")
    private String auditUser;

    @ApiModelProperty("审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty("审核意见")
    private String auditComments;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建人姓名")
    private String createByName;

    @ApiModelProperty("是否逾期")
    private Boolean overdue;

    @ApiModelProperty("逾期天数")
    private Integer overdueDays;
}

