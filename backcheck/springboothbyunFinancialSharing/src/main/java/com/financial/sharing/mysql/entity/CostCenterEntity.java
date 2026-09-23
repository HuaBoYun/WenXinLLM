package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 成本中心实体类 - Oracle/达梦版本
 * 
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_COST_CENTER")
public class CostCenterEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成本中心ID
     */
    @TableId(value = "CENTER_ID", type = IdType.INPUT)
    private String centerId;

    /**
     * 成本中心编码
     */
    @TableField("CENTER_CODE")
    private String centerCode;

    /**
     * 成本中心名称
     */
    @TableField("CENTER_NAME")
    private String centerName;

    /**
     * 中心类型: 1-成本中心, 2-利润中心, 3-投资中心
     */
    @TableField("CENTER_TYPE")
    private Integer centerType;

    /**
     * 上级中心ID
     */
    @TableField("PARENT_CENTER_ID")
    private String parentCenterId;

    /**
     * 上级中心名称
     */
    @TableField("PARENT_CENTER_NAME")
    private String parentCenterName;

    /**
     * 中心级次
     */
    @TableField("CENTER_LEVEL")
    private Integer centerLevel;

    /**
     * 是否末级: 1-是, 0-否
     */
    @TableField("IS_LEAF")
    private Integer isLeaf;

    /**
     * 负责人ID
     */
    @TableField("MANAGER_ID")
    private String managerId;

    /**
     * 负责人名称
     */
    @TableField("MANAGER_NAME")
    private String managerName;

    /**
     * 成本分摊方法: 1-直接分摊, 2-阶梯分摊, 3-比例分摊
     */
    @TableField("COST_ALLOCATION_METHOD")
    private Integer costAllocationMethod;

    /**
     * 是否启用: 1-启用, 0-停用
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 账簿ID
     */
    @TableField("BOOK_ID")
    private String bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

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
    private String creator;

    /**
     * 更新人
     */
    @TableField(value = "UPDATER", fill = FieldFill.INSERT_UPDATE)
    private String updater;
}

