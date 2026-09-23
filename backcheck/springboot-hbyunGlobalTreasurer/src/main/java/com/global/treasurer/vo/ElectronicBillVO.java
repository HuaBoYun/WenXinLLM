package com.global.treasurer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子票据VO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "电子票据VO", description = "电子票据视图对象")
public class ElectronicBillVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "电子票据ID")
    private Long billId;

    @ApiModelProperty(value = "电子票据号码")
    private String billNumber;

    @ApiModelProperty(value = "票据类型")
    private String billType;

    @ApiModelProperty(value = "票据类型名称")
    private String billTypeName;

    @ApiModelProperty(value = "票据金额")
    private BigDecimal billAmount;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "出票日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;

    @ApiModelProperty(value = "到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityDate;

    @ApiModelProperty(value = "剩余天数")
    private Integer remainingDays;

    @ApiModelProperty(value = "出票人名称")
    private String drawerName;

    @ApiModelProperty(value = "出票人账号")
    private String drawerAccount;

    @ApiModelProperty(value = "出票人开户行")
    private String drawerBank;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "收款人账号")
    private String payeeAccount;

    @ApiModelProperty(value = "承兑人名称")
    private String acceptorName;

    @ApiModelProperty(value = "承兑人账号")
    private String acceptorAccount;

    @ApiModelProperty(value = "承兑人开户行")
    private String acceptorBank;

    @ApiModelProperty(value = "票据状态")
    private String billStatus;

    @ApiModelProperty(value = "票据状态名称")
    private String billStatusName;

    @ApiModelProperty(value = "数字签名状态")
    private String signatureStatus;

    @ApiModelProperty(value = "签名状态名称")
    private String signatureStatusName;

    @ApiModelProperty(value = "签名时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date signatureTime;

    @ApiModelProperty(value = "电子票据URL")
    private String billUrl;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "数字签名")
    private String digitalSignature;

    @ApiModelProperty(value = "验证状态")
    private String verificationStatus;

    @ApiModelProperty(value = "电子状态")
    private String electronicStatus;

    @ApiModelProperty(value = "票据用途")
    private String billPurpose;

    @ApiModelProperty(value = "合同号")
    private String contractNumber;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    // Getter and Setter methods

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }

    public String getBillTypeName() { return billTypeName; }
    public void setBillTypeName(String billTypeName) { this.billTypeName = billTypeName; }

    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }

    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }

    public Integer getRemainingDays() { return remainingDays; }
    public void setRemainingDays(Integer remainingDays) { this.remainingDays = remainingDays; }

    public String getDrawerName() { return drawerName; }
    public void setDrawerName(String drawerName) { this.drawerName = drawerName; }

    public String getDrawerAccount() { return drawerAccount; }
    public void setDrawerAccount(String drawerAccount) { this.drawerAccount = drawerAccount; }

    public String getDrawerBank() { return drawerBank; }
    public void setDrawerBank(String drawerBank) { this.drawerBank = drawerBank; }

    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }

    public String getPayeeAccount() { return payeeAccount; }
    public void setPayeeAccount(String payeeAccount) { this.payeeAccount = payeeAccount; }

    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }

    public String getAcceptorAccount() { return acceptorAccount; }
    public void setAcceptorAccount(String acceptorAccount) { this.acceptorAccount = acceptorAccount; }

    public String getAcceptorBank() { return acceptorBank; }
    public void setAcceptorBank(String acceptorBank) { this.acceptorBank = acceptorBank; }

    public String getBillStatus() { return billStatus; }
    public void setBillStatus(String billStatus) { this.billStatus = billStatus; }

    public String getBillStatusName() { return billStatusName; }
    public void setBillStatusName(String billStatusName) { this.billStatusName = billStatusName; }

    public String getSignatureStatus() { return signatureStatus; }
    public void setSignatureStatus(String signatureStatus) { this.signatureStatus = signatureStatus; }

    public String getSignatureStatusName() { return signatureStatusName; }
    public void setSignatureStatusName(String signatureStatusName) { this.signatureStatusName = signatureStatusName; }

    public Date getSignatureTime() { return signatureTime; }
    public void setSignatureTime(Date signatureTime) { this.signatureTime = signatureTime; }

    public String getBillUrl() { return billUrl; }
    public void setBillUrl(String billUrl) { this.billUrl = billUrl; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getDigitalSignature() { return digitalSignature; }
    public void setDigitalSignature(String digitalSignature) { this.digitalSignature = digitalSignature; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public String getElectronicStatus() { return electronicStatus; }
    public void setElectronicStatus(String electronicStatus) { this.electronicStatus = electronicStatus; }

    public String getBillPurpose() { return billPurpose; }
    public void setBillPurpose(String billPurpose) { this.billPurpose = billPurpose; }

    public String getContractNumber() { return contractNumber; }
    public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
