package com.financial.sharing.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 财务组织返回对象
 *
 * @author system
 * @since 2024-12-19
 */
@Data
public class FinanceOrganizationVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 组织名称 - 用于树形结构显示
     */
    private String orgName;

    /**
     * 组织类型(1公司2部门3科室4小组)
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

    /**
     * 删除标识(0否1是)
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    /**
     * 子组织列表（用于树形结构）
     */
    private List<FinanceOrganizationVO> children;

    /**
     * 获取组织类型名称
     */
    public String getOrgTypeName() {
        if (this.orgType == null) {
            return "未知";
        }
        switch (this.orgType) {
            case 1:
                return "公司";
            case 2:
                return "部门";
            case 3:
                return "科室";
            case 4:
                return "小组";
            default:
                return "未知";
        }
    }
}