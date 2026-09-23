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
 * 固定资产盘点任务实体类
 * @author system
 * @since 2026-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FIXED_ASSET_INVENTORY_TASK")
@ApiModel(value = "FixedAssetInventoryTaskEntity对象", description = "盘点任务表")
public class FixedAssetInventoryTaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID")
    @TableId(value = "TASK_ID", type = IdType.ASSIGN_UUID)
    private String taskId;

    @ApiModelProperty(value = "任务编号")
    @TableField("TASK_NO")
    private String taskNo;

    @ApiModelProperty(value = "任务名称")
    @TableField("TASK_NAME")
    private String taskName;

    @ApiModelProperty(value = "盘点类型")
    @TableField("INVENTORY_TYPE")
    private String inventoryType;

    @ApiModelProperty(value = "资产数量")
    @TableField("ASSET_COUNT")
    private Integer assetCount;

    @ApiModelProperty(value = "已盘点数量")
    @TableField("INVENTORIED_COUNT")
    private Integer inventoriedCount;

    @ApiModelProperty(value = "差异数量")
    @TableField("DIFFERENCE_COUNT")
    private Integer differenceCount;

    @ApiModelProperty(value = "进度")
    @TableField("PROGRESS")
    private BigDecimal progress;

    @ApiModelProperty(value = "状态")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "计划日期")
    @TableField("PLAN_DATE")
    private LocalDate planDate;

    @ApiModelProperty(value = "负责人")
    @TableField("RESPONSIBLE_PERSON")
    private String responsiblePerson;

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
}

