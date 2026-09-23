package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 融资计划DTO
 *
 * @author HuaBo Cloud
 * @since 2025-01-14
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancingPlanDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long planId;
    private String planNo;
    private String planName;
    private Integer planType;
    private String planTypeName;
    private Integer planYear;
    private Integer planQuarter;
    private Integer planMonth;
    private BigDecimal financingTarget;
    private BigDecimal actualAmount;
    private BigDecimal completionRate;
    private Integer planStatus;
    private String planStatusName;
    private String approvalBy;
    private LocalDateTime approvalTime;
    private String approvalOpinion;
    private LocalDate planStartDate;
    private LocalDate planEndDate;
    private String currency;
    private String department;
    private String operator;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Integer page;
    private Integer limit;
    private String startDate;
    private String endDate;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getPlanNo() { return planNo; }
    public void setPlanNo(String planNo) { this.planNo = planNo; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Integer getPlanType() { return planType; }
    public void setPlanType(Integer planType) { this.planType = planType; }
    public String getPlanTypeName() { return planTypeName; }
    public void setPlanTypeName(String planTypeName) { this.planTypeName = planTypeName; }
    public Integer getPlanYear() { return planYear; }
    public void setPlanYear(Integer planYear) { this.planYear = planYear; }
    public Integer getPlanQuarter() { return planQuarter; }
    public void setPlanQuarter(Integer planQuarter) { this.planQuarter = planQuarter; }
    public Integer getPlanMonth() { return planMonth; }
    public void setPlanMonth(Integer planMonth) { this.planMonth = planMonth; }
    public BigDecimal getFinancingTarget() { return financingTarget; }
    public void setFinancingTarget(BigDecimal financingTarget) { this.financingTarget = financingTarget; }
    public BigDecimal getActualAmount() { return actualAmount; }
    public void setActualAmount(BigDecimal actualAmount) { this.actualAmount = actualAmount; }
    public BigDecimal getCompletionRate() { return completionRate; }
    public void setCompletionRate(BigDecimal completionRate) { this.completionRate = completionRate; }
    public Integer getPlanStatus() { return planStatus; }
    public void setPlanStatus(Integer planStatus) { this.planStatus = planStatus; }
    public String getPlanStatusName() { return planStatusName; }
    public void setPlanStatusName(String planStatusName) { this.planStatusName = planStatusName; }
    public String getApprovalBy() { return approvalBy; }
    public void setApprovalBy(String approvalBy) { this.approvalBy = approvalBy; }
    public LocalDateTime getApprovalTime() { return approvalTime; }
    public void setApprovalTime(LocalDateTime approvalTime) { this.approvalTime = approvalTime; }
    public String getApprovalOpinion() { return approvalOpinion; }
    public void setApprovalOpinion(String approvalOpinion) { this.approvalOpinion = approvalOpinion; }
    public LocalDate getPlanStartDate() { return planStartDate; }
    public void setPlanStartDate(LocalDate planStartDate) { this.planStartDate = planStartDate; }
    public LocalDate getPlanEndDate() { return planEndDate; }
    public void setPlanEndDate(LocalDate planEndDate) { this.planEndDate = planEndDate; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

}
