package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融资计划实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FINANCING_PLAN")
public class TblFinancingPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 计划ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long planId;

    /** 计划编号 */
    private String planNo;

    /** 计划名称 */
    private String planName;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 计划类型(1-年度计划,2-季度计划,3-月度计划,4-临时计划) */
    private String planType;

    /** 计划年度 */
    private Integer planYear;

    /** 计划融资额 */
    private BigDecimal planAmount;

    /** 币种 */
    private String currencyCode;

    /** 计划状态(DRAFT-草稿,PENDING-审批中,APPROVED-已通过,REJECTED-已拒绝,EXECUTING-执行中,COMPLETED-已完成) */
    private String planStatus;

    /** 开始日期 */
    private Date startDate;

    /** 结束日期 */
    private Date endDate;

    /** 描述 */
    private String description;

    /** 删除标志(0-正常,1-删除) */
    private Integer deleteFlag;

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    private Date updatedTime;

    /** 计划季度 */
    private Integer planQuarter;

    /** 计划金额（planned） */
    private BigDecimal plannedAmount;

    /** 融资目的 */
    private String financingPurposes;

    /** 备注 */
    private String remark;

    /** 审批日期 */
    private Date approvalDate;

    /** 审批意见 */
    private String approvalComments;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getPlanNo() { return planNo; }
    public void setPlanNo(String planNo) { this.planNo = planNo; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }
    public Integer getPlanYear() { return planYear; }
    public void setPlanYear(Integer planYear) { this.planYear = planYear; }
    public BigDecimal getPlanAmount() { return planAmount; }
    public void setPlanAmount(BigDecimal planAmount) { this.planAmount = planAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getPlanStatus() { return planStatus; }
    public void setPlanStatus(String planStatus) { this.planStatus = planStatus; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
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
    public Integer getPlanQuarter() { return planQuarter; }
    public void setPlanQuarter(Integer planQuarter) { this.planQuarter = planQuarter; }
    public BigDecimal getPlannedAmount() { return plannedAmount; }
    public void setPlannedAmount(BigDecimal plannedAmount) { this.plannedAmount = plannedAmount; }
    public String getFinancingPurposes() { return financingPurposes; }
    public void setFinancingPurposes(String financingPurposes) { this.financingPurposes = financingPurposes; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getApprovalDate() { return approvalDate; }
    public void setApprovalDate(Date approvalDate) { this.approvalDate = approvalDate; }
    public String getApprovalComments() { return approvalComments; }
    public void setApprovalComments(String approvalComments) { this.approvalComments = approvalComments; }
}
