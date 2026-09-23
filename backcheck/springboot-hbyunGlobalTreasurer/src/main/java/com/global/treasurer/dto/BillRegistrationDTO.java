package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据登记DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "票据登记DTO", description = "票据登记数据传输对象")
public class BillRegistrationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "票据ID(修改时必填)")
    private Long billId;

    @ApiModelProperty(value = "票据号码", required = true)
    @NotBlank(message = "票据号码不能为空")
    private String billNumber;

    @ApiModelProperty(value = "票据类型:BANK_ACCEPTANCE/COMMERCIAL_ACCEPTANCE/CHECK/PROMISSORY_NOTE/ELECTRONIC_BILL", required = true)
    @NotBlank(message = "票据类型不能为空")
    private String billType;

    @ApiModelProperty(value = "票据金额", required = true)
    @NotNull(message = "票据金额不能为空")
    private BigDecimal billAmount;

    @ApiModelProperty(value = "币种", example = "CNY")
    private String currency;

    @ApiModelProperty(value = "出票日期", required = true)
    @NotNull(message = "出票日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;

    @ApiModelProperty(value = "到期日期", required = true)
    @NotNull(message = "到期日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityDate;

    @ApiModelProperty(value = "出票人名称", required = true)
    @NotBlank(message = "出票人名称不能为空")
    private String drawerName;

    @ApiModelProperty(value = "出票人账号")
    private String drawerAccount;

    @ApiModelProperty(value = "收款人名称", required = true)
    @NotBlank(message = "收款人名称不能为空")
    private String payeeName;

    @ApiModelProperty(value = "收款人账号")
    private String payeeAccount;

    @ApiModelProperty(value = "承兑人名称")
    private String acceptorName;

    @ApiModelProperty(value = "承兑银行")
    private String acceptingBank;

    @ApiModelProperty(value = "票据状态:HOLDING/ENDORSED/DISCOUNTED/MATURED/CANCELLED")
    private String billStatus;

    @ApiModelProperty(value = "票据用途:PAYMENT/SERVICE_FEE/INVESTMENT/OTHER")
    private String billPurpose;

    @ApiModelProperty(value = "票据来源:RECEIVED/ISSUED/ENDORSED")
    private String billSource;

    @ApiModelProperty(value = "保管地点")
    private String storageLocation;

    @ApiModelProperty(value = "登记人ID")
    private Long registrarId;

    @ApiModelProperty(value = "登记人姓名")
    private String registrarName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "公司ID")
    private Long companyId;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public String getDrawerName() { return drawerName; }
    public void setDrawerName(String drawerName) { this.drawerName = drawerName; }
    public String getDrawerAccount() { return drawerAccount; }
    public void setDrawerAccount(String drawerAccount) { this.drawerAccount = drawerAccount; }
    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }
    public String getPayeeAccount() { return payeeAccount; }
    public void setPayeeAccount(String payeeAccount) { this.payeeAccount = payeeAccount; }
    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }
    public String getAcceptingBank() { return acceptingBank; }
    public void setAcceptingBank(String acceptingBank) { this.acceptingBank = acceptingBank; }
    public String getBillStatus() { return billStatus; }
    public void setBillStatus(String billStatus) { this.billStatus = billStatus; }
    public String getBillPurpose() { return billPurpose; }
    public void setBillPurpose(String billPurpose) { this.billPurpose = billPurpose; }
    public String getBillSource() { return billSource; }
    public void setBillSource(String billSource) { this.billSource = billSource; }
    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }
    public Long getRegistrarId() { return registrarId; }
    public void setRegistrarId(Long registrarId) { this.registrarId = registrarId; }
    public String getRegistrarName() { return registrarName; }
    public void setRegistrarName(String registrarName) { this.registrarName = registrarName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
}
