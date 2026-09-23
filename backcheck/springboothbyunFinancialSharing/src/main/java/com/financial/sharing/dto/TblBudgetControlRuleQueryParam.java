package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 预算管控规则查询参数
 */
@Data
@ApiModel("预算管控规则查询参数")
public class TblBudgetControlRuleQueryParam {

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize = 10;

    @ApiModelProperty("规则编码")
    private String ruleCode;

    @ApiModelProperty("规则名称")
    private String ruleName;

    @ApiModelProperty("规则类型")
    private String ruleType;

    @ApiModelProperty("控制类型")
    private String controlType;

    @ApiModelProperty("控制级别")
    private String controlLevel;

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("组织ID")
    private String orgId;
}

