package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算维度值实体类
 * 
 * @description 预算维度值管理实体,支持树形结构的维度值配置
 * @author AI Agent
 * @date 2026-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_DIMENSION_VALUE")
@ApiModel(value = "BudgetDimensionValue对象", description = "预算维度值")
public class BudgetDimensionValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "VALUE_ID", type = IdType.ASSIGN_UUID)
    private String valueId;

    @ApiModelProperty(value = "维度ID")
    @TableField("DIMENSION_ID")
    private String dimensionId;

    @ApiModelProperty(value = "维度值编码")
    @TableField("VALUE_CODE")
    private String valueCode;

    @ApiModelProperty(value = "维度值名称")
    @TableField("VALUE_NAME")
    private String valueName;

    @ApiModelProperty(value = "维度值描述")
    @TableField("VALUE_DESCRIPTION")
    private String valueDescription;

    @ApiModelProperty(value = "父级维度值ID")
    @TableField("PARENT_ID")
    private String parentId;

    @ApiModelProperty(value = "层级（从1开始）")
    @TableField("LEVEL_NUM")
    private Integer levelNum;

    @ApiModelProperty(value = "路径信息（如：/1/2/3）")
    @TableField("PATH_INFO")
    private String pathInfo;

    @ApiModelProperty(value = "是否叶子节点:0-否,1-是")
    @TableField("IS_LEAF")
    private Integer isLeaf;

    @ApiModelProperty(value = "排序序号")
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @ApiModelProperty(value = "是否启用:0-否,1-是")
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @ApiModelProperty(value = "扩展属性（JSON格式）")
    @TableField("ATTRIBUTES")
    private String attributes;

    @ApiModelProperty(value = "创建人ID")
    @TableField("CREATOR_ID")
    private String creatorId;

    @ApiModelProperty(value = "创建人姓名")
    @TableField("CREATOR_NAME")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "更新人ID")
    @TableField("UPDATER_ID")
    private String updaterId;

    @ApiModelProperty(value = "更新人姓名")
    @TableField("UPDATER_NAME")
    private String updaterName;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value = "删除标记:0-未删除,1-已删除")
    @TableField("IS_DELETED")
    private Integer isDeleted;

    @ApiModelProperty(value = "公司ID")
    @TableField("COMPANY_ID")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    @TableField("COMPANY_NAME")
    private String companyName;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;
}

