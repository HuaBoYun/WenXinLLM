package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 指标版本表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_INDEX_VERSION")
public class TblStateassetsPerformanceIndexVersion {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("INDEX_ID")
    private String indexId;

    @TableField("VERSION")
    private Integer version;

    @TableField("INDEX_CODE")
    private String indexCode;

    @TableField("INDEX_NAME")
    private String indexName;

    @TableField("FORMULA")
    private String formula;

    @TableField("UNIT")
    private String unit;

    @TableField("PRECISION_NUM")
    private Integer precisionNum;

    @TableField("MONITOR_CYCLE")
    private String monitorCycle;

    @TableField("ATTR_TYPE")
    private String attrType;

    @TableField("DATA_TYPE")
    private String dataType;

    @TableField("DIRECTION")
    private String direction;

    @TableField("MONITOR_TYPE")
    private String monitorType;

    @TableField("BUSINESS_TYPE")
    private String businessType;

    @TableField("TIME_TYPE")
    private String timeType;

    @TableField("VALUE_TAGS")
    private String valueTags;

    @TableField("FOUR_CATEGORY")
    private String fourCategory;

    @TableField("FIVE_ORIENTATION")
    private String fiveOrientation;

    @TableField("SIX_ASPECT")
    private String sixAspect;

    @TableField("SIX_ASPECT_DIMENSION")
    private String sixAspectDimension;

    @TableField("DATA_SOURCE")
    private String dataSource;

    @TableField("SOURCE_SYSTEM")
    private String sourceSystem;

    @TableField("SOURCE_TABLE")
    private String sourceTable;

    @TableField("SOURCE_FIELD")
    private String sourceField;

    @TableField("MODEL_ID")
    private String modelId;

    @TableField("MODEL_CODE")
    private String modelCode;

    @TableField("MODEL_NAME")
    private String modelName;

    @TableField("COMBINATION_ID")
    private String combinationId;

    @TableField("COMBINATION_CODE")
    private String combinationCode;

    @TableField("COMBINATION_NAME")
    private String combinationName;

    @TableField("CHANGE_CONTENT")
    private String changeContent;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
}
