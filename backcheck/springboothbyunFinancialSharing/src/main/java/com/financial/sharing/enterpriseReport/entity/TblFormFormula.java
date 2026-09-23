package com.financial.sharing.enterpriseReport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 表单公式表
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_FORM_FORMULA")
@ApiModel(value = "TblFormFormula对象", description = "表单公式表")
public class TblFormFormula implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "公式ID")
    @TableId(value = "FORMULA_ID", type = IdType.ASSIGN_UUID)
    private String formulaId;

    @ApiModelProperty(value = "模板ID")
    @TableField("TEMPLATE_ID")
    private String templateId;

    @ApiModelProperty(value = "公式编码")
    @TableField("FORMULA_CODE")
    private String formulaCode;

    @ApiModelProperty(value = "公式名称")
    @TableField("FORMULA_NAME")
    private String formulaName;

    @ApiModelProperty(value = "公式类型：CALCULATION(计算)/SUMMARY(汇总)/VALIDATION(校验)/FETCH(取数)")
    @TableField("FORMULA_TYPE")
    private String formulaType;

    @ApiModelProperty(value = "公式表达式")
    @TableField("FORMULA_EXPRESSION")
    private String formulaExpression;

    @ApiModelProperty(value = "校验类型：MANDATORY(强制)/PROMPT(提示)")
    @TableField("VALIDATION_TYPE")
    private String validationType;

    @ApiModelProperty(value = "容差范围")
    @TableField("TOLERANCE_RANGE")
    private BigDecimal toleranceRange;

    @ApiModelProperty(value = "生效条件（JSON格式）")
    @TableField("EFFECTIVE_CONDITION")
    private String effectiveCondition;

    @ApiModelProperty(value = "排序号")
    @TableField("SORT_NO")
    private Integer sortNo;

    @ApiModelProperty(value = "状态：ACTIVE(启用)/INACTIVE(停用)")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private String tenantId;

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

    // 非数据库字段
    @ApiModelProperty(value = "模板名称")
    @TableField(exist = false)
    private String templateName;
}

