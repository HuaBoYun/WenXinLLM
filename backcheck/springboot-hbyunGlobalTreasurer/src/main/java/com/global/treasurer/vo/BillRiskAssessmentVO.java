package com.global.treasurer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据风险评估VO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "票据风险评估VO", description = "票据风险评估视图对象")
public class BillRiskAssessmentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评估ID")
    private Long assessmentId;

    @ApiModelProperty(value = "风险ID")
    private Long riskId;

    @ApiModelProperty(value = "票据ID")
    private Long billId;

    @ApiModelProperty(value = "票据号码")
    private String billNumber;

    @ApiModelProperty(value = "票据金额")
    private BigDecimal billAmount;

    @ApiModelProperty(value = "风险等级")
    private String riskLevel;

    @ApiModelProperty(value = "风险等级名称")
    private String riskLevelName;

    @ApiModelProperty(value = "风险类型")
    private String riskType;

    @ApiModelProperty(value = "风险类型名称")
    private String riskTypeName;

    @ApiModelProperty(value = "风险评分")
    private Integer riskScore;

    @ApiModelProperty(value = "风险金额")
    private BigDecimal riskAmount;

    @ApiModelProperty(value = "风险描述")
    private String riskDescription;

    @ApiModelProperty(value = "建议措施")
    private String recommendations;

    @ApiModelProperty(value = "评估日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date assessmentDate;

    @ApiModelProperty(value = "评估人姓名")
    private String assessorName;

    @ApiModelProperty(value = "处置状态")
    private String disposalStatus;

    @ApiModelProperty(value = "处置状态名称")
    private String disposalStatusName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "风险因素")
    private String riskFactors;

    @ApiModelProperty(value = "风险建议")
    private String riskSuggestions;

    @ApiModelProperty(value = "评估人ID")
    private Long assessorId;

    @ApiModelProperty(value = "承兑人名称")
    private String acceptorName;

    @ApiModelProperty(value = "评估状态")
    private String assessmentStatus;

    @ApiModelProperty(value = "评估状态名称")
    private String assessmentStatusName;

    @ApiModelProperty(value = "到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityDate;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }
    public Long getRiskId() { return riskId; }
    public void setRiskId(Long riskId) { this.riskId = riskId; }
    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getRiskLevelName() { return riskLevelName; }
    public void setRiskLevelName(String riskLevelName) { this.riskLevelName = riskLevelName; }
    public String getRiskType() { return riskType; }
    public void setRiskType(String riskType) { this.riskType = riskType; }
    public String getRiskTypeName() { return riskTypeName; }
    public void setRiskTypeName(String riskTypeName) { this.riskTypeName = riskTypeName; }
    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }
    public BigDecimal getRiskAmount() { return riskAmount; }
    public void setRiskAmount(BigDecimal riskAmount) { this.riskAmount = riskAmount; }
    public String getRiskDescription() { return riskDescription; }
    public void setRiskDescription(String riskDescription) { this.riskDescription = riskDescription; }
    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }
    public Date getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(Date assessmentDate) { this.assessmentDate = assessmentDate; }
    public String getAssessorName() { return assessorName; }
    public void setAssessorName(String assessorName) { this.assessorName = assessorName; }
    public String getDisposalStatus() { return disposalStatus; }
    public void setDisposalStatus(String disposalStatus) { this.disposalStatus = disposalStatus; }
    public String getDisposalStatusName() { return disposalStatusName; }
    public void setDisposalStatusName(String disposalStatusName) { this.disposalStatusName = disposalStatusName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }
    public String getRiskFactors() { return riskFactors; }
    public void setRiskFactors(String riskFactors) { this.riskFactors = riskFactors; }
    public String getRiskSuggestions() { return riskSuggestions; }
    public void setRiskSuggestions(String riskSuggestions) { this.riskSuggestions = riskSuggestions; }
    public Long getAssessorId() { return assessorId; }
    public void setAssessorId(Long assessorId) { this.assessorId = assessorId; }
    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }
    public String getAssessmentStatus() { return assessmentStatus; }
    public void setAssessmentStatus(String assessmentStatus) { this.assessmentStatus = assessmentStatus; }
    public String getAssessmentStatusName() { return assessmentStatusName; }
    public void setAssessmentStatusName(String assessmentStatusName) { this.assessmentStatusName = assessmentStatusName; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
}
