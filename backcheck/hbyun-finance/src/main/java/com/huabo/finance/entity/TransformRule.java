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
 * 数据转换计算规则实体类
 * 
 * @author 华博云开发团队
 * @since 2025-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_TRANSFORM_RULE")
@Schema(name = "TransformRule对象", description = "数据转换计算规则")
public class TransformRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "规则ID")
    @TableId("RULE_ID")
    private String ruleId;

    @Schema(name =  "关联的字段映射ID")
    @TableField("MAPPING_ID")
    private String mappingId;

    @Schema(name =  "关联转化任务ID")
    @TableField("TASK_ID")
    private String taskId;

    @Schema(name =  "关联采集任务ID(⭐新增)")
    @TableField("COLLECTION_TASK_ID")
    private String collectionTaskId;

    @Schema(name =  "源表名")
    @TableField("SOURCE_TABLE")
    private String sourceTable;

    @Schema(name =  "目标表名")
    @TableField("TARGET_TABLE")
    private String targetTable;

    @Schema(name =  "规则类型(DIRECT-直接映射,CASE_WHEN-条件转换,GROUP_BY-分组聚合)")
    @TableField("RULE_TYPE")
    private String ruleType;

    @Schema(name =  "规则名称")
    @TableField("RULE_NAME")
    private String ruleName;

    @Schema(name =  "计算表达式")
    @TableField("EXPRESSION")
    private String expression;

    @Schema(name =  "参数配置(JSON格式)")
    @TableField("PARAMS")
    private String params;

    @Schema(name =  "条件表达式(用于条件判断类型)")
    @TableField("CONDITION_EXPR")
    private String conditionExpr;

    @Schema(name =  "关联类型(LEFT/INNER/RIGHT,用于跨表关联)")
    @TableField("JOIN_TYPE")
    private String joinType;

    @Schema(name =  "关联表名(用于跨表关联)")
    @TableField("JOIN_TABLE")
    private String joinTable;

    @Schema(name =  "关联条件(用于跨表关联)")
    @TableField("JOIN_CONDITION")
    private String joinCondition;

    @Schema(name =  "聚合函数(SUM/AVG/MAX/MIN/COUNT,用于聚合统计)")
    @TableField("AGGREGATE_FUNC")
    private String aggregateFunc;

    @Schema(name =  "分组字段(逗号分隔,用于聚合统计)")
    @TableField("GROUP_BY_FIELDS")
    private String groupByFields;

    @Schema(name =  "执行顺序(1-100)")
    @TableField("EXECUTE_ORDER")
    private Integer executeOrder;

    @Schema(name =  "是否自动执行(1:是,0:否,⭐新增)")
    @TableField("AUTO_EXECUTE")
    private Integer autoExecute;

    @Schema(name =  "规则配置(JSON格式)")
    @TableField("RULE_CONFIG")
    private String ruleConfig;

    @Schema(name =  "排序序号")
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @Schema(name =  "是否启用(1-启用,0-禁用)")
    @TableField("IS_ENABLED")
    private String isEnabled;

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

