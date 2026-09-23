package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetIndicatorCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算指标分类Mapper接口
 * 
 * @description 预算指标分类数据访问层，支持指标分类的层级结构管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetIndicatorCategoryMapper extends BaseMapper<BudgetIndicatorCategory> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据分类编码查询指标分类
     * @param categoryCode 分类编码
     * @param tenantId 租户ID
     * @return 指标分类信息
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE CATEGORY_CODE = #{categoryCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetIndicatorCategory selectByCategoryCode(@Param("categoryCode") String categoryCode, @Param("tenantId") String tenantId);

    /**
     * 根据分类名称查询指标分类
     * @param categoryName 分类名称
     * @param tenantId 租户ID
     * @return 指标分类信息
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE CATEGORY_NAME = #{categoryName} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetIndicatorCategory selectByCategoryName(@Param("categoryName") String categoryName, @Param("tenantId") String tenantId);

    /**
     * 根据父分类ID查询子分类列表
     * @param parentCategoryId 父分类ID
     * @param tenantId 租户ID
     * @return 子分类列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE PARENT_CATEGORY_ID = #{parentCategoryId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicatorCategory> selectByParentCategoryId(@Param("parentCategoryId") String parentCategoryId, @Param("tenantId") String tenantId);

    /**
     * 根据分类层级查询指标分类列表
     * @param categoryLevel 分类层级
     * @param tenantId 租户ID
     * @return 指标分类列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE CATEGORY_LEVEL = #{categoryLevel} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicatorCategory> selectByCategoryLevel(@Param("categoryLevel") Integer categoryLevel, @Param("tenantId") String tenantId);

    /**
     * 根据分类类型查询指标分类列表
     * @param categoryType 分类类型
     * @param tenantId 租户ID
     * @return 指标分类列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE CATEGORY_TYPE = #{categoryType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicatorCategory> selectByCategoryType(@Param("categoryType") String categoryType, @Param("tenantId") String tenantId);

    /**
     * 根据业务类型查询指标分类列表
     * @param businessType 业务类型
     * @param tenantId 租户ID
     * @return 指标分类列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE BUSINESS_TYPE = #{businessType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicatorCategory> selectByBusinessType(@Param("businessType") String businessType, @Param("tenantId") String tenantId);

    /**
     * 查询叶子节点分类列表
     * @param tenantId 租户ID
     * @return 叶子节点分类列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE IS_LEAF = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicatorCategory> selectLeafCategories(@Param("tenantId") String tenantId);

    /**
     * 查询根节点分类列表
     * @param tenantId 租户ID
     * @return 根节点分类列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE (PARENT_CATEGORY_ID IS NULL OR PARENT_CATEGORY_ID = '') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicatorCategory> selectRootCategories(@Param("tenantId") String tenantId);

    /**
     * 查询允许录入数据的分类列表
     * @param tenantId 租户ID
     * @return 允许录入数据的分类列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE IS_DATA_ENTRY_ALLOWED = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicatorCategory> selectDataEntryAllowedCategories(@Param("tenantId") String tenantId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询指标分类
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetIndicatorCategory> selectIndicatorCategoryPage(Page<BudgetIndicatorCategory> page, @Param("params") Map<String, Object> params);

    /**
     * 根据分类路径查询指标分类
     * @param categoryPath 分类路径
     * @param tenantId 租户ID
     * @return 指标分类列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE CATEGORY_PATH LIKE CONCAT(#{categoryPath}, '%') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CATEGORY_PATH ASC")
    List<BudgetIndicatorCategory> selectByCategoryPath(@Param("categoryPath") String categoryPath, @Param("tenantId") String tenantId);

    /**
     * 查询分类的所有祖先节点
     * @param categoryId 分类ID
     * @param tenantId 租户ID
     * @return 祖先节点列表
     */
    List<BudgetIndicatorCategory> selectAncestorCategories(@Param("categoryId") String categoryId, @Param("tenantId") String tenantId);

    /**
     * 查询分类的所有后代节点
     * @param categoryId 分类ID
     * @param tenantId 租户ID
     * @return 后代节点列表
     */
    List<BudgetIndicatorCategory> selectDescendantCategories(@Param("categoryId") String categoryId, @Param("tenantId") String tenantId);

    /**
     * 查询分类的兄弟节点
     * @param categoryId 分类ID
     * @param tenantId 租户ID
     * @return 兄弟节点列表
     */
    List<BudgetIndicatorCategory> selectSiblingCategories(@Param("categoryId") String categoryId, @Param("tenantId") String tenantId);

    /**
     * 查询启用的指标分类
     * @param tenantId 租户ID
     * @return 启用的指标分类列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE IS_ENABLED = 1 AND STATUS = 'active' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicatorCategory> selectEnabledCategories(@Param("tenantId") String tenantId);

    /**
     * 根据外部编码查询指标分类
     * @param externalCode 外部编码
     * @param tenantId 租户ID
     * @return 指标分类信息
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE EXTERNAL_CODE = #{externalCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetIndicatorCategory selectByExternalCode(@Param("externalCode") String externalCode, @Param("tenantId") String tenantId);

    /**
     * 查询系统预置分类
     * @param tenantId 租户ID
     * @return 系统预置分类列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE IS_SYSTEM = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicatorCategory> selectSystemCategories(@Param("tenantId") String tenantId);

    /**
     * 查询用户自定义分类
     * @param tenantId 租户ID
     * @return 用户自定义分类列表
     */
    @Select("SELECT * FROM BUDGET_INDICATOR_CATEGORY WHERE IS_SYSTEM = 0 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetIndicatorCategory> selectCustomCategories(@Param("tenantId") String tenantId);

    // ==================== 统计查询方法 ====================

    /**
     * 统计指标分类总数
     * @param tenantId 租户ID
     * @return 分类总数
     */
    @Select("SELECT COUNT(*) FROM BUDGET_INDICATOR_CATEGORY WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countTotalCategories(@Param("tenantId") String tenantId);

    /**
     * 统计叶子节点分类数量
     * @param tenantId 租户ID
     * @return 叶子节点分类数量
     */
    @Select("SELECT COUNT(*) FROM BUDGET_INDICATOR_CATEGORY WHERE IS_LEAF = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countLeafCategories(@Param("tenantId") String tenantId);

    /**
     * 统计各层级分类数量
     * @param tenantId 租户ID
     * @return 各层级分类数量统计
     */
    List<Map<String, Object>> countCategoriesByLevel(@Param("tenantId") String tenantId);

    /**
     * 统计各类型分类数量
     * @param tenantId 租户ID
     * @return 各类型分类数量统计
     */
    List<Map<String, Object>> countCategoriesByType(@Param("tenantId") String tenantId);

    /**
     * 统计各业务类型分类数量
     * @param tenantId 租户ID
     * @return 各业务类型分类数量统计
     */
    List<Map<String, Object>> countCategoriesByBusinessType(@Param("tenantId") String tenantId);

    /**
     * 获取指标分类统计信息
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectCategoryStatistics(@Param("tenantId") String tenantId);

    // ==================== 业务操作方法 ====================

    /**
     * 批量更新分类状态
     * @param categoryIds 分类ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateCategoryStatus(@Param("categoryIds") List<String> categoryIds, @Param("status") String status, 
                                 @Param("updateBy") String updateBy, @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 批量更新分类启用状态
     * @param categoryIds 分类ID列表
     * @param isEnabled 是否启用
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateCategoryEnabled(@Param("categoryIds") List<String> categoryIds, @Param("isEnabled") Boolean isEnabled, 
                                  @Param("updateBy") String updateBy, @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 更新分类路径
     * @param categoryId 分类ID
     * @param categoryPath 分类路径
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_INDICATOR_CATEGORY SET CATEGORY_PATH = #{categoryPath}, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{categoryId}")
    int updateCategoryPath(@Param("categoryId") String categoryId, @Param("categoryPath") String categoryPath, 
                          @Param("updateBy") String updateBy, @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 更新分类层级
     * @param categoryId 分类ID
     * @param categoryLevel 分类层级
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_INDICATOR_CATEGORY SET CATEGORY_LEVEL = #{categoryLevel}, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{categoryId}")
    int updateCategoryLevel(@Param("categoryId") String categoryId, @Param("categoryLevel") Integer categoryLevel, 
                           @Param("updateBy") String updateBy, @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 更新叶子节点标志
     * @param categoryId 分类ID
     * @param isLeaf 是否叶子节点
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_INDICATOR_CATEGORY SET IS_LEAF = #{isLeaf}, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{categoryId}")
    int updateCategoryLeafFlag(@Param("categoryId") String categoryId, @Param("isLeaf") Boolean isLeaf, 
                              @Param("updateBy") String updateBy, @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 更新数据录入权限
     * @param categoryId 分类ID
     * @param isDataEntryAllowed 是否允许录入数据
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_INDICATOR_CATEGORY SET IS_DATA_ENTRY_ALLOWED = #{isDataEntryAllowed}, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{categoryId}")
    int updateDataEntryPermission(@Param("categoryId") String categoryId, @Param("isDataEntryAllowed") Boolean isDataEntryAllowed, 
                                  @Param("updateBy") String updateBy, @Param("updateTime") java.time.LocalDateTime updateTime);

    // ==================== 数据清理方法 ====================

    /**
     * 清理过期的指标分类
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupExpiredCategories(@Param("tenantId") String tenantId);

    /**
     * 重建分类路径
     * @param tenantId 租户ID
     * @return 重建数量
     */
    int rebuildCategoryPaths(@Param("tenantId") String tenantId);

    /**
     * 重建分类层级
     * @param tenantId 租户ID
     * @return 重建数量
     */
    int rebuildCategoryLevels(@Param("tenantId") String tenantId);

    // ==================== 验证方法 ====================

    /**
     * 检查分类编码是否存在
     * @param categoryCode 分类编码
     * @param excludeId 排除的分类ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_INDICATOR_CATEGORY WHERE CATEGORY_CODE = #{categoryCode} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkCategoryCodeExists(@Param("categoryCode") String categoryCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查分类名称是否存在
     * @param categoryName 分类名称
     * @param excludeId 排除的分类ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_INDICATOR_CATEGORY WHERE CATEGORY_NAME = #{categoryName} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkCategoryNameExists(@Param("categoryName") String categoryName, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查是否存在循环引用
     * @param categoryId 分类ID
     * @param parentCategoryId 父分类ID
     * @param tenantId 租户ID
     * @return 是否存在循环引用
     */
    boolean checkCircularReference(@Param("categoryId") String categoryId, @Param("parentCategoryId") String parentCategoryId, @Param("tenantId") String tenantId);

    /**
     * 检查分类是否有子节点
     * @param categoryId 分类ID
     * @param tenantId 租户ID
     * @return 是否有子节点
     */
    @Select("SELECT COUNT(*) FROM BUDGET_INDICATOR_CATEGORY WHERE PARENT_CATEGORY_ID = #{categoryId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkHasChildren(@Param("categoryId") String categoryId, @Param("tenantId") String tenantId);

    /**
     * 检查分类是否被指标使用
     * @param categoryId 分类ID
     * @param tenantId 租户ID
     * @return 是否被使用
     */
    int checkCategoryInUse(@Param("categoryId") String categoryId, @Param("tenantId") String tenantId);
}
