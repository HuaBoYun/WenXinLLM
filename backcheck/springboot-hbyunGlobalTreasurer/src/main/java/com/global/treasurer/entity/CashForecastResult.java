package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
import java.math.BigDecimal;
import java.util.Date;

/**
 * 现金流预测结果实体类
 * 对应表：TBL_CASH_FORECAST_RESULT
 *
 * @author AI Developer
 * @date 2025-01-15
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_CASH_FORECAST_RESULT")
public class CashForecastResult {
    @TableId(type = IdType.INPUT)
    @TableField("FORECAST_ID")
    private Long forecastId;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("FORECAST_DATE")
    private Date forecastDate;

    @TableField("PERIOD")
    private String period;

    @TableField("MODEL")
    private String model;

    @TableField("CURRENCY_CODE")
    private String currencyCode;

    @TableField("CONFIDENCE")
    private Integer confidence;

    @TableField("TOTAL_INFLOW")
    private BigDecimal totalInflow;

    @TableField("TOTAL_OUTFLOW")
    private BigDecimal totalOutflow;

    @TableField("NET_CASHFLOW")
    private BigDecimal netCashflow;

    @TableField("FORECAST_DATA")
    private String forecastData;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField("CREATED_BY")
    private Long createdBy;

    @TableField("CREATED_TIME")
    private Date createdTime;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getForecastId() { return forecastId; }
    public void setForecastId(Long forecastId) { this.forecastId = forecastId; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Date getForecastDate() { return forecastDate; }
    public void setForecastDate(Date forecastDate) { this.forecastDate = forecastDate; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Integer getConfidence() { return confidence; }
    public void setConfidence(Integer confidence) { this.confidence = confidence; }
    public BigDecimal getTotalInflow() { return totalInflow; }
    public void setTotalInflow(BigDecimal totalInflow) { this.totalInflow = totalInflow; }
    public BigDecimal getTotalOutflow() { return totalOutflow; }
    public void setTotalOutflow(BigDecimal totalOutflow) { this.totalOutflow = totalOutflow; }
    public BigDecimal getNetCashflow() { return netCashflow; }
    public void setNetCashflow(BigDecimal netCashflow) { this.netCashflow = netCashflow; }
    public String getForecastData() { return forecastData; }
    public void setForecastData(String forecastData) { this.forecastData = forecastData; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}
