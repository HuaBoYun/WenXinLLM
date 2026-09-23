package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会计规则中心查询参数
 */
@Data
@ApiModel("会计规则中心查询参数")
public class TblAccountingRuleCenterQueryParam {

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

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("组织ID")
    private String orgId;
}

