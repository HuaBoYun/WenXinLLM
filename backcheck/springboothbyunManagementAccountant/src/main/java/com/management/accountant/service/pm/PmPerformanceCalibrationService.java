package com.management.accountant.service.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.pm.PmPerformanceCalibration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 绩效校准管理服务接口
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
public interface PmPerformanceCalibrationService extends IService<PmPerformanceCalibration> {

    /**
     * 分页查询绩效校准
     */
    IPage<PmPerformanceCalibration> queryCalibrationPage(
            Integer current, Integer size,
            String calibrationCode, String calibrationTitle, String calibrationType,
            String calibrationStatus, Integer calibrationYear, Integer calibrationQuarter,
            Integer calibrationMonth, String calibrationScope, Long targetDeptId,
            Long calibrationOwnerId, String calibrationOwnerName, String priorityLevel,
            Integer needFollowUp, String followUpStatus,
            LocalDateTime startTime, LocalDateTime endTime
    );

    /**
     * 根据ID查询校准详情
     */
    PmPerformanceCalibration getCalibrationById(Long calibrationId);

    /**
     * 查询校准详情（包含关联信息）
     */
    Map<String, Object> getCalibrationDetail(Long calibrationId);

    /**
     * 创建绩效校准
     */
    boolean createCalibration(PmPerformanceCalibration calibration);

    /**
     * 更新绩效校准
     */
    boolean updateCalibration(PmPerformanceCalibration calibration);

    /**
     * 删除绩效校准
     */
    boolean deleteCalibration(Long calibrationId);

    /**
     * 批量删除绩效校准
     */
    boolean batchDeleteCalibrations(List<Long> calibrationIds);

    /**
     * 开始校准
     */
    boolean startCalibration(Long calibrationId, Map<String, Object> startData);

    /**
     * 完成校准
     */
    boolean completeCalibration(Long calibrationId, Map<String, Object> completeData);

    /**
     * 取消校准
     */
    boolean cancelCalibration(Long calibrationId, Map<String, Object> cancelData);

    /**
     * 批量更新校准状态
     */
    boolean batchUpdateStatus(List<Long> calibrationIds, String status, Map<String, Object> updateData);

    /**
     * 保存校准记录
     */
    boolean saveCalibrationRecord(Long calibrationId, Map<String, Object> recordData);

    /**
     * 创建跟进计划
     */
    boolean createFollowUpPlan(Long calibrationId, Map<String, Object> followUpData);

    /**
     * 更新跟进状态
     */
    boolean updateFollowUpStatus(Long calibrationId, String followUpStatus, Map<String, Object> updateData);

    /**
     * 批量更新跟进状态
     */
    boolean batchUpdateFollowUpStatus(List<Long> calibrationIds, String followUpStatus, Map<String, Object> updateData);

    /**
     * 根据校准负责人查询校准列表
     */
    List<PmPerformanceCalibration> getCalibrationsByOwner(Long calibrationOwnerId, String status, Integer limit);

    /**
     * 根据部门查询校准列表
     */
    List<PmPerformanceCalibration> getCalibrationsByDept(Long targetDeptId, String status, Integer limit);

    /**
     * 根据校准类型查询校准列表
     */
    List<PmPerformanceCalibration> getCalibrationsByType(String calibrationType, Integer calibrationYear, Integer limit);

    /**
     * 查询待跟进的校准
     */
    List<PmPerformanceCalibration> getPendingFollowUpCalibrations(LocalDateTime deadline, Integer limit);

    /**
     * 查询即将到期的校准
     */
    List<PmPerformanceCalibration> getUpcomingCalibrations(LocalDateTime deadline, Integer limit);

    /**
     * 查询逾期的校准
     */
    List<PmPerformanceCalibration> getOverdueCalibrations(Integer limit);

    /**
     * 统计校准数据
     */
    Map<String, Object> getCalibrationStatistics(
            Integer calibrationYear, Integer calibrationQuarter, Integer calibrationMonth,
            Long targetDeptId, String calibrationType
    );

    /**
     * 统计校准状态分布
     */
    List<Map<String, Object>> getCalibrationStatusDistribution(Integer calibrationYear, Long targetDeptId);

    /**
     * 统计校准类型分布
     */
    List<Map<String, Object>> getCalibrationTypeDistribution(Integer calibrationYear, Long targetDeptId);

    /**
     * 统计校准完成趋势
     */
    List<Map<String, Object>> getCalibrationCompletionTrend(LocalDateTime startTime, LocalDateTime endTime, Long targetDeptId);

    /**
     * 统计校准效果分布
     */
    List<Map<String, Object>> getEffectivenessDistribution(Integer calibrationYear, Long targetDeptId);

    /**
     * 查询校准排行榜
     */
    List<Map<String, Object>> getCalibrationRanking(Integer calibrationYear, String rankType, Integer limit);

    /**
     * 检查校准时间冲突
     */
    boolean checkTimeConflict(Long calibrationOwnerId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId);

    /**
     * 查询可用校准负责人
     */
    List<Map<String, Object>> getAvailableOwners(Long targetDeptId, String calibrationType, Integer limit);

    /**
     * 查询校准提醒列表
     */
    List<PmPerformanceCalibration> getCalibrationReminders(LocalDateTime reminderTime, Integer limit);

    /**
     * 智能推荐校准时间
     */
    List<Map<String, Object>> recommendCalibrationTimes(
            Long calibrationOwnerId, List<Long> participantIds, Integer duration, List<String> preferredDates
    );

    /**
     * 生成校准报告
     */
    Map<String, Object> generateCalibrationReport(Long calibrationId, Map<String, Object> reportParams);

    /**
     * 导出校准数据
     */
    Map<String, Object> exportCalibrationData(Map<String, Object> exportParams);

    /**
     * 导入校准数据
     */
    Map<String, Object> importCalibrationData(List<Map<String, Object>> importData, Map<String, Object> importParams);

    /**
     * 复制校准
     */
    boolean copyCalibration(Long calibrationId, Map<String, Object> copyParams);

    /**
     * 批量创建校准
     */
    Map<String, Object> batchCreateCalibrations(List<PmPerformanceCalibration> calibrations, Map<String, Object> batchParams);

    /**
     * 发送校准通知
     */
    Map<String, Object> sendCalibrationNotification(Long calibrationId, Map<String, Object> notificationData);

    /**
     * 批量发送校准通知
     */
    Map<String, Object> batchSendNotifications(List<Long> calibrationIds, Map<String, Object> notificationData);

    /**
     * 查询校准参与者统计
     */
    Map<String, Object> getParticipantStatistics(Long calibrationId);

    /**
     * 查询校准对象统计
     */
    Map<String, Object> getCalibrationObjectStatistics(Long calibrationId);

    /**
     * 查询校准历史记录
     */
    List<Map<String, Object>> getCalibrationHistory(Long calibrationId, Integer limit);

    /**
     * 查询相似校准
     */
    List<PmPerformanceCalibration> getSimilarCalibrations(
            String calibrationType, String calibrationScope, Long targetDeptId, Long excludeId, Integer limit
    );

    /**
     * 查询校准模板
     */
    List<Map<String, Object>> getCalibrationTemplates(String calibrationType, String calibrationScope, Integer limit);

    /**
     * 查询校准最佳实践
     */
    List<Map<String, Object>> getCalibrationBestPractices(String calibrationType, Integer effectivenessRating, Integer limit);

    /**
     * 查询校准改进建议
     */
    List<Map<String, Object>> getImprovementSuggestions(Long calibrationId, String calibrationType);

    /**
     * 查询校准质量指标
     */
    Map<String, Object> getCalibrationQualityMetrics(Long calibrationId);

    /**
     * 查询校准成本分析
     */
    Map<String, Object> getCalibrationCostAnalysis(Long calibrationId);

    /**
     * 查询校准ROI分析
     */
    Map<String, Object> getCalibrationROIAnalysis(Long calibrationId);

    /**
     * 查询校准满意度统计
     */
    Map<String, Object> getCalibrationSatisfactionStats(Long calibrationId);

    /**
     * 查询校准影响分析
     */
    Map<String, Object> getCalibrationImpactAnalysis(Long calibrationId);

    /**
     * 查询校准风险评估
     */
    Map<String, Object> getCalibrationRiskAssessment(Long calibrationId);

    /**
     * 查询校准合规性检查
     */
    Map<String, Object> getCalibrationComplianceCheck(Long calibrationId);

    /**
     * 查询校准数据完整性检查
     */
    Map<String, Object> getCalibrationDataIntegrityCheck(Long calibrationId);

    /**
     * 查询校准审计日志
     */
    List<Map<String, Object>> getCalibrationAuditLog(Long calibrationId, Integer limit);

    /**
     * 查询校准性能指标
     */
    Map<String, Object> getCalibrationPerformanceMetrics(Integer calibrationYear, Long targetDeptId);

    /**
     * 查询校准基准数据
     */
    Map<String, Object> getCalibrationBenchmarkData(String calibrationType, String calibrationScope, Integer calibrationYear);

    /**
     * 查询校准预测分析
     */
    Map<String, Object> getCalibrationForecastAnalysis(Long calibrationId, Integer forecastPeriod);

    /**
     * 校准数据验证
     */
    Map<String, Object> validateCalibrationData(PmPerformanceCalibration calibration);

    /**
     * 校准流程控制
     */
    Map<String, Object> controlCalibrationProcess(Long calibrationId, String action, Map<String, Object> controlData);

    /**
     * 校准智能分析
     */
    Map<String, Object> intelligentCalibrationAnalysis(Long calibrationId, Map<String, Object> analysisParams);

    /**
     * 校准优化建议
     */
    Map<String, Object> getCalibrationOptimizationSuggestions(Long calibrationId);

    /**
     * 校准效果评估
     */
    Map<String, Object> evaluateCalibrationEffectiveness(Long calibrationId, Map<String, Object> evaluationParams);
}
