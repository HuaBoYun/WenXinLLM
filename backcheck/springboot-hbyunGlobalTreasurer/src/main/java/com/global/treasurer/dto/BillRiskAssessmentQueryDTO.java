package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 票据风险评估查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "票据风险评估查询DTO", description = "票据风险评估查询数据传输对象")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillRiskAssessmentQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "票据ID")
    private Long billId;

    @ApiModelProperty(value = "票据号码")
    private String billNumber;

    @ApiModelProperty(value = "风险等级")
    private String riskLevel;

    @ApiModelProperty(value = "风险类型")
    private String riskType;

    @ApiModelProperty(value = "处置状态")
    private String disposalStatus;

    @ApiModelProperty(value = "评估人名称")
    private String assessorName;

    @ApiModelProperty(value = "开始日期")
    private Date startDate;

    @ApiModelProperty(value = "结束日期")
    private Date endDate;

    @ApiModelProperty(value = "风险分数最小值")
    private java.math.BigDecimal riskScoreMin;

    @ApiModelProperty(value = "风险分数最大值")
    private java.math.BigDecimal riskScoreMax;

    @ApiModelProperty(value = "评估开始日期")
    private Date assessmentDateStart;

    @ApiModelProperty(value = "评估结束日期")
    private Date assessmentDateEnd;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getRiskType() { return riskType; }
    public void setRiskType(String riskType) { this.riskType = riskType; }
    public String getDisposalStatus() { return disposalStatus; }
    public void setDisposalStatus(String disposalStatus) { this.disposalStatus = disposalStatus; }
    public String getAssessorName() { return assessorName; }
    public void setAssessorName(String assessorName) { this.assessorName = assessorName; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public java.math.BigDecimal getRiskScoreMin() { return riskScoreMin; }
    public void setRiskScoreMin(java.math.BigDecimal riskScoreMin) { this.riskScoreMin = riskScoreMin; }
    public java.math.BigDecimal getRiskScoreMax() { return riskScoreMax; }
    public void setRiskScoreMax(java.math.BigDecimal riskScoreMax) { this.riskScoreMax = riskScoreMax; }

    public Date getAssessmentDateStart() { return assessmentDateStart; }
    public void setAssessmentDateStart(Date assessmentDateStart) { this.assessmentDateStart = assessmentDateStart; }
    public Date getAssessmentDateEnd() { return assessmentDateEnd; }
    public void setAssessmentDateEnd(Date assessmentDateEnd) { this.assessmentDateEnd = assessmentDateEnd; }


    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
