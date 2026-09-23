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
 * 坏账回收实体类
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AR_BAD_DEBT_RECOVERY")
@ApiModel(value = "ArBadDebtRecoveryEntity对象", description = "坏账回收表")
public class ArBadDebtRecoveryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "回收ID")
    @TableId(value = "RECOVERY_ID", type = IdType.ASSIGN_UUID)
    private String recoveryId;

    @ApiModelProperty(value = "回收单号")
    @TableField("RECOVERY_NO")
    private String recoveryNo;

    @ApiModelProperty(value = "原核销ID")
    @TableField("WRITE_OFF_ID")
    private String writeOffId;

    @ApiModelProperty(value = "客户ID")
    @TableField("CUSTOMER_ID")
    private String customerId;

    @ApiModelProperty(value = "原核销金额")
    @TableField("ORIGINAL_WRITE_OFF_AMOUNT")
    private BigDecimal originalWriteOffAmount;

    @ApiModelProperty(value = "回收金额")
    @TableField("RECOVERY_AMOUNT")
    private BigDecimal recoveryAmount;

    @ApiModelProperty(value = "回收率")
    @TableField("RECOVERY_RATE")
    private BigDecimal recoveryRate;

    @ApiModelProperty(value = "回收方式(1现金回收 2以物抵债 3债务重组 4法院执行)")
    @TableField("RECOVERY_METHOD")
    private Integer recoveryMethod;

    @ApiModelProperty(value = "回收日期")
    @TableField("RECOVERY_DATE")
    private LocalDate recoveryDate;

    @ApiModelProperty(value = "备注")
    @TableField("REMARKS")
    private String remarks;

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

    @ApiModelProperty(value = "客户名称")
    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty(value = "回收方式名称")
    @TableField(exist = false)
    private String recoveryMethodName;
}

