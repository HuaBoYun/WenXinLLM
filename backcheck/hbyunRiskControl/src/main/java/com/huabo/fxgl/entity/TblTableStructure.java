package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 表结构同步表
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_TABLE_STRUCTURE")
@Schema(name="TblTableStructure对象", description="表结构同步表")
public class TblTableStructure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "结构ID")
    @TableId(value = "STRUCTURE_ID", type = IdType.ASSIGN_ID)
    private String structureId;

    @Schema(name = "数据源ID")
    @TableField("DATA_SOURCE_ID")
    private String dataSourceId;

    @Schema(name = "表名")
    @TableField("TABLE_NAME")
    private String tableName;

    @Schema(name = "表注释")
    @TableField("TABLE_COMMENT")
    private String tableComment;

    @Schema(name = "列名")
    @TableField("COLUMN_NAME")
    private String columnName;

    @Schema(name = "列类型")
    @TableField("COLUMN_TYPE")
    private String columnType;

    @Schema(name = "列长度")
    @TableField("COLUMN_LENGTH")
    private Integer columnLength;

    @Schema(name = "列精度")
    @TableField("COLUMN_PRECISION")
    private Integer columnPrecision;

    @Schema(name = "列小数位数")
    @TableField("COLUMN_SCALE")
    private Integer columnScale;

    @Schema(name = "是否可空(Y/N)")
    @TableField("IS_NULLABLE")
    private String isNullable;

    @Schema(name = "是否主键(Y/N)")
    @TableField("IS_PRIMARY_KEY")
    private String isPrimaryKey;

    @Schema(name = "列注释")
    @TableField("COLUMN_COMMENT")
    private String columnComment;

    @Schema(name = "默认值")
    @TableField("COLUMN_DEFAULT")
    private String columnDefault;

    @Schema(name = "列顺序")
    @TableField("COLUMN_ORDER")
    private Integer columnOrder;

    @Schema(name = "同步时间")
    @TableField("SYNC_TIME")
    private LocalDateTime syncTime;
}
