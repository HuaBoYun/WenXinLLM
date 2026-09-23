package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资计划DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "投资计划DTO", description = "投资计划数据传输对象")
public class InvestmentPlanDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "计划ID")
    private Long planId;

    @ApiModelProperty(value = "计划编号")
    private String planNo;

    @NotBlank(message = "计划名称不能为空")
    @ApiModelProperty(value = "计划名称", required = true)
    private String planName;

    @ApiModelProperty(value = "计划类型")
    private String planType;

    @NotBlank(message = "投资类型不能为空")
    @ApiModelProperty(value = "投资类型(BANK_WEALTH-银行理财,BOND-债券,EQUITY-股权,FUND-基金,DERIVATIVE-衍生品)", required = true)
    private String investmentType;

    @NotNull(message = "计划金额不能为空")
    @ApiModelProperty(value = "计划金额", required = true)
    private BigDecimal planAmount;

    @ApiModelProperty(value = "已投资金额")
    private BigDecimal investedAmount;

    @ApiModelProperty(value = "剩余金额")
    private BigDecimal remainingAmount;

    @ApiModelProperty(value = "预期收益率")
    private BigDecimal expectedReturnRate;

    @ApiModelProperty(value = "实际收益率")
    private BigDecimal actualReturnRate;

    @ApiModelProperty(value = "风险等级(LOW-低,MEDIUM-中,HIGH-高)")
    private String riskLevel;

    @ApiModelProperty(value = "计划状态")
    private String planStatus;

    @NotNull(message = "开始日期不能为空")
    @ApiModelProperty(value = "开始日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date planStartDate;

    @NotNull(message = "结束日期不能为空")
    @ApiModelProperty(value = "结束日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date planEndDate;

    @ApiModelProperty(value = "计划描述")
    private String planDescription;

    @ApiModelProperty(value = "审批意见")
    private String approvalComments;

    @ApiModelProperty(value = "驳回原因")
    private String rejectionReason;

    @ApiModelProperty(value = "完成备注")
    private String completionNotes;

    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "公司ID")
    private Long companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "创建人姓名")
    private String createdByName;

    @ApiModelProperty(value = "备注")
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
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
