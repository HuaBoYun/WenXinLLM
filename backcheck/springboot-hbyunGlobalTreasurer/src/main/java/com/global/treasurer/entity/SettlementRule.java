package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 结算规则实体类
 * 
 * @author Global Treasurer System
 * @since 2025-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SETTLEMENT_RULES")
public class SettlementRule implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId(value = "RULEID", type = IdType.AUTO)
    private Long ruleId;

    /**
     * 规则代码
     */
    @TableField("RULECODE")
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField("RULENAME")
    private String ruleName;

    /**
     * 规则类型
     */
    @TableField("RULETYPE")
    private String ruleType;

    /**
     * 业务类型
     */
    @TableField("BUSINESSTYPE")
    private String businessType;

    /**
     * 账户ID
     */
    @TableField("ACCOUNTID")
    private Long accountId;

    /**
     * 币种代码
     */
    @TableField("CURRENCYCODE")
    private String currencyCode;

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
     * 优先级
     */
    @TableField("PRIORITY")
    private Integer priority;

    /**
     * 是否启用
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

    // 业务方法

    /**
     * 检查规则是否启用
     */
    public boolean isEnabled() {
        return this.isEnabled != null && this.isEnabled == 1;
    }

    /**
     * 启用规则
     */
    public void enable() {
        this.isEnabled = 1;
    }

    /**
     * 禁用规则
     */
    public void disable() {
        this.isEnabled = 0;
    }

    /**
     * 检查规则是否在有效期内
     */
    public boolean isEffective() {
        LocalDate now = LocalDate.now();
        
        // 检查生效日期
        if (this.effectiveDate != null && now.isBefore(this.effectiveDate)) {
            return false;
        }
        
        // 检查失效日期
        if (this.expireDate != null && now.isAfter(this.expireDate)) {
            return false;
        }
        
        return true;
    }

    /**
     * 检查规则是否可用（启用且在有效期内）
     */
    public boolean isAvailable() {
        return isEnabled() && isEffective();
    }

    /**
     * 检查是否为时间规则
     */
    public boolean isTimingRule() {
        return "TIMING".equals(this.ruleType);
    }

    /**
     * 检查是否为金额规则
     */
    public boolean isAmountRule() {
        return "AMOUNT".equals(this.ruleType);
    }

    /**
     * 检查是否为优先级规则
     */
    public boolean isPriorityRule() {
        return "PRIORITY".equals(this.ruleType);
    }

    /**
     * 检查是否为路由规则
     */
    public boolean isRoutingRule() {
        return "ROUTING".equals(this.ruleType);
    }

    /**
     * 检查是否即将失效（7天内）
     */
    public boolean isExpiringSoon() {
        if (this.expireDate == null) return false;
        LocalDate sevenDaysLater = LocalDate.now().plusDays(7);
        return this.expireDate.isBefore(sevenDaysLater);
    }

    /**
     * 检查是否已失效
     */
    public boolean isExpired() {
        if (this.expireDate == null) return false;
        return LocalDate.now().isAfter(this.expireDate);
    }

    /**
     * 获取规则类型描述
     */
    public String getRuleTypeDescription() {
        if (this.ruleType == null) return "";
        switch (this.ruleType) {
            case "TIMING": return "时间规则";
            case "AMOUNT": return "金额规则";
            case "PRIORITY": return "优先级规则";
            case "ROUTING": return "路由规则";
            default: return this.ruleType;
        }
    }

    /**
     * 获取业务类型描述
     */
    public String getBusinessTypeDescription() {
        if (this.businessType == null) return "全部";
        switch (this.businessType) {
            case "PAYMENT": return "付款";
            case "RECEIPT": return "收款";
            case "TRANSFER": return "转账";
            case "FOREX": return "外汇";
            case "INVESTMENT": return "投资";
            default: return this.businessType;
        }
    }

    /**
     * 获取优先级描述
     */
    public String getPriorityDescription() {
        if (this.priority == null) return "普通";
        if (this.priority <= 50) return "高";
        if (this.priority <= 100) return "普通";
        return "低";
    }

    /**
     * 获取状态描述
     */
    public String getStatusDescription() {
        if (!isEnabled()) return "已禁用";
        if (isExpired()) return "已失效";
        if (isExpiringSoon()) return "即将失效";
        if (isEffective()) return "生效中";
        return "未生效";
    }

    /**
     * 计算剩余有效天数
     */
    public long getRemainingEffectiveDays() {
        if (this.expireDate == null) return Long.MAX_VALUE;
        LocalDate now = LocalDate.now();
        if (now.isAfter(this.expireDate)) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(now, this.expireDate);
    }

    /**
     * 检查规则配置是否完整
     */
    public boolean isConfigComplete() {
        return this.ruleCode != null && !this.ruleCode.trim().isEmpty() &&
               this.ruleName != null && !this.ruleName.trim().isEmpty() &&
               this.ruleType != null && !this.ruleType.trim().isEmpty() &&
               this.ruleConditions != null && !this.ruleConditions.trim().isEmpty() &&
               this.ruleActions != null && !this.ruleActions.trim().isEmpty();
    }

    /**
     * 检查是否为高优先级规则
     */
    public boolean isHighPriority() {
        return this.priority != null && this.priority <= 50;
    }

    /**
     * 检查是否为低优先级规则
     */
    public boolean isLowPriority() {
        return this.priority != null && this.priority > 100;
    }

    /**
     * 延长有效期
     */
    public void extendExpireDate(int days) {
        if (this.expireDate == null) {
            this.expireDate = LocalDate.now().plusDays(days);
        } else {
            this.expireDate = this.expireDate.plusDays(days);
        }
    }

    /**
     * 设置永久有效
     */
    public void setPermanentEffective() {
        this.expireDate = null;
    }

    /**
     * 生成规则摘要
     */
    public String generateRuleSummary() {
        return String.format("%s - %s - %s - 优先级:%d - %s",
                this.ruleCode,
                this.ruleName,
                getRuleTypeDescription(),
                this.priority != null ? this.priority : 100,
                getStatusDescription());
    }

    /**
     * 检查规则是否适用于指定业务类型
     */
    public boolean isApplicableToBusinessType(String businessType) {
        // 如果规则没有指定业务类型，则适用于所有业务类型
        if (this.businessType == null || this.businessType.trim().isEmpty()) {
            return true;
        }
        return this.businessType.equals(businessType);
    }

    /**
     * 检查规则是否适用于指定账户
     */
    public boolean isApplicableToAccount(Long accountId) {
        // 如果规则没有指定账户，则适用于所有账户
        if (this.accountId == null) {
            return true;
        }
        return this.accountId.equals(accountId);
    }

    /**
     * 检查规则是否适用于指定币种
     */
    public boolean isApplicableToCurrency(String currencyCode) {
        // 如果规则没有指定币种，则适用于所有币种
        if (this.currencyCode == null || this.currencyCode.trim().isEmpty()) {
            return true;
        }
        return this.currencyCode.equals(currencyCode);
    }

    /**
     * 检查规则是否适用于指定条件
     */
    public boolean isApplicableTo(String businessType, Long accountId, String currencyCode) {
        return isApplicableToBusinessType(businessType) &&
               isApplicableToAccount(accountId) &&
               isApplicableToCurrency(currencyCode);
    }
}
