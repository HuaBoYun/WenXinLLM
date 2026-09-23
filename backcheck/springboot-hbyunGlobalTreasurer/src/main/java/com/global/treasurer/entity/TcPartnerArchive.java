package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 伙伴档案实体类
 *
 * @author AI Assistant
 * @date 2025-09-20
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
@TableName("TC_PARTNER_ARCHIVE")
public class TcPartnerArchive implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    /**
     * 伙伴编码
     */
    @TableField("PARTNER_CODE")
    private String partnerCode;

    /**
     * 伙伴名称
     */
    @TableField("PARTNER_NAME")
    private String partnerName;

    /**
     * 伙伴简称
     */
    @TableField("PARTNER_SHORT_NAME")
    private String partnerShortName;

    /**
     * 伙伴英文名称
     */
    @TableField("PARTNER_EN_NAME")
    private String partnerEnName;

    /**
     * 伙伴类型ID
     */
    @TableField("PARTNER_TYPE_ID")
    private String partnerTypeId;

    /**
     * 伙伴类型名称（非数据库字段，用于前端显示）
     */
    @TableField(exist = false)  // MyBatis-Plus 注解
    @Transient  // TK MyBatis 注解，标记为非数据库字段
    private String partnerTypeName;

    /**
     * 统一社会信用代码
     */
    @TableField("UNIFIED_SOCIAL_CREDIT_CODE")
    private String unifiedSocialCreditCode;

    /**
     * 法定代表人
     */
    @TableField("LEGAL_REPRESENTATIVE")
    private String legalRepresentative;

    /**
     * 注册资本
     */
    @TableField("REGISTERED_CAPITAL")
    private BigDecimal registeredCapital;

    /**
     * 成立日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("ESTABLISHMENT_DATE")
    private Date establishmentDate;

    /**
     * 营业期限开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("BUSINESS_TERM_START")
    private Date businessTermStart;

    /**
     * 营业期限结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("BUSINESS_TERM_END")
    private Date businessTermEnd;

    /**
     * 注册地址
     */
    @TableField("REGISTERED_ADDRESS")
    private String registeredAddress;

    /**
     * 办公地址
     */
    @TableField("OFFICE_ADDRESS")
    private String officeAddress;

    /**
     * 经营范围
     */
    @TableField("BUSINESS_SCOPE")
    private String businessScope;

    /**
     * 主营业务
     */
    @TableField("MAIN_BUSINESS")
    private String mainBusiness;

    /**
     * 行业分类
     */
    @TableField("INDUSTRY_CLASSIFICATION")
    private String industryClassification;

    /**
     * 企业规模
     */
    @TableField("ENTERPRISE_SCALE")
    private String enterpriseScale;

    /**
     * 联系电话
     */
    @TableField("CONTACT_PHONE")
    private String contactPhone;

    /**
     * 传真号码
     */
    @TableField("FAX_NUMBER")
    private String faxNumber;

    /**
     * 邮箱
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 网址
     */
    @TableField("WEBSITE")
    private String website;

    /**
     * 实收资本
     */
    @TableField("PAID_IN_CAPITAL")
    private BigDecimal paidInCapital;

    /**
     * 资产规模
     */
    @TableField("ASSET_SCALE")
    private BigDecimal assetScale;

    /**
     * 年营业收入
     */
    @TableField("ANNUAL_REVENUE")
    private BigDecimal annualRevenue;

    /**
     * 信用等级
     */
    @TableField("CREDIT_RATING")
    private String creditRating;

    /**
     * 风险等级
     */
    @TableField("RISK_LEVEL")
    private String riskLevel;

    /**
     * 合作开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("COOPERATION_START_DATE")
    private Date cooperationStartDate;

    /**
     * 合作状态
     */
    @TableField("COOPERATION_STATUS")
    private String cooperationStatus;

    /**
     * 状态(1-启用,0-停用)
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
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_USER", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    /**
     * 版本号
     */
    @Version
    @TableField("VERSION_NO")
    private Integer versionNo;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPartnerCode() { return partnerCode; }
    public void setPartnerCode(String partnerCode) { this.partnerCode = partnerCode; }
    public String getPartnerName() { return partnerName; }
    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
    public String getPartnerShortName() { return partnerShortName; }
    public void setPartnerShortName(String partnerShortName) { this.partnerShortName = partnerShortName; }
    public String getPartnerEnName() { return partnerEnName; }
    public void setPartnerEnName(String partnerEnName) { this.partnerEnName = partnerEnName; }
    public String getPartnerTypeId() { return partnerTypeId; }
    public void setPartnerTypeId(String partnerTypeId) { this.partnerTypeId = partnerTypeId; }
    public String getUnifiedSocialCreditCode() { return unifiedSocialCreditCode; }
    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) { this.unifiedSocialCreditCode = unifiedSocialCreditCode; }
    public String getLegalRepresentative() { return legalRepresentative; }
    public void setLegalRepresentative(String legalRepresentative) { this.legalRepresentative = legalRepresentative; }
    public BigDecimal getRegisteredCapital() { return registeredCapital; }
    public void setRegisteredCapital(BigDecimal registeredCapital) { this.registeredCapital = registeredCapital; }
    public Date getEstablishmentDate() { return establishmentDate; }
    public void setEstablishmentDate(Date establishmentDate) { this.establishmentDate = establishmentDate; }
    public Date getBusinessTermStart() { return businessTermStart; }
    public void setBusinessTermStart(Date businessTermStart) { this.businessTermStart = businessTermStart; }
    public Date getBusinessTermEnd() { return businessTermEnd; }
    public void setBusinessTermEnd(Date businessTermEnd) { this.businessTermEnd = businessTermEnd; }
    public String getRegisteredAddress() { return registeredAddress; }
    public void setRegisteredAddress(String registeredAddress) { this.registeredAddress = registeredAddress; }
    public String getOfficeAddress() { return officeAddress; }
    public void setOfficeAddress(String officeAddress) { this.officeAddress = officeAddress; }
    public String getBusinessScope() { return businessScope; }
    public void setBusinessScope(String businessScope) { this.businessScope = businessScope; }
    public String getMainBusiness() { return mainBusiness; }
    public void setMainBusiness(String mainBusiness) { this.mainBusiness = mainBusiness; }
    public String getIndustryClassification() { return industryClassification; }
    public void setIndustryClassification(String industryClassification) { this.industryClassification = industryClassification; }
    public String getEnterpriseScale() { return enterpriseScale; }
    public void setEnterpriseScale(String enterpriseScale) { this.enterpriseScale = enterpriseScale; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getFaxNumber() { return faxNumber; }
    public void setFaxNumber(String faxNumber) { this.faxNumber = faxNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public BigDecimal getPaidInCapital() { return paidInCapital; }
    public void setPaidInCapital(BigDecimal paidInCapital) { this.paidInCapital = paidInCapital; }
    public BigDecimal getAssetScale() { return assetScale; }
    public void setAssetScale(BigDecimal assetScale) { this.assetScale = assetScale; }
    public BigDecimal getAnnualRevenue() { return annualRevenue; }
    public void setAnnualRevenue(BigDecimal annualRevenue) { this.annualRevenue = annualRevenue; }
    public String getCreditRating() { return creditRating; }
    public void setCreditRating(String creditRating) { this.creditRating = creditRating; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public Date getCooperationStartDate() { return cooperationStartDate; }
    public void setCooperationStartDate(Date cooperationStartDate) { this.cooperationStartDate = cooperationStartDate; }
    public String getCooperationStatus() { return cooperationStatus; }
    public void setCooperationStatus(String cooperationStatus) { this.cooperationStatus = cooperationStatus; }
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

    // 新增字段的 getter 和 setter
    public String getPartnerTypeName() { return partnerTypeName; }
    public void setPartnerTypeName(String partnerTypeName) { this.partnerTypeName = partnerTypeName; }

}
