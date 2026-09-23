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
 * 会计规则分类表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_ACCOUNTING_RULE_CATEGORY")
@ApiModel(value = "TblAccountingRuleCategory", description = "会计规则分类表")
public class TblAccountingRuleCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "CATEGORY_ID")
    @ApiModelProperty(value = "分类ID")
    private String categoryId;

    @TableField("CATEGORY_CODE")
    @ApiModelProperty(value = "分类编码")
    private String categoryCode;

    @TableField("CATEGORY_NAME")
    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @TableField("PARENT_ID")
    @ApiModelProperty(value = "父级ID")
    private String parentId;

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

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
