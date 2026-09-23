package com.huabo.cybermonitor.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.cybermonitor.entity.RiskIncident;
import com.huabo.cybermonitor.mapper.RiskIncidentMapper;
import com.huabo.cybermonitor.service.IRiskIncidentService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.RiskIncidentQueryVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 风险事件业务实现类
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Slf4j
@Service
public class RiskIncidentServiceImpl extends ServiceImpl<RiskIncidentMapper, RiskIncident> implements IRiskIncidentService {

    @Autowired
    private RiskIncidentMapper riskIncidentMapper;

    // ==================== 基础业务方法 ====================

    @Override
    public PageResult<RiskIncident> selectByPage(RiskIncidentQueryVO query) {
        try {
            PageHelper.startPage(query.getPageNumber(), query.getPageSize());
            List<RiskIncident> list = riskIncidentMapper.selectByCondition(query);
            PageInfo<RiskIncident> pageInfo = new PageInfo<>(list);

            return new PageResult<RiskIncident>((int)pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception e) {
            log.error("分页查询风险事件失败", e);
            throw new RuntimeException("分页查询风险事件失败: " + e.getMessage());
        }
    }

    @Override
    public List<RiskIncident> getByEnterpriseId(String enterpriseId) {
        try {
            return riskIncidentMapper.selectByEnterpriseId(enterpriseId);
        } catch (Exception e) {
            log.error("根据企业ID获取风险事件列表失败: {}", enterpriseId, e);
            throw new RuntimeException("获取企业风险事件列表失败: " + e.getMessage());
        }
    }

    @Override
    public RiskIncident getByIncidentNumber(String incidentNumber) {
        try {
            return riskIncidentMapper.selectByIncidentNumber(incidentNumber);
        } catch (Exception e) {
            log.error("根据事件编号获取风险事件失败: {}", incidentNumber, e);
            throw new RuntimeException("获取风险事件失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRiskIncident(RiskIncident riskIncident) {
        try {
            riskIncident.setCreateTime(LocalDateTime.now());
            
            // 评估风险等级
            String riskLevel = assessIncidentRiskLevel(riskIncident);
            riskIncident.setRiskLevel(riskLevel);
            
            // 评估影响程度
            String impactLevel = assessImpactLevel(riskIncident);
            riskIncident.setImpactLevel(impactLevel);
            
            // 评估紧急程度
            String urgencyLevel = assessIncidentUrgency(riskIncident);
            riskIncident.setUrgencyLevel(urgencyLevel);
            
            return save(riskIncident);
        } catch (Exception e) {
            log.error("保存风险事件失败", e);
            throw new RuntimeException("保存风险事件失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRiskIncident(RiskIncident riskIncident) {
        try {
            riskIncident.setUpdateTime(LocalDateTime.now());
            
            // 重新评估损失
            Map<String, BigDecimal> lossAssessment = assessEconomicLoss(riskIncident);
            riskIncident.setTotalEconomicLoss((BigDecimal) lossAssessment.get("totalLoss"));
            
            return updateById(riskIncident);
        } catch (Exception e) {
            log.error("更新风险事件失败", e);
            throw new RuntimeException("更新风险事件失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRiskIncident(String riskIncidentId) {
        try {
            return removeById(riskIncidentId);
        } catch (Exception e) {
            log.error("删除风险事件失败: {}", riskIncidentId, e);
            throw new RuntimeException("删除风险事件失败: " + e.getMessage());
        }
    }

    // ==================== 事件报告业务 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RiskIncident reportRiskIncident(String enterpriseId, String incidentName, String incidentType, String incidentDescription, String reporter) {
        try {
            RiskIncident incident = new RiskIncident();
            incident.setEnterpriseId(enterpriseId);
            incident.setIncidentName(incidentName);
            incident.setIncidentType(incidentType);
            incident.setIncidentDescription(incidentDescription);
            incident.setReporter(reporter);
            incident.setReportTime(LocalDateTime.now());
            incident.setIncidentStatus(RiskIncident.INCIDENT_STATUS_REPORTED);
            incident.setHandlingStatus(RiskIncident.HANDLING_STATUS_NOT_STARTED);
            
            // 生成事件编号
            String incidentNumber = generateIncidentNumber(enterpriseId, incidentType);
            incident.setIncidentNumber(incidentNumber);
            
            // 自动分类事件
            String autoClassification = autoClassifyIncident(incident);
            incident.setIncidentClassification(autoClassification);
            
            // 评估紧急程度
            String urgencyLevel = assessIncidentUrgency(incident);
            incident.setUrgencyLevel(urgencyLevel);
            
            // 评估风险等级
            String riskLevel = assessIncidentRiskLevel(incident);
            incident.setRiskLevel(riskLevel);
            
            // 评估影响程度
            String impactLevel = assessImpactLevel(incident);
            incident.setImpactLevel(impactLevel);
            
            incident.setCreateTime(LocalDateTime.now());
            
            save(incident);
            return incident;
        } catch (Exception e) {
            log.error("报告风险事件失败: {}, {}, {}, {}, {}", enterpriseId, incidentName, incidentType, incidentDescription, reporter, e);
            throw new RuntimeException("报告风险事件失败: " + e.getMessage());
        }
    }

    @Override
    public String generateIncidentNumber(String enterpriseId, String incidentType) {
        try {
            String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String typePrefix = getIncidentTypePrefix(incidentType);
            
            // 获取当日同类型事件数量
            Integer count = riskIncidentMapper.countTodayIncidentsByType(enterpriseId, incidentType);
            String sequence = String.format("%03d", count + 1);
            
            return String.format("INC-%s-%s-%s-%s", enterpriseId.substring(0, Math.min(6, enterpriseId.length())), 
                    typePrefix, dateStr, sequence);
        } catch (Exception e) {
            log.error("生成事件编号失败: {}, {}", enterpriseId, incidentType, e);
            return "INC-" + System.currentTimeMillis();
        }
    }

    private String getIncidentTypePrefix(String incidentType) {
        switch (incidentType) {
            case RiskIncident.INCIDENT_TYPE_FINANCIAL:
                return "FIN";
            case RiskIncident.INCIDENT_TYPE_OPERATIONAL:
                return "OPR";
            case RiskIncident.INCIDENT_TYPE_COMPLIANCE:
                return "CMP";
            case RiskIncident.INCIDENT_TYPE_REPUTATION:
                return "REP";
            case RiskIncident.INCIDENT_TYPE_STRATEGIC:
                return "STR";
            case RiskIncident.INCIDENT_TYPE_TECHNOLOGY:
                return "TEC";
            case RiskIncident.INCIDENT_TYPE_ENVIRONMENTAL:
                return "ENV";
            case RiskIncident.INCIDENT_TYPE_SECURITY:
                return "SEC";
            default:
                return "GEN";
        }
    }

    @Override
    public boolean validateIncidentInfo(RiskIncident riskIncident) {
        try {
            // 验证必填字段
            if (riskIncident.getEnterpriseId() == null || riskIncident.getEnterpriseId().trim().isEmpty()) {
                return false;
            }
            if (riskIncident.getIncidentName() == null || riskIncident.getIncidentName().trim().isEmpty()) {
                return false;
            }
            if (riskIncident.getIncidentType() == null || riskIncident.getIncidentType().trim().isEmpty()) {
                return false;
            }
            if (riskIncident.getIncidentDescription() == null || riskIncident.getIncidentDescription().trim().isEmpty()) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.error("验证事件信息失败", e);
            return false;
        }
    }

    @Override
    public String autoClassifyIncident(RiskIncident riskIncident) {
        try {
            // 根据事件描述和类型自动分类
            String description = riskIncident.getIncidentDescription().toLowerCase();
            String incidentType = riskIncident.getIncidentType();
            
            if (description.contains("资金") || description.contains("财务") || description.contains("损失")) {
                return "FINANCIAL_LOSS";
            } else if (description.contains("系统") || description.contains("技术") || description.contains("故障")) {
                return "SYSTEM_FAILURE";
            } else if (description.contains("合规") || description.contains("违规") || description.contains("法律")) {
                return "COMPLIANCE_VIOLATION";
            } else if (description.contains("声誉") || description.contains("媒体") || description.contains("舆情")) {
                return "REPUTATION_DAMAGE";
            } else {
                return "GENERAL";
            }
        } catch (Exception e) {
            log.error("自动分类事件失败", e);
            return "GENERAL";
        }
    }

    @Override
    public String assessIncidentUrgency(RiskIncident riskIncident) {
        try {
            // 根据事件类型和影响程度评估紧急程度
            String incidentType = riskIncident.getIncidentType();
            String impactLevel = riskIncident.getImpactLevel();
            
            if (RiskIncident.INCIDENT_TYPE_SECURITY.equals(incidentType) || 
                RiskIncident.INCIDENT_TYPE_FINANCIAL.equals(incidentType)) {
                return RiskIncident.URGENCY_LEVEL_URGENT;
            } else if ("HIGH".equals(impactLevel) || "VERY_HIGH".equals(impactLevel)) {
                return RiskIncident.URGENCY_LEVEL_HIGH;
            } else if ("MEDIUM".equals(impactLevel)) {
                return RiskIncident.URGENCY_LEVEL_MEDIUM;
            } else {
                return RiskIncident.URGENCY_LEVEL_LOW;
            }
        } catch (Exception e) {
            log.error("评估事件紧急程度失败", e);
            return RiskIncident.URGENCY_LEVEL_MEDIUM;
        }
    }

    // ==================== 风险评估业务 ====================

    @Override
    public String assessIncidentRiskLevel(RiskIncident riskIncident) {
        try {
            BigDecimal riskScore = calculateRiskScore(riskIncident);
            
            if (riskScore.compareTo(new BigDecimal("90")) >= 0) {
                return RiskIncident.RISK_LEVEL_CRITICAL;
            } else if (riskScore.compareTo(new BigDecimal("80")) >= 0) {
                return RiskIncident.RISK_LEVEL_VERY_HIGH;
            } else if (riskScore.compareTo(new BigDecimal("70")) >= 0) {
                return RiskIncident.RISK_LEVEL_HIGH;
            } else if (riskScore.compareTo(new BigDecimal("50")) >= 0) {
                return RiskIncident.RISK_LEVEL_MEDIUM;
            } else if (riskScore.compareTo(new BigDecimal("30")) >= 0) {
                return RiskIncident.RISK_LEVEL_LOW;
            } else {
                return RiskIncident.RISK_LEVEL_VERY_LOW;
            }
        } catch (Exception e) {
            log.error("评估事件风险等级失败", e);
            return RiskIncident.RISK_LEVEL_MEDIUM;
        }
    }

    @Override
    public BigDecimal calculateRiskScore(RiskIncident riskIncident) {
        try {
            // 影响程度评分 (40%)
            BigDecimal impactScore = getImpactScore(riskIncident.getImpactLevel());
            
            // 发生概率评分 (30%)
            BigDecimal probabilityScore = calculateProbability(riskIncident);
            
            // 紧急程度评分 (20%)
            BigDecimal urgencyScore = getUrgencyScore(riskIncident.getUrgencyLevel());
            
            // 经济损失评分 (10%)
            BigDecimal lossScore = getLossScore(riskIncident.getTotalEconomicLoss());
            
            // 计算综合风险评分
            BigDecimal totalScore = impactScore.multiply(new BigDecimal("0.40"))
                    .add(probabilityScore.multiply(new BigDecimal("0.30")))
                    .add(urgencyScore.multiply(new BigDecimal("0.20")))
                    .add(lossScore.multiply(new BigDecimal("0.10")));
            
            return totalScore.setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("计算风险评分失败", e);
            return new BigDecimal("50.0");
        }
    }

    @Override
    public String assessImpactLevel(RiskIncident riskIncident) {
        try {
            // 根据事件类型和描述评估影响程度
            String incidentType = riskIncident.getIncidentType();
            BigDecimal economicLoss = riskIncident.getTotalEconomicLoss();
            
            if (economicLoss != null && economicLoss.compareTo(new BigDecimal("10000000")) >= 0) {
                return RiskIncident.IMPACT_LEVEL_VERY_HIGH;
            } else if (economicLoss != null && economicLoss.compareTo(new BigDecimal("1000000")) >= 0) {
                return RiskIncident.IMPACT_LEVEL_HIGH;
            } else if (RiskIncident.INCIDENT_TYPE_REPUTATION.equals(incidentType) || 
                       RiskIncident.INCIDENT_TYPE_COMPLIANCE.equals(incidentType)) {
                return RiskIncident.IMPACT_LEVEL_HIGH;
            } else {
                return RiskIncident.IMPACT_LEVEL_MEDIUM;
            }
        } catch (Exception e) {
            log.error("评估影响程度失败", e);
            return RiskIncident.IMPACT_LEVEL_MEDIUM;
        }
    }

    @Override
    public BigDecimal calculateProbability(RiskIncident riskIncident) {
        try {
            // 根据历史数据和事件类型计算发生概率
            // 这里应该实现具体的概率计算逻辑
            // 暂时返回模拟数据
            return new BigDecimal("65.0");
        } catch (Exception e) {
            log.error("计算发生概率失败", e);
            return new BigDecimal("50.0");
        }
    }

    @Override
    public String analyzeImpactScope(RiskIncident riskIncident) {
        try {
            // 分析影响范围
            String incidentType = riskIncident.getIncidentType();
            
            if (RiskIncident.INCIDENT_TYPE_FINANCIAL.equals(incidentType)) {
                return "财务影响：资金流动性、盈利能力、偿债能力";
            } else if (RiskIncident.INCIDENT_TYPE_OPERATIONAL.equals(incidentType)) {
                return "经营影响：业务运营、生产效率、客户服务";
            } else if (RiskIncident.INCIDENT_TYPE_REPUTATION.equals(incidentType)) {
                return "声誉影响：品牌形象、市场信任度、客户关系";
            } else if (RiskIncident.INCIDENT_TYPE_COMPLIANCE.equals(incidentType)) {
                return "合规影响：监管处罚、法律风险、业务资质";
            } else {
                return "综合影响：多个业务领域可能受到影响";
            }
        } catch (Exception e) {
            log.error("分析影响范围失败", e);
            return "影响范围待评估";
        }
    }

    // ==================== 损失评估业务 ====================

    @Override
    public Map<String, BigDecimal> assessEconomicLoss(RiskIncident riskIncident) {
        try {
            Map<String, BigDecimal> lossAssessment = new HashMap<>();
            
            BigDecimal directLoss = calculateDirectLoss(riskIncident);
            BigDecimal indirectLoss = calculateIndirectLoss(riskIncident);
            BigDecimal recoveryCost = calculateRecoveryCost(riskIncident);
            
            BigDecimal totalLoss = directLoss.add(indirectLoss).add(recoveryCost);
            
            lossAssessment.put("directLoss", directLoss);
            lossAssessment.put("indirectLoss", indirectLoss);
            lossAssessment.put("recoveryCost", recoveryCost);
            lossAssessment.put("totalLoss", totalLoss);
            
            return lossAssessment;
        } catch (Exception e) {
            log.error("评估经济损失失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public BigDecimal calculateDirectLoss(RiskIncident riskIncident) {
        try {
            // 计算直接损失
            // 这里应该根据事件类型和具体情况计算
            // 暂时返回模拟数据
            return riskIncident.getDirectEconomicLoss() != null ? 
                    riskIncident.getDirectEconomicLoss() : new BigDecimal("100000");
        } catch (Exception e) {
            log.error("计算直接损失失败", e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateIndirectLoss(RiskIncident riskIncident) {
        try {
            // 计算间接损失
            // 这里应该根据事件类型和具体情况计算
            // 暂时返回模拟数据
            return riskIncident.getIndirectEconomicLoss() != null ? 
                    riskIncident.getIndirectEconomicLoss() : new BigDecimal("50000");
        } catch (Exception e) {
            log.error("计算间接损失失败", e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public String assessReputationLoss(RiskIncident riskIncident) {
        try {
            // 评估声誉损失
            if (RiskIncident.INCIDENT_TYPE_REPUTATION.equals(riskIncident.getIncidentType())) {
                return "重大声誉损失";
            } else if (RiskIncident.INCIDENT_TYPE_COMPLIANCE.equals(riskIncident.getIncidentType())) {
                return "中等声誉损失";
            } else {
                return "轻微声誉损失";
            }
        } catch (Exception e) {
            log.error("评估声誉损失失败", e);
            return "声誉损失待评估";
        }
    }

    @Override
    public BigDecimal calculateRecoveryCost(RiskIncident riskIncident) {
        try {
            // 计算恢复成本
            // 这里应该根据事件类型和具体情况计算
            // 暂时返回模拟数据
            return new BigDecimal("30000");
        } catch (Exception e) {
            log.error("计算恢复成本失败", e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public boolean initiateEmergencyResponse(String riskIncidentId, String emergencyResponseLevel, String responseTeam) {
        return false;
    }

    @Override
    public String determineEmergencyResponseLevel(RiskIncident riskIncident) {
        return "";
    }

    @Override
    public boolean executeEmergencyMeasures(String riskIncidentId, String emergencyMeasures) {
        return false;
    }

    @Override
    public boolean endEmergencyResponse(String riskIncidentId, LocalDateTime endTime, String responseEffectiveness) {
        return false;
    }

    @Override
    public String evaluateEmergencyResponseEffectiveness(String riskIncidentId) {
        return "";
    }

    @Override
    public boolean startInvestigation(String riskIncidentId, String investigationTeam) {
        return false;
    }

    @Override
    public Map<String, String> analyzeCauses(RiskIncident riskIncident) {
        return null;
    }

    @Override
    public String identifyRootCause(RiskIncident riskIncident) {
        return "";
    }

    @Override
    public boolean assignHandler(String riskIncidentId, String handlingResponsiblePerson, String handlingTeam) {
        return false;
    }

    @Override
    public boolean startHandling(String riskIncidentId, String handlingMeasures) {
        return false;
    }

    @Override
    public boolean completeHandling(String riskIncidentId, LocalDateTime completionTime, String handlingResult) {
        return false;
    }

    @Override
    public boolean developPreventiveMeasures(String riskIncidentId, String preventiveMeasures, String responsiblePerson) {
        return false;
    }

    @Override
    public boolean developCorrectiveMeasures(String riskIncidentId, String correctiveMeasures, String responsiblePerson) {
        return false;
    }

    @Override
    public boolean developImprovementMeasures(String riskIncidentId, String improvementMeasures, String responsiblePerson) {
        return false;
    }

    @Override
    public boolean trackMeasureImplementation(String riskIncidentId) {
        return false;
    }

    @Override
    public String evaluateMeasureEffectiveness(String riskIncidentId) {
        return "";
    }

    @Override
    public boolean detectRecurringIncident(RiskIncident riskIncident) {
        return false;
    }

    @Override
    public boolean linkRecurringIncident(String riskIncidentId, String parentIncidentId) {
        return false;
    }

    @Override
    public Map<String, Object> analyzeRecurrencePattern(String enterpriseId, String incidentType) {
        return null;
    }

    @Override
    public List<RiskIncident> getRecurringIncidents(String enterpriseId) {
        return null;
    }

    @Override
    public List<String> developRecurrencePreventionStrategy(String parentIncidentId) {
        return null;
    }

    @Override
    public boolean requiresRegulatoryReport(RiskIncident riskIncident) {
        return false;
    }

    @Override
    public Map<String, Object> generateRegulatoryReport(String riskIncidentId) {
        return null;
    }

    @Override
    public boolean submitRegulatoryReport(String riskIncidentId, String regulatoryAuthority) {
        return false;
    }

    @Override
    public boolean trackRegulatoryFeedback(String riskIncidentId, String regulatoryFeedback) {
        return false;
    }

    @Override
    public boolean collectLessonsLearned(String riskIncidentId, String lessonsLearned, String collector) {
        return false;
    }

    @Override
    public List<String> extractBestPractices(String riskIncidentId) {
        return null;
    }

    @Override
    public boolean shareKnowledge(String riskIncidentId, String knowledgeContent, String targetAudience) {
        return false;
    }

    @Override
    public List<String> identifyTrainingNeeds(String riskIncidentId) {
        return null;
    }

    @Override
    public boolean updatePolicyRecommendations(String riskIncidentId, String policyRecommendations) {
        return false;
    }

    @Override
    public boolean updateIncidentStatus(String riskIncidentId, String incidentStatus, String updateBy) {
        return false;
    }

    @Override
    public boolean escalateIncident(String riskIncidentId, String escalationReason, String escalatedTo) {
        return false;
    }

    @Override
    public boolean closeIncident(String riskIncidentId, String closureReason, String closedBy) {
        return false;
    }

    @Override
    public boolean reopenIncident(String riskIncidentId, String reopenReason, String reopenedBy) {
        return false;
    }

    @Override
    public Map<String, Object> getComprehensiveStatistics() {
        return null;
    }

    @Override
    public Map<String, Object> getEnterpriseIncidentOverview(String enterpriseId) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getIncidentTrendAnalysis(Integer months) {
        return null;
    }

    @Override
    public Map<String, Object> getIncidentTypeDistribution() {
        return null;
    }

    @Override
    public Map<String, Object> getLossStatisticsAnalysis() {
        return null;
    }

    @Override
    public Map<String, Object> getHandlingEffectivenessAnalysis() {
        return null;
    }

    @Override
    public List<Map<String, Object>> getPeerIncidentComparison(String enterpriseId, String industryType) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getHistoricalIncidentComparison(String enterpriseId, Integer years) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getRegionalIncidentComparison(String region) {
        return null;
    }

    @Override
    public Map<String, Object> generateIncidentReport(String riskIncidentId) {
        return null;
    }

    @Override
    public Map<String, Object> generateIncidentAnalysisReport(String enterpriseId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public Map<String, Object> generateIncidentTrendReport(String enterpriseId, Integer months) {
        return null;
    }

    @Override
    public boolean batchUpdateIncidentStatus(List<String> riskIncidentIds, String status, String updateBy) {
        return false;
    }

    @Override
    public boolean batchAssignHandler(List<String> riskIncidentIds, String handlingResponsiblePerson, String updateBy) {
        return false;
    }

    @Override
    public boolean batchCloseIncidents(List<String> riskIncidentIds, String closureReason, String updateBy) {
        return false;
    }

    @Override
    public List<Map<String, Object>> batchGenerateReports(List<String> riskIncidentIds, String reportType) {
        return null;
    }

    @Override
    public List<Map<String, Object>> exportIncidentData(RiskIncidentQueryVO query) {
        return null;
    }

    @Override
    public byte[] exportIncidentReport(String enterpriseId, String reportType, LocalDate startDate, LocalDate endDate, String format) {
        return new byte[0];
    }

    // ==================== 辅助方法 ====================

    private BigDecimal getImpactScore(String impactLevel) {
        if (impactLevel == null) return new BigDecimal("50");
        
        switch (impactLevel) {
            case RiskIncident.IMPACT_LEVEL_VERY_HIGH:
                return new BigDecimal("95");
            case RiskIncident.IMPACT_LEVEL_HIGH:
                return new BigDecimal("80");
            case RiskIncident.IMPACT_LEVEL_MEDIUM:
                return new BigDecimal("60");
            case RiskIncident.IMPACT_LEVEL_LOW:
                return new BigDecimal("40");
            case RiskIncident.IMPACT_LEVEL_VERY_LOW:
                return new BigDecimal("20");
            default:
                return new BigDecimal("50");
        }
    }

    private BigDecimal getUrgencyScore(String urgencyLevel) {
        if (urgencyLevel == null) return new BigDecimal("50");
        
        switch (urgencyLevel) {
            case RiskIncident.URGENCY_LEVEL_URGENT:
                return new BigDecimal("95");
            case RiskIncident.URGENCY_LEVEL_HIGH:
                return new BigDecimal("80");
            case RiskIncident.URGENCY_LEVEL_MEDIUM:
                return new BigDecimal("60");
            case RiskIncident.URGENCY_LEVEL_LOW:
                return new BigDecimal("40");
            default:
                return new BigDecimal("50");
        }
    }

    private BigDecimal getLossScore(BigDecimal economicLoss) {
        if (economicLoss == null) return new BigDecimal("30");
        
        if (economicLoss.compareTo(new BigDecimal("10000000")) >= 0) {
            return new BigDecimal("95");
        } else if (economicLoss.compareTo(new BigDecimal("1000000")) >= 0) {
            return new BigDecimal("80");
        } else if (economicLoss.compareTo(new BigDecimal("100000")) >= 0) {
            return new BigDecimal("60");
        } else if (economicLoss.compareTo(new BigDecimal("10000")) >= 0) {
            return new BigDecimal("40");
        } else {
            return new BigDecimal("20");
        }
    }

    // ==================== 标签转换业务 ====================

    @Override
    public String convertIncidentTypeLabel(String incidentType) {
        if (incidentType == null) return "";
        
        switch (incidentType) {
            case RiskIncident.INCIDENT_TYPE_FINANCIAL:
                return "财务风险事件";
            case RiskIncident.INCIDENT_TYPE_OPERATIONAL:
                return "经营风险事件";
            case RiskIncident.INCIDENT_TYPE_COMPLIANCE:
                return "合规风险事件";
            case RiskIncident.INCIDENT_TYPE_REPUTATION:
                return "声誉风险事件";
            case RiskIncident.INCIDENT_TYPE_STRATEGIC:
                return "战略风险事件";
            case RiskIncident.INCIDENT_TYPE_TECHNOLOGY:
                return "技术风险事件";
            case RiskIncident.INCIDENT_TYPE_ENVIRONMENTAL:
                return "环境风险事件";
            case RiskIncident.INCIDENT_TYPE_SECURITY:
                return "安全风险事件";
            default:
                return incidentType;
        }
    }

    @Override
    public String convertIncidentStatusLabel(String incidentStatus) {
        if (incidentStatus == null) return "";
        
        switch (incidentStatus) {
            case RiskIncident.INCIDENT_STATUS_REPORTED:
                return "已报告";
            case RiskIncident.INCIDENT_STATUS_CONFIRMED:
                return "已确认";
            case RiskIncident.INCIDENT_STATUS_INVESTIGATING:
                return "调查中";
            case RiskIncident.INCIDENT_STATUS_HANDLING:
                return "处理中";
            case RiskIncident.INCIDENT_STATUS_RESOLVED:
                return "已解决";
            case RiskIncident.INCIDENT_STATUS_CLOSED:
                return "已关闭";
            default:
                return incidentStatus;
        }
    }

    @Override
    public String convertRiskLevelLabel(String riskLevel) {
        if (riskLevel == null) return "";

        switch (riskLevel) {
            case RiskIncident.RISK_LEVEL_VERY_LOW:
                return "极低";
            case RiskIncident.RISK_LEVEL_LOW:
                return "低";
            case RiskIncident.RISK_LEVEL_MEDIUM:
                return "中";
            case RiskIncident.RISK_LEVEL_HIGH:
                return "高";
            case RiskIncident.RISK_LEVEL_VERY_HIGH:
                return "极高";
            case RiskIncident.RISK_LEVEL_CRITICAL:
                return "临界";
            default:
                return riskLevel;
        }
    }

    @Override
    public String convertImpactLevelLabel(String impactLevel) {
        if (impactLevel == null) return "";

        switch (impactLevel) {
            case RiskIncident.IMPACT_LEVEL_VERY_HIGH:
                return "极高";
            case RiskIncident.IMPACT_LEVEL_HIGH:
                return "高";
            case RiskIncident.IMPACT_LEVEL_MEDIUM:
                return "中";
            case RiskIncident.IMPACT_LEVEL_LOW:
                return "低";
            case RiskIncident.IMPACT_LEVEL_VERY_LOW:
                return "极低";
            default:
                return impactLevel;
        }
    }

    @Override
    public String convertUrgencyLevelLabel(String urgencyLevel) {
        if (urgencyLevel == null) return "";

        switch (urgencyLevel) {
            case RiskIncident.URGENCY_LEVEL_LOW:
                return "低";
            case RiskIncident.URGENCY_LEVEL_MEDIUM:
                return "中";
            case RiskIncident.URGENCY_LEVEL_HIGH:
                return "高";
            case RiskIncident.URGENCY_LEVEL_URGENT:
                return "紧急";
            case RiskIncident.URGENCY_LEVEL_CRITICAL:
                return "关键";
            default:
                return urgencyLevel;
        }
    }

    @Override
    public String convertHandlingStatusLabel(String handlingStatus) {
        if (handlingStatus == null) return "";

        switch (handlingStatus) {
            case RiskIncident.HANDLING_STATUS_NOT_STARTED:
                return "未开始";
            case RiskIncident.HANDLING_STATUS_IN_PROGRESS:
                return "处理中";
            case RiskIncident.HANDLING_STATUS_COMPLETED:
                return "已完成";
            case RiskIncident.HANDLING_STATUS_SUSPENDED:
                return "已暂停";
            default:
                return handlingStatus;
        }
    }

    @Override
    public String convertEmergencyResponseLevelLabel(String emergencyResponseLevel) {
        if (emergencyResponseLevel == null) return "";

        switch (emergencyResponseLevel) {
            case "LEVEL_1":
                return "一级应急";
            case "LEVEL_2":
                return "二级应急";
            case "LEVEL_3":
                return "三级应急";
            case "LEVEL_4":
                return "四级应急";
            default:
                return emergencyResponseLevel;
        }
    }

}
