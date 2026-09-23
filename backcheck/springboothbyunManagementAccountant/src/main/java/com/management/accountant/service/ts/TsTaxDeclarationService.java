package com.management.accountant.service.ts;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ts.TsTaxDeclaration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 税务申报服务接口
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
public interface TsTaxDeclarationService extends IService<TsTaxDeclaration> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建税务申报
     */
    TsTaxDeclaration createDeclaration(TsTaxDeclaration declaration);

    /**
     * 更新税务申报
     */
    TsTaxDeclaration updateDeclaration(TsTaxDeclaration declaration);

    /**
     * 删除税务申报
     */
    boolean deleteDeclaration(Long tenantId, Long declarationId);

    /**
     * 获取税务申报详情
     */
    TsTaxDeclaration getDeclarationById(Long tenantId, Long declarationId);

    /**
     * 分页查询税务申报列表
     */
    IPage<TsTaxDeclaration> getDeclarationPage(Page<TsTaxDeclaration> page, 
                                              Long tenantId,
                                              String declarationCode,
                                              String declarationName,
                                              String taxType,
                                              String declarationType,
                                              String declarationStatus,
                                              String taxpayerName,
                                              String declarationPeriod,
                                              LocalDateTime startDate,
                                              LocalDateTime endDate,
                                              String businessCategory);

    // ==================== 申报管理功能 ====================

    /**
     * 生成申报计划
     */
    List<TsTaxDeclaration> generateDeclarationPlan(Long tenantId, String taxType, String period, Integer year);

    /**
     * 自动填报申报表
     */
    boolean autoFillDeclaration(Long tenantId, Long declarationId);

    /**
     * 提交申报
     */
    boolean submitDeclaration(Long tenantId, Long declarationId);

    /**
     * 撤回申报
     */
    boolean withdrawDeclaration(Long tenantId, Long declarationId);

    /**
     * 审核申报
     */
    boolean reviewDeclaration(Long tenantId, Long declarationId, String reviewResult, String reviewComment);

    /**
     * 批量提交申报
     */
    Map<String, Object> batchSubmitDeclarations(Long tenantId, List<Long> declarationIds);

    /**
     * 批量审核申报
     */
    Map<String, Object> batchReviewDeclarations(Long tenantId, List<Long> declarationIds, String reviewResult, String reviewComment);

    // ==================== 申报计算功能 ====================

    /**
     * 计算税额
     */
    Map<String, Object> calculateTaxAmount(Long tenantId, Long declarationId);

    /**
     * 重新计算税额
     */
    boolean recalculateTaxAmount(Long tenantId, Long declarationId);

    /**
     * 验证申报数据
     */
    Map<String, Object> validateDeclarationData(Long tenantId, Long declarationId);

    /**
     * 获取计税依据
     */
    Map<String, Object> getTaxBase(Long tenantId, Long declarationId);

    // ==================== 申报跟踪功能 ====================

    /**
     * 获取申报进度
     */
    Map<String, Object> getDeclarationProgress(Long tenantId, Long declarationId);

    /**
     * 更新申报状态
     */
    boolean updateDeclarationStatus(Long tenantId, Long declarationId, String status, String remark);

    /**
     * 获取申报历史
     */
    List<Map<String, Object>> getDeclarationHistory(Long tenantId, Long declarationId);

    /**
     * 获取申报日志
     */
    List<Map<String, Object>> getDeclarationLogs(Long tenantId, Long declarationId);

    // ==================== 申报提醒功能 ====================

    /**
     * 获取待申报列表
     */
    List<TsTaxDeclaration> getPendingDeclarations(Long tenantId, Integer limit);

    /**
     * 获取待审核申报列表
     */
    List<TsTaxDeclaration> getPendingReviewDeclarations(Long tenantId, Integer limit);

    /**
     * 获取逾期申报列表
     */
    List<TsTaxDeclaration> getOverdueDeclarations(Long tenantId);

    /**
     * 获取即将到期申报列表
     */
    List<TsTaxDeclaration> getUpcomingDeclarations(Long tenantId, Integer days);

    /**
     * 发送申报提醒
     */
    boolean sendDeclarationReminder(Long tenantId, Long declarationId);

    /**
     * 批量发送申报提醒
     */
    Map<String, Object> batchSendDeclarationReminders(Long tenantId, List<Long> declarationIds);

    // ==================== 统计分析功能 ====================

    /**
     * 获取申报概览
     */
    Map<String, Object> getDeclarationOverview(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计申报数量按状态分组
     */
    List<Map<String, Object>> countDeclarationsByStatus(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计申报数量按税种分组
     */
    List<Map<String, Object>> countDeclarationsByTaxType(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计申报数量按类型分组
     */
    List<Map<String, Object>> countDeclarationsByType(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计申报金额按月份分组
     */
    List<Map<String, Object>> sumTaxAmountByMonth(Long tenantId, Integer year);

    /**
     * 统计申报金额按税种分组
     */
    List<Map<String, Object>> sumTaxAmountByTaxType(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 计算申报及时率
     */
    Map<String, Object> calculateTimelyRate(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 计算申报成功率
     */
    Map<String, Object> calculateSuccessRate(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计申报处理时长
     */
    Map<String, Object> calculateProcessingTimeStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取申报趋势数据
     */
    List<Map<String, Object>> getDeclarationTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy);

    /**
     * 获取税额趋势数据
     */
    List<Map<String, Object>> getTaxAmountTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy);

    /**
     * 获取申报效率统计
     */
    Map<String, Object> getDeclarationEfficiencyStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取申报质量统计
     */
    Map<String, Object> getDeclarationQualityStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取合规性统计
     */
    Map<String, Object> getComplianceStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取风险分析
     */
    Map<String, Object> getRiskAnalysis(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取成本分析
     */
    Map<String, Object> getCostAnalysis(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    // ==================== 批量操作功能 ====================

    /**
     * 批量更新申报状态
     */
    Map<String, Object> batchUpdateStatus(Long tenantId, List<Long> declarationIds, String status);

    /**
     * 批量删除申报
     */
    Map<String, Object> batchDeleteDeclarations(Long tenantId, List<Long> declarationIds);

    /**
     * 批量导入申报
     */
    Map<String, Object> batchImportDeclarations(Long tenantId, List<Map<String, Object>> declarationData);

    /**
     * 批量导出申报
     */
    List<Map<String, Object>> batchExportDeclarations(Long tenantId, List<Long> declarationIds);

    // ==================== 查询功能 ====================

    /**
     * 根据申报编号查询申报
     */
    TsTaxDeclaration getDeclarationByCode(Long tenantId, String declarationCode);

    /**
     * 根据纳税人识别号查询申报列表
     */
    List<TsTaxDeclaration> getDeclarationsByTaxpayerId(Long tenantId, String taxpayerId);

    /**
     * 高级搜索申报
     */
    IPage<TsTaxDeclaration> advancedSearchDeclarations(Page<TsTaxDeclaration> page, Long tenantId, Map<String, Object> searchParams);

    /**
     * 全文搜索申报
     */
    IPage<TsTaxDeclaration> fullTextSearchDeclarations(Page<TsTaxDeclaration> page, Long tenantId, String keyword);

    // ==================== 系统维护功能 ====================

    /**
     * 获取异常申报列表
     */
    List<TsTaxDeclaration> getAbnormalDeclarations(Long tenantId, List<String> errorTypes);

    /**
     * 获取需要重试的申报列表
     */
    List<TsTaxDeclaration> getRetryDeclarations(Long tenantId);

    /**
     * 重试申报
     */
    boolean retryDeclaration(Long tenantId, Long declarationId);

    /**
     * 清理过期申报
     */
    int cleanExpiredDeclarations(Long tenantId, LocalDateTime expiredDate);

    /**
     * 系统健康检查
     */
    Map<String, Object> healthCheck(Long tenantId);

    /**
     * 获取系统性能指标
     */
    Map<String, Object> getPerformanceMetrics(Long tenantId);
}
