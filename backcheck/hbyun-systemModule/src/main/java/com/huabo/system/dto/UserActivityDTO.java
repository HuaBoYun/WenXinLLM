package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户活跃度DTO
 */
@Data
public class UserActivityDTO {

    /**
     * 年月
     */
    private String yearMonth;

    /**
     * 活跃用户数量
     */
    private BigDecimal c;
}
