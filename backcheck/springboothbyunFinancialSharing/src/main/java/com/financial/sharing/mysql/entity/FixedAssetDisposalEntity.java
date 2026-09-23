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
 * 固定资产处置实体类
 * @author system
 * @since 2026-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FIXED_ASSET_DISPOSAL")
@ApiModel(value = "FixedAssetDisposalEntity对象", description = "资产处置表")
public class FixedAssetDisposalEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "处置ID")
    @TableId(value = "DISPOSAL_ID", type = IdType.ASSIGN_UUID)
    private String disposalId;

    @ApiModelProperty(value = "处置单号")
    @TableField("DISPOSAL_NO")
    private String disposalNo;

    @ApiModelProperty(value = "处置类型")
    @TableField("DISPOSAL_TYPE")
    private String disposalType;

    @ApiModelProperty(value = "资产ID")
    @TableField("ASSET_ID")
    private String assetId;

    @ApiModelProperty(value = "资产原值")
    @TableField("ORIGINAL_VALUE")
    private BigDecimal originalValue;

    @ApiModelProperty(value = "账面净值")
    @TableField("NET_VALUE")
    private BigDecimal netValue;

    @ApiModelProperty(value = "处置收入")
    @TableField("DISPOSAL_INCOME")
    private BigDecimal disposalIncome;

    @ApiModelProperty(value = "处置损益")
    @TableField("DISPOSAL_PROFIT_LOSS")
    private BigDecimal disposalProfitLoss;

    @ApiModelProperty(value = "处置日期")
    @TableField("DISPOSAL_DATE")
    private LocalDate disposalDate;

    @ApiModelProperty(value = "处置原因")
    @TableField("REASON")
    private String reason;

    @ApiModelProperty(value = "状态")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;

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

    // 扩展字段 - 不映射到数据库

    @ApiModelProperty(value = "资产编码")
    @TableField(exist = false)
    private String assetCode;

    @ApiModelProperty(value = "资产名称")
    @TableField(exist = false)
    private String assetName;

    @ApiModelProperty(value = "累计折旧")
    @TableField(exist = false)
    private BigDecimal accumulatedDepreciation;

    @ApiModelProperty(value = "操作人姓名")
    @TableField(exist = false)
    private String operatorName;
}

