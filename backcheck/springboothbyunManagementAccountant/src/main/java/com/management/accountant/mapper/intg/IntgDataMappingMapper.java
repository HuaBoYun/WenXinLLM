package com.management.accountant.mapper.intg;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.intg.IntgDataMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据映射配置 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface IntgDataMappingMapper extends BaseMapper<IntgDataMapping> {

    // 基础查询方法

    /**
     * 根据映射编码查询
     */
    IntgDataMapping getByMappingCode(@Param("mappingCode") String mappingCode, @Param("tenantId") String tenantId);

    /**
     * 根据系统配置ID查询映射列表
     */
    List<IntgDataMapping> getByConfigId(@Param("configId") String configId, @Param("tenantId") String tenantId);

    /**
     * 根据映射类型查询映射列表
     */
    List<IntgDataMapping> getByMappingType(@Param("mappingType") String mappingType, @Param("tenantId") String tenantId);

    /**
     * 根据映射方向查询映射列表
     */
    List<IntgDataMapping> getByMappingDirection(@Param("mappingDirection") String mappingDirection, @Param("tenantId") String tenantId);

    /**
     * 根据源表名查询映射列表
     */
    List<IntgDataMapping> getBySourceTable(@Param("sourceTableName") String sourceTableName, @Param("tenantId") String tenantId);

    /**
     * 根据目标表名查询映射列表
     */
    List<IntgDataMapping> getByTargetTable(@Param("targetTableName") String targetTableName, @Param("tenantId") String tenantId);

    /**
     * 查询启用的映射列表
     */
    List<IntgDataMapping> getEnabledMappings(@Param("configId") String configId, @Param("tenantId") String tenantId);

    // 分页查询方法

    /**
     * 分页查询映射列表
     */
    IPage<IntgDataMapping> getMappingPage(Page<IntgDataMapping> page, @Param("params") Map<String, Object> params);

    /**
     * 分页查询系统映射
     */
    IPage<IntgDataMapping> getSystemMappingPage(Page<IntgDataMapping> page,
                                               @Param("configId") String configId,
                                               @Param("mappingType") String mappingType,
                                               @Param("mappingStatus") String mappingStatus,
                                               @Param("tenantId") String tenantId);

    /**
     * 分页查询表映射
     */
    IPage<IntgDataMapping> getTableMappingPage(Page<IntgDataMapping> page,
                                              @Param("sourceTableName") String sourceTableName,
                                              @Param("targetTableName") String targetTableName,
                                              @Param("tenantId") String tenantId);

    // 统计查询方法

    /**
     * 统计映射总数
     */
    Long countMappings(@Param("tenantId") String tenantId);

    /**
     * 按映射类型统计数量
     */
    List<Map<String, Object>> countByMappingType(@Param("tenantId") String tenantId);

    /**
     * 按映射方向统计数量
     */
    List<Map<String, Object>> countByMappingDirection(@Param("tenantId") String tenantId);

    /**
     * 按映射状态统计数量
     */
    List<Map<String, Object>> countByMappingStatus(@Param("tenantId") String tenantId);

    /**
     * 按系统配置统计映射数量
     */
    List<Map<String, Object>> countBySystemConfig(@Param("tenantId") String tenantId);

    /**
     * 按转换规则类型统计数量
     */
    List<Map<String, Object>> countByTransformRuleType(@Param("tenantId") String tenantId);

    // 映射管理方法

    /**
     * 批量更新映射状态
     */
    int batchUpdateMappingStatus(@Param("mappingIds") List<String> mappingIds,
                                @Param("mappingStatus") String mappingStatus,
                                @Param("updatedBy") String updatedBy,
                                @Param("tenantId") String tenantId);

    /**
     * 批量启用映射
     */
    int batchEnableMappings(@Param("mappingIds") List<String> mappingIds,
                           @Param("updatedBy") String updatedBy,
                           @Param("tenantId") String tenantId);

    /**
     * 批量禁用映射
     */
    int batchDisableMappings(@Param("mappingIds") List<String> mappingIds,
                            @Param("updatedBy") String updatedBy,
                            @Param("tenantId") String tenantId);

    /**
     * 更新映射执行统计
     */
    int updateExecutionStats(@Param("mappingId") String mappingId,
                            @Param("executionTime") Long executionTime,
                            @Param("isSuccess") Boolean isSuccess,
                            @Param("tenantId") String tenantId);

    /**
     * 更新映射性能统计
     */
    int updatePerformanceStats(@Param("mappingId") String mappingId,
                              @Param("performanceStats") String performanceStats,
                              @Param("tenantId") String tenantId);

    // 映射验证方法

    /**
     * 检查映射编码是否存在
     */
    boolean existsByMappingCode(@Param("mappingCode") String mappingCode, @Param("tenantId") String tenantId);

    /**
     * 检查映射编码是否存在（排除指定ID）
     */
    boolean existsByMappingCodeExcludeId(@Param("mappingCode") String mappingCode,
                                        @Param("excludeId") String excludeId,
                                        @Param("tenantId") String tenantId);

    /**
     * 检查字段映射是否重复
     */
    boolean existsByFieldMapping(@Param("configId") String configId,
                                @Param("sourceTableName") String sourceTableName,
                                @Param("sourceFieldName") String sourceFieldName,
                                @Param("targetTableName") String targetTableName,
                                @Param("targetFieldName") String targetFieldName,
                                @Param("tenantId") String tenantId);

    // 映射查找方法

    /**
     * 查找相似映射
     */
    List<IntgDataMapping> findSimilarMappings(@Param("sourceTableName") String sourceTableName,
                                             @Param("targetTableName") String targetTableName,
                                             @Param("mappingType") String mappingType,
                                             @Param("tenantId") String tenantId);

    /**
     * 查找过期映射
     */
    List<IntgDataMapping> findExpiredMappings(@Param("currentTime") LocalDateTime currentTime,
                                             @Param("tenantId") String tenantId);

    /**
     * 查找需要执行的映射
     */
    List<IntgDataMapping> findMappingsForExecution(@Param("configId") String configId,
                                                   @Param("tenantId") String tenantId);

    /**
     * 查找依赖映射
     */
    List<IntgDataMapping> findDependentMappings(@Param("sourceTableName") String sourceTableName,
                                               @Param("tenantId") String tenantId);

    // 映射分析方法

    /**
     * 获取映射使用统计
     */
    List<Map<String, Object>> getMappingUsageStats(@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime,
                                                   @Param("tenantId") String tenantId);

    /**
     * 获取映射性能统计
     */
    List<Map<String, Object>> getMappingPerformanceStats(@Param("mappingIds") List<String> mappingIds,
                                                         @Param("tenantId") String tenantId);

    /**
     * 获取映射质量统计
     */
    List<Map<String, Object>> getMappingQualityStats(@Param("tenantId") String tenantId);

    /**
     * 获取映射趋势分析
     */
    List<Map<String, Object>> getMappingTrendAnalysis(@Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime,
                                                      @Param("granularity") String granularity,
                                                      @Param("tenantId") String tenantId);

    // 映射测试方法

    /**
     * 测试映射配置
     */
    Map<String, Object> testMappingConfig(@Param("mappingId") String mappingId,
                                         @Param("testData") String testData,
                                         @Param("tenantId") String tenantId);

    /**
     * 验证映射规则
     */
    Map<String, Object> validateMappingRules(@Param("mappingId") String mappingId,
                                            @Param("tenantId") String tenantId);

    /**
     * 预览映射结果
     */
    List<Map<String, Object>> previewMappingResult(@Param("mappingId") String mappingId,
                                                   @Param("sampleSize") Integer sampleSize,
                                                   @Param("tenantId") String tenantId);

    // 数据清理方法

    /**
     * 清理过期映射
     */
    int cleanupExpiredMappings(@Param("expiryTime") LocalDateTime expiryTime,
                              @Param("tenantId") String tenantId);

    /**
     * 清理无效映射
     */
    int cleanupInvalidMappings(@Param("tenantId") String tenantId);

    /**
     * 归档历史映射
     */
    int archiveHistoryMappings(@Param("archiveTime") LocalDateTime archiveTime,
                              @Param("tenantId") String tenantId);

    // 系统维护方法

    /**
     * 获取映射概览信息
     */
    Map<String, Object> getMappingOverview(@Param("tenantId") String tenantId);

    /**
     * 生成映射报告
     */
    List<Map<String, Object>> generateMappingReport(@Param("reportType") String reportType,
                                                    @Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime,
                                                    @Param("tenantId") String tenantId);

    /**
     * 检查映射健康状态
     */
    Map<String, Object> checkMappingHealth(@Param("tenantId") String tenantId);

    /**
     * 评估映射质量
     */
    List<Map<String, Object>> assessMappingQuality(@Param("tenantId") String tenantId);

    /**
     * 优化映射配置
     */
    Map<String, Object> optimizeMappingConfig(@Param("optimizationType") String optimizationType,
                                             @Param("parameters") Map<String, Object> parameters,
                                             @Param("tenantId") String tenantId);
}
