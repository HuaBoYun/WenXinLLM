package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算维度实体类
 * 
 * @description 预算维度配置实体,支持多维度预算分析
 * @author AI Assistant
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_DIMENSION")
@ApiModel(value = "BudgetDimension对象", description = "预算维度")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetDimension implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "DIMENSION_ID", type = IdType.ASSIGN_UUID)
    private String dimensionId;

    @ApiModelProperty(value = "维度名称")
    @TableField("DIMENSION_NAME")
    private String dimensionName;

    @ApiModelProperty(value = "维度编码")
    @TableField("DIMENSION_CODE")
    private String dimensionCode;

    @ApiModelProperty(value = "维度类型:ORGANIZATION-组织,TIME-时间,ACCOUNT-科目,PROJECT-项目,PRODUCT-产品,CUSTOM-自定义")
    @TableField("DIMENSION_TYPE")
    private String dimensionType;

    @ApiModelProperty(value = "上级维度ID")
    @TableField("PARENT_ID")
    private String parentId;

    @ApiModelProperty(value = "排序序号")
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @ApiModelProperty(value = "是否必填:0-否,1-是")
    @TableField("IS_REQUIRED")
    private Integer isRequired;

    @ApiModelProperty(value = "维度描述")
    @TableField("DIMENSION_DESCRIPTION")
    private String dimensionDescription;

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

    @ApiModelProperty(value = "是否启用:true-启用,false-停用")
    @TableField("IS_ACTIVE")
    private Boolean isActive;

    @ApiModelProperty(value = "删除标记:0-未删除,1-已删除")
    @TableField("IS_DELETED")
    private Integer isDeleted;

    @ApiModelProperty(value = "公司ID")
    @TableField("COMPANY_ID")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    @TableField("COMPANY_NAME")
    private String companyName;
}

