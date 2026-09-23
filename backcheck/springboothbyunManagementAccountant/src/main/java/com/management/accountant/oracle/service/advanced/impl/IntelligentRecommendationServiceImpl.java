package com.management.accountant.oracle.service.advanced.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.advanced.IntelligentRecommendation;
import com.management.accountant.oracle.mapper.advanced.IntelligentRecommendationMapper;
import com.management.accountant.oracle.service.advanced.IntelligentRecommendationService;
import com.management.accountant.util.PageResult;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 智能推荐Service实现类
 * 
 * @description 智能推荐业务逻辑实现
 * @author AI Assistant
 * @date 2026-02-06
 */
@Slf4j
@Service("intelligentRecommendationServiceOracle")
public class IntelligentRecommendationServiceImpl implements IntelligentRecommendationService {

    @Resource
    private IntelligentRecommendationMapper recommendationMapper;

    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public Map<String, Object> getStats(String companyId) {
        try {
            Map<String, Object> stats = recommendationMapper.getStatistics(companyId);
            if (stats == null) {
                stats = new HashMap<>();
                stats.put("totalRecommendations", 0);
                stats.put("acceptedCount", 0);
                stats.put("pendingCount", 0);
                stats.put("rejectedCount", 0);
                stats.put("avgConfidenceScore", 0);
            }
            return stats;
        } catch (Exception e) {
            log.error("获取智能推荐统计数据失败", e);
            throw new ServiceException("获取统计数据失败：" + e.getMessage());
        }
    }

    @Override
    public PageResult<IntelligentRecommendation> getRecommendationList(String keyword, String status,
                                                                        String companyId, Integer pageNo,
                                                                        Integer pageSize) {
        try {
            // 计算偏移量
            int offset = (pageNo - 1) * pageSize;

            // 查询列表
            List<IntelligentRecommendation> list = recommendationMapper.selectPageList(
                    keyword, status, companyId, offset, pageSize);

            // 查询总数
            Integer total = recommendationMapper.countByCondition(keyword, status, companyId);

            // 构建分页结果
            PageResult<IntelligentRecommendation> pageResult = new PageResult<>();
            pageResult.setList(list);
            pageResult.setTotal(total);
            pageResult.setPageNo(pageNo);
            pageResult.setPageSize(pageSize);

            return pageResult;
        } catch (Exception e) {
            log.error("获取智能推荐列表失败", e);
            throw new ServiceException("获取推荐列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> generateRecommendation(String budgetId, String recommendationType,
                                                      String companyId, String userId, String userName) {
        try {
            // 生成推荐ID
            String recommendationId = "REC" + idWorker.nextId();

            // 创建推荐实体
            IntelligentRecommendation recommendation = new IntelligentRecommendation();
            recommendation.setRecommendationId(recommendationId);
            recommendation.setBudgetId(budgetId);
            recommendation.setRecommendationType(recommendationType);
            recommendation.setStatus("pending");
            recommendation.setCompanyId(companyId);
            recommendation.setCreatedBy(userId);
            recommendation.setCreatorName(userName);
            recommendation.setCreateTime(new Date());

            // 根据推荐类型生成不同的推荐内容
            Map<String, Object> generatedContent = generateRecommendationContent(
                    budgetId, recommendationType, companyId);

            recommendation.setRecommendationName((String) generatedContent.get("name"));
            recommendation.setBudgetName((String) generatedContent.get("budgetName"));
            recommendation.setRecommendationContent(
                    JSON.toJSONString(generatedContent.get("content")));
            recommendation.setAnalysisData(
                    JSON.toJSONString(generatedContent.get("analysisData")));
            recommendation.setExpectedImpact((String) generatedContent.get("expectedImpact"));
            recommendation.setConfidenceScore((BigDecimal) generatedContent.get("confidenceScore"));

            // 保存到数据库
            recommendationMapper.insert(recommendation);

            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("recommendationId", recommendationId);
            result.put("suggestions", generatedContent.get("content"));
            result.put("confidenceScore", generatedContent.get("confidenceScore"));
            result.put("expectedImpact", generatedContent.get("expectedImpact"));

            return result;
        } catch (Exception e) {
            log.error("生成智能推荐失败", e);
            throw new ServiceException("生成推荐失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean acceptRecommendation(String recommendationId, String userId, String userName) {
        try {
            int rows = recommendationMapper.updateStatus(recommendationId, "accepted", userId, userName);
            return rows > 0;
        } catch (Exception e) {
            log.error("接受推荐失败", e);
            throw new ServiceException("接受推荐失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectRecommendation(String recommendationId, String userId, String userName, String reason) {
        try {
            // 更新状态
            int rows = recommendationMapper.updateStatus(recommendationId, "rejected", userId, userName);
            
            // 更新拒绝原因
            if (rows > 0 && reason != null && !reason.isEmpty()) {
                IntelligentRecommendation recommendation = recommendationMapper.selectById(recommendationId);
                if (recommendation != null) {
                    recommendation.setRejectReason(reason);
                    recommendationMapper.updateById(recommendation);
                }
            }
            
            return rows > 0;
        } catch (Exception e) {
            log.error("拒绝推荐失败", e);
            throw new ServiceException("拒绝推荐失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecommendation(String recommendationId) {
        try {
            int rows = recommendationMapper.deleteById(recommendationId);
            return rows > 0;
        } catch (Exception e) {
            log.error("删除推荐失败", e);
            throw new ServiceException("删除推荐失败：" + e.getMessage());
        }
    }

    @Override
    public IntelligentRecommendation getRecommendationById(String recommendationId) {
        try {
            return recommendationMapper.selectById(recommendationId);
        } catch (Exception e) {
            log.error("获取推荐详情失败", e);
            throw new ServiceException("获取推荐详情失败：" + e.getMessage());
        }
    }

    /**
     * 生成推荐内容（基于规则引擎的简化版本）
     * 
     * @param budgetId 预算ID
     * @param recommendationType 推荐类型
     * @param companyId 公司ID
     * @return 推荐内容
     */
    private Map<String, Object> generateRecommendationContent(String budgetId, String recommendationType,
                                                              String companyId) {
        Map<String, Object> result = new HashMap<>();
        
        // 模拟预算名称（实际应从数据库查询）
        String budgetName = "2025年度全面预算";
        result.put("budgetName", budgetName);

        // 根据推荐类型生成不同的内容
        switch (recommendationType) {
            case "cost_reduction":
                result.put("name", budgetName + " - 成本削减建议");
                result.put("content", generateCostReductionSuggestions());
                result.put("expectedImpact", "预计可节约成本5-10%，不影响业务目标达成");
                result.put("confidenceScore", new BigDecimal("85.5"));
                result.put("analysisData", generateCostAnalysisData());
                break;
                
            case "revenue_growth":
                result.put("name", budgetName + " - 收入增长建议");
                result.put("content", generateRevenueGrowthSuggestions());
                result.put("expectedImpact", "预计可提升收入10-15%，增强市场竞争力");
                result.put("confidenceScore", new BigDecimal("88.0"));
                result.put("analysisData", generateRevenueAnalysisData());
                break;
                
            case "budget_optimization":
                result.put("name", budgetName + " - 预算优化建议");
                result.put("content", generateBudgetOptimizationSuggestions());
                result.put("expectedImpact", "优化资源配置，提升预算执行效率");
                result.put("confidenceScore", new BigDecimal("82.3"));
                result.put("analysisData", generateOptimizationAnalysisData());
                break;
                
            case "risk_warning":
                result.put("name", budgetName + " - 风险预警");
                result.put("content", generateRiskWarningSuggestions());
                result.put("expectedImpact", "及时识别和应对潜在风险，保障预算目标实现");
                result.put("confidenceScore", new BigDecimal("78.5"));
                result.put("analysisData", generateRiskAnalysisData());
                break;
                
            default:
                result.put("name", budgetName + " - 综合建议");
                result.put("content", generateGeneralSuggestions());
                result.put("expectedImpact", "综合优化预算管理");
                result.put("confidenceScore", new BigDecimal("80.0"));
                result.put("analysisData", generateGeneralAnalysisData());
        }

        return result;
    }

    // 以下是各种推荐内容生成方法的辅助方法
    
    private Map<String, Object> generateCostReductionSuggestions() {
        Map<String, Object> suggestions = new HashMap<>();
        List<String> items = Arrays.asList(
            "优化市场推广费用，建议减少10%低效渠道投入",
            "加强差旅费用管理，推广视频会议减少出差",
            "调整业务招待费预算，提升费用使用效率",
            "优化办公用品采购，实施集中采购降低成本"
        );
        suggestions.put("suggestions", items);
        return suggestions;
    }

    private Map<String, Object> generateRevenueGrowthSuggestions() {
        Map<String, Object> suggestions = new HashMap<>();
        List<String> items = Arrays.asList(
            "增加研发投入，提升产品竞争力",
            "扩大市场营销预算，开拓新客户群体",
            "加强销售团队建设，提升销售能力",
            "优化定价策略，提高产品附加值"
        );
        suggestions.put("suggestions", items);
        return suggestions;
    }

    private Map<String, Object> generateBudgetOptimizationSuggestions() {
        Map<String, Object> suggestions = new HashMap<>();
        List<String> items = Arrays.asList(
            "调整部门预算分配，向高效部门倾斜",
            "优化预算执行节奏，避免年底突击花钱",
            "加强预算监控，及时发现和纠正偏差",
            "建立预算绩效评价体系，提升预算管理水平"
        );
        suggestions.put("suggestions", items);
        return suggestions;
    }

    private Map<String, Object> generateRiskWarningSuggestions() {
        Map<String, Object> warnings = new HashMap<>();
        List<String> items = Arrays.asList(
            "应收账款周转率下降，需加强回款管理",
            "存货积压风险增加，建议优化库存管理",
            "短期偿债压力较大，需关注现金流状况",
            "部分费用超预算风险，需加强预算控制"
        );
        warnings.put("warnings", items);
        return warnings;
    }

    private Map<String, Object> generateGeneralSuggestions() {
        Map<String, Object> suggestions = new HashMap<>();
        List<String> items = Arrays.asList(
            "综合优化预算结构，平衡收入与支出",
            "加强预算执行监控，提升管理效率",
            "建立预算预警机制，及时发现问题",
            "完善预算考核体系，提升执行力"
        );
        suggestions.put("suggestions", items);
        return suggestions;
    }

    private Map<String, Object> generateCostAnalysisData() {
        Map<String, Object> data = new HashMap<>();
        data.put("historicalCostTrend", "上升");
        data.put("avgGrowthRate", 0.12);
        data.put("volatility", 0.08);
        data.put("industryBenchmark", "中等水平");
        return data;
    }

    private Map<String, Object> generateRevenueAnalysisData() {
        Map<String, Object> data = new HashMap<>();
        data.put("historicalRevenueTrend", "稳定增长");
        data.put("avgGrowthRate", 0.15);
        data.put("marketShare", 0.18);
        data.put("competitivePosition", "领先");
        return data;
    }

    private Map<String, Object> generateOptimizationAnalysisData() {
        Map<String, Object> data = new HashMap<>();
        data.put("budgetExecutionRate", 0.92);
        data.put("varianceRate", 0.08);
        data.put("efficiencyScore", 85);
        data.put("improvementPotential", "中等");
        return data;
    }

    private Map<String, Object> generateRiskAnalysisData() {
        Map<String, Object> data = new HashMap<>();
        data.put("riskLevel", "medium");
        data.put("cashFlowTrend", "下降");
        data.put("debtRatio", 0.65);
        data.put("liquidityRatio", 1.2);
        return data;
    }

    private Map<String, Object> generateGeneralAnalysisData() {
        Map<String, Object> data = new HashMap<>();
        data.put("overallScore", 78);
        data.put("strengths", Arrays.asList("预算编制规范", "执行监控到位"));
        data.put("weaknesses", Arrays.asList("预算调整频繁", "考核体系不完善"));
        return data;
    }
}

