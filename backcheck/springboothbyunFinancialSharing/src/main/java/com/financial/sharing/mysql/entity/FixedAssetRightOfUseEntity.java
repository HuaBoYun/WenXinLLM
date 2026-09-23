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
 * 使用权资产实体类
 * @author system
 * @since 2026-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FIXED_ASSET_RIGHT_OF_USE")
@ApiModel(value = "FixedAssetRightOfUseEntity对象", description = "使用权资产表")
public class FixedAssetRightOfUseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "使用权资产ID")
    @TableId(value = "ROU_ASSET_ID", type = IdType.ASSIGN_UUID)
    private String rouAssetId;

    @ApiModelProperty(value = "关联资产ID")
    @TableField("ASSET_ID")
    private String assetId;

    @ApiModelProperty(value = "租赁合同号")
    @TableField("LEASE_CONTRACT_NO")
    private String leaseContractNo;

    @ApiModelProperty(value = "租赁开始日期")
    @TableField("LEASE_START_DATE")
    private LocalDate leaseStartDate;

    @ApiModelProperty(value = "租赁结束日期")
    @TableField("LEASE_END_DATE")
    private LocalDate leaseEndDate;

    @ApiModelProperty(value = "租赁期限(月)")
    @TableField("LEASE_TERM")
    private Integer leaseTerm;

    @ApiModelProperty(value = "初始计量金额")
    @TableField("INITIAL_MEASUREMENT")
    private BigDecimal initialMeasurement;

    @ApiModelProperty(value = "折现率")
    @TableField("DISCOUNT_RATE")
    private BigDecimal discountRate;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private Long tenantId;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    @TableField(value = "UPDATE_BY", fill = FieldFill.UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "UPDATE_TIME", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除(0否1是)")
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;
}

