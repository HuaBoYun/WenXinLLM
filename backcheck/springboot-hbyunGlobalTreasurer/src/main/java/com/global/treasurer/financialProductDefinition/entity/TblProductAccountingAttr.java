package com.global.treasurer.financialProductDefinition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 产品核算属性实体类
 * 对应数据库表：TBL_PRODUCT_ACCOUNTING_ATTR
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_PRODUCT_ACCOUNTING_ATTR")
public class TblProductAccountingAttr implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ATTR_ID", type = IdType.INPUT)
    private Long attrId;

    @TableField("ATTR_CODE")
    private String attrCode;

    @TableField("ATTR_NAME")
    private String attrName;

    @TableField("PRODUCT_TYPE")
    private String productType;

    @TableField("ACCOUNTING_SUBJECT_CODE")
    private String accountingSubjectCode;

    @TableField("ACCOUNTING_SUBJECT_NAME")
    private String accountingSubjectName;

    @TableField("ACCOUNTING_SUBJECT_TYPE")
    private String accountingSubjectType;

    @TableField("ACCOUNTING_METHOD")
    private String accountingMethod;

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


    public Long getAttrId() { return attrId; }
    public void setAttrId(Long attrId) { this.attrId = attrId; }
    public String getAttrCode() { return attrCode; }
    public void setAttrCode(String attrCode) { this.attrCode = attrCode; }
    public String getAttrName() { return attrName; }
    public void setAttrName(String attrName) { this.attrName = attrName; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public String getAccountingSubjectCode() { return accountingSubjectCode; }
    public void setAccountingSubjectCode(String accountingSubjectCode) { this.accountingSubjectCode = accountingSubjectCode; }
    public String getAccountingSubjectName() { return accountingSubjectName; }
    public void setAccountingSubjectName(String accountingSubjectName) { this.accountingSubjectName = accountingSubjectName; }
    public String getAccountingSubjectType() { return accountingSubjectType; }
    public void setAccountingSubjectType(String accountingSubjectType) { this.accountingSubjectType = accountingSubjectType; }
    public String getAccountingMethod() { return accountingMethod; }
    public void setAccountingMethod(String accountingMethod) { this.accountingMethod = accountingMethod; }
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
