package com.global.treasurer.financialProductDefinition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品风控规则实体类
 * 对应数据库表：TBL_PRODUCT_RISK_CONTROL
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_PRODUCT_RISK_CONTROL")
public class TblProductRiskControl implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "RISK_CONTROL_ID", type = IdType.INPUT)
    private Long riskControlId;

    @TableField("RISK_CONTROL_CODE")
    private String riskControlCode;

    @TableField("RISK_CONTROL_NAME")
    private String riskControlName;

    @TableField("RISK_TYPE")
    private String riskType;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("CONTROL_MEASURE")
    private String controlMeasure;

    @TableField("WARNING_THRESHOLD")
    private BigDecimal warningThreshold;

    @TableField("STOP_LOSS_THRESHOLD")
    private BigDecimal stopLossThreshold;

    @TableField("MONITORING_FREQUENCY")
    private String monitoringFrequency;

    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("ORG_ID")
    private Long orgId;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRiskControlId() { return riskControlId; }
    public void setRiskControlId(Long riskControlId) { this.riskControlId = riskControlId; }
    public String getRiskControlCode() { return riskControlCode; }
    public void setRiskControlCode(String riskControlCode) { this.riskControlCode = riskControlCode; }
    public String getRiskControlName() { return riskControlName; }
    public void setRiskControlName(String riskControlName) { this.riskControlName = riskControlName; }
    public String getRiskType() { return riskType; }
    public void setRiskType(String riskType) { this.riskType = riskType; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getControlMeasure() { return controlMeasure; }
    public void setControlMeasure(String controlMeasure) { this.controlMeasure = controlMeasure; }
    public BigDecimal getWarningThreshold() { return warningThreshold; }
    public void setWarningThreshold(BigDecimal warningThreshold) { this.warningThreshold = warningThreshold; }
    public BigDecimal getStopLossThreshold() { return stopLossThreshold; }
    public void setStopLossThreshold(BigDecimal stopLossThreshold) { this.stopLossThreshold = stopLossThreshold; }
    public String getMonitoringFrequency() { return monitoringFrequency; }
    public void setMonitoringFrequency(String monitoringFrequency) { this.monitoringFrequency = monitoringFrequency; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
}
