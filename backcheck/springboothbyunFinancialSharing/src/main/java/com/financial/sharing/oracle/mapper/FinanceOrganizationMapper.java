package com.financial.sharing.oracle.mapper;

import com.financial.sharing.oracle.entity.FinanceOrganizationEntity;
import com.financial.sharing.vo.result.FinanceOrganizationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 财务组织Mapper接口
 *
 * @author system
 * @since 2024-12-19
 */
public interface FinanceOrganizationMapper {

    /**
     * 查询组织树形结构
     *
     * @param tenantId 租户ID
     * @param bookId   账簿ID
     * @return 组织树形列表
     */
    List<FinanceOrganizationVO> selectOrganizationTree(@Param("tenantId") Long tenantId,
                                                      @Param("bookId") Long bookId);

    /**
     * 根据ID查询组织
     *
     * @param id 主键ID
     * @return 组织信息
     */
    FinanceOrganizationVO selectOrganizationById(@Param("id") Long id);

    /**
     * 查询子组织数量
     *
     * @param parentId 父组织ID
     * @return 子组织数量
     */
    Integer selectChildCount(@Param("parentId") Long parentId);

    /**
     * 插入组织
     *
     * @param entity 组织实体
     * @return 影响行数
     */
    int insertOrganization(FinanceOrganizationEntity entity);

    /**
     * 更新组织
     *
     * @param entity 组织实体
     * @return 影响行数
     */
    int updateOrganization(FinanceOrganizationEntity entity);

    /**
     * 更新组织状态
     *
     * @param id         主键ID
     * @param isEnabled  启用状态
     * @param updateBy   更新人
     * @return 影响行数
     */
    int updateOrganizationStatus(@Param("id") Long id,
                                @Param("isEnabled") Integer isEnabled,
                                @Param("updateBy") Long updateBy);

    /**
     * 删除组织（逻辑删除）
     *
     * @param id       主键ID
     * @param updateBy 更新人
     * @return 影响行数
     */
    int deleteOrganization(@Param("id") Long id, @Param("updateBy") Long updateBy);

    /**
     * 检查组织编码是否存在
     *
     * @param orgCode  组织编码
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @param id       排除的ID（更新时使用）
     * @return 存在数量
     */
    Integer checkOrgCodeExists(@Param("orgCode") String orgCode,
                              @Param("bookId") Long bookId,
                              @Param("tenantId") Long tenantId,
                              @Param("id") Long id);

    /**
     * 查询最大排序号
     *
     * @param parentId 父组织ID
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @return 最大排序号
     */
    Integer selectMaxSortOrder(@Param("parentId") Long parentId,
                              @Param("bookId") Long bookId,
                              @Param("tenantId") Long tenantId);
}