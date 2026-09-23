package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 基础用户信息DTO
 */
@Data
public class BasicUserInfoDTO {

    /**
     * 用户名
     */
    private String type1;

    /**
     * 所属部门
     */
    private String type2;

    /**
     * 入职时间
     */
    private String type4;

    /**
     * 用户标签/角色
     */
    private String type3;

    /**
     * 工龄(年)
     */
    private BigDecimal gl;
}
