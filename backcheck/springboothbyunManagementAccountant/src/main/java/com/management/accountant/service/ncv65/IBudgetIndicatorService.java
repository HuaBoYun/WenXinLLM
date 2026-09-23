package com.management.accountant.service.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ncv65.BudgetIndicator;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算指标服务接口
 * 
 * @description 预算指标业务逻辑接口，支持指标的完整生命周期管理
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
public interface IBudgetIndicatorService extends IService<BudgetIndicator> {

    /**
     * 创建指标
     * 
     * @param indicator 指标信息
     * @return 创建结果
     */
    boolean createIndicator(BudgetIndicator indicator);

    /**
     * 更新指标
     * 
     * @param indicator 指标信息
     * @return 更新结果
     */
    boolean updateIndicator(BudgetIndicator indicator);

    /**
     * 删除指标
     * 
     * @param id 指标ID
     * @return 删除结果
     */
    boolean deleteIndicator(String id);

    /**
     * 批量删除指标
     * 
     * @param ids 指标ID列表
     * @return 删除结果
     */
    boolean batchDeleteIndicators(List<String> ids);

    /**
     * 根据ID查询指标
     * 
     * @param id 指标ID
     * @return 指标信息
     */
    BudgetIndicator getIndicatorById(String id);

    /**
     * 根据指标编码查询指标
     * 
     * @param indicatorCode 指标编码
     * @return 指标信息
     */
    BudgetIndicator getIndicatorByCode(String indicatorCode);

    /**
     * 分页查询指标列表
     * 
     * @param current 当前页
     * @param size 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetIndicator> getIndicatorPage(Long current, Long size, Map<String, Object> params);

    /**
     * 根据指标类型查询指标列表
     * 
     * @param indicatorType 指标类型
     * @return 指标列表
     */
    List<BudgetIndicator> getIndicatorsByType(String indicatorType);

    /**
     * 查询启用的指标列表
     * 
     * @return 指标列表
     */
    List<BudgetIndicator> getEnabledIndicators();

    /**
     * 查询必填指标列表
     * 
     * @return 指标列表
     */
    List<BudgetIndicator> getRequiredIndicators();

    /**
     * 查询系统指标列表
     * 
     * @return 指标列表
     */
    List<BudgetIndicator> getSystemIndicators();

    /**
     * 查询可计算指标列表
     * 
     * @return 指标列表
     */
    List<BudgetIndicator> getCalculatedIndicators();

    /**
     * 启用指标
     * 
     * @param id 指标ID
     * @return 操作结果
     */
    boolean enableIndicator(String id);

    /**
     * 禁用指标
     * 
     * @param id 指标ID
     * @return 操作结果
     */
    boolean disableIndicator(String id);

    /**
     * 批量更新状态
     * 
     * @param ids 指标ID列表
     * @param status 状态
     * @return 操作结果
     */
    boolean batchUpdateStatus(List<String> ids, String status);

    /**
     * 批量更新启用状态
     * 
     * @param ids 指标ID列表
     * @param isEnabled 是否启用
     * @return 操作结果
     */
    boolean batchUpdateEnabled(List<String> ids, Boolean isEnabled);

    /**
     * 复制指标
     * 
     * @param id 源指标ID
     * @param targetName 目标指标名称
     * @param targetCode 目标指标编码
     * @return 新指标信息
     */
    BudgetIndicator copyIndicator(String id, String targetName, String targetCode);

    /**
     * 检查指标编码是否存在
     * 
     * @param indicatorCode 指标编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkIndicatorCodeExists(String indicatorCode, String excludeId);

    /**
     * 获取指标树结构
     * 
     * @param parentId 父指标ID
     * @return 树结构
     */
    List<Map<String, Object>> getIndicatorTree(String parentId);

    /**
     * 获取完整指标树结构
     * 
     * @return 树结构
     */
    List<Map<String, Object>> getFullIndicatorTree();

    /**
     * 根据父指标ID查询子指标列表
     * 
     * @param parentId 父指标ID
     * @return 子指标列表
     */
    List<BudgetIndicator> getChildIndicators(String parentId);

    /**
     * 根据指标级别查询指标列表
     * 
     * @param level 指标级别
     * @return 指标列表
     */
    List<BudgetIndicator> getIndicatorsByLevel(Integer level);

    /**
     * 移动指标
     * 
     * @param id 指标ID
     * @param newParentId 新父指标ID
     * @return 操作结果
     */
    boolean moveIndicator(String id, String newParentId);

    /**
     * 调整指标排序
     * 
     * @param id 指标ID
     * @param sortOrder 排序号
     * @return 操作结果
     */
    boolean adjustIndicatorSort(String id, Integer sortOrder);

    /**
     * 同步指标数据
     * 
     * @param id 指标ID
     * @return 操作结果
     */
    boolean syncIndicatorData(String id);

    /**
     * 验证指标公式
     * 
     * @param formula 公式
     * @return 验证结果
     */
    boolean validateFormula(String formula);

    /**
     * 计算指标值
     * 
     * @param indicatorId 指标ID
     * @param params 计算参数
     * @return 计算结果
     */
    Map<String, Object> calculateIndicatorValue(String indicatorId, Map<String, Object> params);

    /**
     * 获取指标依赖关系
     * 
     * @param indicatorId 指标ID
     * @return 依赖关系
     */
    List<Map<String, Object>> getIndicatorDependencies(String indicatorId);

    /**
     * 获取指标影响范围
     * 
     * @param indicatorId 指标ID
     * @return 影响范围
     */
    List<Map<String, Object>> getIndicatorImpacts(String indicatorId);

    /**
     * 统计各指标类型的数量
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> countByIndicatorType();

    /**
     * 统计各数据类型的数量
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> countByDataType();

    /**
     * 统计各状态的数量
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus();

    /**
     * 导出指标配置
     * 
     * @param ids 指标ID列表
     * @return 导出文件路径
     */
    String exportIndicatorConfig(List<String> ids);

    /**
     * 导入指标配置
     * 
     * @param filePath 文件路径
     * @return 导入结果
     */
    Map<String, Object> importIndicatorConfig(String filePath);

    /**
     * 获取指标层级路径
     * 
     * @param indicatorId 指标ID
     * @return 层级路径
     */
    String getIndicatorPath(String indicatorId);

    /**
     * 验证指标配置
     * 
     * @param indicator 指标信息
     * @return 验证结果
     */
    Map<String, Object> validateIndicatorConfig(BudgetIndicator indicator);

    /**
     * 刷新指标缓存
     * 
     * @param indicatorId 指标ID，为空则刷新全部
     * @return 操作结果
     */
    boolean refreshIndicatorCache(String indicatorId);

    /**
     * 获取指标使用统计
     * 
     * @param indicatorId 指标ID
     * @return 使用统计
     */
    Map<String, Object> getIndicatorUsageStats(String indicatorId);
}
