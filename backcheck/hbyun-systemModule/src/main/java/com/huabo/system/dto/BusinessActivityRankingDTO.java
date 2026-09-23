package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 业务活跃度排名DTO
 */
@Data
public class BusinessActivityRankingDTO {

    /**
     * 业务模块名称
     */
    private String b;

    /**
     * 访问次数
     */
    private BigDecimal c;
}
