package com.huabo.finance.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字段映射配置实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_TRANSFORM_FIELD_MAPPING")
@Schema(name = "TransformFieldMapping对象", description = "字段映射配置")
public class TransformFieldMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "映射ID")
    @TableId("MAPPING_ID")
    private String mappingId;

    @Schema(name =  "转化任务ID")
    @TableField("TRANSFORM_TASK_ID")
    private String transformTaskId;

    @Schema(name =  "源表名")
    @TableField("SOURCE_TABLE")
    private String sourceTable;

    @Schema(name =  "目标表名")
    @TableField("TARGET_TABLE")
    private String targetTable;

    @Schema(name =  "源字段名")
    @TableField("SOURCE_FIELD")
    private String sourceField;

    @Schema(name =  "目标字段名")
    @TableField("TARGET_FIELD")
    private String targetField;

    @Schema(name =  "源字段类型")
    @TableField("SOURCE_FIELD_TYPE")
    private String sourceFieldType;

    @Schema(name =  "目标字段类型")
    @TableField("TARGET_FIELD_TYPE")
    private String targetFieldType;

    @Schema(name =  "源字段长度")
    @TableField("SOURCE_FIELD_LENGTH")
    private Integer sourceFieldLength;

    @Schema(name =  "源字段精度")
    @TableField("SOURCE_FIELD_PRECISION")
    private Integer sourceFieldPrecision;

    @Schema(name =  "目标字段长度")
    @TableField("TARGET_FIELD_LENGTH")
    private Integer targetFieldLength;

    @Schema(name =  "目标字段精度")
    @TableField("TARGET_FIELD_PRECISION")
    private Integer targetFieldPrecision;

    @Schema(name =  "转换规则(DIRECT-直接映射,MAPPING-值映射,CALCULATE-计算,CONCAT-拼接,FUNCTION-函数,LOOKUP-查找)")
    @TableField("TRANSFORM_RULE")
    private String transformRule;

    @Schema(name =  "转换表达式")
    @TableField("TRANSFORM_EXPRESSION")
    private String transformExpression;

    @Schema(name =  "默认值")
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    @Schema(name =  "是否必填(1-必填,0-非必填)")
    @TableField("IS_REQUIRED")
    private Integer isRequired;

    @Schema(name =  "是否启用(1-启用,0-禁用)")
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @Schema(name =  "排序序号")
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @Schema(name =  "备注")
    @TableField("REMARK")
    private String remark;

    @Schema(name =  "创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(name =  "更新时间")
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}

