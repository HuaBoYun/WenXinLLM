package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 监管分析实体类
 * @author Claude
 * @date 2026-01-20
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_REGULATORY_ANALYSIS")
@ApiModel(value = "TblRegulatoryAnalysis", description = "监管分析实体")
public class TblRegulatoryAnalysis implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ANALYSIS_ID", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("分析ID")
    private String analysisId;

    @TableField("ANALYSIS_NO")
    @ApiModelProperty("分析编号")
    private String analysisNo;

    @TableField("ANALYSIS_NAME")
    @ApiModelProperty("分析名称")
    private String analysisName;

    @TableField("ANALYSIS_TYPE")
    @ApiModelProperty("分析类型(CROSS_BORDER-跨境资金/LARGE_AMOUNT-大额交易/FREQUENCY-高频交易/COMPLIANCE-合规检查)")
    private String analysisType;

    @TableField("POOL_ID")
    @ApiModelProperty("资金池ID")
    private String poolId;

    @TableField("COMPANY_ID")
    @ApiModelProperty("公司ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    @ApiModelProperty("公司名称")
    private String companyName;

    @TableField("ANALYSIS_DATE")
    @ApiModelProperty("分析日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date analysisDate;

    @TableField("REGULATOR")
    @ApiModelProperty("监管机构(pboc-中国人民银行/cbirc-银保监会/csrc-证监会/safe-外汇管理局)")
    private String regulator;

    @TableField("COMPLIANCE_SCORE")
    @ApiModelProperty("合规评分(0-100)")
    private Integer complianceScore;

    @TableField("COMPLIANCE_STATUS")
    @ApiModelProperty("合规状态(COMPLIANT-合规/NON_COMPLIANT-不合规/PENDING_REVIEW-待审核)")
    private String complianceStatus;

    @TableField("RISK_LEVEL")
    @ApiModelProperty("风险等级(LOW-低/MEDIUM-中/HIGH-高/CRITICAL-严重)")
    private String riskLevel;

    @TableField("TOTAL_AMOUNT")
    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @TableField("CROSS_BORDER_AMOUNT")
    @ApiModelProperty("跨境金额")
    private BigDecimal crossBorderAmount;

    @TableField("DOMESTIC_AMOUNT")
    @ApiModelProperty("境内金额")
    private BigDecimal domesticAmount;

    @TableField("VIOLATION_COUNT")
    @ApiModelProperty("违规次数")
    private Integer violationCount;

    @TableField("WARNING_COUNT")
    @ApiModelProperty("预警次数")
    private Integer warningCount;

    @TableField("ANALYSIS_RESULT")
    @ApiModelProperty("分析结果")
    private String analysisResult;

    @TableField("RECOMMENDATIONS")
    @ApiModelProperty("建议措施")
    private String recommendations;

    @TableField("DESCRIPTION")
    @ApiModelProperty("分析描述")
    private String description;

    @TableField("REPORT_FILE_PATH")
    @ApiModelProperty("报告文件路径")
    private String reportFilePath;

    @TableField("ANALYSIS_STATUS")
    @ApiModelProperty("分析状态(DRAFT-草稿/ANALYZING-分析中/COMPLETED-已完成/REVIEWED-已审核)")
    private String analysisStatus;

    @TableField("GENERATE_TIME")
    @ApiModelProperty("生成时间")
    private Date generateTime;

    @TableField("GENERATE_BY")
    @ApiModelProperty("生成人")
    private String generateBy;

    @TableField("REVIEW_TIME")
    @ApiModelProperty("审核时间")
    private Date reviewTime;

    @TableField("REVIEW_BY")
    @ApiModelProperty("审核人")
    private String reviewBy;

    @TableField("REVIEW_REMARK")
    @ApiModelProperty("审核备注")
    private String reviewRemark;

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


    public String getAnalysisId() { return analysisId; }
    public void setAnalysisId(String analysisId) { this.analysisId = analysisId; }
    public String getAnalysisNo() { return analysisNo; }
    public void setAnalysisNo(String analysisNo) { this.analysisNo = analysisNo; }
    public String getAnalysisName() { return analysisName; }
    public void setAnalysisName(String analysisName) { this.analysisName = analysisName; }
    public String getAnalysisType() { return analysisType; }
    public void setAnalysisType(String analysisType) { this.analysisType = analysisType; }
    public String getPoolId() { return poolId; }
    public void setPoolId(String poolId) { this.poolId = poolId; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Date getAnalysisDate() { return analysisDate; }
    public void setAnalysisDate(Date analysisDate) { this.analysisDate = analysisDate; }
    public String getRegulator() { return regulator; }
    public void setRegulator(String regulator) { this.regulator = regulator; }
    public Integer getComplianceScore() { return complianceScore; }
    public void setComplianceScore(Integer complianceScore) { this.complianceScore = complianceScore; }
    public String getComplianceStatus() { return complianceStatus; }
    public void setComplianceStatus(String complianceStatus) { this.complianceStatus = complianceStatus; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getCrossBorderAmount() { return crossBorderAmount; }
    public void setCrossBorderAmount(BigDecimal crossBorderAmount) { this.crossBorderAmount = crossBorderAmount; }
    public BigDecimal getDomesticAmount() { return domesticAmount; }
    public void setDomesticAmount(BigDecimal domesticAmount) { this.domesticAmount = domesticAmount; }
    public Integer getViolationCount() { return violationCount; }
    public void setViolationCount(Integer violationCount) { this.violationCount = violationCount; }
    public Integer getWarningCount() { return warningCount; }
    public void setWarningCount(Integer warningCount) { this.warningCount = warningCount; }
    public String getAnalysisResult() { return analysisResult; }
    public void setAnalysisResult(String analysisResult) { this.analysisResult = analysisResult; }
    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReportFilePath() { return reportFilePath; }
    public void setReportFilePath(String reportFilePath) { this.reportFilePath = reportFilePath; }
    public String getAnalysisStatus() { return analysisStatus; }
    public void setAnalysisStatus(String analysisStatus) { this.analysisStatus = analysisStatus; }
    public Date getGenerateTime() { return generateTime; }
    public void setGenerateTime(Date generateTime) { this.generateTime = generateTime; }
    public String getGenerateBy() { return generateBy; }
    public void setGenerateBy(String generateBy) { this.generateBy = generateBy; }
    public Date getReviewTime() { return reviewTime; }
    public void setReviewTime(Date reviewTime) { this.reviewTime = reviewTime; }
    public String getReviewBy() { return reviewBy; }
    public void setReviewBy(String reviewBy) { this.reviewBy = reviewBy; }
    public String getReviewRemark() { return reviewRemark; }
    public void setReviewRemark(String reviewRemark) { this.reviewRemark = reviewRemark; }
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
