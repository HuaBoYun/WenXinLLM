package com.management.accountant.service.intg;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.intg.IntgApiManagement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * API接口管理服务接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
public interface IntgApiManagementService extends IService<IntgApiManagement> {

    // 基础CRUD操作

    /**
     * 创建API管理
     */
    IntgApiManagement createApiManagement(IntgApiManagement apiManagement);

    /**
     * 更新API管理
     */
    IntgApiManagement updateApiManagement(IntgApiManagement apiManagement);

    /**
     * 删除API管理
     */
    boolean deleteApiManagement(String apiId);

    /**
     * 根据ID获取API管理
     */
    IntgApiManagement getApiManagementById(String apiId);

    /**
     * 根据编码获取API管理
     */
    IntgApiManagement getApiManagementByCode(String apiCode);

    // 查询操作

    /**
     * 分页查询API管理
     */
    IPage<IntgApiManagement> getApiManagementPage(Integer current, Integer size, Map<String, Object> params);

    /**
     * 根据API类型获取API列表
     */
    List<IntgApiManagement> getApiManagementsByType(String apiType);

    /**
     * 根据API分类获取API列表
     */
    List<IntgApiManagement> getApiManagementsByCategory(String apiCategory);

    /**
     * 根据状态获取API列表
     */
    List<IntgApiManagement> getApiManagementsByStatus(String status);

    /**
     * 根据HTTP方法获取API列表
     */
    List<IntgApiManagement> getApiManagementsByHttpMethod(String httpMethod);

    /**
     * 根据服务提供者获取API列表
     */
    List<IntgApiManagement> getApiManagementsByServiceProvider(String serviceProvider);

    /**
     * 根据业务域获取API列表
     */
    List<IntgApiManagement> getApiManagementsByBusinessDomain(String businessDomain);

    /**
     * 获取启用的API列表
     */
    List<IntgApiManagement> getActiveApiManagements();

    /**
     * 获取已废弃的API列表
     */
    List<IntgApiManagement> getDeprecatedApiManagements();

    /**
     * 获取需要认证的API列表
     */
    List<IntgApiManagement> getAuthRequiredApiManagements();

    // API管理操作

    /**
     * 启用API
     */
    boolean enableApiManagement(String apiId);

    /**
     * 禁用API
     */
    boolean disableApiManagement(String apiId);

    /**
     * 设置API为废弃
     */
    boolean deprecateApiManagement(String apiId, LocalDateTime deprecationDate, String replacementApiId);

    /**
     * 批量更新API状态
     */
    boolean batchUpdateApiStatus(List<String> apiIds, String status);

    /**
     * 批量启用API
     */
    boolean batchEnableApiManagements(List<String> apiIds);

    /**
     * 批量禁用API
     */
    boolean batchDisableApiManagements(List<String> apiIds);

    /**
     * 批量设置为废弃
     */
    boolean batchDeprecateApiManagements(List<String> apiIds, LocalDateTime deprecationDate);

    /**
     * 批量更新监控状态
     */
    boolean batchUpdateMonitoringStatus(List<String> apiIds, Boolean monitoringEnabled);

    /**
     * 批量更新日志级别
     */
    boolean batchUpdateLoggingLevel(List<String> apiIds, String loggingLevel);

    // API版本管理

    /**
     * 获取API的所有版本
     */
    List<IntgApiManagement> getApiVersions(String apiCode);

    /**
     * 获取API的最新版本
     */
    IntgApiManagement getLatestApiVersion(String apiCode);

    /**
     * 检查API版本是否存在
     */
    boolean checkApiVersionExists(String apiCode, String apiVersion);

    /**
     * 创建API新版本
     */
    IntgApiManagement createApiVersion(String baseApiId, String newVersion);

    /**
     * 发布API版本
     */
    boolean publishApiVersion(String apiId);

    /**
     * 回滚API版本
     */
    boolean rollbackApiVersion(String apiId, String targetVersion);

    // API查找操作

    /**
     * 根据接口路径查找API
     */
    List<IntgApiManagement> findApiManagementsByEndpointPath(String endpointPath);

    /**
     * 根据关键词搜索API
     */
    List<IntgApiManagement> searchApiManagementsByKeyword(String keyword);

    /**
     * 根据标签搜索API
     */
    List<IntgApiManagement> searchApiManagementsByTags(List<String> tags);

    /**
     * 查找相似的API
     */
    List<IntgApiManagement> findSimilarApiManagements(String apiId);

    /**
     * 查找依赖的API
     */
    List<IntgApiManagement> findDependentApiManagements(String apiId);

    /**
     * 查找被依赖的API
     */
    List<IntgApiManagement> findDependencyApiManagements(String apiId);

    // API验证操作

    /**
     * 验证API配置
     */
    Map<String, Object> validateApiConfiguration(IntgApiManagement apiManagement);

    /**
     * 验证API结构
     */
    Map<String, Object> validateApiSchema(String apiId);

    /**
     * 验证API文档
     */
    Map<String, Object> validateApiDocumentation(String apiId);

    /**
     * 测试API连接
     */
    Map<String, Object> testApiConnection(String apiId);

    /**
     * 测试API功能
     */
    Map<String, Object> testApiFunction(String apiId, Map<String, Object> testData);

    // API分析操作

    /**
     * 获取API使用统计
     */
    Map<String, Object> getApiUsageStats(String apiId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取API性能统计
     */
    Map<String, Object> getApiPerformanceStats(String apiId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取API错误统计
     */
    Map<String, Object> getApiErrorStats(String apiId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取API健康度评分
     */
    Map<String, Object> getApiHealthScore(String apiId);

    /**
     * 获取API质量评估
     */
    Map<String, Object> getApiQualityAssessment(String apiId);

    /**
     * 生成API报告
     */
    Map<String, Object> generateApiReport(String reportType, LocalDateTime startTime, LocalDateTime endTime);

    // 统计分析操作

    /**
     * 统计API总数
     */
    Long countApiManagements();

    /**
     * 按API类型统计数量
     */
    List<Map<String, Object>> countByApiType();

    /**
     * 按API分类统计数量
     */
    List<Map<String, Object>> countByApiCategory();

    /**
     * 按状态统计数量
     */
    List<Map<String, Object>> countByStatus();

    /**
     * 按HTTP方法统计数量
     */
    List<Map<String, Object>> countByHttpMethod();

    /**
     * 按服务提供者统计数量
     */
    List<Map<String, Object>> countByServiceProvider();

    /**
     * 按业务域统计数量
     */
    List<Map<String, Object>> countByBusinessDomain();

    /**
     * 按创建时间统计数量
     */
    List<Map<String, Object>> countByCreateTime(LocalDateTime startTime, LocalDateTime endTime);

    // API导入导出操作

    /**
     * 导入API配置
     */
    Map<String, Object> importApiConfigurations(String importData, String importFormat);

    /**
     * 导出API配置
     */
    Map<String, Object> exportApiConfigurations(List<String> apiIds, String exportFormat);

    /**
     * 生成API文档
     */
    Map<String, Object> generateApiDocumentation(String apiId, String docFormat);

    /**
     * 生成API SDK
     */
    Map<String, Object> generateApiSdk(String apiId, String language);

    // 数据清理操作

    /**
     * 清理过期的API数据
     */
    int cleanExpiredApiData(LocalDateTime expiredDate);

    /**
     * 清理废弃的API
     */
    int cleanDeprecatedApis(LocalDateTime deprecatedDate);

    /**
     * 清理无效的API配置
     */
    int cleanInvalidApiConfigs();

    // 系统维护操作

    /**
     * 重建API索引
     */
    boolean rebuildApiIndex();

    /**
     * 优化API配置
     */
    boolean optimizeApiConfigurations();

    /**
     * 同步API状态
     */
    boolean syncApiStatus();

    /**
     * 验证API配置
     */
    List<Map<String, Object>> validateApiConfigurations();

    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview();

    /**
     * 检查系统健康状态
     */
    Map<String, Object> checkSystemHealth();

    /**
     * 评估API质量
     */
    Map<String, Object> assessApiQuality();
}
