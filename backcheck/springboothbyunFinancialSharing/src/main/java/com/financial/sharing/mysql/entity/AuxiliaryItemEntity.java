package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 辅助核算项表 - MySQL版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_auxiliary_item")
public class AuxiliaryItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 辅助核算项ID
     */
    @TableId(value = "auxiliary_id", type = IdType.INPUT)
    private Long auxiliaryId;

    /**
     * 辅助核算项编码
     */
    @TableField("auxiliary_code")
    private String auxiliaryCode;

    /**
     * 辅助核算项名称
     */
    @TableField("auxiliary_name")
    private String auxiliaryName;

    /**
     * 辅助核算类型
     */
    @TableField("auxiliary_type")
    private String auxiliaryType;

    /**
     * 上级ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 是否末级(0否1是)
     */
    @TableField("is_leaf")
    private Integer isLeaf;

    /**
     * 是否启用(0否1是)
     */
    @TableField("is_enabled")
    private Integer isEnabled;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 账簿ID
     */
    @TableField("book_id")
    private Long bookId;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 版本号
     */
    @TableField("version")
    @Version
    private Integer version;

    /**
     * 删除标识(0否1是)
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private Long creator;

    /**
     * 更新人
     */
    @TableField(value = "updater", fill = FieldFill.INSERT_UPDATE)
    private Long updater;
}
