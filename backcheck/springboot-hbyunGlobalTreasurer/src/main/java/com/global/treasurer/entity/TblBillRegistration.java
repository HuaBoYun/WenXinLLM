package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据登记实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BILL_REGISTRATION")
public class TblBillRegistration implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 票据ID
     */
    @TableId(value = "BILL_ID", type = IdType.INPUT)
    private Long billId;

    /**
     * 票据号码
     */
    @TableField("BILL_NUMBER")
    private String billNumber;

    /**
     * 票据类型:BANK_ACCEPTANCE/COMMERCIAL_ACCEPTANCE/CHECK/PROMISSORY_NOTE/ELECTRONIC_BILL
     */
    @TableField("BILL_TYPE")
    private String billType;

    /**
     * 票据金额
     */
    @TableField("BILL_AMOUNT")
    private BigDecimal billAmount;

    /**
     * 币种
     */
    @TableField("CURRENCY")
    private String currency;

    /**
     * 出票日期
     */
    @TableField("ISSUE_DATE")
    private Date issueDate;

    /**
     * 到期日期
     */
    @TableField("MATURITY_DATE")
    private Date maturityDate;

    /**
     * 出票人名称
     */
    @TableField("DRAWER_NAME")
    private String drawerName;

    /**
     * 出票人账号
     */
    @TableField("DRAWER_ACCOUNT")
    private String drawerAccount;

    /**
     * 收款人名称
     */
    @TableField("PAYEE_NAME")
    private String payeeName;

    /**
     * 收款人账号
     */
    @TableField("PAYEE_ACCOUNT")
    private String payeeAccount;

    /**
     * 承兑人名称
     */
    @TableField("ACCEPTOR_NAME")
    private String acceptorName;

    /**
     * 承兑银行
     */
    @TableField("ACCEPTING_BANK")
    private String acceptingBank;

    /**
     * 票据状态:HOLDING/ENDORSED/DISCOUNTED/MATURED/CANCELLED
     */
    @TableField("BILL_STATUS")
    private String billStatus;

    /**
     * 票据用途:PAYMENT/SERVICE_FEE/INVESTMENT/OTHER
     */
    @TableField("BILL_PURPOSE")
    private String billPurpose;

    /**
     * 票据来源:RECEIVED/ISSUED/ENDORSED
     */
    @TableField("BILL_SOURCE")
    private String billSource;

    /**
     * 保管地点
     */
    @TableField("STORAGE_LOCATION")
    private String storageLocation;

    /**
     * 登记人ID
     */
    @TableField("REGISTRAR_ID")
    private Long registrarId;

    /**
     * 登记人姓名
     */
    @TableField("REGISTRAR_NAME")
    private String registrarName;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 公司ID
     */
    @TableField("COMPANY_ID")
    private Long companyId;

    /**
     * 部门ID
     */
    @TableField("DEPT_ID")
    private Long deptId;

    /**
     * 删除标记:0-未删除,1-已删除
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;


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
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }

}
