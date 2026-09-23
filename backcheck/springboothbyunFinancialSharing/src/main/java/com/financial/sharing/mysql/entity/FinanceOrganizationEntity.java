package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 财务组织表 - MySQL版本
 *
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FINANCE_ORGANIZATION")
public class FinanceOrganizationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 组织编码
     */
    @TableField("ORG_CODE")
    private String orgCode;

    /**
     * 组织名称
     */
    @TableField("ORG_NAME")
    private String orgName;

    /**
     * 组织类型(1公司2部门3科室4小组)
     */
    @TableField("ORG_TYPE")
    private Integer orgType;

    /**
     * 上级组织ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 组织级次
     */
    @TableField("ORG_LEVEL")
    private Integer orgLevel;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 负责人ID
     */
    @TableField("MANAGER_ID")
    private Long managerId;

    /**
     * 负责人姓名
     */
    @TableField("MANAGER_NAME")
    private String managerName;

    /**
     * 联系电话
     */
    @TableField("PHONE")
    private String phone;

    /**
     * 邮箱地址
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 联系地址
     */
    @TableField("ADDRESS")
    private String address;

    /**
     * 是否启用(0否1是)
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 组织描述
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
     * 删除标识(0否1是)
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号，用于乐观锁
     */
    @TableField("VERSION")
    @Version
    private Integer version;
}