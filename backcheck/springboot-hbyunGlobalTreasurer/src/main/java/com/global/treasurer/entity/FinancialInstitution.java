package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 金融机构实体类
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FINANCIAL_INSTITUTION")
public class FinancialInstitution implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 机构ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long institutionId;

    /** 机构编码 */
    private String institutionCode;

    /** 机构名称 */
    private String institutionName;

    /** 机构类型 */
    private String institutionType;

    /** 法定代表人 */
    private String legalRepresentative;

    /** 注册资本 */
    private BigDecimal registeredCapital;

    /** 注册地址 */
    private String registeredAddress;

    /** 经营范围 */
    private String businessScope;

    /** 信用评级 */
    private String creditRating;

    /** 联系人 */
    private String contactPerson;

    /** 联系电话 */
    private String contactPhone;

    /** 联系邮箱 */
    private String contactEmail;

    /** 网站 */
    private String website;

    /** 状态 */
    private Integer status;

    /** 删除标志 */
    private Integer deleteFlag;

    /** 创建人 */
    private Long createdBy;

    /** 创建人姓名 */
    private String createdByName;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private Long updatedBy;

    /** 更新人姓名 */
    private String updatedByName;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getInstitutionId() { return institutionId; }
    public void setInstitutionId(Long institutionId) { this.institutionId = institutionId; }
    public String getInstitutionCode() { return institutionCode; }
    public void setInstitutionCode(String institutionCode) { this.institutionCode = institutionCode; }
    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }
    public String getInstitutionType() { return institutionType; }
    public void setInstitutionType(String institutionType) { this.institutionType = institutionType; }
    public String getLegalRepresentative() { return legalRepresentative; }
    public void setLegalRepresentative(String legalRepresentative) { this.legalRepresentative = legalRepresentative; }
    public BigDecimal getRegisteredCapital() { return registeredCapital; }
    public void setRegisteredCapital(BigDecimal registeredCapital) { this.registeredCapital = registeredCapital; }
    public String getRegisteredAddress() { return registeredAddress; }
    public void setRegisteredAddress(String registeredAddress) { this.registeredAddress = registeredAddress; }
    public String getBusinessScope() { return businessScope; }
    public void setBusinessScope(String businessScope) { this.businessScope = businessScope; }
    public String getCreditRating() { return creditRating; }
    public void setCreditRating(String creditRating) { this.creditRating = creditRating; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
