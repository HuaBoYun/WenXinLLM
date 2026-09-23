package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 审批规则保存参数
 *
 * @author system
 * @since 2025-01-30
 */
@Data
@ApiModel(value = "TblAuditRuleSaveParam", description = "审批规则保存参数")
public class TblAuditRuleSaveParam {

    @ApiModelProperty(value = "规则ID（更新时必填）")
    private String ruleId;

    @ApiModelProperty(value = "规则编码")
    @NotBlank(message = "规则编码不能为空")
    private String ruleCode;

    @ApiModelProperty(value = "规则名称")
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    @ApiModelProperty(value = "规则类型(AMOUNT-金额校验,DUPLICATE-重复检查,INVOICE-发票校验,TIME-时间校验)")
    @NotBlank(message = "规则类型不能为空")
    private String ruleType;

    @ApiModelProperty(value = "优先级(数字越大优先级越高)")
    @NotNull(message = "优先级不能为空")
    private Integer priority;

    @ApiModelProperty(value = "规则表达式")
    private String ruleExpression;

    @ApiModelProperty(value = "规则动作(WARNING-警告,BLOCK-拦截,AUTO_APPROVE-自动通过)")
    @NotBlank(message = "规则动作不能为空")
    private String ruleAction;

    @ApiModelProperty(value = "警告消息")
    private String warningMessage;

    @ApiModelProperty(value = "生效日期")
    private LocalDate effectiveDate;

    @ApiModelProperty(value = "失效日期")
    private LocalDate expiryDate;

    @ApiModelProperty(value = "是否启用(0-禁用,1-启用)")
    private Integer isEnabled;

    @ApiModelProperty(value = "备注")
    private String remark;
}
