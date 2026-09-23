package com.global.treasurer.entity;

// import lombok.Data; // 已移除

import java.util.Date;

/**
 * 银行信息实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-31
 */
// @Data // 已移除,使用手动编写的getter/setter
public class TblBankInfo {
    private Long bankId;
    private String bankCode;
    private String bankName;
    private String bankShortName;
    private String bankType;
    private String swiftCode;
    private String countryCode;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private String address;
    private String bankStatus;
    private Integer deleteFlag;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getBankId() { return bankId; }
    public void setBankId(Long bankId) { this.bankId = bankId; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getBankShortName() { return bankShortName; }
    public void setBankShortName(String bankShortName) { this.bankShortName = bankShortName; }
    public String getBankType() { return bankType; }
    public void setBankType(String bankType) { this.bankType = bankType; }
    public String getSwiftCode() { return swiftCode; }
    public void setSwiftCode(String swiftCode) { this.swiftCode = swiftCode; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getBankStatus() { return bankStatus; }
    public void setBankStatus(String bankStatus) { this.bankStatus = bankStatus; }
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

}
