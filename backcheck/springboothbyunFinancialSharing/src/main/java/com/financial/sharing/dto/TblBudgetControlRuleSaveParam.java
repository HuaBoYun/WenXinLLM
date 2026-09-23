package com.financial.sharing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * 预算管控规则保存参数
 */
@Data
@ApiModel("预算管控规则保存参数")
public class TblBudgetControlRuleSaveParam {

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

    @ApiModelProperty("控制类型")
    private String controlType;

    @ApiModelProperty("控制级别")
    private String controlLevel;

    @ApiModelProperty("规则描述")
    private String ruleDescription;

    @ApiModelProperty("规则内容")
    private String ruleContent;

    @ApiModelProperty("控制方式")
    private String controlMethod;

    @ApiModelProperty("预警阈值")
    private String warningThreshold;

    @ApiModelProperty("控制阈值")
    private String controlThreshold;

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    @ApiModelProperty("失效日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("更新人")
    private String updateUser;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("组织ID")
    private String orgId;

    @ApiModelProperty("备注")
    private String remark;
}

