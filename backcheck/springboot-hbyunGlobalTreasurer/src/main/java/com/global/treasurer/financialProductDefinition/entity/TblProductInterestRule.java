package com.global.treasurer.financialProductDefinition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品利息规则实体类
 * 对应数据库表：TBL_PRODUCT_INTEREST_RULE
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_PRODUCT_INTEREST_RULE")
public class TblProductInterestRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "RULE_ID", type = IdType.INPUT)
    private Long ruleId;

    @TableField("RULE_CODE")
    private String ruleCode;

    @TableField("RULE_NAME")
    private String ruleName;

    @TableField("INTEREST_CALCULATION_METHOD")
    private String interestCalculationMethod;

    @TableField("INTEREST_RATE_TYPE")
    private String interestRateType;

    @TableField("INTEREST_RATE")
    private BigDecimal interestRate;

    @TableField("INTEREST_FREQUENCY")
    private String interestFrequency;

    @TableField("COMPOUNDING_METHOD")
    private String compoundingMethod;

    @TableField("DAY_COUNT_CONVENTION")
    private String dayCountConvention;

    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("ORG_ID")
    private Long orgId;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getInterestCalculationMethod() { return interestCalculationMethod; }
    public void setInterestCalculationMethod(String interestCalculationMethod) { this.interestCalculationMethod = interestCalculationMethod; }
    public String getInterestRateType() { return interestRateType; }
    public void setInterestRateType(String interestRateType) { this.interestRateType = interestRateType; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public String getInterestFrequency() { return interestFrequency; }
    public void setInterestFrequency(String interestFrequency) { this.interestFrequency = interestFrequency; }
    public String getCompoundingMethod() { return compoundingMethod; }
    public void setCompoundingMethod(String compoundingMethod) { this.compoundingMethod = compoundingMethod; }
    public String getDayCountConvention() { return dayCountConvention; }
    public void setDayCountConvention(String dayCountConvention) { this.dayCountConvention = dayCountConvention; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

}
