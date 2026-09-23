package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.OrganizationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织架构Mapper
 * @author system
 * @since 2024-12-19
 */
public interface OrganizationMapper extends BaseMapper<OrganizationEntity> {

    /**
     * 获取组织架构树
     * @param tenantId 租户ID
     * @return 组织树列表
     */
    List<OrganizationEntity> getOrganizationTree(@Param("tenantId") Long tenantId);

    /**
     * 根据父级ID查询子组织
     * @param parentId 父级ID
     * @param tenantId 租户ID
     * @return 子组织列表
     */
    List<OrganizationEntity> getChildOrganizations(@Param("parentId") Long parentId,
                                                  @Param("tenantId") Long tenantId);

    /**
     * 根据组织编码查询
     * @param orgCode 组织编码
     * @param tenantId 租户ID
     * @return 组织信息
     */
    OrganizationEntity getByOrgCode(@Param("orgCode") String orgCode,
                                    @Param("tenantId") Long tenantId);

    /**
     * 检查组织编码是否存在
     * @param orgCode 组织编码
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 存在数量
     */
    int checkOrgCodeExists(@Param("orgCode") String orgCode,
                          @Param("tenantId") Long tenantId,
                          @Param("excludeId") Long excludeId);

    /**
     * 获取子组织数量
     * @param orgId 组织ID
     * @return 子组织数量
     */
    int getChildOrganizationsCount(@Param("orgId") Long orgId);

    /**
     * 获取子部门数量
     * @param orgId 组织ID
     * @return 子部门数量
     */
    int getChildDepartmentsCount(@Param("orgId") Long orgId);

    /**
     * 检查是否有子组织
     * @param orgId 组织ID
     * @param tenantId 租户ID
     * @return 子组织数量
     */
    int countChildOrganizations(@Param("orgId") Long orgId,
                                @Param("tenantId") Long tenantId);

    /**
     * 批量更新状态
     * @param ids ID列表
     * @param isEnabled 启用状态
     * @param tenantId 租户ID
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("isEnabled") Integer isEnabled, @Param("tenantId") Long tenantId);

    /**
     * 获取最大组织层级
     * @param tenantId 租户ID
     * @return 最大层级
     */
    Integer getMaxOrgLevel(@Param("tenantId") Long tenantId);

    /**
     * 根据组织类型查询
     * @param orgType 组织类型(1公司 2部门 3科室 4小组)
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<OrganizationEntity> getByOrgType(@Param("orgType") Integer orgType,
                                         @Param("tenantId") Long tenantId);
}