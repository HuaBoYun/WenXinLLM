package com.management.accountant.mapper.ss;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ss.SsQualityControl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 质量管控 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
@Mapper
public interface SsQualityControlMapper extends BaseMapper<SsQualityControl> {

    /**
     * 分页查询质量管控
     */
    IPage<SsQualityControl> selectQualityControlPage(Page<SsQualityControl> page, @Param("params") Map<String, Object> params);

    /**
     * 根据质量管控编码查询
     */
    SsQualityControl selectByQualityCode(@Param("qualityCode") String qualityCode, @Param("tenantId") Long tenantId);

    /**
     * 根据质量管控名称查询
     */
    List<SsQualityControl> selectByQualityName(@Param("qualityName") String qualityName, @Param("tenantId") Long tenantId);

    /**
     * 根据质量管控类型查询
     */
    List<SsQualityControl> selectByQualityType(@Param("qualityType") String qualityType, @Param("tenantId") Long tenantId);

    /**
     * 根据质量管控状态查询
     */
    List<SsQualityControl> selectByQualityStatus(@Param("qualityStatus") String qualityStatus, @Param("tenantId") Long tenantId);

    /**
     * 根据质量等级查询
     */
    List<SsQualityControl> selectByQualityLevel(@Param("qualityLevel") String qualityLevel, @Param("tenantId") Long tenantId);

    /**
     * 根据检测状态查询
     */
    List<SsQualityControl> selectByDetectionStatus(@Param("detectionStatus") String detectionStatus, @Param("tenantId") Long tenantId);

    /**
     * 根据负责人查询
     */
    List<SsQualityControl> selectByResponsiblePerson(@Param("responsiblePersonId") Long responsiblePersonId, @Param("tenantId") Long tenantId);

    /**
     * 根据检测人员查询
     */
    List<SsQualityControl> selectByInspector(@Param("inspectorId") Long inspectorId, @Param("tenantId") Long tenantId);

    /**
     * 根据时间范围查询
     */
    List<SsQualityControl> selectByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 根据质量评分范围查询
     */
    List<SsQualityControl> selectByScoreRange(@Param("minScore") BigDecimal minScore, @Param("maxScore") BigDecimal maxScore, @Param("tenantId") Long tenantId);

    /**
     * 根据合格率范围查询
     */
    List<SsQualityControl> selectByPassRateRange(@Param("minPassRate") BigDecimal minPassRate, @Param("maxPassRate") BigDecimal maxPassRate, @Param("tenantId") Long tenantId);

    /**
     * 查询待检测的质量管控
     */
    List<SsQualityControl> selectPendingDetection(@Param("tenantId") Long tenantId);

    /**
     * 查询正在检测的质量管控
     */
    List<SsQualityControl> selectInDetection(@Param("tenantId") Long tenantId);

    /**
     * 查询已完成检测的质量管控
     */
    List<SsQualityControl> selectCompletedDetection(@Param("tenantId") Long tenantId);

    /**
     * 查询检测失败的质量管控
     */
    List<SsQualityControl> selectFailedDetection(@Param("tenantId") Long tenantId);

    /**
     * 查询需要改进的质量管控
     */
    List<SsQualityControl> selectNeedImprovement(@Param("tenantId") Long tenantId);

    /**
     * 查询高风险质量管控
     */
    List<SsQualityControl> selectHighRisk(@Param("tenantId") Long tenantId);

    /**
     * 查询超期未检测的质量管控
     */
    List<SsQualityControl> selectOverdueDetection(@Param("tenantId") Long tenantId);

    /**
     * 批量更新质量管控状态
     */
    int batchUpdateStatus(@Param("qualityIds") List<Long> qualityIds, @Param("status") String status, @Param("tenantId") Long tenantId);

    /**
     * 批量更新检测状态
     */
    int batchUpdateDetectionStatus(@Param("qualityIds") List<Long> qualityIds, @Param("detectionStatus") String detectionStatus, @Param("tenantId") Long tenantId);

    /**
     * 批量分配负责人
     */
    int batchAssignResponsiblePerson(@Param("qualityIds") List<Long> qualityIds, @Param("responsiblePersonId") Long responsiblePersonId, @Param("responsiblePersonName") String responsiblePersonName, @Param("tenantId") Long tenantId);

    /**
     * 批量分配检测人员
     */
    int batchAssignInspector(@Param("qualityIds") List<Long> qualityIds, @Param("inspectorId") Long inspectorId, @Param("inspectorName") String inspectorName, @Param("tenantId") Long tenantId);

    /**
     * 批量设置优先级
     */
    int batchSetPriority(@Param("qualityIds") List<Long> qualityIds, @Param("priority") Integer priority, @Param("priorityWeight") BigDecimal priorityWeight, @Param("tenantId") Long tenantId);

    /**
     * 批量启用/禁用
     */
    int batchUpdateEnabled(@Param("qualityIds") List<Long> qualityIds, @Param("isEnabled") Boolean isEnabled, @Param("tenantId") Long tenantId);

    /**
     * 统计质量管控数据
     */
    Map<String, Object> selectQualityControlStatistics(@Param("tenantId") Long tenantId);

    /**
     * 统计质量管控状态分布
     */
    List<Map<String, Object>> selectQualityStatusDistribution(@Param("tenantId") Long tenantId);

    /**
     * 统计质量管控类型分布
     */
    List<Map<String, Object>> selectQualityTypeDistribution(@Param("tenantId") Long tenantId);

    /**
     * 统计质量等级分布
     */
    List<Map<String, Object>> selectQualityLevelDistribution(@Param("tenantId") Long tenantId);

    /**
     * 统计检测状态分布
     */
    List<Map<String, Object>> selectDetectionStatusDistribution(@Param("tenantId") Long tenantId);

    /**
     * 统计风险等级分布
     */
    List<Map<String, Object>> selectRiskLevelDistribution(@Param("tenantId") Long tenantId);

    /**
     * 统计质量管控趋势
     */
    List<Map<String, Object>> selectQualityControlTrend(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("tenantId") Long tenantId);

    /**
     * 统计检测效率
     */
    List<Map<String, Object>> selectDetectionEfficiency(@Param("tenantId") Long tenantId);

    /**
     * 统计质量成本分析
     */
    List<Map<String, Object>> selectQualityCostAnalysis(@Param("tenantId") Long tenantId);

    /**
     * 统计合格率趋势
     */
    List<Map<String, Object>> selectPassRateTrend(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("tenantId") Long tenantId);

    /**
     * 统计缺陷类型分布
     */
    List<Map<String, Object>> selectDefectTypeDistribution(@Param("tenantId") Long tenantId);

    /**
     * 统计改进效果分析
     */
    List<Map<String, Object>> selectImprovementEffectAnalysis(@Param("tenantId") Long tenantId);

    /**
     * 统计负责人工作负载
     */
    List<Map<String, Object>> selectResponsiblePersonWorkload(@Param("tenantId") Long tenantId);

    /**
     * 统计检测人员工作负载
     */
    List<Map<String, Object>> selectInspectorWorkload(@Param("tenantId") Long tenantId);

    /**
     * 统计质量培训效果
     */
    List<Map<String, Object>> selectTrainingEffectAnalysis(@Param("tenantId") Long tenantId);

    /**
     * 查询质量管控排行榜
     */
    List<Map<String, Object>> selectQualityControlRanking(@Param("rankType") String rankType, @Param("tenantId") Long tenantId);

    /**
     * 查询质量管控预警信息
     */
    List<Map<String, Object>> selectQualityControlAlerts(@Param("tenantId") Long tenantId);

    /**
     * 查询质量管控KPI指标
     */
    Map<String, Object> selectQualityControlKPI(@Param("tenantId") Long tenantId);

    /**
     * 查询质量管控性能指标
     */
    Map<String, Object> selectQualityControlPerformance(@Param("tenantId") Long tenantId);

    /**
     * 查询质量管控健康度评估
     */
    Map<String, Object> selectQualityControlHealthAssessment(@Param("tenantId") Long tenantId);

    /**
     * 自定义查询
     */
    List<SsQualityControl> selectByCustomCondition(@Param("condition") Map<String, Object> condition, @Param("tenantId") Long tenantId);

    /**
     * 复杂统计查询
     */
    List<Map<String, Object>> selectComplexStatistics(@Param("params") Map<String, Object> params, @Param("tenantId") Long tenantId);
}
