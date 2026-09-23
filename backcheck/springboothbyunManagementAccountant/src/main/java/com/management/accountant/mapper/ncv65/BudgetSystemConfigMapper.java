package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetSystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 系统配置数据访问接口
 * 
 * @description 系统配置数据访问层，提供系统配置的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetSystemConfigMapper extends BaseMapper<BudgetSystemConfig> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据配置编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_SYSTEM_CONFIG WHERE CONFIG_CODE = #{configCode} AND IS_DELETED = 0")
    BudgetSystemConfig selectByConfigCode(@Param("configCode") String configCode);

    /**
     * 根据配置分类查询配置列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_SYSTEM_CONFIG WHERE CONFIG_CATEGORY = #{configCategory} AND IS_DELETED = 0 ORDER BY CONFIG_CODE")
    List<BudgetSystemConfig> selectByConfigCategory(@Param("configCategory") String configCategory);

    /**
     * 根据数据类型查询配置列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_SYSTEM_CONFIG WHERE DATA_TYPE = #{dataType} AND IS_DELETED = 0 ORDER BY CONFIG_CODE")
    List<BudgetSystemConfig> selectByDataType(@Param("dataType") String dataType);

    /**
     * 查询可修改的配置列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_SYSTEM_CONFIG WHERE IS_MODIFIABLE = 1 AND IS_DELETED = 0 ORDER BY CONFIG_CODE")
    List<BudgetSystemConfig> selectModifiableConfigs();

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询系统配置
     */
    IPage<BudgetSystemConfig> selectBudgetSystemConfigPage(Page<BudgetSystemConfig> page, @Param("params") Map<String, Object> params);

    /**
     * 查询所有系统配置
     */
    @Select("SELECT * FROM NCV65_BUDGET_SYSTEM_CONFIG WHERE IS_DELETED = 0 ORDER BY CONFIG_CATEGORY, CONFIG_CODE")
    List<BudgetSystemConfig> selectAllConfigs();

    /**
     * 根据关键字搜索配置
     */
    List<BudgetSystemConfig> searchConfigs(@Param("keyword") String keyword);

    // ==================== 业务操作方法 ====================

    /**
     * 更新配置值
     */
    int updateConfigValue(@Param("configId") String configId, @Param("configValue") String configValue, @Param("updateBy") String updateBy);

    /**
     * 重置配置为默认值
     */
    int resetConfigToDefault(@Param("configId") String configId, @Param("updateBy") String updateBy);

    /**
     * 批量更新配置值
     */
    int batchUpdateConfigValues(@Param("configUpdates") List<Map<String, Object>> configUpdates, @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计配置总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_SYSTEM_CONFIG WHERE IS_DELETED = 0")
    int countTotalConfigs();

    /**
     * 按配置分类统计数量
     */
    List<Map<String, Object>> countByConfigCategory();

    /**
     * 按数据类型统计数量
     */
    List<Map<String, Object>> countByDataType();

    /**
     * 统计可修改配置数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_SYSTEM_CONFIG WHERE IS_MODIFIABLE = 1 AND IS_DELETED = 0")
    int countModifiableConfigs();

    // ==================== 数据验证方法 ====================

    /**
     * 检查配置编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_SYSTEM_CONFIG WHERE CONFIG_CODE = #{configCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkConfigCodeExists(@Param("configCode") String configCode, @Param("excludeId") String excludeId);

    /**
     * 检查配置是否可以删除
     */
    boolean checkConfigCanDelete(@Param("configId") String configId);

    /**
     * 检查配置是否可以修改
     */
    boolean checkConfigCanModify(@Param("configId") String configId);

    /**
     * 验证配置值
     */
    boolean validateConfigValue(@Param("configValue") String configValue, @Param("dataType") String dataType);
}
