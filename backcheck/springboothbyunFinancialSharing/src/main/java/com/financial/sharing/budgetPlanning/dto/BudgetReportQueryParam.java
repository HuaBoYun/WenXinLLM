package com.financial.sharing.budgetPlanning.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 预算报表查询参数
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
public class BudgetReportQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报表ID
     */
    private String reportId;

    /**
     * 报表类型：DETAIL(明细表)/SUMMARY(汇总表)/COMPARE(对比表)/TREND(趋势表)
     */
    private String reportType;

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 组织ID列表
     */
    private String[] orgIds;

    /**
     * 科目编码列表
     */
    private String[] subjectCodes;

    /**
     * 期间列表
     */
    private String[] periods;

    /**
     * 开始期间
     */
    private String startPeriod;

    /**
     * 结束期间
     */
    private String endPeriod;

    /**
     * 预算年度
     */
    private String budgetYear;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 维度1编码
     */
    private String dimension1Code;

    /**
     * 维度2编码
     */
    private String dimension2Code;

    /**
     * 是否包含调整
     */
    private Boolean includeAdjustment;

    /**
     * 是否包含实际
     */
    private Boolean includeActual;

    /**
     * 汇总维度：ORG(按组织)/SUBJECT(按科目)/PERIOD(按期间)
     */
    private String summaryDimension;

    /**
     * 对比类型：BUDGET_VS_ACTUAL(预算vs实际)/PERIOD_VS_PERIOD(期间对比)/YEAR_VS_YEAR(年度对比)
     */
    private String compareType;

    /**
     * 对比期间1
     */
    private String comparePeriod1;

    /**
     * 对比期间2
     */
    private String comparePeriod2;

    /**
     * 组织ID（来自登录用户 orgid，原 TENANT_ID 字段，已重命名为 ORG_ID）
     */
    private String orgId;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页数量
     */
    private Integer pageSize;
}

