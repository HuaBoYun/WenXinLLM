package com.management.accountant.mapper.eps;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.eps.EpsDataQuality;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据质量管理Mapper接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface EpsDataQualityMapper extends BaseMapper<EpsDataQuality> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据质量编码获取数据质量
     */
    EpsDataQuality getByQualityCode(@Param("qualityCode") String qualityCode);

    /**
     * 根据质量类型获取数据质量列表
     */
    List<EpsDataQuality> getByQualityType(@Param("qualityType") String qualityType);

    /**
     * 根据质量分类获取数据质量列表
     */
    List<EpsDataQuality> getByQualityCategory(@Param("qualityCategory") String qualityCategory);

    /**
     * 根据质量模块获取数据质量列表
     */
    List<EpsDataQuality> getByQualityModule(@Param("qualityModule") String qualityModule);

    /**
     * 根据数据源ID获取数据质量列表
     */
    List<EpsDataQuality> getByDataSourceId(@Param("dataSourceId") String dataSourceId);

    /**
     * 根据检查状态获取数据质量列表
     */
    List<EpsDataQuality> getByCheckStatus(@Param("checkStatus") String checkStatus);

    /**
     * 根据检查结果获取数据质量列表
     */
    List<EpsDataQuality> getByCheckResult(@Param("checkResult") String checkResult);

    /**
     * 根据质量等级获取数据质量列表
     */
    List<EpsDataQuality> getByQualityLevel(@Param("qualityLevel") String qualityLevel);

    /**
     * 根据问题类型获取数据质量列表
     */
    List<EpsDataQuality> getByIssueType(@Param("issueType") String issueType);

    /**
     * 根据修复状态获取数据质量列表
     */
    List<EpsDataQuality> getByFixStatus(@Param("fixStatus") String fixStatus);

    /**
     * 根据检查批次号获取数据质量列表
     */
    List<EpsDataQuality> getByCheckBatchNo(@Param("checkBatchNo") String checkBatchNo);

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

    // ==================== 分页查询方法 ====================

    /**
     * 分页查询数据质量
     */
    IPage<EpsDataQuality> getDataQualityPage(Page<EpsDataQuality> page, @Param("params") Map<String, Object> params);

    /**
     * 条件分页查询数据质量
     */
    IPage<EpsDataQuality> getDataQualityPageByCondition(Page<EpsDataQuality> page, @Param("condition") EpsDataQuality condition);

    /**
     * 高级搜索分页查询
     */
    IPage<EpsDataQuality> advancedSearchPage(Page<EpsDataQuality> page, @Param("params") Map<String, Object> params);

    // ==================== 统计查询方法 ====================

    /**
     * 统计数据质量总数
     */
    Long countDataQuality();

    /**
     * 根据质量类型统计数据质量
     */
    Long countByQualityType(@Param("qualityType") String qualityType);

    /**
     * 根据质量分类统计数据质量
     */
    Long countByQualityCategory(@Param("qualityCategory") String qualityCategory);

    /**
     * 根据质量模块统计数据质量
     */
    Long countByQualityModule(@Param("qualityModule") String qualityModule);

    /**
     * 根据检查状态统计数据质量
     */
    Long countByCheckStatus(@Param("checkStatus") String checkStatus);

    /**
     * 根据检查结果统计数据质量
     */
    Long countByCheckResult(@Param("checkResult") String checkResult);

    /**
     * 根据质量等级统计数据质量
     */
    Long countByQualityLevel(@Param("qualityLevel") String qualityLevel);

    /**
     * 根据创建时间统计数据质量
     */
    Long countByCreateTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新检查状态
     */
    int batchUpdateCheckStatus(@Param("qualityIds") List<String> qualityIds, @Param("checkStatus") String checkStatus);

    /**
     * 批量更新检查结果
     */
    int batchUpdateCheckResult(@Param("qualityIds") List<String> qualityIds, @Param("checkResult") String checkResult);

    /**
     * 批量更新修复状态
     */
    int batchUpdateFixStatus(@Param("qualityIds") List<String> qualityIds, @Param("fixStatus") String fixStatus);

    /**
     * 批量设置自动修复
     */
    int batchSetAutoFix(@Param("qualityIds") List<String> qualityIds, @Param("autoFix") Boolean autoFix);

    /**
     * 批量删除数据质量
     */
    int batchDeleteDataQuality(@Param("qualityIds") List<String> qualityIds);

    // ==================== 数据质量检查方法 ====================

    /**
     * 更新检查状态
     */
    int updateCheckStatus(@Param("qualityId") String qualityId, @Param("checkStatus") String checkStatus);

    /**
     * 更新检查结果
     */
    int updateCheckResult(@Param("qualityId") String qualityId, @Param("checkResult") String checkResult, @Param("qualityScore") BigDecimal qualityScore);

    /**
     * 更新检查时间
     */
    int updateCheckTime(@Param("qualityId") String qualityId, @Param("checkTime") LocalDateTime checkTime);

    /**
     * 更新检查统计
     */
    int updateCheckStatistics(@Param("qualityId") String qualityId, @Param("totalCount") Integer totalCount, 
                             @Param("passCount") Integer passCount, @Param("failCount") Integer failCount, 
                             @Param("warningCount") Integer warningCount);

    /**
     * 更新问题信息
     */
    int updateIssueInfo(@Param("qualityId") String qualityId, @Param("issueCount") Integer issueCount, 
                       @Param("issueDescription") String issueDescription, @Param("issueType") String issueType, 
                       @Param("issueSeverity") String issueSeverity);

    /**
     * 更新修复信息
     */
    int updateFixInfo(@Param("qualityId") String qualityId, @Param("fixStatus") String fixStatus, 
                     @Param("fixSolution") String fixSolution, @Param("fixTime") LocalDateTime fixTime, 
                     @Param("fixUserId") String fixUserId, @Param("fixUserName") String fixUserName);

    // ==================== 搜索方法 ====================

    /**
     * 根据关键词搜索数据质量
     */
    List<EpsDataQuality> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 根据标签搜索数据质量
     */
    List<EpsDataQuality> searchByTags(@Param("tags") List<String> tags);

    /**
     * 查找相似数据质量
     */
    List<EpsDataQuality> findSimilarDataQuality(@Param("qualityId") String qualityId);

    /**
     * 查找热门数据质量
     */
    List<EpsDataQuality> findPopularDataQuality(@Param("limit") Integer limit);

    /**
     * 查找推荐数据质量
     */
    List<EpsDataQuality> findRecommendedDataQuality(@Param("userId") String userId, @Param("limit") Integer limit);

    /**
     * 查找需要关注的数据质量
     */
    List<EpsDataQuality> findDataQualityNeedingAttention();

    /**
     * 查找高风险数据质量
     */
    List<EpsDataQuality> findHighRiskDataQuality();

    // ==================== 分析统计方法 ====================

    /**
     * 获取数据质量使用统计
     */
    Map<String, Object> getDataQualityUsageStats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取数据质量性能统计
     */
    Map<String, Object> getDataQualityPerformanceStats(@Param("qualityId") String qualityId);

    /**
     * 获取数据质量趋势统计
     */
    Map<String, Object> getDataQualityTrendStats(@Param("qualityId") String qualityId);

    /**
     * 获取数据质量健康评分
     */
    BigDecimal getDataQualityHealthScore(@Param("qualityId") String qualityId);

    /**
     * 生成数据质量报告
     */
    Map<String, Object> generateDataQualityReport(@Param("params") Map<String, Object> params);

    /**
     * 获取数据质量监控数据
     */
    Map<String, Object> getDataQualityMonitoringData(@Param("params") Map<String, Object> params);

    /**
     * 获取数据质量异常记录
     */
    List<Map<String, Object>> getDataQualityExceptionRecords(@Param("params") Map<String, Object> params);

    /**
     * 获取数据质量改进建议
     */
    List<Map<String, Object>> getDataQualityImprovementSuggestions(@Param("qualityId") String qualityId);

    // ==================== 数据清理方法 ====================

    /**
     * 清理过期数据质量记录
     */
    int cleanExpiredDataQuality();

    /**
     * 清理无效数据质量记录
     */
    int cleanInvalidDataQuality();

    /**
     * 清理重复数据质量记录
     */
    int cleanDuplicateDataQuality();

    // ==================== 系统维护方法 ====================

    /**
     * 重建数据质量索引
     */
    int rebuildDataQualityIndex();

    /**
     * 优化数据质量存储
     */
    int optimizeDataQualityStorage();

    /**
     * 同步数据质量状态
     */
    int syncDataQualityStatus();

    /**
     * 验证数据质量完整性
     */
    List<String> validateDataQualityIntegrity();

    /**
     * 获取系统概览
     */
    Map<String, Object> getSystemOverview();

    // ==================== 质量规则方法 ====================

    /**
     * 根据规则ID获取数据质量列表
     */
    List<EpsDataQuality> getByRuleId(@Param("ruleId") String ruleId);

    /**
     * 根据规则类型获取数据质量列表
     */
    List<EpsDataQuality> getByRuleType(@Param("ruleType") String ruleType);

    /**
     * 执行质量规则检查
     */
    Map<String, Object> executeQualityRuleCheck(@Param("ruleId") String ruleId, @Param("params") Map<String, Object> params);

    /**
     * 获取规则执行历史
     */
    List<Map<String, Object>> getRuleExecutionHistory(@Param("ruleId") String ruleId);

    /**
     * 获取规则执行统计
     */
    Map<String, Object> getRuleExecutionStats(@Param("ruleId") String ruleId);

    // ==================== 数据源质量方法 ====================

    /**
     * 获取数据源质量概览
     */
    Map<String, Object> getDataSourceQualityOverview(@Param("dataSourceId") String dataSourceId);

    /**
     * 获取数据源质量趋势
     */
    List<Map<String, Object>> getDataSourceQualityTrend(@Param("dataSourceId") String dataSourceId, @Param("days") Integer days);

    /**
     * 获取数据源质量排名
     */
    List<Map<String, Object>> getDataSourceQualityRanking(@Param("limit") Integer limit);

    /**
     * 比较数据源质量
     */
    Map<String, Object> compareDataSourceQuality(@Param("sourceIds") List<String> sourceIds);
}
