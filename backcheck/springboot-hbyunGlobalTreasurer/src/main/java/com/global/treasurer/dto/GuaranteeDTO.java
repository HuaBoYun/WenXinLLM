package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 保函DTO
 *
 * 数据库表 TBL_GUARANTEE 实际字段：
 * - COMPANY_ID, GUARANTEE_ID, GUARANTEE_TARGET, GUARANTEE_AMOUNT
 * - GUARANTEE_DATE, GUARANTEE_STATUS, GUARANTEE_TYPE, CREATE_TIME, GUARANTEED_PARTY_ID
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class GuaranteeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 保函ID (数据库类型 VARCHAR2(10)) */
    private String guaranteeId;

    /** 保函类型 (必填，对应数据库 GUARANTEE_TYPE) */
    @NotBlank(message = "保函类型不能为空")
    private String guaranteeType;

    /** 保函金额 (必填，对应数据库 GUARANTEE_AMOUNT) */
    @NotNull(message = "保函金额不能为空")
    private BigDecimal guaranteeAmount;

    /** 币种 (非数据库字段) */
    private String currency;

    /** 开立日期 (非数据库字段) */
    private Date issueDate;

    /** 有效期/到期日期 (对应数据库 GUARANTEE_DATE) */
    private Date expiryDate;

    /** 申请人 (对应数据库 GUARANTEED_PARTY_ID) */
    @NotBlank(message = "申请人不能为空")
    private String applicant;

    /** 受益人 (对应数据库 GUARANTEE_TARGET) */
    @NotBlank(message = "受益人不能为空")
    private String beneficiary;

    /** 担保银行/开立银行 (非数据库字段，可选) */
    private String guaranteeBank;

    /** 开立银行 (前端字段名，映射到 guaranteeBank) */
    private String issuingBank;

    /** 公司ID (对应数据库 COMPANY_ID) */
    private String companyId;

    /** 公司名称 (非数据库字段) */
    private String companyName;

    /** 备注 (非数据库字段) */
    private String remark;


    // Getter 和 Setter 方法

    public String getGuaranteeId() { return guaranteeId; }
    public void setGuaranteeId(String guaranteeId) { this.guaranteeId = guaranteeId; }
    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }
    public BigDecimal getGuaranteeAmount() { return guaranteeAmount; }
    public void setGuaranteeAmount(BigDecimal guaranteeAmount) { this.guaranteeAmount = guaranteeAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }
    public String getBeneficiary() { return beneficiary; }
    public void setBeneficiary(String beneficiary) { this.beneficiary = beneficiary; }
    public String getGuaranteeBank() { return guaranteeBank; }
    public void setGuaranteeBank(String guaranteeBank) { this.guaranteeBank = guaranteeBank; }
    public String getIssuingBank() { return issuingBank; }
    public void setIssuingBank(String issuingBank) { this.issuingBank = issuingBank; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
