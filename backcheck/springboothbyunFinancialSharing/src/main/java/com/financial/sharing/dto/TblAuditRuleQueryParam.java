package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 审批规则查询参数
 */
@Data
@ApiModel("审批规则查询参数")
public class TblAuditRuleQueryParam {

    @ApiModelProperty("规则名称")
    private String ruleName;

    @ApiModelProperty("规则编码")
    private String ruleCode;

    @ApiModelProperty("规则类型(AMOUNT-金额校验,DUPLICATE-重复检查,INVOICE-发票校验,TIME-时间校验)")
    private String ruleType;

    @ApiModelProperty("是否启用(0-禁用,1-启用)")
    private Integer isEnabled;

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("页大小")
    private Integer pageSize = 10;
}
