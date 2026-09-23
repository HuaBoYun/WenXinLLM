package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 合规检查规则实体类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_COMPLIANCE_RULE")
public class TblComplianceRule implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 规则ID */
    @TableId(type = IdType.ASSIGN_UUID)
    private String ruleId;

    /** 规则代码 */
    private String ruleCode;

    /** 规则名称 */
    private String ruleName;

    /** 规则类型 */
    private String ruleType;

    /** 监管机构ID */
    private String authorityId;

    /** 规则表达式 */
    private String ruleExpression;

    /** 严重级别(HIGH/MEDIUM/LOW) */
    private String severityLevel;

    /** 是否启用 */
    private Integer isEnabled;

    /** 检查频率(REAL_TIME/HOURLY/DAILY/WEEKLY/MONTHLY) */
    private String checkFrequency;

    /** 阈值 */
    private java.math.BigDecimal thresholdValue;

    /** 警告阈值 */
    private java.math.BigDecimal warningThreshold;

    /** 生效日期 */
    private Date effectiveDate;

    /** 失效日期 */
    private Date expiryDate;

    /** 描述 */
    private String description;

    /** 公司ID */
    private String companyId;

    /** 删除标志 */
    private Integer deleteFlag;

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    private Date updatedTime;

    /** 检查范围 */
    private String checkScope;

    /** 规则条件 */
    private String ruleCondition;

    /** 规则公式 */
    private String ruleFormula;

    /** 法规依据 */
    private String regulationReference;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }
    public String getAuthorityId() { return authorityId; }
    public void setAuthorityId(String authorityId) { this.authorityId = authorityId; }
    public String getRuleExpression() { return ruleExpression; }
    public void setRuleExpression(String ruleExpression) { this.ruleExpression = ruleExpression; }
    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getCheckFrequency() { return checkFrequency; }
    public void setCheckFrequency(String checkFrequency) { this.checkFrequency = checkFrequency; }
    public java.math.BigDecimal getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(java.math.BigDecimal thresholdValue) { this.thresholdValue = thresholdValue; }
    public java.math.BigDecimal getWarningThreshold() { return warningThreshold; }
    public void setWarningThreshold(java.math.BigDecimal warningThreshold) { this.warningThreshold = warningThreshold; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getCheckScope() { return checkScope; }
    public void setCheckScope(String checkScope) { this.checkScope = checkScope; }
    public String getRuleCondition() { return ruleCondition; }
    public void setRuleCondition(String ruleCondition) { this.ruleCondition = ruleCondition; }
    public String getRuleFormula() { return ruleFormula; }
    public void setRuleFormula(String ruleFormula) { this.ruleFormula = ruleFormula; }
    public String getRegulationReference() { return regulationReference; }
    public void setRegulationReference(String regulationReference) { this.regulationReference = regulationReference; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
