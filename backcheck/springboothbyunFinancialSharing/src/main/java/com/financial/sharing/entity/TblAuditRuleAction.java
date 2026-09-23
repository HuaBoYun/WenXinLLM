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
 * 规则动作表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_AUDIT_RULE_ACTION")
@ApiModel(value = "TblAuditRuleAction", description = "规则动作表")
public class TblAuditRuleAction implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ACTION_ID")
    @ApiModelProperty(value = "动作ID")
    private String actionId;

    @TableField("RULE_ID")
    @ApiModelProperty(value = "规则ID")
    private String ruleId;

    @TableField("ACTION_NAME")
    @ApiModelProperty(value = "动作名称")
    private String actionName;

    @TableField("ACTION_TYPE")
    @ApiModelProperty(value = "动作类型")
    private String actionType;

    @TableField("ACTION_CONFIG")
    @ApiModelProperty(value = "动作配置(JSON格式)")
    private String actionConfig;

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
