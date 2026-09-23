 package com.management.accountant.oracle.entity.budget;
 
 import com.baomidou.mybatisplus.annotation.IdType;
 import com.baomidou.mybatisplus.annotation.TableField;
 import com.baomidou.mybatisplus.annotation.TableId;
 import com.baomidou.mybatisplus.annotation.TableLogic;
 import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.Data;
 
 import java.math.BigDecimal;
 import java.util.Date;
 
 /**
  * 预算预测分析实体
  *
  * @author AI Agent
  * @date 2025-12-31
  */
 @Data
 @TableName("TBL_BUDGET_FORECAST_ANALYSIS")
 public class BudgetForecastAnalysis {
 
     @TableId(type = IdType.ASSIGN_UUID)
     private String id;
 
     /** 分析编码 */
     @TableField("ANALYSIS_CODE")
     private String analysisCode;
 
     /** 分析名称 */
     @TableField("ANALYSIS_NAME")
     private String analysisName;
 
     /** 预算ID */
     @TableField("BUDGET_ID")
     private String budgetId;
 
     /** 预算年度 */
     @TableField("BUDGET_YEAR")
     private Integer budgetYear;
 
     /** 组织ID */
     @TableField("ORGANIZATION_ID")
     private String organizationId;
 
     /** 组织名称 */
     @TableField("ORGANIZATION_NAME")
     private String organizationName;
 
     /** 预算科目ID */
     @TableField("ACCOUNT_ID")
     private String accountId;
 
     /** 预算科目名称 */
     @TableField("ACCOUNT_NAME")
     private String accountName;
 
     /** 预测类型(MONTHLY/QUARTERLY/ANNUAL) */
     @TableField("FORECAST_TYPE")
     private String forecastType;
 
     /** 预测方法(LINEAR_REGRESSION/MOVING_AVERAGE/EXPONENTIAL_SMOOTHING/ARIMA/NEURAL_NETWORK/ENSEMBLE/TREND_ANALYSIS) */
     @TableField("FORECAST_METHOD")
     private String forecastMethod;
 
     /** 预测模型名称 */
     @TableField("FORECAST_MODEL_NAME")
     private String forecastModelName;
 
     /** 预测期间 */
     @TableField("FORECAST_PERIOD")
     private String forecastPeriod;
 
     /** 预测日期 */
     @TableField("FORECAST_DATE")
     private String forecastDate;
 
     /** 预测金额 */
     @TableField("FORECAST_AMOUNT")
     private BigDecimal forecastAmount;
 
     /** 实际金额 */
     @TableField("ACTUAL_AMOUNT")
     private BigDecimal actualAmount;
 
     /** 历史值 */
     @TableField("HISTORICAL_VALUE")
     private BigDecimal historicalValue;
 
     /** 置信区间上限 */
     @TableField("UPPER_BOUND")
     private BigDecimal upperBound;
 
     /** 置信区间下限 */
     @TableField("LOWER_BOUND")
     private BigDecimal lowerBound;
 
     /** 置信度(%) */
     @TableField("CONFIDENCE_LEVEL")
     private BigDecimal confidenceLevel;
 
     /** 准确率(%) */
     @TableField("ACCURACY_RATE")
     private BigDecimal accuracyRate;
 
     /** 趋势(UP/DOWN/STABLE) */
     @TableField("TREND")
     private String trend;
 
     /** 预测粒度(MONTHLY/QUARTERLY/YEARLY) */
     @TableField("FORECAST_GRANULARITY")
     private String forecastGranularity;
 
     /** 置信区间 */
     @TableField("CONFIDENCE_INTERVAL")
     private String confidenceInterval;
 
     /** 分析状态(DRAFT/IN_PROGRESS/COMPLETED) */
     @TableField("ANALYSIS_STATUS")
     private String analysisStatus;
 
     /** 分析人 */
     @TableField("ANALYZED_BY")
     private String analyzedBy;
 
     /** 备注 */
     @TableField("REMARK")
     private String remark;
 
     /** 创建人 */
     @TableField("CREATE_BY")
     private String createBy;
 
     /** 创建时间 */
     @TableField("CREATE_TIME")
     private Date createTime;
 
     /** 更新人 */
     @TableField("UPDATE_BY")
     private String updateBy;
 
     /** 更新时间 */
     @TableField("UPDATE_TIME")
     private Date updateTime;
 
     /** 删除标志（0-正常 1-已删除） */
     @TableLogic
     @TableField("DEL_FLAG")
     private Integer delFlag;
 }
