package com.global.treasurer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据登记VO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "票据登记VO", description = "票据登记视图对象")
public class BillRegistrationVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "票据ID")
    private Long billId;

    @ApiModelProperty(value = "票据号码")
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

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "收款人账号")
    private String payeeAccount;

    @ApiModelProperty(value = "承兑人名称")
    private String acceptorName;

    @ApiModelProperty(value = "承兑银行")
    private String acceptingBank;

    @ApiModelProperty(value = "票据状态")
    private String billStatus;

    @ApiModelProperty(value = "票据状态名称")
    private String billStatusName;

    @ApiModelProperty(value = "票据用途")
    private String billPurpose;

    @ApiModelProperty(value = "票据用途名称")
    private String billPurposeName;

    @ApiModelProperty(value = "票据来源")
    private String billSource;

    @ApiModelProperty(value = "票据来源名称")
    private String billSourceName;

    @ApiModelProperty(value = "保管地点")
    private String storageLocation;

    @ApiModelProperty(value = "登记人ID")
    private Long registrarId;

    @ApiModelProperty(value = "登记人姓名")
    private String registrarName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

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
    public String getBillStatusName() { return billStatusName; }
    public void setBillStatusName(String billStatusName) { this.billStatusName = billStatusName; }
    public String getBillPurpose() { return billPurpose; }
    public void setBillPurpose(String billPurpose) { this.billPurpose = billPurpose; }
    public String getBillPurposeName() { return billPurposeName; }
    public void setBillPurposeName(String billPurposeName) { this.billPurposeName = billPurposeName; }
    public String getBillSource() { return billSource; }
    public void setBillSource(String billSource) { this.billSource = billSource; }
    public String getBillSourceName() { return billSourceName; }
    public void setBillSourceName(String billSourceName) { this.billSourceName = billSourceName; }
    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }
    public Long getRegistrarId() { return registrarId; }
    public void setRegistrarId(Long registrarId) { this.registrarId = registrarId; }
    public String getRegistrarName() { return registrarName; }
    public void setRegistrarName(String registrarName) { this.registrarName = registrarName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

}
