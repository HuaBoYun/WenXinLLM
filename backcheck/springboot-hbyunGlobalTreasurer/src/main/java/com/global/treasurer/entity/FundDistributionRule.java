package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资金下拨规则实体类
 * 
 * @author Global Treasurer System
 * @since 2025-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FUND_DISTRIBUTION_RULES")
@ApiModel(value = "FundDistributionRule对象", description = "资金下拨规则表")
public class FundDistributionRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规则ID")
    @TableId(value = "RULEID", type = IdType.AUTO)
    private Long ruleId;

    @ApiModelProperty(value = "规则编码")
    @TableField("RULECODE")
    @NotBlank(message = "规则编码不能为空")
    private String ruleCode;

    @ApiModelProperty(value = "规则名称")
    @TableField("RULENAME")
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    @ApiModelProperty(value = "资金池ID")
    @TableField("POOLID")
    @NotNull(message = "资金池ID不能为空")
    private Long poolId;

    @ApiModelProperty(value = "下拨类型：FIXED_AMOUNT-固定金额，PERCENTAGE-按比例，BALANCE_BASED-余额触发，DEMAND_BASED-需求触发")
    @TableField("DISTRIBUTIONTYPE")
    @NotBlank(message = "下拨类型不能为空")
    private String distributionType;

    @ApiModelProperty(value = "触发类型：SCHEDULED-定时，THRESHOLD-阈值，REQUEST-申请，MANUAL-手动")
    @TableField("TRIGGERTYPE")
    @NotBlank(message = "触发类型不能为空")
    private String triggerType;

    @ApiModelProperty(value = "定时时间")
    @TableField("SCHEDULETIME")
    private String scheduleTime;

    @ApiModelProperty(value = "阈值金额")
    @TableField("THRESHOLDAMOUNT")
    private BigDecimal thresholdAmount;

    @ApiModelProperty(value = "下拨比例")
    @TableField("DISTRIBUTIONRATIO")
    private BigDecimal distributionRatio;

    @ApiModelProperty(value = "固定金额")
    @TableField("FIXEDAMOUNT")
    private BigDecimal fixedAmount;

    @ApiModelProperty(value = "执行顺序")
    @TableField("EXECUTIONORDER")
    private Integer executionOrder;

    @ApiModelProperty(value = "是否启用：1-启用，0-禁用")
    @TableField("ISENABLED")
    private Boolean isEnabled;

    @ApiModelProperty(value = "生效日期")
    @TableField("EFFECTIVEDATE")
    private LocalDate effectiveDate;

    @ApiModelProperty(value = "失效日期")
    @TableField("EXPIREDATE")
    private LocalDate expireDate;

    @ApiModelProperty(value = "规则条件")
    @TableField("RULECONDITIONS")
    private String ruleConditions;

    @ApiModelProperty(value = "规则动作")
    @TableField("RULEACTIONS")
    private String ruleActions;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;

    @ApiModelProperty(value = "组织ID")
    @TableField("ORGID")
    @NotNull(message = "组织ID不能为空")
    private Long orgId;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATETIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "UPDATETIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建用户")
    @TableField(value = "CREATEUSER", fill = FieldFill.INSERT)
    private Long createUser;

    @ApiModelProperty(value = "更新用户")
    @TableField(value = "UPDATEUSER", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    // ==================== 扩展字段 ====================

    @ApiModelProperty(value = "资金池名称")
    @TableField(exist = false)
    private String poolName;

    @ApiModelProperty(value = "下拨类型名称")
    @TableField(exist = false)
    private String distributionTypeName;

    @ApiModelProperty(value = "触发类型名称")
    @TableField(exist = false)
    private String triggerTypeName;

    @ApiModelProperty(value = "状态名称")
    @TableField(exist = false)
    private String statusName;

    @ApiModelProperty(value = "最后执行时间")
    @TableField(exist = false)
    private LocalDateTime lastExecutionTime;

    @ApiModelProperty(value = "执行次数")
    @TableField(exist = false)
    private Integer executionCount;

    @ApiModelProperty(value = "成功次数")
    @TableField(exist = false)
    private Integer successCount;

    @ApiModelProperty(value = "失败次数")
    @TableField(exist = false)
    private Integer failureCount;

    @ApiModelProperty(value = "成功率")
    @TableField(exist = false)
    private BigDecimal successRate;

    @ApiModelProperty(value = "累计下拨金额")
    @TableField(exist = false)
    private BigDecimal totalDistributionAmount;

    // ==================== 业务方法 ====================

    /**
     * 获取下拨类型显示名称
     */
    public String getDistributionTypeDisplay() {
        if (distributionType == null) return "";
        switch (distributionType) {
            case "FIXED_AMOUNT": return "固定金额";
            case "PERCENTAGE": return "按比例";
            case "BALANCE_BASED": return "余额触发";
            case "DEMAND_BASED": return "需求触发";
            default: return distributionType;
        }
    }

    /**
     * 获取触发类型显示名称
     */
    public String getTriggerTypeDisplay() {
        if (triggerType == null) return "";
        switch (triggerType) {
            case "SCHEDULED": return "定时触发";
            case "THRESHOLD": return "阈值触发";
            case "REQUEST": return "申请触发";
            case "MANUAL": return "手动触发";
            default: return triggerType;
        }
    }

    /**
     * 获取状态显示名称
     */
    public String getStatusDisplay() {
        return Boolean.TRUE.equals(isEnabled) ? "启用" : "禁用";
    }

    /**
     * 检查规则是否有效
     */
    public boolean isEffective() {
        if (!Boolean.TRUE.equals(isEnabled)) {
            return false;
        }
        
        LocalDate now = LocalDate.now();
        
        if (effectiveDate != null && now.isBefore(effectiveDate)) {
            return false;
        }
        
        if (expireDate != null && now.isAfter(expireDate)) {
            return false;
        }
        
        return true;
    }

    /**
     * 检查是否为定时规则
     */
    public boolean isScheduledRule() {
        return "SCHEDULED".equals(triggerType);
    }

    /**
     * 检查是否为阈值规则
     */
    public boolean isThresholdRule() {
        return "THRESHOLD".equals(triggerType);
    }

    /**
     * 检查是否为申请规则
     */
    public boolean isRequestRule() {
        return "REQUEST".equals(triggerType);
    }

    /**
     * 检查是否为手动规则
     */
    public boolean isManualRule() {
        return "MANUAL".equals(triggerType);
    }

    /**
     * 获取规则描述
     */
    public String getRuleDescription() {
        StringBuilder desc = new StringBuilder();
        desc.append(getDistributionTypeDisplay());
        
        if ("FIXED_AMOUNT".equals(distributionType) && fixedAmount != null) {
            desc.append("(").append(fixedAmount).append("元)");
        } else if ("PERCENTAGE".equals(distributionType) && distributionRatio != null) {
            desc.append("(").append(distributionRatio).append("%)");
        } else if ("BALANCE_BASED".equals(distributionType) && thresholdAmount != null) {
            desc.append("(阈值:").append(thresholdAmount).append("元)");
        }
        
        desc.append(" - ").append(getTriggerTypeDisplay());
        
        if ("SCHEDULED".equals(triggerType) && scheduleTime != null) {
            desc.append("(").append(scheduleTime).append(")");
        }
        
        return desc.toString();
    }
}
