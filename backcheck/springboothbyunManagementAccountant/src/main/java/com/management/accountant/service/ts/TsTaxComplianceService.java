package com.management.accountant.service.ts;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ts.TsTaxCompliance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 税务合规检查服务接口
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
public interface TsTaxComplianceService extends IService<TsTaxCompliance> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建合规检查
     */
    TsTaxCompliance createCompliance(Long tenantId, TsTaxCompliance compliance);

    /**
     * 更新合规检查
     */
    TsTaxCompliance updateCompliance(Long tenantId, Long complianceId, TsTaxCompliance compliance);

    /**
     * 删除合规检查
     */
    boolean deleteCompliance(Long tenantId, Long complianceId);

    /**
     * 获取合规检查详情
     */
    TsTaxCompliance getComplianceDetail(Long tenantId, Long complianceId);

    /**
     * 根据编号获取合规检查详情
     */
    TsTaxCompliance getComplianceByCode(Long tenantId, String complianceCode);

    /**
     * 分页查询合规检查
     */
    IPage<TsTaxCompliance> getCompliancePage(Long tenantId, Integer current, Integer size, Map<String, Object> params);

    // ==================== 合规检查管理功能 ====================

    /**
     * 生成合规检查编号
     */
    String generateComplianceCode(Long tenantId);

    /**
     * 验证合规检查数据
     */
    Map<String, Object> validateComplianceData(Long tenantId, TsTaxCompliance compliance);

    /**
     * 启动合规检查
     */
    boolean startComplianceCheck(Long tenantId, Long complianceId);

    /**
     * 暂停合规检查
     */
    boolean pauseComplianceCheck(Long tenantId, Long complianceId);

    /**
     * 恢复合规检查
     */
    boolean resumeComplianceCheck(Long tenantId, Long complianceId);

    /**
     * 完成合规检查
     */
    boolean completeComplianceCheck(Long tenantId, Long complianceId, Map<String, Object> completionData);

    /**
     * 取消合规检查
     */
    boolean cancelComplianceCheck(Long tenantId, Long complianceId, String reason);

    /**
     * 更新检查进度
     */
    boolean updateCheckProgress(Long tenantId, Long complianceId, Double progress);

    // ==================== 合规规则管理 ====================

    /**
     * 配置合规规则
     */
    boolean configureComplianceRule(Long tenantId, Long complianceId, Map<String, Object> ruleConfig);

    /**
     * 执行合规检查
     */
    Map<String, Object> executeComplianceCheck(Long tenantId, Long complianceId);

    /**
     * 评估合规风险
     */
    Map<String, Object> assessComplianceRisk(Long tenantId, Long complianceId);

    /**
     * 生成合规报告
     */
    Map<String, Object> generateComplianceReport(Long tenantId, Long complianceId);

    /**
     * 分析合规结果
     */
    Map<String, Object> analyzeComplianceResult(Long tenantId, Long complianceId);

    // ==================== 整改管理功能 ====================

    /**
     * 创建整改计划
     */
    boolean createRectificationPlan(Long tenantId, Long complianceId, Map<String, Object> planData);

    /**
     * 更新整改进度
     */
    boolean updateRectificationProgress(Long tenantId, Long complianceId, Double progress);

    /**
     * 完成整改
     */
    boolean completeRectification(Long tenantId, Long complianceId, Map<String, Object> completionData);

    /**
     * 申请复查
     */
    boolean requestRecheck(Long tenantId, Long complianceId);

    /**
     * 执行复查
     */
    Map<String, Object> executeRecheck(Long tenantId, Long complianceId);

    /**
     * 完成复查
     */
    boolean completeRecheck(Long tenantId, Long complianceId, Map<String, Object> recheckResult);

    // ==================== 查询统计功能 ====================

    /**
     * 根据检查类型查询
     */
    List<TsTaxCompliance> getCompliancesByType(Long tenantId, String complianceType);

    /**
     * 根据检查状态查询
     */
    List<TsTaxCompliance> getCompliancesByCheckStatus(Long tenantId, String checkStatus);

    /**
     * 根据合规状态查询
     */
    List<TsTaxCompliance> getCompliancesByComplianceStatus(Long tenantId, String complianceStatus);

    /**
     * 根据风险等级查询
     */
    List<TsTaxCompliance> getCompliancesByRiskLevel(Long tenantId, String riskLevel);

    /**
     * 根据优先级查询
     */
    List<TsTaxCompliance> getCompliancesByPriority(Long tenantId, String priority);

    /**
     * 根据检查人员查询
     */
    List<TsTaxCompliance> getCompliancesByChecker(Long tenantId, String checker);

    /**
     * 根据整改状态查询
     */
    List<TsTaxCompliance> getCompliancesByRectificationStatus(Long tenantId, String rectificationStatus);

    /**
     * 查询即将到期的检查
     */
    List<TsTaxCompliance> getExpiringSoonCompliances(Long tenantId, Integer days);

    /**
     * 查询逾期的检查
     */
    List<TsTaxCompliance> getOverdueCompliances(Long tenantId);

    /**
     * 查询高风险检查
     */
    List<TsTaxCompliance> getHighRiskCompliances(Long tenantId);

    /**
     * 查询需要整改的检查
     */
    List<TsTaxCompliance> getNeedRectificationCompliances(Long tenantId);

    // ==================== 统计分析功能 ====================

    /**
     * 获取合规检查概览
     */
    Map<String, Object> getComplianceOverview(Long tenantId);

    /**
     * 按检查状态统计数量
     */
    List<Map<String, Object>> countCompliancesByCheckStatus(Long tenantId);

    /**
     * 按合规状态统计数量
     */
    List<Map<String, Object>> countCompliancesByComplianceStatus(Long tenantId);

    /**
     * 按检查类型统计数量
     */
    List<Map<String, Object>> countCompliancesByType(Long tenantId);

    /**
     * 按风险等级统计数量
     */
    List<Map<String, Object>> countCompliancesByRiskLevel(Long tenantId);

    /**
     * 按优先级统计数量
     */
    List<Map<String, Object>> countCompliancesByPriority(Long tenantId);

    /**
     * 按整改状态统计数量
     */
    List<Map<String, Object>> countCompliancesByRectificationStatus(Long tenantId);

    /**
     * 获取检查趋势数据
     */
    List<Map<String, Object>> getCheckTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy);

    /**
     * 获取合规评分趋势
     */
    List<Map<String, Object>> getComplianceScoreTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy);

    /**
     * 获取风险分布数据
     */
    List<Map<String, Object>> getRiskDistribution(Long tenantId);

    /**
     * 获取效果评估数据
     */
    List<Map<String, Object>> getEffectivenessData(Long tenantId);

    /**
     * 获取检查排行榜
     */
    List<Map<String, Object>> getCheckRanking(Long tenantId, String rankBy, Integer limit);

    /**
     * 获取检查效率统计
     */
    Map<String, Object> getCheckEfficiencyStats(Long tenantId);

    // ==================== 批量操作功能 ====================

    /**
     * 批量创建合规检查
     */
    List<TsTaxCompliance> batchCreateCompliances(Long tenantId, List<TsTaxCompliance> compliances);

    /**
     * 批量更新检查状态
     */
    boolean batchUpdateCheckStatus(Long tenantId, List<Long> complianceIds, String checkStatus);

    /**
     * 批量更新合规状态
     */
    boolean batchUpdateComplianceStatus(Long tenantId, List<Long> complianceIds, String complianceStatus);

    /**
     * 批量更新整改状态
     */
    boolean batchUpdateRectificationStatus(Long tenantId, List<Long> complianceIds, String rectificationStatus);

    /**
     * 批量删除合规检查
     */
    boolean batchDeleteCompliances(Long tenantId, List<Long> complianceIds);

    /**
     * 批量归档合规检查
     */
    boolean batchArchiveCompliances(Long tenantId, List<Long> complianceIds);

    /**
     * 批量激活合规检查
     */
    boolean batchActivateCompliances(Long tenantId, List<Long> complianceIds);

    /**
     * 批量导入合规检查
     */
    Map<String, Object> batchImportCompliances(Long tenantId, List<Map<String, Object>> complianceData);

    /**
     * 批量导出合规检查
     */
    List<Map<String, Object>> batchExportCompliances(Long tenantId, List<Long> complianceIds);

    // ==================== 工具功能 ====================

    /**
     * 复制合规检查
     */
    TsTaxCompliance copyCompliance(Long tenantId, Long sourceComplianceId, String newComplianceName);

    /**
     * 发送合规提醒
     */
    boolean sendComplianceReminder(Long tenantId, Long complianceId, String reminderType);

    // ==================== 系统维护功能 ====================

    /**
     * 系统健康检查
     */
    Map<String, Object> systemHealthCheck(Long tenantId);

    /**
     * 数据一致性检查
     */
    List<Map<String, Object>> dataConsistencyCheck(Long tenantId);

    /**
     * 性能统计
     */
    Map<String, Object> performanceStats(Long tenantId);

    /**
     * 清理过期数据
     */
    boolean cleanupExpiredData(Long tenantId, Integer days);
}
