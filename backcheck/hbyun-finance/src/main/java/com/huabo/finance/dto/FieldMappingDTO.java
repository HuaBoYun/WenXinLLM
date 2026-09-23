package com.huabo.finance.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字段映射DTO
 * 
 * @author 华博云开发团队
 * @since 2025-10-24
 */
@Data
@Schema(name="FieldMappingDTO", description="字段映射数据传输对象")
public class FieldMappingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "源表名")
    private String sourceTable;

    @Schema(name = "源字段名")
    private String sourceField;

    @Schema(name = "源字段类型")
    private String sourceFieldType;

    @Schema(name = "源字段长度")
    private Integer sourceFieldLength;

    @Schema(name = "源字段精度")
    private Integer sourceFieldPrecision;

    @Schema(name = "目标系统表名")
    private String targetTable;

    @Schema(name = "目标系统字段名")
    private String targetField;

    @Schema(name = "目标字段类型")
    private String targetFieldType;

    @Schema(name = "目标字段长度")
    private Integer targetFieldLength;

    @Schema(name = "目标字段精度")
    private Integer targetFieldPrecision;

    @Schema(name = "排序序号")
    private Integer sortOrder;

    @Schema(name = "备注")
    private String remark;
}

