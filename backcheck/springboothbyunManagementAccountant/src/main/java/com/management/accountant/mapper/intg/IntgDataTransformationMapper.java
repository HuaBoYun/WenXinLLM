package com.management.accountant.mapper.intg;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.intg.IntgDataTransformation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据转换 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface IntgDataTransformationMapper extends BaseMapper<IntgDataTransformation> {

    // 基础查询方法

    /**
     * 根据转换编码获取转换信息
     */
    IntgDataTransformation getByTransformationCode(@Param("transformationCode") String transformationCode, @Param("tenantId") String tenantId);

    /**
     * 根据转换类型获取转换列表
     */
    List<IntgDataTransformation> getByTransformationType(@Param("transformationType") String transformationType, @Param("tenantId") String tenantId);

    /**
     * 根据转换方向获取转换列表
     */
    List<IntgDataTransformation> getByTransformationDirection(@Param("transformationDirection") String transformationDirection, @Param("tenantId") String tenantId);

    /**
     * 根据转换分类获取转换列表
     */
    List<IntgDataTransformation> getByTransformationCategory(@Param("transformationCategory") String transformationCategory, @Param("tenantId") String tenantId);

    /**
     * 根据状态获取转换列表
     */
    List<IntgDataTransformation> getByStatus(@Param("status") String status, @Param("tenantId") String tenantId);

    /**
     * 根据源数据格式获取转换列表
     */
    List<IntgDataTransformation> getBySourceFormat(@Param("sourceFormat") String sourceFormat, @Param("tenantId") String tenantId);

    /**
     * 根据目标数据格式获取转换列表
     */
    List<IntgDataTransformation> getByTargetFormat(@Param("targetFormat") String targetFormat, @Param("tenantId") String tenantId);

    /**
     * 根据业务域获取转换列表
     */
    List<IntgDataTransformation> getByBusinessDomain(@Param("businessDomain") String businessDomain, @Param("tenantId") String tenantId);

    /**
     * 获取激活的转换列表
     */
    List<IntgDataTransformation> getActiveTransformations(@Param("tenantId") String tenantId);

    /**
     * 获取启用监控的转换列表
     */
    List<IntgDataTransformation> getMonitoringEnabledTransformations(@Param("tenantId") String tenantId);

    // 分页查询方法

    /**
     * 分页查询数据转换信息
     */
    IPage<IntgDataTransformation> getTransformationPage(Page<IntgDataTransformation> page, @Param("params") Map<String, Object> params);

    /**
     * 根据条件分页查询转换
     */
    IPage<IntgDataTransformation> getTransformationPageByCondition(Page<IntgDataTransformation> page, @Param("condition") IntgDataTransformation condition);

    /**
     * 高级搜索分页查询
     */
    IPage<IntgDataTransformation> advancedSearchPage(Page<IntgDataTransformation> page, @Param("params") Map<String, Object> params);

    // 统计查询方法

    /**
     * 统计转换总数
     */
    Long countTransformations(@Param("tenantId") String tenantId);

    /**
     * 按转换类型统计数量
     */
    List<Map<String, Object>> countByTransformationType(@Param("tenantId") String tenantId);

    /**
     * 按转换方向统计数量
     */
    List<Map<String, Object>> countByTransformationDirection(@Param("tenantId") String tenantId);

    /**
     * 按转换分类统计数量
     */
    List<Map<String, Object>> countByTransformationCategory(@Param("tenantId") String tenantId);

    /**
     * 按状态统计数量
     */
    List<Map<String, Object>> countByStatus(@Param("tenantId") String tenantId);

    /**
     * 按数据格式统计数量
     */
    List<Map<String, Object>> countByDataFormat(@Param("tenantId") String tenantId);

    /**
     * 按业务域统计数量
     */
    List<Map<String, Object>> countByBusinessDomain(@Param("tenantId") String tenantId);

    /**
     * 按创建时间统计数量
     */
    List<Map<String, Object>> countByCreateTime(@Param("startTime") LocalDateTime startTime, 
                                                @Param("endTime") LocalDateTime endTime, 
                                                @Param("tenantId") String tenantId);

    // 转换管理操作方法

    /**
     * 批量更新转换状态
     */
    int batchUpdateStatus(@Param("transformationIds") List<String> transformationIds, 
                         @Param("status") String status, 
                         @Param("updatedBy") String updatedBy, 
                         @Param("tenantId") String tenantId);

    /**
     * 批量启用转换
     */
    int batchEnableTransformations(@Param("transformationIds") List<String> transformationIds, 
                                  @Param("updatedBy") String updatedBy, 
                                  @Param("tenantId") String tenantId);

    /**
     * 批量禁用转换
     */
    int batchDisableTransformations(@Param("transformationIds") List<String> transformationIds, 
                                   @Param("updatedBy") String updatedBy, 
                                   @Param("tenantId") String tenantId);

    /**
     * 批量更新监控状态
     */
    int batchUpdateMonitoringStatus(@Param("transformationIds") List<String> transformationIds, 
                                   @Param("monitoringEnabled") Boolean monitoringEnabled,
                                   @Param("updatedBy") String updatedBy, 
                                   @Param("tenantId") String tenantId);

    /**
     * 批量更新优先级
     */
    int batchUpdatePriority(@Param("transformationIds") List<String> transformationIds, 
                           @Param("transformationPriority") String transformationPriority,
                           @Param("updatedBy") String updatedBy, 
                           @Param("tenantId") String tenantId);

    // 转换执行方法

    /**
     * 更新最后执行时间
     */
    int updateLastExecutionTime(@Param("transformationId") String transformationId, 
                               @Param("lastExecutionTime") LocalDateTime lastExecutionTime,
                               @Param("lastExecutionStatus") String lastExecutionStatus,
                               @Param("lastExecutionResult") String lastExecutionResult);

    /**
     * 增加执行次数
     */
    int incrementExecutionCount(@Param("transformationId") String transformationId, 
                               @Param("isSuccess") Boolean isSuccess,
                               @Param("executionTime") Long executionTime);

    /**
     * 更新性能统计
     */
    int updatePerformanceStats(@Param("transformationId") String transformationId, 
                              @Param("performanceStats") String performanceStats);

    /**
     * 重置执行统计
     */
    int resetExecutionStats(@Param("transformationId") String transformationId);

    // 转换查找方法

    /**
     * 根据关键词搜索转换
     */
    List<IntgDataTransformation> searchByKeyword(@Param("keyword") String keyword, @Param("tenantId") String tenantId);

    /**
     * 根据数据源查找转换
     */
    List<IntgDataTransformation> findByDataSource(@Param("dataSource") String dataSource, @Param("tenantId") String tenantId);

    /**
     * 根据数据目标查找转换
     */
    List<IntgDataTransformation> findByDataTarget(@Param("dataTarget") String dataTarget, @Param("tenantId") String tenantId);

    /**
     * 查找相似的转换
     */
    List<IntgDataTransformation> findSimilarTransformations(@Param("transformationId") String transformationId, @Param("tenantId") String tenantId);

    /**
     * 查找依赖的转换
     */
    List<IntgDataTransformation> findDependentTransformations(@Param("transformationId") String transformationId, @Param("tenantId") String tenantId);

    /**
     * 查找被依赖的转换
     */
    List<IntgDataTransformation> findDependencyTransformations(@Param("transformationId") String transformationId, @Param("tenantId") String tenantId);

    // 转换分析方法

    /**
     * 获取转换执行统计
     */
    Map<String, Object> getExecutionStats(@Param("transformationId") String transformationId, 
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    /**
     * 获取转换性能统计
     */
    Map<String, Object> getPerformanceStats(@Param("transformationId") String transformationId, 
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 获取转换错误统计
     */
    Map<String, Object> getErrorStats(@Param("transformationId") String transformationId, 
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 获取转换成功率趋势
     */
    List<Map<String, Object>> getSuccessRateTrend(@Param("transformationId") String transformationId, 
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime,
                                                  @Param("granularity") String granularity);

    /**
     * 获取转换健康度评分
     */
    Map<String, Object> getHealthScore(@Param("transformationId") String transformationId);

    /**
     * 获取转换质量评估
     */
    Map<String, Object> getQualityAssessment(@Param("transformationId") String transformationId);

    // 转换验证方法

    /**
     * 验证转换规则
     */
    List<Map<String, Object>> validateTransformationRules(@Param("transformationId") String transformationId);

    /**
     * 验证数据结构
     */
    List<Map<String, Object>> validateDataSchema(@Param("transformationId") String transformationId);

    /**
     * 验证映射配置
     */
    List<Map<String, Object>> validateMappingConfig(@Param("transformationId") String transformationId);

    /**
     * 测试转换逻辑
     */
    Map<String, Object> testTransformationLogic(@Param("transformationId") String transformationId, @Param("testData") String testData);

    // 数据清理方法

    /**
     * 清理过期的转换数据
     */
    int cleanExpiredData(@Param("expiredDate") LocalDateTime expiredDate, @Param("tenantId") String tenantId);

    /**
     * 清理停用的转换
     */
    int cleanInactiveTransformations(@Param("inactiveDate") LocalDateTime inactiveDate, @Param("tenantId") String tenantId);

    /**
     * 清理无效的转换配置
     */
    int cleanInvalidConfigs(@Param("tenantId") String tenantId);

    // 系统维护方法

    /**
     * 重建转换索引
     */
    int rebuildTransformationIndex(@Param("tenantId") String tenantId);

    /**
     * 优化转换配置
     */
    int optimizeTransformationConfigs(@Param("tenantId") String tenantId);

    /**
     * 同步转换状态
     */
    int syncTransformationStatus(@Param("tenantId") String tenantId);

    /**
     * 验证转换配置
     */
    List<Map<String, Object>> validateTransformationConfigs(@Param("tenantId") String tenantId);

    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview(@Param("tenantId") String tenantId);
}
