package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金计划执行实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FUND_PLAN_EXECUTION")
public class TblFundPlanExecution implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 执行ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long executionId;

    /** 执行编号 */
    private String executionNo;

    /** 计划ID */
    private Long planId;

    /** 明细ID */
    private Long detailId;

    /** 执行类型(INCOME-收入执行,EXPENSE-支出执行,INVESTMENT-投资执行,FINANCING-融资执行,TRANSFER-转账执行) */
    private String executionType;

    /** 执行状态(PENDING-待执行,IN_PROGRESS-执行中,COMPLETED-已完成,PAUSED-已暂停,CANCELLED-已取消) */
    private String executionStatus;

    /** 计划金额 */
    private BigDecimal plannedAmount;

    /** 执行金额 */
    private BigDecimal executedAmount;

    /** 执行率 */
    private BigDecimal executionRate;

    /** 计划日期 */
    private Date plannedDate;

    /** 实际日期 */
    private Date actualDate;

    /** 延期天数 */
    private Integer delayDays;

    /** 执行效率 */
    private BigDecimal executionEfficiency;

    /** 风险等级(HIGH-高风险,MEDIUM-中风险,LOW-低风险) */
    private String riskLevel;

    /** 执行说明 */
    private String executionNotes;

    /** 删除标志(0-正常,1-删除) */
    private Integer deleteFlag;

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

    /** 组织ID */
    private Long orgId;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getExecutionId() { return executionId; }
    public void setExecutionId(Long executionId) { this.executionId = executionId; }
    public String getExecutionNo() { return executionNo; }
    public void setExecutionNo(String executionNo) { this.executionNo = executionNo; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Long getDetailId() { return detailId; }
    public void setDetailId(Long detailId) { this.detailId = detailId; }
    public String getExecutionType() { return executionType; }
    public void setExecutionType(String executionType) { this.executionType = executionType; }
    public String getExecutionStatus() { return executionStatus; }
    public void setExecutionStatus(String executionStatus) { this.executionStatus = executionStatus; }
    public BigDecimal getPlannedAmount() { return plannedAmount; }
    public void setPlannedAmount(BigDecimal plannedAmount) { this.plannedAmount = plannedAmount; }
    public BigDecimal getExecutedAmount() { return executedAmount; }
    public void setExecutedAmount(BigDecimal executedAmount) { this.executedAmount = executedAmount; }
    public BigDecimal getExecutionRate() { return executionRate; }
    public void setExecutionRate(BigDecimal executionRate) { this.executionRate = executionRate; }
    public Date getPlannedDate() { return plannedDate; }
    public void setPlannedDate(Date plannedDate) { this.plannedDate = plannedDate; }
    public Date getActualDate() { return actualDate; }
    public void setActualDate(Date actualDate) { this.actualDate = actualDate; }
    public Integer getDelayDays() { return delayDays; }
    public void setDelayDays(Integer delayDays) { this.delayDays = delayDays; }
    public BigDecimal getExecutionEfficiency() { return executionEfficiency; }
    public void setExecutionEfficiency(BigDecimal executionEfficiency) { this.executionEfficiency = executionEfficiency; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getExecutionNotes() { return executionNotes; }
    public void setExecutionNotes(String executionNotes) { this.executionNotes = executionNotes; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
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
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

}
