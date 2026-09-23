package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应收单据实体类
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AR_RECEIVABLE")
@ApiModel(value = "ArReceivableEntity对象", description = "应收单据表")
public class ArReceivableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "应收单据ID")
    @TableId(value = "RECEIVABLE_ID", type = IdType.ASSIGN_UUID)
    private String receivableId;

    @ApiModelProperty(value = "单据编号")
    @TableField("DOCUMENT_NO")
    private String documentNo;

    @ApiModelProperty(value = "客户ID")
    @TableField("CUSTOMER_ID")
    private String customerId;

    @ApiModelProperty(value = "业务类型(1销售收入 2服务收入 3租赁收入 4其他收入)")
    @TableField("BUSINESS_TYPE")
    private Integer businessType;

    @ApiModelProperty(value = "应收金额")
    @TableField("RECEIVABLE_AMOUNT")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "已收金额")
    @TableField("RECEIVED_AMOUNT")
    private BigDecimal receivedAmount;

    @ApiModelProperty(value = "剩余金额")
    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    @ApiModelProperty(value = "单据状态(0草稿 1待审核 2已审核 3已拒绝)")
    @TableField("DOCUMENT_STATUS")
    private Integer documentStatus;

    @ApiModelProperty(value = "到期日期")
    @TableField("DUE_DATE")
    private LocalDate dueDate;

    @ApiModelProperty(value = "币种")
    @TableField("CURRENCY")
    private String currency;

    @ApiModelProperty(value = "汇率")
    @TableField("EXCHANGE_RATE")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "备注")
    @TableField("REMARKS")
    private String remarks;

    @ApiModelProperty(value = "审核时间")
    @TableField("AUDIT_TIME")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "审核人ID")
    @TableField("AUDITOR_ID")
    private String auditorId;

    @ApiModelProperty(value = "审核意见")
    @TableField("AUDIT_COMMENTS")
    private String auditComments;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    @TableField("ORG_ID")
    private String orgId;

    @ApiModelProperty(value = "账簿ID")
    @TableField("BOOK_ID")
    private Long bookId;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "删除标识(0否 1是)")
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    // ========== 扩展字段（不映射到数据库）==========

    @ApiModelProperty(value = "客户名称")
    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty(value = "业务类型名称")
    @TableField(exist = false)
    private String businessTypeName;

    @ApiModelProperty(value = "单据状态名称")
    @TableField(exist = false)
    private String documentStatusName;

    @ApiModelProperty(value = "创建人姓名")
    @TableField(exist = false)
    private String creatorName;

    @ApiModelProperty(value = "审核人姓名")
    @TableField(exist = false)
    private String auditorName;

    @ApiModelProperty(value = "逾期天数")
    @TableField(exist = false)
    private Integer overdueDays;
}

