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
import com.huabo.cybermonitor.entity.RiskAssessment;
import com.huabo.cybermonitor.mapper.RiskAssessmentMapper;
import com.huabo.cybermonitor.service.IRiskAssessmentService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.RiskAssessmentQueryVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 风险评估业务实现类
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Slf4j
@Service
public class RiskAssessmentServiceImpl extends ServiceImpl<RiskAssessmentMapper, RiskAssessment> implements IRiskAssessmentService {

    @Autowired
    private RiskAssessmentMapper riskAssessmentMapper;

    // ==================== 基础业务方法 ====================

    @Override
    public PageResult<RiskAssessment> selectByPage(RiskAssessmentQueryVO query) {
        try {
            PageHelper.startPage(query.getPageNumber(), query.getPageSize());
            List<RiskAssessment> list = riskAssessmentMapper.selectByCondition(query);
            PageInfo<RiskAssessment> pageInfo = new PageInfo<>(list);
            
            return new PageResult<RiskAssessment>((int)pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            log.error("分页查询风险评估失败", e);
            throw new RuntimeException("分页查询风险评估失败: " + e.getMessage());
        }
    }

    @Override
    public List<RiskAssessment> getByEnterpriseId(String enterpriseId) {
        try {
            return riskAssessmentMapper.selectByEnterpriseId(enterpriseId);
        } catch (Exception e) {
            log.error("根据企业ID获取风险评估列表失败: {}", enterpriseId, e);
            throw new RuntimeException("获取企业风险评估列表失败: " + e.getMessage());
        }
    }

    @Override
    public RiskAssessment getByEnterpriseIdAndYear(String enterpriseId, Integer assessmentYear) {
        try {
            return riskAssessmentMapper.selectByEnterpriseIdAndYear(enterpriseId, assessmentYear);
        } catch (Exception e) {
            log.error("根据企业ID和年度获取风险评估失败: {}, {}", enterpriseId, assessmentYear, e);
            throw new RuntimeException("获取企业年度风险评估失败: " + e.getMessage());
        }
    }

    @Override
    public RiskAssessment getLatestByEnterpriseIdAndType(String enterpriseId, String assessmentType) {
        try {
            return riskAssessmentMapper.selectLatestByEnterpriseIdAndType(enterpriseId, assessmentType);
        } catch (Exception e) {
            log.error("获取最新风险评估失败: {}, {}", enterpriseId, assessmentType, e);
            throw new RuntimeException("获取最新风险评估失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRiskAssessment(RiskAssessment riskAssessment) {
        try {
            // 设置创建时间
            riskAssessment.setCreateTime(LocalDateTime.now());
            
            // 计算综合风险评分
            BigDecimal overallRiskScore = calculateOverallRiskScore(riskAssessment);
            riskAssessment.setOverallRiskScore(overallRiskScore);
            
            // 确定风险等级
            String riskLevel = determineRiskLevel(overallRiskScore);
            riskAssessment.setOverallRiskLevel(riskLevel);
            
            // 检查风险预警
            checkRiskWarning(riskAssessment);
            
            return save(riskAssessment);
        } catch (Exception e) {
            log.error("保存风险评估失败", e);
            throw new RuntimeException("保存风险评估失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRiskAssessment(RiskAssessment riskAssessment) {
        try {
            // 设置更新时间
            riskAssessment.setUpdateTime(LocalDateTime.now());
            
            // 重新计算综合风险评分
            BigDecimal overallRiskScore = calculateOverallRiskScore(riskAssessment);
            riskAssessment.setOverallRiskScore(overallRiskScore);
            
            // 重新确定风险等级
            String riskLevel = determineRiskLevel(overallRiskScore);
            riskAssessment.setOverallRiskLevel(riskLevel);
            
            // 检查风险预警
            checkRiskWarning(riskAssessment);
            
            return updateById(riskAssessment);
        } catch (Exception e) {
            log.error("更新风险评估失败", e);
            throw new RuntimeException("更新风险评估失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRiskAssessment(String riskAssessmentId) {
        try {
            return removeById(riskAssessmentId);
        } catch (Exception e) {
            log.error("删除风险评估失败: {}", riskAssessmentId, e);
            throw new RuntimeException("删除风险评估失败: " + e.getMessage());
        }
    }

    // ==================== 风险评估核心业务 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RiskAssessment performRiskAssessment(String enterpriseId, String assessmentType, String assessmentMethod) {
        try {
            RiskAssessment riskAssessment = new RiskAssessment();
            riskAssessment.setEnterpriseId(enterpriseId);
            riskAssessment.setAssessmentType(assessmentType);
            riskAssessment.setAssessmentMethod(assessmentMethod);
            riskAssessment.setAssessmentYear(LocalDate.now().getYear());
            riskAssessment.setAssessmentDate(LocalDate.now());
            riskAssessment.setAssessmentStatus(RiskAssessment.ASSESSMENT_STATUS_IN_PROGRESS);
            
            // 计算各类风险评分
            BigDecimal financialRiskScore = calculateFinancialRiskScore(enterpriseId, riskAssessment.getAssessmentYear());
            riskAssessment.setFinancialRiskScore(financialRiskScore);
            riskAssessment.setFinancialRiskLevel(determineRiskLevel(financialRiskScore));
            
            BigDecimal operationalRiskScore = calculateOperationalRiskScore(enterpriseId, riskAssessment.getAssessmentYear());
            riskAssessment.setOperationalRiskScore(operationalRiskScore);
            riskAssessment.setOperationalRiskLevel(determineRiskLevel(operationalRiskScore));
            
            BigDecimal complianceRiskScore = calculateComplianceRiskScore(enterpriseId, riskAssessment.getAssessmentYear());
            riskAssessment.setComplianceRiskScore(complianceRiskScore);
            riskAssessment.setComplianceRiskLevel(determineRiskLevel(complianceRiskScore));
            
            BigDecimal governanceRiskScore = calculateGovernanceRiskScore(enterpriseId, riskAssessment.getAssessmentYear());
            riskAssessment.setGovernanceRiskScore(governanceRiskScore);
            riskAssessment.setGovernanceRiskLevel(determineRiskLevel(governanceRiskScore));
            
            BigDecimal externalRiskScore = calculateExternalRiskScore(enterpriseId, riskAssessment.getAssessmentYear());
            riskAssessment.setExternalRiskScore(externalRiskScore);
            riskAssessment.setExternalRiskLevel(determineRiskLevel(externalRiskScore));
            
            // 计算综合风险评分
            BigDecimal overallRiskScore = calculateOverallRiskScore(riskAssessment);
            riskAssessment.setOverallRiskScore(overallRiskScore);
            riskAssessment.setOverallRiskLevel(determineRiskLevel(overallRiskScore));
            
            // 分析风险趋势
            String riskTrend = analyzeRiskTrend(enterpriseId, assessmentType);
            riskAssessment.setRiskTrend(riskTrend);
            
            // 识别风险因素
            List<String> riskFactors = identifyRiskFactors(enterpriseId, assessmentType);
            riskAssessment.setMajorRiskFactors(String.join(",", riskFactors));
            riskAssessment.setRiskFactorCount(riskFactors.size());
            
            // 制定应对策略
            String responseStrategy = developRiskResponseStrategy(riskAssessment);
            riskAssessment.setRiskResponseStrategy(responseStrategy);
            
            // 检查预警
            checkRiskWarning(riskAssessment);
            
            // 保存评估结果
            save(riskAssessment);
            
            return riskAssessment;
        } catch (Exception e) {
            log.error("执行风险评估失败: {}, {}, {}", enterpriseId, assessmentType, assessmentMethod, e);
            throw new RuntimeException("执行风险评估失败: " + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateOverallRiskScore(RiskAssessment riskAssessment) {
        try {
            // 权重配置
            BigDecimal financialWeight = new BigDecimal("0.30");
            BigDecimal operationalWeight = new BigDecimal("0.25");
            BigDecimal complianceWeight = new BigDecimal("0.20");
            BigDecimal governanceWeight = new BigDecimal("0.15");
            BigDecimal externalWeight = new BigDecimal("0.10");
            
            BigDecimal totalScore = BigDecimal.ZERO;
            
            if (riskAssessment.getFinancialRiskScore() != null) {
                totalScore = totalScore.add(riskAssessment.getFinancialRiskScore().multiply(financialWeight));
            }
            if (riskAssessment.getOperationalRiskScore() != null) {
                totalScore = totalScore.add(riskAssessment.getOperationalRiskScore().multiply(operationalWeight));
            }
            if (riskAssessment.getComplianceRiskScore() != null) {
                totalScore = totalScore.add(riskAssessment.getComplianceRiskScore().multiply(complianceWeight));
            }
            if (riskAssessment.getGovernanceRiskScore() != null) {
                totalScore = totalScore.add(riskAssessment.getGovernanceRiskScore().multiply(governanceWeight));
            }
            if (riskAssessment.getExternalRiskScore() != null) {
                totalScore = totalScore.add(riskAssessment.getExternalRiskScore().multiply(externalWeight));
            }
            
            return totalScore.setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("计算综合风险评分失败", e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateFinancialRiskScore(String enterpriseId, Integer assessmentYear) {
        try {
            // 这里应该根据财务数据计算财务风险评分
            // 包括流动性风险、偿债能力风险、盈利能力风险等
            // 暂时返回模拟数据
            return new BigDecimal("65.5");
        } catch (Exception e) {
            log.error("计算财务风险评分失败: {}, {}", enterpriseId, assessmentYear, e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateOperationalRiskScore(String enterpriseId, Integer assessmentYear) {
        try {
            // 这里应该根据经营数据计算经营风险评分
            // 包括市场风险、竞争风险、技术风险等
            // 暂时返回模拟数据
            return new BigDecimal("58.3");
        } catch (Exception e) {
            log.error("计算经营风险评分失败: {}, {}", enterpriseId, assessmentYear, e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateComplianceRiskScore(String enterpriseId, Integer assessmentYear) {
        try {
            // 这里应该根据合规数据计算合规风险评分
            // 包括法律风险、监管风险、税务风险等
            // 暂时返回模拟数据
            return new BigDecimal("72.1");
        } catch (Exception e) {
            log.error("计算合规风险评分失败: {}, {}", enterpriseId, assessmentYear, e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateGovernanceRiskScore(String enterpriseId, Integer assessmentYear) {
        try {
            // 这里应该根据治理数据计算治理风险评分
            // 包括内控风险、决策风险、人员风险等
            // 暂时返回模拟数据
            return new BigDecimal("61.8");
        } catch (Exception e) {
            log.error("计算治理风险评分失败: {}, {}", enterpriseId, assessmentYear, e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateExternalRiskScore(String enterpriseId, Integer assessmentYear) {
        try {
            // 这里应该根据外部环境数据计算外部风险评分
            // 包括宏观经济风险、政策风险、行业风险等
            // 暂时返回模拟数据
            return new BigDecimal("55.7");
        } catch (Exception e) {
            log.error("计算外部风险评分失败: {}, {}", enterpriseId, assessmentYear, e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public String determineRiskLevel(BigDecimal riskScore) {
        if (riskScore == null) {
            return RiskAssessment.RISK_LEVEL_MEDIUM;
        }
        
        if (riskScore.compareTo(new BigDecimal("90")) >= 0) {
            return RiskAssessment.RISK_LEVEL_CRITICAL;
        } else if (riskScore.compareTo(new BigDecimal("80")) >= 0) {
            return RiskAssessment.RISK_LEVEL_VERY_HIGH;
        } else if (riskScore.compareTo(new BigDecimal("70")) >= 0) {
            return RiskAssessment.RISK_LEVEL_HIGH;
        } else if (riskScore.compareTo(new BigDecimal("50")) >= 0) {
            return RiskAssessment.RISK_LEVEL_MEDIUM;
        } else if (riskScore.compareTo(new BigDecimal("30")) >= 0) {
            return RiskAssessment.RISK_LEVEL_LOW;
        } else {
            return RiskAssessment.RISK_LEVEL_VERY_LOW;
        }
    }

    @Override
    public String analyzeRiskTrend(String enterpriseId, String riskType) {
        try {
            // 这里应该分析历史风险数据的趋势
            // 暂时返回模拟数据
            return RiskAssessment.RISK_TREND_STABLE;
        } catch (Exception e) {
            log.error("分析风险趋势失败: {}, {}", enterpriseId, riskType, e);
            return RiskAssessment.RISK_TREND_STABLE;
        }
    }

    // ==================== 风险预警业务 ====================

    @Override
    public boolean checkRiskWarning(RiskAssessment riskAssessment) {
        try {
            boolean isWarningTriggered = false;
            String warningLevel = null;
            String warningReason = null;
            
            // 检查综合风险等级
            if (RiskAssessment.RISK_LEVEL_CRITICAL.equals(riskAssessment.getOverallRiskLevel())) {
                isWarningTriggered = true;
                warningLevel = RiskAssessment.WARNING_LEVEL_RED;
                warningReason = "综合风险等级达到临界水平";
            } else if (RiskAssessment.RISK_LEVEL_VERY_HIGH.equals(riskAssessment.getOverallRiskLevel())) {
                isWarningTriggered = true;
                warningLevel = RiskAssessment.WARNING_LEVEL_ORANGE;
                warningReason = "综合风险等级为极高";
            } else if (RiskAssessment.RISK_LEVEL_HIGH.equals(riskAssessment.getOverallRiskLevel())) {
                isWarningTriggered = true;
                warningLevel = RiskAssessment.WARNING_LEVEL_YELLOW;
                warningReason = "综合风险等级为高风险";
            }
            
            if (isWarningTriggered) {
                triggerRiskWarning(riskAssessment, warningLevel, warningReason);
            }
            
            return isWarningTriggered;
        } catch (Exception e) {
            log.error("检查风险预警失败", e);
            return false;
        }
    }

    @Override
    public void triggerRiskWarning(RiskAssessment riskAssessment, String warningLevel, String warningReason) {
        try {
            riskAssessment.setIsWarningTriggered(true);
            riskAssessment.setWarningLevel(warningLevel);
            riskAssessment.setWarningReason(warningReason);
            riskAssessment.setWarningTriggerTime(LocalDateTime.now());
            
            log.warn("触发风险预警 - 企业: {}, 等级: {}, 原因: {}", 
                    riskAssessment.getEnterpriseId(), warningLevel, warningReason);
        } catch (Exception e) {
            log.error("触发风险预警失败", e);
        }
    }

    @Override
    public List<RiskAssessment> getTriggeredWarnings() {
        try {
            return riskAssessmentMapper.selectTriggeredWarnings();
        } catch (Exception e) {
            log.error("获取触发预警的风险评估列表失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> getWarningStatistics() {
        try {
            return riskAssessmentMapper.selectWarningLevelStatistics().stream()
                    .collect(HashMap::new, 
                            (map, item) -> map.put((String) item.get("warningLevel"), item.get("count")), 
                            HashMap::putAll);
        } catch (Exception e) {
            log.error("获取预警统计数据失败", e);
            return new HashMap<>();
        }
    }

    // ==================== 风险因素分析 ====================

    @Override
    public List<String> identifyRiskFactors(String enterpriseId, String assessmentType) {
        try {
            // 这里应该根据企业数据和评估类型识别风险因素
            // 暂时返回模拟数据
            List<String> riskFactors = new ArrayList<>();
            riskFactors.add("流动性不足");
            riskFactors.add("市场竞争激烈");
            riskFactors.add("监管政策变化");
            return riskFactors;
        } catch (Exception e) {
            log.error("识别风险因素失败: {}, {}", enterpriseId, assessmentType, e);
            return new ArrayList<>();
        }
    }

    @Override
    public BigDecimal analyzeRiskCorrelation(String enterpriseId, List<String> riskFactors) {
        try {
            // 这里应该分析风险因素之间的关联度
            // 暂时返回模拟数据
            return new BigDecimal("0.75");
        } catch (Exception e) {
            log.error("分析风险关联度失败: {}", enterpriseId, e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateRiskConcentration(String enterpriseId, String riskType) {
        try {
            // 这里应该计算风险集中度
            // 暂时返回模拟数据
            return new BigDecimal("0.68");
        } catch (Exception e) {
            log.error("计算风险集中度失败: {}, {}", enterpriseId, riskType, e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public String assessRiskImpact(String enterpriseId, String riskType, BigDecimal riskScore) {
        try {
            // 这里应该评估风险影响程度
            // 暂时返回模拟数据
            if (riskScore.compareTo(new BigDecimal("80")) >= 0) {
                return "重大影响";
            } else if (riskScore.compareTo(new BigDecimal("60")) >= 0) {
                return "中等影响";
            } else {
                return "轻微影响";
            }
        } catch (Exception e) {
            log.error("评估风险影响程度失败: {}, {}, {}", enterpriseId, riskType, riskScore, e);
            return "未知影响";
        }
    }

    // ==================== 风险应对策略 ====================

    @Override
    public String developRiskResponseStrategy(RiskAssessment riskAssessment) {
        try {
            String riskLevel = riskAssessment.getOverallRiskLevel();
            
            if (RiskAssessment.RISK_LEVEL_CRITICAL.equals(riskLevel) || 
                RiskAssessment.RISK_LEVEL_VERY_HIGH.equals(riskLevel)) {
                return RiskAssessment.RESPONSE_STRATEGY_MITIGATE;
            } else if (RiskAssessment.RISK_LEVEL_HIGH.equals(riskLevel)) {
                return RiskAssessment.RESPONSE_STRATEGY_MITIGATE;
            } else if (RiskAssessment.RISK_LEVEL_MEDIUM.equals(riskLevel)) {
                return RiskAssessment.RESPONSE_STRATEGY_MONITOR;
            } else {
                return RiskAssessment.RESPONSE_STRATEGY_ACCEPT;
            }
        } catch (Exception e) {
            log.error("制定风险应对策略失败", e);
            return RiskAssessment.RESPONSE_STRATEGY_MONITOR;
        }
    }

    @Override
    public List<String> generateControlMeasureRecommendations(RiskAssessment riskAssessment) {
        try {
            List<String> recommendations = new ArrayList<>();
            
            // 根据风险类型和等级生成建议
            if (riskAssessment.getFinancialRiskScore() != null && 
                riskAssessment.getFinancialRiskScore().compareTo(new BigDecimal("70")) >= 0) {
                recommendations.add("加强财务风险管控，优化资金结构");
                recommendations.add("建立流动性风险预警机制");
            }
            
            if (riskAssessment.getOperationalRiskScore() != null && 
                riskAssessment.getOperationalRiskScore().compareTo(new BigDecimal("70")) >= 0) {
                recommendations.add("完善经营风险管理体系");
                recommendations.add("加强市场风险监控");
            }
            
            return recommendations;
        } catch (Exception e) {
            log.error("生成风险控制措施建议失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public BigDecimal evaluateResponseEffectiveness(String riskAssessmentId) {
        try {
            // 这里应该评估风险应对效果
            // 暂时返回模拟数据
            return new BigDecimal("78.5");
        } catch (Exception e) {
            log.error("评估风险应对效果失败: {}", riskAssessmentId, e);
            return BigDecimal.ZERO;
        }
    }

    // ==================== 标签转换业务 ====================

    @Override
    public String convertAssessmentTypeLabel(String assessmentType) {
        if (assessmentType == null) return "";
        
        switch (assessmentType) {
            case RiskAssessment.ASSESSMENT_TYPE_COMPREHENSIVE:
                return "综合评估";
            case RiskAssessment.ASSESSMENT_TYPE_FINANCIAL:
                return "财务风险评估";
            case RiskAssessment.ASSESSMENT_TYPE_OPERATIONAL:
                return "经营风险评估";
            case RiskAssessment.ASSESSMENT_TYPE_COMPLIANCE:
                return "合规风险评估";
            case RiskAssessment.ASSESSMENT_TYPE_GOVERNANCE:
                return "治理风险评估";
            case RiskAssessment.ASSESSMENT_TYPE_EXTERNAL:
                return "外部风险评估";
            case RiskAssessment.ASSESSMENT_TYPE_SPECIAL:
                return "专项评估";
            default:
                return assessmentType;
        }
    }

    @Override
    public String convertRiskLevelLabel(String riskLevel) {
        if (riskLevel == null) return "";
        
        switch (riskLevel) {
            case RiskAssessment.RISK_LEVEL_VERY_LOW:
                return "极低风险";
            case RiskAssessment.RISK_LEVEL_LOW:
                return "低风险";
            case RiskAssessment.RISK_LEVEL_MEDIUM:
                return "中等风险";
            case RiskAssessment.RISK_LEVEL_HIGH:
                return "高风险";
            case RiskAssessment.RISK_LEVEL_VERY_HIGH:
                return "极高风险";
            case RiskAssessment.RISK_LEVEL_CRITICAL:
                return "临界风险";
            default:
                return riskLevel;
        }
    }

    // ==================== 审核流程业务 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitForReview(String riskAssessmentId, String assessor) {
        try {
            RiskAssessment riskAssessment = getById(riskAssessmentId);
            if (riskAssessment == null) {
                return false;
            }

            riskAssessment.setAssessmentStatus(RiskAssessment.ASSESSMENT_STATUS_COMPLETED);
            riskAssessment.setAssessor(assessor);
            riskAssessment.setReviewStatus("PENDING");
            riskAssessment.setUpdateTime(LocalDateTime.now());

            return updateById(riskAssessment);
        } catch (Exception e) {
            log.error("提交审核失败: {}", riskAssessmentId, e);
            throw new RuntimeException("提交审核失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewRiskAssessment(String riskAssessmentId, String reviewer, String reviewStatus, String reviewComments) {
        try {
            RiskAssessment riskAssessment = getById(riskAssessmentId);
            if (riskAssessment == null) {
                return false;
            }

            riskAssessment.setReviewer(reviewer);
            riskAssessment.setReviewStatus(reviewStatus);
            riskAssessment.setReviewComments(reviewComments);
            riskAssessment.setReviewTime(LocalDateTime.now());
            riskAssessment.setUpdateTime(LocalDateTime.now());

            return updateById(riskAssessment);
        } catch (Exception e) {
            log.error("审核风险评估失败: {}", riskAssessmentId, e);
            throw new RuntimeException("审核风险评估失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveRiskAssessment(String riskAssessmentId, String approver, String approvalComments) {
        try {
            RiskAssessment riskAssessment = getById(riskAssessmentId);
            if (riskAssessment == null) {
                return false;
            }

            riskAssessment.setApprover(approver);
            riskAssessment.setApprovalTime(LocalDateTime.now());
            riskAssessment.setReviewStatus(RiskAssessment.ASSESSMENT_STATUS_APPROVED);
            riskAssessment.setUpdateTime(LocalDateTime.now());

            return updateById(riskAssessment);
        } catch (Exception e) {
            log.error("批准风险评估失败: {}", riskAssessmentId, e);
            throw new RuntimeException("批准风险评估失败: " + e.getMessage());
        }
    }

    @Override
    public List<RiskAssessment> getPendingReview() {
        try {
            return riskAssessmentMapper.selectPendingReview();
        } catch (Exception e) {
            log.error("获取待审核风险评估列表失败", e);
            return new ArrayList<>();
        }
    }

    // ==================== 统计分析业务 ====================

    @Override
    public Map<String, Object> getRiskLevelDistribution() {
        try {
            List<Map<String, Object>> distribution = riskAssessmentMapper.selectRiskLevelDistribution();
            Map<String, Object> result = new HashMap<>();

            for (Map<String, Object> item : distribution) {
                String riskLevel = (String) item.get("riskLevel");
                String label = convertRiskLevelLabel(riskLevel);
                result.put(label, item.get("count"));
            }

            return result;
        } catch (Exception e) {
            log.error("获取风险等级分布统计失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public List<Map<String, Object>> getRiskTrendAnalysis(String enterpriseId, Integer months) {
        try {
            return riskAssessmentMapper.selectRiskTrendAnalysis(enterpriseId, months);
        } catch (Exception e) {
            log.error("获取风险趋势分析数据失败: {}, {}", enterpriseId, months, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getIndustryRiskComparison(String enterpriseId, String industryType) {
        try {
            return riskAssessmentMapper.selectPeerRiskComparison(enterpriseId, industryType);
        } catch (Exception e) {
            log.error("获取行业风险对比数据失败: {}, {}", enterpriseId, industryType, e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> getComprehensiveStatistics() {
        try {
            return riskAssessmentMapper.selectComprehensiveStatistics();
        } catch (Exception e) {
            log.error("获取综合统计数据失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getEnterpriseRiskOverview(String enterpriseId) {
        try {
            return riskAssessmentMapper.selectEnterpriseRiskOverview(enterpriseId);
        } catch (Exception e) {
            log.error("获取企业风险概览失败: {}", enterpriseId, e);
            return new HashMap<>();
        }
    }

    // ==================== 报告生成业务 ====================

    @Override
    public Map<String, Object> generateAssessmentReport(String enterpriseId, Integer assessmentYear) {
        try {
            List<Map<String, Object>> reportData = riskAssessmentMapper.selectForReport(enterpriseId, assessmentYear);

            Map<String, Object> report = new HashMap<>();
            report.put("enterpriseId", enterpriseId);
            report.put("assessmentYear", assessmentYear);
            report.put("reportData", reportData);
            report.put("generateTime", LocalDateTime.now());

            return report;
        } catch (Exception e) {
            log.error("生成风险评估报告失败: {}, {}", enterpriseId, assessmentYear, e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> generateRiskAnalysisReport(String enterpriseId, LocalDate startDate, LocalDate endDate) {
        try {
            Map<String, Object> report = new HashMap<>();

            // 获取期间内的风险评估数据
            RiskAssessmentQueryVO query = new RiskAssessmentQueryVO();
            query.setEnterpriseId(enterpriseId);
            query.setAssessmentDateStart(startDate);
            query.setAssessmentDateEnd(endDate);

            List<RiskAssessment> assessments = riskAssessmentMapper.selectByCondition(query);

            report.put("enterpriseId", enterpriseId);
            report.put("startDate", startDate);
            report.put("endDate", endDate);
            report.put("assessments", assessments);
            report.put("generateTime", LocalDateTime.now());

            return report;
        } catch (Exception e) {
            log.error("生成风险分析报告失败: {}, {}, {}", enterpriseId, startDate, endDate, e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> generateWarningReport(String enterpriseId, Integer days) {
        try {
            List<RiskAssessment> warnings = riskAssessmentMapper.selectRecentWarnings(days);

            Map<String, Object> report = new HashMap<>();
            report.put("enterpriseId", enterpriseId);
            report.put("days", days);
            report.put("warnings", warnings);
            report.put("generateTime", LocalDateTime.now());

            return report;
        } catch (Exception e) {
            log.error("生成预警报告失败: {}, {}", enterpriseId, days, e);
            return new HashMap<>();
        }
    }

    // ==================== 数据质量业务 ====================

    @Override
    public boolean validateAssessmentDataQuality(RiskAssessment riskAssessment) {
        try {
            BigDecimal completenessScore = calculateDataCompletenessScore(riskAssessment);
            BigDecimal accuracyScore = calculateDataAccuracyScore(riskAssessment);
            BigDecimal timelinessScore = calculateDataTimelinessScore(riskAssessment);
            BigDecimal consistencyScore = calculateDataConsistencyScore(riskAssessment);

            // 计算总体质量评分
            BigDecimal overallScore = completenessScore.add(accuracyScore)
                    .add(timelinessScore).add(consistencyScore)
                    .divide(new BigDecimal("4"), 2, RoundingMode.HALF_UP);

            // 质量评分大于70分认为合格
            return overallScore.compareTo(new BigDecimal("70")) >= 0;
        } catch (Exception e) {
            log.error("验证评估数据质量失败", e);
            return false;
        }
    }

    @Override
    public BigDecimal calculateDataCompletenessScore(RiskAssessment riskAssessment) {
        try {
            int totalFields = 20; // 关键字段总数
            int completedFields = 0;

            if (riskAssessment.getEnterpriseId() != null) completedFields++;
            if (riskAssessment.getAssessmentType() != null) completedFields++;
            if (riskAssessment.getAssessmentDate() != null) completedFields++;
            if (riskAssessment.getFinancialRiskScore() != null) completedFields++;
            if (riskAssessment.getOperationalRiskScore() != null) completedFields++;
            // ... 检查其他关键字段

            return new BigDecimal(completedFields * 100 / totalFields);
        } catch (Exception e) {
            log.error("计算数据完整性评分失败", e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateDataAccuracyScore(RiskAssessment riskAssessment) {
        try {
            // 这里应该实现数据准确性检查逻辑
            // 暂时返回模拟数据
            return new BigDecimal("85.0");
        } catch (Exception e) {
            log.error("计算数据准确性评分失败", e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateDataTimelinessScore(RiskAssessment riskAssessment) {
        try {
            // 这里应该实现数据及时性检查逻辑
            // 暂时返回模拟数据
            return new BigDecimal("90.0");
        } catch (Exception e) {
            log.error("计算数据及时性评分失败", e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateDataConsistencyScore(RiskAssessment riskAssessment) {
        try {
            // 这里应该实现数据一致性检查逻辑
            // 暂时返回模拟数据
            return new BigDecimal("88.0");
        } catch (Exception e) {
            log.error("计算数据一致性评分失败", e);
            return BigDecimal.ZERO;
        }
    }

    // ==================== 批量操作业务 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RiskAssessment> batchPerformAssessment(List<String> enterpriseIds, String assessmentType) {
        try {
            List<RiskAssessment> results = new ArrayList<>();

            for (String enterpriseId : enterpriseIds) {
                RiskAssessment assessment = performRiskAssessment(enterpriseId, assessmentType, RiskAssessment.ASSESSMENT_METHOD_QUANTITATIVE);
                results.add(assessment);
            }

            return results;
        } catch (Exception e) {
            log.error("批量执行风险评估失败", e);
            throw new RuntimeException("批量执行风险评估失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<String> riskAssessmentIds, String status, String updateBy) {
        try {
            return riskAssessmentMapper.batchUpdateStatus(riskAssessmentIds, status, updateBy) > 0;
        } catch (Exception e) {
            log.error("批量更新评估状态失败", e);
            throw new RuntimeException("批量更新评估状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchReview(List<String> riskAssessmentIds, String reviewer, String reviewStatus, String reviewComments) {
        try {
            return riskAssessmentMapper.batchReview(riskAssessmentIds, reviewStatus, reviewer, reviewComments) > 0;
        } catch (Exception e) {
            log.error("批量审核风险评估失败", e);
            throw new RuntimeException("批量审核风险评估失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<String> riskAssessmentIds, String updateBy) {
        try {
            return riskAssessmentMapper.batchDelete(riskAssessmentIds, updateBy) > 0;
        } catch (Exception e) {
            log.error("批量删除风险评估失败", e);
            throw new RuntimeException("批量删除风险评估失败: " + e.getMessage());
        }
    }

    // ==================== 导出业务 ====================

    @Override
    public List<Map<String, Object>> exportAssessmentData(RiskAssessmentQueryVO query) {
        try {
            return riskAssessmentMapper.selectForExport(query);
        } catch (Exception e) {
            log.error("导出风险评估数据失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public byte[] exportAssessmentReport(String enterpriseId, Integer assessmentYear, String format) {
        try {
            // 这里应该实现报告导出逻辑
            // 暂时返回空数组
            return new byte[0];
        } catch (Exception e) {
            log.error("导出风险评估报告失败: {}, {}, {}", enterpriseId, assessmentYear, format, e);
            return new byte[0];
        }
    }

    // ==================== 其他标签转换方法 ====================

    @Override
    public String convertAssessmentMethodLabel(String assessmentMethod) {
        if (assessmentMethod == null) return "";

        switch (assessmentMethod) {
            case RiskAssessment.ASSESSMENT_METHOD_QUANTITATIVE:
                return "定量评估";
            case RiskAssessment.ASSESSMENT_METHOD_QUALITATIVE:
                return "定性评估";
            case RiskAssessment.ASSESSMENT_METHOD_MIXED:
                return "混合评估";
            case RiskAssessment.ASSESSMENT_METHOD_MODEL:
                return "模型评估";
            case RiskAssessment.ASSESSMENT_METHOD_EXPERT:
                return "专家评估";
            default:
                return assessmentMethod;
        }
    }

    @Override
    public String convertAssessmentStatusLabel(String assessmentStatus) {
        if (assessmentStatus == null) return "";

        switch (assessmentStatus) {
            case RiskAssessment.ASSESSMENT_STATUS_DRAFT:
                return "草稿";
            case RiskAssessment.ASSESSMENT_STATUS_IN_PROGRESS:
                return "评估中";
            case RiskAssessment.ASSESSMENT_STATUS_COMPLETED:
                return "已完成";
            case RiskAssessment.ASSESSMENT_STATUS_REVIEWED:
                return "已审核";
            case RiskAssessment.ASSESSMENT_STATUS_APPROVED:
                return "已批准";
            case RiskAssessment.ASSESSMENT_STATUS_REJECTED:
                return "已拒绝";
            default:
                return assessmentStatus;
        }
    }

    @Override
    public String convertRiskTrendLabel(String riskTrend) {
        if (riskTrend == null) return "";

        switch (riskTrend) {
            case RiskAssessment.RISK_TREND_DECREASING:
                return "下降趋势";
            case RiskAssessment.RISK_TREND_STABLE:
                return "稳定趋势";
            case RiskAssessment.RISK_TREND_INCREASING:
                return "上升趋势";
            case RiskAssessment.RISK_TREND_VOLATILE:
                return "波动趋势";
            default:
                return riskTrend;
        }
    }

    @Override
    public String convertWarningLevelLabel(String warningLevel) {
        if (warningLevel == null) return "";

        switch (warningLevel) {
            case RiskAssessment.WARNING_LEVEL_GREEN:
                return "绿色预警";
            case RiskAssessment.WARNING_LEVEL_YELLOW:
                return "黄色预警";
            case RiskAssessment.WARNING_LEVEL_ORANGE:
                return "橙色预警";
            case RiskAssessment.WARNING_LEVEL_RED:
                return "红色预警";
            default:
                return warningLevel;
        }
    }

    @Override
    public String convertResponseStrategyLabel(String responseStrategy) {
        if (responseStrategy == null) return "";

        switch (responseStrategy) {
            case RiskAssessment.RESPONSE_STRATEGY_ACCEPT:
                return "接受风险";
            case RiskAssessment.RESPONSE_STRATEGY_AVOID:
                return "规避风险";
            case RiskAssessment.RESPONSE_STRATEGY_MITIGATE:
                return "缓解风险";
            case RiskAssessment.RESPONSE_STRATEGY_TRANSFER:
                return "转移风险";
            case RiskAssessment.RESPONSE_STRATEGY_MONITOR:
                return "监控风险";
            default:
                return responseStrategy;
        }
    }

}
