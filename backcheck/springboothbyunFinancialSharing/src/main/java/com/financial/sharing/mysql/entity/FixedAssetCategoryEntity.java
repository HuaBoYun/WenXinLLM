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
 * 固定资产类别实体类
 * @author system
 * @since 2026-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FIXED_ASSET_CATEGORY")
@ApiModel(value = "FixedAssetCategoryEntity对象", description = "资产类别表")
public class FixedAssetCategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类别ID")
    @TableId(value = "CATEGORY_ID", type = IdType.ASSIGN_UUID)
    private String categoryId;

    @ApiModelProperty(value = "类别编码")
    @TableField("CATEGORY_CODE")
    private String categoryCode;

    @ApiModelProperty(value = "类别名称")
    @TableField("CATEGORY_NAME")
    private String categoryName;

    @ApiModelProperty(value = "父类别ID")
    @TableField("PARENT_ID")
    private String parentId;

    @ApiModelProperty(value = "默认折旧方法")
    @TableField("DEPRECIATION_METHOD")
    private String depreciationMethod;

    @ApiModelProperty(value = "默认使用年限(月)")
    @TableField("USEFUL_LIFE")
    private Integer usefulLife;

    @ApiModelProperty(value = "默认残值率")
    @TableField("RESIDUAL_RATE")
    private BigDecimal residualRate;

    @ApiModelProperty(value = "排序号")
    @TableField("SORT_ORDER")
    private Integer sortOrder;

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
    
    @ApiModelProperty(value = "父类别名称")
    @TableField(exist = false)
    private String parentName;

    @ApiModelProperty(value = "子类别列表")
    @TableField(exist = false)
    private java.util.List<FixedAssetCategoryEntity> children;
}

