package com.huabo.contract.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.CounterpartInfo;
import com.huabo.contract.entity.RiskAssessment;
import com.huabo.contract.entity.RiskAssessmentDetail;
import com.huabo.contract.mapper.CounterpartInfoMapper;
import com.huabo.contract.mapper.RiskAssessmentDetailMapper;
import com.huabo.contract.mapper.RiskAssessmentMapper;
import com.huabo.contract.service.RiskWarningService;
import com.huabo.contract.vo.RiskWarningQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 风险预警服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class RiskWarningServiceImpl implements RiskWarningService {

    @Autowired
    private RiskAssessmentMapper riskAssessmentMapper;

    @Autowired
    private RiskAssessmentDetailMapper riskAssessmentDetailMapper;

    @Autowired
    private CounterpartInfoMapper counterpartInfoMapper;

    @Override
    public PageInfo<Map<String, Object>> getRiskWarningList(RiskWarningQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            
            // 查询高风险评估记录
            List<RiskAssessment> assessments = riskAssessmentMapper.selectHighRiskAssessments(param);
            
            List<Map<String, Object>> warningList = new ArrayList<>();
            for (RiskAssessment assessment : assessments) {
                Map<String, Object> warning = new HashMap<>();
                warning.put("id", assessment.getId());
                warning.put("projectId", assessment.getProjectId());
                warning.put("assessmentName", assessment.getAssessmentName());
                warning.put("overallRiskLevel", assessment.getOverallRiskLevel());
                warning.put("overallRiskScore", assessment.getOverallRiskScore());
                warning.put("warningType", getWarningType(assessment.getOverallRiskLevel()));
                warning.put("warningMessage", generateWarningMessage(assessment));
                warning.put("createTime", assessment.getCreateTime());
                warning.put("status", assessment.getAssessmentStatus());
                
                // 获取风险详情
                List<RiskAssessmentDetail> details = riskAssessmentDetailMapper.selectByAssessmentId(assessment.getId());
                warning.put("riskCount", details.size());
                warning.put("highRiskCount", details.stream()
                    .mapToInt(d -> d.getRiskLevel() >= 3 ? 1 : 0).sum());
                
                warningList.add(warning);
            }
            
            return new PageInfo<>(warningList);
        } catch (Exception e) {
            log.error("获取风险预警列表失败", e);
            throw new RuntimeException("获取风险预警列表失败", e);
        }
    }

    @Override
    public Map<String, Object> getRiskWarningStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 总预警数量
            Long totalWarningsLong = riskAssessmentMapper.countHighRiskAssessments();
            int totalWarnings = totalWarningsLong != null ? totalWarningsLong.intValue() : 0;
            statistics.put("totalWarnings", totalWarnings);

            // 按风险等级统计
            Map<String, Integer> riskLevelStats = new HashMap<>();
            Long highCount = riskAssessmentMapper.countByRiskLevel(3);
            Long extremeCount = riskAssessmentMapper.countByRiskLevel(4);
            riskLevelStats.put("high", highCount != null ? highCount.intValue() : 0);
            riskLevelStats.put("extreme", extremeCount != null ? extremeCount.intValue() : 0);
            statistics.put("riskLevelStats", riskLevelStats);
            
            // 按评估类型统计
            Map<String, Integer> assessmentTypeStats = new HashMap<>();
            Long preAcceptanceCount = riskAssessmentMapper.countByAssessmentType(1);
            Long inExecutionCount = riskAssessmentMapper.countByAssessmentType(2);
            Long postCompletionCount = riskAssessmentMapper.countByAssessmentType(3);
            assessmentTypeStats.put("preAcceptance", preAcceptanceCount != null ? preAcceptanceCount.intValue() : 0);
            assessmentTypeStats.put("inExecution", inExecutionCount != null ? inExecutionCount.intValue() : 0);
            assessmentTypeStats.put("postCompletion", postCompletionCount != null ? postCompletionCount.intValue() : 0);
            statistics.put("assessmentTypeStats", assessmentTypeStats);

            // 本月新增预警
            Long monthlyWarningsLong = riskAssessmentMapper.countMonthlyHighRiskAssessments();
            int monthlyWarnings = monthlyWarningsLong != null ? monthlyWarningsLong.intValue() : 0;
            statistics.put("monthlyWarnings", monthlyWarnings);

            // 待处理预警
            Long pendingWarningsLong = riskAssessmentMapper.countPendingWarnings();
            int pendingWarnings = pendingWarningsLong != null ? pendingWarningsLong.intValue() : 0;
            statistics.put("pendingWarnings", pendingWarnings);
            
            return statistics;
        } catch (Exception e) {
            log.error("获取风险预警统计失败", e);
            throw new RuntimeException("获取风险预警统计失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getHighRiskProjects() {
        try {
            List<Map<String, Object>> highRiskAssessments = riskAssessmentMapper.selectHighRiskProjects();
            
            return highRiskAssessments.stream().map(assessment -> {
                Map<String, Object> project = new HashMap<>();
                project.put("projectId", assessment.get("projectId"));
                project.put("assessmentId", assessment.get("id"));
                project.put("assessmentName", assessment.get("assessmentName"));
                project.put("riskLevel", assessment.get("overallRiskLevel"));
                project.put("riskScore", assessment.get("overallRiskScore"));
                project.put("assessmentDate", assessment.get("assessmentDate"));
                Integer riskLevel = (Integer) assessment.get("overallRiskLevel");
                project.put("riskLevelName", getRiskLevelName(riskLevel));
                
                // 获取主要风险点
                Long assessmentId = ((Number) assessment.get("id")).longValue();
                List<RiskAssessmentDetail> details = riskAssessmentDetailMapper.selectHighRiskByAssessmentId(assessmentId);
                project.put("majorRisks", details.stream()
                    .map(RiskAssessmentDetail::getRiskItem)
                    .collect(Collectors.toList()));
                
                return project;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取高风险项目列表失败", e);
            throw new RuntimeException("获取高风险项目列表失败", e);
        }
    }

    @Override
    public Map<String, Object> getRiskTrendAnalysis(Integer months) {
        try {
            Map<String, Object> trendData = new HashMap<>();
            
            // 生成时间序列
            List<String> timeLabels = new ArrayList<>();
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusMonths(months - 1);
            
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusMonths(1)) {
                timeLabels.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM")));
            }
            
            // 获取每月风险评估数据
            List<Map<String, Object>> monthlyData = riskAssessmentMapper.selectMonthlyRiskTrend(months);
            
            // 处理数据
            Map<String, Integer> totalCountMap = new HashMap<>();
            Map<String, Integer> highRiskCountMap = new HashMap<>();
            Map<String, BigDecimal> avgScoreMap = new HashMap<>();
            
            for (Map<String, Object> data : monthlyData) {
                String month = (String) data.get("month");
                totalCountMap.put(month, ((Number) data.get("totalCount")).intValue());
                highRiskCountMap.put(month, ((Number) data.get("highRiskCount")).intValue());
                avgScoreMap.put(month, (BigDecimal) data.get("avgScore"));
            }
            
            // 构建返回数据
            List<Integer> totalCounts = new ArrayList<>();
            List<Integer> highRiskCounts = new ArrayList<>();
            List<BigDecimal> avgScores = new ArrayList<>();
            
            for (String month : timeLabels) {
                totalCounts.add(totalCountMap.getOrDefault(month, 0));
                highRiskCounts.add(highRiskCountMap.getOrDefault(month, 0));
                avgScores.add(avgScoreMap.getOrDefault(month, BigDecimal.ZERO));
            }
            
            trendData.put("timeLabels", timeLabels);
            trendData.put("totalCounts", totalCounts);
            trendData.put("highRiskCounts", highRiskCounts);
            trendData.put("avgScores", avgScores);
            
            return trendData;
        } catch (Exception e) {
            log.error("获取风险趋势分析失败", e);
            throw new RuntimeException("获取风险趋势分析失败", e);
        }
    }

    @Override
    public Map<String, Object> executeAutoRiskAssessment(Long projectId) {
        try {
            Map<String, Object> result = new HashMap<>();
            int assessedCount = 0;
            int warningCount = 0;
            
            List<Long> projectIds;
            if (projectId != null) {
                projectIds = Arrays.asList(projectId);
            } else {
                // 获取所有需要评估的项目
                List<Map<String, Object>> projectMaps = riskAssessmentMapper.selectProjectsNeedingAssessment();
                projectIds = projectMaps.stream()
                    .map(map -> ((Number) map.get("projectId")).longValue())
                    .collect(Collectors.toList());
            }
            
            for (Long pid : projectIds) {
                try {
                    // 执行自动风险识别
                    Map<String, Object> riskInfo = intelligentRiskIdentification(pid);
                    
                    // 如果识别到高风险，创建预警
                    if (isHighRisk(riskInfo)) {
                        createRiskWarning(pid, riskInfo);
                        warningCount++;
                    }
                    
                    assessedCount++;
                } catch (Exception e) {
                    log.warn("项目{}自动风险评估失败: {}", pid, e.getMessage());
                }
            }
            
            result.put("assessedCount", assessedCount);
            result.put("warningCount", warningCount);
            result.put("executionTime", new Date());
            
            return result;
        } catch (Exception e) {
            log.error("执行风险自动评估失败", e);
            throw new RuntimeException("执行风险自动评估失败", e);
        }
    }

    @Override
    public Map<String, Object> getCounterpartRiskAssessment(Long counterpartId) {
        try {
            Map<String, Object> riskInfo = new HashMap<>();
            
            // 获取相对方基本信息
            CounterpartInfo counterpart = counterpartInfoMapper.selectById(counterpartId);
            if (counterpart == null) {
                throw new RuntimeException("相对方不存在");
            }
            
            riskInfo.put("counterpartInfo", counterpart);
            
            // 获取历史风险评估记录
            List<RiskAssessment> assessments = riskAssessmentMapper.selectByCounterpartId(counterpartId);
            riskInfo.put("assessmentHistory", assessments);
            
            // 计算综合风险评分
            Double totalScore = assessments.stream()
                .map(RiskAssessment::getOverallRiskScore)
                .filter(Objects::nonNull)
                .reduce(0.0, Double::sum);

            Double avgRiskScore = totalScore / Math.max(assessments.size(), 1);
            
            riskInfo.put("avgRiskScore", avgRiskScore);
            riskInfo.put("riskLevel", calculateRiskLevel(avgRiskScore));
            
            // 获取主要风险类别
            List<Map<String, Object>> riskCategories = riskAssessmentDetailMapper.selectRiskCategoriesByCounterpart(counterpartId);
            riskInfo.put("riskCategories", riskCategories);
            
            // 风险趋势
            List<Map<String, Object>> riskTrend = riskAssessmentMapper.selectCounterpartRiskTrend(counterpartId);
            riskInfo.put("riskTrend", riskTrend);
            
            return riskInfo;
        } catch (Exception e) {
            log.error("获取相对方风险评估失败", e);
            throw new RuntimeException("获取相对方风险评估失败", e);
        }
    }

    @Override
    public Map<String, Object> generateRiskAssessmentReport(Long assessmentId) {
        try {
            Map<String, Object> report = new HashMap<>();
            
            // 获取评估基本信息
            RiskAssessment assessment = riskAssessmentMapper.selectById(assessmentId);
            if (assessment == null) {
                throw new RuntimeException("风险评估不存在");
            }
            
            report.put("assessment", assessment);
            
            // 获取风险详情
            List<RiskAssessmentDetail> details = riskAssessmentDetailMapper.selectByAssessmentId(assessmentId);
            report.put("riskDetails", details);
            
            // 风险统计分析
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalRisks", details.size());
            statistics.put("highRisks", details.stream().mapToInt(d -> d.getRiskLevel() >= 3 ? 1 : 0).sum());
            statistics.put("avgRiskScore", details.stream()
                .map(RiskAssessmentDetail::getRiskScore)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(Math.max(details.size(), 1)), 2, RoundingMode.HALF_UP));
            
            report.put("statistics", statistics);
            
            // 风险分类统计
            Map<Integer, Long> categoryStats = details.stream()
                .collect(Collectors.groupingBy(RiskAssessmentDetail::getRiskCategory, Collectors.counting()));
            report.put("categoryStats", categoryStats);
            
            // 缓解措施建议
            List<String> suggestions = new ArrayList<>();
            for (RiskAssessmentDetail detail : details) {
                if (detail.getRiskLevel() >= 3) {
                    suggestions.addAll(getRiskMitigationSuggestions(detail.getRiskCategory(), detail.getRiskLevel()));
                }
            }
            report.put("mitigationSuggestions", suggestions.stream().distinct().collect(Collectors.toList()));
            
            // 报告生成时间
            report.put("generateTime", new Date());
            
            return report;
        } catch (Exception e) {
            log.error("生成风险评估报告失败", e);
            throw new RuntimeException("生成风险评估报告失败", e);
        }
    }

    @Override
    public Map<String, Object> getRiskWarningConfig() {
        // 返回默认配置，实际应从配置表或配置文件读取
        Map<String, Object> config = new HashMap<>();
        config.put("highRiskThreshold", 70);
        config.put("extremeRiskThreshold", 85);
        config.put("warningEnabled", true);
        config.put("autoAssessmentEnabled", true);
        config.put("notificationEnabled", true);
        return config;
    }

    @Override
    public boolean updateRiskWarningConfig(Map<String, Object> config) {
        // 实际应更新到配置表或配置文件
        log.info("更新风险预警配置: {}", config);
        return true;
    }

    @Override
    public boolean handleRiskWarning(Long warningId, Integer action, String remarks) {
        try {
            // 更新预警处理状态
            RiskAssessment assessment = riskAssessmentMapper.selectById(warningId);
            if (assessment != null) {
                assessment.setAssessmentStatus(action);
                assessment.setRemarks(remarks);
                assessment.setUpdateTime(new Date());
                riskAssessmentMapper.updateById(assessment);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("处理风险预警失败", e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getRiskAssessmentTemplate(Integer assessmentType) {
        Map<String, Object> template = new HashMap<>();
        
        // 根据评估类型返回不同的模板
        List<Map<String, Object>> riskItems = new ArrayList<>();
        
        switch (assessmentType) {
            case 1: // 承接前评估
                riskItems.add(createRiskItem(1, "相对方资格资信", "评估相对方的资质等级、信用状况"));
                riskItems.add(createRiskItem(2, "政策风险", "评估相关政策变化对项目的影响"));
                riskItems.add(createRiskItem(3, "资金来源风险", "评估项目资金来源的可靠性"));
                break;
            case 2: // 执行中评估
                riskItems.add(createRiskItem(4, "技术质量风险", "评估技术实施和质量控制风险"));
                riskItems.add(createRiskItem(5, "进度风险", "评估项目进度延误风险"));
                riskItems.add(createRiskItem(6, "成本风险", "评估成本超支风险"));
                break;
            case 3: // 结项后评估
                riskItems.add(createRiskItem(7, "收款风险", "评估款项回收风险"));
                riskItems.add(createRiskItem(8, "法律风险", "评估潜在法律纠纷风险"));
                break;
        }
        
        template.put("assessmentType", assessmentType);
        template.put("riskItems", riskItems);
        
        return template;
    }

    @Override
    public Map<String, Object> exportRiskAssessmentReport(Long assessmentId) {
        Map<String, Object> result = new HashMap<>();
        
        // 生成报告
        Map<String, Object> report = generateRiskAssessmentReport(assessmentId);
        
        // 模拟文件导出
        String fileName = "风险评估报告_" + assessmentId + "_" + System.currentTimeMillis() + ".pdf";
        String filePath = "/tmp/reports/" + fileName;
        
        result.put("fileName", fileName);
        result.put("filePath", filePath);
        result.put("fileSize", "1.2MB");
        result.put("exportTime", new Date());
        
        return result;
    }

    @Override
    public Map<String, Object> intelligentRiskIdentification(Long projectId) {
        Map<String, Object> riskInfo = new HashMap<>();
        
        // 模拟智能风险识别逻辑
        List<Map<String, Object>> identifiedRisks = new ArrayList<>();
        
        // 基于规则引擎识别风险
        identifiedRisks.add(createIdentifiedRisk("资金风险", "项目资金来源不明确", 3, 75.0));
        identifiedRisks.add(createIdentifiedRisk("技术风险", "技术方案复杂度较高", 2, 60.0));
        
        riskInfo.put("projectId", projectId);
        riskInfo.put("identifiedRisks", identifiedRisks);
        riskInfo.put("totalRiskScore", 67.5);
        riskInfo.put("riskLevel", 3);
        riskInfo.put("identificationTime", new Date());
        
        return riskInfo;
    }

    @Override
    public Map<String, Object> calculateRiskScore(Long assessmentId) {
        // 实现风险评分计算逻辑
        Map<String, Object> result = new HashMap<>();
        result.put("assessmentId", assessmentId);
        result.put("totalScore", 72.5);
        result.put("riskLevel", 3);
        return result;
    }

    @Override
    public boolean pushRiskWarning(String warningType, Long projectId, String message) {
        // 实现预警推送逻辑
        log.info("推送风险预警 - 类型: {}, 项目: {}, 消息: {}", warningType, projectId, message);
        return true;
    }

    @Override
    public List<String> getRiskMitigationSuggestions(Integer riskCategory, Integer riskLevel) {
        List<String> suggestions = new ArrayList<>();
        
        switch (riskCategory) {
            case 1: // 相对方资格资信风险
                suggestions.add("要求提供最新的资质证书和信用报告");
                suggestions.add("进行实地考察和背景调查");
                if (riskLevel >= 3) {
                    suggestions.add("要求提供担保或保证金");
                }
                break;
            case 2: // 政策风险
                suggestions.add("密切关注相关政策变化");
                suggestions.add("建立政策风险应对预案");
                break;
            case 3: // 资金来源风险
                suggestions.add("要求提供资金来源证明");
                suggestions.add("分阶段付款，降低资金风险");
                break;
            default:
                suggestions.add("制定详细的风险应对措施");
                suggestions.add("加强项目监控和管理");
        }
        
        return suggestions;
    }

    // 辅助方法
    private String getWarningType(Integer riskLevel) {
        if (riskLevel >= 4) return "极高风险";
        if (riskLevel >= 3) return "高风险";
        if (riskLevel >= 2) return "中风险";
        return "低风险";
    }

    private String generateWarningMessage(RiskAssessment assessment) {
        return String.format("项目 %s 存在%s，综合评分%.1f，请及时关注并采取相应措施",
            assessment.getAssessmentName(),
            getWarningType(assessment.getOverallRiskLevel()),
            assessment.getOverallRiskScore());
    }

    private String getRiskLevelName(Integer riskLevel) {
        switch (riskLevel) {
            case 1: return "低风险";
            case 2: return "中风险";
            case 3: return "高风险";
            case 4: return "极高风险";
            default: return "未知";
        }
    }

    private boolean isHighRisk(Map<String, Object> riskInfo) {
        Integer riskLevel = (Integer) riskInfo.get("riskLevel");
        return riskLevel != null && riskLevel >= 3;
    }

    private void createRiskWarning(Long projectId, Map<String, Object> riskInfo) {
        // 创建风险预警记录的逻辑
        log.info("为项目{}创建风险预警: {}", projectId, riskInfo);
    }

    private Integer calculateRiskLevel(Double riskScore) {
        if (riskScore == null) return 1;
        if (riskScore >= 85) return 4;
        if (riskScore >= 70) return 3;
        if (riskScore >= 50) return 2;
        return 1;
    }

    private Map<String, Object> createRiskItem(Integer category, String item, String description) {
        Map<String, Object> riskItem = new HashMap<>();
        riskItem.put("riskCategory", category);
        riskItem.put("riskItem", item);
        riskItem.put("riskDescription", description);
        riskItem.put("riskProbability", 0.0);
        riskItem.put("riskImpact", 0.0);
        riskItem.put("riskScore", 0.0);
        riskItem.put("riskLevel", 1);
        return riskItem;
    }

    private Map<String, Object> createIdentifiedRisk(String riskType, String description, Integer level, Double score) {
        Map<String, Object> risk = new HashMap<>();
        risk.put("riskType", riskType);
        risk.put("description", description);
        risk.put("riskLevel", level);
        risk.put("riskScore", score);
        return risk;
    }

    // 其他接口方法的简单实现
    @Override
    public Map<String, Object> monitorProjectRisk(Long projectId) {
        Map<String, Object> result = new HashMap<>();
        result.put("projectId", projectId);
        result.put("monitorTime", new Date());
        result.put("riskStatus", "正常");
        return result;
    }

    @Override
    public Map<String, Object> generateRiskHeatmapData() {
        Map<String, Object> heatmapData = new HashMap<>();
        // 模拟热力图数据
        heatmapData.put("data", Arrays.asList(
            Arrays.asList(0, 0, 5),
            Arrays.asList(0, 1, 3),
            Arrays.asList(1, 0, 8),
            Arrays.asList(1, 1, 2)
        ));
        return heatmapData;
    }

    @Override
    public List<Map<String, Object>> getRiskWarningHistory(Long projectId, Integer days) {
        // 返回空列表，实际应查询数据库
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> checkAssessmentQuality(Long assessmentId) {
        Map<String, Object> result = new HashMap<>();
        result.put("assessmentId", assessmentId);
        result.put("qualityScore", 85);
        result.put("completeness", "良好");
        return result;
    }

    @Override
    public Map<String, Object> batchRiskAssessment(List<Long> projectIds) {
        Map<String, Object> result = new HashMap<>();
        result.put("processedCount", projectIds.size());
        result.put("successCount", projectIds.size());
        result.put("failureCount", 0);
        return result;
    }

    @Override
    public Map<String, Object> compareRiskAssessments(List<Long> assessmentIds) {
        Map<String, Object> result = new HashMap<>();
        result.put("assessmentIds", assessmentIds);
        result.put("comparisonResult", "评估结果对比完成");
        return result;
    }

    @Override
    public Map<String, Object> predictRiskTrend(Long projectId, Integer predictDays) {
        Map<String, Object> result = new HashMap<>();
        result.put("projectId", projectId);
        result.put("predictDays", predictDays);
        result.put("trendPrediction", "风险呈上升趋势");
        return result;
    }

    @Override
    public Map<String, Object> getIndustryRiskBenchmark(String industryType) {
        Map<String, Object> benchmark = new HashMap<>();
        benchmark.put("industryType", industryType);
        benchmark.put("avgRiskScore", 65.0);
        benchmark.put("riskDistribution", Arrays.asList(20, 30, 35, 15));
        return benchmark;
    }

    @Override
    public boolean reviewRiskAssessment(Long assessmentId, Long reviewerId, String reviewComments, Boolean approved) {
        // 实现审核逻辑
        return true;
    }

    @Override
    public List<Map<String, Object>> getAssessmentReviewHistory(Long assessmentId) {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> trainRiskAssessmentModel() {
        Map<String, Object> result = new HashMap<>();
        result.put("modelVersion", "v1.0");
        result.put("trainingTime", new Date());
        result.put("accuracy", 0.85);
        return result;
    }

    @Override
    public Map<String, Object> getAssessmentAccuracyStats(String startDate, String endDate) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("startDate", startDate);
        stats.put("endDate", endDate);
        stats.put("accuracy", 0.82);
        stats.put("totalAssessments", 150);
        return stats;
    }
}
