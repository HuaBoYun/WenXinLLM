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
 * 固定资产变动实体类
 * @author system
 * @since 2026-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FIXED_ASSET_CHANGE")
@ApiModel(value = "FixedAssetChangeEntity对象", description = "资产变动表")
public class FixedAssetChangeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "变动ID")
    @TableId(value = "CHANGE_ID", type = IdType.ASSIGN_UUID)
    private String changeId;

    @ApiModelProperty(value = "变动单号")
    @TableField("CHANGE_NO")
    private String changeNo;

    @ApiModelProperty(value = "变动类型")
    @TableField("CHANGE_TYPE")
    private String changeType;

    @ApiModelProperty(value = "资产ID")
    @TableField("ASSET_ID")
    private String assetId;

    @ApiModelProperty(value = "变动金额")
    @TableField("CHANGE_AMOUNT")
    private BigDecimal changeAmount;

    @ApiModelProperty(value = "变动日期")
    @TableField("CHANGE_DATE")
    private LocalDate changeDate;

    @ApiModelProperty(value = "原部门ID")
    @TableField("FROM_DEPT_ID")
    private String fromDeptId;

    @ApiModelProperty(value = "目标部门ID")
    @TableField("TO_DEPT_ID")
    private String toDeptId;

    @ApiModelProperty(value = "变动原因")
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

    @ApiModelProperty(value = "原部门名称")
    @TableField(exist = false)
    private String fromDeptName;

    @ApiModelProperty(value = "目标部门名称")
    @TableField(exist = false)
    private String toDeptName;

    @ApiModelProperty(value = "操作人姓名")
    @TableField(exist = false)
    private String operatorName;
}

