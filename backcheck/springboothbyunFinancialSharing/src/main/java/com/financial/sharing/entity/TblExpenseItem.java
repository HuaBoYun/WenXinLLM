package com.financial.sharing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 费用项目表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_EXPENSE_ITEM")
@ApiModel(value = "TblExpenseItem", description = "费用项目表")
public class TblExpenseItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ITEM_ID")
    @ApiModelProperty(value = "费用项目ID")
    private String itemId;

    @TableField("ITEM_CODE")
    @ApiModelProperty(value = "费用项目编码")
    private String itemCode;

    @TableField("ITEM_NAME")
    @ApiModelProperty(value = "费用项目名称")
    private String itemName;

    @TableField("PARENT_ID")
    @ApiModelProperty(value = "父级ID")
    private String parentId;

    @TableField("ITEM_LEVEL")
    @ApiModelProperty(value = "层级")
    private Integer itemLevel;

    @TableField("SORT_ORDER")
    @ApiModelProperty(value = "排序号")
    private Integer sortOrder;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用(0-禁用,1-启用)")
    private Integer isEnabled;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
