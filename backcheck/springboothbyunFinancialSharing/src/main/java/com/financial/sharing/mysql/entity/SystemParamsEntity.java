package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 财务参数配置实体类
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FINANCE_SYSTEM_PARAMS")
public class SystemParamsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 参数编码
     */
    @TableField("PARAM_CODE")
    private String paramCode;

    /**
     * 参数名称
     */
    @TableField("PARAM_NAME")
    private String paramName;

    /**
     * 分类编码
     */
    @TableField("CATEGORY_CODE")
    private String categoryCode;

    /**
     * 参数类型
     */
    @TableField("PARAM_TYPE")
    private String paramType;

    /**
     * 参数值
     */
    @TableField("PARAM_VALUE")
    private String paramValue;

    /**
     * 默认值
     */
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    /**
     * 是否必填
     */
    @TableField("IS_REQUIRED")
    private Integer isRequired;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 参数描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 账簿ID
     */
    @TableField("BOOK_ID")
    private Long bookId;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新人ID
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    @TableField("IS_DELETED")
    private Integer isDeleted;
}