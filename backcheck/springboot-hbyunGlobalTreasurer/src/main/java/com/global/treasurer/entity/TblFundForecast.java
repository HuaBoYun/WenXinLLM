package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金预测实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FUND_FORECAST")
public class TblFundForecast implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 预测ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long forecastId;

    /** 预测编号 */
    private String forecastNo;

    /** 预测类型(INCOME-收入预测,EXPENSE-支出预测,CASHFLOW-现金流预测,INVESTMENT-投资预测,FINANCING-融资预测) */
    private String forecastType;

    /** 预测方法(HISTORICAL-历史数据法,REGRESSION-回归分析法,SEASONAL-季节性分析,MOVING_AVERAGE-移动平均法,EXPONENTIAL-指数平滑法,MANUAL-人工预测) */
    private String forecastMethod;

    /** 预测日期 */
    private Date forecastDate;

    /** 预测金额 */
    private BigDecimal forecastAmount;

    /** 实际金额 */
    private BigDecimal actualAmount;

    /** 预测准确率 */
    private BigDecimal forecastAccuracy;

    /** 置信区间 */
    private BigDecimal confidenceInterval;

    /** 预测周期 */
    private String forecastPeriod;

    /** 数据源(SYSTEM-系统数据,MANUAL-手工录入,IMPORT-导入数据,API-接口数据) */
    private String dataSource;

    /** 说明 */
    private String description;

    /** 删除标志(0-正常,1-删除) */
    private Integer deleteFlag;

    /** 创建人 */
    private Long createdBy;

    /** 创建人姓名 */
    private String createdByName;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private Long updatedBy;

    /** 更新人姓名 */
    private String updatedByName;

    /** 更新时间 */
    private Date updatedTime;

    /** 组织ID */
    private Long orgId;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getForecastId() { return forecastId; }
    public void setForecastId(Long forecastId) { this.forecastId = forecastId; }
    public String getForecastNo() { return forecastNo; }
    public void setForecastNo(String forecastNo) { this.forecastNo = forecastNo; }
    public String getForecastType() { return forecastType; }
    public void setForecastType(String forecastType) { this.forecastType = forecastType; }
    public String getForecastMethod() { return forecastMethod; }
    public void setForecastMethod(String forecastMethod) { this.forecastMethod = forecastMethod; }
    public Date getForecastDate() { return forecastDate; }
    public void setForecastDate(Date forecastDate) { this.forecastDate = forecastDate; }
    public BigDecimal getForecastAmount() { return forecastAmount; }
    public void setForecastAmount(BigDecimal forecastAmount) { this.forecastAmount = forecastAmount; }
    public BigDecimal getActualAmount() { return actualAmount; }
    public void setActualAmount(BigDecimal actualAmount) { this.actualAmount = actualAmount; }
    public BigDecimal getForecastAccuracy() { return forecastAccuracy; }
    public void setForecastAccuracy(BigDecimal forecastAccuracy) { this.forecastAccuracy = forecastAccuracy; }
    public BigDecimal getConfidenceInterval() { return confidenceInterval; }
    public void setConfidenceInterval(BigDecimal confidenceInterval) { this.confidenceInterval = confidenceInterval; }
    public String getForecastPeriod() { return forecastPeriod; }
    public void setForecastPeriod(String forecastPeriod) { this.forecastPeriod = forecastPeriod; }
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
}
