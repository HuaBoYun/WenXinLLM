package com.financial.sharing.vo.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统参数返回值VO
 * @author system
 * @since 2024-12-19
 */
@Data
public class SystemParamsVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 参数编码
     */
    private String paramCode;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 分类编码
     */
    private String categoryCode;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 参数类型名称
     */
    private String paramTypeName;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否必填
     */
    private Integer isRequired;

    /**
     * 是否必填名称
     */
    private String isRequiredName;

    /**
     * 是否启用
     */
    private Integer isEnabled;

    /**
     * 是否启用名称
     */
    private String isEnabledName;

    /**
     * 参数描述
     */
    private String description;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}