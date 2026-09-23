package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算维度成员实体类
 * 
 * @description 预算维度成员管理实体，支持维度成员的层级结构和属性管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_DIMENSION_MEMBER")
public class BudgetDimensionMember implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 维度ID
     */
    @TableField("DIMENSION_ID")
    private String dimensionId;

    /**
     * 成员编码
     */
    @TableField("MEMBER_CODE")
    private String memberCode;

    /**
     * 成员名称
     */
    @TableField("MEMBER_NAME")
    private String memberName;

    /**
     * 成员简称
     */
    @TableField("MEMBER_SHORT_NAME")
    private String memberShortName;

    /**
     * 父成员ID
     */
    @TableField("PARENT_MEMBER_ID")
    private String parentMemberId;

    /**
     * 成员层级
     */
    @TableField("MEMBER_LEVEL")
    private Integer memberLevel;

    /**
     * 成员路径（用于快速查询层级关系）
     */
    @TableField("MEMBER_PATH")
    private String memberPath;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 成员类型：leaf-叶子节点，branch-分支节点
     */
    @TableField("MEMBER_TYPE")
    private String memberType;

    /**
     * 成员属性（JSON格式）
     */
    @TableField("MEMBER_ATTRIBUTES")
    private String memberAttributes;

    /**
     * 成员描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 数据来源：manual-手工录入，import-导入，sync-同步
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 数据来源配置（JSON格式）
     */
    @TableField("DATA_SOURCE_CONFIG")
    private String dataSourceConfig;

    /**
     * 外部系统编码
     */
    @TableField("EXTERNAL_CODE")
    private String externalCode;

    /**
     * 外部系统ID
     */
    @TableField("EXTERNAL_ID")
    private String externalId;

    /**
     * 映射规则（JSON格式）
     */
    @TableField("MAPPING_RULES")
    private String mappingRules;

    /**
     * 是否叶子节点
     */
    @TableField("IS_LEAF")
    private Boolean isLeaf;

    /**
     * 是否虚拟节点
     */
    @TableField("IS_VIRTUAL")
    private Boolean isVirtual;

    /**
     * 是否系统预置
     */
    @TableField("IS_SYSTEM")
    private Boolean isSystem;

    /**
     * 是否可编辑
     */
    @TableField("IS_EDITABLE")
    private Boolean isEditable;

    /**
     * 是否可删除
     */
    @TableField("IS_DELETABLE")
    private Boolean isDeletable;

    /**
     * 生效时间
     */
    @TableField("EFFECTIVE_TIME")
    private LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    @TableField("EXPIRY_TIME")
    private LocalDateTime expiryTime;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用，expired-已过期
     */
    @TableField("STATUS")
    private String status;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

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

    // 常量定义
    public static final String MEMBER_TYPE_LEAF = "leaf";
    public static final String MEMBER_TYPE_BRANCH = "branch";

    public static final String DATA_SOURCE_MANUAL = "manual";
    public static final String DATA_SOURCE_IMPORT = "import";
    public static final String DATA_SOURCE_SYNC = "sync";

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_EXPIRED = "expired";
}
