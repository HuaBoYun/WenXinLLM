package com.global.treasurer.dto;

// import lombok.Data; // 已移除,使用手动编写的getter/setter

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融资审批DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancingApprovalDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 审批ID */
    private Long approvalId;

    /** 融资类型(BANK_LOAN-银行贷款,BOND_ISSUANCE-债券发行,FINANCIAL_LEASE-融资租赁) */
    @NotBlank(message = "融资类型不能为空")
    private String financingType;

    /** 融资ID */
    @NotNull(message = "融资ID不能为空")
    private Long financingId;

    /** 融资编号 */
    private String financingNo;

    /** 公司ID */
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 融资金额 */
    @NotNull(message = "融资金额不能为空")
    private BigDecimal financingAmount;

    /** 币种 */
    @NotBlank(message = "币种不能为空")
    private String currencyCode;

    /** 融资期限 */
    private Integer financingTerm;

    /** 期限单位 */
    private String termUnit;

    /** 利率 */
    private BigDecimal interestRate;

    /** 还款方式 */
    private String repaymentMethod;

    /** 担保方式 */
    private String guaranteeMethod;

    /** 资金用途 */
    private String fundPurpose;

    /** 申请人ID */
    private Long applicantId;

    /** 申请人姓名 */
    private String applicantName;

    /** 申请时间 */
    private Date applicationTime;

    /** 当前审批节点 */
    private String currentApprovalNode;

    /** 审批状态(PENDING-待审批,APPROVED-已通过,REJECTED-已拒绝,CANCELLED-已撤销) */
    private String approvalStatus;

    /** 附件路径 */
    private String attachmentPath;

    /** 备注 */
    private String remarks;

    // 以下方法由Lombok生成,手动添加以解决编译问题

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public String getFinancingType() {
        return financingType;
    }

    public void setFinancingType(String financingType) {
        this.financingType = financingType;
    }

    public Long getFinancingId() {
        return financingId;
    }

    public void setFinancingId(Long financingId) {
        this.financingId = financingId;
    }

    public String getFinancingNo() {
        return financingNo;
    }

    public void setFinancingNo(String financingNo) {
        this.financingNo = financingNo;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getFinancingAmount() {
        return financingAmount;
    }

    public void setFinancingAmount(BigDecimal financingAmount) {
        this.financingAmount = financingAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getFinancingTerm() {
        return financingTerm;
    }

    public void setFinancingTerm(Integer financingTerm) {
        this.financingTerm = financingTerm;
    }

    public String getTermUnit() {
        return termUnit;
    }

    public void setTermUnit(String termUnit) {
        this.termUnit = termUnit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public String getGuaranteeMethod() {
        return guaranteeMethod;
    }

    public void setGuaranteeMethod(String guaranteeMethod) {
        this.guaranteeMethod = guaranteeMethod;
    }

    public String getFundPurpose() {
        return fundPurpose;
    }

    public void setFundPurpose(String fundPurpose) {
        this.fundPurpose = fundPurpose;
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Date getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    public String getCurrentApprovalNode() {
        return currentApprovalNode;
    }

    public void setCurrentApprovalNode(String currentApprovalNode) {
        this.currentApprovalNode = currentApprovalNode;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
