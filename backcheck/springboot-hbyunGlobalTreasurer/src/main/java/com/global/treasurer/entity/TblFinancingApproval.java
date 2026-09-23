package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除,使用手动编写的getter/setter

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融资审批实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FINANCING_APPROVAL")
public class TblFinancingApproval implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 审批ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long approvalId;

    /** 融资类型(BANK_LOAN-银行贷款,BOND_ISSUANCE-债券发行,FINANCIAL_LEASE-融资租赁) */
    private String financingType;

    /** 融资ID */
    private Long financingId;

    /** 融资编号 */
    private String financingNo;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 融资金额 */
    private BigDecimal financingAmount;

    /** 币种 */
    private String currencyCode;

    /** 融资期限 */
    private Integer financingTerm;

    /** 期限单位(YEAR-年,MONTH-月,DAY-日) */
    private String termUnit;

    /** 利率(%) */
    private BigDecimal interestRate;

    /** 还款方式(EQUAL_INSTALLMENT-等额本息,EQUAL_PRINCIPAL-等额本金,BULLET-到期还本) */
    private String repaymentMethod;

    /** 担保方式(CREDIT-信用,MORTGAGE-抵押,PLEDGE-质押,GUARANTEE-保证) */
    private String guaranteeMethod;

    /** 资金用途 */
    private String fundPurpose;

    /** 申请人ID */
    private Long applicantId;

    /** 申请人姓名 */
    private String applicantName;

    /** 申请时间 */
    private Date applicationTime;

    /** 当前审批节点(APPLICANT-申请人,DEPT_MANAGER-部门经理,FINANCE_DIRECTOR-财务总监,GENERAL_MANAGER-总经理) */
    private String currentApprovalNode;

    /** 审批状态(PENDING-待审批,APPROVED-已通过,REJECTED-已拒绝,CANCELLED-已撤销) */
    private String approvalStatus;

    /** 附件路径 */
    private String attachmentPath;

    /** 备注 */
    private String remarks;

    /** 创建人ID */
    private Long createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新人ID */
    private Long updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 删除标记(0-未删除,1-已删除) */
    private Integer deleted;

    /** 租户ID */
    private Long tenantId;

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

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
