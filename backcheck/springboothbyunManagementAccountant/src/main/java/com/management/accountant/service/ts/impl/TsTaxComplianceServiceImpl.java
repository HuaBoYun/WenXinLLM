package com.management.accountant.service.ts.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.ts.TsTaxCompliance;
import com.management.accountant.mapper.ts.TsTaxComplianceMapper;
import com.management.accountant.service.ts.TsTaxComplianceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 税务合规检查服务实现类
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@Service
public class TsTaxComplianceServiceImpl extends ServiceImpl<TsTaxComplianceMapper, TsTaxCompliance> implements TsTaxComplianceService {

    // ==================== 基础CRUD操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsTaxCompliance createCompliance(Long tenantId, TsTaxCompliance compliance) {
        log.info("创建合规检查，租户ID: {}", tenantId);
        
        // 设置租户ID
        compliance.setTenantId(tenantId);
        
        // 生成合规检查编号
        if (!StringUtils.hasText(compliance.getComplianceCode())) {
            compliance.setComplianceCode(generateComplianceCode(tenantId));
        }
        
        // 设置默认值
        if (compliance.getCheckStatus() == null) {
            compliance.setCheckStatus("DRAFT");
        }
        if (compliance.getComplianceStatus() == null) {
            compliance.setComplianceStatus("PENDING");
        }
        if (compliance.getRiskLevel() == null) {
            compliance.setRiskLevel("MEDIUM");
        }
        if (compliance.getPriority() == null) {
            compliance.setPriority("NORMAL");
        }
        if (compliance.getCheckProgress() == null) {
            compliance.setCheckProgress(BigDecimal.ZERO);
        }
        if (compliance.getRectificationProgress() == null) {
            compliance.setRectificationProgress(BigDecimal.ZERO);
        }
        
        // 保存到数据库
        save(compliance);
        
        log.info("合规检查创建成功，ID: {}", compliance.getComplianceId());
        return compliance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsTaxCompliance updateCompliance(Long tenantId, Long complianceId, TsTaxCompliance compliance) {
        log.info("更新合规检查，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        // 检查记录是否存在
        TsTaxCompliance existingCompliance = getComplianceDetail(tenantId, complianceId);
        if (existingCompliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        // 更新字段
        compliance.setComplianceId(complianceId);
        compliance.setTenantId(tenantId);
        
        // 更新到数据库
        updateById(compliance);
        
        log.info("合规检查更新成功，ID: {}", complianceId);
        return getComplianceDetail(tenantId, complianceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCompliance(Long tenantId, Long complianceId) {
        log.info("删除合规检查，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        // 检查记录是否存在
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        // 逻辑删除
        boolean result = removeById(complianceId);
        
        log.info("合规检查删除成功，ID: {}", complianceId);
        return result;
    }

    @Override
    public TsTaxCompliance getComplianceDetail(Long tenantId, Long complianceId) {
        QueryWrapper<TsTaxCompliance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tenant_id", tenantId)
                   .eq("compliance_id", complianceId);
        return getOne(queryWrapper);
    }

    @Override
    public TsTaxCompliance getComplianceByCode(Long tenantId, String complianceCode) {
        return baseMapper.selectByComplianceCode(tenantId, complianceCode);
    }

    @Override
    public IPage<TsTaxCompliance> getCompliancePage(Long tenantId, Integer current, Integer size, Map<String, Object> params) {
        Page<TsTaxCompliance> page = new Page<>(current, size);
        return baseMapper.selectCompliancePage(page, tenantId, params);
    }

    // ==================== 合规检查管理功能 ====================

    @Override
    public String generateComplianceCode(Long tenantId) {
        String prefix = "TC";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return prefix + timestamp + random;
    }

    @Override
    public Map<String, Object> validateComplianceData(Long tenantId, TsTaxCompliance compliance) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        // 验证必填字段
        if (!StringUtils.hasText(compliance.getComplianceName())) {
            errors.add("合规检查名称不能为空");
        }
        if (!StringUtils.hasText(compliance.getComplianceType())) {
            errors.add("合规检查类型不能为空");
        }
        if (!StringUtils.hasText(compliance.getCheckScope())) {
            errors.add("检查范围不能为空");
        }
        
        // 验证时间逻辑
        if (compliance.getStartTime() != null && compliance.getEndTime() != null) {
            if (compliance.getStartTime().isAfter(compliance.getEndTime())) {
                errors.add("开始时间不能晚于结束时间");
            }
        }
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startComplianceCheck(Long tenantId, Long complianceId) {
        log.info("启动合规检查，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        // 更新状态
        compliance.setCheckStatus("IN_PROGRESS");
        compliance.setActualCheckTime(LocalDateTime.now());
        compliance.setCheckProgress(BigDecimal.valueOf(10));
        
        return updateById(compliance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean pauseComplianceCheck(Long tenantId, Long complianceId) {
        log.info("暂停合规检查，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        compliance.setCheckStatus("PAUSED");
        return updateById(compliance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resumeComplianceCheck(Long tenantId, Long complianceId) {
        log.info("恢复合规检查，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        compliance.setCheckStatus("IN_PROGRESS");
        return updateById(compliance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeComplianceCheck(Long tenantId, Long complianceId, Map<String, Object> completionData) {
        log.info("完成合规检查，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        // 更新完成信息
        compliance.setCheckStatus("COMPLETED");
        compliance.setCompletionTime(LocalDateTime.now());
        compliance.setCheckProgress(BigDecimal.valueOf(100));
        
        // 设置检查结果
        if (completionData.containsKey("checkResult")) {
            compliance.setCheckResult((String) completionData.get("checkResult"));
        }
        if (completionData.containsKey("complianceScore")) {
            compliance.setComplianceScore(new BigDecimal(completionData.get("complianceScore").toString()));
        }
        if (completionData.containsKey("riskScore")) {
            compliance.setRiskScore(new BigDecimal(completionData.get("riskScore").toString()));
        }
        if (completionData.containsKey("issueCount")) {
            compliance.setIssueCount((Integer) completionData.get("issueCount"));
        }
        
        return updateById(compliance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelComplianceCheck(Long tenantId, Long complianceId, String reason) {
        log.info("取消合规检查，租户ID: {}, 检查ID: {}, 原因: {}", tenantId, complianceId, reason);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        compliance.setCheckStatus("CANCELLED");
        compliance.setRemarks(reason);
        
        return updateById(compliance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCheckProgress(Long tenantId, Long complianceId, Double progress) {
        log.info("更新检查进度，租户ID: {}, 检查ID: {}, 进度: {}%", tenantId, complianceId, progress);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        compliance.setCheckProgress(BigDecimal.valueOf(progress));
        
        // 根据进度自动更新状态
        if (progress >= 100) {
            compliance.setCheckStatus("COMPLETED");
            compliance.setCompletionTime(LocalDateTime.now());
        } else if (progress > 0) {
            compliance.setCheckStatus("IN_PROGRESS");
        }
        
        return updateById(compliance);
    }

    // ==================== 合规规则管理 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean configureComplianceRule(Long tenantId, Long complianceId, Map<String, Object> ruleConfig) {
        log.info("配置合规规则，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        // 更新规则配置
        if (ruleConfig.containsKey("ruleName")) {
            compliance.setRuleName((String) ruleConfig.get("ruleName"));
        }
        if (ruleConfig.containsKey("ruleDescription")) {
            compliance.setRuleDescription((String) ruleConfig.get("ruleDescription"));
        }
        if (ruleConfig.containsKey("checkMethod")) {
            compliance.setCheckMethod((String) ruleConfig.get("checkMethod"));
        }
        if (ruleConfig.containsKey("checkFrequency")) {
            compliance.setCheckFrequency((String) ruleConfig.get("checkFrequency"));
        }
        
        return updateById(compliance);
    }

    @Override
    public Map<String, Object> executeComplianceCheck(Long tenantId, Long complianceId) {
        log.info("执行合规检查，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        
        // 模拟执行合规检查
        try {
            // 更新状态为执行中
            compliance.setCheckStatus("IN_PROGRESS");
            compliance.setActualCheckTime(LocalDateTime.now());
            updateById(compliance);
            
            // 模拟检查过程
            Thread.sleep(1000); // 模拟检查耗时
            
            // 生成检查结果
            Random random = new Random();
            BigDecimal complianceScore = BigDecimal.valueOf(60 + random.nextInt(40)); // 60-100分
            BigDecimal riskScore = BigDecimal.valueOf(random.nextInt(100)); // 0-100分
            int issueCount = random.nextInt(10); // 0-9个问题
            
            // 更新检查结果
            compliance.setCheckStatus("COMPLETED");
            compliance.setCompletionTime(LocalDateTime.now());
            compliance.setCheckProgress(BigDecimal.valueOf(100));
            compliance.setComplianceScore(complianceScore);
            compliance.setRiskScore(riskScore);
            compliance.setIssueCount(issueCount);
            
            // 根据评分确定合规状态
            if (complianceScore.compareTo(BigDecimal.valueOf(80)) >= 0) {
                compliance.setComplianceStatus("COMPLIANT");
            } else if (complianceScore.compareTo(BigDecimal.valueOf(60)) >= 0) {
                compliance.setComplianceStatus("PARTIALLY_COMPLIANT");
            } else {
                compliance.setComplianceStatus("NON_COMPLIANT");
            }
            
            // 根据风险评分确定风险等级
            if (riskScore.compareTo(BigDecimal.valueOf(80)) >= 0) {
                compliance.setRiskLevel("HIGH");
            } else if (riskScore.compareTo(BigDecimal.valueOf(50)) >= 0) {
                compliance.setRiskLevel("MEDIUM");
            } else {
                compliance.setRiskLevel("LOW");
            }
            
            updateById(compliance);
            
            result.put("success", true);
            result.put("complianceScore", complianceScore);
            result.put("riskScore", riskScore);
            result.put("issueCount", issueCount);
            result.put("complianceStatus", compliance.getComplianceStatus());
            result.put("riskLevel", compliance.getRiskLevel());
            
        } catch (Exception e) {
            log.error("执行合规检查失败", e);
            compliance.setCheckStatus("FAILED");
            compliance.setCheckResult("检查执行失败: " + e.getMessage());
            updateById(compliance);
            
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> assessComplianceRisk(Long tenantId, Long complianceId) {
        log.info("评估合规风险，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        
        // 模拟风险评估
        Random random = new Random();
        BigDecimal riskScore = BigDecimal.valueOf(random.nextInt(100));
        String riskLevel;
        String riskDescription;
        
        if (riskScore.compareTo(BigDecimal.valueOf(80)) >= 0) {
            riskLevel = "HIGH";
            riskDescription = "高风险：存在重大合规风险，需要立即处理";
        } else if (riskScore.compareTo(BigDecimal.valueOf(50)) >= 0) {
            riskLevel = "MEDIUM";
            riskDescription = "中等风险：存在一定合规风险，建议及时处理";
        } else {
            riskLevel = "LOW";
            riskDescription = "低风险：合规风险较小，可正常运营";
        }
        
        // 更新风险信息
        compliance.setRiskScore(riskScore);
        compliance.setRiskLevel(riskLevel);
        updateById(compliance);
        
        result.put("riskScore", riskScore);
        result.put("riskLevel", riskLevel);
        result.put("riskDescription", riskDescription);
        
        return result;
    }

    @Override
    public Map<String, Object> generateComplianceReport(Long tenantId, Long complianceId) {
        log.info("生成合规报告，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        Map<String, Object> report = new HashMap<>();
        
        // 基本信息
        report.put("complianceCode", compliance.getComplianceCode());
        report.put("complianceName", compliance.getComplianceName());
        report.put("complianceType", compliance.getComplianceType());
        report.put("checkStatus", compliance.getCheckStatus());
        report.put("complianceStatus", compliance.getComplianceStatus());
        
        // 检查结果
        report.put("complianceScore", compliance.getComplianceScore());
        report.put("riskScore", compliance.getRiskScore());
        report.put("riskLevel", compliance.getRiskLevel());
        report.put("issueCount", compliance.getIssueCount());
        
        // 时间信息
        report.put("startTime", compliance.getStartTime());
        report.put("endTime", compliance.getEndTime());
        report.put("actualCheckTime", compliance.getActualCheckTime());
        report.put("completionTime", compliance.getCompletionTime());
        
        // 更新报告生成信息
        String reportPath = "/reports/compliance/" + compliance.getComplianceCode() + "_" + 
                           LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".pdf";
        compliance.setReportPath(reportPath);
        compliance.setReportGenerationTime(LocalDateTime.now());
        updateById(compliance);
        
        report.put("reportPath", reportPath);
        report.put("generationTime", LocalDateTime.now());
        
        return report;
    }

    @Override
    public Map<String, Object> analyzeComplianceResult(Long tenantId, Long complianceId) {
        log.info("分析合规结果，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        Map<String, Object> analysis = new HashMap<>();
        
        // 合规性分析
        BigDecimal complianceScore = compliance.getComplianceScore();
        if (complianceScore != null) {
            if (complianceScore.compareTo(BigDecimal.valueOf(90)) >= 0) {
                analysis.put("complianceLevel", "优秀");
                analysis.put("complianceDescription", "合规性表现优秀，继续保持");
            } else if (complianceScore.compareTo(BigDecimal.valueOf(80)) >= 0) {
                analysis.put("complianceLevel", "良好");
                analysis.put("complianceDescription", "合规性表现良好，有小幅提升空间");
            } else if (complianceScore.compareTo(BigDecimal.valueOf(60)) >= 0) {
                analysis.put("complianceLevel", "一般");
                analysis.put("complianceDescription", "合规性表现一般，需要改进");
            } else {
                analysis.put("complianceLevel", "较差");
                analysis.put("complianceDescription", "合规性表现较差，需要重点关注");
            }
        }
        
        // 风险分析
        BigDecimal riskScore = compliance.getRiskScore();
        if (riskScore != null) {
            analysis.put("riskScore", riskScore);
            analysis.put("riskLevel", compliance.getRiskLevel());
            
            if (riskScore.compareTo(BigDecimal.valueOf(80)) >= 0) {
                analysis.put("riskSuggestion", "风险较高，建议立即采取措施降低风险");
            } else if (riskScore.compareTo(BigDecimal.valueOf(50)) >= 0) {
                analysis.put("riskSuggestion", "风险适中，建议定期监控并适时调整");
            } else {
                analysis.put("riskSuggestion", "风险较低，可正常运营");
            }
        }
        
        // 问题分析
        Integer issueCount = compliance.getIssueCount();
        if (issueCount != null) {
            analysis.put("issueCount", issueCount);
            if (issueCount > 5) {
                analysis.put("issueSeverity", "严重");
                analysis.put("issueSuggestion", "问题较多，需要制定详细的整改计划");
            } else if (issueCount > 2) {
                analysis.put("issueSeverity", "中等");
                analysis.put("issueSuggestion", "存在一些问题，建议逐步整改");
            } else if (issueCount > 0) {
                analysis.put("issueSeverity", "轻微");
                analysis.put("issueSuggestion", "问题较少，可按计划处理");
            } else {
                analysis.put("issueSeverity", "无");
                analysis.put("issueSuggestion", "未发现问题，表现良好");
            }
        }
        
        return analysis;
    }

    // ==================== 整改管理功能 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRectificationPlan(Long tenantId, Long complianceId, Map<String, Object> planData) {
        log.info("创建整改计划，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        // 更新整改信息
        compliance.setRectificationStatus("PLANNED");
        compliance.setRectificationProgress(BigDecimal.ZERO);
        
        if (planData.containsKey("rectificationResponsible")) {
            compliance.setRectificationResponsible((String) planData.get("rectificationResponsible"));
        }
        if (planData.containsKey("rectificationDeadline")) {
            compliance.setRectificationDeadline((LocalDateTime) planData.get("rectificationDeadline"));
        }
        if (planData.containsKey("rectificationSuggestion")) {
            compliance.setRectificationSuggestion((String) planData.get("rectificationSuggestion"));
        }
        
        return updateById(compliance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRectificationProgress(Long tenantId, Long complianceId, Double progress) {
        log.info("更新整改进度，租户ID: {}, 检查ID: {}, 进度: {}%", tenantId, complianceId, progress);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        compliance.setRectificationProgress(BigDecimal.valueOf(progress));
        
        // 根据进度更新状态
        if (progress >= 100) {
            compliance.setRectificationStatus("COMPLETED");
            compliance.setRectificationCompletionTime(LocalDateTime.now());
        } else if (progress > 0) {
            compliance.setRectificationStatus("IN_PROGRESS");
        }
        
        return updateById(compliance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeRectification(Long tenantId, Long complianceId, Map<String, Object> completionData) {
        log.info("完成整改，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        compliance.setRectificationStatus("COMPLETED");
        compliance.setRectificationProgress(BigDecimal.valueOf(100));
        compliance.setRectificationCompletionTime(LocalDateTime.now());
        
        return updateById(compliance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean requestRecheck(Long tenantId, Long complianceId) {
        log.info("申请复查，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        compliance.setRecheckStatus("REQUESTED");
        
        return updateById(compliance);
    }

    @Override
    public Map<String, Object> executeRecheck(Long tenantId, Long complianceId) {
        log.info("执行复查，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        
        // 模拟复查过程
        compliance.setRecheckStatus("IN_PROGRESS");
        compliance.setRecheckTime(LocalDateTime.now());
        updateById(compliance);
        
        // 模拟复查结果
        Random random = new Random();
        boolean recheckPassed = random.nextBoolean();
        
        result.put("recheckPassed", recheckPassed);
        result.put("recheckTime", LocalDateTime.now());
        
        if (recheckPassed) {
            result.put("recheckResult", "复查通过，整改效果良好");
        } else {
            result.put("recheckResult", "复查未通过，需要进一步整改");
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeRecheck(Long tenantId, Long complianceId, Map<String, Object> recheckResult) {
        log.info("完成复查，租户ID: {}, 检查ID: {}", tenantId, complianceId);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        compliance.setRecheckStatus("COMPLETED");
        compliance.setRecheckTime(LocalDateTime.now());
        
        if (recheckResult.containsKey("recheckResult")) {
            compliance.setRecheckResult((String) recheckResult.get("recheckResult"));
        }
        
        return updateById(compliance);
    }

    // ==================== 查询统计功能 ====================

    @Override
    public List<TsTaxCompliance> getCompliancesByType(Long tenantId, String complianceType) {
        return baseMapper.selectByComplianceType(tenantId, complianceType);
    }

    @Override
    public List<TsTaxCompliance> getCompliancesByCheckStatus(Long tenantId, String checkStatus) {
        return baseMapper.selectByCheckStatus(tenantId, checkStatus);
    }

    @Override
    public List<TsTaxCompliance> getCompliancesByComplianceStatus(Long tenantId, String complianceStatus) {
        return baseMapper.selectByComplianceStatus(tenantId, complianceStatus);
    }

    @Override
    public List<TsTaxCompliance> getCompliancesByRiskLevel(Long tenantId, String riskLevel) {
        return baseMapper.selectByRiskLevel(tenantId, riskLevel);
    }

    @Override
    public List<TsTaxCompliance> getCompliancesByPriority(Long tenantId, String priority) {
        return baseMapper.selectByPriority(tenantId, priority);
    }

    @Override
    public List<TsTaxCompliance> getCompliancesByChecker(Long tenantId, String checker) {
        return baseMapper.selectByChecker(tenantId, checker);
    }

    @Override
    public List<TsTaxCompliance> getCompliancesByRectificationStatus(Long tenantId, String rectificationStatus) {
        return baseMapper.selectByRectificationStatus(tenantId, rectificationStatus);
    }

    @Override
    public List<TsTaxCompliance> getExpiringSoonCompliances(Long tenantId, Integer days) {
        return baseMapper.selectExpiringSoon(tenantId, days);
    }

    @Override
    public List<TsTaxCompliance> getOverdueCompliances(Long tenantId) {
        return baseMapper.selectOverdue(tenantId);
    }

    @Override
    public List<TsTaxCompliance> getHighRiskCompliances(Long tenantId) {
        return baseMapper.selectHighRisk(tenantId);
    }

    @Override
    public List<TsTaxCompliance> getNeedRectificationCompliances(Long tenantId) {
        return baseMapper.selectNeedRectification(tenantId);
    }

    // ==================== 统计分析功能 ====================

    @Override
    public Map<String, Object> getComplianceOverview(Long tenantId) {
        return baseMapper.selectOverviewStats(tenantId);
    }

    @Override
    public List<Map<String, Object>> countCompliancesByCheckStatus(Long tenantId) {
        return baseMapper.countByCheckStatus(tenantId);
    }

    @Override
    public List<Map<String, Object>> countCompliancesByComplianceStatus(Long tenantId) {
        return baseMapper.countByComplianceStatus(tenantId);
    }

    @Override
    public List<Map<String, Object>> countCompliancesByType(Long tenantId) {
        return baseMapper.countByComplianceType(tenantId);
    }

    @Override
    public List<Map<String, Object>> countCompliancesByRiskLevel(Long tenantId) {
        return baseMapper.countByRiskLevel(tenantId);
    }

    @Override
    public List<Map<String, Object>> countCompliancesByPriority(Long tenantId) {
        return baseMapper.countByPriority(tenantId);
    }

    @Override
    public List<Map<String, Object>> countCompliancesByRectificationStatus(Long tenantId) {
        return baseMapper.countByRectificationStatus(tenantId);
    }

    @Override
    public List<Map<String, Object>> getCheckTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy) {
        return baseMapper.selectCheckTrend(tenantId, startDate, endDate, groupBy);
    }

    @Override
    public List<Map<String, Object>> getComplianceScoreTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy) {
        return baseMapper.selectComplianceScoreTrend(tenantId, startDate, endDate, groupBy);
    }

    @Override
    public List<Map<String, Object>> getRiskDistribution(Long tenantId) {
        return baseMapper.selectRiskDistribution(tenantId);
    }

    @Override
    public List<Map<String, Object>> getEffectivenessData(Long tenantId) {
        return baseMapper.selectEffectivenessData(tenantId);
    }

    @Override
    public List<Map<String, Object>> getCheckRanking(Long tenantId, String rankBy, Integer limit) {
        return baseMapper.selectCheckRanking(tenantId, rankBy, limit);
    }

    @Override
    public Map<String, Object> getCheckEfficiencyStats(Long tenantId) {
        return baseMapper.selectCheckEfficiencyStats(tenantId);
    }

    // ==================== 批量操作功能 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TsTaxCompliance> batchCreateCompliances(Long tenantId, List<TsTaxCompliance> compliances) {
        log.info("批量创建合规检查，租户ID: {}, 数量: {}", tenantId, compliances.size());
        
        List<TsTaxCompliance> results = new ArrayList<>();
        for (TsTaxCompliance compliance : compliances) {
            results.add(createCompliance(tenantId, compliance));
        }
        
        return results;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateCheckStatus(Long tenantId, List<Long> complianceIds, String checkStatus) {
        log.info("批量更新检查状态，租户ID: {}, 数量: {}, 状态: {}", tenantId, complianceIds.size(), checkStatus);
        
        int result = baseMapper.batchUpdateCheckStatus(tenantId, complianceIds, checkStatus, "system");
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateComplianceStatus(Long tenantId, List<Long> complianceIds, String complianceStatus) {
        log.info("批量更新合规状态，租户ID: {}, 数量: {}, 状态: {}", tenantId, complianceIds.size(), complianceStatus);
        
        int result = baseMapper.batchUpdateComplianceStatus(tenantId, complianceIds, complianceStatus, "system");
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateRectificationStatus(Long tenantId, List<Long> complianceIds, String rectificationStatus) {
        log.info("批量更新整改状态，租户ID: {}, 数量: {}, 状态: {}", tenantId, complianceIds.size(), rectificationStatus);
        
        int result = baseMapper.batchUpdateRectificationStatus(tenantId, complianceIds, rectificationStatus, "system");
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteCompliances(Long tenantId, List<Long> complianceIds) {
        log.info("批量删除合规检查，租户ID: {}, 数量: {}", tenantId, complianceIds.size());
        
        int result = baseMapper.batchDeleteByIds(tenantId, complianceIds, "system");
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchArchiveCompliances(Long tenantId, List<Long> complianceIds) {
        log.info("批量归档合规检查，租户ID: {}, 数量: {}", tenantId, complianceIds.size());
        
        int result = baseMapper.batchArchive(tenantId, complianceIds, "system");
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchActivateCompliances(Long tenantId, List<Long> complianceIds) {
        log.info("批量激活合规检查，租户ID: {}, 数量: {}", tenantId, complianceIds.size());
        
        int result = baseMapper.batchActivate(tenantId, complianceIds, "system");
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchImportCompliances(Long tenantId, List<Map<String, Object>> complianceData) {
        log.info("批量导入合规检查，租户ID: {}, 数量: {}", tenantId, complianceData.size());
        
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Map<String, Object> data : complianceData) {
            try {
                TsTaxCompliance compliance = new TsTaxCompliance();
                // 设置字段值（这里简化处理）
                compliance.setComplianceName((String) data.get("complianceName"));
                compliance.setComplianceType((String) data.get("complianceType"));
                compliance.setCheckScope((String) data.get("checkScope"));
                
                createCompliance(tenantId, compliance);
                successCount++;
            } catch (Exception e) {
                failCount++;
                errors.add("导入失败: " + e.getMessage());
            }
        }
        
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        
        return result;
    }

    @Override
    public List<Map<String, Object>> batchExportCompliances(Long tenantId, List<Long> complianceIds) {
        log.info("批量导出合规检查，租户ID: {}, 数量: {}", tenantId, complianceIds.size());
        
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Long complianceId : complianceIds) {
            TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
            if (compliance != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("complianceCode", compliance.getComplianceCode());
                data.put("complianceName", compliance.getComplianceName());
                data.put("complianceType", compliance.getComplianceType());
                data.put("checkStatus", compliance.getCheckStatus());
                data.put("complianceStatus", compliance.getComplianceStatus());
                data.put("riskLevel", compliance.getRiskLevel());
                data.put("complianceScore", compliance.getComplianceScore());
                data.put("riskScore", compliance.getRiskScore());
                data.put("createdTime", compliance.getCreatedTime());
                
                result.add(data);
            }
        }
        
        return result;
    }

    // ==================== 工具功能 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TsTaxCompliance copyCompliance(Long tenantId, Long sourceComplianceId, String newComplianceName) {
        log.info("复制合规检查，租户ID: {}, 源ID: {}, 新名称: {}", tenantId, sourceComplianceId, newComplianceName);
        
        TsTaxCompliance sourceCompliance = getComplianceDetail(tenantId, sourceComplianceId);
        if (sourceCompliance == null) {
            throw new RuntimeException("源合规检查记录不存在");
        }
        
        // 创建新的合规检查
        TsTaxCompliance newCompliance = new TsTaxCompliance();
        newCompliance.setTenantId(tenantId);
        newCompliance.setComplianceName(newComplianceName);
        newCompliance.setComplianceType(sourceCompliance.getComplianceType());
        newCompliance.setCheckScope(sourceCompliance.getCheckScope());
        newCompliance.setCheckObject(sourceCompliance.getCheckObject());
        newCompliance.setRuleName(sourceCompliance.getRuleName());
        newCompliance.setRuleDescription(sourceCompliance.getRuleDescription());
        newCompliance.setCheckMethod(sourceCompliance.getCheckMethod());
        newCompliance.setCheckFrequency(sourceCompliance.getCheckFrequency());
        newCompliance.setCheckCycle(sourceCompliance.getCheckCycle());
        newCompliance.setPriority(sourceCompliance.getPriority());
        
        return createCompliance(tenantId, newCompliance);
    }

    @Override
    public boolean sendComplianceReminder(Long tenantId, Long complianceId, String reminderType) {
        log.info("发送合规提醒，租户ID: {}, 检查ID: {}, 提醒类型: {}", tenantId, complianceId, reminderType);
        
        TsTaxCompliance compliance = getComplianceDetail(tenantId, complianceId);
        if (compliance == null) {
            throw new RuntimeException("合规检查记录不存在");
        }
        
        // 模拟发送提醒
        compliance.setNotificationStatus("SENT");
        compliance.setNotificationTime(LocalDateTime.now());
        updateById(compliance);
        
        return true;
    }

    // ==================== 系统维护功能 ====================

    @Override
    public Map<String, Object> systemHealthCheck(Long tenantId) {
        return baseMapper.systemHealthCheck(tenantId);
    }

    @Override
    public List<Map<String, Object>> dataConsistencyCheck(Long tenantId) {
        return baseMapper.dataConsistencyCheck(tenantId);
    }

    @Override
    public Map<String, Object> performanceStats(Long tenantId) {
        return baseMapper.performanceStats(tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanupExpiredData(Long tenantId, Integer days) {
        log.info("清理过期数据，租户ID: {}, 天数: {}", tenantId, days);
        
        int result = baseMapper.cleanupExpiredData(tenantId, days);
        return result >= 0;
    }
}
