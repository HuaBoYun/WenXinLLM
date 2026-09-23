package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子票据实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@TableName("TBL_ELECTRONIC_BILL")
public class TblElectronicBill implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "E_BILL_ID", type = IdType.INPUT)
    private Long electronicBillId;

    @TableField("E_BILL_NO")
    private String billNumber;

    @TableField("BILL_TYPE")
    private String billType;

    @TableField("BILL_AMOUNT")
    private BigDecimal billAmount;

    @TableField("CURRENCY")
    private String currency;

    @TableField("DRAWER_NAME")
    private String drawerName;

    @TableField("DRAWER_ACCOUNT")
    private String drawerAccount;

    @TableField("DRAWER_BANK")
    private String drawerBank;

    @TableField("ACCEPTOR_NAME")
    private String acceptorName;

    @TableField("ACCEPTOR_ACCOUNT")
    private String acceptorAccount;

    @TableField("ACCEPTOR_BANK")
    private String acceptorBank;

    @TableField("PAYEE_NAME")
    private String payeeName;

    @TableField("PAYEE_ACCOUNT")
    private String payeeAccount;

    @TableField("ISSUE_DATE")
    private Date issueDate;

    @TableField("MATURITY_DATE")
    private Date maturityDate;

    @TableField("BILL_STATUS")
    private String billStatus;

    @TableField("DIGITAL_SIGNATURE")
    private String digitalSignature;

    @TableField("SIGNATURE_STATUS")
    private String signatureStatus;

    @TableField("SIGNATURE_ALGORITHM")
    private String signatureAlgorithm;

    @TableField("TIMESTAMP_SERVER")
    private String timestampServer;

    @TableField("VERIFICATION_STATUS")
    private String verificationStatus;

    @TableField("ELECTRONIC_STATUS")
    private String electronicStatus;

    @TableField("BILL_PURPOSE")
    private String billPurpose;

    @TableField("CONTRACT_NUMBER")
    private String contractNumber;

    @TableField("SIGNER_NAME")
    private String signerName;

    @TableField("VERIFICATION_TIME")
    private Date verificationTime;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("DEPT_ID")
    private Long deptId;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField("REMARK")
    private String remark;


    // Getter and Setter methods

    public Long getElectronicBillId() { return electronicBillId; }
    public void setElectronicBillId(Long electronicBillId) { this.electronicBillId = electronicBillId; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }

    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getDrawerName() { return drawerName; }
    public void setDrawerName(String drawerName) { this.drawerName = drawerName; }

    public String getDrawerAccount() { return drawerAccount; }
    public void setDrawerAccount(String drawerAccount) { this.drawerAccount = drawerAccount; }

    public String getDrawerBank() { return drawerBank; }
    public void setDrawerBank(String drawerBank) { this.drawerBank = drawerBank; }

    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }

    public String getAcceptorAccount() { return acceptorAccount; }
    public void setAcceptorAccount(String acceptorAccount) { this.acceptorAccount = acceptorAccount; }

    public String getAcceptorBank() { return acceptorBank; }
    public void setAcceptorBank(String acceptorBank) { this.acceptorBank = acceptorBank; }

    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }

    public String getPayeeAccount() { return payeeAccount; }
    public void setPayeeAccount(String payeeAccount) { this.payeeAccount = payeeAccount; }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }

    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }

    public String getBillStatus() { return billStatus; }
    public void setBillStatus(String billStatus) { this.billStatus = billStatus; }

    public String getDigitalSignature() { return digitalSignature; }
    public void setDigitalSignature(String digitalSignature) { this.digitalSignature = digitalSignature; }

    public String getSignatureStatus() { return signatureStatus; }
    public void setSignatureStatus(String signatureStatus) { this.signatureStatus = signatureStatus; }

    public String getSignatureAlgorithm() { return signatureAlgorithm; }
    public void setSignatureAlgorithm(String signatureAlgorithm) { this.signatureAlgorithm = signatureAlgorithm; }

    public String getTimestampServer() { return timestampServer; }
    public void setTimestampServer(String timestampServer) { this.timestampServer = timestampServer; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public String getElectronicStatus() { return electronicStatus; }
    public void setElectronicStatus(String electronicStatus) { this.electronicStatus = electronicStatus; }

    public String getBillPurpose() { return billPurpose; }
    public void setBillPurpose(String billPurpose) { this.billPurpose = billPurpose; }

    public String getContractNumber() { return contractNumber; }
    public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }

    public String getSignerName() { return signerName; }
    public void setSignerName(String signerName) { this.signerName = signerName; }

    public Date getVerificationTime() { return verificationTime; }
    public void setVerificationTime(Date verificationTime) { this.verificationTime = verificationTime; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }

    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
