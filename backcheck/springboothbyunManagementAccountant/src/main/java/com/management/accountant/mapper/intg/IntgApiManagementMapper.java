package com.management.accountant.mapper.intg;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.intg.IntgApiManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * API接口管理 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface IntgApiManagementMapper extends BaseMapper<IntgApiManagement> {

    // 基础查询方法

    /**
     * 根据API编码获取API管理信息
     */
    IntgApiManagement getByApiCode(@Param("apiCode") String apiCode, @Param("tenantId") String tenantId);

    /**
     * 根据API类型获取API列表
     */
    List<IntgApiManagement> getByApiType(@Param("apiType") String apiType, @Param("tenantId") String tenantId);

    /**
     * 根据API分类获取API列表
     */
    List<IntgApiManagement> getByApiCategory(@Param("apiCategory") String apiCategory, @Param("tenantId") String tenantId);

    /**
     * 根据状态获取API列表
     */
    List<IntgApiManagement> getByStatus(@Param("status") String status, @Param("tenantId") String tenantId);

    /**
     * 根据HTTP方法获取API列表
     */
    List<IntgApiManagement> getByHttpMethod(@Param("httpMethod") String httpMethod, @Param("tenantId") String tenantId);

    /**
     * 根据服务提供者获取API列表
     */
    List<IntgApiManagement> getByServiceProvider(@Param("serviceProvider") String serviceProvider, @Param("tenantId") String tenantId);

    /**
     * 根据业务域获取API列表
     */
    List<IntgApiManagement> getByBusinessDomain(@Param("businessDomain") String businessDomain, @Param("tenantId") String tenantId);

    /**
     * 获取启用的API列表
     */
    List<IntgApiManagement> getActiveApis(@Param("tenantId") String tenantId);

    /**
     * 获取已废弃的API列表
     */
    List<IntgApiManagement> getDeprecatedApis(@Param("tenantId") String tenantId);

    /**
     * 获取需要认证的API列表
     */
    List<IntgApiManagement> getAuthRequiredApis(@Param("tenantId") String tenantId);

    // 分页查询方法

    /**
     * 分页查询API管理信息
     */
    IPage<IntgApiManagement> getApiPage(Page<IntgApiManagement> page, @Param("params") Map<String, Object> params);

    /**
     * 根据条件分页查询API
     */
    IPage<IntgApiManagement> getApiPageByCondition(Page<IntgApiManagement> page, @Param("condition") IntgApiManagement condition);

    /**
     * 高级搜索分页查询
     */
    IPage<IntgApiManagement> advancedSearchPage(Page<IntgApiManagement> page, @Param("params") Map<String, Object> params);

    // 统计查询方法

    /**
     * 统计API总数
     */
    Long countApis(@Param("tenantId") String tenantId);

    /**
     * 按API类型统计数量
     */
    List<Map<String, Object>> countByApiType(@Param("tenantId") String tenantId);

    /**
     * 按API分类统计数量
     */
    List<Map<String, Object>> countByApiCategory(@Param("tenantId") String tenantId);

    /**
     * 按状态统计数量
     */
    List<Map<String, Object>> countByStatus(@Param("tenantId") String tenantId);

    /**
     * 按HTTP方法统计数量
     */
    List<Map<String, Object>> countByHttpMethod(@Param("tenantId") String tenantId);

    /**
     * 按服务提供者统计数量
     */
    List<Map<String, Object>> countByServiceProvider(@Param("tenantId") String tenantId);

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

    // API管理操作方法

    /**
     * 批量更新API状态
     */
    int batchUpdateStatus(@Param("apiIds") List<String> apiIds, 
                         @Param("status") String status, 
                         @Param("updatedBy") String updatedBy, 
                         @Param("tenantId") String tenantId);

    /**
     * 批量启用API
     */
    int batchEnableApis(@Param("apiIds") List<String> apiIds, 
                       @Param("updatedBy") String updatedBy, 
                       @Param("tenantId") String tenantId);

    /**
     * 批量禁用API
     */
    int batchDisableApis(@Param("apiIds") List<String> apiIds, 
                        @Param("updatedBy") String updatedBy, 
                        @Param("tenantId") String tenantId);

    /**
     * 批量设置为废弃
     */
    int batchDeprecateApis(@Param("apiIds") List<String> apiIds, 
                          @Param("deprecationDate") LocalDateTime deprecationDate,
                          @Param("updatedBy") String updatedBy, 
                          @Param("tenantId") String tenantId);

    /**
     * 批量更新监控状态
     */
    int batchUpdateMonitoringStatus(@Param("apiIds") List<String> apiIds, 
                                   @Param("monitoringEnabled") Boolean monitoringEnabled,
                                   @Param("updatedBy") String updatedBy, 
                                   @Param("tenantId") String tenantId);

    /**
     * 批量更新日志级别
     */
    int batchUpdateLoggingLevel(@Param("apiIds") List<String> apiIds, 
                               @Param("loggingLevel") String loggingLevel,
                               @Param("updatedBy") String updatedBy, 
                               @Param("tenantId") String tenantId);

    // API版本管理方法

    /**
     * 获取API的所有版本
     */
    List<IntgApiManagement> getApiVersions(@Param("apiCode") String apiCode, @Param("tenantId") String tenantId);

    /**
     * 获取API的最新版本
     */
    IntgApiManagement getLatestVersion(@Param("apiCode") String apiCode, @Param("tenantId") String tenantId);

    /**
     * 检查API版本是否存在
     */
    boolean checkVersionExists(@Param("apiCode") String apiCode, 
                              @Param("apiVersion") String apiVersion, 
                              @Param("tenantId") String tenantId);

    // API查找方法

    /**
     * 根据接口路径查找API
     */
    List<IntgApiManagement> findByEndpointPath(@Param("endpointPath") String endpointPath, @Param("tenantId") String tenantId);

    /**
     * 根据关键词搜索API
     */
    List<IntgApiManagement> searchByKeyword(@Param("keyword") String keyword, @Param("tenantId") String tenantId);

    /**
     * 根据标签搜索API
     */
    List<IntgApiManagement> searchByTags(@Param("tags") List<String> tags, @Param("tenantId") String tenantId);

    /**
     * 查找相似的API
     */
    List<IntgApiManagement> findSimilarApis(@Param("apiId") String apiId, @Param("tenantId") String tenantId);

    /**
     * 查找依赖的API
     */
    List<IntgApiManagement> findDependentApis(@Param("apiId") String apiId, @Param("tenantId") String tenantId);

    /**
     * 查找被依赖的API
     */
    List<IntgApiManagement> findDependencyApis(@Param("apiId") String apiId, @Param("tenantId") String tenantId);

    // API分析方法

    /**
     * 获取API使用统计
     */
    Map<String, Object> getApiUsageStats(@Param("apiId") String apiId, 
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);

    /**
     * 获取API性能统计
     */
    Map<String, Object> getApiPerformanceStats(@Param("apiId") String apiId, 
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    /**
     * 获取API错误统计
     */
    Map<String, Object> getApiErrorStats(@Param("apiId") String apiId, 
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);

    /**
     * 获取API健康度评分
     */
    Map<String, Object> getApiHealthScore(@Param("apiId") String apiId);

    /**
     * 获取API质量评估
     */
    Map<String, Object> getApiQualityAssessment(@Param("apiId") String apiId);

    // 数据清理方法

    /**
     * 清理过期的API数据
     */
    int cleanExpiredData(@Param("expiredDate") LocalDateTime expiredDate, @Param("tenantId") String tenantId);

    /**
     * 清理废弃的API
     */
    int cleanDeprecatedApis(@Param("deprecatedDate") LocalDateTime deprecatedDate, @Param("tenantId") String tenantId);

    /**
     * 清理无效的API配置
     */
    int cleanInvalidConfigs(@Param("tenantId") String tenantId);

    // 系统维护方法

    /**
     * 重建API索引
     */
    int rebuildApiIndex(@Param("tenantId") String tenantId);

    /**
     * 优化API配置
     */
    int optimizeApiConfigs(@Param("tenantId") String tenantId);

    /**
     * 同步API状态
     */
    int syncApiStatus(@Param("tenantId") String tenantId);

    /**
     * 验证API配置
     */
    List<Map<String, Object>> validateApiConfigs(@Param("tenantId") String tenantId);

    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview(@Param("tenantId") String tenantId);
}
