package com.financial.sharing.dataCollection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 映射规则实体类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@TableName("TBL_FS_MAPPING_RULE")
public class TblMappingRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId(value = "RULE_ID", type = IdType.ASSIGN_ID)
    private String ruleId;

    /**
     * 数据源ID
     */
    @TableField("SOURCE_ID")
    private String sourceId;

    /**
     * 规则编码
     */
    @TableField("RULE_CODE")
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField("RULE_NAME")
    private String ruleName;

    /**
     * 目标表
     */
    @TableField("TARGET_TABLE")
    private String targetTable;

    /**
     * 源查询语句/API路径
     */
    @TableField("SOURCE_QUERY")
    private String sourceQuery;

    /**
     * 字段映射（JSON格式）
     */
    @TableField("FIELD_MAPPINGS")
    private String fieldMappings;

    /**
     * 过滤条件
     */
    @TableField("FILTER_CONDITION")
    private String filterCondition;

    /**
     * 转换规则（JSON格式）
     */
    @TableField("TRANSFORM_RULES")
    private String transformRules;

    /**
     * 校验规则（JSON格式）
     */
    @TableField("VALIDATION_RULES")
    private String validationRules;

    /**
     * 冲突策略：SKIP(跳过)/UPDATE(更新)/ERROR(报错)
     */
    @TableField("CONFLICT_STRATEGY")
    private String conflictStrategy;

    /**
     * 批次大小
     */
    @TableField("BATCH_SIZE")
    private Integer batchSize;

    /**
     * 是否增量：Y/N
     */
    @TableField("IS_INCREMENTAL")
    private String isIncremental;

    /**
     * 增量字段
     */
    @TableField("INCREMENTAL_FIELD")
    private String incrementalField;

    /**
     * 最后同步值
     */
    @TableField("LAST_SYNC_VALUE")
    private String lastSyncValue;

    /**
     * 是否启用：Y/N
     */
    @TableField("IS_ENABLED")
    private String isEnabled;

    /**
     * 排序号
     */
    @TableField("SORT_NO")
    private Integer sortNo;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private String orgId;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}

