package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 多账簿资产实体类
 * @author system
 * @since 2026-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FIXED_ASSET_MULTI_BOOK")
@ApiModel(value = "FixedAssetMultiBookEntity对象", description = "多账簿资产表")
public class FixedAssetMultiBookEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "多账簿ID")
    @TableId(value = "MULTI_BOOK_ID", type = IdType.ASSIGN_UUID)
    private String multiBookId;

    @ApiModelProperty(value = "资产ID")
    @TableField("ASSET_ID")
    private String assetId;

    @ApiModelProperty(value = "账簿ID")
    @TableField("BOOK_ID")
    private String bookId;

    @ApiModelProperty(value = "折旧方法")
    @TableField("DEPRECIATION_METHOD")
    private String depreciationMethod;

    @ApiModelProperty(value = "使用年限(月)")
    @TableField("USEFUL_LIFE")
    private Integer usefulLife;

    @ApiModelProperty(value = "累计折旧")
    @TableField("ACCUMULATED_DEPRECIATION")
    private BigDecimal accumulatedDepreciation;

    @ApiModelProperty(value = "净值")
    @TableField("NET_VALUE")
    private BigDecimal netValue;

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

