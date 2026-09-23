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
 * 商业汇票实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_COMMERCIAL_BILL")
public class TblCommercialBill implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 汇票ID
     */
    @TableId(value = "BILL_ID", type = IdType.INPUT)
    private Long billId;

    /**
     * 汇票号码
     */
    @TableField("BILL_NO")
    private String billNo;

    /**
     * 汇票类型:BANK_ACCEPTANCE/COMMERCIAL_ACCEPTANCE/ELECTRONIC_BILL
     */
    @TableField("BILL_TYPE")
    private String billType;

    /**
     * 汇票金额
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
     * 出票人开户行
     */
    @TableField("DRAWER_BANK")
    private String drawerBank;

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
     * 收款人开户行
     */
    @TableField("PAYEE_BANK")
    private String payeeBank;

    /**
     * 承兑人名称
     */
    @TableField("ACCEPTOR_NAME")
    private String acceptorName;

    /**
     * 承兑人账号
     */
    @TableField("ACCEPTOR_ACCOUNT")
    private String acceptorAccount;

    /**
     * 承兑人开户行
     */
    @TableField("ACCEPTOR_BANK")
    private String acceptorBank;

    /**
     * 汇票状态:ISSUED/ACCEPTED/ENDORSED/DISCOUNTED/MATURED/DEFAULT
     */
    @TableField("BILL_STATUS")
    private String billStatus;

    /**
     * 是否可转让
     */
    @TableField("TRANSFERABLE")
    private String transferable;

    /**
     * 票面利率
     */
    @TableField("INTEREST_RATE")
    private BigDecimal interestRate;

    /**
     * 贴现利率
     */
    @TableField("DISCOUNT_RATE")
    private BigDecimal discountRate;

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
     * 创建人ID
     */
    @TableField("CREATE_USER_ID")
    private Long createUserId;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATE_USER_ID")
    private Long updateUserId;

    /**
     * 删除标志(0-正常,1-删除)
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

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

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getBillNo() { return billNo; }
    public void setBillNo(String billNo) { this.billNo = billNo; }
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
    public String getDrawerBank() { return drawerBank; }
    public void setDrawerBank(String drawerBank) { this.drawerBank = drawerBank; }
    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }
    public String getPayeeAccount() { return payeeAccount; }
    public void setPayeeAccount(String payeeAccount) { this.payeeAccount = payeeAccount; }
    public String getPayeeBank() { return payeeBank; }
    public void setPayeeBank(String payeeBank) { this.payeeBank = payeeBank; }
    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }
    public String getAcceptorAccount() { return acceptorAccount; }
    public void setAcceptorAccount(String acceptorAccount) { this.acceptorAccount = acceptorAccount; }
    public String getAcceptorBank() { return acceptorBank; }
    public void setAcceptorBank(String acceptorBank) { this.acceptorBank = acceptorBank; }
    public String getBillStatus() { return billStatus; }
    public void setBillStatus(String billStatus) { this.billStatus = billStatus; }
    public String getTransferable() { return transferable; }
    public void setTransferable(String transferable) { this.transferable = transferable; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public BigDecimal getDiscountRate() { return discountRate; }
    public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Long getCreateUserId() { return createUserId; }
    public void setCreateUserId(Long createUserId) { this.createUserId = createUserId; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Long getUpdateUserId() { return updateUserId; }
    public void setUpdateUserId(Long updateUserId) { this.updateUserId = updateUserId; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

}
