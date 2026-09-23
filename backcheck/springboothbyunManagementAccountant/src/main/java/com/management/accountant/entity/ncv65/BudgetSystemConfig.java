package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 系统配置实体类
 * 
 * @description 系统配置管理实体，支持系统参数和配置的管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_SYSTEM_CONFIG")
public class BudgetSystemConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 配置编码
     */
    @TableField("CONFIG_CODE")
    private String configCode;

    /**
     * 配置名称
     */
    @TableField("CONFIG_NAME")
    private String configName;

    /**
     * 配置分类：SYSTEM-系统配置，BUSINESS-业务配置，SECURITY-安全配置
     */
    @TableField("CONFIG_CATEGORY")
    private String configCategory;

    /**
     * 配置值
     */
    @TableField("CONFIG_VALUE")
    private String configValue;

    /**
     * 默认值
     */
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    /**
     * 数据类型：STRING-字符串，INTEGER-整数，BOOLEAN-布尔值
     */
    @TableField("DATA_TYPE")
    private String dataType;

    /**
     * 是否可修改
     */
    @TableField("IS_MODIFIABLE")
    private Boolean isModifiable;

    /**
     * 配置描述
     */
    @TableField("CONFIG_DESCRIPTION")
    private String configDescription;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
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

    // ==================== 常量定义 ====================

    /**
     * 配置分类常量
     */
    public static final String CONFIG_CATEGORY_SYSTEM = "SYSTEM";
    public static final String CONFIG_CATEGORY_BUSINESS = "BUSINESS";
    public static final String CONFIG_CATEGORY_SECURITY = "SECURITY";

    /**
     * 数据类型常量
     */
    public static final String DATA_TYPE_STRING = "STRING";
    public static final String DATA_TYPE_INTEGER = "INTEGER";
    public static final String DATA_TYPE_BOOLEAN = "BOOLEAN";
}
