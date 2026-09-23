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
 * 账单稽核规则关联表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_BILL_AUDIT_RULE")
@ApiModel(value = "TblBillAuditRule", description = "账单稽核规则关联表")
public class TblBillAuditRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID")
    @ApiModelProperty(value = "ID")
    private String id;

    @TableField("CONFIG_ID")
    @ApiModelProperty(value = "配置ID")
    private String configId;

    @TableField("RULE_ID")
    @ApiModelProperty(value = "规则ID")
    private String ruleId;

    @TableField("RULE_NAME")
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @TableField("RULE_TYPE")
    @ApiModelProperty(value = "规则类型")
    private String ruleType;

    @TableField("PRIORITY")
    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @TableField("RULE_EXPRESSION")
    @ApiModelProperty(value = "规则表达式")
    private String ruleExpression;

    @TableField("WARNING_MESSAGE")
    @ApiModelProperty(value = "警告消息")
    private String warningMessage;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用(0-禁用,1-启用)")
    private Integer isEnabled;

    @TableField("DESCRIPTION")
    @ApiModelProperty(value = "规则描述")
    private String description;

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
