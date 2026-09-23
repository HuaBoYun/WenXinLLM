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
 * 预算维度关联关系实体类
 *
 * @author AI Assistant
 * @date 2026-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_DIMENSION_RELATION")
@ApiModel(value = "BudgetDimensionRelation对象", description = "预算维度关联关系")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetDimensionRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联ID")
    @TableId(value = "RELATION_ID", type = IdType.ASSIGN_UUID)
    private String relationId;

    @ApiModelProperty(value = "源维度ID")
    @TableField("SOURCE_DIMENSION_ID")
    private String sourceDimensionId;

    @ApiModelProperty(value = "目标维度ID")
    @TableField("TARGET_DIMENSION_ID")
    private String targetDimensionId;

    @ApiModelProperty(value = "关联类型: ONE_TO_ONE/ONE_TO_MANY/MANY_TO_MANY/HIERARCHY")
    @TableField("RELATION_TYPE")
    private String relationType;

    @ApiModelProperty(value = "关联规则")
    @TableField("RELATION_RULE")
    private String relationRule;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "删除标记: 0-未删除, 1-已删除")
    @TableField("IS_DELETED")
    private Integer isDeleted;

    /** 关联的维度名称（非数据库字段，JOIN 查询填充） */
    @TableField(exist = false)
    private String relatedDimensionName;

    /** 源维度名称（非数据库字段，JOIN 查询填充） */
    @TableField(exist = false)
    private String sourceDimensionName;

    /** 目标维度名称（非数据库字段，JOIN 查询填充） */
    @TableField(exist = false)
    private String targetDimensionName;
}
