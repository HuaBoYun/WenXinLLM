package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetIndicator;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算指标数据访问层接口
 * 
 * @description 预算指标数据访问层，支持指标的CRUD操作和复杂查询
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Mapper
public interface BudgetIndicatorMapper extends BaseMapper<BudgetIndicator> {

    /**
     * 根据指标编码查询指标
     */
    @Select("SELECT * FROM BUDGET_INDICATOR WHERE INDICATOR_CODE = #{indicatorCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetIndicator selectByIndicatorCode(@Param("indicatorCode") String indicatorCode, @Param("tenantId") String tenantId);

    /**
     * 根据指标类型查询指标列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR WHERE INDICATOR_TYPE = #{indicatorType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicator> selectByIndicatorType(@Param("indicatorType") String indicatorType, @Param("tenantId") String tenantId);

    /**
     * 查询启用的指标列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR WHERE IS_ENABLED = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicator> selectEnabledIndicators(@Param("tenantId") String tenantId);

    /**
     * 查询必填指标列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR WHERE IS_REQUIRED = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicator> selectRequiredIndicators(@Param("tenantId") String tenantId);

    /**
     * 查询系统指标列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR WHERE IS_SYSTEM = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicator> selectSystemIndicators(@Param("tenantId") String tenantId);

    /**
     * 查询可计算指标列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR WHERE IS_CALCULATED = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicator> selectCalculatedIndicators(@Param("tenantId") String tenantId);

    /**
     * 根据父指标ID查询子指标列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR WHERE PARENT_ID = #{parentId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicator> selectByParentId(@Param("parentId") String parentId, @Param("tenantId") String tenantId);

    /**
     * 查询指标树结构
     */
    @Select("SELECT * FROM BUDGET_INDICATOR WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicator> selectIndicatorTree(@Param("tenantId") String tenantId);

    /**
     * 根据指标级别查询指标列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR WHERE INDICATOR_LEVEL = #{level} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicator> selectByLevel(@Param("level") Integer level, @Param("tenantId") String tenantId);

    /**
     * 获取指定父指标下的最大排序号
     */
    @Select("SELECT COALESCE(MAX(SORT_ORDER), 0) FROM BUDGET_INDICATOR WHERE PARENT_ID = #{parentId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    Integer getMaxSortOrder(@Param("parentId") String parentId, @Param("tenantId") String tenantId);

    /**
     * 分页查询指标列表（带条件）
     */
    @Select("<script>" +
            "SELECT * FROM BUDGET_INDICATOR WHERE TENANT_ID = #{params.tenantId} AND IS_DELETED = 0" +
            "<if test='params.indicatorName != null and params.indicatorName != \"\"'>" +
            " AND INDICATOR_NAME LIKE CONCAT('%', #{params.indicatorName}, '%')" +
            "</if>" +
            "<if test='params.indicatorCode != null and params.indicatorCode != \"\"'>" +
            " AND INDICATOR_CODE LIKE CONCAT('%', #{params.indicatorCode}, '%')" +
            "</if>" +
            "<if test='params.indicatorType != null and params.indicatorType != \"\"'>" +
            " AND INDICATOR_TYPE = #{params.indicatorType}" +
            "</if>" +
            "<if test='params.dataType != null and params.dataType != \"\"'>" +
            " AND DATA_TYPE = #{params.dataType}" +
            "</if>" +
            "<if test='params.isEnabled != null'>" +
            " AND IS_ENABLED = #{params.isEnabled}" +
            "</if>" +
            "<if test='params.isRequired != null'>" +
            " AND IS_REQUIRED = #{params.isRequired}" +
            "</if>" +
            "<if test='params.isSystem != null'>" +
            " AND IS_SYSTEM = #{params.isSystem}" +
            "</if>" +
            "<if test='params.isCalculated != null'>" +
            " AND IS_CALCULATED = #{params.isCalculated}" +
            "</if>" +
            "<if test='params.status != null and params.status != \"\"'>" +
            " AND STATUS = #{params.status}" +
            "</if>" +
            " ORDER BY SORT_ORDER ASC, CREATE_TIME DESC" +
            "</script>")
    IPage<BudgetIndicator> selectPageWithConditions(Page<BudgetIndicator> page, @Param("params") Map<String, Object> params);

    /**
     * 批量更新状态
     */
    @Update("<script>" +
            "UPDATE BUDGET_INDICATOR SET STATUS = #{status}, UPDATE_TIME = NOW()" +
            " WHERE ID IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            " AND TENANT_ID = #{tenantId}" +
            "</script>")
    int batchUpdateStatus(@Param("ids") List<String> ids, @Param("status") String status, @Param("tenantId") String tenantId);

    /**
     * 批量更新启用状态
     */
    @Update("<script>" +
            "UPDATE BUDGET_INDICATOR SET IS_ENABLED = #{isEnabled}, UPDATE_TIME = NOW()" +
            " WHERE ID IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            " AND TENANT_ID = #{tenantId}" +
            "</script>")
    int batchUpdateEnabled(@Param("ids") List<String> ids, @Param("isEnabled") Boolean isEnabled, @Param("tenantId") String tenantId);

    /**
     * 统计各指标类型的数量
     */
    @Select("SELECT INDICATOR_TYPE as type, COUNT(*) as count FROM BUDGET_INDICATOR WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0 GROUP BY INDICATOR_TYPE")
    List<Map<String, Object>> countByIndicatorType(@Param("tenantId") String tenantId);

    /**
     * 统计各数据类型的数量
     */
    @Select("SELECT DATA_TYPE as type, COUNT(*) as count FROM BUDGET_INDICATOR WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0 GROUP BY DATA_TYPE")
    List<Map<String, Object>> countByDataType(@Param("tenantId") String tenantId);

    /**
     * 统计各状态的数量
     */
    @Select("SELECT STATUS as status, COUNT(*) as count FROM BUDGET_INDICATOR WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0 GROUP BY STATUS")
    List<Map<String, Object>> countByStatus(@Param("tenantId") String tenantId);

    /**
     * 检查指标编码是否存在
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM BUDGET_INDICATOR WHERE INDICATOR_CODE = #{indicatorCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0" +
            "<if test='excludeId != null and excludeId != \"\"'>" +
            " AND ID != #{excludeId}" +
            "</if>" +
            "</script>")
    int checkIndicatorCodeExists(@Param("indicatorCode") String indicatorCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 根据公式查询相关指标
     */
    @Select("SELECT * FROM BUDGET_INDICATOR WHERE FORMULA LIKE CONCAT('%', #{indicatorCode}, '%') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    List<BudgetIndicator> selectByFormulaContains(@Param("indicatorCode") String indicatorCode, @Param("tenantId") String tenantId);

    /**
     * 查询指标的层级路径
     */
    @Select("<script>" +
            "WITH RECURSIVE indicator_path AS (" +
            "  SELECT ID, INDICATOR_CODE, INDICATOR_NAME, PARENT_ID, 0 as level, CAST(INDICATOR_NAME AS CHAR(1000)) as path" +
            "  FROM BUDGET_INDICATOR WHERE ID = #{indicatorId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0" +
            "  UNION ALL" +
            "  SELECT p.ID, p.INDICATOR_CODE, p.INDICATOR_NAME, p.PARENT_ID, ip.level + 1, CONCAT(p.INDICATOR_NAME, ' > ', ip.path)" +
            "  FROM BUDGET_INDICATOR p INNER JOIN indicator_path ip ON p.ID = ip.PARENT_ID" +
            "  WHERE p.TENANT_ID = #{tenantId} AND p.IS_DELETED = 0" +
            ")" +
            "SELECT path FROM indicator_path ORDER BY level DESC LIMIT 1" +
            "</script>")
    String getIndicatorPath(@Param("indicatorId") String indicatorId, @Param("tenantId") String tenantId);

    /**
     * 更新指标排序
     */
    @Update("UPDATE BUDGET_INDICATOR SET SORT_ORDER = #{sortOrder}, UPDATE_TIME = NOW() WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int updateSortOrder(@Param("id") String id, @Param("sortOrder") Integer sortOrder, @Param("tenantId") String tenantId);

    /**
     * 移动指标到新的父节点
     */
    @Update("UPDATE BUDGET_INDICATOR SET PARENT_ID = #{newParentId}, INDICATOR_LEVEL = #{newLevel}, UPDATE_TIME = NOW() WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int moveIndicator(@Param("id") String id, @Param("newParentId") String newParentId, @Param("newLevel") Integer newLevel, @Param("tenantId") String tenantId);

    /**
     * 同步指标数据
     */
    @Update("UPDATE BUDGET_INDICATOR SET SYNC_TIME = NOW(), SYNC_STATUS = #{syncStatus} WHERE ID = #{id} AND TENANT_ID = #{tenantId}")
    int syncIndicatorData(@Param("id") String id, @Param("syncStatus") String syncStatus, @Param("tenantId") String tenantId);
}
