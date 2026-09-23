package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.io.Serializable;
import java.util.Date;

/**
 * 财资公共模块 - 业务规则表
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
@ApiModel(value = "TcBusinessRule", description = "业务规则管理")
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
@TableName("TC_BUSINESS_RULE")
public class TcBusinessRule implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 规则编码
     */
    @TableField("RULE_CODE")
    @ApiModelProperty(value = "规则编码", required = true)
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField("RULE_NAME")
    @ApiModelProperty(value = "规则名称", required = true)
    private String ruleName;

    /**
     * 规则类型
     */
    @TableField("RULE_TYPE")
    @ApiModelProperty(value = "规则类型", required = true)
    private String ruleType;

    /**
     * 规则分类
     */
    @TableField("RULE_CATEGORY")
    @ApiModelProperty(value = "规则分类")
    private String ruleCategory;

    /**
     * 业务域
     */
    @TableField("BUSINESS_DOMAIN")
    @ApiModelProperty(value = "业务域")
    private String businessDomain;

    /**
     * 规则描述
     */
    @TableField("RULE_DESC")
    @ApiModelProperty(value = "规则描述")
    private String ruleDesc;

    /**
     * 规则表达式
     */
    @TableField("RULE_EXPRESSION")
    @ApiModelProperty(value = "规则表达式")
    private String ruleExpression;

    /**
     * 规则语法
     */
    @TableField("RULE_SYNTAX")
    @ApiModelProperty(value = "规则语法")
    private String ruleSyntax;

    /**
     * 执行顺序
     */
    @TableField("EXECUTION_ORDER")
    @ApiModelProperty(value = "执行顺序")
    private Integer executionOrder;

    /**
     * 是否激活：1-是，0-否
     */
    @TableField("IS_ACTIVE")
    @ApiModelProperty(value = "是否激活：1-是，0-否")
    private String isActive;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    @ApiModelProperty(value = "生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRE_DATE")
    @ApiModelProperty(value = "失效日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 状态：1-启用，0-停用
     */
    @TableField("STATUS")
    @ApiModelProperty(value = "状态：1-启用，0-停用")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    @ApiModelProperty(value = "版本号", hidden = true)
    private Integer versionNo;

    /**
     * 根据ID创建实例
     */
    public static TcBusinessRule ofId(String id) {
        TcBusinessRule businessRule = new TcBusinessRule();
        businessRule.setId(id);
        return businessRule;
    }

    /**
     * 根据规则编码创建实例
     */
    public static TcBusinessRule ofRuleCode(String ruleCode) {
        TcBusinessRule businessRule = new TcBusinessRule();
        businessRule.setRuleCode(ruleCode);
        return businessRule;
    }

    /**
     * 根据规则类型创建实例
     */
    public static TcBusinessRule ofRuleType(String ruleType) {
        TcBusinessRule businessRule = new TcBusinessRule();
        businessRule.setRuleType(ruleType);
        return businessRule;
    }

    /**
     * 根据业务域创建实例
     */
    public static TcBusinessRule ofBusinessDomain(String businessDomain) {
        TcBusinessRule businessRule = new TcBusinessRule();
        businessRule.setBusinessDomain(businessDomain);
        return businessRule;
    }

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }
    public String getRuleCategory() { return ruleCategory; }
    public void setRuleCategory(String ruleCategory) { this.ruleCategory = ruleCategory; }
    public String getBusinessDomain() { return businessDomain; }
    public void setBusinessDomain(String businessDomain) { this.businessDomain = businessDomain; }
    public String getRuleDesc() { return ruleDesc; }
    public void setRuleDesc(String ruleDesc) { this.ruleDesc = ruleDesc; }
    public String getRuleExpression() { return ruleExpression; }
    public void setRuleExpression(String ruleExpression) { this.ruleExpression = ruleExpression; }
    public String getRuleSyntax() { return ruleSyntax; }
    public void setRuleSyntax(String ruleSyntax) { this.ruleSyntax = ruleSyntax; }
    public Integer getExecutionOrder() { return executionOrder; }
    public void setExecutionOrder(Integer executionOrder) { this.executionOrder = executionOrder; }
    public String getIsActive() { return isActive; }
    public void setIsActive(String isActive) { this.isActive = isActive; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getExpireDate() { return expireDate; }
    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }

}
