package com.management.accountant.mapper.mobile;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.mobile.MobileFunctionConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 移动功能配置 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface MobileFunctionConfigMapper extends BaseMapper<MobileFunctionConfig> {

    // 基础查询方法

    /**
     * 根据功能编码获取功能配置
     */
    MobileFunctionConfig getByFunctionCode(@Param("functionCode") String functionCode, @Param("tenantId") String tenantId);

    /**
     * 根据应用配置ID获取功能列表
     */
    List<MobileFunctionConfig> getByAppConfigId(@Param("appConfigId") String appConfigId, @Param("tenantId") String tenantId);

    /**
     * 根据功能类型获取功能列表
     */
    List<MobileFunctionConfig> getByFunctionType(@Param("functionType") String functionType, @Param("tenantId") String tenantId);

    /**
     * 根据功能分类获取功能列表
     */
    List<MobileFunctionConfig> getByFunctionCategory(@Param("functionCategory") String functionCategory, @Param("tenantId") String tenantId);

    /**
     * 根据功能模块获取功能列表
     */
    List<MobileFunctionConfig> getByFunctionModule(@Param("functionModule") String functionModule, @Param("tenantId") String tenantId);

    /**
     * 根据功能状态获取功能列表
     */
    List<MobileFunctionConfig> getByFunctionStatus(@Param("functionStatus") String functionStatus, @Param("tenantId") String tenantId);

    /**
     * 获取启用的功能列表
     */
    List<MobileFunctionConfig> getEnabledFunctions(@Param("tenantId") String tenantId);

    /**
     * 获取可见的功能列表
     */
    List<MobileFunctionConfig> getVisibleFunctions(@Param("tenantId") String tenantId);

    /**
     * 获取必需的功能列表
     */
    List<MobileFunctionConfig> getRequiredFunctions(@Param("tenantId") String tenantId);

    /**
     * 获取支持离线的功能列表
     */
    List<MobileFunctionConfig> getOfflineSupportFunctions(@Param("tenantId") String tenantId);

    /**
     * 根据父功能ID获取子功能列表
     */
    List<MobileFunctionConfig> getByParentFunctionId(@Param("parentFunctionId") String parentFunctionId, @Param("tenantId") String tenantId);

    /**
     * 获取根功能列表（无父功能）
     */
    List<MobileFunctionConfig> getRootFunctions(@Param("tenantId") String tenantId);

    /**
     * 获取功能树结构
     */
    List<MobileFunctionConfig> getFunctionTree(@Param("tenantId") String tenantId);

    // 分页查询方法

    /**
     * 分页查询功能配置
     */
    IPage<MobileFunctionConfig> getFunctionConfigPage(Page<MobileFunctionConfig> page, @Param("params") Map<String, Object> params);

    /**
     * 根据条件分页查询功能
     */
    IPage<MobileFunctionConfig> getFunctionConfigPageByCondition(Page<MobileFunctionConfig> page, @Param("condition") MobileFunctionConfig condition);

    /**
     * 高级搜索分页查询
     */
    IPage<MobileFunctionConfig> advancedSearchPage(Page<MobileFunctionConfig> page, @Param("params") Map<String, Object> params);

    // 统计查询方法

    /**
     * 统计功能总数
     */
    Long countFunctions(@Param("tenantId") String tenantId);

    /**
     * 按功能类型统计数量
     */
    List<Map<String, Object>> countByFunctionType(@Param("tenantId") String tenantId);

    /**
     * 按功能分类统计数量
     */
    List<Map<String, Object>> countByFunctionCategory(@Param("tenantId") String tenantId);

    /**
     * 按功能模块统计数量
     */
    List<Map<String, Object>> countByFunctionModule(@Param("tenantId") String tenantId);

    /**
     * 按功能状态统计数量
     */
    List<Map<String, Object>> countByFunctionStatus(@Param("tenantId") String tenantId);

    /**
     * 按功能优先级统计数量
     */
    List<Map<String, Object>> countByFunctionPriority(@Param("tenantId") String tenantId);

    /**
     * 按创建时间统计数量
     */
    List<Map<String, Object>> countByCreateTime(@Param("startTime") LocalDateTime startTime, 
                                                @Param("endTime") LocalDateTime endTime, 
                                                @Param("tenantId") String tenantId);

    // 功能管理操作方法

    /**
     * 批量更新功能状态
     */
    int batchUpdateFunctionStatus(@Param("functionConfigIds") List<String> functionConfigIds, 
                                 @Param("functionStatus") String functionStatus, 
                                 @Param("updatedBy") String updatedBy, 
                                 @Param("tenantId") String tenantId);

    /**
     * 批量启用功能
     */
    int batchEnableFunctions(@Param("functionConfigIds") List<String> functionConfigIds, 
                            @Param("updatedBy") String updatedBy, 
                            @Param("tenantId") String tenantId);

    /**
     * 批量禁用功能
     */
    int batchDisableFunctions(@Param("functionConfigIds") List<String> functionConfigIds, 
                             @Param("updatedBy") String updatedBy, 
                             @Param("tenantId") String tenantId);

    /**
     * 批量设置功能可见性
     */
    int batchSetFunctionVisibility(@Param("functionConfigIds") List<String> functionConfigIds, 
                                  @Param("isVisible") Boolean isVisible,
                                  @Param("updatedBy") String updatedBy, 
                                  @Param("tenantId") String tenantId);

    /**
     * 批量设置功能必需性
     */
    int batchSetFunctionRequired(@Param("functionConfigIds") List<String> functionConfigIds, 
                                @Param("isRequired") Boolean isRequired,
                                @Param("updatedBy") String updatedBy, 
                                @Param("tenantId") String tenantId);

    /**
     * 批量更新功能优先级
     */
    int batchUpdateFunctionPriority(@Param("functionConfigIds") List<String> functionConfigIds, 
                                   @Param("functionPriority") String functionPriority,
                                   @Param("updatedBy") String updatedBy, 
                                   @Param("tenantId") String tenantId);

    /**
     * 批量更新功能排序
     */
    int batchUpdateSortOrder(@Param("functionConfigIds") List<String> functionConfigIds, 
                            @Param("sortOrders") List<Integer> sortOrders,
                            @Param("updatedBy") String updatedBy, 
                            @Param("tenantId") String tenantId);

    // 功能版本管理方法

    /**
     * 获取功能的所有版本
     */
    List<MobileFunctionConfig> getFunctionVersions(@Param("functionCode") String functionCode, @Param("tenantId") String tenantId);

    /**
     * 获取功能的最新版本
     */
    MobileFunctionConfig getLatestFunctionVersion(@Param("functionCode") String functionCode, @Param("tenantId") String tenantId);

    /**
     * 检查功能版本是否存在
     */
    boolean checkFunctionVersionExists(@Param("functionCode") String functionCode, 
                                      @Param("functionVersion") String functionVersion, 
                                      @Param("tenantId") String tenantId);

    /**
     * 更新功能使用次数
     */
    int updateUsageCount(@Param("functionConfigId") String functionConfigId, @Param("increment") Long increment);

    /**
     * 更新功能使用时长
     */
    int updateUsageDuration(@Param("functionConfigId") String functionConfigId, @Param("increment") Long increment);

    /**
     * 更新功能错误次数
     */
    int updateErrorCount(@Param("functionConfigId") String functionConfigId, @Param("increment") Long increment);

    /**
     * 更新功能成功率
     */
    int updateSuccessRate(@Param("functionConfigId") String functionConfigId, @Param("successRate") Double successRate);

    /**
     * 更新功能性能指标
     */
    int updatePerformanceMetrics(@Param("functionConfigId") String functionConfigId, 
                                @Param("responseTime") Long responseTime,
                                @Param("memoryUsage") Double memoryUsage,
                                @Param("cpuUsage") Double cpuUsage,
                                @Param("networkUsage") Long networkUsage);

    /**
     * 更新最后使用时间
     */
    int updateLastUsedTime(@Param("functionConfigId") String functionConfigId, @Param("lastUsedTime") LocalDateTime lastUsedTime);

    // 功能查找方法

    /**
     * 根据关键词搜索功能
     */
    List<MobileFunctionConfig> searchByKeyword(@Param("keyword") String keyword, @Param("tenantId") String tenantId);

    /**
     * 根据标签搜索功能
     */
    List<MobileFunctionConfig> searchByTags(@Param("tags") List<String> tags, @Param("tenantId") String tenantId);

    /**
     * 查找相似的功能
     */
    List<MobileFunctionConfig> findSimilarFunctions(@Param("functionConfigId") String functionConfigId, @Param("tenantId") String tenantId);

    /**
     * 查找热门功能
     */
    List<MobileFunctionConfig> findPopularFunctions(@Param("limit") Integer limit, @Param("tenantId") String tenantId);

    /**
     * 查找推荐功能
     */
    List<MobileFunctionConfig> findRecommendedFunctions(@Param("userId") String userId, 
                                                        @Param("limit") Integer limit, 
                                                        @Param("tenantId") String tenantId);

    /**
     * 查找依赖的功能
     */
    List<MobileFunctionConfig> findDependentFunctions(@Param("functionConfigId") String functionConfigId, @Param("tenantId") String tenantId);

    /**
     * 查找被依赖的功能
     */
    List<MobileFunctionConfig> findDependencyFunctions(@Param("functionConfigId") String functionConfigId, @Param("tenantId") String tenantId);

    // 功能分析方法

    /**
     * 获取功能使用统计
     */
    Map<String, Object> getFunctionUsageStats(@Param("functionConfigId") String functionConfigId, 
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 获取功能性能统计
     */
    Map<String, Object> getFunctionPerformanceStats(@Param("functionConfigId") String functionConfigId, 
                                                    @Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime);

    /**
     * 获取功能错误统计
     */
    Map<String, Object> getFunctionErrorStats(@Param("functionConfigId") String functionConfigId, 
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 获取功能健康度评分
     */
    Map<String, Object> getFunctionHealthScore(@Param("functionConfigId") String functionConfigId);

    /**
     * 获取功能质量评估
     */
    Map<String, Object> getFunctionQualityAssessment(@Param("functionConfigId") String functionConfigId);

    /**
     * 生成功能报告
     */
    Map<String, Object> generateFunctionReport(@Param("functionConfigId") String functionConfigId, 
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    // 数据清理方法

    /**
     * 清理过期的功能数据
     */
    int cleanExpiredData(@Param("expiredDate") LocalDateTime expiredDate, @Param("tenantId") String tenantId);

    /**
     * 清理已废弃的功能
     */
    int cleanDeprecatedFunctions(@Param("deprecatedDate") LocalDateTime deprecatedDate, @Param("tenantId") String tenantId);

    /**
     * 清理无效的功能配置
     */
    int cleanInvalidConfigs(@Param("tenantId") String tenantId);

    // 系统维护方法

    /**
     * 重建功能索引
     */
    int rebuildFunctionIndex(@Param("tenantId") String tenantId);

    /**
     * 优化功能配置
     */
    int optimizeFunctionConfigs(@Param("tenantId") String tenantId);

    /**
     * 同步功能状态
     */
    int syncFunctionStatus(@Param("tenantId") String tenantId);

    /**
     * 验证功能配置
     */
    List<Map<String, Object>> validateFunctionConfigs(@Param("tenantId") String tenantId);

    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview(@Param("tenantId") String tenantId);
}
