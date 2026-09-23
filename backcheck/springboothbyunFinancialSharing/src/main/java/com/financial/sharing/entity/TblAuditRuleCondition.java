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
 * 规则条件表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_AUDIT_RULE_CONDITION")
@ApiModel(value = "TblAuditRuleCondition", description = "规则条件表")
public class TblAuditRuleCondition implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "CONDITION_ID")
    @ApiModelProperty(value = "条件ID")
    private String conditionId;

    @TableField("RULE_ID")
    @ApiModelProperty(value = "规则ID")
    private String ruleId;

    @TableField("CONDITION_NAME")
    @ApiModelProperty(value = "条件名称")
    private String conditionName;

    @TableField("CONDITION_FIELD")
    @ApiModelProperty(value = "条件字段")
    private String conditionField;

    @TableField("CONDITION_OPERATOR")
    @ApiModelProperty(value = "条件操作符(EQUAL-等于,GREATER-大于,LESS-小于,CONTAIN-包含)")
    private String conditionOperator;

    @TableField("CONDITION_VALUE")
    @ApiModelProperty(value = "条件值")
    private String conditionValue;

    @TableField("LOGIC_OPERATOR")
    @ApiModelProperty(value = "逻辑操作符(AND-且,OR-或)")
    private String logicOperator;

    @TableField("SORT_ORDER")
    @ApiModelProperty(value = "排序号")
    private Integer sortOrder;

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
