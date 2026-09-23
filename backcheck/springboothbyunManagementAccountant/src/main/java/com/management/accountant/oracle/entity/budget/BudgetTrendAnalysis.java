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
 * 预算趋势分析实体
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_TREND_ANALYSIS")
public class BudgetTrendAnalysis {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 分析编码
     */
    @TableField("ANALYSIS_CODE")
    private String analysisCode;

    /**
     * 分析名称
     */
    @TableField("ANALYSIS_NAME")
    private String analysisName;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    /**
     * 组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 组织名称
     */
    @TableField("ORGANIZATION_NAME")
    private String organizationName;

    /**
     * 预算科目ID
     */
    @TableField("ACCOUNT_ID")
    private String accountId;

    /**
     * 预算科目名称
     */
    @TableField("ACCOUNT_NAME")
    private String accountName;

    /**
     * 趋势类型(monthly-月度趋势, quarterly-季度趋势, yearly-年度趋势)
     */
    @TableField("TREND_TYPE")
    private String trendType;

    /**
     * 开始期间
     */
    @TableField("START_PERIOD")
    private String startPeriod;

    /**
     * 结束期间
     */
    @TableField("END_PERIOD")
    private String endPeriod;

    /**
     * 趋势数据(JSON格式存储各期间数据)
     */
    @TableField("TREND_DATA")
    private String trendData;

    /**
     * 平均值
     */
    @TableField("AVERAGE_VALUE")
    private BigDecimal averageValue;

    /**
     * 最大值
     */
    @TableField("MAX_VALUE")
    private BigDecimal maxValue;

    /**
     * 最小值
     */
    @TableField("MIN_VALUE")
    private BigDecimal minValue;

    /**
     * 增长率(%)
     */
    @TableField("GROWTH_RATE")
    private BigDecimal growthRate;

    /**
     * 趋势方向(up-上升, down-下降, stable-稳定)
     */
    @TableField("TREND_DIRECTION")
    private String trendDirection;

    /**
     * 趋势强度(strong-强, moderate-中等, weak-弱)
     */
    @TableField("TREND_STRENGTH")
    private String trendStrength;

    /**
     * 趋势分析说明
     */
    @TableField("ANALYSIS_DESCRIPTION")
    private String analysisDescription;

    /**
     * 预测建议
     */
    @TableField("FORECAST_SUGGESTION")
    private String forecastSuggestion;

    /**
     * 分析结果
     */
    @TableField("ANALYSIS_RESULT")
    private String analysisResult;

    /**
     * 分析维度(department-部门, project-项目, cost_center-成本中心)
     */
    @TableField("ANALYSIS_DIMENSION")
    private String analysisDimension;

    /**
     * 分析状态(draft-草稿, analyzing-分析中, completed-已完成)
     */
    @TableField("ANALYSIS_STATUS")
    private String analysisStatus;

    /**
     * 分析人
     */
    @TableField("ANALYZED_BY")
    private String analyzedBy;

    /**
     * 分析时间
     */
    @TableField("ANALYZED_TIME")
    private Date analyzedTime;

    /**
     * 审核人
     */
    @TableField("REVIEWED_BY")
    private String reviewedBy;

    /**
     * 审核时间
     */
    @TableField("REVIEWED_TIME")
    private Date reviewedTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 删除标志（0-正常 1-已删除）
     */
    @TableLogic
    @TableField("DEL_FLAG")
    private Integer delFlag;
}

