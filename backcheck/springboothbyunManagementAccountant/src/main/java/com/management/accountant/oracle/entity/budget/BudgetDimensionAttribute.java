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
 * 预算维度属性实体类
 *
 * @author AI Assistant
 * @date 2026-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_DIMENSION_ATTRIBUTE")
@ApiModel(value = "BudgetDimensionAttribute对象", description = "预算维度属性")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetDimensionAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "属性ID")
    @TableId(value = "ATTRIBUTE_ID", type = IdType.ASSIGN_UUID)
    private String attributeId;

    @ApiModelProperty(value = "维度ID")
    @TableField("DIMENSION_ID")
    private String dimensionId;

    @ApiModelProperty(value = "属性名称")
    @TableField("ATTRIBUTE_NAME")
    private String attributeName;

    @ApiModelProperty(value = "属性类型: STRING/NUMBER/DATE/BOOLEAN/LIST")
    @TableField("ATTRIBUTE_TYPE")
    private String attributeType;

    @ApiModelProperty(value = "默认值")
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    @ApiModelProperty(value = "是否必填: 0-否, 1-是")
    @TableField("IS_REQUIRED")
    private Integer isRequired;

    @ApiModelProperty(value = "属性描述")
    @TableField("ATTRIBUTE_DESCRIPTION")
    private String attributeDescription;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "删除标记: 0-未删除, 1-已删除")
    @TableField("IS_DELETED")
    private Integer isDeleted;
}
