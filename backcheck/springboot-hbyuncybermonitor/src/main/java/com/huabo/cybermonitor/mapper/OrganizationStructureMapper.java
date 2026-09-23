package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.OrganizationStructure;
import com.huabo.cybermonitor.vo.OrganizationStructureQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 组织架构管理Mapper接口
 *
 * @author huabo
 * @since 2024-12-12
 */
@Mapper
public interface OrganizationStructureMapper extends BaseMapper<OrganizationStructure> {

    /**
     * 分页查询组织架构列表
     *
     * @param page 分页参数
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<OrganizationStructure> selectOrganizationStructurePage(Page<OrganizationStructure> page, @Param("queryVo") OrganizationStructureQueryVo queryVo);

    /**
     * 查询组织架构列表
     *
     * @param queryVo 查询条件
     * @return 组织架构列表
     */
    List<OrganizationStructure> selectOrganizationStructureList(@Param("queryVo") OrganizationStructureQueryVo queryVo);

    /**
     * 查询组织架构树形结构
     *
     * @param enterpriseId 企业ID
     * @param parentOrgId 父级组织ID
     * @return 树形结构
     */
    List<OrganizationStructure> selectOrganizationTree(@Param("enterpriseId") String enterpriseId, @Param("parentOrgId") String parentOrgId);

    /**
     * 根据企业ID查询组织架构统计
     *
     * @param enterpriseId 企业ID
     * @return 统计结果
     */
    Map<String, Object> selectOrganizationStatisticsByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询组织层级分布
     *
     * @param enterpriseId 企业ID
     * @return 层级分布
     */
    List<Map<String, Object>> selectOrganizationLevelDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询组织类型分布
     *
     * @param enterpriseId 企业ID
     * @return 类型分布
     */
    List<Map<String, Object>> selectOrganizationTypeDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询组织人员分布
     *
     * @param enterpriseId 企业ID
     * @return 人员分布
     */
    List<Map<String, Object>> selectStaffDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询组织效率分析
     *
     * @param enterpriseId 企业ID
     * @return 效率分析
     */
    Map<String, Object> selectOrganizationEfficiencyAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询管理幅度分析
     *
     * @param enterpriseId 企业ID
     * @return 管理幅度分析
     */
    List<Map<String, Object>> selectManagementSpanAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询组织变更历史
     *
     * @param enterpriseId 企业ID
     * @param months 月份数
     * @return 变更历史
     */
    List<Map<String, Object>> selectOrganizationChangeHistory(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 查询子组织列表
     *
     * @param parentOrgId 父级组织ID
     * @return 子组织列表
     */
    List<OrganizationStructure> selectChildOrganizations(@Param("parentOrgId") String parentOrgId);

    /**
     * 查询父级组织路径
     *
     * @param orgId 组织ID
     * @return 父级组织路径
     */
    List<OrganizationStructure> selectParentOrganizationPath(@Param("orgId") String orgId);

    /**
     * 更新组织路径
     *
     * @param orgId 组织ID
     * @param orgPath 组织路径
     * @return 更新数量
     */
    int updateOrganizationPath(@Param("orgId") String orgId, @Param("orgPath") String orgPath);

    /**
     * 批量更新组织状态
     *
     * @param orgIds 组织ID列表
     * @param status 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateOrganizationStatus(@Param("orgIds") List<String> orgIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量删除组织
     *
     * @param orgIds 组织ID列表
     * @param updateBy 更新人
     * @return 删除数量
     */
    int batchDeleteOrganizations(@Param("orgIds") List<String> orgIds, @Param("updateBy") String updateBy);

    /**
     * 查询组织协作关系
     *
     * @param enterpriseId 企业ID
     * @return 协作关系
     */
    List<Map<String, Object>> selectOrganizationCollaborationRelationships(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询组织汇报关系
     *
     * @param enterpriseId 企业ID
     * @return 汇报关系
     */
    List<Map<String, Object>> selectOrganizationReportingRelationships(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询组织监督关系
     *
     * @param enterpriseId 企业ID
     * @return 监督关系
     */
    List<Map<String, Object>> selectOrganizationSupervisionRelationships(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询组织绩效统计
     *
     * @param enterpriseId 企业ID
     * @return 绩效统计
     */
    Map<String, Object> selectOrganizationPerformanceStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询组织预算统计
     *
     * @param enterpriseId 企业ID
     * @return 预算统计
     */
    Map<String, Object> selectOrganizationBudgetStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询组织成本统计
     *
     * @param enterpriseId 企业ID
     * @return 成本统计
     */
    Map<String, Object> selectOrganizationCostStatistics(@Param("enterpriseId") String enterpriseId);
}
