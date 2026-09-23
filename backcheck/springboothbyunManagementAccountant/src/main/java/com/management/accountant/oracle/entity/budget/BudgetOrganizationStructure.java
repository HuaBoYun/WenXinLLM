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
 * 预算组织体系实体类
 * 
 * @description 预算组织体系管理实体,支持多种组织架构类型
 * @author AI Assistant
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_ORGANIZATION_STRUCTURE")
@ApiModel(value = "BudgetOrganizationStructure对象", description = "预算组织体系")
public class BudgetOrganizationStructure implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "STRUCTURE_ID", type = IdType.ASSIGN_UUID)
    private String structureId;

    @ApiModelProperty(value = "体系编码")
    @TableField("STRUCTURE_CODE")
    private String structureCode;

    @ApiModelProperty(value = "体系名称")
    @TableField("STRUCTURE_NAME")
    private String structureName;

    @ApiModelProperty(value = "体系类型:single-单一集团,multi-多集团,hierarchical-分级管理,matrix-矩阵式,hybrid-混合式")
    @TableField("STRUCTURE_TYPE")
    private String structureType;

    @ApiModelProperty(value = "控制模式:centralized-集中式,decentralized-分散式,hybrid-混合式")
    @TableField("CONTROL_MODE")
    private String controlMode;

    @ApiModelProperty(value = "最大层级数(1-20)")
    @TableField("MAX_LEVELS")
    private Integer maxLevels;

    @ApiModelProperty(value = "是否启用:0-否,1-是")
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @ApiModelProperty(value = "体系描述")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;

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
}

