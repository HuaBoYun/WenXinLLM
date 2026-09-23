package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字段映射实体
 */
@Data
@TableName("TBL_FIELD_MAPPING")
public class FieldMappingEntity {

    @TableId(type = IdType.AUTO)
    @TableField("MAPPING_ID")
    private Long mappingId;

    @TableField("DATA_SOURCE_ID")
    private Long dataSourceId;

    @TableField("TARGET_TABLE")
    private String targetTable;

    @TableField("SOURCE_FIELD")
    private String sourceField;

    @TableField("TARGET_FIELD")
    private String targetField;

    @TableField("FIELD_TYPE")
    private String fieldType;

    @TableField("FIELD_LENGTH")
    private Integer fieldLength;

    @TableField("IS_NULLABLE")
    private Boolean isNullable;

    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    @TableField("TRANSFORMATION_RULE")
    private String transformationRule;

    @TableField("VALIDATION_RULE")
    private String validationRule;

    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("TENANT_ID")
    private Long tenantId;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @TableField("CREATE_USER")
    private Long createUser;

    @TableField("UPDATE_USER")
    private Long updateUser;
}