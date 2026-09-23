package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 产品期限规则表
 * @author hbyun-admin
 * @date 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
@TableName("TC_PRODUCT_TERM_RULE")
public class TcProductTermRule implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    private String id;

    /**
     * 产品ID
     */
    @TableField("PRODUCT_ID")
    private String productId;

    /**
     * 期限类型
     */
    @TableField("TERM_TYPE")
    private String termType;

    /**
     * 最小期限
     */
    @TableField("MIN_TERM")
    private Integer minTerm;

    /**
     * 最大期限
     */
    @TableField("MAX_TERM")
    private Integer maxTerm;

    /**
     * 期限单位
     */
    @TableField("TERM_UNIT")
    private String termUnit;

    /**
     * 计算方式
     */
    @TableField("CALCULATION_METHOD")
    private String calculationMethod;

    /**
     * 允许提前终止
     */
    @TableField("EARLY_TERMINATION_ALLOWED")
    private String earlyTerminationAllowed;

    /**
     * 提前终止规则
     */
    @TableField("EARLY_TERMINATION_RULES")
    private String earlyTerminationRules;

    /**
     * 允许展期
     */
    @TableField("EXTENSION_ALLOWED")
    private String extensionAllowed;

    /**
     * 展期规则
     */
    @TableField("EXTENSION_RULES")
    private String extensionRules;

    /**
     * 允许自动续期
     */
    @TableField("AUTO_RENEWAL_ALLOWED")
    private String autoRenewalAllowed;

    /**
     * 续期规则
     */
    @TableField("RENEWAL_RULES")
    private String renewalRules;

    /**
     * 状态(1:启用 0:停用)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建用户
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 更新用户
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private Long versionNo;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getTermType() { return termType; }
    public void setTermType(String termType) { this.termType = termType; }
    public Integer getMinTerm() { return minTerm; }
    public void setMinTerm(Integer minTerm) { this.minTerm = minTerm; }
    public Integer getMaxTerm() { return maxTerm; }
    public void setMaxTerm(Integer maxTerm) { this.maxTerm = maxTerm; }
    public String getTermUnit() { return termUnit; }
    public void setTermUnit(String termUnit) { this.termUnit = termUnit; }
    public String getCalculationMethod() { return calculationMethod; }
    public void setCalculationMethod(String calculationMethod) { this.calculationMethod = calculationMethod; }
    public String getEarlyTerminationAllowed() { return earlyTerminationAllowed; }
    public void setEarlyTerminationAllowed(String earlyTerminationAllowed) { this.earlyTerminationAllowed = earlyTerminationAllowed; }
    public String getEarlyTerminationRules() { return earlyTerminationRules; }
    public void setEarlyTerminationRules(String earlyTerminationRules) { this.earlyTerminationRules = earlyTerminationRules; }
    public String getExtensionAllowed() { return extensionAllowed; }
    public void setExtensionAllowed(String extensionAllowed) { this.extensionAllowed = extensionAllowed; }
    public String getExtensionRules() { return extensionRules; }
    public void setExtensionRules(String extensionRules) { this.extensionRules = extensionRules; }
    public String getAutoRenewalAllowed() { return autoRenewalAllowed; }
    public void setAutoRenewalAllowed(String autoRenewalAllowed) { this.autoRenewalAllowed = autoRenewalAllowed; }
    public String getRenewalRules() { return renewalRules; }
    public void setRenewalRules(String renewalRules) { this.renewalRules = renewalRules; }
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
    public Long getVersionNo() { return versionNo; }
    public void setVersionNo(Long versionNo) { this.versionNo = versionNo; }

}
