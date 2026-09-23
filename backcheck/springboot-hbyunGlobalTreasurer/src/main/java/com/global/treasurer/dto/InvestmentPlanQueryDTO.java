package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资计划查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "投资计划查询DTO", description = "投资计划查询条件")
public class InvestmentPlanQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "计划编号")
    private String planNo;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "计划类型")
    private String planType;

    @ApiModelProperty(value = "投资类型")
    private String investmentType;

    @ApiModelProperty(value = "风险等级")
    private String riskLevel;

    @ApiModelProperty(value = "计划状态")
    private String planStatus;

    @ApiModelProperty(value = "最小计划金额")
    private BigDecimal minPlanAmount;

    @ApiModelProperty(value = "最大计划金额")
    private BigDecimal maxPlanAmount;

    @ApiModelProperty(value = "开始日期起")
    private Date planStartDateFrom;

    @ApiModelProperty(value = "开始日期止")
    private Date planStartDateTo;

    @ApiModelProperty(value = "结束日期起")
    private Date planEndDateFrom;

    @ApiModelProperty(value = "结束日期止")
    private Date planEndDateTo;

    @ApiModelProperty(value = "公司ID")
    private Long companyId;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer pageSize = 10;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getPlanNo() { return planNo; }
    public void setPlanNo(String planNo) { this.planNo = planNo; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }
    public String getInvestmentType() { return investmentType; }
    public void setInvestmentType(String investmentType) { this.investmentType = investmentType; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getPlanStatus() { return planStatus; }
    public void setPlanStatus(String planStatus) { this.planStatus = planStatus; }
    public BigDecimal getMinPlanAmount() { return minPlanAmount; }
    public void setMinPlanAmount(BigDecimal minPlanAmount) { this.minPlanAmount = minPlanAmount; }
    public BigDecimal getMaxPlanAmount() { return maxPlanAmount; }
    public void setMaxPlanAmount(BigDecimal maxPlanAmount) { this.maxPlanAmount = maxPlanAmount; }
    public Date getPlanStartDateFrom() { return planStartDateFrom; }
    public void setPlanStartDateFrom(Date planStartDateFrom) { this.planStartDateFrom = planStartDateFrom; }
    public Date getPlanStartDateTo() { return planStartDateTo; }
    public void setPlanStartDateTo(Date planStartDateTo) { this.planStartDateTo = planStartDateTo; }
    public Date getPlanEndDateFrom() { return planEndDateFrom; }
    public void setPlanEndDateFrom(Date planEndDateFrom) { this.planEndDateFrom = planEndDateFrom; }
    public Date getPlanEndDateTo() { return planEndDateTo; }
    public void setPlanEndDateTo(Date planEndDateTo) { this.planEndDateTo = planEndDateTo; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }


    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
