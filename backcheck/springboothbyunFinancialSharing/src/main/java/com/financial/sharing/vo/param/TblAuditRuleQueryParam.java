package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 审批规则查询参数
 *
 * @author system
 * @since 2025-01-30
 */
@Data
@ApiModel(value = "TblAuditRuleQueryParam", description = "审批规则查询参数")
public class TblAuditRuleQueryParam {

    @ApiModelProperty(value = "分页当前页数,默认为1")
    private Integer pageNumber = 1;

    @ApiModelProperty(value = "每页记录数,默认为15")
    private Integer pageSize = 15;

    @ApiModelProperty(value = "规则编码")
    private String ruleCode;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "规则类型(AMOUNT-金额校验,DUPLICATE-重复检查,INVOICE-发票校验,TIME-时间校验)")
    private String ruleType;

    @ApiModelProperty(value = "是否启用(0-禁用,1-启用)")
    private Integer isEnabled;

    @ApiModelProperty(value = "生效日期开始")
    private LocalDate effectiveDateStart;

    @ApiModelProperty(value = "生效日期结束")
    private LocalDate effectiveDateEnd;
}
