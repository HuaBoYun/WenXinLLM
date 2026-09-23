package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.math.BigDecimal;
import java.util.Date;

/**
 * 印章组合配置实体类
 *
 * @author system
 * @date 2024-12-09
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_SEAL_COMBINATION_CONFIG")
public class TblSealCombinationConfig {
    @TableId(type = IdType.AUTO)
    private Long combinationId;

    /**
     * 组合编码
     */
    private String combinationCode;

    /**
     * 组合名称
     */
    private String combinationName;

    /**
     * 组合类型
     */
    private String combinationType;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 权限级别
     */
    private String authorityLevel;

    /**
     * 金额限制
     */
    private BigDecimal maxAmountLimit;

    /**
     * 最小金额限制
     */
    private BigDecimal minAmountLimit;

    /**
     * 货币代码
     */
    private String currencyCode;

    /**
     * 所需印章ID列表，逗号分隔
     */
    private String requiredSeals;

    /**
     * 所需印章数量
     */
    private Integer requiredCount;

    /**
     * 是否需要审批(1-是,0-否)
     */
    private Integer approvalRequired;

    /**
     * 审批级别
     */
    private Integer approvalLevel;

    /**
     * 审批流程配置
     */
    private String approvalFlow;

    /**
     * 时间限制配置
     */
    private String timeRestriction;

    /**
     * 地理限制配置
     */
    private String geographicRestriction;

    /**
     * 使用条件
     */
    private String usageConditions;

    /**
     * 风险级别(1-低,2-中,3-高)
     */
    private Integer riskLevel;

    /**
     * 是否启用(1-启用,0-禁用)
     */
    private Integer isEnabled;

    /**
     * 优先级
     */
    private Integer priorityLevel;

    /**
     * 每日使用限制
     */
    private Integer dailyUsageLimit;

    /**
     * 每月使用限制
     */
    private Integer monthlyUsageLimit;

    /**
     * 已使用次数
     */
    private Integer usageCount;

    /**
     * 最后使用时间
     */
    private Date lastUsageTime;

    /**
     * 有效开始日期
     */
    private Date validFrom;

    /**
     * 有效结束日期
     */
    private Date validTo;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 更新人
     */
    private Long updateUser;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getCombinationId() { return combinationId; }
    public void setCombinationId(Long combinationId) { this.combinationId = combinationId; }
    public String getCombinationCode() { return combinationCode; }
    public void setCombinationCode(String combinationCode) { this.combinationCode = combinationCode; }
    public String getCombinationName() { return combinationName; }
    public void setCombinationName(String combinationName) { this.combinationName = combinationName; }
    public String getCombinationType() { return combinationType; }
    public void setCombinationType(String combinationType) { this.combinationType = combinationType; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getAuthorityLevel() { return authorityLevel; }
    public void setAuthorityLevel(String authorityLevel) { this.authorityLevel = authorityLevel; }
    public BigDecimal getMaxAmountLimit() { return maxAmountLimit; }
    public void setMaxAmountLimit(BigDecimal maxAmountLimit) { this.maxAmountLimit = maxAmountLimit; }
    public BigDecimal getMinAmountLimit() { return minAmountLimit; }
    public void setMinAmountLimit(BigDecimal minAmountLimit) { this.minAmountLimit = minAmountLimit; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getRequiredSeals() { return requiredSeals; }
    public void setRequiredSeals(String requiredSeals) { this.requiredSeals = requiredSeals; }
    public Integer getRequiredCount() { return requiredCount; }
    public void setRequiredCount(Integer requiredCount) { this.requiredCount = requiredCount; }
    public Integer getApprovalRequired() { return approvalRequired; }
    public void setApprovalRequired(Integer approvalRequired) { this.approvalRequired = approvalRequired; }
    public Integer getApprovalLevel() { return approvalLevel; }
    public void setApprovalLevel(Integer approvalLevel) { this.approvalLevel = approvalLevel; }
    public String getApprovalFlow() { return approvalFlow; }
    public void setApprovalFlow(String approvalFlow) { this.approvalFlow = approvalFlow; }
    public String getTimeRestriction() { return timeRestriction; }
    public void setTimeRestriction(String timeRestriction) { this.timeRestriction = timeRestriction; }
    public String getGeographicRestriction() { return geographicRestriction; }
    public void setGeographicRestriction(String geographicRestriction) { this.geographicRestriction = geographicRestriction; }
    public String getUsageConditions() { return usageConditions; }
    public void setUsageConditions(String usageConditions) { this.usageConditions = usageConditions; }
    public Integer getRiskLevel() { return riskLevel; }
    public void setRiskLevel(Integer riskLevel) { this.riskLevel = riskLevel; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Integer getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(Integer priorityLevel) { this.priorityLevel = priorityLevel; }
    public Integer getDailyUsageLimit() { return dailyUsageLimit; }
    public void setDailyUsageLimit(Integer dailyUsageLimit) { this.dailyUsageLimit = dailyUsageLimit; }
    public Integer getMonthlyUsageLimit() { return monthlyUsageLimit; }
    public void setMonthlyUsageLimit(Integer monthlyUsageLimit) { this.monthlyUsageLimit = monthlyUsageLimit; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
    public Date getLastUsageTime() { return lastUsageTime; }
    public void setLastUsageTime(Date lastUsageTime) { this.lastUsageTime = lastUsageTime; }
    public Date getValidFrom() { return validFrom; }
    public void setValidFrom(Date validFrom) { this.validFrom = validFrom; }
    public Date getValidTo() { return validTo; }
    public void setValidTo(Date validTo) { this.validTo = validTo; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }

}
