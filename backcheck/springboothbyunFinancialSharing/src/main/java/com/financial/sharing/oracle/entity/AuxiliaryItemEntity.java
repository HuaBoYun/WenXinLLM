package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 辅助核算项表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_AUXILIARY_ITEM")
public class AuxiliaryItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 辅助核算项ID
     */
    @TableId(value = "AUXILIARY_ID", type = IdType.INPUT)
    private Long auxiliaryId;

    /**
     * 辅助核算项编码
     */
    @TableField("AUXILIARY_CODE")
    private String auxiliaryCode;

    /**
     * 辅助核算项名称
     */
    @TableField("AUXILIARY_NAME")
    private String auxiliaryName;

    /**
     * 辅助核算类型
     */
    @TableField("AUXILIARY_TYPE")
    private String auxiliaryType;

    /**
     * 上级ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 是否末级(0否1是)
     */
    @TableField("IS_LEAF")
    private Integer isLeaf;

    /**
     * 是否启用(0否1是)
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 账簿ID
     */
    @TableField("BOOK_ID")
    private Long bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 删除标识(0否1是)
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

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
     * 创建人
     */
    @TableField(value = "CREATOR", fill = FieldFill.INSERT)
    private Long creator;

    /**
     * 更新人
     */
    @TableField(value = "UPDATER", fill = FieldFill.INSERT_UPDATE)
    private Long updater;
}
