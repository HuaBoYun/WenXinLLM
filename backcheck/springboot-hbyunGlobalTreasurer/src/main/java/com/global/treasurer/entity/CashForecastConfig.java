package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
import java.util.Date;

/**
 * 现金流预测配置实体类
 * 对应表：TBL_CASH_FORECAST_CONFIG
 *
 * @author AI Developer
 * @date 2025-01-15
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_CASH_FORECAST_CONFIG")
public class CashForecastConfig {
    @TableId(type = IdType.INPUT)
    @TableField("CONFIG_ID")
    private Long configId;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("HISTORICAL_PERIOD")
    private String historicalPeriod;

    @TableField("SEASONAL_ADJUSTMENT")
    private Integer seasonalAdjustment;

    @TableField("OUTLIER_HANDLING")
    private Integer outlierHandling;

    @TableField("BUSINESS_PLAN_WEIGHT")
    private Integer businessPlanWeight;

    @TableField("UPDATE_FREQUENCY")
    private String updateFrequency;

    @TableField("ALERT_THRESHOLD")
    private Integer alertThreshold;

    @TableField("START_DATE")
    private Date startDate;

    @TableField("FORECAST_DAYS")
    private Integer forecastDays;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField("CREATED_BY")
    private Long createdBy;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_BY")
    private Long updatedBy;

    @TableField("UPDATED_TIME")
    private Date updatedTime;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getConfigId() { return configId; }
    public void setConfigId(Long configId) { this.configId = configId; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public String getHistoricalPeriod() { return historicalPeriod; }
    public void setHistoricalPeriod(String historicalPeriod) { this.historicalPeriod = historicalPeriod; }
    public Integer getSeasonalAdjustment() { return seasonalAdjustment; }
    public void setSeasonalAdjustment(Integer seasonalAdjustment) { this.seasonalAdjustment = seasonalAdjustment; }
    public Integer getOutlierHandling() { return outlierHandling; }
    public void setOutlierHandling(Integer outlierHandling) { this.outlierHandling = outlierHandling; }
    public Integer getBusinessPlanWeight() { return businessPlanWeight; }
    public void setBusinessPlanWeight(Integer businessPlanWeight) { this.businessPlanWeight = businessPlanWeight; }
    public String getUpdateFrequency() { return updateFrequency; }
    public void setUpdateFrequency(String updateFrequency) { this.updateFrequency = updateFrequency; }
    public Integer getAlertThreshold() { return alertThreshold; }
    public void setAlertThreshold(Integer alertThreshold) { this.alertThreshold = alertThreshold; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Integer getForecastDays() { return forecastDays; }
    public void setForecastDays(Integer forecastDays) { this.forecastDays = forecastDays; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
}
