package com.management.accountant.service.ts;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ts.TsTaxPlanning;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 税务筹划服务接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
public interface TsTaxPlanningService extends IService<TsTaxPlanning> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建税务筹划
     */
    TsTaxPlanning createPlanning(Long tenantId, TsTaxPlanning planning);

    /**
     * 更新税务筹划
     */
    TsTaxPlanning updatePlanning(Long tenantId, Long planningId, TsTaxPlanning planning);

    /**
     * 删除税务筹划
     */
    boolean deletePlanning(Long tenantId, Long planningId);

    /**
     * 根据ID获取税务筹划
     */
    TsTaxPlanning getPlanningById(Long tenantId, Long planningId);

    /**
     * 根据编号获取税务筹划
     */
    TsTaxPlanning getPlanningByCode(Long tenantId, String planningCode);

    /**
     * 分页查询税务筹划
     */
    IPage<TsTaxPlanning> getPlanningPage(Long tenantId, Page<TsTaxPlanning> page, Map<String, Object> params);

    // ==================== 筹划管理功能 ====================

    /**
     * 生成筹划编号
     */
    String generatePlanningCode(Long tenantId);

    /**
     * 验证筹划数据
     */
    Map<String, Object> validatePlanningData(Long tenantId, TsTaxPlanning planning);

    /**
     * 计算筹划效益
     */
    Map<String, Object> calculatePlanningBenefit(Long tenantId, Long planningId);

    /**
     * 评估筹划风险
     */
    Map<String, Object> assessPlanningRisk(Long tenantId, Long planningId);

    /**
     * 分析筹划可行性
     */
    Map<String, Object> analyzeFeasibility(Long tenantId, Long planningId);

    /**
     * 优化筹划方案
     */
    Map<String, Object> optimizePlanningScheme(Long tenantId, Long planningId);

    /**
     * 比较筹划方案
     */
    Map<String, Object> comparePlanningSchemes(Long tenantId, List<Long> planningIds);

    /**
     * 推荐筹划方案
     */
    List<Map<String, Object>> recommendPlanningSchemes(Long tenantId, Map<String, Object> criteria);

    // ==================== 执行管理功能 ====================

    /**
     * 启动筹划执行
     */
    boolean startPlanningExecution(Long tenantId, Long planningId);

    /**
     * 暂停筹划执行
     */
    boolean pausePlanningExecution(Long tenantId, Long planningId);

    /**
     * 恢复筹划执行
     */
    boolean resumePlanningExecution(Long tenantId, Long planningId);

    /**
     * 完成筹划执行
     */
    boolean completePlanningExecution(Long tenantId, Long planningId, Map<String, Object> completionData);

    /**
     * 取消筹划执行
     */
    boolean cancelPlanningExecution(Long tenantId, Long planningId, String reason);

    /**
     * 更新执行进度
     */
    boolean updateExecutionProgress(Long tenantId, Long planningId, BigDecimal progress);

    /**
     * 记录执行日志
     */
    boolean logExecutionActivity(Long tenantId, Long planningId, String activity, String details);

    // ==================== 监控跟踪功能 ====================

    /**
     * 监控筹划执行
     */
    Map<String, Object> monitorPlanningExecution(Long tenantId, Long planningId);

    /**
     * 跟踪筹划效果
     */
    Map<String, Object> trackPlanningEffectiveness(Long tenantId, Long planningId);

    /**
     * 检查筹划合规性
     */
    Map<String, Object> checkPlanningCompliance(Long tenantId, Long planningId);

    /**
     * 预警风险提醒
     */
    List<Map<String, Object>> getRiskAlerts(Long tenantId, Long planningId);

    /**
     * 生成监控报告
     */
    Map<String, Object> generateMonitoringReport(Long tenantId, Long planningId);

    // ==================== 查询统计功能 ====================

    /**
     * 根据筹划类型查询
     */
    List<TsTaxPlanning> getPlanningsByType(Long tenantId, String planningType);

    /**
     * 根据筹划状态查询
     */
    List<TsTaxPlanning> getPlanningsByStatus(Long tenantId, String planningStatus);

    /**
     * 根据执行状态查询
     */
    List<TsTaxPlanning> getPlanningsByExecutionStatus(Long tenantId, String executionStatus);

    /**
     * 根据风险等级查询
     */
    List<TsTaxPlanning> getPlanningsByRiskLevel(Long tenantId, String riskLevel);

    /**
     * 根据税种查询
     */
    List<TsTaxPlanning> getPlanningsByTaxType(Long tenantId, String taxType);

    /**
     * 根据责任人查询
     */
    List<TsTaxPlanning> getPlanningsByResponsiblePerson(Long tenantId, String responsiblePerson);

    /**
     * 根据时间范围查询
     */
    List<TsTaxPlanning> getPlanningsByTimeRange(Long tenantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询即将到期的筹划
     */
    List<TsTaxPlanning> getExpiringSoonPlannings(Long tenantId, Integer days);

    /**
     * 查询逾期的筹划
     */
    List<TsTaxPlanning> getOverduePlannings(Long tenantId);

    /**
     * 查询高风险筹划
     */
    List<TsTaxPlanning> getHighRiskPlannings(Long tenantId);

    /**
     * 查询高收益筹划
     */
    List<TsTaxPlanning> getHighBenefitPlannings(Long tenantId, BigDecimal minBenefit);

    // ==================== 统计分析功能 ====================

    /**
     * 获取筹划概览统计
     */
    Map<String, Object> getPlanningOverview(Long tenantId);

    /**
     * 按状态统计筹划数量
     */
    List<Map<String, Object>> countPlanningsByStatus(Long tenantId);

    /**
     * 按类型统计筹划数量
     */
    List<Map<String, Object>> countPlanningsByType(Long tenantId);

    /**
     * 按税种统计筹划数量
     */
    List<Map<String, Object>> countPlanningsByTaxType(Long tenantId);

    /**
     * 按风险等级统计筹划数量
     */
    List<Map<String, Object>> countPlanningsByRiskLevel(Long tenantId);

    /**
     * 按执行状态统计筹划数量
     */
    List<Map<String, Object>> countPlanningsByExecutionStatus(Long tenantId);

    /**
     * 获取筹划趋势数据
     */
    List<Map<String, Object>> getPlanningTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy);

    /**
     * 获取节税趋势数据
     */
    List<Map<String, Object>> getTaxSavingTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy);

    /**
     * 获取收益趋势数据
     */
    List<Map<String, Object>> getBenefitTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy);

    /**
     * 获取风险分布数据
     */
    List<Map<String, Object>> getRiskDistribution(Long tenantId);

    /**
     * 获取效果评估数据
     */
    List<Map<String, Object>> getEffectivenessData(Long tenantId);

    /**
     * 获取筹划排行榜
     */
    List<Map<String, Object>> getPlanningRanking(Long tenantId, String rankBy, Integer limit);

    /**
     * 获取筹划效率统计
     */
    Map<String, Object> getPlanningEfficiencyStats(Long tenantId);

    // ==================== 批量操作功能 ====================

    /**
     * 批量创建筹划
     */
    List<TsTaxPlanning> batchCreatePlannings(Long tenantId, List<TsTaxPlanning> plannings);

    /**
     * 批量更新筹划状态
     */
    boolean batchUpdatePlanningStatus(Long tenantId, List<Long> planningIds, String status);

    /**
     * 批量更新执行状态
     */
    boolean batchUpdateExecutionStatus(Long tenantId, List<Long> planningIds, String executionStatus);

    /**
     * 批量删除筹划
     */
    boolean batchDeletePlannings(Long tenantId, List<Long> planningIds);

    /**
     * 批量归档筹划
     */
    boolean batchArchivePlannings(Long tenantId, List<Long> planningIds);

    /**
     * 批量激活筹划
     */
    boolean batchActivatePlannings(Long tenantId, List<Long> planningIds);

    /**
     * 批量导入筹划
     */
    Map<String, Object> batchImportPlannings(Long tenantId, List<Map<String, Object>> planningData);

    /**
     * 批量导出筹划
     */
    List<Map<String, Object>> batchExportPlannings(Long tenantId, List<Long> planningIds);

    // ==================== 工具功能 ====================

    /**
     * 复制筹划
     */
    TsTaxPlanning copyPlanning(Long tenantId, Long sourcePlanningId, String newPlanningName);

    /**
     * 生成筹划报告
     */
    Map<String, Object> generatePlanningReport(Long tenantId, Long planningId);

    /**
     * 发送筹划提醒
     */
    boolean sendPlanningReminder(Long tenantId, Long planningId, String reminderType);

    /**
     * 同步筹划数据
     */
    boolean syncPlanningData(Long tenantId, Long planningId);

    /**
     * 备份筹划数据
     */
    boolean backupPlanningData(Long tenantId, List<Long> planningIds);

    /**
     * 恢复筹划数据
     */
    boolean restorePlanningData(Long tenantId, String backupId);

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

    /**
     * 重建索引
     */
    boolean rebuildIndexes(Long tenantId);

    /**
     * 优化数据库
     */
    boolean optimizeDatabase(Long tenantId);
}
