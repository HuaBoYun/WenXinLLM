package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetOrganizationStructure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算组织体系Mapper接口
 * 
 * @description 预算组织体系数据访问层接口，兼容达梦数据库和MySQL数据库
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Mapper
public interface BudgetOrganizationStructureMapper extends BaseMapper<BudgetOrganizationStructure> {

    /**
     * 根据体系编码查询组织体系
     * @param structureCode 体系编码
     * @param tenantId 租户ID
     * @return 组织体系信息
     */
    @Select("SELECT * FROM BUDGET_ORGANIZATION_STRUCTURE WHERE STRUCTURE_CODE = #{structureCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetOrganizationStructure selectByStructureCode(@Param("structureCode") String structureCode, @Param("tenantId") String tenantId);

    /**
     * 根据体系类型查询组织体系列表
     * @param structureType 体系类型
     * @param tenantId 租户ID
     * @return 组织体系列表
     */
    @Select("SELECT * FROM BUDGET_ORGANIZATION_STRUCTURE WHERE STRUCTURE_TYPE = #{structureType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetOrganizationStructure> selectByStructureType(@Param("structureType") String structureType, @Param("tenantId") String tenantId);

    /**
     * 查询启用的组织体系列表
     * @param tenantId 租户ID
     * @return 启用的组织体系列表
     */
    @Select("SELECT * FROM BUDGET_ORGANIZATION_STRUCTURE WHERE IS_ENABLED = 1 AND STATUS = 'active' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetOrganizationStructure> selectEnabledStructures(@Param("tenantId") String tenantId);

    /**
     * 分页查询组织体系列表（支持多条件查询）
     * @param page 分页参数
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetOrganizationStructure> selectPageWithConditions(Page<BudgetOrganizationStructure> page, @Param("params") Map<String, Object> params);

    /**
     * 统计各体系类型的数量
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<Map<String, Object>> countByStructureType(@Param("tenantId") String tenantId);

    /**
     * 统计各状态的数量
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus(@Param("tenantId") String tenantId);

    /**
     * 检查体系编码是否存在
     * @param structureCode 体系编码
     * @param excludeId 排除的ID（用于更新时检查）
     * @param tenantId 租户ID
     * @return 存在数量
     */
    @Select("SELECT COUNT(*) FROM BUDGET_ORGANIZATION_STRUCTURE WHERE STRUCTURE_CODE = #{structureCode} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkStructureCodeExists(@Param("structureCode") String structureCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 批量更新状态
     * @param ids ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @param tenantId 租户ID
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ids") List<String> ids, @Param("status") String status, @Param("updateBy") String updateBy, @Param("tenantId") String tenantId);

    /**
     * 批量启用/禁用
     * @param ids ID列表
     * @param isEnabled 是否启用
     * @param updateBy 更新人
     * @param tenantId 租户ID
     * @return 更新数量
     */
    int batchUpdateEnabled(@Param("ids") List<String> ids, @Param("isEnabled") Boolean isEnabled, @Param("updateBy") String updateBy, @Param("tenantId") String tenantId);

    /**
     * 根据控制模式查询组织体系
     * @param controlMode 控制模式
     * @param tenantId 租户ID
     * @return 组织体系列表
     */
    @Select("SELECT * FROM BUDGET_ORGANIZATION_STRUCTURE WHERE CONTROL_MODE = #{controlMode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetOrganizationStructure> selectByControlMode(@Param("controlMode") String controlMode, @Param("tenantId") String tenantId);

    /**
     * 查询即将过期的组织体系
     * @param days 天数
     * @param tenantId 租户ID
     * @return 即将过期的组织体系列表
     */
    List<BudgetOrganizationStructure> selectExpiringStructures(@Param("days") Integer days, @Param("tenantId") String tenantId);

    /**
     * 查询已过期的组织体系
     * @param tenantId 租户ID
     * @return 已过期的组织体系列表
     */
    List<BudgetOrganizationStructure> selectExpiredStructures(@Param("tenantId") String tenantId);

    /**
     * 获取组织体系使用情况统计
     * @param structureId 体系ID
     * @param tenantId 租户ID
     * @return 使用情况统计
     */
    Map<String, Object> getStructureUsageStatistics(@Param("structureId") String structureId, @Param("tenantId") String tenantId);

    /**
     * 复制组织体系
     * @param sourceId 源体系ID
     * @param targetStructure 目标体系信息
     * @return 插入数量
     */
    int copyStructure(@Param("sourceId") String sourceId, @Param("target") BudgetOrganizationStructure targetStructure);

    /**
     * 查询体系层级统计
     * @param tenantId 租户ID
     * @return 层级统计
     */
    List<Map<String, Object>> getLevelStatistics(@Param("tenantId") String tenantId);

    /**
     * 根据创建时间范围查询
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param tenantId 租户ID
     * @return 组织体系列表
     */
    List<BudgetOrganizationStructure> selectByCreateTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("tenantId") String tenantId);

    /**
     * 获取体系趋势数据
     * @param tenantId 租户ID
     * @param months 月份数
     * @return 趋势数据
     */
    List<Map<String, Object>> getStructureTrendData(@Param("tenantId") String tenantId, @Param("months") Integer months);
}
