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
 * 规则执行日志表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_AUDIT_RULE_LOG")
@ApiModel(value = "TblAuditRuleLog", description = "规则执行日志表")
public class TblAuditRuleLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "LOG_ID")
    @ApiModelProperty(value = "日志ID")
    private String logId;

    @TableField("RULE_ID")
    @ApiModelProperty(value = "规则ID")
    private String ruleId;

    @TableField("BUSINESS_TYPE")
    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @TableField("BUSINESS_ID")
    @ApiModelProperty(value = "业务ID")
    private String businessId;

    @TableField("EXECUTION_RESULT")
    @ApiModelProperty(value = "执行结果(SUCCESS-成功,FAILED-失败)")
    private String executionResult;

    @TableField("EXECUTION_MESSAGE")
    @ApiModelProperty(value = "执行消息")
    private String executionMessage;

    @TableField("EXECUTION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "执行时间")
    private LocalDateTime executionTime;

    @TableField("EXECUTION_USER")
    @ApiModelProperty(value = "执行人ID")
    private String executionUser;

    @TableField("EXECUTION_USER_NAME")
    @ApiModelProperty(value = "执行人姓名")
    private String executionUserName;

    @TableField("IP_ADDRESS")
    @ApiModelProperty(value = "IP地址")
    private String ipAddress;
}
