package com.global.treasurer.financialProductDefinition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 产品期限规则实体类
 * 对应数据库表：TBL_PRODUCT_TERM_RULE
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_PRODUCT_TERM_RULE")
public class TblProductTermRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "RULE_ID", type = IdType.ASSIGN_ID)
    private Long ruleId;

    @TableField("RULE_CODE")
    private String ruleCode;

    @TableField("RULE_NAME")
    private String ruleName;

    /**
     * 产品类型：BANK_WEALTH-银行理财, BOND-债券投资, EQUITY-股票投资, FUND-基金投资, DEPOSIT-存款产品
     */
    @TableField("PRODUCT_TYPE")
    private String productType;

    /**
     * 期限类型：FIXED-固定期限, OPEN_ENDED-开放式, PERIODIC_OPEN-定期开放, ROLLING-滚动期限
     */
    @TableField("TERM_TYPE")
    private String termType;

    @TableField("MIN_TERM")
    private Integer minTerm;

    @TableField("MAX_TERM")
    private Integer maxTerm;

    @TableField("TERM_UNIT")
    private String termUnit;

    /**
     * 锁定期(天)
     */
    @TableField("LOCK_PERIOD")
    private Integer lockPeriod;

    @TableField("EXTENSION_ALLOWED")
    private Integer extensionAllowed;

    @TableField("MAX_EXTENSION_TIMES")
    private Integer maxExtensionTimes;

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

    // 以下字段为前端兼容保留，不映射到数据库
    @TableField(exist = false)
    private Long id; // 映射到 ruleId，保持前端兼容

    @TableField(exist = false)
    private Long productId; // 前端可能需要，但数据库中没有此字段

    @TableField(exist = false)
    private Integer earlyRedemptionFeeRate; // 前端需要，提前赎回费率


    // 以下方法由Lombok生成,手动添加以解决编译问题

    // 主键ID - 数据库字段为RULE_ID
    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }

    // id - 映射到ruleId，保持前端兼容
    public Long getId() { return ruleId; }
    public void setId(Long id) { this.ruleId = id; }

    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getTermType() { return termType; }
    public void setTermType(String termType) { this.termType = termType; }

    public Integer getMinTerm() { return minTerm; }
    public void setMinTerm(Integer minTerm) { this.minTerm = minTerm; }

    public Integer getMaxTerm() { return maxTerm; }
    public void setMaxTerm(Integer maxTerm) { this.maxTerm = maxTerm; }

    public String getTermUnit() { return termUnit; }
    public void setTermUnit(String termUnit) { this.termUnit = termUnit; }

    public Integer getLockPeriod() { return lockPeriod != null ? lockPeriod : 0; }
    public void setLockPeriod(Integer lockPeriod) { this.lockPeriod = lockPeriod; }

    public Integer getExtensionAllowed() { return extensionAllowed; }
    public void setExtensionAllowed(Integer extensionAllowed) { this.extensionAllowed = extensionAllowed; }

    public Integer getMaxExtensionTimes() { return maxExtensionTimes; }
    public void setMaxExtensionTimes(Integer maxExtensionTimes) { this.maxExtensionTimes = maxExtensionTimes; }

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

    // 前端兼容字段
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getEarlyRedemptionFeeRate() { return earlyRedemptionFeeRate != null ? earlyRedemptionFeeRate : 0; }
    public void setEarlyRedemptionFeeRate(Integer earlyRedemptionFeeRate) { this.earlyRedemptionFeeRate = earlyRedemptionFeeRate; }
}
