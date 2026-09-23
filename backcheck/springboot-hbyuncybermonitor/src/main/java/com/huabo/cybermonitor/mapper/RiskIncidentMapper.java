package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.RiskIncident;
import com.huabo.cybermonitor.vo.RiskIncidentQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 风险事件数据访问接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Mapper
public interface RiskIncidentMapper extends BaseMapper<RiskIncident> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据查询条件获取风险事件列表
     */
    List<RiskIncident> selectByCondition(@Param("query") RiskIncidentQueryVO query);

    /**
     * 根据查询条件统计风险事件数量
     */
    Long countByCondition(@Param("query") RiskIncidentQueryVO query);

    /**
     * 根据企业ID获取风险事件列表
     */
    List<RiskIncident> selectByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据事件编号获取风险事件
     */
    RiskIncident selectByIncidentNumber(@Param("incidentNumber") String incidentNumber);

    /**
     * 根据事件类型获取风险事件列表
     */
    List<RiskIncident> selectByIncidentType(@Param("incidentType") String incidentType);

    // ==================== 事件状态查询 ====================

    /**
     * 根据事件状态获取风险事件列表
     */
    List<RiskIncident> selectByIncidentStatus(@Param("incidentStatus") String incidentStatus);

    /**
     * 获取已报告的风险事件列表
     */
    List<RiskIncident> selectReportedIncidents();

    /**
     * 获取调查中的风险事件列表
     */
    List<RiskIncident> selectInvestigatingIncidents();

    /**
     * 获取处理中的风险事件列表
     */
    List<RiskIncident> selectHandlingIncidents();

    /**
     * 获取已解决的风险事件列表
     */
    List<RiskIncident> selectResolvedIncidents();

    /**
     * 获取已关闭的风险事件列表
     */
    List<RiskIncident> selectClosedIncidents();

    // ==================== 风险等级查询 ====================

    /**
     * 根据风险等级获取风险事件列表
     */
    List<RiskIncident> selectByRiskLevel(@Param("riskLevel") String riskLevel);

    /**
     * 获取高风险事件列表
     */
    List<RiskIncident> selectHighRiskIncidents();

    /**
     * 获取临界风险事件列表
     */
    List<RiskIncident> selectCriticalRiskIncidents();

    /**
     * 根据风险评分范围获取风险事件列表
     */
    List<RiskIncident> selectByRiskScoreRange(@Param("minScore") BigDecimal minScore, 
                                              @Param("maxScore") BigDecimal maxScore);

    // ==================== 影响程度查询 ====================

    /**
     * 根据影响程度获取风险事件列表
     */
    List<RiskIncident> selectByImpactLevel(@Param("impactLevel") String impactLevel);

    /**
     * 获取重大影响事件列表
     */
    List<RiskIncident> selectMajorImpactIncidents();

    /**
     * 获取严重影响事件列表
     */
    List<RiskIncident> selectSevereImpactIncidents();

    /**
     * 根据影响人员数量范围获取风险事件列表
     */
    List<RiskIncident> selectByAffectedPersonnelCountRange(@Param("minCount") Integer minCount, 
                                                           @Param("maxCount") Integer maxCount);

    // ==================== 经济损失查询 ====================

    /**
     * 根据总经济损失范围获取风险事件列表
     */
    List<RiskIncident> selectByTotalEconomicLossRange(@Param("minLoss") BigDecimal minLoss, 
                                                      @Param("maxLoss") BigDecimal maxLoss);

    /**
     * 获取有经济损失的风险事件列表
     */
    List<RiskIncident> selectWithEconomicLoss();

    /**
     * 获取重大经济损失事件列表
     */
    List<RiskIncident> selectMajorEconomicLossIncidents(@Param("lossThreshold") BigDecimal lossThreshold);

    /**
     * 获取经济损失统计数据
     */
    List<Map<String, Object>> selectEconomicLossStatistics();

    // ==================== 时间维度查询 ====================

    /**
     * 根据发生时间范围获取风险事件列表
     */
    List<RiskIncident> selectByOccurrenceTimeRange(@Param("startTime") LocalDateTime startTime, 
                                                   @Param("endTime") LocalDateTime endTime);

    /**
     * 根据报告时间范围获取风险事件列表
     */
    List<RiskIncident> selectByReportTimeRange(@Param("startTime") LocalDateTime startTime, 
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 获取最近发生的风险事件列表
     */
    List<RiskIncident> selectRecentIncidents(@Param("days") Integer days);

    /**
     * 获取按月统计的事件数量
     */
    List<Map<String, Object>> selectMonthlyIncidentStatistics(@Param("year") Integer year);

    // ==================== 应急响应查询 ====================

    /**
     * 根据应急响应等级获取风险事件列表
     */
    List<RiskIncident> selectByEmergencyResponseLevel(@Param("emergencyResponseLevel") String emergencyResponseLevel);

    /**
     * 获取触发应急响应的风险事件列表
     */
    List<RiskIncident> selectWithEmergencyResponse();

    /**
     * 根据应急响应效果获取风险事件列表
     */
    List<RiskIncident> selectByEmergencyResponseEffectiveness(@Param("effectiveness") String effectiveness);

    /**
     * 获取应急响应统计数据
     */
    List<Map<String, Object>> selectEmergencyResponseStatistics();

    // ==================== 处理信息查询 ====================

    /**
     * 根据处理状态获取风险事件列表
     */
    List<RiskIncident> selectByHandlingStatus(@Param("handlingStatus") String handlingStatus);

    /**
     * 根据处理负责人获取风险事件列表
     */
    List<RiskIncident> selectByHandlingResponsiblePerson(@Param("handlingResponsiblePerson") String handlingResponsiblePerson);

    /**
     * 获取超期处理的风险事件列表
     */
    List<RiskIncident> selectOverdueHandlingIncidents();

    /**
     * 获取处理效果分析数据
     */
    List<Map<String, Object>> selectHandlingEffectivenessAnalysis();

    // ==================== 重复事件查询 ====================

    /**
     * 获取重复事件列表
     */
    List<RiskIncident> selectRecurringIncidents();

    /**
     * 根据重复次数范围获取风险事件列表
     */
    List<RiskIncident> selectByRecurrenceCountRange(@Param("minCount") Integer minCount, 
                                                    @Param("maxCount") Integer maxCount);

    /**
     * 根据父事件ID获取子事件列表
     */
    List<RiskIncident> selectByParentIncidentId(@Param("parentIncidentId") String parentIncidentId);

    /**
     * 获取重复事件统计数据
     */
    List<Map<String, Object>> selectRecurrenceStatistics();

    // ==================== 监管报告查询 ====================

    /**
     * 获取需要监管报告的风险事件列表
     */
    List<RiskIncident> selectRequiringRegulatoryReport();

    /**
     * 根据监管报告状态获取风险事件列表
     */
    List<RiskIncident> selectByRegulatoryReportStatus(@Param("regulatoryReportStatus") String regulatoryReportStatus);

    /**
     * 根据监管机构获取风险事件列表
     */
    List<RiskIncident> selectByRegulatoryAuthority(@Param("regulatoryAuthority") String regulatoryAuthority);

    /**
     * 获取监管报告统计数据
     */
    List<Map<String, Object>> selectRegulatoryReportStatistics();

    // ==================== 原因分析查询 ====================

    /**
     * 根据根本原因关键字获取风险事件列表
     */
    List<RiskIncident> selectByRootCauseKeyword(@Param("keyword") String keyword);

    /**
     * 获取原因分析统计数据
     */
    List<Map<String, Object>> selectCauseAnalysisStatistics();

    /**
     * 获取人为因素事件列表
     */
    List<RiskIncident> selectHumanFactorIncidents();

    /**
     * 获取系统因素事件列表
     */
    List<RiskIncident> selectSystemFactorIncidents();

    // ==================== 地理位置查询 ====================

    /**
     * 根据发生地点获取风险事件列表
     */
    List<RiskIncident> selectByOccurrenceLocation(@Param("location") String location);

    /**
     * 获取地理分布统计数据
     */
    List<Map<String, Object>> selectGeographicalDistribution();

    /**
     * 根据区域获取风险事件列表
     */
    List<RiskIncident> selectByRegion(@Param("region") String region);

    // ==================== 趋势分析 ====================

    /**
     * 获取事件趋势分析数据
     */
    List<Map<String, Object>> selectIncidentTrendAnalysis(@Param("months") Integer months);

    /**
     * 获取事件类型趋势分析
     */
    List<Map<String, Object>> selectIncidentTypeTrendAnalysis();

    /**
     * 获取风险等级趋势分析
     */
    List<Map<String, Object>> selectRiskLevelTrendAnalysis();

    /**
     * 获取损失趋势分析数据
     */
    List<Map<String, Object>> selectLossTrendAnalysis();

    // ==================== 综合统计分析 ====================

    /**
     * 获取风险事件综合统计数据
     */
    Map<String, Object> selectComprehensiveStatistics();

    /**
     * 获取企业风险事件概览数据
     */
    Map<String, Object> selectEnterpriseIncidentOverview(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取事件类型分布统计
     */
    List<Map<String, Object>> selectIncidentTypeDistribution();

    /**
     * 获取风险等级分布统计
     */
    List<Map<String, Object>> selectRiskLevelDistribution();

    // ==================== 对比分析 ====================

    /**
     * 获取同业事件对比数据
     */
    List<Map<String, Object>> selectPeerIncidentComparison(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("industryType") String industryType);

    /**
     * 获取历史事件对比数据
     */
    List<Map<String, Object>> selectHistoricalIncidentComparison(@Param("enterpriseId") String enterpriseId, 
                                                                 @Param("years") Integer years);

    /**
     * 获取区域事件对比数据
     */
    List<Map<String, Object>> selectRegionalIncidentComparison(@Param("region") String region);

    // ==================== 导出查询 ====================

    /**
     * 获取风险事件导出数据
     */
    List<Map<String, Object>> selectForExport(@Param("query") RiskIncidentQueryVO query);

    /**
     * 获取事件报告数据
     */
    List<Map<String, Object>> selectForIncidentReport(@Param("enterpriseId") String enterpriseId, 
                                                      @Param("startDate") LocalDate startDate, 
                                                      @Param("endDate") LocalDate endDate);

    // ==================== 批量操作 ====================

    /**
     * 批量更新事件状态
     */
    Integer batchUpdateIncidentStatus(@Param("ids") List<String> ids, 
                                     @Param("status") String status, 
                                     @Param("updateBy") String updateBy);

    /**
     * 批量分配处理人员
     */
    Integer batchAssignHandler(@Param("ids") List<String> ids, 
                              @Param("handlingResponsiblePerson") String handlingResponsiblePerson, 
                              @Param("updateBy") String updateBy);

    /**
     * 批量关闭事件
     */
    Integer batchCloseIncidents(@Param("ids") List<String> ids,
                               @Param("updateBy") String updateBy);

    /**
     * 统计当日同类型事件数量
     */
    Integer countTodayIncidentsByType(@Param("enterpriseId") String enterpriseId,
                                      @Param("incidentType") String incidentType);

}
