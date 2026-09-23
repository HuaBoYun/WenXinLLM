package com.financial.sharing.service;

import com.financial.sharing.oracle.entity.OrganizationEntity;
import com.financial.sharing.vo.param.OrganizationSaveParam;
import com.financial.sharing.vo.result.OrganizationVO;

import java.util.List;

/**
 * 组织架构配置Service接口
 * @author system
 * @since 2024-12-19
 */
public interface OrganizationService {

    /**
     * 获取组织架构树
     * @param tenantId 租户ID
     * @return 组织树列表
     */
    List<OrganizationVO> getOrganizationTree(Long tenantId);

    /**
     * 根据ID查询组织详情
     * @param id 组织ID
     * @return 组织详情
     */
    OrganizationVO getOrganizationById(Long id);

    /**
     * 保存组织信息
     * @param param 保存参数
     * @return 保存结果
     */
    boolean saveOrganization(OrganizationSaveParam param);

    /**
     * 更新组织信息
     * @param param 更新参数
     * @return 更新结果
     */
    boolean updateOrganization(OrganizationSaveParam param);

    /**
     * 删除组织
     * @param id 组织ID
     * @return 删除结果
     */
    boolean deleteOrganization(Long id);

    /**
     * 更新组织状态
     * @param id 组织ID
     * @param isEnabled 启用状态
     * @return 更新结果
     */
    boolean updateOrganizationStatus(Long id, Integer isEnabled);

    /**
     * 获取下级组织列表
     * @param parentId 父级ID
     * @param tenantId 租户ID
     * @return 子组织列表
     */
    List<OrganizationVO> getChildOrganizations(Long parentId, Long tenantId);

    /**
     * 同步组织数据
     * @return 同步结果
     */
    String syncOrganizations();

    /**
     * 根据组织类型查询
     * @param orgType 组织类型(1公司 2部门 3科室 4小组)
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<OrganizationVO> getByOrgType(Integer orgType, Long tenantId);

    /**
     * 移动组织到新的父级下
     * @param orgId 组织ID
     * @param newParentId 新父级ID
     * @param tenantId 租户ID
     * @return 移动结果
     */
    boolean moveOrganization(Long orgId, Long newParentId, Long tenantId);
}