package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算滚动预测实体
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("budget_rolling_forecast")
public class BudgetRollingForecast {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 预测编码
     */
    private String forecastCode;

    /**
     * 预测名称
     */
    private String forecastName;

    /**
     * 预算ID
     */
    private String budgetId;

    /**
     * 预算年度
     */
    private Integer budgetYear;

    /**
     * 当前期间
     */
    private String currentPeriod;

    /**
     * 滚动周期(月数)
     */
    private Integer rollingPeriods;

    /**
     * 组织ID
     */
    private String organizationId;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 预算科目ID
     */
    private String accountId;

    /**
     * 预算科目名称
     */
    private String accountName;

    /**
     * 历史实际数据(JSON格式)
     */
    private String historicalActualData;

    /**
     * 滚动预测数据(JSON格式)
     */
    private String rollingForecastData;

    /**
     * 预测总金额
     */
    private BigDecimal totalForecastAmount;

    /**
     * 滚动频率(monthly-月度, quarterly-季度)
     */
    private String rollingFrequency;

    /**
     * 预测方法(trend_based-趋势法, driver_based-驱动因素法)
     */
    private String forecastMethod;

    /**
     * 关键驱动因素(JSON格式)
     */
    private String keyDrivers;

    /**
     * 预测假设
     */
    private String forecastAssumptions;

    /**
     * 预测准确度(%)
     */
    private BigDecimal forecastAccuracy;

    /**
     * 预测分析说明
     */
    private String analysisDescription;

    /**
     * 调整建议
     */
    private String adjustmentSuggestion;

    /**
     * 版本号
     */
    private Integer versionNumber;

    /**
     * 是否最新版本(0-否, 1-是)
     */
    private Integer isLatestVersion;

    /**
     * 预测状态(draft-草稿, forecasting-预测中, completed-已完成, archived-已归档)
     */
    private String forecastStatus;

    /**
     * 预测人
     */
    private String forecastBy;

    /**
     * 预测时间
     */
    private Date forecastTime;

    /**
     * 审核人
     */
    private String reviewedBy;

    /**
     * 审核时间
     */
    private Date reviewedTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}

