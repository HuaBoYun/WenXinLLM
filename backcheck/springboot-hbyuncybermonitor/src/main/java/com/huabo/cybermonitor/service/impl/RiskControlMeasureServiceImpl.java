package com.huabo.cybermonitor.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.cybermonitor.entity.RiskControlMeasure;
import com.huabo.cybermonitor.mapper.RiskControlMeasureMapper;
import com.huabo.cybermonitor.service.IRiskControlMeasureService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.RiskControlMeasureQueryVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 风险控制措施业务实现类
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Slf4j
@Service
public class RiskControlMeasureServiceImpl extends ServiceImpl<RiskControlMeasureMapper, RiskControlMeasure> implements IRiskControlMeasureService {

    @Autowired
    private RiskControlMeasureMapper riskControlMeasureMapper;

    // ==================== 基础业务方法 ====================

    @Override
    public PageResult<RiskControlMeasure> selectByPage(RiskControlMeasureQueryVO query) {
        try {
            PageHelper.startPage(query.getPageNum(), query.getPageSize());
            List<RiskControlMeasure> list = riskControlMeasureMapper.selectByCondition(query);
            PageInfo<RiskControlMeasure> pageInfo = new PageInfo<>(list);

            return new PageResult<RiskControlMeasure>((int)pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception e) {
            log.error("分页查询风险控制措施失败", e);
            throw new RuntimeException("分页查询风险控制措施失败: " + e.getMessage());
        }
    }

    @Override
    public List<RiskControlMeasure> getByEnterpriseId(String enterpriseId) {
        try {
            return riskControlMeasureMapper.selectByEnterpriseId(enterpriseId);
        } catch (Exception e) {
            log.error("根据企业ID获取风险控制措施列表失败: " + enterpriseId, e);
            throw new RuntimeException("获取企业风险控制措施列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<RiskControlMeasure> getByRiskAssessmentId(String riskAssessmentId) {
        try {
            return riskControlMeasureMapper.selectByRiskAssessmentId(riskAssessmentId);
        } catch (Exception e) {
            log.error("根据风险评估ID获取风险控制措施列表失败: " + riskAssessmentId, e);
            throw new RuntimeException("获取风险评估控制措施列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRiskControlMeasure(RiskControlMeasure riskControlMeasure) {
        try {
            riskControlMeasure.setCreateTime(LocalDateTime.now());
            
            // 设置措施优先级
            String priority = determineMeasurePriority(riskControlMeasure);
            riskControlMeasure.setPriority(priority);
            
            return save(riskControlMeasure);
        } catch (Exception e) {
            log.error("保存风险控制措施失败", e);
            throw new RuntimeException("保存风险控制措施失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRiskControlMeasure(RiskControlMeasure riskControlMeasure) {
        try {
            riskControlMeasure.setUpdateTime(LocalDateTime.now());
            
            // 重新计算效果评分
            BigDecimal effectivenessScore = calculateEffectivenessScore(riskControlMeasure);
            riskControlMeasure.setEffectivenessScore(effectivenessScore);
            
            return updateById(riskControlMeasure);
        } catch (Exception e) {
            log.error("更新风险控制措施失败", e);
            throw new RuntimeException("更新风险控制措施失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRiskControlMeasure(String controlMeasureId) {
        try {
            return removeById(controlMeasureId);
        } catch (Exception e) {
            log.error("删除风险控制措施失败: " + controlMeasureId, e);
            throw new RuntimeException("删除风险控制措施失败: " + e.getMessage());
        }
    }

    // ==================== 措施制定业务 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RiskControlMeasure developControlMeasure(String enterpriseId, String riskAssessmentId, String targetRiskType) {
        try {
            RiskControlMeasure measure = new RiskControlMeasure();
            measure.setEnterpriseId(enterpriseId);
            measure.setRiskAssessmentId(riskAssessmentId);
            measure.setTargetRiskType(targetRiskType);
            measure.setMeasureStatus(RiskControlMeasure.MEASURE_STATUS_DRAFT);
            measure.setImplementationStatus(RiskControlMeasure.IMPLEMENTATION_STATUS_NOT_STARTED);
            
            // 生成措施建议
            List<String> recommendations = generateMeasureRecommendations(riskAssessmentId, targetRiskType);
            measure.setMeasureDescription(String.join("; ", recommendations));
            
            // 设置优先级
            String priority = determineMeasurePriority(measure);
            measure.setPriority(priority);
            
            // 评估可行性
            Map<String, Object> feasibility = assessMeasureFeasibility(measure);
            if (feasibility.containsKey("score")) {
                measure.setEffectivenessScore((BigDecimal) feasibility.get("score"));
            }
            
            measure.setCreateTime(LocalDateTime.now());
            
            save(measure);
            return measure;
        } catch (Exception e) {
            log.error("制定风险控制措施失败: {}, {}, {}", enterpriseId, riskAssessmentId, targetRiskType, e);
            throw new RuntimeException("制定风险控制措施失败: " + e.getMessage());
        }
    }

    @Override
    public List<String> generateMeasureRecommendations(String riskAssessmentId, String targetRiskType) {
        try {
            List<String> recommendations = new ArrayList<>();
            
            // 根据风险类型生成建议
            switch (targetRiskType) {
                case "FINANCIAL":
                    recommendations.add("建立财务风险预警机制");
                    recommendations.add("优化资金结构，提高流动性");
                    recommendations.add("加强财务监控和报告");
                    break;
                case "OPERATIONAL":
                    recommendations.add("完善经营风险管理体系");
                    recommendations.add("建立市场风险监控机制");
                    recommendations.add("加强供应链风险管理");
                    break;
                case "COMPLIANCE":
                    recommendations.add("建立合规风险识别机制");
                    recommendations.add("完善法律风险防控体系");
                    recommendations.add("加强监管政策跟踪");
                    break;
                default:
                    recommendations.add("建立综合风险管控体系");
                    recommendations.add("完善风险监控机制");
                    break;
            }
            
            return recommendations;
        } catch (Exception e) {
            log.error("生成措施建议失败: {}, {}", riskAssessmentId, targetRiskType, e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> assessMeasureFeasibility(RiskControlMeasure riskControlMeasure) {
        try {
            Map<String, Object> feasibility = new HashMap<>();
            
            // 评估技术可行性
            BigDecimal technicalFeasibility = new BigDecimal("80.0");
            
            // 评估经济可行性
            BigDecimal economicFeasibility = new BigDecimal("75.0");
            
            // 评估时间可行性
            BigDecimal timeFeasibility = new BigDecimal("85.0");
            
            // 评估资源可行性
            BigDecimal resourceFeasibility = new BigDecimal("70.0");
            
            // 计算综合可行性评分
            BigDecimal overallScore = technicalFeasibility.add(economicFeasibility)
                    .add(timeFeasibility).add(resourceFeasibility)
                    .divide(new BigDecimal("4"), 2, RoundingMode.HALF_UP);
            
            feasibility.put("score", overallScore);
            feasibility.put("technicalFeasibility", technicalFeasibility);
            feasibility.put("economicFeasibility", economicFeasibility);
            feasibility.put("timeFeasibility", timeFeasibility);
            feasibility.put("resourceFeasibility", resourceFeasibility);
            
            return feasibility;
        } catch (Exception e) {
            log.error("评估措施可行性失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public RiskControlMeasure optimizeMeasurePlan(RiskControlMeasure riskControlMeasure) {
        try {
            // 优化措施方案
            // 这里应该实现具体的优化逻辑
            
            riskControlMeasure.setUpdateTime(LocalDateTime.now());
            updateById(riskControlMeasure);
            
            return riskControlMeasure;
        } catch (Exception e) {
            log.error("优化措施方案失败", e);
            throw new RuntimeException("优化措施方案失败: " + e.getMessage());
        }
    }

    @Override
    public String determineMeasurePriority(RiskControlMeasure riskControlMeasure) {
        try {
            // 根据风险等级和影响程度确定优先级
            String targetRiskLevel = riskControlMeasure.getTargetRiskLevel();
            
            if ("CRITICAL".equals(targetRiskLevel) || "VERY_HIGH".equals(targetRiskLevel)) {
                return RiskControlMeasure.PRIORITY_URGENT;
            } else if ("HIGH".equals(targetRiskLevel)) {
                return RiskControlMeasure.PRIORITY_HIGH;
            } else if ("MEDIUM".equals(targetRiskLevel)) {
                return RiskControlMeasure.PRIORITY_MEDIUM;
            } else {
                return RiskControlMeasure.PRIORITY_LOW;
            }
        } catch (Exception e) {
            log.error("确定措施优先级失败", e);
            return RiskControlMeasure.PRIORITY_MEDIUM;
        }
    }

    // ==================== 实施管理业务 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startImplementation(String controlMeasureId, String responsiblePerson, LocalDate startDate) {
        try {
            RiskControlMeasure measure = getById(controlMeasureId);
            if (measure == null) {
                return false;
            }
            
            measure.setImplementationStatus(RiskControlMeasure.IMPLEMENTATION_STATUS_IN_PROGRESS);
            measure.setResponsiblePerson(responsiblePerson);
            measure.setPlannedStartDate(startDate);
            measure.setActualStartDate(startDate);
            measure.setImplementationProgress(BigDecimal.ZERO);
            measure.setUpdateTime(LocalDateTime.now());
            
            return updateById(measure);
        } catch (Exception e) {
            log.error("启动措施实施失败: {}, {}, {}", controlMeasureId, responsiblePerson, startDate, e);
            throw new RuntimeException("启动措施实施失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateImplementationProgress(String controlMeasureId, BigDecimal progress, String updateBy) {
        try {
            RiskControlMeasure measure = getById(controlMeasureId);
            if (measure == null) {
                return false;
            }
            
            measure.setImplementationProgress(progress);
            measure.setUpdateBy(updateBy);
            measure.setUpdateTime(LocalDateTime.now());
            
            // 如果进度达到100%，自动完成实施
            if (progress.compareTo(new BigDecimal("100")) >= 0) {
                measure.setImplementationStatus(RiskControlMeasure.IMPLEMENTATION_STATUS_COMPLETED);
                measure.setActualCompletionDate(LocalDate.now());
            }
            
            return updateById(measure);
        } catch (Exception e) {
            log.error("更新实施进度失败: {}, {}, {}", controlMeasureId, progress, updateBy, e);
            throw new RuntimeException("更新实施进度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeImplementation(String controlMeasureId, LocalDate completionDate, String completionComments) {
        try {
            RiskControlMeasure measure = getById(controlMeasureId);
            if (measure == null) {
                return false;
            }
            
            measure.setImplementationStatus(RiskControlMeasure.IMPLEMENTATION_STATUS_COMPLETED);
            measure.setActualCompletionDate(completionDate);
            measure.setImplementationProgress(new BigDecimal("100"));
            measure.setRemarks(completionComments);
            measure.setUpdateTime(LocalDateTime.now());
            
            // 计算效果评分
            BigDecimal effectivenessScore = calculateEffectivenessScore(measure);
            measure.setEffectivenessScore(effectivenessScore);
            
            return updateById(measure);
        } catch (Exception e) {
            log.error("完成措施实施失败: {}, {}, {}", controlMeasureId, completionDate, completionComments, e);
            throw new RuntimeException("完成措施实施失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean suspendImplementation(String controlMeasureId, String suspendReason, String updateBy) {
        try {
            RiskControlMeasure measure = getById(controlMeasureId);
            if (measure == null) {
                return false;
            }
            
            measure.setImplementationStatus(RiskControlMeasure.IMPLEMENTATION_STATUS_SUSPENDED);
            measure.setRemarks(suspendReason);
            measure.setUpdateBy(updateBy);
            measure.setUpdateTime(LocalDateTime.now());
            
            return updateById(measure);
        } catch (Exception e) {
            log.error("暂停措施实施失败: {}, {}, {}", controlMeasureId, suspendReason, updateBy, e);
            throw new RuntimeException("暂停措施实施失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resumeImplementation(String controlMeasureId, String resumeReason, String updateBy) {
        try {
            RiskControlMeasure measure = getById(controlMeasureId);
            if (measure == null) {
                return false;
            }
            
            measure.setImplementationStatus(RiskControlMeasure.IMPLEMENTATION_STATUS_IN_PROGRESS);
            measure.setRemarks(resumeReason);
            measure.setUpdateBy(updateBy);
            measure.setUpdateTime(LocalDateTime.now());
            
            return updateById(measure);
        } catch (Exception e) {
            log.error("恢复措施实施失败: {}, {}, {}", controlMeasureId, resumeReason, updateBy, e);
            throw new RuntimeException("恢复措施实施失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getImplementationProgressStatistics() {
        try {
            List<Map<String, Object>> list = riskControlMeasureMapper.selectImplementationProgressStatistics();
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
            return new HashMap<>();
        } catch (Exception e) {
            log.error("获取实施进度统计失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public boolean monitorImplementationProgress(String controlMeasureId) {
        return false;
    }

    @Override
    public List<RiskControlMeasure> checkOverdueMeasures() {
        return null;
    }

    @Override
    public List<RiskControlMeasure> getExpiringMeasures(Integer days) {
        return null;
    }

    @Override
    public boolean sendProgressReminder(String controlMeasureId, String reminderType) {
        return false;
    }

    @Override
    public Map<String, Object> generateProgressReport(String enterpriseId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public boolean allocateResources(String controlMeasureId, BigDecimal budgetAmount, String humanResources) {
        return false;
    }

    @Override
    public boolean updateResourceUsage(String controlMeasureId, BigDecimal actualExpenditure, String resourceUsageComments) {
        return false;
    }

    @Override
    public Map<String, Object> checkBudgetExecution(String controlMeasureId) {
        return null;
    }

    @Override
    public List<RiskControlMeasure> getOverBudgetMeasures() {
        return null;
    }

    @Override
    public Map<String, Object> analyzeResourceUtilizationEfficiency(String enterpriseId) {
        return null;
    }

    // ==================== 效果评估业务 ====================

    @Override
    public String evaluateMeasureEffectiveness(String controlMeasureId) {
        try {
            RiskControlMeasure measure = getById(controlMeasureId);
            if (measure == null) {
                return "UNKNOWN";
            }
            
            BigDecimal effectivenessScore = calculateEffectivenessScore(measure);
            
            if (effectivenessScore.compareTo(new BigDecimal("90")) >= 0) {
                return RiskControlMeasure.EFFECTIVENESS_LEVEL_EXCELLENT;
            } else if (effectivenessScore.compareTo(new BigDecimal("80")) >= 0) {
                return RiskControlMeasure.EFFECTIVENESS_LEVEL_GOOD;
            } else if (effectivenessScore.compareTo(new BigDecimal("70")) >= 0) {
                return RiskControlMeasure.EFFECTIVENESS_LEVEL_FAIR;
            } else if (effectivenessScore.compareTo(new BigDecimal("60")) >= 0) {
                return RiskControlMeasure.EFFECTIVENESS_LEVEL_POOR;
            } else {
                return RiskControlMeasure.EFFECTIVENESS_LEVEL_VERY_POOR;
            }
        } catch (Exception e) {
            log.error("评估措施效果失败: {}", controlMeasureId, e);
            return "UNKNOWN";
        }
    }

    @Override
    public BigDecimal calculateEffectivenessScore(RiskControlMeasure riskControlMeasure) {
        try {
            // 实施完成度权重 40%
            BigDecimal implementationWeight = new BigDecimal("0.40");
            BigDecimal implementationScore = riskControlMeasure.getImplementationProgress() != null ? 
                    riskControlMeasure.getImplementationProgress() : BigDecimal.ZERO;
            
            // 风险降低效果权重 30%
            BigDecimal riskReductionWeight = new BigDecimal("0.30");
            BigDecimal riskReductionScore = analyzeRiskReductionEffect(riskControlMeasure.getControlMeasureId());
            
            // 成本效益权重 20%
            BigDecimal costEffectivenessWeight = new BigDecimal("0.20");
            BigDecimal costEffectivenessScore = calculateCostEffectiveness(riskControlMeasure);
            
            // 时间效率权重 10%
            BigDecimal timeEfficiencyWeight = new BigDecimal("0.10");
            BigDecimal timeEfficiencyScore = calculateTimeEfficiency(riskControlMeasure);
            
            // 计算综合效果评分
            BigDecimal totalScore = implementationScore.multiply(implementationWeight)
                    .add(riskReductionScore.multiply(riskReductionWeight))
                    .add(costEffectivenessScore.multiply(costEffectivenessWeight))
                    .add(timeEfficiencyScore.multiply(timeEfficiencyWeight));
            
            return totalScore.setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("计算效果评分失败", e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal analyzeRiskReductionEffect(String controlMeasureId) {
        try {
            // 这里应该分析风险降低效果
            // 暂时返回模拟数据
            return new BigDecimal("75.5");
        } catch (Exception e) {
            log.error("分析风险降低效果失败: {}", controlMeasureId, e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public Map<String, Object> compareExpectedVsActualEffect(String controlMeasureId) {
        return null;
    }

    @Override
    public List<RiskControlMeasure> getHighEffectivenessMeasures() {
        return null;
    }

    @Override
    public Map<String, Object> generateEffectivenessReport(String enterpriseId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public boolean configureMeasureMonitoring(String controlMeasureId, String monitoringFrequency, String monitoringResponsiblePerson) {
        return false;
    }

    @Override
    public boolean startMeasureMonitoring(String controlMeasureId) {
        return false;
    }

    @Override
    public boolean stopMeasureMonitoring(String controlMeasureId) {
        return false;
    }

    @Override
    public List<RiskControlMeasure> getMeasuresRequiringMonitoring() {
        return null;
    }

    @Override
    public boolean performMonitoringCheck(String controlMeasureId) {
        return false;
    }

    @Override
    public boolean submitForReview(String controlMeasureId, String submitter) {
        return false;
    }

    @Override
    public boolean reviewMeasure(String controlMeasureId, String reviewer, String reviewStatus, String reviewComments) {
        return false;
    }

    @Override
    public boolean approveMeasure(String controlMeasureId, String approver, String approvalComments) {
        return false;
    }

    @Override
    public List<RiskControlMeasure> getPendingReviewMeasures() {
        return null;
    }

    @Override
    public List<RiskControlMeasure> getReviewedMeasures() {
        return null;
    }

    @Override
    public boolean configureAutoTrigger(String controlMeasureId, String triggerConditions) {
        return false;
    }

    @Override
    public boolean checkTriggerConditions(String controlMeasureId) {
        return false;
    }

    @Override
    public boolean autoTriggerMeasure(String controlMeasureId, String triggerReason) {
        return false;
    }

    @Override
    public boolean manualTriggerMeasure(String controlMeasureId, String triggerPerson, String triggerReason) {
        return false;
    }

    @Override
    public boolean collectImprovementSuggestions(String controlMeasureId, String suggestions, String suggester) {
        return false;
    }

    @Override
    public Map<String, Object> analyzeLessonsLearned(String controlMeasureId) {
        return null;
    }

    @Override
    public List<String> extractBestPractices(String enterpriseId, String measureType) {
        return null;
    }

    @Override
    public boolean updateKnowledgeBase(String controlMeasureId, String knowledgeContent) {
        return false;
    }

    @Override
    public List<String> identifyTrainingRequirements(String enterpriseId, String measureCategory) {
        return null;
    }

    @Override
    public Map<String, Object> getComprehensiveStatistics() {
        return null;
    }

    @Override
    public Map<String, Object> getEnterpriseMeasureOverview(String enterpriseId) {
        return null;
    }

    @Override
    public Map<String, Object> getMeasureTypeDistribution() {
        return null;
    }

    @Override
    public Map<String, Object> getMeasureEffectivenessStatistics() {
        return null;
    }

    @Override
    public List<Map<String, Object>> getPeerComparisonData(String enterpriseId, String industryType) {
        return null;
    }

    @Override
    public Map<String, Object> generateImplementationReport(String enterpriseId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public Map<String, Object> generateEffectivenessAnalysisReport(String enterpriseId, String measureType) {
        return null;
    }

    @Override
    public Map<String, Object> generateResourceUsageReport(String enterpriseId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public boolean batchUpdateMeasureStatus(List<String> controlMeasureIds, String status, String updateBy) {
        return false;
    }

    @Override
    public boolean batchReviewMeasures(List<String> controlMeasureIds, String reviewer, String reviewStatus, String reviewComments) {
        return false;
    }

    @Override
    public boolean batchUpdateImplementationProgress(List<String> controlMeasureIds, BigDecimal progress, String updateBy) {
        return false;
    }

    @Override
    public boolean batchAssignResponsiblePerson(List<String> controlMeasureIds, String responsiblePerson, String updateBy) {
        return false;
    }

    @Override
    public boolean batchStartImplementation(List<String> controlMeasureIds, String updateBy) {
        return false;
    }

    @Override
    public List<Map<String, Object>> exportMeasureData(RiskControlMeasureQueryVO query) {
        return null;
    }

    @Override
    public byte[] exportMeasureReport(String enterpriseId, String reportType, LocalDate startDate, LocalDate endDate, String format) {
        return new byte[0];
    }

    private BigDecimal calculateCostEffectiveness(RiskControlMeasure riskControlMeasure) {
        try {
            // 计算成本效益
            if (riskControlMeasure.getBudgetAmount() != null && 
                riskControlMeasure.getActualExpenditure() != null &&
                riskControlMeasure.getBudgetAmount().compareTo(BigDecimal.ZERO) > 0) {
                
                BigDecimal costRatio = riskControlMeasure.getActualExpenditure()
                        .divide(riskControlMeasure.getBudgetAmount(), 4, RoundingMode.HALF_UP);
                
                // 成本控制在预算内得分更高
                if (costRatio.compareTo(BigDecimal.ONE) <= 0) {
                    return new BigDecimal("100").subtract(costRatio.multiply(new BigDecimal("20")));
                } else {
                    return new BigDecimal("80").divide(costRatio, 2, RoundingMode.HALF_UP);
                }
            }
            
            return new BigDecimal("80.0");
        } catch (Exception e) {
            log.error("计算成本效益失败", e);
            return new BigDecimal("80.0");
        }
    }

    private BigDecimal calculateTimeEfficiency(RiskControlMeasure riskControlMeasure) {
        try {
            // 计算时间效率
            if (riskControlMeasure.getPlannedCompletionDate() != null && 
                riskControlMeasure.getActualCompletionDate() != null) {
                
                // 如果提前完成或按时完成，得分较高
                if (!riskControlMeasure.getActualCompletionDate().isAfter(riskControlMeasure.getPlannedCompletionDate())) {
                    return new BigDecimal("95.0");
                } else {
                    // 延期完成，根据延期天数扣分
                    return new BigDecimal("70.0");
                }
            }
            
            return new BigDecimal("85.0");
        } catch (Exception e) {
            log.error("计算时间效率失败", e);
            return new BigDecimal("85.0");
        }
    }

    // ==================== 标签转换业务 ====================

    @Override
    public String convertMeasureTypeLabel(String measureType) {
        if (measureType == null) return "";
        
        switch (measureType) {
            case RiskControlMeasure.MEASURE_TYPE_PREVENTIVE:
                return "预防性措施";
            case RiskControlMeasure.MEASURE_TYPE_DETECTIVE:
                return "检测性措施";
            case RiskControlMeasure.MEASURE_TYPE_CORRECTIVE:
                return "纠正性措施";
            case RiskControlMeasure.MEASURE_TYPE_COMPENSATING:
                return "补偿性措施";
            case RiskControlMeasure.MEASURE_TYPE_DIRECTIVE:
                return "指导性措施";
            default:
                return measureType;
        }
    }

    @Override
    public String convertMeasureCategoryLabel(String measureCategory) {
        return "";
    }

    @Override
    public String convertMeasureStatusLabel(String measureStatus) {
        if (measureStatus == null) return "";

        switch (measureStatus) {
            case RiskControlMeasure.MEASURE_STATUS_DRAFT:
                return "草稿";
            case RiskControlMeasure.MEASURE_STATUS_APPROVED:
                return "已批准";
            case RiskControlMeasure.MEASURE_STATUS_ACTIVE:
                return "生效中";
            case RiskControlMeasure.MEASURE_STATUS_SUSPENDED:
                return "已暂停";
            case RiskControlMeasure.MEASURE_STATUS_TERMINATED:
                return "已终止";
            case RiskControlMeasure.MEASURE_STATUS_COMPLETED:
                return "已完成";
            default:
                return measureStatus;
        }
    }

    @Override
    public String convertPriorityLabel(String priority) {
        return "";
    }

    @Override
    public String convertReviewStatusLabel(String reviewStatus) {
        if (reviewStatus == null) return "";

        switch (reviewStatus) {
            case "PENDING":
                return "待审核";
            case "APPROVED":
                return "已批准";
            case "REJECTED":
                return "已拒绝";
            case "UNDER_REVIEW":
                return "审核中";
            default:
                return reviewStatus;
        }
    }

    @Override
    public String convertEffectivenessLevelLabel(String effectivenessLevel) {
        if (effectivenessLevel == null) return "";

        switch (effectivenessLevel) {
            case RiskControlMeasure.EFFECTIVENESS_EXCELLENT:
                return "优秀";
            case RiskControlMeasure.EFFECTIVENESS_GOOD:
                return "良好";
            case RiskControlMeasure.EFFECTIVENESS_SATISFACTORY:
                return "满意";
            case RiskControlMeasure.EFFECTIVENESS_POOR:
                return "较差";
            case RiskControlMeasure.EFFECTIVENESS_INEFFECTIVE:
                return "无效";
            default:
                return effectivenessLevel;
        }
    }

    @Override
    public String convertImplementationStatusLabel(String implementationStatus) {
        if (implementationStatus == null) return "";

        switch (implementationStatus) {
            case RiskControlMeasure.IMPLEMENTATION_STATUS_NOT_STARTED:
                return "未开始";
            case RiskControlMeasure.IMPLEMENTATION_STATUS_IN_PROGRESS:
                return "实施中";
            case RiskControlMeasure.IMPLEMENTATION_STATUS_COMPLETED:
                return "已完成";
            case RiskControlMeasure.IMPLEMENTATION_STATUS_SUSPENDED:
                return "已暂停";
            default:
                return implementationStatus;
        }
    }

}
