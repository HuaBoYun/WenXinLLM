package com.management.accountant.mapper.pm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.pm.PmPerformanceCalibration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 绩效校准管理 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
@Mapper
public interface PmPerformanceCalibrationMapper extends BaseMapper<PmPerformanceCalibration> {

    /**
     * 分页查询绩效校准
     */
    IPage<PmPerformanceCalibration> selectCalibrationPage(
            Page<PmPerformanceCalibration> page,
            @Param("calibrationCode") String calibrationCode,
            @Param("calibrationTitle") String calibrationTitle,
            @Param("calibrationType") String calibrationType,
            @Param("calibrationStatus") String calibrationStatus,
            @Param("calibrationYear") Integer calibrationYear,
            @Param("calibrationQuarter") Integer calibrationQuarter,
            @Param("calibrationMonth") Integer calibrationMonth,
            @Param("calibrationScope") String calibrationScope,
            @Param("targetDeptId") Long targetDeptId,
            @Param("calibrationOwnerId") Long calibrationOwnerId,
            @Param("calibrationOwnerName") String calibrationOwnerName,
            @Param("priorityLevel") String priorityLevel,
            @Param("needFollowUp") Integer needFollowUp,
            @Param("followUpStatus") String followUpStatus,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("tenantId") Long tenantId
    );

    /**
     * 根据校准负责人查询校准列表
     */
    List<PmPerformanceCalibration> selectCalibrationsByOwner(
            @Param("calibrationOwnerId") Long calibrationOwnerId,
            @Param("calibrationStatus") String calibrationStatus,
            @Param("limit") Integer limit
    );

    /**
     * 根据部门查询校准列表
     */
    List<PmPerformanceCalibration> selectCalibrationsByDept(
            @Param("targetDeptId") Long targetDeptId,
            @Param("calibrationStatus") String calibrationStatus,
            @Param("limit") Integer limit
    );

    /**
     * 根据校准类型查询校准列表
     */
    List<PmPerformanceCalibration> selectCalibrationsByType(
            @Param("calibrationType") String calibrationType,
            @Param("calibrationYear") Integer calibrationYear,
            @Param("limit") Integer limit
    );

    /**
     * 查询待跟进的校准
     */
    List<PmPerformanceCalibration> selectPendingFollowUpCalibrations(
            @Param("deadline") LocalDateTime deadline,
            @Param("limit") Integer limit
    );

    /**
     * 查询即将到期的校准
     */
    List<PmPerformanceCalibration> selectUpcomingCalibrations(
            @Param("deadline") LocalDateTime deadline,
            @Param("limit") Integer limit
    );

    /**
     * 查询逾期的校准
     */
    List<PmPerformanceCalibration> selectOverdueCalibrations(
            @Param("limit") Integer limit
    );

    /**
     * 统计校准数据
     */
    Map<String, Object> selectCalibrationStatistics(
            @Param("calibrationYear") Integer calibrationYear,
            @Param("calibrationQuarter") Integer calibrationQuarter,
            @Param("calibrationMonth") Integer calibrationMonth,
            @Param("targetDeptId") Long targetDeptId,
            @Param("calibrationType") String calibrationType,
            @Param("tenantId") Long tenantId
    );

    /**
     * 统计校准状态分布
     */
    List<Map<String, Object>> selectCalibrationStatusDistribution(
            @Param("calibrationYear") Integer calibrationYear,
            @Param("targetDeptId") Long targetDeptId
    );

    /**
     * 统计校准类型分布
     */
    List<Map<String, Object>> selectCalibrationTypeDistribution(
            @Param("calibrationYear") Integer calibrationYear,
            @Param("targetDeptId") Long targetDeptId
    );

    /**
     * 统计校准完成趋势
     */
    List<Map<String, Object>> selectCalibrationCompletionTrend(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("targetDeptId") Long targetDeptId
    );

    /**
     * 统计校准效果分布
     */
    List<Map<String, Object>> selectEffectivenessDistribution(
            @Param("calibrationYear") Integer calibrationYear,
            @Param("targetDeptId") Long targetDeptId
    );

    /**
     * 查询校准排行榜
     */
    List<Map<String, Object>> selectCalibrationRanking(
            @Param("calibrationYear") Integer calibrationYear,
            @Param("rankType") String rankType,
            @Param("limit") Integer limit
    );

    /**
     * 检查校准时间冲突
     */
    int checkTimeConflict(
            @Param("calibrationOwnerId") Long calibrationOwnerId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId
    );

    /**
     * 查询可用校准负责人
     */
    List<Map<String, Object>> selectAvailableOwners(
            @Param("targetDeptId") Long targetDeptId,
            @Param("calibrationType") String calibrationType,
            @Param("limit") Integer limit
    );

    /**
     * 查询校准提醒列表
     */
    List<PmPerformanceCalibration> selectCalibrationReminders(
            @Param("reminderTime") LocalDateTime reminderTime,
            @Param("limit") Integer limit
    );

    /**
     * 智能推荐校准时间
     */
    List<Map<String, Object>> recommendCalibrationTimes(
            @Param("calibrationOwnerId") Long calibrationOwnerId,
            @Param("participantIds") String participantIds,
            @Param("duration") Integer duration,
            @Param("preferredDates") String preferredDates
    );

    /**
     * 批量更新校准状态
     */
    int batchUpdateStatus(
            @Param("calibrationIds") List<Long> calibrationIds,
            @Param("calibrationStatus") String calibrationStatus,
            @Param("updatedBy") Long updatedBy,
            @Param("updatedName") String updatedName,
            @Param("updatedTime") LocalDateTime updatedTime
    );

    /**
     * 批量更新跟进状态
     */
    int batchUpdateFollowUpStatus(
            @Param("calibrationIds") List<Long> calibrationIds,
            @Param("followUpStatus") String followUpStatus,
            @Param("updatedBy") Long updatedBy,
            @Param("updatedName") String updatedName,
            @Param("updatedTime") LocalDateTime updatedTime
    );

    /**
     * 批量更新通知状态
     */
    int batchUpdateNotificationStatus(
            @Param("calibrationIds") List<Long> calibrationIds,
            @Param("notificationStatus") String notificationStatus,
            @Param("notificationTime") LocalDateTime notificationTime
    );

    /**
     * 批量更新提醒状态
     */
    int batchUpdateReminderStatus(
            @Param("calibrationIds") List<Long> calibrationIds,
            @Param("isReminded") Integer isReminded,
            @Param("reminderTime") LocalDateTime reminderTime
    );

    /**
     * 根据条件删除校准
     */
    int deleteByCondition(
            @Param("calibrationYear") Integer calibrationYear,
            @Param("calibrationStatus") String calibrationStatus,
            @Param("targetDeptId") Long targetDeptId
    );

    /**
     * 复制校准
     */
    int copyCalibration(
            @Param("sourceId") Long sourceId,
            @Param("targetTitle") String targetTitle,
            @Param("targetYear") Integer targetYear,
            @Param("createdBy") Long createdBy,
            @Param("createdName") String createdName
    );

    /**
     * 查询校准详情（包含关联信息）
     */
    Map<String, Object> selectCalibrationDetail(@Param("calibrationId") Long calibrationId);

    /**
     * 查询校准参与者统计
     */
    Map<String, Object> selectParticipantStatistics(@Param("calibrationId") Long calibrationId);

    /**
     * 查询校准对象统计
     */
    Map<String, Object> selectCalibrationObjectStatistics(@Param("calibrationId") Long calibrationId);

    /**
     * 查询校准历史记录
     */
    List<Map<String, Object>> selectCalibrationHistory(
            @Param("calibrationId") Long calibrationId,
            @Param("limit") Integer limit
    );

    /**
     * 查询相似校准
     */
    List<PmPerformanceCalibration> selectSimilarCalibrations(
            @Param("calibrationType") String calibrationType,
            @Param("calibrationScope") String calibrationScope,
            @Param("targetDeptId") Long targetDeptId,
            @Param("excludeId") Long excludeId,
            @Param("limit") Integer limit
    );

    /**
     * 查询校准模板
     */
    List<Map<String, Object>> selectCalibrationTemplates(
            @Param("calibrationType") String calibrationType,
            @Param("calibrationScope") String calibrationScope,
            @Param("limit") Integer limit
    );

    /**
     * 查询校准最佳实践
     */
    List<Map<String, Object>> selectCalibrationBestPractices(
            @Param("calibrationType") String calibrationType,
            @Param("effectivenessRating") Integer effectivenessRating,
            @Param("limit") Integer limit
    );

    /**
     * 查询校准改进建议
     */
    List<Map<String, Object>> selectImprovementSuggestions(
            @Param("calibrationId") Long calibrationId,
            @Param("calibrationType") String calibrationType
    );

    /**
     * 查询校准质量指标
     */
    Map<String, Object> selectCalibrationQualityMetrics(
            @Param("calibrationId") Long calibrationId
    );

    /**
     * 查询校准成本分析
     */
    Map<String, Object> selectCalibrationCostAnalysis(
            @Param("calibrationId") Long calibrationId
    );

    /**
     * 查询校准ROI分析
     */
    Map<String, Object> selectCalibrationROIAnalysis(
            @Param("calibrationId") Long calibrationId
    );

    /**
     * 查询校准满意度统计
     */
    Map<String, Object> selectCalibrationSatisfactionStats(
            @Param("calibrationId") Long calibrationId
    );

    /**
     * 查询校准影响分析
     */
    Map<String, Object> selectCalibrationImpactAnalysis(
            @Param("calibrationId") Long calibrationId
    );

    /**
     * 查询校准风险评估
     */
    Map<String, Object> selectCalibrationRiskAssessment(
            @Param("calibrationId") Long calibrationId
    );

    /**
     * 查询校准合规性检查
     */
    Map<String, Object> selectCalibrationComplianceCheck(
            @Param("calibrationId") Long calibrationId
    );

    /**
     * 查询校准数据完整性检查
     */
    Map<String, Object> selectCalibrationDataIntegrityCheck(
            @Param("calibrationId") Long calibrationId
    );

    /**
     * 查询校准审计日志
     */
    List<Map<String, Object>> selectCalibrationAuditLog(
            @Param("calibrationId") Long calibrationId,
            @Param("limit") Integer limit
    );

    /**
     * 查询校准性能指标
     */
    Map<String, Object> selectCalibrationPerformanceMetrics(
            @Param("calibrationYear") Integer calibrationYear,
            @Param("targetDeptId") Long targetDeptId
    );

    /**
     * 查询校准基准数据
     */
    Map<String, Object> selectCalibrationBenchmarkData(
            @Param("calibrationType") String calibrationType,
            @Param("calibrationScope") String calibrationScope,
            @Param("calibrationYear") Integer calibrationYear
    );

    /**
     * 查询校准预测分析
     */
    Map<String, Object> selectCalibrationForecastAnalysis(
            @Param("calibrationId") Long calibrationId,
            @Param("forecastPeriod") Integer forecastPeriod
    );
}
