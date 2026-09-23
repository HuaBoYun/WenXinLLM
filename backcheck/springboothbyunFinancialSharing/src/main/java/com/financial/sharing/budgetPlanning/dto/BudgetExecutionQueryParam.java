package com.financial.sharing.budgetPlanning.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 预算执行分析查询参数
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
public class BudgetExecutionQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 业务组织ID（华东/华南分公司等业务维度过滤，原 ORG_ID 字段，已重命名为 BIZ_ORG_ID）
     */
    private String bizOrgId;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 期间
     */
    private String period;

    /**
     * 预算年度
     */
    private String budgetYear;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 状态：NORMAL(正常)/WARNING(预警)/EXCEEDED(超支)
     */
    private String status;

    /**
     * 预警级别：LOW(低)/MEDIUM(中)/HIGH(高)
     */
    private String warningLevel;

    /**
     * 开始期间
     */
    private String startPeriod;

    /**
     * 结束期间
     */
    private String endPeriod;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

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

