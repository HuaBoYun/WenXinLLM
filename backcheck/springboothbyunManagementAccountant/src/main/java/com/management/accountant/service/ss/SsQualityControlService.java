package com.management.accountant.service.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ss.SsQualityControl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 质量管控服务接口
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
public interface SsQualityControlService extends IService<SsQualityControl> {

    /**
     * 分页查询质量管控
     */
    IPage<SsQualityControl> getQualityControlPage(Map<String, Object> params);

    /**
     * 根据ID查询质量管控
     */
    SsQualityControl getQualityControlById(Long qualityId);

    /**
     * 根据编码查询质量管控
     */
    SsQualityControl getQualityControlByCode(String qualityCode, Long tenantId);

    /**
     * 创建质量管控
     */
    boolean createQualityControl(SsQualityControl qualityControl);

    /**
     * 更新质量管控
     */
    boolean updateQualityControl(Long qualityId, SsQualityControl qualityControl);

    /**
     * 删除质量管控
     */
    boolean deleteQualityControl(Long qualityId);

    /**
     * 批量删除质量管控
     */
    boolean batchDeleteQualityControl(List<Long> qualityIds);

    /**
     * 启用质量管控
     */
    boolean enableQualityControl(Long qualityId);

    /**
     * 禁用质量管控
     */
    boolean disableQualityControl(Long qualityId);

    /**
     * 批量启用质量管控
     */
    boolean batchEnableQualityControl(List<Long> qualityIds);

    /**
     * 批量禁用质量管控
     */
    boolean batchDisableQualityControl(List<Long> qualityIds);

    /**
     * 开始检测
     */
    boolean startDetection(Long qualityId);

    /**
     * 停止检测
     */
    boolean stopDetection(Long qualityId);

    /**
     * 暂停检测
     */
    boolean pauseDetection(Long qualityId);

    /**
     * 恢复检测
     */
    boolean resumeDetection(Long qualityId);

    /**
     * 完成检测
     */
    boolean completeDetection(Long qualityId, String detectionResult, BigDecimal qualityScore);

    /**
     * 批量开始检测
     */
    boolean batchStartDetection(List<Long> qualityIds);

    /**
     * 批量停止检测
     */
    boolean batchStopDetection(List<Long> qualityIds);

    /**
     * 批量暂停检测
     */
    boolean batchPauseDetection(List<Long> qualityIds);

    /**
     * 批量恢复检测
     */
    boolean batchResumeDetection(List<Long> qualityIds);

    /**
     * 立即执行检测
     */
    boolean executeDetectionImmediately(Long qualityId);

    /**
     * 重新检测
     */
    boolean reDetection(Long qualityId);

    /**
     * 配置检测规则
     */
    boolean configureDetectionRule(Long qualityId, String ruleType, Map<String, Object> ruleConfig);

    /**
     * 配置抽检机制
     */
    boolean configureSamplingMechanism(Long qualityId, BigDecimal samplingRatio, Integer samplingQuantity);

    /**
     * 分配负责人
     */
    boolean assignResponsiblePerson(Long qualityId, Long responsiblePersonId, String responsiblePersonName);

    /**
     * 批量分配负责人
     */
    boolean batchAssignResponsiblePerson(List<Long> qualityIds, Long responsiblePersonId, String responsiblePersonName);

    /**
     * 分配检测人员
     */
    boolean assignInspector(Long qualityId, Long inspectorId, String inspectorName);

    /**
     * 批量分配检测人员
     */
    boolean batchAssignInspector(List<Long> qualityIds, Long inspectorId, String inspectorName);

    /**
     * 设置优先级
     */
    boolean setPriority(Long qualityId, Integer priority, BigDecimal priorityWeight);

    /**
     * 批量设置优先级
     */
    boolean batchSetPriority(List<Long> qualityIds, Integer priority, BigDecimal priorityWeight);

    /**
     * 设置质量标准
     */
    boolean setQualityStandard(Long qualityId, Long standardId, String standardName, String standardVersion);

    /**
     * 更新质量评分
     */
    boolean updateQualityScore(Long qualityId, BigDecimal qualityScore, String qualityLevel);

    /**
     * 更新合格率
     */
    boolean updatePassRate(Long qualityId, BigDecimal passRate, Integer failQuantity);

    /**
     * 记录缺陷信息
     */
    boolean recordDefectInfo(Long qualityId, String defectType, String defectLevel, String defectDescription);

    /**
     * 制定改进措施
     */
    boolean makeImprovementMeasures(Long qualityId, String improvementMeasures);

    /**
     * 更新改进状态
     */
    boolean updateImprovementStatus(Long qualityId, String improvementStatus, String improvementEffect);

    /**
     * 计算质量成本
     */
    boolean calculateQualityCost(Long qualityId, BigDecimal preventionCost, BigDecimal appraisalCost, 
                                BigDecimal internalFailureCost, BigDecimal externalFailureCost);

    /**
     * 安排质量培训
     */
    boolean arrangeQualityTraining(Long qualityId, Long trainingId, String trainingName);

    /**
     * 更新培训状态
     */
    boolean updateTrainingStatus(Long qualityId, String trainingStatus, BigDecimal trainingCompletionRate);

    /**
     * 生成质量报告
     */
    String generateQualityReport(Long qualityId);

    /**
     * 批量生成质量报告
     */
    List<String> batchGenerateQualityReport(List<Long> qualityIds);

    /**
     * 导入质量管控数据
     */
    boolean importQualityControlData(List<SsQualityControl> qualityControlList);

    /**
     * 导出质量管控数据
     */
    List<SsQualityControl> exportQualityControlData(Map<String, Object> queryParams);

    /**
     * 发送通知
     */
    boolean sendNotification(Long qualityId, String notificationType, String message);

    /**
     * 批量发送通知
     */
    boolean batchSendNotification(List<Long> qualityIds, String notificationType, String message);

    /**
     * 查询待检测的质量管控
     */
    List<SsQualityControl> getPendingDetection(Long tenantId);

    /**
     * 查询正在检测的质量管控
     */
    List<SsQualityControl> getInDetection(Long tenantId);

    /**
     * 查询已完成检测的质量管控
     */
    List<SsQualityControl> getCompletedDetection(Long tenantId);

    /**
     * 查询检测失败的质量管控
     */
    List<SsQualityControl> getFailedDetection(Long tenantId);

    /**
     * 查询需要改进的质量管控
     */
    List<SsQualityControl> getNeedImprovement(Long tenantId);

    /**
     * 查询高风险质量管控
     */
    List<SsQualityControl> getHighRisk(Long tenantId);

    /**
     * 查询超期未检测的质量管控
     */
    List<SsQualityControl> getOverdueDetection(Long tenantId);

    /**
     * 统计质量管控数据
     */
    Map<String, Object> getQualityControlStatistics(Long tenantId);

    /**
     * 统计质量管控状态分布
     */
    List<Map<String, Object>> getQualityStatusDistribution(Long tenantId);

    /**
     * 统计质量管控类型分布
     */
    List<Map<String, Object>> getQualityTypeDistribution(Long tenantId);

    /**
     * 统计质量等级分布
     */
    List<Map<String, Object>> getQualityLevelDistribution(Long tenantId);

    /**
     * 统计检测状态分布
     */
    List<Map<String, Object>> getDetectionStatusDistribution(Long tenantId);

    /**
     * 统计风险等级分布
     */
    List<Map<String, Object>> getRiskLevelDistribution(Long tenantId);

    /**
     * 统计质量管控趋势
     */
    List<Map<String, Object>> getQualityControlTrend(Map<String, Object> params);

    /**
     * 统计检测效率
     */
    List<Map<String, Object>> getDetectionEfficiency(Long tenantId);

    /**
     * 统计质量成本分析
     */
    List<Map<String, Object>> getQualityCostAnalysis(Long tenantId);

    /**
     * 统计合格率趋势
     */
    List<Map<String, Object>> getPassRateTrend(Map<String, Object> params);

    /**
     * 统计缺陷类型分布
     */
    List<Map<String, Object>> getDefectTypeDistribution(Long tenantId);

    /**
     * 统计改进效果分析
     */
    List<Map<String, Object>> getImprovementEffectAnalysis(Long tenantId);

    /**
     * 统计负责人工作负载
     */
    List<Map<String, Object>> getResponsiblePersonWorkload(Long tenantId);

    /**
     * 统计检测人员工作负载
     */
    List<Map<String, Object>> getInspectorWorkload(Long tenantId);

    /**
     * 统计质量培训效果
     */
    List<Map<String, Object>> getTrainingEffectAnalysis(Long tenantId);

    /**
     * 查询质量管控排行榜
     */
    List<Map<String, Object>> getQualityControlRanking(String rankType, Long tenantId);

    /**
     * 查询质量管控预警信息
     */
    List<Map<String, Object>> getQualityControlAlerts(Long tenantId);

    /**
     * 查询质量管控KPI指标
     */
    Map<String, Object> getQualityControlKPI(Long tenantId);

    /**
     * 查询质量管控性能指标
     */
    Map<String, Object> getQualityControlPerformance(Long tenantId);

    /**
     * 查询质量管控健康度评估
     */
    Map<String, Object> getQualityControlHealthAssessment(Long tenantId);
}
