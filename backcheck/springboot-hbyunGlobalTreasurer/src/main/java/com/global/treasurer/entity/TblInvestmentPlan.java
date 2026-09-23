package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资计划实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_INVESTMENT_PLAN")
public class TblInvestmentPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 计划ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long planId;

    /** 计划编号 */
    private String planNo;

    /** 计划名称 */
    private String planName;

    /** 计划类型 */
    private String planType;

    /** 投资类型(BANK_WEALTH-银行理财,BOND-债券,EQUITY-股权,FUND-基金,DERIVATIVE-衍生品) */
    private String investmentType;

    /** 计划金额 */
    private BigDecimal planAmount;

    /** 已投资金额 */
    private BigDecimal investedAmount;

    /** 剩余金额 */
    private BigDecimal remainingAmount;

    /** 预期收益率 */
    private BigDecimal expectedReturnRate;

    /** 实际收益率 */
    private BigDecimal actualReturnRate;

    /** 风险等级(LOW-低,MEDIUM-中,HIGH-高) */
    private String riskLevel;

    /** 计划状态(DRAFT-草稿,SUBMITTED-已提交,APPROVED-已审批,EXECUTING-执行中,COMPLETED-已完成,CANCELLED-已取消) */
    private String planStatus;

    /** 开始日期 */
    private Date planStartDate;

    /** 结束日期 */
    private Date planEndDate;

    /** 计划描述 */
    private String planDescription;

    /** 审批意见 */
    private String approvalComments;

    /** 驳回原因 */
    private String rejectionReason;

    /** 完成备注 */
    private String completionNotes;

    /** 取消原因 */
    private String cancelReason;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 创建人 */
    private Long createdBy;

    /** 创建人姓名 */
    private String createdByName;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private Long updatedBy;

    /** 更新人姓名 */
    private String updatedByName;

    /** 更新时间 */
    private Date updatedTime;

    /** 删除标志(0-正常,1-删除) */
    private Integer deleteFlag;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getPlanNo() { return planNo; }
    public void setPlanNo(String planNo) { this.planNo = planNo; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }
    public String getInvestmentType() { return investmentType; }
    public void setInvestmentType(String investmentType) { this.investmentType = investmentType; }
    public BigDecimal getPlanAmount() { return planAmount; }
    public void setPlanAmount(BigDecimal planAmount) { this.planAmount = planAmount; }
    public BigDecimal getInvestedAmount() { return investedAmount; }
    public void setInvestedAmount(BigDecimal investedAmount) { this.investedAmount = investedAmount; }
    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
    public BigDecimal getExpectedReturnRate() { return expectedReturnRate; }
    public void setExpectedReturnRate(BigDecimal expectedReturnRate) { this.expectedReturnRate = expectedReturnRate; }
    public BigDecimal getActualReturnRate() { return actualReturnRate; }
    public void setActualReturnRate(BigDecimal actualReturnRate) { this.actualReturnRate = actualReturnRate; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getPlanStatus() { return planStatus; }
    public void setPlanStatus(String planStatus) { this.planStatus = planStatus; }
    public Date getPlanStartDate() { return planStartDate; }
    public void setPlanStartDate(Date planStartDate) { this.planStartDate = planStartDate; }
    public Date getPlanEndDate() { return planEndDate; }
    public void setPlanEndDate(Date planEndDate) { this.planEndDate = planEndDate; }
    public String getPlanDescription() { return planDescription; }
    public void setPlanDescription(String planDescription) { this.planDescription = planDescription; }
    public String getApprovalComments() { return approvalComments; }
    public void setApprovalComments(String approvalComments) { this.approvalComments = approvalComments; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public String getCompletionNotes() { return completionNotes; }
    public void setCompletionNotes(String completionNotes) { this.completionNotes = completionNotes; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
