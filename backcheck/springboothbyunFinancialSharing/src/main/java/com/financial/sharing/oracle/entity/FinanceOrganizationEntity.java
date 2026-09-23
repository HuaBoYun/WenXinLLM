package com.financial.sharing.oracle.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 财务组织表 - Oracle版本
 *
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FinanceOrganizationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 组织类型(1公司2部门3科室4小组)
     */
    private Integer orgType;

    /**
     * 上级组织ID
     */
    private Long parentId;

    /**
     * 组织级次
     */
    private Integer orgLevel;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 负责人ID
     */
    private Long managerId;

    /**
     * 负责人姓名
     */
    private String managerName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 是否启用(0否1是)
     */
    private Integer isEnabled;

    /**
     * 组织描述
     */
    private String description;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 删除标识(0否1是)
     */
    private Integer isDeleted;

    /**
     * 版本号，用于乐观锁
     */
    private Integer version;
}