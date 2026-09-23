package com.financial.sharing.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 余额重新计算参数DTO
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
public class BalanceRecalculateParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会计期间
     */
    private String period;

    /**
     * 科目编码（可选，为空则重新计算所有科目）
     */
    private String subjectCode;

    /**
     * 是否强制重新计算（true：忽略现有余额重新计算，false：只计算不存在的余额）
     */
    private Boolean forceRecalculate = false;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;
}