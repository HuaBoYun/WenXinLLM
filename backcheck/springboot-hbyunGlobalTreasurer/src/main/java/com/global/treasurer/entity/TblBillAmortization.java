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
 * 票据摊销实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BILL_AMORTIZATION")
public class TblBillAmortization implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "AMORTIZATION_ID", type = IdType.INPUT)
    private Long amortizationId;

    @TableField("BILL_NUMBER")
    private String billNumber;

    @TableField("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @TableField("AMORTIZED_AMOUNT")
    private BigDecimal amortizedAmount;

    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    @TableField("AMORTIZATION_METHOD")
    private String amortizationMethod;

    @TableField("AMORTIZATION_PERIOD")
    private Integer amortizationPeriod;

    @TableField("MONTHLY_AMOUNT")
    private BigDecimal monthlyAmount;

    @TableField("AMORTIZATION_STATUS")
    private String amortizationStatus;

    @TableField("START_DATE")
    private Date startDate;

    @TableField("END_DATE")
    private Date endDate;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("DEPT_ID")
    private Long deptId;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getAmortizationId() { return amortizationId; }
    public void setAmortizationId(Long amortizationId) { this.amortizationId = amortizationId; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getAmortizedAmount() { return amortizedAmount; }
    public void setAmortizedAmount(BigDecimal amortizedAmount) { this.amortizedAmount = amortizedAmount; }
    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
    public String getAmortizationMethod() { return amortizationMethod; }
    public void setAmortizationMethod(String amortizationMethod) { this.amortizationMethod = amortizationMethod; }
    public Integer getAmortizationPeriod() { return amortizationPeriod; }
    public void setAmortizationPeriod(Integer amortizationPeriod) { this.amortizationPeriod = amortizationPeriod; }
    public BigDecimal getMonthlyAmount() { return monthlyAmount; }
    public void setMonthlyAmount(BigDecimal monthlyAmount) { this.monthlyAmount = monthlyAmount; }
    public String getAmortizationStatus() { return amortizationStatus; }
    public void setAmortizationStatus(String amortizationStatus) { this.amortizationStatus = amortizationStatus; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
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
