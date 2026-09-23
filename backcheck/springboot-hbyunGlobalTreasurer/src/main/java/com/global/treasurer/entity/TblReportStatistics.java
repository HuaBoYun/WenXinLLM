package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 报表统计实体类
 * @author Claude
 * @date 2026-01-20
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_REPORT_STATISTICS")
@ApiModel(value = "TblReportStatistics", description = "报表统计实体")
public class TblReportStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "REPORT_ID", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("报表ID")
    private String reportId;

    @TableField("REPORT_NO")
    @ApiModelProperty("报表编号")
    private String reportNo;

    @TableField("REPORT_NAME")
    @ApiModelProperty("报表名称")
    private String reportName;

    @TableField("REPORT_TYPE")
    @ApiModelProperty("报表类型(COLLECTION-归集报表/ALLOCATION-下拨报表/LOAN-借贷报表/COMPREHENSIVE-综合报表)")
    private String reportType;

    @TableField("REPORT_PERIOD")
    @ApiModelProperty("报表周期(DAILY-日报/WEEKLY-周报/MONTHLY-月报/QUARTERLY-季报/YEARLY-年报)")
    private String reportPeriod;

    @TableField("POOL_ID")
    @ApiModelProperty("资金池ID")
    private String poolId;

    @TableField("COMPANY_ID")
    @ApiModelProperty("公司ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    @ApiModelProperty("公司名称")
    private String companyName;

    @TableField("START_DATE")
    @ApiModelProperty("开始日期")
    private Date startDate;

    @TableField("END_DATE")
    @ApiModelProperty("结束日期")
    private Date endDate;

    @TableField("TOTAL_COLLECTION")
    @ApiModelProperty("归集总额")
    private BigDecimal totalCollection;

    @TableField("TOTAL_ALLOCATION")
    @ApiModelProperty("下拨总额")
    private BigDecimal totalAllocation;

    @TableField("TOTAL_LOAN")
    @ApiModelProperty("借贷总额")
    private BigDecimal totalLoan;

    @TableField("TOTAL_REPAYMENT")
    @ApiModelProperty("还款总额")
    private BigDecimal totalRepayment;

    @TableField("TOTAL_INTEREST")
    @ApiModelProperty("利息总额")
    private BigDecimal totalInterest;

    @TableField("COLLECTION_COUNT")
    @ApiModelProperty("归集笔数")
    private Integer collectionCount;

    @TableField("ALLOCATION_COUNT")
    @ApiModelProperty("下拨笔数")
    private Integer allocationCount;

    @TableField("LOAN_COUNT")
    @ApiModelProperty("借贷笔数")
    private Integer loanCount;

    @TableField("SUCCESS_RATE")
    @ApiModelProperty("成功率")
    private BigDecimal successRate;

    @TableField("AVERAGE_AMOUNT")
    @ApiModelProperty("平均金额")
    private BigDecimal averageAmount;

    @TableField("REPORT_STATUS")
    @ApiModelProperty("报表状态(DRAFT-草稿/GENERATED-已生成/PUBLISHED-已发布)")
    private String reportStatus;

    @TableField("GENERATE_TIME")
    @ApiModelProperty("生成时间")
    private Date generateTime;

    @TableField("GENERATE_BY")
    @ApiModelProperty("生成人")
    private String generateBy;

    @TableField("REPORT_DATA")
    @ApiModelProperty("报表数据(JSON格式)")
    private String reportData;

    @TableField("REMARK")
    @ApiModelProperty("备注")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("DEL_FLAG")
    private String delFlag;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }
    public String getReportNo() { return reportNo; }
    public void setReportNo(String reportNo) { this.reportNo = reportNo; }
    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public String getReportPeriod() { return reportPeriod; }
    public void setReportPeriod(String reportPeriod) { this.reportPeriod = reportPeriod; }
    public String getPoolId() { return poolId; }
    public void setPoolId(String poolId) { this.poolId = poolId; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public BigDecimal getTotalCollection() { return totalCollection; }
    public void setTotalCollection(BigDecimal totalCollection) { this.totalCollection = totalCollection; }
    public BigDecimal getTotalAllocation() { return totalAllocation; }
    public void setTotalAllocation(BigDecimal totalAllocation) { this.totalAllocation = totalAllocation; }
    public BigDecimal getTotalLoan() { return totalLoan; }
    public void setTotalLoan(BigDecimal totalLoan) { this.totalLoan = totalLoan; }
    public BigDecimal getTotalRepayment() { return totalRepayment; }
    public void setTotalRepayment(BigDecimal totalRepayment) { this.totalRepayment = totalRepayment; }
    public BigDecimal getTotalInterest() { return totalInterest; }
    public void setTotalInterest(BigDecimal totalInterest) { this.totalInterest = totalInterest; }
    public Integer getCollectionCount() { return collectionCount; }
    public void setCollectionCount(Integer collectionCount) { this.collectionCount = collectionCount; }
    public Integer getAllocationCount() { return allocationCount; }
    public void setAllocationCount(Integer allocationCount) { this.allocationCount = allocationCount; }
    public Integer getLoanCount() { return loanCount; }
    public void setLoanCount(Integer loanCount) { this.loanCount = loanCount; }
    public BigDecimal getSuccessRate() { return successRate; }
    public void setSuccessRate(BigDecimal successRate) { this.successRate = successRate; }
    public BigDecimal getAverageAmount() { return averageAmount; }
    public void setAverageAmount(BigDecimal averageAmount) { this.averageAmount = averageAmount; }
    public String getReportStatus() { return reportStatus; }
    public void setReportStatus(String reportStatus) { this.reportStatus = reportStatus; }
    public Date getGenerateTime() { return generateTime; }
    public void setGenerateTime(Date generateTime) { this.generateTime = generateTime; }
    public String getGenerateBy() { return generateBy; }
    public void setGenerateBy(String generateBy) { this.generateBy = generateBy; }
    public String getReportData() { return reportData; }
    public void setReportData(String reportData) { this.reportData = reportData; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
}
