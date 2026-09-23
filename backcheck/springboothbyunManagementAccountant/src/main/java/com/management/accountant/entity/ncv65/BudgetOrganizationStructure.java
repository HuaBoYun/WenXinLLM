package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算组织体系实体类
 * 
 * @description 预算组织体系管理实体，支持5种组织体系类型
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_ORGANIZATION_STRUCTURE")
public class BudgetOrganizationStructure implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 体系编码
     */
    @TableField("STRUCTURE_CODE")
    private String structureCode;

    /**
     * 体系名称
     */
    @TableField("STRUCTURE_NAME")
    private String structureName;

    /**
     * 体系类型：single-单一集团，multi-多集团，hierarchical-分级管理，matrix-矩阵式，hybrid-混合式
     */
    @TableField("STRUCTURE_TYPE")
    private String structureType;

    /**
     * 体系描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 控制模式：centralized-集中式，decentralized-分散式，hybrid-混合式
     */
    @TableField("CONTROL_MODE")
    private String controlMode;

    /**
     * 最大层级数
     */
    @TableField("MAX_LEVELS")
    private Integer maxLevels;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用
     */
    @TableField("STATUS")
    private String status;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private LocalDateTime effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRY_DATE")
    private LocalDateTime expiryDate;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号（乐观锁）
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 扩展字段1
     */
    @TableField("EXT_FIELD1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("EXT_FIELD2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("EXT_FIELD3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("EXT_FIELD4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("EXT_FIELD5")
    private String extField5;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // 组织体系类型常量
    public static final String STRUCTURE_TYPE_SINGLE = "single";
    public static final String STRUCTURE_TYPE_MULTI = "multi";
    public static final String STRUCTURE_TYPE_HIERARCHICAL = "hierarchical";
    public static final String STRUCTURE_TYPE_MATRIX = "matrix";
    public static final String STRUCTURE_TYPE_HYBRID = "hybrid";

    // 控制模式常量
    public static final String CONTROL_MODE_CENTRALIZED = "centralized";
    public static final String CONTROL_MODE_DECENTRALIZED = "decentralized";
    public static final String CONTROL_MODE_HYBRID = "hybrid";

    // 状态常量
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
}
