package com.financial.sharing.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 账簿分析查询参数
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
public class LedgerAnalysisQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 会计期间
     */
    private String accountingPeriod;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 科目代码
     */
    private String accountCode;

    /**
     * 科目名称
     */
    private String accountName;

    /**
     * 分析类型（BALANCE/TREND/RATIO）
     */
    private String analysisType;

    /**
     * 页码
     */
    private Integer pageNo = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向
     */
    private String orderDirection;

    /**
     * 租户ID
     */
    private Long tenantId;
}