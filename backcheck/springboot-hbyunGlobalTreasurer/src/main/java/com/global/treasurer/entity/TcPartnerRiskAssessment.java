package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合作伙伴风险评估实体类
 * 
 * @author AI Assistant
 * @date 2026-03-05
 */
@TableName("TC_PARTNER_RISK_ASSESSMENT")
public class TcPartnerRiskAssessment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @TableField("ID")
    private String id;

    @TableField("PARTNER_ID")
    private String partnerId;

    @TableField("ASSESSMENT_TYPE")
    private String assessmentType;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("RISK_SCORE")
    private BigDecimal riskScore;

    @TableField("RISK_STATUS")
    private String riskStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("LAST_ASSESSMENT_DATE")
    private Date lastAssessmentDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("NEXT_ASSESSMENT_DATE")
    private Date nextAssessmentDate;

    @TableField("ASSESSMENT_DESCRIPTION")
    private String assessmentDescription;

    @TableField("CONTROL_MEASURES")
    private String controlMeasures;

    @TableField("REMARKS")
    private String remarks;

    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("VERSION_NO")
    private Integer versionNo;

    // 关联查询字段
    @TableField(exist = false)
    private String partnerCode;

    @TableField(exist = false)
    private String partnerName;

    @TableField(exist = false)
    private String partnerType;

    // Getter and Setter methods
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPartnerId() { return partnerId; }
    public void setPartnerId(String partnerId) { this.partnerId = partnerId; }
    public String getAssessmentType() { return assessmentType; }
    public void setAssessmentType(String assessmentType) { this.assessmentType = assessmentType; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public BigDecimal getRiskScore() { return riskScore; }
    public void setRiskScore(BigDecimal riskScore) { this.riskScore = riskScore; }
    public String getRiskStatus() { return riskStatus; }
    public void setRiskStatus(String riskStatus) { this.riskStatus = riskStatus; }
    public Date getLastAssessmentDate() { return lastAssessmentDate; }
    public void setLastAssessmentDate(Date lastAssessmentDate) { this.lastAssessmentDate = lastAssessmentDate; }
    public Date getNextAssessmentDate() { return nextAssessmentDate; }
    public void setNextAssessmentDate(Date nextAssessmentDate) { this.nextAssessmentDate = nextAssessmentDate; }
    public String getAssessmentDescription() { return assessmentDescription; }
    public void setAssessmentDescription(String assessmentDescription) { this.assessmentDescription = assessmentDescription; }
    public String getControlMeasures() { return controlMeasures; }
    public void setControlMeasures(String controlMeasures) { this.controlMeasures = controlMeasures; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }
    public String getPartnerCode() { return partnerCode; }
    public void setPartnerCode(String partnerCode) { this.partnerCode = partnerCode; }
    public String getPartnerName() { return partnerName; }
    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
    public String getPartnerType() { return partnerType; }
    public void setPartnerType(String partnerType) { this.partnerType = partnerType; }
}

