package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融资还款实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FINANCING_REPAYMENT")
public class TblFinancingRepayment implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 还款ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long repaymentId;

    /** 还款编号 */
    private String repaymentNo;

    /** 融资ID */
    private Long financingId;

    /** 提款ID */
    private Long drawdownId;

    /** 提款编号 */
    private String drawdownNo;

    /** 融资类型(LOAN-贷款,BOND-债券,LEASE-租赁) */
    private String financingType;

    /** 还款类型(PRINCIPAL-本金,INTEREST-利息,BOTH-本息) */
    private String repaymentType;

    /** 应还金额 */
    private BigDecimal repaymentAmount;

    /** 实还金额 */
    private BigDecimal actualAmount;

    /** 总金额 */
    private BigDecimal totalAmount;

    /** 币种 */
    private String currencyCode;

    /** 应还日期 */
    private Date repaymentDate;

    /** 计划还款日期 */
    private Date planDate;

    /** 实际还款日期 */
    private Date actualDate;

    /** 实还日期 */
    private Date actualRepaymentDate;

    /** 还款状态(PENDING-未还,PAID-已还,PARTIAL-部分还,OVERDUE-逾期) */
    private String repaymentStatus;

    /** 本金金额 */
    private BigDecimal principalAmount;

    /** 利息金额 */
    private BigDecimal interestAmount;

    /** 罚息金额 */
    private BigDecimal penaltyAmount;

    /** 还款方式 */
    private String paymentMethod;

    /** 还款账户 */
    private String paymentAccount;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 删除标志 */
    private Integer deleteFlag;

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRepaymentId() { return repaymentId; }
    public void setRepaymentId(Long repaymentId) { this.repaymentId = repaymentId; }
    public String getRepaymentNo() { return repaymentNo; }
    public void setRepaymentNo(String repaymentNo) { this.repaymentNo = repaymentNo; }
    public Long getFinancingId() { return financingId; }
    public void setFinancingId(Long financingId) { this.financingId = financingId; }
    public Long getDrawdownId() { return drawdownId; }
    public void setDrawdownId(Long drawdownId) { this.drawdownId = drawdownId; }
    public String getDrawdownNo() { return drawdownNo; }
    public void setDrawdownNo(String drawdownNo) { this.drawdownNo = drawdownNo; }
    public String getFinancingType() { return financingType; }
    public void setFinancingType(String financingType) { this.financingType = financingType; }
    public String getRepaymentType() { return repaymentType; }
    public void setRepaymentType(String repaymentType) { this.repaymentType = repaymentType; }
    public BigDecimal getRepaymentAmount() { return repaymentAmount; }
    public void setRepaymentAmount(BigDecimal repaymentAmount) { this.repaymentAmount = repaymentAmount; }
    public BigDecimal getActualAmount() { return actualAmount; }
    public void setActualAmount(BigDecimal actualAmount) { this.actualAmount = actualAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Date getRepaymentDate() { return repaymentDate; }
    public void setRepaymentDate(Date repaymentDate) { this.repaymentDate = repaymentDate; }
    public Date getPlanDate() { return planDate; }
    public void setPlanDate(Date planDate) { this.planDate = planDate; }
    public Date getActualDate() { return actualDate; }
    public void setActualDate(Date actualDate) { this.actualDate = actualDate; }
    public Date getActualRepaymentDate() { return actualRepaymentDate; }
    public void setActualRepaymentDate(Date actualRepaymentDate) { this.actualRepaymentDate = actualRepaymentDate; }
    public String getRepaymentStatus() { return repaymentStatus; }
    public void setRepaymentStatus(String repaymentStatus) { this.repaymentStatus = repaymentStatus; }
    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }
    public BigDecimal getInterestAmount() { return interestAmount; }
    public void setInterestAmount(BigDecimal interestAmount) { this.interestAmount = interestAmount; }
    public BigDecimal getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(BigDecimal penaltyAmount) { this.penaltyAmount = penaltyAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentAccount() { return paymentAccount; }
    public void setPaymentAccount(String paymentAccount) { this.paymentAccount = paymentAccount; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
