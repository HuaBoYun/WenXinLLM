package com.global.treasurer.entity.derivatives;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 衍生品预警实体类
 *
 * @author AI Developer
 * @date 2026-01-21
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_DERIVATIVES_ALERT")
public class TblDerivativesAlert implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ALERT_ID", type = IdType.AUTO)
    private Long alertId;

    /**
     * 预警名称
     */
    @TableField(value = "ALERT_NAME")
    private String alertName;

    /**
     * 预警类型(RISK_LIMIT-风险限额, VAR-风险价值, MARGIN-保证金, MARKET-市场变化)
     */
    @TableField(value = "ALERT_TYPE")
    private String alertType;

    /**
     * 监控指标
     */
    @TableField(value = "MONITORING_METRIC")
    private String monitoringMetric;

    /**
     * 预警阈值
     */
    @TableField(value = "THRESHOLD")
    private BigDecimal threshold;

    /**
     * 当前值
     */
    @TableField(value = "CURRENT_VALUE")
    private BigDecimal currentValue;

    /**
     * 预警级别(LOW-低, MEDIUM-中, HIGH-高, CRITICAL-严重)
     */
    @TableField(value = "ALERT_LEVEL")
    private String alertLevel;

    /**
     * 状态(ACTIVE-活跃, TRIGGERED-已触发, PROCESSED-已处理, IGNORED-已忽略)
     */
    @TableField(value = "STATUS")
    private String status;

    /**
     * 通知方式
     */
    @TableField(value = "NOTIFICATION_METHOD")
    private String notificationMethod;

    /**
     * 触发时间
     */
    @TableField(value = "TRIGGER_TIME")
    private Date triggerTime;

    /**
     * 描述
     */
    @TableField(value = "DESCRIPTION")
    private String description;

    /**
     * 备注
     */
    @TableField(value = "REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private Date createTime;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATE_BY")
    private Long createBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新人ID
     */
    @TableField(value = "UPDATE_BY")
    private Long updateBy;

    /**
     * 删除标志(0-正常, 1-删除)
     */
    @TableField(value = "DEL_FLAG")
    private Integer delFlag;

    /**
     * 机构ID
     */
    @TableField(value = "ORG_ID")
    private Long orgId;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getAlertId() { return alertId; }
    public void setAlertId(Long alertId) { this.alertId = alertId; }
    public String getAlertName() { return alertName; }
    public void setAlertName(String alertName) { this.alertName = alertName; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getMonitoringMetric() { return monitoringMetric; }
    public void setMonitoringMetric(String monitoringMetric) { this.monitoringMetric = monitoringMetric; }
    public BigDecimal getThreshold() { return threshold; }
    public void setThreshold(BigDecimal threshold) { this.threshold = threshold; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotificationMethod() { return notificationMethod; }
    public void setNotificationMethod(String notificationMethod) { this.notificationMethod = notificationMethod; }
    public Date getTriggerTime() { return triggerTime; }
    public void setTriggerTime(Date triggerTime) { this.triggerTime = triggerTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Long getCreateBy() { return createBy; }
    public void setCreateBy(Long createBy) { this.createBy = createBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Long getUpdateBy() { return updateBy; }
    public void setUpdateBy(Long updateBy) { this.updateBy = updateBy; }
    public Integer getDelFlag() { return delFlag; }
    public void setDelFlag(Integer delFlag) { this.delFlag = delFlag; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

}
