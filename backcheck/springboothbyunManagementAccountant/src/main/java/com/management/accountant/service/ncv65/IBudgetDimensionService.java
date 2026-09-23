package com.management.accountant.service.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ncv65.BudgetDimension;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算维度服务接口
 * 
 * @description 预算维度业务逻辑接口，支持6-20个维度配置
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
public interface IBudgetDimensionService extends IService<BudgetDimension> {

    /**
     * 创建维度
     * @param dimension 维度信息
     * @return 创建结果
     */
    boolean createDimension(BudgetDimension dimension);

    /**
     * 更新维度
     * @param dimension 维度信息
     * @return 更新结果
     */
    boolean updateDimension(BudgetDimension dimension);

    /**
     * 根据ID删除维度
     * @param id 维度ID
     * @return 删除结果
     */
    boolean deleteDimension(String id);

    /**
     * 批量删除维度
     * @param ids ID列表
     * @return 删除结果
     */
    boolean batchDeleteDimensions(List<String> ids);

    /**
     * 根据ID查询维度详情
     * @param id 维度ID
     * @return 维度信息
     */
    BudgetDimension getDimensionById(String id);

    /**
     * 根据维度编码查询维度
     * @param dimensionCode 维度编码
     * @return 维度信息
     */
    BudgetDimension getDimensionByCode(String dimensionCode);

    /**
     * 分页查询维度列表
     * @param current 当前页
     * @param size 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetDimension> getDimensionPage(Long current, Long size, Map<String, Object> params);

    /**
     * 根据维度类型查询维度列表
     * @param dimensionType 维度类型
     * @return 维度列表
     */
    List<BudgetDimension> getDimensionsByType(String dimensionType);

    /**
     * 查询启用的维度列表
     * @return 启用的维度列表
     */
    List<BudgetDimension> getEnabledDimensions();

    /**
     * 查询必填维度列表
     * @return 必填维度列表
     */
    List<BudgetDimension> getRequiredDimensions();

    /**
     * 查询系统维度列表
     * @return 系统维度列表
     */
    List<BudgetDimension> getSystemDimensions();

    /**
     * 启用维度
     * @param id 维度ID
     * @return 操作结果
     */
    boolean enableDimension(String id);

    /**
     * 禁用维度
     * @param id 维度ID
     * @return 操作结果
     */
    boolean disableDimension(String id);

    /**
     * 批量更新状态
     * @param ids ID列表
     * @param status 状态
     * @return 操作结果
     */
    boolean batchUpdateStatus(List<String> ids, String status);

    /**
     * 批量启用/禁用
     * @param ids ID列表
     * @param isEnabled 是否启用
     * @return 操作结果
     */
    boolean batchUpdateEnabled(List<String> ids, Boolean isEnabled);

    /**
     * 复制维度
     * @param sourceId 源维度ID
     * @param targetName 目标维度名称
     * @param targetCode 目标维度编码
     * @return 复制结果
     */
    BudgetDimension copyDimension(String sourceId, String targetName, String targetCode);

    /**
     * 检查维度编码是否存在
     * @param dimensionCode 维度编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkDimensionCodeExists(String dimensionCode, String excludeId);

    /**
     * 获取维度树结构
     * @param parentId 父维度ID
     * @return 树结构数据
     */
    List<Map<String, Object>> getDimensionTree(String parentId);

    /**
     * 获取完整维度树结构
     * @return 完整树结构数据
     */
    List<Map<String, Object>> getFullDimensionTree();

    /**
     * 验证维度配置
     * @param dimension 维度信息
     * @return 验证结果
     */
    Map<String, Object> validateDimensionConfig(BudgetDimension dimension);

    /**
     * 移动维度（调整父子关系）
     * @param dimensionId 维度ID
     * @param newParentId 新父维度ID
     * @return 移动结果
     */
    boolean moveDimension(String dimensionId, String newParentId);

    /**
     * 调整维度排序
     * @param dimensionId 维度ID
     * @param newSortOrder 新排序号
     * @return 调整结果
     */
    boolean adjustDimensionSort(String dimensionId, Integer newSortOrder);

    /**
     * 统计各维度类型的数量
     * @return 统计结果
     */
    List<Map<String, Object>> countByDimensionType();

    /**
     * 统计各状态的数量
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus();

    /**
     * 获取维度使用情况统计
     * @param dimensionId 维度ID
     * @return 使用情况统计
     */
    Map<String, Object> getDimensionUsageStatistics(String dimensionId);

    /**
     * 查询维度层级统计
     * @return 层级统计
     */
    List<Map<String, Object>> getDimensionLevelStatistics();

    /**
     * 根据层级查询维度
     * @param dimensionLevel 维度层级
     * @return 维度列表
     */
    List<BudgetDimension> getDimensionsByLevel(Integer dimensionLevel);

    /**
     * 根据数据来源查询维度
     * @param dataSource 数据来源
     * @return 维度列表
     */
    List<BudgetDimension> getDimensionsByDataSource(String dataSource);

    /**
     * 根据创建时间范围查询
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 维度列表
     */
    List<BudgetDimension> getDimensionsByCreateTimeRange(String startTime, String endTime);

    /**
     * 获取维度趋势数据
     * @param months 月份数
     * @return 趋势数据
     */
    List<Map<String, Object>> getDimensionTrendData(Integer months);

    /**
     * 导出维度数据
     * @param params 导出参数
     * @return 导出数据
     */
    List<Map<String, Object>> exportDimensionData(Map<String, Object> params);

    /**
     * 导入维度数据
     * @param dataList 导入数据
     * @return 导入结果
     */
    Map<String, Object> importDimensionData(List<Map<String, Object>> dataList);

    /**
     * 获取维度配置模板
     * @param dimensionType 维度类型
     * @return 配置模板
     */
    Map<String, Object> getDimensionConfigTemplate(String dimensionType);

    /**
     * 应用维度配置模板
     * @param dimensionId 维度ID
     * @param templateConfig 模板配置
     * @return 应用结果
     */
    boolean applyDimensionConfigTemplate(String dimensionId, Map<String, Object> templateConfig);

    /**
     * 同步维度数据
     * @param dimensionId 维度ID
     * @param syncConfig 同步配置
     * @return 同步结果
     */
    Map<String, Object> syncDimensionData(String dimensionId, Map<String, Object> syncConfig);

    /**
     * 验证维度值
     * @param dimensionId 维度ID
     * @param dimensionValue 维度值
     * @return 验证结果
     */
    Map<String, Object> validateDimensionValue(String dimensionId, String dimensionValue);

    /**
     * 获取维度值列表
     * @param dimensionId 维度ID
     * @param params 查询参数
     * @return 维度值列表
     */
    List<Map<String, Object>> getDimensionValues(String dimensionId, Map<String, Object> params);

    /**
     * 刷新维度缓存
     * @param dimensionId 维度ID
     * @return 刷新结果
     */
    boolean refreshDimensionCache(String dimensionId);
}
