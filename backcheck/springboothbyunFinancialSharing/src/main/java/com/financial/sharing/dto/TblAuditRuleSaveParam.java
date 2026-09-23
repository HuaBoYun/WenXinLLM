package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 审批规则保存参数
 */
@Data
@ApiModel("审批规则保存参数")
public class TblAuditRuleSaveParam {

    @ApiModelProperty("规则ID(更新时必填)")
    private String ruleId;

    @ApiModelProperty("规则编码")
    private String ruleCode;

    @ApiModelProperty("规则名称")
    private String ruleName;

    @ApiModelProperty("规则类型(AMOUNT-金额校验,DUPLICATE-重复检查,INVOICE-发票校验,TIME-时间校验)")
    private String ruleType;

    @ApiModelProperty("优先级(数字越大优先级越高)")
    private Integer priority;

    @ApiModelProperty("规则表达式")
    private String ruleExpression;

    @ApiModelProperty("规则动作(WARNING-警告,BLOCK-拦截,AUTO_APPROVE-自动通过)")
    private String ruleAction;

    @ApiModelProperty("警告消息")
    private String warningMessage;

    @ApiModelProperty("生效日期")
    private LocalDate effectiveDate;

    @ApiModelProperty("失效日期")
    private LocalDate expiryDate;

    @ApiModelProperty("是否启用(0-禁用,1-启用)")
    private Integer isEnabled;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人(新增时必填)")
    private String createUser;

    @ApiModelProperty("更新人(更新时必填)")
    private String updateUser;
}
