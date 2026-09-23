package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * UKey厂商信息实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_UKEY_VENDOR")
public class TblUkeyVendor implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 厂商编码
     */
    private String vendorCode;

    /**
     * 厂商名称
     */
    private String vendorName;

    /**
     * 厂商类型
     */
    private String vendorType;

    /**
     * 厂商等级
     */
    private String vendorLevel;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 合作状态
     */
    private String cooperationStatus;

    /**
     * 评价等级
     */
    private String evaluationLevel;

    /**
     * 证书信息
     */
    private String certificateInfo;

    /**
     * 认证状态: CERTIFIED-已认证, PENDING-待认证, UNCERTIFIED-未认证
     */
    private String certificationStatus;

    /**
     * 产品型号
     */
    private String productModels;

    /**
     * 联系方式(整合电话、邮箱等)
     */
    private String contactInfo;

    /**
     * 厂商地址
     */
    private String address;

    /**
     * 厂商描述
     */
    private String description;

    /**
     * 认证方式
     */
    private String authMethods;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否启用: 0-禁用, 1-启用
     */
    private Integer isEnabled;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVendorCode() { return vendorCode; }
    public void setVendorCode(String vendorCode) { this.vendorCode = vendorCode; }
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public String getVendorType() { return vendorType; }
    public void setVendorType(String vendorType) { this.vendorType = vendorType; }
    public String getVendorLevel() { return vendorLevel; }
    public void setVendorLevel(String vendorLevel) { this.vendorLevel = vendorLevel; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getCooperationStatus() { return cooperationStatus; }
    public void setCooperationStatus(String cooperationStatus) { this.cooperationStatus = cooperationStatus; }
    public String getEvaluationLevel() { return evaluationLevel; }
    public void setEvaluationLevel(String evaluationLevel) { this.evaluationLevel = evaluationLevel; }
    public String getCertificateInfo() { return certificateInfo; }
    public void setCertificateInfo(String certificateInfo) { this.certificateInfo = certificateInfo; }
    public String getCertificationStatus() { return certificationStatus; }
    public void setCertificationStatus(String certificationStatus) { this.certificationStatus = certificationStatus; }
    public String getProductModels() { return productModels; }
    public void setProductModels(String productModels) { this.productModels = productModels; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAuthMethods() { return authMethods; }
    public void setAuthMethods(String authMethods) { this.authMethods = authMethods; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public void setId(long id) { this.id = id; }
}
