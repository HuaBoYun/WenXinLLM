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
 * 坏账准备实体类
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AR_BAD_DEBT_PROVISION")
@ApiModel(value = "ArBadDebtProvisionEntity对象", description = "坏账准备表")
public class ArBadDebtProvisionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "计提ID")
    @TableId(value = "PROVISION_ID", type = IdType.ASSIGN_UUID)
    private String provisionId;

    @ApiModelProperty(value = "客户ID")
    @TableField("CUSTOMER_ID")
    private String customerId;

    @ApiModelProperty(value = "应收金额")
    @TableField("RECEIVABLE_AMOUNT")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "计提比例")
    @TableField("PROVISION_RATE")
    private BigDecimal provisionRate;

    @ApiModelProperty(value = "计提金额")
    @TableField("PROVISION_AMOUNT")
    private BigDecimal provisionAmount;

    @ApiModelProperty(value = "计提方法(1账龄分析法 2余额百分比法 3销售百分比法 4个别认定法)")
    @TableField("PROVISION_METHOD")
    private Integer provisionMethod;

    @ApiModelProperty(value = "计提日期")
    @TableField("PROVISION_DATE")
    private LocalDate provisionDate;

    @ApiModelProperty(value = "计提状态(0待确认 1已确认 2已调整)")
    @TableField("PROVISION_STATUS")
    private Integer provisionStatus;

    @ApiModelProperty(value = "备注")
    @TableField("REMARKS")
    private String remarks;

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

    @ApiModelProperty(value = "计提方法名称")
    @TableField(exist = false)
    private String provisionMethodName;

    @ApiModelProperty(value = "计提状态名称")
    @TableField(exist = false)
    private String provisionStatusName;
}

