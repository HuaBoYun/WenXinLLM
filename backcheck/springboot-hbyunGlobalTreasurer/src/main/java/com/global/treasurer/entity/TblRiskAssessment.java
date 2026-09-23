package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 风险评估实体类
 * 对应已存在的 TBL_RISK_ASSESSMENT 表
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_RISK_ASSESSMENT")
public class TblRiskAssessment implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 风险ID */
    @TableId(type = IdType.ASSIGN_UUID)
    private String riskId;

    /** 企业ID */
    private String enterpriseId;

    /** 风险类别 */
    private String riskCategory;

    /** 风险项 */
    private String riskItem;

    /** 风险等级 */
    private String riskLevel;

    /** 风险描述 */
    private String riskDescription;

    /** 影响程度 */
    private String impactDegree;

    /** 发生概率 */
    private String occurrenceProbability;

    /** 应对措施 */
    private String countermeasures;

    /** 评估日期 */
    private Date assessmentDate;

    /** 状态 */
    private String status;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 数据源类型 */
    private Integer fDatasourcetype;

    /** 导入批次 */
    private String fImportbatches;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getRiskId() { return riskId; }
    public void setRiskId(String riskId) { this.riskId = riskId; }
    public String getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(String enterpriseId) { this.enterpriseId = enterpriseId; }
    public String getRiskCategory() { return riskCategory; }
    public void setRiskCategory(String riskCategory) { this.riskCategory = riskCategory; }
    public String getRiskItem() { return riskItem; }
    public void setRiskItem(String riskItem) { this.riskItem = riskItem; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getRiskDescription() { return riskDescription; }
    public void setRiskDescription(String riskDescription) { this.riskDescription = riskDescription; }
    public String getImpactDegree() { return impactDegree; }
    public void setImpactDegree(String impactDegree) { this.impactDegree = impactDegree; }
    public String getOccurrenceProbability() { return occurrenceProbability; }
    public void setOccurrenceProbability(String occurrenceProbability) { this.occurrenceProbability = occurrenceProbability; }
    public String getCountermeasures() { return countermeasures; }
    public void setCountermeasures(String countermeasures) { this.countermeasures = countermeasures; }
    public Date getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(Date assessmentDate) { this.assessmentDate = assessmentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Integer getFDatasourcetype() { return fDatasourcetype; }
    public void setFDatasourcetype(Integer fDatasourcetype) { this.fDatasourcetype = fDatasourcetype; }
    public String getFImportbatches() { return fImportbatches; }
    public void setFImportbatches(String fImportbatches) { this.fImportbatches = fImportbatches; }

}
