package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 集成配置参数实体
 * 存储每个集成配置的具体参数信息，支持 STRING/NUMBER/BOOLEAN/JSON 等参数类型
 *
 * @author system
 * @date 2026-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INTEGRATION_CONFIG_PARAM")
public class IntegrationConfigParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 参数主键ID */
    @TableId(value = "PARAM_ID", type = IdType.ASSIGN_UUID)
    private String paramId;

    /** 关联的集成配置ID */
    @TableField("CONFIG_ID")
    private String configId;

    /** 参数名称 */
    @TableField("PARAMETER_NAME")
    private String parameterName;

    /** 参数类型（STRING/NUMBER/BOOLEAN/JSON） */
    @TableField("PARAMETER_TYPE")
    private String parameterType;

    /** 参数值 */
    @TableField("PARAMETER_VALUE")
    private String parameterValue;

    /** 默认值 */
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    /** 是否必填 */
    @TableField("IS_REQUIRED")
    private Boolean isRequired;

    /** 参数描述 */
    @TableField("DESCRIPTION")
    private String description;

    /** 排序序号 */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /** 创建人 */
    @TableField("CREATED_BY")
    private String createdBy;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /** 更新人 */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /** 更新时间 */
    @TableField("UPDATED_TIME")
    private Date updatedTime;
}
