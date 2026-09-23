package com.management.accountant.service.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.eps.EpsDataQuality;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据质量管理服务接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
public interface EpsDataQualityService extends IService<EpsDataQuality> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建数据质量
     */
    boolean createDataQuality(EpsDataQuality dataQuality);

    /**
     * 更新数据质量
     */
    boolean updateDataQuality(EpsDataQuality dataQuality);

    /**
     * 删除数据质量
     */
    boolean deleteDataQuality(String qualityId);

    /**
     * 根据ID获取数据质量
     */
    EpsDataQuality getDataQualityById(String qualityId);

    /**
     * 根据编码获取数据质量
     */
    EpsDataQuality getDataQualityByCode(String qualityCode);

    // ==================== 查询操作 ====================

    /**
     * 根据质量类型获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByType(String qualityType);

    /**
     * 根据质量分类获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByCategory(String qualityCategory);

    /**
     * 根据质量模块获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByModule(String qualityModule);

    /**
     * 根据数据源ID获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByDataSourceId(String dataSourceId);

    /**
     * 根据检查状态获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByCheckStatus(String checkStatus);

    /**
     * 根据检查结果获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByCheckResult(String checkResult);

    /**
     * 根据质量等级获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByQualityLevel(String qualityLevel);

    /**
     * 根据问题类型获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByIssueType(String issueType);

    /**
     * 根据修复状态获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByFixStatus(String fixStatus);

    /**
     * 根据检查批次号获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByCheckBatchNo(String checkBatchNo);

    /**
     * 获取待检查的数据质量列表
     */
    List<EpsDataQuality> getPendingCheckDataQuality();

    /**
     * 获取检查失败的数据质量列表
     */
    List<EpsDataQuality> getFailedCheckDataQuality();

    /**
     * 获取需要修复的数据质量列表
     */
    List<EpsDataQuality> getNeedFixDataQuality();

    // ==================== 分页查询操作 ====================

    /**
     * 分页查询数据质量
     */
    IPage<EpsDataQuality> getDataQualityPage(Page<EpsDataQuality> page, Map<String, Object> params);

    /**
     * 条件分页查询数据质量
     */
    IPage<EpsDataQuality> getDataQualityPageByCondition(Page<EpsDataQuality> page, EpsDataQuality condition);

    /**
     * 高级搜索分页查询
     */
    IPage<EpsDataQuality> advancedSearchPage(Page<EpsDataQuality> page, Map<String, Object> params);

    // ==================== 数据质量检查操作 ====================

    /**
     * 执行数据质量检查
     */
    boolean executeDataQualityCheck(String qualityId);

    /**
     * 批量执行数据质量检查
     */
    boolean batchExecuteDataQualityCheck(List<String> qualityIds);

    /**
     * 自动执行数据质量检查
     */
    boolean autoExecuteDataQualityCheck();

    /**
     * 定时执行数据质量检查
     */
    boolean scheduledExecuteDataQualityCheck();

    /**
     * 更新检查状态
     */
    boolean updateCheckStatus(String qualityId, String checkStatus);

    /**
     * 更新检查结果
     */
    boolean updateCheckResult(String qualityId, String checkResult, BigDecimal qualityScore);

    /**
     * 更新检查时间
     */
    boolean updateCheckTime(String qualityId, LocalDateTime checkTime);

    /**
     * 更新检查统计
     */
    boolean updateCheckStatistics(String qualityId, Integer totalCount, Integer passCount, Integer failCount, Integer warningCount);

    /**
     * 更新问题信息
     */
    boolean updateIssueInfo(String qualityId, Integer issueCount, String issueDescription, String issueType, String issueSeverity);

    /**
     * 更新修复信息
     */
    boolean updateFixInfo(String qualityId, String fixStatus, String fixSolution, LocalDateTime fixTime, String fixUserId, String fixUserName);

    // ==================== 数据质量修复操作 ====================

    /**
     * 修复数据质量问题
     */
    boolean fixDataQualityIssue(String qualityId);

    /**
     * 批量修复数据质量问题
     */
    boolean batchFixDataQualityIssue(List<String> qualityIds);

    /**
     * 自动修复数据质量问题
     */
    boolean autoFixDataQualityIssue(String qualityId);

    /**
     * 手动修复数据质量问题
     */
    boolean manualFixDataQualityIssue(String qualityId, String fixSolution);

    /**
     * 忽略数据质量问题
     */
    boolean ignoreDataQualityIssue(String qualityId, String reason);

    // ==================== 批量操作 ====================

    /**
     * 批量更新检查状态
     */
    boolean batchUpdateCheckStatus(List<String> qualityIds, String checkStatus);

    /**
     * 批量更新检查结果
     */
    boolean batchUpdateCheckResult(List<String> qualityIds, String checkResult);

    /**
     * 批量更新修复状态
     */
    boolean batchUpdateFixStatus(List<String> qualityIds, String fixStatus);

    /**
     * 批量设置自动修复
     */
    boolean batchSetAutoFix(List<String> qualityIds, Boolean autoFix);

    /**
     * 批量启用数据质量
     */
    boolean batchEnableDataQuality(List<String> qualityIds);

    /**
     * 批量禁用数据质量
     */
    boolean batchDisableDataQuality(List<String> qualityIds);

    /**
     * 批量删除数据质量
     */
    boolean batchDeleteDataQuality(List<String> qualityIds);

    // ==================== 搜索功能 ====================

    /**
     * 根据关键词搜索数据质量
     */
    List<EpsDataQuality> searchDataQualityByKeyword(String keyword);

    /**
     * 根据标签搜索数据质量
     */
    List<EpsDataQuality> searchDataQualityByTags(List<String> tags);

    /**
     * 查找相似数据质量
     */
    List<EpsDataQuality> findSimilarDataQuality(String qualityId);

    /**
     * 查找热门数据质量
     */
    List<EpsDataQuality> findPopularDataQuality(Integer limit);

    /**
     * 查找推荐数据质量
     */
    List<EpsDataQuality> findRecommendedDataQuality(String userId, Integer limit);

    /**
     * 查找需要关注的数据质量
     */
    List<EpsDataQuality> findDataQualityNeedingAttention();

    /**
     * 查找高风险数据质量
     */
    List<EpsDataQuality> findHighRiskDataQuality();

    // ==================== 验证功能 ====================

    /**
     * 验证数据质量
     */
    boolean validateDataQuality(EpsDataQuality dataQuality);

    /**
     * 验证数据质量完整性
     */
    boolean validateDataQualityIntegrity(String qualityId);

    /**
     * 测试数据质量连接
     */
    boolean testDataQualityConnection(String qualityId);

    /**
     * 检查数据质量配置
     */
    boolean checkDataQualityConfiguration(String qualityId);

    // ==================== 统计分析功能 ====================

    /**
     * 统计数据质量总数
     */
    Long countDataQuality();

    /**
     * 根据质量类型统计数据质量
     */
    Long countDataQualityByType(String qualityType);

    /**
     * 根据质量分类统计数据质量
     */
    Long countDataQualityByCategory(String qualityCategory);

    /**
     * 根据质量模块统计数据质量
     */
    Long countDataQualityByModule(String qualityModule);

    /**
     * 根据检查状态统计数据质量
     */
    Long countDataQualityByCheckStatus(String checkStatus);

    /**
     * 根据检查结果统计数据质量
     */
    Long countDataQualityByCheckResult(String checkResult);

    /**
     * 根据质量等级统计数据质量
     */
    Long countDataQualityByQualityLevel(String qualityLevel);

    /**
     * 获取数据质量使用统计
     */
    Map<String, Object> getDataQualityUsageStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取数据质量性能统计
     */
    Map<String, Object> getDataQualityPerformanceStats(String qualityId);

    /**
     * 获取数据质量趋势统计
     */
    Map<String, Object> getDataQualityTrendStats(String qualityId);

    /**
     * 获取数据质量健康评分
     */
    BigDecimal getDataQualityHealthScore(String qualityId);

    /**
     * 生成数据质量报告
     */
    Map<String, Object> generateDataQualityReport(Map<String, Object> params);

    /**
     * 获取数据质量监控数据
     */
    Map<String, Object> getDataQualityMonitoringData(Map<String, Object> params);

    /**
     * 获取数据质量异常记录
     */
    List<Map<String, Object>> getDataQualityExceptionRecords(Map<String, Object> params);

    /**
     * 获取数据质量改进建议
     */
    List<Map<String, Object>> getDataQualityImprovementSuggestions(String qualityId);

    // ==================== 导入导出功能 ====================

    /**
     * 导入数据质量
     */
    boolean importDataQuality(List<EpsDataQuality> dataQualityList);

    /**
     * 导出数据质量
     */
    List<EpsDataQuality> exportDataQuality(Map<String, Object> params);

    /**
     * 导出数据质量模板
     */
    String exportDataQualityTemplate();

    /**
     * 导入数据质量模板
     */
    boolean importDataQualityTemplate(String templatePath);

    // ==================== 数据清理功能 ====================

    /**
     * 清理过期数据质量记录
     */
    boolean cleanExpiredDataQuality();

    /**
     * 清理无效数据质量记录
     */
    boolean cleanInvalidDataQuality();

    /**
     * 清理重复数据质量记录
     */
    boolean cleanDuplicateDataQuality();

    // ==================== 系统维护功能 ====================

    /**
     * 重建数据质量索引
     */
    boolean rebuildDataQualityIndex();

    /**
     * 优化数据质量存储
     */
    boolean optimizeDataQualityStorage();

    /**
     * 同步数据质量状态
     */
    boolean syncDataQualityStatus();

    /**
     * 获取系统概览
     */
    Map<String, Object> getSystemOverview();

    // ==================== 质量规则功能 ====================

    /**
     * 根据规则ID获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByRuleId(String ruleId);

    /**
     * 根据规则类型获取数据质量列表
     */
    List<EpsDataQuality> getDataQualityByRuleType(String ruleType);

    /**
     * 执行质量规则检查
     */
    Map<String, Object> executeQualityRuleCheck(String ruleId, Map<String, Object> params);

    /**
     * 获取规则执行历史
     */
    List<Map<String, Object>> getRuleExecutionHistory(String ruleId);

    /**
     * 获取规则执行统计
     */
    Map<String, Object> getRuleExecutionStats(String ruleId);

    // ==================== 数据源质量功能 ====================

    /**
     * 获取数据源质量概览
     */
    Map<String, Object> getDataSourceQualityOverview(String dataSourceId);

    /**
     * 获取数据源质量趋势
     */
    List<Map<String, Object>> getDataSourceQualityTrend(String dataSourceId, Integer days);

    /**
     * 获取数据源质量排名
     */
    List<Map<String, Object>> getDataSourceQualityRanking(Integer limit);

    /**
     * 比较数据源质量
     */
    Map<String, Object> compareDataSourceQuality(List<String> sourceIds);
}
