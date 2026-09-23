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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预算控制规则表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_BUDGET_CONTROL_RULE")
@ApiModel(value = "TblBudgetControlRule", description = "预算控制规则表")
public class TblBudgetControlRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "RULE_ID")
    @ApiModelProperty(value = "规则ID")
    private String ruleId;

    @TableField("RULE_CODE")
    @ApiModelProperty(value = "规则编码")
    private String ruleCode;

    @TableField("RULE_NAME")
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @TableField("CONTROL_TYPE")
    @ApiModelProperty(value = "控制类型")
    private String controlType;

    @TableField("WARNING_THRESHOLD")
    @ApiModelProperty(value = "预警阈值(百分比)")
    private BigDecimal warningThreshold;

    @TableField("CONTROL_THRESHOLD")
    @ApiModelProperty(value = "控制阈值(百分比)")
    private BigDecimal controlThreshold;

    @TableField("EFFECTIVE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生效日期")
    private LocalDate effectiveDate;

    @TableField("EXPIRY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "失效日期")
    private LocalDate expiryDate;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
