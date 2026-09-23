package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.*;
// import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资金归集规则实体类
 * 对应数据库表：TBL_FUND_CONCENTRATION_RULES
 * 
 * @author Global Treasurer System
 * @since 2025-09-22
 */
// @Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_FUND_CONCENTRATION_RULES")
public class FundConcentrationRule implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId(value = "RULEID", type = IdType.AUTO)
    private Long ruleId;

    /**
     * 规则编码
     */
    @TableField("RULECODE")
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField("RULENAME")
    private String ruleName;

    /**
     * 资金池ID
     */
    @TableField("POOLID")
    private Long poolId;

    /**
     * 规则类型：TIME_BASED-基于时间, BALANCE_BASED-基于余额, MIXED-混合
     */
    @TableField("RULETYPE")
    private String ruleType;

    /**
     * 触发类型：SCHEDULED-定时, THRESHOLD-阈值, MANUAL-手动
     */
    @TableField("TRIGGERTYPE")
    private String triggerType;

    /**
     * 调度时间（Cron表达式）
     */
    @TableField("SCHEDULETIME")
    private String scheduleTime;

    /**
     * 阈值金额
     */
    @TableField("THRESHOLDAMOUNT")
    private BigDecimal thresholdAmount;

    /**
     * 归集比例（百分比）
     */
    @TableField("CONCENTRATIONRATIO")
    private BigDecimal concentrationRatio;

    /**
     * 保留金额
     */
    @TableField("RESERVEAMOUNT")
    private BigDecimal reserveAmount;

    /**
     * 执行顺序
     */
    @TableField("EXECUTIONORDER")
    private Integer executionOrder;

    /**
     * 是否启用：1-启用, 0-禁用
     */
    @TableField("ISENABLED")
    private Integer isEnabled;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVEDATE")
    private LocalDate effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIREDATE")
    private LocalDate expireDate;

    /**
     * 规则条件（JSON格式）
     */
    @TableField("RULECONDITIONS")
    private String ruleConditions;

    /**
     * 规则动作（JSON格式）
     */
    @TableField("RULEACTIONS")
    private String ruleActions;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 组织ID
     */
    @TableField("ORGID")
    private Long orgId;

    /**
     * 创建时间
     */
    @TableField(value = "CREATETIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建用户
     */
    @TableField(value = "CREATEUSER", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATETIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 更新用户
     */
    @TableField(value = "UPDATEUSER", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    // ==================== 业务方法 ====================

    /**
     * 检查规则是否启用
     */
    public boolean isEnabled() {
        return Integer.valueOf(1).equals(this.isEnabled);
    }

    /**
     * 检查规则是否禁用
     */
    public boolean isDisabled() {
        return !isEnabled();
    }

    /**
     * 检查规则是否在有效期内
     */
    public boolean isEffective() {
        return isEffective(LocalDate.now());
    }

    /**
     * 检查规则在指定日期是否有效
     */
    public boolean isEffective(LocalDate date) {
        if (effectiveDate != null && date.isBefore(effectiveDate)) {
            return false;
        }
        if (expireDate != null && date.isAfter(expireDate)) {
            return false;
        }
        return true;
    }

    /**
     * 检查规则是否已过期
     */
    public boolean isExpired() {
        return expireDate != null && LocalDate.now().isAfter(expireDate);
    }

    /**
     * 检查规则是否尚未生效
     */
    public boolean isNotYetEffective() {
        return effectiveDate != null && LocalDate.now().isBefore(effectiveDate);
    }

    /**
     * 检查规则是否可以执行
     */
    public boolean canExecute() {
        return isEnabled() && isEffective() && !isExpired();
    }

    /**
     * 检查是否为基于时间的规则
     */
    public boolean isTimeBased() {
        return "TIME_BASED".equals(this.ruleType);
    }

    /**
     * 检查是否为基于余额的规则
     */
    public boolean isBalanceBased() {
        return "BALANCE_BASED".equals(this.ruleType);
    }

    /**
     * 检查是否为混合规则
     */
    public boolean isMixed() {
        return "MIXED".equals(this.ruleType);
    }

    /**
     * 检查是否为定时触发
     */
    public boolean isScheduledTrigger() {
        return "SCHEDULED".equals(this.triggerType);
    }

    /**
     * 检查是否为阈值触发
     */
    public boolean isThresholdTrigger() {
        return "THRESHOLD".equals(this.triggerType);
    }

    /**
     * 检查是否为手动触发
     */
    public boolean isManualTrigger() {
        return "MANUAL".equals(this.triggerType);
    }

    /**
     * 检查余额是否达到触发阈值
     */
    public boolean isThresholdReached(BigDecimal currentBalance) {
        if (!isThresholdTrigger() || thresholdAmount == null || currentBalance == null) {
            return false;
        }
        return currentBalance.compareTo(thresholdAmount) >= 0;
    }

    /**
     * 计算归集金额
     */
    public BigDecimal calculateConcentrationAmount(BigDecimal availableBalance) {
        if (availableBalance == null || availableBalance.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal reserveAmount = this.reserveAmount != null ? this.reserveAmount : BigDecimal.ZERO;
        BigDecimal concentrableBalance = availableBalance.subtract(reserveAmount);
        
        if (concentrableBalance.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        if (concentrationRatio == null) {
            return concentrableBalance;
        }

        return concentrableBalance.multiply(concentrationRatio.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * 获取规则优先级（执行顺序越小优先级越高）
     */
    public int getPriority() {
        return executionOrder != null ? executionOrder : 999;
    }

    /**
     * 检查规则配置是否有效
     */
    public boolean isValidConfiguration() {
        // 检查必要字段
        if (ruleCode == null || ruleName == null || poolId == null || 
            ruleType == null || triggerType == null) {
            return false;
        }

        // 检查定时触发规则必须有调度时间
        if (isScheduledTrigger() && (scheduleTime == null || scheduleTime.trim().isEmpty())) {
            return false;
        }

        // 检查阈值触发规则必须有阈值金额
        if (isThresholdTrigger() && thresholdAmount == null) {
            return false;
        }

        // 检查归集比例范围
        if (concentrationRatio != null && 
            (concentrationRatio.compareTo(BigDecimal.ZERO) < 0 || 
             concentrationRatio.compareTo(new BigDecimal("100")) > 0)) {
            return false;
        }

        // 检查日期逻辑
        if (effectiveDate != null && expireDate != null && 
            effectiveDate.isAfter(expireDate)) {
            return false;
        }

        return true;
    }

    /**
     * 获取规则状态描述
     */
    public String getStatusDescription() {
        if (isDisabled()) {
            return "已禁用";
        } else if (isExpired()) {
            return "已过期";
        } else if (isNotYetEffective()) {
            return "未生效";
        } else if (canExecute()) {
            return "正常";
        } else {
            return "异常";
        }
    }

    /**
     * 获取下次执行时间（仅适用于定时规则）
     */
    public LocalDateTime getNextExecutionTime() {
        if (!isScheduledTrigger() || scheduleTime == null) {
            return null;
        }
        
        // 这里需要根据Cron表达式计算下次执行时间
        // 实际实现中可以使用Quartz的CronExpression类
        // 此处仅作示例
        return LocalDateTime.now().plusHours(1);
    }

    /**
     * 克隆规则（用于创建相似规则）
     */
    public FundConcentrationRule cloneRule() {
        FundConcentrationRule rule = new FundConcentrationRule();
        rule.setRuleName(this.ruleName + "_副本");
        rule.setPoolId(this.poolId);
        rule.setRuleType(this.ruleType);
        rule.setTriggerType(this.triggerType);
        rule.setScheduleTime(this.scheduleTime);
        rule.setThresholdAmount(this.thresholdAmount);
        rule.setConcentrationRatio(this.concentrationRatio);
        rule.setReserveAmount(this.reserveAmount);
        rule.setExecutionOrder(this.executionOrder);
        rule.setIsEnabled(0); // 默认禁用
        rule.setEffectiveDate(LocalDate.now());
        rule.setRuleConditions(this.ruleConditions);
        rule.setRuleActions(this.ruleActions);
        rule.setRemark(this.remark);
        rule.setOrgId(this.orgId);
        return rule;
    }

    // ==================== 常量定义 ====================

    /**
     * 规则类型常量
     */
    public static class RuleType {
        public static final String TIME_BASED = "TIME_BASED";
        public static final String BALANCE_BASED = "BALANCE_BASED";
        public static final String MIXED = "MIXED";
    }

    /**
     * 触发类型常量
     */
    public static class TriggerType {
        public static final String SCHEDULED = "SCHEDULED";
        public static final String THRESHOLD = "THRESHOLD";
        public static final String MANUAL = "MANUAL";
    }

    /**
     * 启用状态常量
     */
    public static class EnabledStatus {
        public static final Integer ENABLED = 1;
        public static final Integer DISABLED = 0;
    }
    // 完整的getter和setter方法
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public Long getPoolId() { return poolId; }
    public void setPoolId(Long poolId) { this.poolId = poolId; }
    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }
    public String getTriggerType() { return triggerType; }
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }
    public String getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(String scheduleTime) { this.scheduleTime = scheduleTime; }
    public BigDecimal getThresholdAmount() { return thresholdAmount; }
    public void setThresholdAmount(BigDecimal thresholdAmount) { this.thresholdAmount = thresholdAmount; }
    public BigDecimal getConcentrationRatio() { return concentrationRatio; }
    public void setConcentrationRatio(BigDecimal concentrationRatio) { this.concentrationRatio = concentrationRatio; }
    public BigDecimal getReserveAmount() { return reserveAmount; }
    public void setReserveAmount(BigDecimal reserveAmount) { this.reserveAmount = reserveAmount; }
    public Integer getExecutionOrder() { return executionOrder; }
    public void setExecutionOrder(Integer executionOrder) { this.executionOrder = executionOrder; }
    public int getIsEnabled() { return isEnabled; }
    public void setIsEnabled(int isEnabled) { this.isEnabled = isEnabled; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public String getRuleConditions() { return ruleConditions; }
    public void setRuleConditions(String ruleConditions) { this.ruleConditions = ruleConditions; }
    public String getRuleActions() { return ruleActions; }
    public void setRuleActions(String ruleActions) { this.ruleActions = ruleActions; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
}
