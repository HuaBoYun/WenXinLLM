package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品风险预警实体类
 * 对应数据库表：TBL_RISK_ALERT
 *
 * @author 华博云开发团队
 * @since 2026-02-28
 */
@TableName("TBL_RISK_ALERT")
public class TblBillRiskAlert implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ALERT_ID", type = IdType.INPUT)
    private Long alertId;

    @TableField("ALERT_TYPE")
    private String alertType;

    @TableField("ALERT_LEVEL")
    private String alertLevel;

    @TableField("PRODUCT_ID")
    private Long productId;

    @TableField("PRODUCT_NAME")
    private String productName;

    @TableField("RISK_CONTROL_ID")
    private Long riskControlId;

    @TableField("RISK_CONTROL_CODE")
    private String riskControlCode;

    @TableField("RISK_CONTROL_NAME")
    private String riskControlName;

    @TableField("THRESHOLD_VALUE")
    private BigDecimal thresholdValue;

    @TableField("CURRENT_VALUE")
    private BigDecimal currentValue;

    @TableField("ALERT_STATUS")
    private String alertStatus;

    @TableField("ALERT_TIME")
    private Date alertTime;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("HANDLER")
    private String handler;

    @TableField("HANDLE_TIME")
    private Date handleTime;

    @TableField("HANDLE_REMARK")
    private String handleRemark;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("CREATE_BY")
    private String createBy;

    // Getter and Setter methods

    public Long getAlertId() { return alertId; }
    public void setAlertId(Long alertId) { this.alertId = alertId; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Long getRiskControlId() { return riskControlId; }
    public void setRiskControlId(Long riskControlId) { this.riskControlId = riskControlId; }

    public String getRiskControlCode() { return riskControlCode; }
    public void setRiskControlCode(String riskControlCode) { this.riskControlCode = riskControlCode; }

    public String getRiskControlName() { return riskControlName; }
    public void setRiskControlName(String riskControlName) { this.riskControlName = riskControlName; }

    public BigDecimal getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(BigDecimal thresholdValue) { this.thresholdValue = thresholdValue; }

    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }

    public String getAlertStatus() { return alertStatus; }
    public void setAlertStatus(String alertStatus) { this.alertStatus = alertStatus; }

    public Date getAlertTime() { return alertTime; }
    public void setAlertTime(Date alertTime) { this.alertTime = alertTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHandler() { return handler; }
    public void setHandler(String handler) { this.handler = handler; }

    public Date getHandleTime() { return handleTime; }
    public void setHandleTime(Date handleTime) { this.handleTime = handleTime; }

    public String getHandleRemark() { return handleRemark; }
    public void setHandleRemark(String handleRemark) { this.handleRemark = handleRemark; }

    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
}
