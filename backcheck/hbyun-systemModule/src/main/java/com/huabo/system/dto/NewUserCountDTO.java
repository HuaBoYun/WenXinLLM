package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 新增用户数DTO
 */
@Data
public class NewUserCountDTO {

    /**
     * 年月
     */
    private String m;

    /**
     * 新增用户数量
     */
    private BigDecimal c;
}
