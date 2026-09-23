package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据风险评估DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "票据风险评估DTO", description = "票据风险评估数据传输对象")
public class BillRiskAssessmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "风险ID(修改时必填)")
    private Long riskId;

    @ApiModelProperty(value = "评估ID(与riskId相同,兼容前端)")
    private Long assessmentId;

    @ApiModelProperty(value = "票据ID")
    private Long billId;

    @ApiModelProperty(value = "票据号码")
    private String billNumber;

    @ApiModelProperty(value = "风险评估等级:HIGH/MEDIUM/LOW", required = true)
    @NotBlank(message = "风险评估等级不能为空")
    private String riskLevel;

    @ApiModelProperty(value = "风险类型:CREDIT_RISK/LIQUIDITY_RISK/OPERATIONAL_RISK/MARKET_RISK")
    private String riskType;

    @ApiModelProperty(value = "风险评分(0-100)")
    private Integer riskScore;

    @ApiModelProperty(value = "风险金额")
    private BigDecimal riskAmount;

    @ApiModelProperty(value = "票据金额")
    private BigDecimal billAmount;

    @ApiModelProperty(value = "风险描述")
    private String riskDescription;

    @ApiModelProperty(value = "风险因素")
    private String riskFactors;

    @ApiModelProperty(value = "建议措施")
    private String recommendations;

    @ApiModelProperty(value = "评估日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date assessmentDate;

    @ApiModelProperty(value = "评估人ID")
    private Long assessorId;

    @ApiModelProperty(value = "评估人姓名")
    private String assessorName;

    @ApiModelProperty(value = "处置状态:PENDING/DISPOSING/DISPOSED")
    private String disposalStatus;

    @ApiModelProperty(value = "处置方案")
    private String disposalPlan;

    @ApiModelProperty(value = "处置日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date disposalDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "公司ID")
    private Long companyId;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "承兑人名称")
    private String acceptorName;

    @ApiModelProperty(value = "评估状态:PENDING/ASSESSED/REASSESS/DISPOSED")
    private String assessmentStatus;

    @ApiModelProperty(value = "到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityDate;

    // 以下方法由Lombok生成,手动添加以解决编译问题

    public Long getRiskId() {
        // 如果 riskId 为空但 assessmentId 有值，返回 assessmentId
        return riskId != null ? riskId : assessmentId;
    }
    public void setRiskId(Long riskId) { this.riskId = riskId; }

    public Long getAssessmentId() { return assessmentId != null ? assessmentId : riskId; }
    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
        // 同时设置 riskId，保持兼容
        if (this.riskId == null) {
            this.riskId = assessmentId;
        }
    }

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public String getRiskType() { return riskType; }
    public void setRiskType(String riskType) { this.riskType = riskType; }

    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }

    public BigDecimal getRiskAmount() { return riskAmount; }
    public void setRiskAmount(BigDecimal riskAmount) { this.riskAmount = riskAmount; }

    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }

    public String getRiskDescription() { return riskDescription; }
    public void setRiskDescription(String riskDescription) { this.riskDescription = riskDescription; }

    public String getRiskFactors() { return riskFactors; }
    public void setRiskFactors(String riskFactors) { this.riskFactors = riskFactors; }

    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }

    public Date getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(Date assessmentDate) { this.assessmentDate = assessmentDate; }

    public Long getAssessorId() { return assessorId; }
    public void setAssessorId(Long assessorId) { this.assessorId = assessorId; }

    public String getAssessorName() { return assessorName; }
    public void setAssessorName(String assessorName) { this.assessorName = assessorName; }

    public String getDisposalStatus() { return disposalStatus; }
    public void setDisposalStatus(String disposalStatus) { this.disposalStatus = disposalStatus; }

    public String getDisposalPlan() { return disposalPlan; }
    public void setDisposalPlan(String disposalPlan) { this.disposalPlan = disposalPlan; }

    public Date getDisposalDate() { return disposalDate; }
    public void setDisposalDate(Date disposalDate) { this.disposalDate = disposalDate; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }

    public String getAssessmentStatus() { return assessmentStatus; }
    public void setAssessmentStatus(String assessmentStatus) { this.assessmentStatus = assessmentStatus; }

    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
}
