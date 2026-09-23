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
 * 票据风险评估实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BILL_RISK_ASSESSMENT")
public class TblBillRiskAssessment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ASSESSMENT_ID", type = IdType.INPUT)
    private Long assessmentId;

    @TableField("BILL_NUMBER")
    private String billNumber;

    @TableField("BILL_AMOUNT")
    private BigDecimal billAmount;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("RISK_TYPE")
    private String riskType;

    @TableField("ACCEPTOR_NAME")
    private String acceptorName;

    @TableField("ASSESSMENT_STATUS")
    private String assessmentStatus;

    @TableField("MATURITY_DATE")
    private Date maturityDate;

    @TableField("RISK_SCORE")
    private Integer riskScore;

    @TableField("RISK_FACTORS")
    private String riskFactors;

    @TableField("RISK_SUGGESTIONS")
    private String riskSuggestions;

    @TableField("ASSESSOR_ID")
    private Long assessorId;

    @TableField("ASSESSOR_NAME")
    private String assessorName;

    @TableField("ASSESSMENT_DATE")
    private Date assessmentDate;

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


    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getRiskType() { return riskType; }
    public void setRiskType(String riskType) { this.riskType = riskType; }
    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }
    public String getAssessmentStatus() { return assessmentStatus; }
    public void setAssessmentStatus(String assessmentStatus) { this.assessmentStatus = assessmentStatus; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }
    public String getRiskFactors() { return riskFactors; }
    public void setRiskFactors(String riskFactors) { this.riskFactors = riskFactors; }
    public String getRiskSuggestions() { return riskSuggestions; }
    public void setRiskSuggestions(String riskSuggestions) { this.riskSuggestions = riskSuggestions; }
    public Long getAssessorId() { return assessorId; }
    public void setAssessorId(Long assessorId) { this.assessorId = assessorId; }
    public String getAssessorName() { return assessorName; }
    public void setAssessorName(String assessorName) { this.assessorName = assessorName; }
    public Date getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(Date assessmentDate) { this.assessmentDate = assessmentDate; }
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
