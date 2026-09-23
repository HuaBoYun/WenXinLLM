package com.financial.sharing.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 总账查询参数DTO
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
public class GeneralLedgerQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer pageNo = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 账户名称（用于总账查询）
     */
    private String accountName;

    /**
     * 会计期间
     */
    private String accountingPeriod;

    /**
     * 开始期间
     */
    private String beginPeriod;

    /**
     * 结束期间
     */
    private String endPeriod;

    /**
     * 开始日期
     */
    private String beginDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 凭证号
     */
    private String voucherNo;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 科目级别
     */
    private String subjectLevel;

    /**
     * 科目类型
     */
    private String subjectType;

    /**
     * 余额方向
     */
    private String balanceDirection;

    /**
     * 辅助核算类型
     */
    private String auxiliaryType;

    /**
     * 辅助核算值
     */
    private String auxiliaryValue;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向
     */
    private String sortOrder;
}