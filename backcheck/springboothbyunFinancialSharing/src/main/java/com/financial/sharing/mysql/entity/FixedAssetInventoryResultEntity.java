package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 固定资产盘点结果实体类
 * @author system
 * @since 2026-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FIXED_ASSET_INVENTORY_RESULT")
@ApiModel(value = "FixedAssetInventoryResultEntity对象", description = "盘点结果表")
public class FixedAssetInventoryResultEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结果ID")
    @TableId(value = "RESULT_ID", type = IdType.ASSIGN_UUID)
    private String resultId;

    @ApiModelProperty(value = "任务ID")
    @TableField("TASK_ID")
    private String taskId;

    @ApiModelProperty(value = "资产ID")
    @TableField("ASSET_ID")
    private String assetId;

    @ApiModelProperty(value = "账面数量")
    @TableField("BOOK_QUANTITY")
    private Integer bookQuantity;

    @ApiModelProperty(value = "实盘数量")
    @TableField("ACTUAL_QUANTITY")
    private Integer actualQuantity;

    @ApiModelProperty(value = "差异数量")
    @TableField("DIFFERENCE_QUANTITY")
    private Integer differenceQuantity;

    @ApiModelProperty(value = "盘点结果")
    @TableField("INVENTORY_RESULT")
    private String inventoryResult;

    @ApiModelProperty(value = "存放地点")
    @TableField("LOCATION")
    private String location;

    @ApiModelProperty(value = "盘点日期")
    @TableField("INVENTORY_DATE")
    private LocalDate inventoryDate;

    @ApiModelProperty(value = "盘点人")
    @TableField("INVENTORY_PERSON")
    private String inventoryPerson;

    @ApiModelProperty(value = "处理状态")
    @TableField("HANDLE_STATUS")
    private String handleStatus;

    @ApiModelProperty(value = "处理说明")
    @TableField("HANDLE_REMARK")
    private String handleRemark;

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
}

