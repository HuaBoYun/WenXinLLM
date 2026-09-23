package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 财务内账组织架构实体类
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FINANCE_ORGANIZATION")
public class OrganizationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
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
     * 组织类型
     */
    @TableField("ORG_TYPE")
    private String orgType;

    /**
     * 上级组织ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 组织层级
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
     * 邮箱
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 地址
     */
    @TableField("ADDRESS")
    private String address;

    /**
     * 启用状态
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

    /**
     * 子组织列表（非持久化字段）
     */
    @TableField(exist = false)
    private List<OrganizationEntity> children;

    /**
     * 下级组织数量
     */
    @TableField(exist = false)
    private Integer childCount;

    /**
     * 部门数量
     */
    @TableField(exist = false)
    private Integer deptCount;

    /**
     * 人员数量
     */
    @TableField(exist = false)
    private Integer userCount;

    /**
     * 上级组织名称
     */
    @TableField(exist = false)
    private String parentOrgName;
}