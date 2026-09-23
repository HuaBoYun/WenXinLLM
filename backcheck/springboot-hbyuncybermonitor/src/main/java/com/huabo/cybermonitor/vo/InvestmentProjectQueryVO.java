package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 投资项目查询参数VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestmentProjectQueryVO extends BaseVo {

    /**
     * 投资项目ID
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 投资方企业ID
     */
    private String investorEnterpriseId;

    /**
     * 投资方企业名称
     */
    private String investorEnterpriseName;

    /**
     * 被投资方企业ID
     */
    private String investeeEnterpriseId;

    /**
     * 被投资方企业名称
     */
    private String investeeEnterpriseName;

    /**
     * 投资类型
     */
    private String investmentType;

    /**
     * 投资方式
     */
    private String investmentMethod;

    /**
     * 投资行业
     */
    private String investmentIndustry;

    /**
     * 投资地区
     */
    private String investmentRegion;

    /**
     * 最小计划投资金额（万元）
     */
    private BigDecimal minPlannedInvestmentAmount;

    /**
     * 最大计划投资金额（万元）
     */
    private BigDecimal maxPlannedInvestmentAmount;

    /**
     * 最小实际投资金额（万元）
     */
    private BigDecimal minActualInvestmentAmount;

    /**
     * 最大实际投资金额（万元）
     */
    private BigDecimal maxActualInvestmentAmount;

    /**
     * 最小投资进度（%）
     */
    private BigDecimal minInvestmentProgress;

    /**
     * 最大投资进度（%）
     */
    private BigDecimal maxInvestmentProgress;

    /**
     * 最小预期收益率（%）
     */
    private BigDecimal minExpectedReturnRate;

    /**
     * 最大预期收益率（%）
     */
    private BigDecimal maxExpectedReturnRate;

    /**
     * 项目状态
     */
    private String projectStatus;

    /**
     * 投资阶段
     */
    private String investmentStage;

    /**
     * 风险等级
     */
    private String riskLevel;

    /**
     * 决策流程状态
     */
    private String decisionProcessStatus;

    /**
     * 审批状态
     */
    private String approvalStatus;

    /**
     * 审批机关
     */
    private String approvalAuthority;

    /**
     * 项目开始日期
     */
    private String projectStartDate;

    /**
     * 项目结束日期
     */
    private String projectEndDate;

    /**
     * 是否战略投资
     */
    private Boolean isStrategicInvestment;

    /**
     * 是否关联交易
     */
    private Boolean isRelatedTransaction;

    /**
     * 是否需要监管关注
     */
    private Boolean needRegulatoryAttention;

    /**
     * 项目负责人
     */
    private String projectManager;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 公司ID（穿透查询：查当前公司及所有下级公司的数据）
     */
    private String companyId;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向
     */
    private String sortDirection;
}
