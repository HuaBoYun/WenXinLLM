package com.management.accountant.service.intg;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.intg.IntgDataMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据映射配置服务接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
public interface IntgDataMappingService extends IService<IntgDataMapping> {

    // 基础CRUD操作

    /**
     * 创建数据映射
     */
    IntgDataMapping createDataMapping(IntgDataMapping dataMapping);

    /**
     * 更新数据映射
     */
    IntgDataMapping updateDataMapping(IntgDataMapping dataMapping);

    /**
     * 删除数据映射
     */
    boolean deleteDataMapping(String mappingId);

    /**
     * 根据ID获取数据映射
     */
    IntgDataMapping getDataMappingById(String mappingId);

    /**
     * 根据编码获取数据映射
     */
    IntgDataMapping getDataMappingByCode(String mappingCode);

    // 查询操作

    /**
     * 分页查询数据映射
     */
    IPage<IntgDataMapping> getDataMappingPage(Integer current, Integer size, Map<String, Object> params);

    /**
     * 根据系统配置ID查询映射列表
     */
    List<IntgDataMapping> getDataMappingsByConfigId(String configId);

    /**
     * 根据映射类型查询映射列表
     */
    List<IntgDataMapping> getDataMappingsByType(String mappingType);

    /**
     * 根据映射方向查询映射列表
     */
    List<IntgDataMapping> getDataMappingsByDirection(String mappingDirection);

    /**
     * 根据源表名查询映射列表
     */
    List<IntgDataMapping> getDataMappingsBySourceTable(String sourceTableName);

    /**
     * 根据目标表名查询映射列表
     */
    List<IntgDataMapping> getDataMappingsByTargetTable(String targetTableName);

    /**
     * 查询启用的映射列表
     */
    List<IntgDataMapping> getEnabledDataMappings(String configId);

    // 映射管理操作

    /**
     * 启用数据映射
     */
    boolean enableDataMapping(String mappingId);

    /**
     * 禁用数据映射
     */
    boolean disableDataMapping(String mappingId);

    /**
     * 批量更新映射状态
     */
    boolean batchUpdateMappingStatus(List<String> mappingIds, String mappingStatus);

    /**
     * 批量启用映射
     */
    boolean batchEnableMappings(List<String> mappingIds);

    /**
     * 批量禁用映射
     */
    boolean batchDisableMappings(List<String> mappingIds);

    /**
     * 复制数据映射
     */
    IntgDataMapping copyDataMapping(String mappingId, String newMappingName);

    /**
     * 批量复制映射
     */
    List<IntgDataMapping> batchCopyMappings(List<String> mappingIds, String namePrefix);

    // 映射验证操作

    /**
     * 验证映射配置
     */
    Map<String, Object> validateDataMapping(IntgDataMapping dataMapping);

    /**
     * 检查映射编码是否存在
     */
    boolean existsByMappingCode(String mappingCode);

    /**
     * 检查映射编码是否存在（排除指定ID）
     */
    boolean existsByMappingCodeExcludeId(String mappingCode, String excludeId);

    /**
     * 检查字段映射是否重复
     */
    boolean existsByFieldMapping(String configId, String sourceTableName, String sourceFieldName, 
                                String targetTableName, String targetFieldName);

    /**
     * 验证映射规则
     */
    Map<String, Object> validateMappingRules(String mappingId);

    /**
     * 验证转换脚本
     */
    Map<String, Object> validateTransformScript(String mappingId);

    // 映射测试操作

    /**
     * 测试映射配置
     */
    Map<String, Object> testMappingConfig(String mappingId, Map<String, Object> testData);

    /**
     * 批量测试映射
     */
    List<Map<String, Object>> batchTestMappings(List<String> mappingIds);

    /**
     * 预览映射结果
     */
    List<Map<String, Object>> previewMappingResult(String mappingId, Integer sampleSize);

    /**
     * 执行映射转换
     */
    Map<String, Object> executeMappingTransform(String mappingId, List<Map<String, Object>> sourceData);

    // 映射执行操作

    /**
     * 执行数据映射
     */
    Map<String, Object> executeDataMapping(String mappingId);

    /**
     * 批量执行映射
     */
    List<Map<String, Object>> batchExecuteMappings(List<String> mappingIds);

    /**
     * 调度执行映射
     */
    boolean scheduleDataMapping(String mappingId, String cronExpression);

    /**
     * 停止映射执行
     */
    boolean stopMappingExecution(String mappingId);

    /**
     * 更新映射执行统计
     */
    boolean updateExecutionStats(String mappingId, Long executionTime, Boolean isSuccess);

    // 映射查找操作

    /**
     * 查找相似映射
     */
    List<IntgDataMapping> findSimilarMappings(String sourceTableName, String targetTableName, String mappingType);

    /**
     * 查找过期映射
     */
    List<IntgDataMapping> findExpiredMappings();

    /**
     * 查找需要执行的映射
     */
    List<IntgDataMapping> findMappingsForExecution(String configId);

    /**
     * 查找依赖映射
     */
    List<IntgDataMapping> findDependentMappings(String sourceTableName);

    /**
     * 查找冲突映射
     */
    List<IntgDataMapping> findConflictingMappings(String mappingId);

    // 统计分析操作

    /**
     * 统计映射总数
     */
    Long countDataMappings();

    /**
     * 按映射类型统计数量
     */
    List<Map<String, Object>> countByMappingType();

    /**
     * 按映射方向统计数量
     */
    List<Map<String, Object>> countByMappingDirection();

    /**
     * 按映射状态统计数量
     */
    List<Map<String, Object>> countByMappingStatus();

    /**
     * 按系统配置统计映射数量
     */
    List<Map<String, Object>> countBySystemConfig();

    /**
     * 获取映射使用统计
     */
    List<Map<String, Object>> getMappingUsageStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取映射性能统计
     */
    List<Map<String, Object>> getMappingPerformanceStats(List<String> mappingIds);

    /**
     * 获取映射质量统计
     */
    List<Map<String, Object>> getMappingQualityStats();

    /**
     * 获取映射趋势分析
     */
    List<Map<String, Object>> getMappingTrendAnalysis(LocalDateTime startTime, LocalDateTime endTime, String granularity);

    // 映射优化操作

    /**
     * 优化映射性能
     */
    Map<String, Object> optimizeMappingPerformance(String mappingId);

    /**
     * 批量优化映射
     */
    List<Map<String, Object>> batchOptimizeMappings(List<String> mappingIds);

    /**
     * 分析映射瓶颈
     */
    Map<String, Object> analyzeMappingBottlenecks(String mappingId);

    /**
     * 生成优化建议
     */
    List<Map<String, Object>> generateOptimizationSuggestions(String mappingId);

    /**
     * 自动优化映射配置
     */
    Map<String, Object> autoOptimizeMappingConfig(String mappingId);

    // 映射模板操作

    /**
     * 创建映射模板
     */
    IntgDataMapping createMappingTemplate(IntgDataMapping templateMapping);

    /**
     * 应用映射模板
     */
    IntgDataMapping applyMappingTemplate(String templateId, Map<String, Object> parameters);

    /**
     * 获取映射模板列表
     */
    List<IntgDataMapping> getMappingTemplates();

    /**
     * 删除映射模板
     */
    boolean deleteMappingTemplate(String templateId);

    // 映射版本管理操作

    /**
     * 创建映射版本
     */
    IntgDataMapping createMappingVersion(String mappingId, String versionName);

    /**
     * 回滚映射版本
     */
    boolean rollbackMappingVersion(String mappingId, String versionId);

    /**
     * 获取映射版本历史
     */
    List<IntgDataMapping> getMappingVersionHistory(String mappingId);

    /**
     * 比较映射版本
     */
    Map<String, Object> compareMappingVersions(String mappingId, String version1, String version2);

    // 数据清理操作

    /**
     * 清理过期映射
     */
    int cleanupExpiredMappings();

    /**
     * 清理无效映射
     */
    int cleanupInvalidMappings();

    /**
     * 归档历史映射
     */
    int archiveHistoryMappings(LocalDateTime archiveTime);

    /**
     * 压缩映射数据
     */
    int compressMappingData();

    // 系统维护操作

    /**
     * 获取映射概览信息
     */
    Map<String, Object> getMappingOverview();

    /**
     * 生成映射报告
     */
    List<Map<String, Object>> generateMappingReport(String reportType, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 检查映射健康状态
     */
    Map<String, Object> checkMappingHealth();

    /**
     * 评估映射质量
     */
    List<Map<String, Object>> assessMappingQuality();

    /**
     * 执行映射维护任务
     */
    Map<String, Object> executeMappingMaintenanceTasks(String taskType, Map<String, Object> parameters);

    // 导入导出操作

    /**
     * 导出映射配置
     */
    Map<String, Object> exportMappingConfigs(List<String> mappingIds, String exportFormat);

    /**
     * 导入映射配置
     */
    Map<String, Object> importMappingConfigs(String importData, String importFormat);

    /**
     * 生成映射文档
     */
    Map<String, Object> generateMappingDocumentation(List<String> mappingIds, String documentFormat);
}
