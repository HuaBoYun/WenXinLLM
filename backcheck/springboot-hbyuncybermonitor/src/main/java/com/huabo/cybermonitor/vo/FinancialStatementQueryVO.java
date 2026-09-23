package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 财务报表查询参数VO
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FinancialStatementQueryVO extends BaseVo {

    /**
     * 企业ID
     */
    private String enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 报表类型
     */
    private String statementType;

    /**
     * 报表期间类型
     */
    private String periodType;

    /**
     * 报表年度
     */
    private Integer reportYear;

    /**
     * 报表期间
     */
    private Integer reportPeriod;

    /**
     * 报表状态
     */
    private String statementStatus;

    /**
     * 审计状态
     */
    private String auditStatus;

    /**
     * 审计机构
     */
    private String auditFirm;

    /**
     * 审计意见
     */
    private String auditOpinion;

    /**
     * 报送状态
     */
    private String submissionStatus;

    /**
     * 报送机构
     */
    private String submissionOrganization;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 数据质量等级
     */
    private String dataQualityLevel;

    /**
     * 是否合并报表
     */
    private Boolean isConsolidated;

    /**
     * 会计准则
     */
    private String accountingStandards;

    /**
     * 货币单位
     */
    private String currency;

    /**
     * 是否需要监管关注
     */
    private Boolean needRegulatoryAttention;

    /**
     * 风险等级
     */
    private String riskLevel;

    /**
     * 异常标识
     */
    private Boolean hasAnomalies;

    /**
     * 合规检查结果
     */
    private String complianceCheckResult;

    /**
     * 最小数据完整性评分
     */
    private BigDecimal minDataCompletenessScore;

    /**
     * 最大数据完整性评分
     */
    private BigDecimal maxDataCompletenessScore;

    /**
     * 最小数据准确性评分
     */
    private BigDecimal minDataAccuracyScore;

    /**
     * 最大数据准确性评分
     */
    private BigDecimal maxDataAccuracyScore;

    /**
     * 最小数据及时性评分
     */
    private BigDecimal minDataTimelinessScore;

    /**
     * 最大数据及时性评分
     */
    private BigDecimal maxDataTimelinessScore;

    /**
     * 最小数据一致性评分
     */
    private BigDecimal minDataConsistencyScore;

    /**
     * 最大数据一致性评分
     */
    private BigDecimal maxDataConsistencyScore;

    /**
     * 开始年度
     */
    private Integer startYear;

    /**
     * 结束年度
     */
    private Integer endYear;

    /**
     * 开始期间
     */
    private Integer startPeriod;

    /**
     * 结束期间
     */
    private Integer endPeriod;

    /**
     * 编制人
     */
    private String preparedBy;

    /**
     * 复核人
     */
    private String reviewedBy;

    /**
     * 批准人
     */
    private String approvedBy;

    /**
     * 质量检查人
     */
    private String qualityCheckedBy;

    /**
     * 合规检查人
     */
    private String complianceCheckedBy;

    /**
     * 开始编制日期
     */
    private String startPreparedDate;

    /**
     * 结束编制日期
     */
    private String endPreparedDate;

    /**
     * 开始审计日期
     */
    private String startAuditDate;

    /**
     * 结束审计日期
     */
    private String endAuditDate;

    /**
     * 开始报送日期
     */
    private String startSubmissionDate;

    /**
     * 结束报送日期
     */
    private String endSubmissionDate;

    /**
     * 关键字搜索（报表名称、企业名称等）
     */
    private String keyword;

    /**
     * 创建时间-开始
     */
    private String startTime;

    /**
     * 创建时间-结束
     */
    private String endTime;

    /**
     * 报告期关键字（兼容前端字段，字符串类型如2026-Q1）
     */
    private String periodKeyword;

    /**
     * 企业名称（兼容前端字段companyName）
     */
    private String companyName;

    /**
     * 所属行业
     */
    private String industry;

}
