package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目经营管理表
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_project_operations")
public class ProjectOperations implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 经营编号
     */
    @TableField("operations_no")
    private String operationsNo;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 经营类型(1:收入管理,2:成本管理,3:利润分析,4:现金流管理,5:风险控制,6:绩效评估)
     */
    @TableField("operations_type")
    private Integer operationsType;

    /**
     * 经营期间
     */
    @TableField("operations_period")
    private String operationsPeriod;

    /**
     * 合同金额
     */
    @TableField("contract_amount")
    private BigDecimal contractAmount;

    /**
     * 预算收入
     */
    @TableField("budget_revenue")
    private BigDecimal budgetRevenue;

    /**
     * 实际收入
     */
    @TableField("actual_revenue")
    private BigDecimal actualRevenue;

    /**
     * 预算成本
     */
    @TableField("budget_cost")
    private BigDecimal budgetCost;

    /**
     * 实际成本
     */
    @TableField("actual_cost")
    private BigDecimal actualCost;

    /**
     * 预算利润
     */
    @TableField("budget_profit")
    private BigDecimal budgetProfit;

    /**
     * 实际利润
     */
    @TableField("actual_profit")
    private BigDecimal actualProfit;

    /**
     * 利润率(%)
     */
    @TableField("profit_rate")
    private BigDecimal profitRate;

    /**
     * 毛利率(%)
     */
    @TableField("gross_profit_rate")
    private BigDecimal grossProfitRate;

    /**
     * 净利率(%)
     */
    @TableField("net_profit_rate")
    private BigDecimal netProfitRate;

    /**
     * 现金流入
     */
    @TableField("cash_inflow")
    private BigDecimal cashInflow;

    /**
     * 现金流出
     */
    @TableField("cash_outflow")
    private BigDecimal cashOutflow;

    /**
     * 净现金流
     */
    @TableField("net_cash_flow")
    private BigDecimal netCashFlow;

    /**
     * 应收账款
     */
    @TableField("accounts_receivable")
    private BigDecimal accountsReceivable;

    /**
     * 应付账款
     */
    @TableField("accounts_payable")
    private BigDecimal accountsPayable;

    /**
     * 资产总额
     */
    @TableField("total_assets")
    private BigDecimal totalAssets;

    /**
     * 负债总额
     */
    @TableField("total_liabilities")
    private BigDecimal totalLiabilities;

    /**
     * 净资产
     */
    @TableField("net_assets")
    private BigDecimal netAssets;

    /**
     * 资产负债率(%)
     */
    @TableField("debt_ratio")
    private BigDecimal debtRatio;

    /**
     * 流动比率
     */
    @TableField("current_ratio")
    private BigDecimal currentRatio;

    /**
     * 速动比率
     */
    @TableField("quick_ratio")
    private BigDecimal quickRatio;

    /**
     * 投资回报率(%)
     */
    @TableField("roi")
    private BigDecimal roi;

    /**
     * 净现值(NPV)
     */
    @TableField("npv")
    private BigDecimal npv;

    /**
     * 内部收益率(%)
     */
    @TableField("irr")
    private BigDecimal irr;

    /**
     * 回收期(月)
     */
    @TableField("payback_period")
    private Integer paybackPeriod;

    /**
     * 风险等级(1:低,2:中,3:高,4:极高)
     */
    @TableField("risk_level")
    private Integer riskLevel;

    /**
     * 风险评分
     */
    @TableField("risk_score")
    private BigDecimal riskScore;

    /**
     * 绩效评分
     */
    @TableField("performance_score")
    private BigDecimal performanceScore;

    /**
     * 经营状态(1:正常,2:预警,3:异常,4:停止)
     */
    @TableField("operations_status")
    private Integer operationsStatus;

    /**
     * 负责人ID
     */
    @TableField("manager_id")
    private Long managerId;

    /**
     * 负责人姓名
     */
    @TableField("manager_name")
    private String managerName;

    /**
     * 分析报告
     */
    @TableField("analysis_report")
    private String analysisReport;

    /**
     * 改进建议
     */
    @TableField("improvement_suggestions")
    private String improvementSuggestions;

    /**
     * 风险提示
     */
    @TableField("risk_warnings")
    private String riskWarnings;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;

    /**
     * 删除标志(0:未删除,1:已删除)
     */
    @TableField("del_flag")
    private Integer delFlag;

    /**
     * 版本号
     */
    @TableField("version")
    private Integer version;

    /**
     * 部门ID
     */
    @TableField("department_id")
    private Long departmentId;

    /**
     * 部门名称
     */
    @TableField("department_name")
    private String departmentName;

    /**
     * 组织ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @TableField("organization_name")
    private String organizationName;

    // ==================== 业务方法 ====================

    /**
     * 获取经营类型名称
     *
     * @return 经营类型名称
     */
    public String getOperationsTypeName() {
        if (operationsType == null) {
            return "";
        }
        switch (operationsType) {
            case 1:
                return "收入管理";
            case 2:
                return "成本管理";
            case 3:
                return "利润分析";
            case 4:
                return "现金流管理";
            case 5:
                return "风险控制";
            case 6:
                return "绩效评估";
            default:
                return "未知类型";
        }
    }

    /**
     * 获取风险等级名称
     *
     * @return 风险等级名称
     */
    public String getRiskLevelName() {
        if (riskLevel == null) {
            return "";
        }
        switch (riskLevel) {
            case 1:
                return "低";
            case 2:
                return "中";
            case 3:
                return "高";
            case 4:
                return "极高";
            default:
                return "未知等级";
        }
    }

    /**
     * 获取经营状态名称
     *
     * @return 经营状态名称
     */
    public String getOperationsStatusName() {
        if (operationsStatus == null) {
            return "";
        }
        switch (operationsStatus) {
            case 1:
                return "正常";
            case 2:
                return "预警";
            case 3:
                return "异常";
            case 4:
                return "停止";
            default:
                return "未知状态";
        }
    }

    /**
     * 计算收入完成率
     *
     * @return 收入完成率
     */
    public BigDecimal getRevenueCompletionRate() {
        if (budgetRevenue == null || budgetRevenue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (actualRevenue == null) {
            return BigDecimal.ZERO;
        }
        return actualRevenue.divide(budgetRevenue, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
    }

    /**
     * 计算成本控制率
     *
     * @return 成本控制率
     */
    public BigDecimal getCostControlRate() {
        if (budgetCost == null || budgetCost.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (actualCost == null) {
            return BigDecimal.ZERO;
        }
        return actualCost.divide(budgetCost, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
    }

    /**
     * 计算利润完成率
     *
     * @return 利润完成率
     */
    public BigDecimal getProfitCompletionRate() {
        if (budgetProfit == null || budgetProfit.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (actualProfit == null) {
            return BigDecimal.ZERO;
        }
        return actualProfit.divide(budgetProfit, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
    }

    /**
     * 是否盈利
     *
     * @return 是否盈利
     */
    public boolean isProfitable() {
        return actualProfit != null && actualProfit.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 是否亏损
     *
     * @return 是否亏损
     */
    public boolean isLoss() {
        return actualProfit != null && actualProfit.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * 是否高风险
     *
     * @return 是否高风险
     */
    public boolean isHighRisk() {
        return riskLevel != null && riskLevel >= 3;
    }

    /**
     * 是否需要预警
     *
     * @return 是否需要预警
     */
    public boolean needsWarning() {
        return operationsStatus != null && operationsStatus >= 2;
    }

    /**
     * 获取经营健康度
     *
     * @return 经营健康度描述
     */
    public String getOperationsHealth() {
        if (operationsStatus == null) {
            return "未知";
        }
        
        StringBuilder health = new StringBuilder();
        health.append(getOperationsStatusName());
        
        if (isProfitable()) {
            health.append("（盈利）");
        } else if (isLoss()) {
            health.append("（亏损）");
        }
        
        if (isHighRisk()) {
            health.append("（高风险）");
        }
        
        return health.toString();
    }

    /**
     * 获取财务指标摘要
     *
     * @return 财务指标摘要
     */
    public String getFinancialSummary() {
        StringBuilder summary = new StringBuilder();
        
        if (profitRate != null) {
            summary.append("利润率：").append(profitRate).append("%");
        }
        
        if (grossProfitRate != null) {
            if (summary.length() > 0) summary.append("，");
            summary.append("毛利率：").append(grossProfitRate).append("%");
        }
        
        if (roi != null) {
            if (summary.length() > 0) summary.append("，");
            summary.append("投资回报率：").append(roi).append("%");
        }
        
        return summary.toString();
    }
}
