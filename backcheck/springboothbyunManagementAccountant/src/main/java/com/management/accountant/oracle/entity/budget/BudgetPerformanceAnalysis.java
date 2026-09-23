package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算绩效分析实体
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("budget_performance_analysis")
public class BudgetPerformanceAnalysis {

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
     * 预算ID
     */
    private String budgetId;

    /**
     * 预算年度
     */
    private Integer budgetYear;

    /**
     * 预算期间
     */
    private String budgetPeriod;

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
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 执行率(%)
     */
    private BigDecimal executionRate;

    /**
     * 完成率(%)
     */
    private BigDecimal completionRate;

    /**
     * 绩效得分
     */
    private BigDecimal performanceScore;

    /**
     * 绩效等级(excellent-优秀, good-良好, average-一般, poor-较差)
     */
    private String performanceLevel;

    /**
     * 目标达成情况
     */
    private String targetAchievement;

    /**
     * 关键绩效指标(JSON格式)
     */
    private String kpiData;

    /**
     * 绩效分析说明
     */
    private String analysisDescription;

    /**
     * 优势分析
     */
    private String strengthAnalysis;

    /**
     * 劣势分析
     */
    private String weaknessAnalysis;

    /**
     * 改进措施
     */
    private String improvementMeasures;

    /**
     * 下期目标
     */
    private String nextPeriodTarget;

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

