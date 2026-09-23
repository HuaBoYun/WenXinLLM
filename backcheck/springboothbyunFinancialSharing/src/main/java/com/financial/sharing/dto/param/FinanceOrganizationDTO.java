package com.financial.sharing.dto.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 财务组织参数对象
 *
 * @author system
 * @since 2024-12-19
 */
@Data
public class FinanceOrganizationDTO {

    /**
     * 主键ID（更新时需要）
     */
    private Long id;

    /**
     * 上级组织ID
     */
    private Long parentId;

    /**
     * 组织编码
     */
    @NotBlank(message = "组织编码不能为空")
    private String orgCode;

    /**
     * 组织名称
     */
    @NotBlank(message = "组织名称不能为空")
    private String orgName;

    /**
     * 组织类型(1公司2部门3科室4小组)
     */
    @NotNull(message = "组织类型不能为空")
    private Integer orgType;

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
     * 版本号，用于乐观锁
     */
    private Integer version;
}