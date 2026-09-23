package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 风险识别实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_RISK_IDENTIFICATION")
public class TblRiskIdentification implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 风险ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long riskId;

    /** 风险编号 */
    private String riskCode;

    /** 风险名称 */
    private String riskName;

    /** 风险类型 */
    private String riskType;

    /** 风险等级(LOW/MEDIUM/HIGH/CRITICAL) */
    private String riskLevel;

    /** 影响程度(MINOR/MODERATE/MAJOR/CATASTROPHIC) */
    private String impactLevel;

    /** 发生概率(0-1) */
    private BigDecimal probability;

    /** 风险值 */
    private BigDecimal riskValue;

    /** 风险描述 */
    private String riskDescription;

    /** 风险来源(INTERNAL/EXTERNAL) */
    private String riskSource;

    /** 业务领域 */
    private String businessArea;

    /** 关联产品 */
    private String relatedProduct;

    /** 关联交易对手 */
    private String relatedCounterparty;

    /** 触发条件 */
    private String triggerConditions;

    /** 识别人 */
    private String identifier;

    /** 识别方法 */
    private String identificationMethod;

    /** 识别依据 */
    private String identificationBasis;

    /** 确认人 */
    private String confirmer;

    /** 识别日期 */
    private Date identificationDate;

    /** 识别备注 */
    private String identificationRemark;

    /** 状态(PENDING/IDENTIFIED/ASSESSING/ASSESSED/CLOSED) */
    private String status;

    /** 组织ID */
    private Long orgId;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 创建人 */
    private String createBy;

    /** 更新人 */
    private String updateBy;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRiskId() { return riskId; }
    public void setRiskId(Long riskId) { this.riskId = riskId; }
    public String getRiskCode() { return riskCode; }
    public void setRiskCode(String riskCode) { this.riskCode = riskCode; }
    public String getRiskName() { return riskName; }
    public void setRiskName(String riskName) { this.riskName = riskName; }
    public String getRiskType() { return riskType; }
    public void setRiskType(String riskType) { this.riskType = riskType; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getImpactLevel() { return impactLevel; }
    public void setImpactLevel(String impactLevel) { this.impactLevel = impactLevel; }
    public BigDecimal getProbability() { return probability; }
    public void setProbability(BigDecimal probability) { this.probability = probability; }
    public BigDecimal getRiskValue() { return riskValue; }
    public void setRiskValue(BigDecimal riskValue) { this.riskValue = riskValue; }
    public String getRiskDescription() { return riskDescription; }
    public void setRiskDescription(String riskDescription) { this.riskDescription = riskDescription; }
    public String getRiskSource() { return riskSource; }
    public void setRiskSource(String riskSource) { this.riskSource = riskSource; }
    public String getBusinessArea() { return businessArea; }
    public void setBusinessArea(String businessArea) { this.businessArea = businessArea; }
    public String getRelatedProduct() { return relatedProduct; }
    public void setRelatedProduct(String relatedProduct) { this.relatedProduct = relatedProduct; }
    public String getRelatedCounterparty() { return relatedCounterparty; }
    public void setRelatedCounterparty(String relatedCounterparty) { this.relatedCounterparty = relatedCounterparty; }
    public String getTriggerConditions() { return triggerConditions; }
    public void setTriggerConditions(String triggerConditions) { this.triggerConditions = triggerConditions; }
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getIdentificationMethod() { return identificationMethod; }
    public void setIdentificationMethod(String identificationMethod) { this.identificationMethod = identificationMethod; }
    public String getIdentificationBasis() { return identificationBasis; }
    public void setIdentificationBasis(String identificationBasis) { this.identificationBasis = identificationBasis; }
    public String getConfirmer() { return confirmer; }
    public void setConfirmer(String confirmer) { this.confirmer = confirmer; }
    public Date getIdentificationDate() { return identificationDate; }
    public void setIdentificationDate(Date identificationDate) { this.identificationDate = identificationDate; }
    public String getIdentificationRemark() { return identificationRemark; }
    public void setIdentificationRemark(String identificationRemark) { this.identificationRemark = identificationRemark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
}
