package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金计划明细实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FUND_PLAN_DETAIL")
public class TblFundPlanDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long detailId;

    /** 计划ID */
    private Long planId;

    /** 业务类型(INCOME-收入,EXPENSE-支出,INVESTMENT-投资,FINANCING-融资,OTHER-其他) */
    private String businessType;

    /** 业务项目 */
    private String businessItem;

    /** 计划日期 */
    private Date plannedDate;

    /** 计划金额 */
    private BigDecimal plannedAmount;

    /** 实际日期 */
    private Date actualDate;

    /** 实际金额 */
    private BigDecimal actualAmount;

    /** 差异金额 */
    private BigDecimal varianceAmount;

    /** 差异率 */
    private BigDecimal varianceRate;

    /** 执行状态(PENDING-待执行,PROCESSING-执行中,EXECUTED-已执行,CANCELLED-已取消) */
    private String executionStatus;

    /** 执行率 */
    private BigDecimal executionRate;

    /** 币种 */
    private String currencyCode;

    /** 说明 */
    private String description;

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

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getDetailId() { return detailId; }
    public void setDetailId(Long detailId) { this.detailId = detailId; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getBusinessItem() { return businessItem; }
    public void setBusinessItem(String businessItem) { this.businessItem = businessItem; }
    public Date getPlannedDate() { return plannedDate; }
    public void setPlannedDate(Date plannedDate) { this.plannedDate = plannedDate; }
    public BigDecimal getPlannedAmount() { return plannedAmount; }
    public void setPlannedAmount(BigDecimal plannedAmount) { this.plannedAmount = plannedAmount; }
    public Date getActualDate() { return actualDate; }
    public void setActualDate(Date actualDate) { this.actualDate = actualDate; }
    public BigDecimal getActualAmount() { return actualAmount; }
    public void setActualAmount(BigDecimal actualAmount) { this.actualAmount = actualAmount; }
    public BigDecimal getVarianceAmount() { return varianceAmount; }
    public void setVarianceAmount(BigDecimal varianceAmount) { this.varianceAmount = varianceAmount; }
    public BigDecimal getVarianceRate() { return varianceRate; }
    public void setVarianceRate(BigDecimal varianceRate) { this.varianceRate = varianceRate; }
    public String getExecutionStatus() { return executionStatus; }
    public void setExecutionStatus(String executionStatus) { this.executionStatus = executionStatus; }
    public BigDecimal getExecutionRate() { return executionRate; }
    public void setExecutionRate(BigDecimal executionRate) { this.executionRate = executionRate; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
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

}
