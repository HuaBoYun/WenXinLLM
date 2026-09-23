package com.global.treasurer.vo;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行承兑汇票VO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BankAcceptanceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 承兑汇票ID */
    private Long acceptanceId;

    /** 承兑汇票编号 */
    private String acceptanceNumber;

    /** 承兑金额 */
    private BigDecimal acceptanceAmount;

    /** 币种 */
    private String currency;

    /** 出票日期 */
    private Date issueDate;

    /** 到期日期 */
    private Date maturityDate;

    /** 出票人 */
    private String drawer;

    /** 收款人 */
    private String payee;

    /** 承兑银行 */
    private String acceptingBank;

    /** 承兑银行账号 */
    private String acceptingBankAccount;

    /** 保证金金额 */
    private BigDecimal marginAmount;

    /** 保证金比例 */
    private BigDecimal marginRate;

    /** 承兑状态 */
    private String acceptanceStatus;

    /** 承兑状态名称 */
    private String acceptanceStatusName;

    /** 提交时间 */
    private Date submitTime;

    /** 审批意见 */
    private String approvalComment;

    /** 审批时间 */
    private Date approvalTime;

    /** 公司ID */
    private String companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getAcceptanceId() { return acceptanceId; }
    public void setAcceptanceId(Long acceptanceId) { this.acceptanceId = acceptanceId; }
    public String getAcceptanceNumber() { return acceptanceNumber; }
    public void setAcceptanceNumber(String acceptanceNumber) { this.acceptanceNumber = acceptanceNumber; }
    public BigDecimal getAcceptanceAmount() { return acceptanceAmount; }
    public void setAcceptanceAmount(BigDecimal acceptanceAmount) { this.acceptanceAmount = acceptanceAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public String getDrawer() { return drawer; }
    public void setDrawer(String drawer) { this.drawer = drawer; }
    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }
    public String getAcceptingBank() { return acceptingBank; }
    public void setAcceptingBank(String acceptingBank) { this.acceptingBank = acceptingBank; }
    public String getAcceptingBankAccount() { return acceptingBankAccount; }
    public void setAcceptingBankAccount(String acceptingBankAccount) { this.acceptingBankAccount = acceptingBankAccount; }
    public BigDecimal getMarginAmount() { return marginAmount; }
    public void setMarginAmount(BigDecimal marginAmount) { this.marginAmount = marginAmount; }
    public BigDecimal getMarginRate() { return marginRate; }
    public void setMarginRate(BigDecimal marginRate) { this.marginRate = marginRate; }
    public String getAcceptanceStatus() { return acceptanceStatus; }
    public void setAcceptanceStatus(String acceptanceStatus) { this.acceptanceStatus = acceptanceStatus; }
    public String getAcceptanceStatusName() { return acceptanceStatusName; }
    public void setAcceptanceStatusName(String acceptanceStatusName) { this.acceptanceStatusName = acceptanceStatusName; }
    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }
    public String getApprovalComment() { return approvalComment; }
    public void setApprovalComment(String approvalComment) { this.approvalComment = approvalComment; }
    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

}
