package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetDataMapping;

import java.util.List;

/**
 * 预算数据映射配置Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetDataMappingService extends IService<BudgetDataMapping> {

    /**
     * 根据映射类型查询映射列表
     * 
     * @param mappingType 映射类型
     * @return 映射列表
     */
    List<BudgetDataMapping> listByMappingType(String mappingType);

    /**
     * 根据源系统类型查询映射列表
     * 
     * @param sourceSystemType 源系统类型
     * @return 映射列表
     */
    List<BudgetDataMapping> listBySourceSystemType(String sourceSystemType);

    /**
     * 根据目标系统类型查询映射列表
     * 
     * @param targetSystemType 目标系统类型
     * @return 映射列表
     */
    List<BudgetDataMapping> listByTargetSystemType(String targetSystemType);

    /**
     * 根据源系统ID和目标系统ID查询映射列表
     * 
     * @param sourceSystemId 源系统ID
     * @param targetSystemId 目标系统ID
     * @return 映射列表
     */
    List<BudgetDataMapping> listBySystemIds(String sourceSystemId, String targetSystemId);

    /**
     * 查询启用的映射列表
     * 
     * @return 映射列表
     */
    List<BudgetDataMapping> listEnabled();

    /**
     * 执行数据映射转换
     * 
     * @param mappingId 映射ID
     * @param sourceData 源数据
     * @return 转换后的数据
     */
    Object transformData(String mappingId, Object sourceData);

    /**
     * 批量执行数据映射转换
     * 
     * @param mappingIds 映射ID列表
     * @param sourceData 源数据
     * @return 转换后的数据
     */
    Object batchTransformData(List<String> mappingIds, Object sourceData);

    /**
     * 验证映射配置
     * 
     * @param mappingId 映射ID
     * @return 验证结果
     */
    boolean validateMapping(String mappingId);

    /**
     * 更新使用统计
     * 
     * @param mappingId 映射ID
     * @param success 是否成功
     * @return 是否更新成功
     */
    boolean updateUsageStatistics(String mappingId, boolean success);

    /**
     * 批量启用/禁用映射
     * 
     * @param mappingIds 映射ID列表
     * @param enabled 是否启用
     * @return 是否操作成功
     */
    boolean batchUpdateEnabled(List<String> mappingIds, boolean enabled);
}

