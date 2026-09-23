package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 审计问题数量DTO
 */
@Data
public class AuditIssueCountDTO {

    /**
     * 年月
     */
    private String d;

    /**
     * 数量
     */
    private BigDecimal c;
}
