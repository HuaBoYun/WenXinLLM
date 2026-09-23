package com.financial.sharing.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * 会计规则中心保存参数
 */
@Data
@ApiModel("会计规则中心保存参数")
public class TblAccountingRuleCenterSaveParam {

    @ApiModelProperty("规则ID")
    private String ruleId;

    @ApiModelProperty(value = "规则编码", required = true)
    @NotBlank(message = "规则编码不能为空")
    private String ruleCode;

    @ApiModelProperty(value = "规则名称", required = true)
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    @ApiModelProperty("规则类型")
    private String ruleType;

    @ApiModelProperty("优先级(数字越大优先级越高)")
    private Integer priority;

    @ApiModelProperty("借方科目")
    private String debitAccount;

    @ApiModelProperty("贷方科目")
    private String creditAccount;

    @ApiModelProperty("核算维度配置(JSON格式)")
    private String dimensionConfig;

    @ApiModelProperty("规则描述")
    private String ruleDescription;

    @ApiModelProperty("规则内容")
    private String ruleContent;

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    @ApiModelProperty("失效日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("组织ID")
    private String orgId;

    @ApiModelProperty("备注")
    private String remark;
}

