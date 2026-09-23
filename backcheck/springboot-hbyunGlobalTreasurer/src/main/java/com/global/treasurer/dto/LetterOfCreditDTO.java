package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 信用证DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class LetterOfCreditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 信用证ID */
    private Long lcId;

    /** 信用证类型 */
    @NotBlank(message = "信用证类型不能为空")
    private String lcType;

    /** 信用证金额 */
    @NotNull(message = "信用证金额不能为空")
    private BigDecimal lcAmount;

    /** 币种 */
    private String currency;

    /** 开证日期 */
    private Date issueDate;

    /** 有效期 */
    @NotNull(message = "有效期不能为空")
    private Date expiryDate;

    /** 申请人 */
    @NotBlank(message = "申请人不能为空")
    private String applicant;

    /** 受益人 */
    @NotBlank(message = "受益人不能为空")
    private String beneficiary;

    /** 开证银行 */
    @NotBlank(message = "开证银行不能为空")
    private String issuingBank;

    /** 通知银行 */
    private String advisingBank;

    /** 公司ID */
    private String companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getLcId() { return lcId; }
    public void setLcId(Long lcId) { this.lcId = lcId; }
    public String getLcType() { return lcType; }
    public void setLcType(String lcType) { this.lcType = lcType; }
    public BigDecimal getLcAmount() { return lcAmount; }
    public void setLcAmount(BigDecimal lcAmount) { this.lcAmount = lcAmount; }
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
    public String getIssuingBank() { return issuingBank; }
    public void setIssuingBank(String issuingBank) { this.issuingBank = issuingBank; }
    public String getAdvisingBank() { return advisingBank; }
    public void setAdvisingBank(String advisingBank) { this.advisingBank = advisingBank; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
