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
 * 费用参数表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_EXPENSE_PARAMETER")
@ApiModel(value = "TblExpenseParameter", description = "费用参数表")
public class TblExpenseParameter implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PARAMETER_ID")
    @ApiModelProperty(value = "参数ID")
    private String parameterId;

    @TableField("PARAMETER_CODE")
    @ApiModelProperty(value = "参数编码")
    private String parameterCode;

    @TableField("PARAMETER_NAME")
    @ApiModelProperty(value = "参数名称")
    private String parameterName;

    @TableField("PARAMETER_TYPE")
    @ApiModelProperty(value = "参数类型")
    private String parameterType;

    @TableField("DATA_TYPE")
    @ApiModelProperty(value = "数据类型")
    private String dataType;

    @TableField("PARAMETER_VALUE")
    @ApiModelProperty(value = "参数值")
    private String parameterValue;

    @TableField("DEFAULT_VALUE")
    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    @TableField("IS_REQUIRED")
    @ApiModelProperty(value = "是否必填")
    private Integer isRequired;

    @TableField("IS_EDITABLE")
    @ApiModelProperty(value = "是否可编辑")
    private Integer isEditable;

    @TableField("VALIDATION_RULE")
    @ApiModelProperty(value = "验证规则")
    private String validationRule;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用")
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
