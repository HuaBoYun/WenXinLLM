package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 银行机构信息实体类
 * 
 * @author system
 * @since 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("t_am_bank_institution")
public class AmBankInstitution implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 银行编码
     */
    @TableField("bank_code")
    private String bankCode;

    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;

    /**
     * 银行简称
     */
    @TableField("bank_short_name")
    private String bankShortName;

    /**
     * 银行英文名称
     */
    @TableField("bank_english_name")
    private String bankEnglishName;

    /**
     * 银行类型：COMMERCIAL-商业银行，POLICY-政策性银行，FOREIGN-外资银行
     */
    @TableField("bank_type")
    private String bankType;

    /**
     * 银行级别：HEAD-总行，BRANCH-分行，SUB_BRANCH-支行
     */
    @TableField("bank_level")
    private String bankLevel;

    /**
     * 上级银行ID
     */
    @TableField("parent_bank_id")
    private Long parentBankId;

    /**
     * 联行号
     */
    @TableField("clearing_code")
    private String clearingCode;

    /**
     * SWIFT代码
     */
    @TableField("swift_code")
    private String swiftCode;

    /**
     * 银行地址
     */
    @TableField("bank_address")
    private String bankAddress;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 联系人
     */
    @TableField("contact_person")
    private String contactPerson;

    /**
     * 邮箱地址
     */
    @TableField("email")
    private String email;

    /**
     * 传真号码
     */
    @TableField("fax")
    private String fax;

    /**
     * 邮政编码
     */
    @TableField("postal_code")
    private String postalCode;

    /**
     * 所在城市
     */
    @TableField("city")
    private String city;

    /**
     * 所在省份
     */
    @TableField("province")
    private String province;

    /**
     * 所在国家
     */
    @TableField("country")
    private String country;

    /**
     * 是否支持银企直连
     */
    @TableField("support_direct_connection")
    private String supportDirectConnection;

    /**
     * 银企直连类型：EBPP，SWIFT，PROPRIETARY
     */
    @TableField("direct_connection_type")
    private String directConnectionType;

    /**
     * 合作状态：ACTIVE-合作中，INACTIVE-暂停合作，TERMINATED-终止合作
     */
    @TableField("cooperation_status")
    private String cooperationStatus;

    /**
     * 风险等级：LOW-低风险，MEDIUM-中风险，HIGH-高风险
     */
    @TableField("risk_level")
    private String riskLevel;

    /**
     * 信用评级
     */
    @TableField("credit_rating")
    private String creditRating;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态：1-有效，0-无效
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField("update_user")
    private Long updateUser;

    /**
     * 版本号
     */
    @TableField("version_no")
    private Long versionNo;

    /**
     * 客户端IP
     */
    @TableField("client_ip")
    private String clientIp;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getBankShortName() { return bankShortName; }
    public void setBankShortName(String bankShortName) { this.bankShortName = bankShortName; }
    public String getBankEnglishName() { return bankEnglishName; }
    public void setBankEnglishName(String bankEnglishName) { this.bankEnglishName = bankEnglishName; }
    public String getBankType() { return bankType; }
    public void setBankType(String bankType) { this.bankType = bankType; }
    public String getBankLevel() { return bankLevel; }
    public void setBankLevel(String bankLevel) { this.bankLevel = bankLevel; }
    public Long getParentBankId() { return parentBankId; }
    public void setParentBankId(Long parentBankId) { this.parentBankId = parentBankId; }
    public String getClearingCode() { return clearingCode; }
    public void setClearingCode(String clearingCode) { this.clearingCode = clearingCode; }
    public String getSwiftCode() { return swiftCode; }
    public void setSwiftCode(String swiftCode) { this.swiftCode = swiftCode; }
    public String getBankAddress() { return bankAddress; }
    public void setBankAddress(String bankAddress) { this.bankAddress = bankAddress; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFax() { return fax; }
    public void setFax(String fax) { this.fax = fax; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getSupportDirectConnection() { return supportDirectConnection; }
    public void setSupportDirectConnection(String supportDirectConnection) { this.supportDirectConnection = supportDirectConnection; }
    public String getDirectConnectionType() { return directConnectionType; }
    public void setDirectConnectionType(String directConnectionType) { this.directConnectionType = directConnectionType; }
    public String getCooperationStatus() { return cooperationStatus; }
    public void setCooperationStatus(String cooperationStatus) { this.cooperationStatus = cooperationStatus; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getCreditRating() { return creditRating; }
    public void setCreditRating(String creditRating) { this.creditRating = creditRating; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }
    public Long getVersionNo() { return versionNo; }
    public void setVersionNo(Long versionNo) { this.versionNo = versionNo; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }

}
