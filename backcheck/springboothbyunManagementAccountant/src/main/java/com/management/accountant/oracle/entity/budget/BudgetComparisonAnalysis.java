package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算对比分析实体
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("budget_comparison_analysis")
public class BudgetComparisonAnalysis {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 分析编码
     */
    private String analysisCode;

    /**
     * 分析名称
     */
    private String analysisName;

    /**
     * 对比类型(year_over_year-同比, month_over_month-环比, budget_vs_actual-预算vs实际)
     */
    private String comparisonType;

    /**
     * 基准预算ID
     */
    private String baseBudgetId;

    /**
     * 基准预算年度
     */
    private Integer baseBudgetYear;

    /**
     * 基准预算期间
     */
    private String baseBudgetPeriod;

    /**
     * 对比预算ID
     */
    private String compareBudgetId;

    /**
     * 对比预算年度
     */
    private Integer compareBudgetYear;

    /**
     * 对比预算期间
     */
    private String compareBudgetPeriod;

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
     * 基准金额
     */
    private BigDecimal baseAmount;

    /**
     * 对比金额
     */
    private BigDecimal compareAmount;

    /**
     * 差异金额
     */
    private BigDecimal differenceAmount;

    /**
     * 差异率(%)
     */
    private BigDecimal differenceRate;

    /**
     * 对比结果(increase-增长, decrease-下降, stable-持平)
     */
    private String comparisonResult;

    /**
     * 对比分析说明
     */
    private String analysisDescription;

    /**
     * 差异原因分析
     */
    private String differenceReason;

    /**
     * 改进建议
     */
    private String improvementSuggestion;

    /**
     * 分析结果
     */
    private String analysisResult;

    /**
     * 分析维度(department-部门, project-项目, cost_center-成本中心)
     */
    private String analysisDimension;

    /**
     * 分析状态(draft-草稿, analyzing-分析中, completed-已完成)
     */
    private String analysisStatus;

    /**
     * 分析人
     */
    private String analyzedBy;

    /**
     * 分析时间
     */
    private Date analyzedTime;

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

