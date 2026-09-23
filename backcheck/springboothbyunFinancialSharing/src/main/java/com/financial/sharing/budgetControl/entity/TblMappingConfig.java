package com.financial.sharing.budgetControl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 映射配置表实体类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@TableName("TBL_MAPPING_CONFIG")
@ApiModel(value = "TblMappingConfig对象", description = "映射配置表")
public class TblMappingConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "映射ID")
    @TableId(value = "MAPPING_ID", type = IdType.ASSIGN_ID)
    private String mappingId;

    @ApiModelProperty(value = "映射编码")
    @TableField("MAPPING_CODE")
    private String mappingCode;

    @ApiModelProperty(value = "映射名称")
    @TableField("MAPPING_NAME")
    private String mappingName;

    @ApiModelProperty(value = "来源系统：VOUCHER(凭证)/PAYMENT(付款)/CONTRACT(合同)/PURCHASE(采购)")
    @TableField("SOURCE_SYSTEM")
    private String sourceSystem;

    @ApiModelProperty(value = "来源单据类型")
    @TableField("SOURCE_DOC_TYPE")
    private String sourceDocType;

    @ApiModelProperty(value = "映射类型：SUBJECT(科目)/ORG(组织)/PROJECT(项目)/DIMENSION(维度)")
    @TableField("MAPPING_TYPE")
    private String mappingType;

    @ApiModelProperty(value = "映射规则（JSON格式）")
    @TableField("MAPPING_RULES")
    private String mappingRules;

    @ApiModelProperty(value = "默认值")
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    @ApiModelProperty(value = "是否必填：Y/N")
    @TableField("IS_MANDATORY")
    private String isMandatory;

    @ApiModelProperty(value = "校验规则")
    @TableField("VALIDATION_RULE")
    private String validationRule;

    @ApiModelProperty(value = "是否启用：Y/N")
    @TableField("IS_ENABLED")
    private String isEnabled;

    @ApiModelProperty(value = "排序号")
    @TableField("SORT_NO")
    private Integer sortNo;

    @ApiModelProperty(value = "组织ID")
    @TableField("ORG_ID")
    private String orgId;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @ApiModelProperty(value = "修改时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;
}

