package com.financial.sharing.vo.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 组织架构保存参数
 * @author system
 * @since 2024-12-19
 */
@Data
public class OrganizationSaveParam {

    /**
     * 组织ID（更新时需要）
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
    @Size(max = 50, message = "组织编码长度不能超过50个字符")
    private String orgCode;

    /**
     * 组织名称
     */
    @NotBlank(message = "组织名称不能为空")
    @Size(max = 100, message = "组织名称长度不能超过100个字符")
    private String orgName;

    /**
     * 组织类型(1公司 2部门 3科室 4小组)
     */
    @NotNull(message = "组织类型不能为空")
    private Integer orgType;

    /**
     * 组织层级
     */
    @NotNull(message = "组织层级不能为空")
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
    @Size(max = 50, message = "负责人姓名长度不能超过50个字符")
    private String managerName;

    /**
     * 联系电话
     */
    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String phone;

    /**
     * 邮箱
     */
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 地址
     */
    @Size(max = 200, message = "地址长度不能超过200个字符")
    private String address;

    /**
     * 启用状态
     */
    @NotNull(message = "启用状态不能为空")
    private Integer isEnabled;

    /**
     * 组织描述
     */
    @Size(max = 500, message = "组织描述长度不能超过500个字符")
    private String description;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
}