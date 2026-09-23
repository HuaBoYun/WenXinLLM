package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 内控测试缺陷程度DTO
 */
@Data
public class InternalControlDefectDTO {

    /**
     * 缺陷级别
     */
    private String b;

    /**
     * 数量
     */
    private BigDecimal c;
}
