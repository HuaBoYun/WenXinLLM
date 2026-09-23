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
 * 固定资产卡片实体类
 * @author system
 * @since 2026-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FIXED_ASSET_CARD")
@ApiModel(value = "FixedAssetCardEntity对象", description = "资产卡片表")
public class FixedAssetCardEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资产ID")
    @TableId(value = "ASSET_ID", type = IdType.ASSIGN_UUID)
    private String assetId;

    @ApiModelProperty(value = "资产编码")
    @TableField("ASSET_CODE")
    private String assetCode;

    @ApiModelProperty(value = "资产名称")
    @TableField("ASSET_NAME")
    private String assetName;

    @ApiModelProperty(value = "资产类别ID")
    @TableField("CATEGORY_ID")
    private String categoryId;

    @ApiModelProperty(value = "资产原值")
    @TableField("ORIGINAL_VALUE")
    private BigDecimal originalValue;

    @ApiModelProperty(value = "残值")
    @TableField("RESIDUAL_VALUE")
    private BigDecimal residualValue;

    @ApiModelProperty(value = "累计折旧")
    @TableField("ACCUMULATED_DEPRECIATION")
    private BigDecimal accumulatedDepreciation;

    @ApiModelProperty(value = "净值")
    @TableField("NET_VALUE")
    private BigDecimal netValue;

    @ApiModelProperty(value = "折旧方法")
    @TableField("DEPRECIATION_METHOD")
    private String depreciationMethod;

    @ApiModelProperty(value = "使用年限(月)")
    @TableField("USEFUL_LIFE")
    private Integer usefulLife;

    @ApiModelProperty(value = "使用部门ID")
    @TableField("DEPT_ID")
    private String deptId;

    @ApiModelProperty(value = "责任人")
    @TableField("RESPONSIBLE_PERSON")
    private String responsiblePerson;

    @ApiModelProperty(value = "购置日期")
    @TableField("PURCHASE_DATE")
    private LocalDate purchaseDate;

    @ApiModelProperty(value = "开始折旧日期")
    @TableField("START_DEPRECIATION_DATE")
    private LocalDate startDepreciationDate;

    @ApiModelProperty(value = "资产状态")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "规格型号")
    @TableField("SPECIFICATION")
    private String specification;

    @ApiModelProperty(value = "生产厂商")
    @TableField("MANUFACTURER")
    private String manufacturer;

    @ApiModelProperty(value = "供应商")
    @TableField("SUPPLIER")
    private String supplier;

    @ApiModelProperty(value = "存放地点")
    @TableField("LOCATION")
    private String location;

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
    
    @ApiModelProperty(value = "资产类别名称")
    @TableField(exist = false)
    private String categoryName;

    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty(value = "创建人姓名")
    @TableField(exist = false)
    private String createByName;

    @ApiModelProperty(value = "更新人姓名")
    @TableField(exist = false)
    private String updateByName;
}

