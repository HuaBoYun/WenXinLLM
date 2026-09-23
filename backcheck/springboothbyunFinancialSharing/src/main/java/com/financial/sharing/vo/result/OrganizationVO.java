package com.financial.sharing.vo.result;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 组织架构返回值VO
 * @author system
 * @since 2024-12-19
 */
@Data
public class OrganizationVO {

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
     * 组织类型(1公司 2部门 3科室 4小组)
     */
    private Integer orgType;

    /**
     * 组织类型名称
     */
    private String orgTypeName;

    /**
     * 上级组织ID
     */
    private Long parentId;

    /**
     * 上级组织名称
     */
    private String parentOrgName;

    /**
     * 组织层级
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
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 启用状态
     */
    private Integer isEnabled;

    /**
     * 启用状态名称
     */
    private String isEnabledName;

    /**
     * 组织描述
     */
    private String description;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 子组织列表
     */
    private List<OrganizationVO> children;

    /**
     * 下级组织数量
     */
    private Integer childCount;

    /**
     * 部门数量
     */
    private Integer deptCount;

    /**
     * 人员数量
     */
    private Integer userCount;
}