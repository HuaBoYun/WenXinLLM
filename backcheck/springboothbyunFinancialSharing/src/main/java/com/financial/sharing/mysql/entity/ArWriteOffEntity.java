package com.financial.sharing.mysql.entity;

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
 * 核销记录实体类
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AR_WRITE_OFF")
@ApiModel(value = "ArWriteOffEntity对象", description = "核销记录表")
public class ArWriteOffEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "核销ID")
    @TableId(value = "WRITE_OFF_ID", type = IdType.ASSIGN_UUID)
    private String writeOffId;

    @ApiModelProperty(value = "收款单ID")
    @TableField("RECEIPT_ID")
    private String receiptId;

    @ApiModelProperty(value = "应收单ID")
    @TableField("RECEIVABLE_ID")
    private String receivableId;

    @ApiModelProperty(value = "核销金额")
    @TableField("WRITE_OFF_AMOUNT")
    private BigDecimal writeOffAmount;

    @ApiModelProperty(value = "核销日期")
    @TableField("WRITE_OFF_DATE")
    private LocalDate writeOffDate;

    @ApiModelProperty(value = "核销方式(1全额核销 2部分核销)")
    @TableField("WRITE_OFF_TYPE")
    private Integer writeOffType;

    @ApiModelProperty(value = "备注")
    @TableField("REMARKS")
    private String remarks;

    @ApiModelProperty(value = "是否锁定(0否 1是)")
    @TableField("IS_LOCKED")
    private Integer isLocked;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    @TableField("ORG_ID")
    private String orgId;

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

    @ApiModelProperty(value = "收款单号")
    @TableField(exist = false)
    private String receiptNo;

    @ApiModelProperty(value = "应收单号")
    @TableField(exist = false)
    private String documentNo;

    @ApiModelProperty(value = "客户名称")
    @TableField(exist = false)
    private String customerName;
}

