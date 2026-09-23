package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 财资公共模块 - 业务活动规则管理实体类
 * 
 * @author HuaBo Cloud
 * @since 2025-09-22
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("TBL_TREASURY_BUSINESS_RULES")
@ApiModel(value = "TreasuryBusinessRule", description = "业务活动规则管理")
public class TreasuryBusinessRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务规则ID")
    @TableId(value = "RULEID", type = IdType.AUTO)
    private Long ruleId;

    @ApiModelProperty(value = "规则编码")
    @NotBlank(message = "规则编码不能为空")
    private String ruleCode;

    @ApiModelProperty(value = "规则名称")
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    @ApiModelProperty(value = "规则类型：APPROVAL-审批规则，LIMIT-限额规则，RISK-风险规则，WORKFLOW-流程规则")
    @NotBlank(message = "规则类型不能为空")
    private String ruleType;

    @ApiModelProperty(value = "业务模块：ACCOUNT-账户管理，CASH-现金管理，FUND-资金计划，FINANCE-融资管理")
    private String businessModule;

    @ApiModelProperty(value = "业务品种ID")
    private Long businessDefinitionId;

    @ApiModelProperty(value = "业务品种名称")
    private String businessDefinitionName;

    @ApiModelProperty(value = "规则条件（JSON格式）")
    private String ruleConditions;

    @ApiModelProperty(value = "规则动作（JSON格式）")
    private String ruleActions;

    @ApiModelProperty(value = "规则优先级")
    private Integer priority;

    @ApiModelProperty(value = "规则版本")
    private String ruleVersion;

    @ApiModelProperty(value = "是否启用：1-启用，0-禁用")
    @NotNull(message = "是否启用不能为空")
    private Integer isEnabled;

    @ApiModelProperty(value = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectiveTime;

    @ApiModelProperty(value = "失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "规则脚本")
    private String ruleScript;

    @ApiModelProperty(value = "规则引擎类型：DROOLS-Drools，GROOVY-Groovy，JAVASCRIPT-JavaScript")
    private String ruleEngine;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "组织ID")
    @NotNull(message = "组织ID不能为空")
    private Long orgId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建用户")
    private Long createUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新用户")
    private Long updateUser;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }
    public String getBusinessModule() { return businessModule; }
    public void setBusinessModule(String businessModule) { this.businessModule = businessModule; }
    public Long getBusinessDefinitionId() { return businessDefinitionId; }
    public void setBusinessDefinitionId(Long businessDefinitionId) { this.businessDefinitionId = businessDefinitionId; }
    public String getBusinessDefinitionName() { return businessDefinitionName; }
    public void setBusinessDefinitionName(String businessDefinitionName) { this.businessDefinitionName = businessDefinitionName; }
    public String getRuleConditions() { return ruleConditions; }
    public void setRuleConditions(String ruleConditions) { this.ruleConditions = ruleConditions; }
    public String getRuleActions() { return ruleActions; }
    public void setRuleActions(String ruleActions) { this.ruleActions = ruleActions; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getRuleVersion() { return ruleVersion; }
    public void setRuleVersion(String ruleVersion) { this.ruleVersion = ruleVersion; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public LocalDateTime getEffectiveTime() { return effectiveTime; }
    public void setEffectiveTime(LocalDateTime effectiveTime) { this.effectiveTime = effectiveTime; }
    public LocalDateTime getExpireTime() { return expireTime; }
    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }
    public String getRuleScript() { return ruleScript; }
    public void setRuleScript(String ruleScript) { this.ruleScript = ruleScript; }
    public String getRuleEngine() { return ruleEngine; }
    public void setRuleEngine(String ruleEngine) { this.ruleEngine = ruleEngine; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }

}
