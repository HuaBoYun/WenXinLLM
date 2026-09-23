package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算敏感性分析实体
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("budget_sensitivity_analysis")
public class BudgetSensitivityAnalysis {

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
     * 敏感变量名称
     */
    private String variableName;

    /**
     * 基准值
     */
    private BigDecimal baseValue;

    /**
     * 变动范围(%)
     */
    private BigDecimal changeRange;

    /**
     * 敏感性系数
     */
    private BigDecimal sensitivityCoefficient;

    /**
     * 敏感度等级(high-高, medium-中, low-低)
     */
    private String sensitivityLevel;

    /**
     * 测试数据(JSON格式,包含不同变动值对应的结果)
     */
    private String testData;

    /**
     * 临界点
     */
    private BigDecimal breakEvenPoint;

    /**
     * 影响程度分析
     */
    private String impactAnalysis;

    /**
     * 敏感性分析说明
     */
    private String analysisDescription;

    /**
     * 风险提示
     */
    private String riskWarning;

    /**
     * 控制建议
     */
    private String controlSuggestion;

    /**
     * 图表数据(JSON格式)
     */
    private String chartData;

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

