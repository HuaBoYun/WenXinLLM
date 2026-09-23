package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetDataMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算数据映射配置Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetDataMappingMapper extends BaseMapper<BudgetDataMapping> {

    /**
     * 根据映射类型查询映射列表
     * 
     * @param mappingType 映射类型
     * @return 映射列表
     */
    List<BudgetDataMapping> listByMappingType(@Param("mappingType") String mappingType);

    /**
     * 根据源系统类型查询映射列表
     * 
     * @param sourceSystemType 源系统类型
     * @return 映射列表
     */
    List<BudgetDataMapping> listBySourceSystemType(@Param("sourceSystemType") String sourceSystemType);

    /**
     * 根据目标系统类型查询映射列表
     * 
     * @param targetSystemType 目标系统类型
     * @return 映射列表
     */
    List<BudgetDataMapping> listByTargetSystemType(@Param("targetSystemType") String targetSystemType);

    /**
     * 根据源系统ID和目标系统ID查询映射列表
     * 
     * @param sourceSystemId 源系统ID
     * @param targetSystemId 目标系统ID
     * @return 映射列表
     */
    List<BudgetDataMapping> listBySystemIds(@Param("sourceSystemId") String sourceSystemId,
                                           @Param("targetSystemId") String targetSystemId);

    /**
     * 查询启用的映射列表
     * 
     * @return 映射列表
     */
    List<BudgetDataMapping> listEnabled();

    /**
     * 更新使用统计
     * 
     * @param mappingId 映射ID
     * @param success 是否成功
     * @return 更新记录数
     */
    int updateUsageStatistics(@Param("mappingId") String mappingId, 
                            @Param("success") boolean success);
}

