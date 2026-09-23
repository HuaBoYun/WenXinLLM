package com.huabo.financialdata.entity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Gbi字段信息表
 * </p>
 *
 * @author 
 * @since 2024-04-21
 */
@Getter
@Setter
@TableName("TBL_GBI_COLUMNS")
@Schema(name="GbiTableColumns对象", description="Gbi字段信息表")
public class GbiTableColumns implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键id")
    @TableId("ID")
    private String id;

    @Schema(name="appid")
    @TableField("APP_ID")
    private String appId;

    @Schema(name="批次号")
    @TableField("BATCH_NO")
    private String batchNo;

    @Schema(name="字段名称")
    @TableField("COLUMN_NAME")
    private String columnName;

    @Schema(name="数据库真实字段名称")
    @TableField("MAPPING_COLUMN")
    private String mappingColumn;

    @Schema(name="字段描述")
    @TableField("COLUMN_COMMENT")
    private String columnComment;

    @TableField("DATA_BASE")
    private String dataBase;

    @TableField("DATA_SOURCE_ID")
    private String dataSourceId;

    @Schema(name="字段样例")
    @TableField("DEMO")
    private String demo;

    @Schema(name="是否索引1-是，0-否")
    @TableField("INDEXED")
    private Boolean indexed;

    @Schema
    @TableField("LABELS")
    private String labels;

    @Schema(name="父级字段,传父级id")
    @TableField("PARENT_COLUMN")
    private String parentColumn;

    @Schema(name="页面字段类型")
    @TableField("CELL_TYPE")
    private String cellType;

    @Schema(name="用户id")
    @TableField("USER_ID")
    private BigDecimal userId;

    @Schema(name="创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @Schema(name="修改时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(name="是否删除1-是，0-否")
    @TableField("DELETED")
    private Integer deleted;

    @Schema(name="创建企业")
    @TableField("CREATE_COMPANY")
    private String createCompany;

    @Schema(name="创建部门")
    @TableField("CREATE_DEPT")
    private String createDept;

    @Schema(name="批次记录id")
    @TableField("BATCH_ID")
    private String batchId;

    @Schema(name="gbi表信息id")
    @TableField("TABLE_ID")
    private String tableId;

    @Schema(name="顺序")
    @TableField("SORT")
    private Integer sort;

    @Schema(name="索引名称")
    @TableField("INDEX_NAME")
    private String indexName;

    @Schema(name="字段类型")
    @TableField("FIELD_TYPE")
    private String fieldType;
}
