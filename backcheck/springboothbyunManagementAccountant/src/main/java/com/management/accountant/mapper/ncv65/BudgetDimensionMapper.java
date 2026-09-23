package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetDimension;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算维度Mapper接口
 * 
 * @description 预算维度数据访问层接口，兼容达梦数据库和MySQL数据库
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Mapper
public interface BudgetDimensionMapper extends BaseMapper<BudgetDimension> {

    /**
     * 根据维度编码查询维度
     * @param dimensionCode 维度编码
     * @param tenantId 租户ID
     * @return 维度信息
     */
    @Select("SELECT * FROM BUDGET_DIMENSION WHERE DIMENSION_CODE = #{dimensionCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetDimension selectByDimensionCode(@Param("dimensionCode") String dimensionCode, @Param("tenantId") String tenantId);

    /**
     * 根据维度类型查询维度列表
     * @param dimensionType 维度类型
     * @param tenantId 租户ID
     * @return 维度列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION WHERE DIMENSION_TYPE = #{dimensionType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC, CREATE_TIME DESC")
    List<BudgetDimension> selectByDimensionType(@Param("dimensionType") String dimensionType, @Param("tenantId") String tenantId);

    /**
     * 查询启用的维度列表
     * @param tenantId 租户ID
     * @return 启用的维度列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION WHERE IS_ENABLED = 1 AND STATUS = 'active' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimension> selectEnabledDimensions(@Param("tenantId") String tenantId);

    /**
     * 查询必填维度列表
     * @param tenantId 租户ID
     * @return 必填维度列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION WHERE IS_REQUIRED = 1 AND IS_ENABLED = 1 AND STATUS = 'active' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimension> selectRequiredDimensions(@Param("tenantId") String tenantId);

    /**
     * 查询系统维度列表
     * @param tenantId 租户ID
     * @return 系统维度列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION WHERE IS_SYSTEM = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimension> selectSystemDimensions(@Param("tenantId") String tenantId);

    /**
     * 分页查询维度列表（支持多条件查询）
     * @param page 分页参数
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetDimension> selectPageWithConditions(Page<BudgetDimension> page, @Param("params") Map<String, Object> params);

    /**
     * 查询维度树结构
     * @param parentId 父维度ID
     * @param tenantId 租户ID
     * @return 维度树列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION WHERE PARENT_ID = #{parentId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimension> selectDimensionTree(@Param("parentId") String parentId, @Param("tenantId") String tenantId);

    /**
     * 查询根维度列表
     * @param tenantId 租户ID
     * @return 根维度列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION WHERE (PARENT_ID IS NULL OR PARENT_ID = '') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimension> selectRootDimensions(@Param("tenantId") String tenantId);

    /**
     * 统计各维度类型的数量
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<Map<String, Object>> countByDimensionType(@Param("tenantId") String tenantId);

    /**
     * 统计各状态的数量
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus(@Param("tenantId") String tenantId);

    /**
     * 检查维度编码是否存在
     * @param dimensionCode 维度编码
     * @param excludeId 排除的ID（用于更新时检查）
     * @param tenantId 租户ID
     * @return 存在数量
     */
    @Select("SELECT COUNT(*) FROM BUDGET_DIMENSION WHERE DIMENSION_CODE = #{dimensionCode} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkDimensionCodeExists(@Param("dimensionCode") String dimensionCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

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
     * 根据数据来源查询维度
     * @param dataSource 数据来源
     * @param tenantId 租户ID
     * @return 维度列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION WHERE DATA_SOURCE = #{dataSource} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimension> selectByDataSource(@Param("dataSource") String dataSource, @Param("tenantId") String tenantId);

    /**
     * 获取维度使用情况统计
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 使用情况统计
     */
    Map<String, Object> getDimensionUsageStatistics(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    /**
     * 复制维度
     * @param sourceId 源维度ID
     * @param targetDimension 目标维度信息
     * @return 插入数量
     */
    int copyDimension(@Param("sourceId") String sourceId, @Param("target") BudgetDimension targetDimension);

    /**
     * 查询维度层级统计
     * @param tenantId 租户ID
     * @return 层级统计
     */
    List<Map<String, Object>> getDimensionLevelStatistics(@Param("tenantId") String tenantId);

    /**
     * 根据层级查询维度
     * @param dimensionLevel 维度层级
     * @param tenantId 租户ID
     * @return 维度列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION WHERE DIMENSION_LEVEL = #{dimensionLevel} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimension> selectByDimensionLevel(@Param("dimensionLevel") Integer dimensionLevel, @Param("tenantId") String tenantId);

    /**
     * 获取维度的最大排序号
     * @param parentId 父维度ID
     * @param tenantId 租户ID
     * @return 最大排序号
     */
    @Select("SELECT COALESCE(MAX(SORT_ORDER), 0) FROM BUDGET_DIMENSION WHERE PARENT_ID = #{parentId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    Integer getMaxSortOrder(@Param("parentId") String parentId, @Param("tenantId") String tenantId);

    /**
     * 更新排序号
     * @param id 维度ID
     * @param sortOrder 排序号
     * @param updateBy 更新人
     * @return 更新数量
     */
    int updateSortOrder(@Param("id") String id, @Param("sortOrder") Integer sortOrder, @Param("updateBy") String updateBy);

    /**
     * 查询子维度数量
     * @param parentId 父维度ID
     * @param tenantId 租户ID
     * @return 子维度数量
     */
    @Select("SELECT COUNT(*) FROM BUDGET_DIMENSION WHERE PARENT_ID = #{parentId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countChildDimensions(@Param("parentId") String parentId, @Param("tenantId") String tenantId);

    /**
     * 根据创建时间范围查询
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param tenantId 租户ID
     * @return 维度列表
     */
    List<BudgetDimension> selectByCreateTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("tenantId") String tenantId);

    /**
     * 获取维度趋势数据
     * @param tenantId 租户ID
     * @param months 月份数
     * @return 趋势数据
     */
    List<Map<String, Object>> getDimensionTrendData(@Param("tenantId") String tenantId, @Param("months") Integer months);
}
