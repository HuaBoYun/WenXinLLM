package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 内控缺陷整改跟进DTO
 */
@Data
public class InternalControlRectificationDTO {

    /**
     * 状态
     */
    private String s;

    /**
     * 数量
     */
    private BigDecimal c;
}
