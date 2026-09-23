package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 授信评估实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_CREDIT_ASSESSMENT")
public class TblCreditAssessment implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 评估ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long assessmentId;

    /** 评估编号 */
    private String assessmentNo;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 评估类型(ANNUAL-年度评估,SPECIAL-专项评估) */
    private String assessmentType;

    /** 评估日期 */
    private Date assessmentDate;

    /** 评估人ID */
    private Long assessorId;

    /** 评估人姓名 */
    private String assessorName;

    /** 风险等级(HIGH-高,MEDIUM-中,LOW-低) */
    private String riskLevel;

    /** 信用评级 */
    private String creditRating;

    /** 财务评分 */
    private BigDecimal financialScore;

    /** 经营评分 */
    private BigDecimal operationalScore;

    /** 管理评分 */
    private BigDecimal managementScore;

    /** 总评分 */
    private BigDecimal totalScore;

    /** 评估结果 */
    private String assessmentResult;

    /** 建议授信额度 */
    private BigDecimal creditLimit;

    /** 评估状态(DRAFT-草稿,PENDING-待审批,APPROVED-已通过,REJECTED-已拒绝) */
    private String assessmentStatus;

    /** 审批日期 */
    private Date approvalDate;

    /** 审批人ID */
    private Long approverId;

    /** 审批人姓名 */
    private String approverName;

    /** 评估意见 */
    private String comments;

    /** 有效期至 */
    private Date validUntil;

    /** 下次评估日期 */
    private Date nextAssessmentDate;

    /** 删除标志 */
    private Integer deleteFlag;

    /** 创建时间 */
    private Date createdTime;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }
    public String getAssessmentNo() { return assessmentNo; }
    public void setAssessmentNo(String assessmentNo) { this.assessmentNo = assessmentNo; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getAssessmentType() { return assessmentType; }
    public void setAssessmentType(String assessmentType) { this.assessmentType = assessmentType; }
    public Date getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(Date assessmentDate) { this.assessmentDate = assessmentDate; }
    public Long getAssessorId() { return assessorId; }
    public void setAssessorId(Long assessorId) { this.assessorId = assessorId; }
    public String getAssessorName() { return assessorName; }
    public void setAssessorName(String assessorName) { this.assessorName = assessorName; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getCreditRating() { return creditRating; }
    public void setCreditRating(String creditRating) { this.creditRating = creditRating; }
    public BigDecimal getFinancialScore() { return financialScore; }
    public void setFinancialScore(BigDecimal financialScore) { this.financialScore = financialScore; }
    public BigDecimal getOperationalScore() { return operationalScore; }
    public void setOperationalScore(BigDecimal operationalScore) { this.operationalScore = operationalScore; }
    public BigDecimal getManagementScore() { return managementScore; }
    public void setManagementScore(BigDecimal managementScore) { this.managementScore = managementScore; }
    public BigDecimal getTotalScore() { return totalScore; }
    public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }
    public String getAssessmentResult() { return assessmentResult; }
    public void setAssessmentResult(String assessmentResult) { this.assessmentResult = assessmentResult; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    public String getAssessmentStatus() { return assessmentStatus; }
    public void setAssessmentStatus(String assessmentStatus) { this.assessmentStatus = assessmentStatus; }
    public Date getApprovalDate() { return approvalDate; }
    public void setApprovalDate(Date approvalDate) { this.approvalDate = approvalDate; }
    public Long getApproverId() { return approverId; }
    public void setApproverId(Long approverId) { this.approverId = approverId; }
    public String getApproverName() { return approverName; }
    public void setApproverName(String approverName) { this.approverName = approverName; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public Date getValidUntil() { return validUntil; }
    public void setValidUntil(Date validUntil) { this.validUntil = validUntil; }
    public Date getNextAssessmentDate() { return nextAssessmentDate; }
    public void setNextAssessmentDate(Date nextAssessmentDate) { this.nextAssessmentDate = nextAssessmentDate; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
