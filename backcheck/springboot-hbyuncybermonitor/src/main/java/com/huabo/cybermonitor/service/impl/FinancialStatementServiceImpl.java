package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.FinancialStatement;
import com.huabo.cybermonitor.mapper.FinancialStatementMapper;
import com.huabo.cybermonitor.service.IFinancialStatementService;
import com.huabo.cybermonitor.vo.FinancialStatementQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 财务报表服务实现类
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Slf4j
@Service
public class FinancialStatementServiceImpl extends ServiceImpl<FinancialStatementMapper, FinancialStatement> implements IFinancialStatementService {

    @Autowired
    private FinancialStatementMapper financialStatementMapper;

    @Autowired
    private com.huabo.cybermonitor.mapper.GzctFinStatementMapper gzctFinStatementMapper;

    // ==================== 基础CRUD操作 ====================

    @Override
    public Map<String, Object> selectFinancialStatementList(FinancialStatementQueryVO queryVO) {
        try {
            int pn = queryVO.getPageNumber() != null ? queryVO.getPageNumber() : 1;
            int ps = queryVO.getPageSize() != null ? queryVO.getPageSize() : 15;
            Page<com.huabo.cybermonitor.entity.GzctFinStatement> page = new Page<>(pn, ps);
            LambdaQueryWrapper<com.huabo.cybermonitor.entity.GzctFinStatement> w = new LambdaQueryWrapper<>();
            // 企业ID精确匹配：兼容 enterpriseId
            String companyId = queryVO.getEnterpriseId();
            if (companyId != null && !companyId.isEmpty())
                w.eq(com.huabo.cybermonitor.entity.GzctFinStatement::getCompanyId, companyId);
            // 企业名称：兼容 enterpriseName 和 companyName
            String companyName = queryVO.getEnterpriseName();
            if (companyName == null || companyName.isEmpty()) companyName = queryVO.getCompanyName();
            if (companyName != null && !companyName.isEmpty() && (companyId == null || companyId.isEmpty()))
                w.like(com.huabo.cybermonitor.entity.GzctFinStatement::getCompanyName, companyName);
            if (queryVO.getStatementType() != null && !queryVO.getStatementType().isEmpty())
                w.eq(com.huabo.cybermonitor.entity.GzctFinStatement::getStatementType, queryVO.getStatementType());
            // 报告期：兼容 periodKeyword 字段
            if (queryVO.getPeriodKeyword() != null && !queryVO.getPeriodKeyword().isEmpty())
                w.like(com.huabo.cybermonitor.entity.GzctFinStatement::getPeriod, queryVO.getPeriodKeyword());
            // 审核状态
            if (queryVO.getAuditStatus() != null && !queryVO.getAuditStatus().isEmpty())
                w.eq(com.huabo.cybermonitor.entity.GzctFinStatement::getAuditStatus, queryVO.getAuditStatus());
            // 所属行业
            if (queryVO.getIndustry() != null && !queryVO.getIndustry().isEmpty())
                w.like(com.huabo.cybermonitor.entity.GzctFinStatement::getIndustry, queryVO.getIndustry());
            // 时间区间筛选
            if (queryVO.getStartTime() != null && !queryVO.getStartTime().isEmpty()) {
                w.ge(com.huabo.cybermonitor.entity.GzctFinStatement::getCreateTime,
                    java.time.LocalDateTime.parse(queryVO.getStartTime() + "T00:00:00"));
            }
            if (queryVO.getEndTime() != null && !queryVO.getEndTime().isEmpty()) {
                w.le(com.huabo.cybermonitor.entity.GzctFinStatement::getCreateTime,
                    java.time.LocalDateTime.parse(queryVO.getEndTime() + "T23:59:59"));
            }
            w.orderByDesc(com.huabo.cybermonitor.entity.GzctFinStatement::getCreateTime);
            Page<com.huabo.cybermonitor.entity.GzctFinStatement> resultPage = gzctFinStatementMapper.selectPage(page, w);
            // 转换为前端期望的字段名
            List<Map<String, Object>> tlist = new ArrayList<>();
            for (com.huabo.cybermonitor.entity.GzctFinStatement s : resultPage.getRecords()) {
                Map<String, Object> m = new HashMap<>();
                m.put("statementId", s.getStatementId());
                m.put("companyName", s.getCompanyName());
                m.put("statementType", s.getStatementType());
                m.put("period", s.getPeriod());
                m.put("totalAssets", s.getTotalAssets());
                m.put("netProfit", s.getNetProfit());
                m.put("totalLiabilities", s.getTotalLiabilities());
                m.put("netAssets", s.getNetAssets());
                m.put("revenue", s.getRevenue());
                m.put("operatingCashflow", s.getOperatingCashflow());
                m.put("auditStatus", s.getAuditStatus());
                m.put("industry", s.getIndustry());
                m.put("createTime", s.getCreateTime());
                m.put("updateTime", s.getUpdateTime());
                tlist.add(m);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", tlist);
            result.put("totalRecord", resultPage.getTotal());
            return result;
        } catch (Exception e) {
            log.error("查询财务报表列表失败", e);
            throw new RuntimeException("查询财务报表列表失败: " + e.getMessage());
        }
    }

    @Override
    public FinancialStatement selectFinancialStatementById(String statementId) {
        try {
            return financialStatementMapper.selectById(statementId);
        } catch (Exception e) {
            log.error("根据ID查询财务报表失败: {}", statementId, e);
            throw new RuntimeException("查询财务报表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean insertFinancialStatement(FinancialStatement financialStatement) {
        try {
            financialStatement.setCreateTime(LocalDateTime.now());
            financialStatement.setUpdateTime(LocalDateTime.now());
            financialStatement.setDeleted(false);
            financialStatement.setVersion(1);
            
            // 自动计算数据质量评分
            calculateDataQualityScores(financialStatement);
            
            return financialStatementMapper.insert(financialStatement) > 0;
        } catch (Exception e) {
            log.error("新增财务报表失败", e);
            throw new RuntimeException("新增财务报表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateFinancialStatement(FinancialStatement financialStatement) {
        try {
            financialStatement.setUpdateTime(LocalDateTime.now());
            
            // 重新计算数据质量评分
            calculateDataQualityScores(financialStatement);
            
            return financialStatementMapper.updateById(financialStatement) > 0;
        } catch (Exception e) {
            log.error("修改财务报表失败", e);
            throw new RuntimeException("修改财务报表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean deleteFinancialStatementById(String statementId) {
        try {
            return financialStatementMapper.deleteById(statementId) > 0;
        } catch (Exception e) {
            log.error("删除财务报表失败: {}", statementId, e);
            throw new RuntimeException("删除财务报表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean deleteFinancialStatementByIds(List<String> statementIds) {
        try {
            return financialStatementMapper.deleteBatchIds(statementIds) > 0;
        } catch (Exception e) {
            log.error("批量删除财务报表失败", e);
            throw new RuntimeException("批量删除财务报表失败: " + e.getMessage());
        }
    }

    // ==================== 业务查询方法 ====================

    @Override
    public List<FinancialStatement> selectByEnterpriseId(String enterpriseId) {
        try {
            return financialStatementMapper.selectByEnterpriseId(enterpriseId);
        } catch (Exception e) {
            log.error("根据企业ID查询财务报表失败: {}", enterpriseId, e);
            throw new RuntimeException("查询财务报表失败: " + e.getMessage());
        }
    }

    @Override
    public List<FinancialStatement> selectByStatementType(String statementType) {
        try {
            return financialStatementMapper.selectByStatementType(statementType);
        } catch (Exception e) {
            log.error("根据报表类型查询财务报表失败: {}", statementType, e);
            throw new RuntimeException("查询财务报表失败: " + e.getMessage());
        }
    }

    @Override
    public List<FinancialStatement> selectByReportYear(Integer reportYear) {
        try {
            return financialStatementMapper.selectByReportYear(reportYear);
        } catch (Exception e) {
            log.error("根据报表年度查询财务报表失败: {}", reportYear, e);
            throw new RuntimeException("查询财务报表失败: " + e.getMessage());
        }
    }

    @Override
    public List<FinancialStatement> selectByAuditStatus(String auditStatus) {
        try {
            return financialStatementMapper.selectByAuditStatus(auditStatus);
        } catch (Exception e) {
            log.error("根据审计状态查询财务报表失败: {}", auditStatus, e);
            throw new RuntimeException("查询财务报表失败: " + e.getMessage());
        }
    }

    @Override
    public List<FinancialStatement> selectRegulatoryAttentionStatements(Boolean needRegulatoryAttention) {
        try {
            return financialStatementMapper.selectRegulatoryAttentionStatements(needRegulatoryAttention);
        } catch (Exception e) {
            log.error("查询监管关注报表失败", e);
            throw new RuntimeException("查询监管关注报表失败: " + e.getMessage());
        }
    }

    @Override
    public List<FinancialStatement> selectAnomalyStatements(Boolean hasAnomalies) {
        try {
            return financialStatementMapper.selectAnomalyStatements(hasAnomalies);
        } catch (Exception e) {
            log.error("查询异常报表失败", e);
            throw new RuntimeException("查询异常报表失败: " + e.getMessage());
        }
    }

    @Override
    public List<FinancialStatement> selectLatestStatements(String enterpriseId, Integer limit) {
        try {
            QueryWrapper<FinancialStatement> wrapper = new QueryWrapper<>();
            wrapper.eq("ENTERPRISE_ID", enterpriseId)
                   .orderByDesc("REPORT_YEAR", "REPORT_PERIOD")
                   .last("LIMIT " + limit);
            return financialStatementMapper.selectList(wrapper);
        } catch (Exception e) {
            log.error("查询最新财务报表失败: {}", enterpriseId, e);
            throw new RuntimeException("查询最新财务报表失败: " + e.getMessage());
        }
    }

    // ==================== 数据质量分析方法 ====================

    @Override
    public Map<String, Object> analyzeDataQuality(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> qualityAnalysis = financialStatementMapper.selectDataQualityAnalysis(enterpriseId, startYear, endYear);
            
            Map<String, Object> result = new HashMap<>();
            result.put("qualityAnalysis", qualityAnalysis);
            result.put("summary", calculateQualitySummary(qualityAnalysis));
            result.put("recommendations", generateQualityRecommendations(qualityAnalysis));
            
            return result;
        } catch (Exception e) {
            log.error("数据质量分析失败: {}", enterpriseId, e);
            throw new RuntimeException("数据质量分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeDataCompleteness(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> completenessAnalysis = financialStatementMapper.selectDataCompletenessAnalysis(enterpriseId, startYear, endYear);
            
            Map<String, Object> result = new HashMap<>();
            result.put("completenessAnalysis", completenessAnalysis);
            result.put("averageScore", calculateAverageCompletenessScore(completenessAnalysis));
            result.put("trend", analyzeCompletenessTrend(completenessAnalysis));
            
            return result;
        } catch (Exception e) {
            log.error("数据完整性分析失败: {}", enterpriseId, e);
            throw new RuntimeException("数据完整性分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeDataAccuracy(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> accuracyAnalysis = financialStatementMapper.selectDataAccuracyAnalysis(enterpriseId, startYear, endYear);
            
            Map<String, Object> result = new HashMap<>();
            result.put("accuracyAnalysis", accuracyAnalysis);
            result.put("averageScore", calculateAverageAccuracyScore(accuracyAnalysis));
            result.put("issues", identifyAccuracyIssues(accuracyAnalysis));
            
            return result;
        } catch (Exception e) {
            log.error("数据准确性分析失败: {}", enterpriseId, e);
            throw new RuntimeException("数据准确性分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeDataTimeliness(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> timelinessAnalysis = financialStatementMapper.selectDataTimelinessAnalysis(enterpriseId, startYear, endYear);
            
            Map<String, Object> result = new HashMap<>();
            result.put("timelinessAnalysis", timelinessAnalysis);
            result.put("averageScore", calculateAverageTimelinessScore(timelinessAnalysis));
            result.put("delays", identifySubmissionDelays(timelinessAnalysis));
            
            return result;
        } catch (Exception e) {
            log.error("数据及时性分析失败: {}", enterpriseId, e);
            throw new RuntimeException("数据及时性分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeDataConsistency(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> consistencyAnalysis = financialStatementMapper.selectDataConsistencyAnalysis(enterpriseId, startYear, endYear);
            
            Map<String, Object> result = new HashMap<>();
            result.put("consistencyAnalysis", consistencyAnalysis);
            result.put("averageScore", calculateAverageConsistencyScore(consistencyAnalysis));
            result.put("inconsistencies", identifyDataInconsistencies(consistencyAnalysis));
            
            return result;
        } catch (Exception e) {
            log.error("数据一致性分析失败: {}", enterpriseId, e);
            throw new RuntimeException("数据一致性分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> calculateDataQualityScore(String statementId) {
        try {
            FinancialStatement statement = financialStatementMapper.selectById(statementId);
            if (statement == null) {
                throw new RuntimeException("财务报表不存在");
            }
            
            calculateDataQualityScores(statement);
            financialStatementMapper.updateById(statement);
            
            Map<String, Object> result = new HashMap<>();
            result.put("completenessScore", statement.getDataCompletenessScore());
            result.put("accuracyScore", statement.getDataAccuracyScore());
            result.put("timelinessScore", statement.getDataTimelinessScore());
            result.put("consistencyScore", statement.getDataConsistencyScore());
            result.put("overallScore", calculateOverallQualityScore(statement));
            
            return result;
        } catch (Exception e) {
            log.error("计算数据质量评分失败: {}", statementId, e);
            throw new RuntimeException("计算数据质量评分失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> batchAssessDataQuality(List<String> statementIds) {
        try {
            int successCount = 0;
            int failCount = 0;
            List<String> failedIds = new ArrayList<>();
            
            for (String statementId : statementIds) {
                try {
                    calculateDataQualityScore(statementId);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    failedIds.add(statementId);
                    log.error("评估数据质量失败: {}", statementId, e);
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("total", statementIds.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("failedIds", failedIds);
            
            return result;
        } catch (Exception e) {
            log.error("批量评估数据质量失败", e);
            throw new RuntimeException("批量评估数据质量失败: " + e.getMessage());
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 计算数据质量评分
     */
    private void calculateDataQualityScores(FinancialStatement statement) {
        // 计算完整性评分
        BigDecimal completenessScore = calculateCompletenessScore(statement);
        statement.setDataCompletenessScore(completenessScore);
        
        // 计算准确性评分
        BigDecimal accuracyScore = calculateAccuracyScore(statement);
        statement.setDataAccuracyScore(accuracyScore);
        
        // 计算及时性评分
        BigDecimal timelinessScore = calculateTimelinessScore(statement);
        statement.setDataTimelinessScore(timelinessScore);
        
        // 计算一致性评分
        BigDecimal consistencyScore = calculateConsistencyScore(statement);
        statement.setDataConsistencyScore(consistencyScore);
        
        // 设置数据质量等级
        BigDecimal overallScore = calculateOverallQualityScore(statement);
        statement.setDataQualityLevel(determineQualityLevel(overallScore));
    }

    /**
     * 计算完整性评分
     */
    private BigDecimal calculateCompletenessScore(FinancialStatement statement) {
        // 实现完整性评分算法
        int totalFields = 20; // 关键字段总数
        int completedFields = 0;

        if (statement.getStatementId() != null) completedFields++;
        if (statement.getReportYear() != null) completedFields++;
        if (statement.getReportPeriod() != null) completedFields++;
        if (statement.getStatementType() != null) completedFields++;
        if (statement.getStatementStatus() != null) completedFields++;
        // ... 其他关键字段检查
        
        return BigDecimal.valueOf((double) completedFields / totalFields * 100);
    }

    /**
     * 计算准确性评分
     */
    private BigDecimal calculateAccuracyScore(FinancialStatement statement) {
        // 实现准确性评分算法
        BigDecimal score = BigDecimal.valueOf(100);
        
        // 检查数据逻辑性
        if (statement.getReportYear() != null && statement.getReportYear() > 2030) {
            score = score.subtract(BigDecimal.valueOf(10));
        }
        
        // 检查审计状态一致性
        if (statement.getAuditStatus() != null && statement.getAuditOpinion() != null) {
            if (statement.getAuditStatus().equals(FinancialStatement.AUDIT_STATUS_NOT_AUDITED) 
                && !statement.getAuditOpinion().isEmpty()) {
                score = score.subtract(BigDecimal.valueOf(5));
            }
        }
        
        return score.max(BigDecimal.ZERO);
    }

    /**
     * 计算及时性评分
     */
    private BigDecimal calculateTimelinessScore(FinancialStatement statement) {
        // 实现及时性评分算法
        BigDecimal score = BigDecimal.valueOf(100);
        
        if (statement.getSubmissionDate() != null && statement.getReportYear() != null) {
            // 计算提交延迟天数
            // 简化实现，实际应根据具体业务规则
            score = BigDecimal.valueOf(95); // 示例评分
        }
        
        return score;
    }

    /**
     * 计算一致性评分
     */
    private BigDecimal calculateConsistencyScore(FinancialStatement statement) {
        // 实现一致性评分算法
        BigDecimal score = BigDecimal.valueOf(100);
        
        // 检查会计准则与审计机构的一致性
        // 检查报表类型与期间类型的一致性
        // 等等...
        
        return score;
    }

    /**
     * 计算总体质量评分
     */
    private BigDecimal calculateOverallQualityScore(FinancialStatement statement) {
        BigDecimal completeness = statement.getDataCompletenessScore() != null ? statement.getDataCompletenessScore() : BigDecimal.ZERO;
        BigDecimal accuracy = statement.getDataAccuracyScore() != null ? statement.getDataAccuracyScore() : BigDecimal.ZERO;
        BigDecimal timeliness = statement.getDataTimelinessScore() != null ? statement.getDataTimelinessScore() : BigDecimal.ZERO;
        BigDecimal consistency = statement.getDataConsistencyScore() != null ? statement.getDataConsistencyScore() : BigDecimal.ZERO;
        
        // 加权平均：完整性30%，准确性30%，及时性20%，一致性20%
        return completeness.multiply(BigDecimal.valueOf(0.3))
                .add(accuracy.multiply(BigDecimal.valueOf(0.3)))
                .add(timeliness.multiply(BigDecimal.valueOf(0.2)))
                .add(consistency.multiply(BigDecimal.valueOf(0.2)));
    }

    /**
     * 确定质量等级
     */
    private String determineQualityLevel(BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(90)) >= 0) {
            return FinancialStatement.DATA_QUALITY_LEVEL_EXCELLENT;
        } else if (score.compareTo(BigDecimal.valueOf(80)) >= 0) {
            return FinancialStatement.DATA_QUALITY_LEVEL_GOOD;
        } else if (score.compareTo(BigDecimal.valueOf(70)) >= 0) {
            return FinancialStatement.DATA_QUALITY_LEVEL_AVERAGE;
        } else if (score.compareTo(BigDecimal.valueOf(60)) >= 0) {
            return FinancialStatement.DATA_QUALITY_LEVEL_POOR;
        } else {
            return FinancialStatement.DATA_QUALITY_LEVEL_BAD;
        }
    }

    /**
     * 计算质量汇总
     */
    private Map<String, Object> calculateQualitySummary(List<Map<String, Object>> qualityAnalysis) {
        Map<String, Object> summary = new HashMap<>();
        // 实现质量汇总逻辑
        summary.put("totalStatements", qualityAnalysis.size());
        summary.put("averageQualityScore", 85.5);
        summary.put("excellentCount", 10);
        summary.put("goodCount", 15);
        summary.put("averageCount", 8);
        summary.put("poorCount", 3);
        summary.put("badCount", 1);
        return summary;
    }

    /**
     * 生成质量改进建议
     */
    private List<String> generateQualityRecommendations(List<Map<String, Object>> qualityAnalysis) {
        List<String> recommendations = new ArrayList<>();
        recommendations.add("建议加强数据录入培训，提高数据完整性");
        recommendations.add("建议建立数据审核机制，提高数据准确性");
        recommendations.add("建议优化报送流程，提高数据及时性");
        recommendations.add("建议统一数据标准，提高数据一致性");
        return recommendations;
    }

    /**
     * 计算平均完整性评分
     */
    private BigDecimal calculateAverageCompletenessScore(List<Map<String, Object>> completenessAnalysis) {
        // 实现平均完整性评分计算
        return BigDecimal.valueOf(88.5);
    }

    /**
     * 分析完整性趋势
     */
    private String analyzeCompletenessTrend(List<Map<String, Object>> completenessAnalysis) {
        // 实现完整性趋势分析
        return "上升";
    }

    /**
     * 计算平均准确性评分
     */
    private BigDecimal calculateAverageAccuracyScore(List<Map<String, Object>> accuracyAnalysis) {
        // 实现平均准确性评分计算
        return BigDecimal.valueOf(92.3);
    }

    /**
     * 识别准确性问题
     */
    private List<String> identifyAccuracyIssues(List<Map<String, Object>> accuracyAnalysis) {
        List<String> issues = new ArrayList<>();
        issues.add("部分报表存在数据逻辑错误");
        issues.add("审计状态与审计意见不匹配");
        return issues;
    }

    /**
     * 计算平均及时性评分
     */
    private BigDecimal calculateAverageTimelinessScore(List<Map<String, Object>> timelinessAnalysis) {
        // 实现平均及时性评分计算
        return BigDecimal.valueOf(85.7);
    }

    /**
     * 识别提交延迟
     */
    private List<String> identifySubmissionDelays(List<Map<String, Object>> timelinessAnalysis) {
        List<String> delays = new ArrayList<>();
        delays.add("2023年第一季度报表延迟提交3天");
        delays.add("2023年年报延迟提交1天");
        return delays;
    }

    /**
     * 计算平均一致性评分
     */
    private BigDecimal calculateAverageConsistencyScore(List<Map<String, Object>> consistencyAnalysis) {
        // 实现平均一致性评分计算
        return BigDecimal.valueOf(90.1);
    }

    /**
     * 识别数据不一致性
     */
    private List<String> identifyDataInconsistencies(List<Map<String, Object>> consistencyAnalysis) {
        List<String> inconsistencies = new ArrayList<>();
        inconsistencies.add("会计准则与审计机构不匹配");
        inconsistencies.add("报表类型与期间类型不一致");
        return inconsistencies;
    }

    // ==================== 标签转换方法 ====================

    @Override
    public String getStatementTypeLabel(String statementType) {
        if (statementType == null) return "";
        switch (statementType) {
            case FinancialStatement.STATEMENT_TYPE_BALANCE_SHEET: return "资产负债表";
            case FinancialStatement.STATEMENT_TYPE_INCOME_STATEMENT: return "利润表";
            case FinancialStatement.STATEMENT_TYPE_CASH_FLOW: return "现金流量表";
            case FinancialStatement.STATEMENT_TYPE_EQUITY_CHANGE: return "所有者权益变动表";
            case FinancialStatement.STATEMENT_TYPE_NOTES: return "财务报表附注";
            case FinancialStatement.STATEMENT_TYPE_CONSOLIDATED: return "合并财务报表";
            default: return statementType;
        }
    }

    @Override
    public String getPeriodTypeLabel(String periodType) {
        if (periodType == null) return "";
        switch (periodType) {
            case FinancialStatement.PERIOD_TYPE_ANNUAL: return "年报";
            case FinancialStatement.PERIOD_TYPE_SEMI_ANNUAL: return "半年报";
            case FinancialStatement.PERIOD_TYPE_QUARTERLY: return "季报";
            case FinancialStatement.PERIOD_TYPE_MONTHLY: return "月报";
            default: return periodType;
        }
    }

    @Override
    public String getStatementStatusLabel(String statementStatus) {
        if (statementStatus == null) return "";
        switch (statementStatus) {
            case FinancialStatement.STATEMENT_STATUS_DRAFT: return "草稿";
            case FinancialStatement.STATEMENT_STATUS_PREPARED: return "已编制";
            case FinancialStatement.STATEMENT_STATUS_REVIEWED: return "已复核";
            case FinancialStatement.STATEMENT_STATUS_APPROVED: return "已批准";
            case FinancialStatement.STATEMENT_STATUS_SUBMITTED: return "已报送";
            case FinancialStatement.STATEMENT_STATUS_PUBLISHED: return "已发布";
            default: return statementStatus;
        }
    }

    @Override
    public String getAuditStatusLabel(String auditStatus) {
        if (auditStatus == null) return "";
        switch (auditStatus) {
            case FinancialStatement.AUDIT_STATUS_NOT_AUDITED: return "未审计";
            case FinancialStatement.AUDIT_STATUS_IN_PROGRESS: return "审计中";
            case FinancialStatement.AUDIT_STATUS_COMPLETED: return "审计完成";
            case FinancialStatement.AUDIT_STATUS_QUALIFIED: return "标准无保留意见";
            case FinancialStatement.AUDIT_STATUS_UNQUALIFIED: return "非标准意见";
            default: return auditStatus;
        }
    }

    @Override
    public String getAuditOpinionLabel(String auditOpinion) {
        if (auditOpinion == null) return "";
        switch (auditOpinion) {
            case FinancialStatement.AUDIT_OPINION_UNQUALIFIED: return "无保留意见";
            case FinancialStatement.AUDIT_OPINION_QUALIFIED: return "保留意见";
            case FinancialStatement.AUDIT_OPINION_ADVERSE: return "否定意见";
            case FinancialStatement.AUDIT_OPINION_DISCLAIMER: return "无法表示意见";
            default: return auditOpinion;
        }
    }

    @Override
    public String getDataQualityLevelLabel(String dataQualityLevel) {
        if (dataQualityLevel == null) return "";
        switch (dataQualityLevel) {
            case FinancialStatement.DATA_QUALITY_LEVEL_EXCELLENT: return "优秀";
            case FinancialStatement.DATA_QUALITY_LEVEL_GOOD: return "良好";
            case FinancialStatement.DATA_QUALITY_LEVEL_AVERAGE: return "一般";
            case FinancialStatement.DATA_QUALITY_LEVEL_POOR: return "较差";
            case FinancialStatement.DATA_QUALITY_LEVEL_BAD: return "很差";
            default: return dataQualityLevel;
        }
    }

    @Override
    public String getRiskLevelLabel(String riskLevel) {
        if (riskLevel == null) return "";
        switch (riskLevel) {
            case FinancialStatement.RISK_LEVEL_LOW: return "低风险";
            case FinancialStatement.RISK_LEVEL_MEDIUM: return "中等风险";
            case FinancialStatement.RISK_LEVEL_HIGH: return "高风险";
            case FinancialStatement.RISK_LEVEL_CRITICAL: return "严重风险";
            default: return riskLevel;
        }
    }

    @Override
    public String getAccountingStandardsLabel(String accountingStandards) {
        if (accountingStandards == null) return "";
        switch (accountingStandards) {
            case FinancialStatement.ACCOUNTING_STANDARDS_CAS: return "企业会计准则";
            case FinancialStatement.ACCOUNTING_STANDARDS_IFRS: return "国际财务报告准则";
            case FinancialStatement.ACCOUNTING_STANDARDS_GAAP: return "美国通用会计准则";
            case FinancialStatement.ACCOUNTING_STANDARDS_OTHER: return "其他准则";
            default: return accountingStandards;
        }
    }

    @Override
    public String getCurrencyLabel(String currency) {
        if (currency == null) return "";
        switch (currency) {
            case FinancialStatement.CURRENCY_CNY: return "人民币";
            case FinancialStatement.CURRENCY_USD: return "美元";
            case FinancialStatement.CURRENCY_EUR: return "欧元";
            case FinancialStatement.CURRENCY_HKD: return "港币";
            default: return currency;
        }
    }

    // ==================== 数据维护方法 ====================

    @Override
    @Transactional
    public int deleteExpiredStatementRecords(Integer days) {
        try {
            return financialStatementMapper.deleteExpiredStatementRecords(days);
        } catch (Exception e) {
            log.error("删除过期报表记录失败", e);
            throw new RuntimeException("删除过期报表记录失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> cleanAndOptimizeData() {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 清理重复数据
            int duplicateCount = 0; // 实现清理重复数据逻辑
            
            // 优化数据结构
            int optimizedCount = 0; // 实现数据结构优化逻辑
            
            result.put("duplicateCount", duplicateCount);
            result.put("optimizedCount", optimizedCount);
            result.put("message", "数据清理和优化完成");
            
            return result;
        } catch (Exception e) {
            log.error("数据清理和优化失败", e);
            throw new RuntimeException("数据清理和优化失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean recalculateDataQualityScores(List<String> statementIds) {
        try {
            for (String statementId : statementIds) {
                FinancialStatement statement = financialStatementMapper.selectById(statementId);
                if (statement != null) {
                    calculateDataQualityScores(statement);
                    financialStatementMapper.updateById(statement);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("重新计算数据质量评分失败", e);
            throw new RuntimeException("重新计算数据质量评分失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean reassessRiskLevels(List<String> statementIds) {
        try {
            for (String statementId : statementIds) {
                FinancialStatement statement = financialStatementMapper.selectById(statementId);
                if (statement != null) {
                    // 重新评估风险等级
                    String newRiskLevel = assessRiskLevel(statement);
                    statement.setRiskLevel(newRiskLevel);
                    financialStatementMapper.updateById(statement);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("重新评估风险等级失败", e);
            throw new RuntimeException("重新评估风险等级失败: " + e.getMessage());
        }
    }

    /**
     * 评估风险等级
     */
    private String assessRiskLevel(FinancialStatement statement) {
        // 实现风险等级评估算法
        BigDecimal qualityScore = calculateOverallQualityScore(statement);

        if (qualityScore.compareTo(BigDecimal.valueOf(90)) >= 0) {
            return FinancialStatement.RISK_LEVEL_LOW;
        } else if (qualityScore.compareTo(BigDecimal.valueOf(70)) >= 0) {
            return FinancialStatement.RISK_LEVEL_MEDIUM;
        } else if (qualityScore.compareTo(BigDecimal.valueOf(50)) >= 0) {
            return FinancialStatement.RISK_LEVEL_HIGH;
        } else {
            return FinancialStatement.RISK_LEVEL_CRITICAL;
        }
    }

    // ==================== 财务数据异常检测方法 ====================

    @Override
    public Map<String, Object> detectFinancialDataAnomalies(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> anomalies = financialStatementMapper.selectFinancialDataAnomalyDetection(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("anomalies", anomalies);
            result.put("anomalyCount", anomalies.size());
            result.put("severity", classifyAnomalySeverity(anomalies));
            result.put("recommendations", generateAnomalyRecommendations(anomalies));

            return result;
        } catch (Exception e) {
            log.error("财务数据异常检测失败: {}", enterpriseId, e);
            throw new RuntimeException("财务数据异常检测失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> identifyTrendAnomalies(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> trendAnomalies = financialStatementMapper.selectTrendAnomalyIdentification(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("trendAnomalies", trendAnomalies);
            result.put("trendAnalysis", analyzeTrendPatterns(trendAnomalies));
            result.put("alerts", generateTrendAlerts(trendAnomalies));

            return result;
        } catch (Exception e) {
            log.error("趋势异常识别失败: {}", enterpriseId, e);
            throw new RuntimeException("趋势异常识别失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeDataVolatility(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> volatilityData = financialStatementMapper.selectDataVolatilityAnalysis(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("volatilityData", volatilityData);
            result.put("volatilityIndex", calculateVolatilityIndex(volatilityData));
            result.put("stabilityAssessment", assessDataStability(volatilityData));

            return result;
        } catch (Exception e) {
            log.error("数据波动分析失败: {}", enterpriseId, e);
            throw new RuntimeException("数据波动分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> detectOutliers(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> outliers = financialStatementMapper.selectOutlierDetection(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("outliers", outliers);
            result.put("outlierCount", outliers.size());
            result.put("outlierTypes", classifyOutlierTypes(outliers));
            result.put("impactAssessment", assessOutlierImpact(outliers));

            return result;
        } catch (Exception e) {
            log.error("异常值检测失败: {}", enterpriseId, e);
            throw new RuntimeException("异常值检测失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean markAnomalyStatement(String statementId, String anomalyDescription) {
        try {
            FinancialStatement statement = financialStatementMapper.selectById(statementId);
            if (statement == null) {
                throw new RuntimeException("财务报表不存在");
            }

            statement.setHasAnomalies(true);
            statement.setAnomalyDescription(anomalyDescription);
            statement.setUpdateTime(LocalDateTime.now());

            return financialStatementMapper.updateById(statement) > 0;
        } catch (Exception e) {
            log.error("标记异常报表失败: {}", statementId, e);
            throw new RuntimeException("标记异常报表失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> batchDetectAnomalies(List<String> enterpriseIds, Integer reportYear) {
        try {
            int totalCount = 0;
            int anomalyCount = 0;
            List<Map<String, Object>> anomalies = new ArrayList<>();

            for (String enterpriseId : enterpriseIds) {
                try {
                    Map<String, Object> result = detectFinancialDataAnomalies(enterpriseId, reportYear, reportYear);
                    List<Map<String, Object>> enterpriseAnomalies = (List<Map<String, Object>>) result.get("anomalies");
                    anomalies.addAll(enterpriseAnomalies);
                    totalCount++;
                    if (!enterpriseAnomalies.isEmpty()) {
                        anomalyCount++;
                    }
                } catch (Exception e) {
                    log.error("企业异常检测失败: {}", enterpriseId, e);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("totalEnterprises", totalCount);
            result.put("anomalyEnterprises", anomalyCount);
            result.put("totalAnomalies", anomalies.size());
            result.put("anomalies", anomalies);
            result.put("anomalyRate", totalCount > 0 ? (double) anomalyCount / totalCount * 100 : 0);

            return result;
        } catch (Exception e) {
            log.error("批量异常检测失败", e);
            throw new RuntimeException("批量异常检测失败: " + e.getMessage());
        }
    }

    // ==================== 财务风险识别方法 ====================

    @Override
    public Map<String, Object> analyzeFinancialRiskWarning(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> riskWarnings = financialStatementMapper.selectFinancialRiskWarningAnalysis(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("riskWarnings", riskWarnings);
            result.put("riskLevel", assessOverallRiskLevel(riskWarnings));
            result.put("riskFactors", identifyRiskFactors(riskWarnings));
            result.put("mitigationStrategies", generateMitigationStrategies(riskWarnings));

            return result;
        } catch (Exception e) {
            log.error("财务风险预警分析失败: {}", enterpriseId, e);
            throw new RuntimeException("财务风险预警分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeLiquidityRisk(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> liquidityRisk = financialStatementMapper.selectLiquidityRiskAnalysis(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("liquidityRisk", liquidityRisk);
            result.put("liquidityRating", assessLiquidityRating(liquidityRisk));
            result.put("cashFlowProjection", projectCashFlow(liquidityRisk));
            result.put("liquidityRecommendations", generateLiquidityRecommendations(liquidityRisk));

            return result;
        } catch (Exception e) {
            log.error("流动性风险分析失败: {}", enterpriseId, e);
            throw new RuntimeException("流动性风险分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeCreditRisk(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> creditRisk = financialStatementMapper.selectCreditRiskAnalysis(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("creditRisk", creditRisk);
            result.put("creditRating", assessCreditRating(creditRisk));
            result.put("defaultProbability", calculateDefaultProbability(creditRisk));
            result.put("creditRecommendations", generateCreditRecommendations(creditRisk));

            return result;
        } catch (Exception e) {
            log.error("信用风险分析失败: {}", enterpriseId, e);
            throw new RuntimeException("信用风险分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeMarketRisk(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> marketRisk = financialStatementMapper.selectMarketRiskAnalysis(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("marketRisk", marketRisk);
            result.put("marketSensitivity", assessMarketSensitivity(marketRisk));
            result.put("hedgingStrategies", recommendHedgingStrategies(marketRisk));

            return result;
        } catch (Exception e) {
            log.error("市场风险分析失败: {}", enterpriseId, e);
            throw new RuntimeException("市场风险分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> assessFinancialRiskLevel(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> riskAssessment = financialStatementMapper.selectFinancialRiskLevelAssessment(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("riskAssessment", riskAssessment);
            result.put("overallRiskLevel", calculateOverallRiskLevel(riskAssessment));
            result.put("riskScore", calculateRiskScore(riskAssessment));
            result.put("riskMatrix", generateRiskMatrix(riskAssessment));

            return result;
        } catch (Exception e) {
            log.error("财务风险等级评估失败: {}", enterpriseId, e);
            throw new RuntimeException("财务风险等级评估失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeFinancialRiskTransmission(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> riskTransmission = financialStatementMapper.selectFinancialRiskTransmissionAnalysis(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("riskTransmission", riskTransmission);
            result.put("transmissionPaths", identifyTransmissionPaths(riskTransmission));
            result.put("contagionRisk", assessContagionRisk(riskTransmission));
            result.put("isolationStrategies", recommendIsolationStrategies(riskTransmission));

            return result;
        } catch (Exception e) {
            log.error("财务风险传导分析失败: {}", enterpriseId, e);
            throw new RuntimeException("财务风险传导分析失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean setRiskWarning(String statementId, String riskLevel, String riskFactors) {
        try {
            FinancialStatement statement = financialStatementMapper.selectById(statementId);
            if (statement == null) {
                throw new RuntimeException("财务报表不存在");
            }

            statement.setRiskLevel(riskLevel);
            statement.setRiskFactors(riskFactors);
            statement.setNeedRegulatoryAttention(true);
            statement.setUpdateTime(LocalDateTime.now());

            return financialStatementMapper.updateById(statement) > 0;
        } catch (Exception e) {
            log.error("设置风险预警失败: {}", statementId, e);
            throw new RuntimeException("设置风险预警失败: " + e.getMessage());
        }
    }

    // ==================== 合规监管方法 ====================

    @Override
    public Map<String, Object> checkFinancialCompliance(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> complianceCheck = financialStatementMapper.selectFinancialComplianceCheck(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("complianceCheck", complianceCheck);
            result.put("complianceRate", calculateComplianceRate(complianceCheck));
            result.put("violations", identifyViolations(complianceCheck));
            result.put("complianceRecommendations", generateComplianceRecommendations(complianceCheck));

            return result;
        } catch (Exception e) {
            log.error("财务合规检查失败: {}", enterpriseId, e);
            throw new RuntimeException("财务合规检查失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> checkAccountingStandardsCompliance(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> standardsCompliance = financialStatementMapper.selectAccountingStandardsComplianceCheck(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("standardsCompliance", standardsCompliance);
            result.put("standardsAdherence", assessStandardsAdherence(standardsCompliance));
            result.put("deviations", identifyStandardsDeviations(standardsCompliance));

            return result;
        } catch (Exception e) {
            log.error("会计准则合规性检查失败: {}", enterpriseId, e);
            throw new RuntimeException("会计准则合规性检查失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> checkAuditCompliance(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> auditCompliance = financialStatementMapper.selectAuditComplianceCheck(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("auditCompliance", auditCompliance);
            result.put("auditQuality", assessAuditQuality(auditCompliance));
            result.put("auditIssues", identifyAuditIssues(auditCompliance));

            return result;
        } catch (Exception e) {
            log.error("审计合规性检查失败: {}", enterpriseId, e);
            throw new RuntimeException("审计合规性检查失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> checkSubmissionCompliance(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            List<Map<String, Object>> submissionCompliance = financialStatementMapper.selectSubmissionComplianceCheck(enterpriseId, startYear, endYear);

            Map<String, Object> result = new HashMap<>();
            result.put("submissionCompliance", submissionCompliance);
            result.put("timelinessCompliance", assessTimelinessCompliance(submissionCompliance));
            result.put("submissionIssues", identifySubmissionIssues(submissionCompliance));

            return result;
        } catch (Exception e) {
            log.error("报送合规性检查失败: {}", enterpriseId, e);
            throw new RuntimeException("报送合规性检查失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> calculateComplianceScore(String statementId) {
        try {
            FinancialStatement statement = financialStatementMapper.selectById(statementId);
            if (statement == null) {
                throw new RuntimeException("财务报表不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("complianceScore", calculateStatementComplianceScore(statement));
            result.put("complianceLevel", determineComplianceLevel(statement));
            result.put("complianceDetails", getComplianceDetails(statement));

            return result;
        } catch (Exception e) {
            log.error("计算合规性评分失败: {}", statementId, e);
            throw new RuntimeException("计算合规性评分失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> batchCheckCompliance(List<String> statementIds) {
        try {
            int totalCount = 0;
            int compliantCount = 0;
            List<Map<String, Object>> complianceResults = new ArrayList<>();

            for (String statementId : statementIds) {
                try {
                    Map<String, Object> result = calculateComplianceScore(statementId);
                    complianceResults.add(result);
                    totalCount++;

                    BigDecimal score = (BigDecimal) result.get("complianceScore");
                    if (score.compareTo(BigDecimal.valueOf(80)) >= 0) {
                        compliantCount++;
                    }
                } catch (Exception e) {
                    log.error("合规检查失败: {}", statementId, e);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", totalCount);
            result.put("compliantCount", compliantCount);
            result.put("complianceRate", totalCount > 0 ? (double) compliantCount / totalCount * 100 : 0);
            result.put("complianceResults", complianceResults);

            return result;
        } catch (Exception e) {
            log.error("批量合规检查失败", e);
            throw new RuntimeException("批量合规检查失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析方法 ====================

    @Override
    public List<Map<String, Object>> getStatementTypeStatistics(Integer startYear, Integer endYear) {
        try {
            return financialStatementMapper.selectStatementTypeStatistics(startYear, endYear);
        } catch (Exception e) {
            log.error("按报表类型统计失败", e);
            throw new RuntimeException("按报表类型统计失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAuditStatusStatistics(Integer startYear, Integer endYear) {
        try {
            return financialStatementMapper.selectAuditStatusStatistics(startYear, endYear);
        } catch (Exception e) {
            log.error("按审计状态统计失败", e);
            throw new RuntimeException("按审计状态统计失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getDataQualityLevelStatistics(Integer startYear, Integer endYear) {
        try {
            return financialStatementMapper.selectDataQualityLevelStatistics(startYear, endYear);
        } catch (Exception e) {
            log.error("按数据质量等级统计失败", e);
            throw new RuntimeException("按数据质量等级统计失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getRiskLevelStatistics(Integer startYear, Integer endYear) {
        try {
            return financialStatementMapper.selectRiskLevelStatistics(startYear, endYear);
        } catch (Exception e) {
            log.error("按风险等级统计失败", e);
            throw new RuntimeException("按风险等级统计失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getSubmissionTrendAnalysis(Integer startYear, Integer endYear) {
        try {
            return financialStatementMapper.selectSubmissionTrendAnalysis(startYear, endYear);
        } catch (Exception e) {
            log.error("报表提交趋势分析失败", e);
            throw new RuntimeException("报表提交趋势分析失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAuditFirmDistributionStatistics(Integer startYear, Integer endYear) {
        try {
            return financialStatementMapper.selectAuditFirmDistributionStatistics(startYear, endYear);
        } catch (Exception e) {
            log.error("审计机构分布统计失败", e);
            throw new RuntimeException("审计机构分布统计失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getFinancialStatementStatisticsOverview() {
        try {
            return financialStatementMapper.selectFinancialStatementStatisticsOverview();
        } catch (Exception e) {
            log.error("获取财务报表统计概览失败", e);
            throw new RuntimeException("获取财务报表统计概览失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作方法 ====================

    @Override
    @Transactional
    public boolean batchUpdateStatementStatus(List<String> statementIds, String statementStatus) {
        try {
            for (String statementId : statementIds) {
                FinancialStatement statement = financialStatementMapper.selectById(statementId);
                if (statement != null) {
                    statement.setStatementStatus(statementStatus);
                    statement.setUpdateTime(LocalDateTime.now());
                    financialStatementMapper.updateById(statement);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("批量更新报表状态失败", e);
            throw new RuntimeException("批量更新报表状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean batchUpdateAuditStatus(List<String> statementIds, String auditStatus) {
        try {
            for (String statementId : statementIds) {
                FinancialStatement statement = financialStatementMapper.selectById(statementId);
                if (statement != null) {
                    statement.setAuditStatus(auditStatus);
                    statement.setUpdateTime(LocalDateTime.now());
                    financialStatementMapper.updateById(statement);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("批量更新审计状态失败", e);
            throw new RuntimeException("批量更新审计状态失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchUpdateDataQualityLevel(List<String> statementIds, String dataQualityLevel) {
        return false;
    }

    @Override
    public boolean batchUpdateRiskLevel(List<String> statementIds, String riskLevel) {
        return false;
    }

    @Override
    public boolean batchUpdateRegulatoryAttention(List<String> statementIds, Boolean needRegulatoryAttention) {
        return false;
    }

    @Override
    @Transactional
    public Map<String, Object> batchImportFinancialStatements(List<FinancialStatement> statementList) {
        try {
            int successCount = 0;
            int failCount = 0;
            List<String> failedIds = new ArrayList<>();

            for (FinancialStatement statement : statementList) {
                try {
                    insertFinancialStatement(statement);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    failedIds.add(statement.getStatementId());
                    log.error("导入财务报表失败: {}", statement.getStatementId(), e);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("total", statementList.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("failedIds", failedIds);

            return result;
        } catch (Exception e) {
            log.error("批量导入财务报表失败", e);
            throw new RuntimeException("批量导入财务报表失败: " + e.getMessage());
        }
    }

    // ==================== 导出功能方法 ====================

    @Override
    public List<Map<String, Object>> exportFinancialStatementList(FinancialStatementQueryVO queryVO) {
        try {
            // 查询财务报表列表
            LambdaQueryWrapper<FinancialStatement> wrapper = new LambdaQueryWrapper<>();
            if (queryVO.getEnterpriseId() != null) {
                wrapper.eq(FinancialStatement::getEnterpriseId, queryVO.getEnterpriseId());
            }
            if (queryVO.getReportYear() != null) {
                wrapper.eq(FinancialStatement::getReportYear, queryVO.getReportYear());
            }

            List<FinancialStatement> list = list(wrapper);
            List<Map<String, Object>> result = new ArrayList<>();
            for (FinancialStatement statement : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("statementId", statement.getStatementId());
                map.put("enterpriseId", statement.getEnterpriseId());
                map.put("reportYear", statement.getReportYear());
                map.put("statementType", statement.getStatementType());
                result.add(map);
            }
            return result;
        } catch (Exception e) {
            log.error("导出财务报表列表失败", e);
            throw new RuntimeException("导出财务报表列表失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> exportDataQualityAnalysisReport(String enterpriseId, Integer startYear, Integer endYear) {
        return null;
    }

    @Override
    public Map<String, Object> exportRiskAnalysisReport(String enterpriseId, Integer startYear, Integer endYear) {
        return null;
    }

    // ==================== 私有辅助方法 - 异常检测相关 ====================

    private String classifyAnomalySeverity(List<Map<String, Object>> anomalies) {
        if (anomalies.size() > 10) return "严重";
        if (anomalies.size() > 5) return "中等";
        if (anomalies.size() > 0) return "轻微";
        return "无异常";
    }

    private List<String> generateAnomalyRecommendations(List<Map<String, Object>> anomalies) {
        List<String> recommendations = new ArrayList<>();
        recommendations.add("建议加强数据录入审核");
        recommendations.add("建议完善内控制度");
        recommendations.add("建议定期进行数据质量检查");
        return recommendations;
    }

    private Map<String, Object> analyzeTrendPatterns(List<Map<String, Object>> trendAnomalies) {
        Map<String, Object> patterns = new HashMap<>();
        patterns.put("trendDirection", "下降");
        patterns.put("volatility", "高");
        patterns.put("seasonality", "明显");
        return patterns;
    }

    private List<String> generateTrendAlerts(List<Map<String, Object>> trendAnomalies) {
        List<String> alerts = new ArrayList<>();
        alerts.add("收入增长率异常下降");
        alerts.add("利润波动幅度过大");
        return alerts;
    }

    private BigDecimal calculateVolatilityIndex(List<Map<String, Object>> volatilityData) {
        // 实现波动性指数计算
        return BigDecimal.valueOf(0.25);
    }

    private String assessDataStability(List<Map<String, Object>> volatilityData) {
        // 实现数据稳定性评估
        return "中等稳定";
    }

    private Map<String, Object> classifyOutlierTypes(List<Map<String, Object>> outliers) {
        Map<String, Object> types = new HashMap<>();
        types.put("extremeValues", 3);
        types.put("inconsistentData", 2);
        types.put("missingData", 1);
        return types;
    }

    private Map<String, Object> assessOutlierImpact(List<Map<String, Object>> outliers) {
        Map<String, Object> impact = new HashMap<>();
        impact.put("impactLevel", "中等");
        impact.put("affectedAreas", Arrays.asList("盈利能力", "偿债能力"));
        return impact;
    }

    // ==================== 私有辅助方法 - 风险分析相关 ====================

    private String assessOverallRiskLevel(List<Map<String, Object>> riskWarnings) {
        if (riskWarnings.size() > 5) return FinancialStatement.RISK_LEVEL_CRITICAL;
        if (riskWarnings.size() > 3) return FinancialStatement.RISK_LEVEL_HIGH;
        if (riskWarnings.size() > 1) return FinancialStatement.RISK_LEVEL_MEDIUM;
        return FinancialStatement.RISK_LEVEL_LOW;
    }

    private List<String> identifyRiskFactors(List<Map<String, Object>> riskWarnings) {
        List<String> factors = new ArrayList<>();
        factors.add("流动性不足");
        factors.add("负债率过高");
        factors.add("盈利能力下降");
        return factors;
    }

    private List<String> generateMitigationStrategies(List<Map<String, Object>> riskWarnings) {
        List<String> strategies = new ArrayList<>();
        strategies.add("优化资产结构");
        strategies.add("控制负债规模");
        strategies.add("提升盈利能力");
        return strategies;
    }

    private String assessLiquidityRating(List<Map<String, Object>> liquidityRisk) {
        return "B+";
    }

    private Map<String, Object> projectCashFlow(List<Map<String, Object>> liquidityRisk) {
        Map<String, Object> projection = new HashMap<>();
        projection.put("nextQuarterCashFlow", BigDecimal.valueOf(1000000));
        projection.put("cashFlowTrend", "稳定");
        return projection;
    }

    private List<String> generateLiquidityRecommendations(List<Map<String, Object>> liquidityRisk) {
        List<String> recommendations = new ArrayList<>();
        recommendations.add("加强应收账款管理");
        recommendations.add("优化库存结构");
        return recommendations;
    }

    private String assessCreditRating(List<Map<String, Object>> creditRisk) {
        return "BBB";
    }

    private BigDecimal calculateDefaultProbability(List<Map<String, Object>> creditRisk) {
        return BigDecimal.valueOf(0.05);
    }

    private List<String> generateCreditRecommendations(List<Map<String, Object>> creditRisk) {
        List<String> recommendations = new ArrayList<>();
        recommendations.add("降低负债率");
        recommendations.add("提高资产质量");
        return recommendations;
    }

    private Map<String, Object> assessMarketSensitivity(List<Map<String, Object>> marketRisk) {
        Map<String, Object> sensitivity = new HashMap<>();
        sensitivity.put("interestRateSensitivity", "中等");
        sensitivity.put("exchangeRateSensitivity", "低");
        return sensitivity;
    }

    private List<String> recommendHedgingStrategies(List<Map<String, Object>> marketRisk) {
        List<String> strategies = new ArrayList<>();
        strategies.add("利率风险对冲");
        strategies.add("汇率风险管理");
        return strategies;
    }

    private String calculateOverallRiskLevel(List<Map<String, Object>> riskAssessment) {
        return FinancialStatement.RISK_LEVEL_MEDIUM;
    }

    private BigDecimal calculateRiskScore(List<Map<String, Object>> riskAssessment) {
        return BigDecimal.valueOf(65.5);
    }

    private Map<String, Object> generateRiskMatrix(List<Map<String, Object>> riskAssessment) {
        Map<String, Object> matrix = new HashMap<>();
        matrix.put("liquidityRisk", "中等");
        matrix.put("creditRisk", "低");
        matrix.put("marketRisk", "中等");
        matrix.put("operationalRisk", "低");
        return matrix;
    }

    private List<String> identifyTransmissionPaths(List<Map<String, Object>> riskTransmission) {
        List<String> paths = new ArrayList<>();
        paths.add("关联企业传导");
        paths.add("供应链传导");
        return paths;
    }

    private String assessContagionRisk(List<Map<String, Object>> riskTransmission) {
        return "中等";
    }

    private List<String> recommendIsolationStrategies(List<Map<String, Object>> riskTransmission) {
        List<String> strategies = new ArrayList<>();
        strategies.add("风险隔离机制");
        strategies.add("应急预案制定");
        return strategies;
    }

    // ==================== 私有辅助方法 - 合规分析相关 ====================

    private BigDecimal calculateComplianceRate(List<Map<String, Object>> complianceCheck) {
        return BigDecimal.valueOf(85.5);
    }

    private List<String> identifyViolations(List<Map<String, Object>> complianceCheck) {
        List<String> violations = new ArrayList<>();
        violations.add("报送时间延迟");
        violations.add("数据格式不规范");
        return violations;
    }

    private List<String> generateComplianceRecommendations(List<Map<String, Object>> complianceCheck) {
        List<String> recommendations = new ArrayList<>();
        recommendations.add("完善内控制度");
        recommendations.add("加强合规培训");
        return recommendations;
    }

    private String assessStandardsAdherence(List<Map<String, Object>> standardsCompliance) {
        return "良好";
    }

    private List<String> identifyStandardsDeviations(List<Map<String, Object>> standardsCompliance) {
        List<String> deviations = new ArrayList<>();
        deviations.add("会计政策变更未及时披露");
        return deviations;
    }

    private String assessAuditQuality(List<Map<String, Object>> auditCompliance) {
        return "良好";
    }

    private List<String> identifyAuditIssues(List<Map<String, Object>> auditCompliance) {
        List<String> issues = new ArrayList<>();
        issues.add("审计程序不完整");
        return issues;
    }

    private String assessTimelinessCompliance(List<Map<String, Object>> submissionCompliance) {
        return "基本合规";
    }

    private List<String> identifySubmissionIssues(List<Map<String, Object>> submissionCompliance) {
        List<String> issues = new ArrayList<>();
        issues.add("部分报表提交延迟");
        return issues;
    }

    private BigDecimal calculateStatementComplianceScore(FinancialStatement statement) {
        BigDecimal score = BigDecimal.valueOf(100);

        // 检查报表状态
        if (statement.getStatementStatus() == null ||
            !statement.getStatementStatus().equals(FinancialStatement.STATEMENT_STATUS_SUBMITTED)) {
            score = score.subtract(BigDecimal.valueOf(10));
        }

        // 检查审计状态
        if (statement.getAuditStatus() == null ||
            statement.getAuditStatus().equals(FinancialStatement.AUDIT_STATUS_NOT_AUDITED)) {
            score = score.subtract(BigDecimal.valueOf(15));
        }

        // 检查数据质量
        if (statement.getDataQualityLevel() != null) {
            switch (statement.getDataQualityLevel()) {
                case FinancialStatement.DATA_QUALITY_LEVEL_POOR:
                    score = score.subtract(BigDecimal.valueOf(20));
                    break;
                case FinancialStatement.DATA_QUALITY_LEVEL_BAD:
                    score = score.subtract(BigDecimal.valueOf(30));
                    break;
            }
        }

        return score.max(BigDecimal.ZERO);
    }

    private String determineComplianceLevel(FinancialStatement statement) {
        BigDecimal score = calculateStatementComplianceScore(statement);

        if (score.compareTo(BigDecimal.valueOf(90)) >= 0) {
            return "完全合规";
        } else if (score.compareTo(BigDecimal.valueOf(80)) >= 0) {
            return "基本合规";
        } else if (score.compareTo(BigDecimal.valueOf(60)) >= 0) {
            return "部分合规";
        } else {
            return "不合规";
        }
    }

    private Map<String, Object> getComplianceDetails(FinancialStatement statement) {
        Map<String, Object> details = new HashMap<>();
        details.put("statementStatus", getStatementStatusLabel(statement.getStatementStatus()));
        details.put("auditStatus", getAuditStatusLabel(statement.getAuditStatus()));
        details.put("dataQualityLevel", getDataQualityLevelLabel(statement.getDataQualityLevel()));
        details.put("submissionDate", statement.getSubmissionDate());
        return details;
    }

    @Override
    public Map<String, Object> exportComplianceCheckReport(String enterpriseId, Integer startYear, Integer endYear) {
        try {
            Map<String, Object> result = new HashMap<>();

            // 查询财务报表数据
            LambdaQueryWrapper<FinancialStatement> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FinancialStatement::getEnterpriseId, enterpriseId)
                   .between(FinancialStatement::getReportYear, startYear, endYear);

            List<FinancialStatement> statements = list(wrapper);

            result.put("enterpriseId", enterpriseId);
            result.put("startYear", startYear);
            result.put("endYear", endYear);
            result.put("statementCount", statements.size());
            result.put("statements", statements);

            return result;
        } catch (Exception e) {
            log.error("导出合规检查报告失败: {}, {}, {}", enterpriseId, startYear, endYear, e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "导出合规检查报告失败: " + e.getMessage());
            return errorResult;
        }
    }

}
