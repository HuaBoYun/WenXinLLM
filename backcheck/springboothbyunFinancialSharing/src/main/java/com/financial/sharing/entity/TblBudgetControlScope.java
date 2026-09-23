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
 * 规则适用范围表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_BUDGET_CONTROL_SCOPE")
@ApiModel(value = "TblBudgetControlScope", description = "规则适用范围表")
public class TblBudgetControlScope implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "SCOPE_ID")
    @ApiModelProperty(value = "范围ID")
    private String scopeId;

    @TableField("RULE_ID")
    @ApiModelProperty(value = "规则ID")
    private String ruleId;

    @TableField("SCOPE_TYPE")
    @ApiModelProperty(value = "范围类型")
    private String scopeType;

    @TableField("SCOPE_VALUE")
    @ApiModelProperty(value = "范围值")
    private String scopeValue;

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
