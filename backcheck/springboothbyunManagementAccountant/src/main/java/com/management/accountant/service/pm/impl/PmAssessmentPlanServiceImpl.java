package com.management.accountant.service.pm.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.pm.PmAssessmentPlan;
import com.management.accountant.mapper.pm.PmAssessmentPlanMapper;
import com.management.accountant.service.pm.PmAssessmentPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 考核方案配置服务实现
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PmAssessmentPlanServiceImpl extends ServiceImpl<PmAssessmentPlanMapper, PmAssessmentPlan>
        implements PmAssessmentPlanService {

    @Autowired
    private PmAssessmentPlanMapper assessmentPlanMapper;

    @Override
    public IPage<PmAssessmentPlan> queryAssessmentPlanPage(Long current, Long size, Long organizationId,
                                                          String planType, String assessmentMode, String planStatus,
                                                          Integer assessmentYear, String keyword) {
        Page<PmAssessmentPlan> page = new Page<>(current, size);
        QueryWrapper<PmAssessmentPlan> queryWrapper = new QueryWrapper<>();
        
        if (organizationId != null) {
            queryWrapper.eq("organization_id", organizationId);
        }
        if (StringUtils.hasText(planType)) {
            queryWrapper.eq("plan_type", planType);
        }
        if (StringUtils.hasText(assessmentMode)) {
            queryWrapper.eq("assessment_mode", assessmentMode);
        }
        if (StringUtils.hasText(planStatus)) {
            queryWrapper.eq("plan_status", planStatus);
        }
        if (assessmentYear != null) {
            queryWrapper.eq("assessment_year", assessmentYear);
        }
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper -> wrapper.like("plan_name", keyword)
                                              .or().like("plan_description", keyword)
                                              .or().like("plan_code", keyword));
        }
        
        queryWrapper.orderByDesc("created_time");
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean createAssessmentPlan(PmAssessmentPlan assessmentPlan) {
        try {
            // 设置默认值
            if (assessmentPlan.getPlanStatus() == null) {
                assessmentPlan.setPlanStatus("DRAFT");
            }
            if (assessmentPlan.getApprovalStatus() == null) {
                assessmentPlan.setApprovalStatus("PENDING");
            }
            if (assessmentPlan.getIsEnabled() == null) {
                assessmentPlan.setIsEnabled(1);
            }
            if (assessmentPlan.getIsDefault() == null) {
                assessmentPlan.setIsDefault(0);
            }
            if (assessmentPlan.getAllowAppeal() == null) {
                assessmentPlan.setAllowAppeal(1);
            }
            if (assessmentPlan.getAppealDeadline() == null) {
                assessmentPlan.setAppealDeadline(7);
            }
            
            // 生成方案编码
            if (!StringUtils.hasText(assessmentPlan.getPlanCode())) {
                assessmentPlan.setPlanCode(generatePlanCode(assessmentPlan));
            }
            
            // 设置默认评分配置
            setDefaultScoringConfig(assessmentPlan);
            
            return this.save(assessmentPlan);
        } catch (Exception e) {
            log.error("创建考核方案失败", e);
            throw new RuntimeException("创建考核方案失败: " + e.getMessage());
        }
    }

    @Override
    public boolean updateAssessmentPlan(PmAssessmentPlan assessmentPlan) {
        try {
            // 验证方案状态
            PmAssessmentPlan existingPlan = this.getById(assessmentPlan.getPlanId());
            if (existingPlan == null) {
                throw new RuntimeException("考核方案不存在");
            }
            
            // 如果方案已激活，限制某些字段的修改
            if ("ACTIVE".equals(existingPlan.getPlanStatus())) {
                validateActiveAssessmentPlanUpdate(assessmentPlan, existingPlan);
            }
            
            return this.updateById(assessmentPlan);
        } catch (Exception e) {
            log.error("更新考核方案失败", e);
            throw new RuntimeException("更新考核方案失败: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteAssessmentPlan(Long planId) {
        try {
            PmAssessmentPlan assessmentPlan = this.getById(planId);
            if (assessmentPlan == null) {
                throw new RuntimeException("考核方案不存在");
            }
            
            // 检查方案状态
            if ("ACTIVE".equals(assessmentPlan.getPlanStatus())) {
                throw new RuntimeException("激活状态的方案不能删除");
            }
            
            return this.removeById(planId);
        } catch (Exception e) {
            log.error("删除考核方案失败", e);
            throw new RuntimeException("删除考核方案失败: " + e.getMessage());
        }
    }

    @Override
    public PmAssessmentPlan getAssessmentPlanById(Long planId) {
        try {
            return this.getById(planId);
        } catch (Exception e) {
            log.error("获取考核方案详情失败", e);
            throw new RuntimeException("获取考核方案详情失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> configureIndicators(Long planId, Map<String, Object> indicatorConfig) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            PmAssessmentPlan assessmentPlan = this.getById(planId);
            if (assessmentPlan == null) {
                throw new RuntimeException("考核方案不存在");
            }
            
            // 配置指标体系
            String indicatorSystemConfig = processIndicatorConfig(indicatorConfig);
            assessmentPlan.setWeightConfig(indicatorSystemConfig);
            
            // 更新指标体系信息
            if (indicatorConfig.containsKey("indicatorSystemId")) {
                assessmentPlan.setIndicatorSystemId(Long.valueOf(indicatorConfig.get("indicatorSystemId").toString()));
            }
            if (indicatorConfig.containsKey("indicatorSystemName")) {
                assessmentPlan.setIndicatorSystemName(indicatorConfig.get("indicatorSystemName").toString());
            }
            
            this.updateById(assessmentPlan);
            
            result.put("planId", planId);
            result.put("indicatorConfig", indicatorConfig);
            result.put("configTime", LocalDateTime.now());
            
            return result;
        } catch (Exception e) {
            log.error("配置考核指标失败", e);
            throw new RuntimeException("配置考核指标失败: " + e.getMessage());
        }
    }

    @Override
    public boolean setScoringRules(Long planId, Map<String, Object> scoringRules) {
        try {
            PmAssessmentPlan assessmentPlan = this.getById(planId);
            if (assessmentPlan == null) {
                throw new RuntimeException("考核方案不存在");
            }
            
            // 应用评分规则
            applyScoringRules(assessmentPlan, scoringRules);
            
            return this.updateById(assessmentPlan);
        } catch (Exception e) {
            log.error("设置评分规则失败", e);
            throw new RuntimeException("设置评分规则失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> configureProcess(Long planId, Map<String, Object> processConfig) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            PmAssessmentPlan assessmentPlan = this.getById(planId);
            if (assessmentPlan == null) {
                throw new RuntimeException("考核方案不存在");
            }
            
            // 配置考核流程
            String processConfigJson = processProcessConfig(processConfig);
            assessmentPlan.setProcessConfig(processConfigJson);
            
            // 更新流程信息
            if (processConfig.containsKey("processId")) {
                assessmentPlan.setProcessId(Long.valueOf(processConfig.get("processId").toString()));
            }
            if (processConfig.containsKey("processName")) {
                assessmentPlan.setProcessName(processConfig.get("processName").toString());
            }
            
            this.updateById(assessmentPlan);
            
            result.put("planId", planId);
            result.put("processConfig", processConfig);
            result.put("configTime", LocalDateTime.now());
            
            return result;
        } catch (Exception e) {
            log.error("配置考核流程失败", e);
            throw new RuntimeException("配置考核流程失败: " + e.getMessage());
        }
    }

    @Override
    public boolean setWeightConfig(Long planId, Map<String, Object> weightConfig) {
        try {
            PmAssessmentPlan assessmentPlan = this.getById(planId);
            if (assessmentPlan == null) {
                throw new RuntimeException("考核方案不存在");
            }
            
            // 验证权重配置
            validateWeightConfig(weightConfig);
            
            // 应用权重配置
            applyWeightConfig(assessmentPlan, weightConfig);
            
            return this.updateById(assessmentPlan);
        } catch (Exception e) {
            log.error("设置权重配置失败", e);
            throw new RuntimeException("设置权重配置失败: " + e.getMessage());
        }
    }

    @Override
    public boolean activateAssessmentPlan(Long planId) {
        try {
            PmAssessmentPlan assessmentPlan = this.getById(planId);
            if (assessmentPlan == null) {
                throw new RuntimeException("考核方案不存在");
            }
            
            // 验证方案配置完整性
            validateAssessmentPlanCompleteness(assessmentPlan);
            
            assessmentPlan.setPlanStatus("ACTIVE");
            return this.updateById(assessmentPlan);
        } catch (Exception e) {
            log.error("激活考核方案失败", e);
            throw new RuntimeException("激活考核方案失败: " + e.getMessage());
        }
    }

    @Override
    public boolean pauseAssessmentPlan(Long planId) {
        try {
            PmAssessmentPlan assessmentPlan = this.getById(planId);
            if (assessmentPlan == null) {
                throw new RuntimeException("考核方案不存在");
            }
            
            assessmentPlan.setPlanStatus("PAUSED");
            return this.updateById(assessmentPlan);
        } catch (Exception e) {
            log.error("暂停考核方案失败", e);
            throw new RuntimeException("暂停考核方案失败: " + e.getMessage());
        }
    }

    @Override
    public boolean completeAssessmentPlan(Long planId) {
        try {
            PmAssessmentPlan assessmentPlan = this.getById(planId);
            if (assessmentPlan == null) {
                throw new RuntimeException("考核方案不存在");
            }
            
            assessmentPlan.setPlanStatus("COMPLETED");
            return this.updateById(assessmentPlan);
        } catch (Exception e) {
            log.error("完成考核方案失败", e);
            throw new RuntimeException("完成考核方案失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> copyAssessmentPlan(Long planId, Map<String, Object> copyParams) {
        try {
            return assessmentPlanMapper.copyAssessmentPlan(planId, copyParams);
        } catch (Exception e) {
            log.error("复制考核方案失败", e);
            throw new RuntimeException("复制考核方案失败: " + e.getMessage());
        }
    }

    // 私有辅助方法
    private String generatePlanCode(PmAssessmentPlan assessmentPlan) {
        String prefix = "AP";
        if (StringUtils.hasText(assessmentPlan.getPlanType())) {
            prefix += "_" + assessmentPlan.getPlanType().substring(0, 3);
        }
        if (assessmentPlan.getAssessmentYear() != null) {
            prefix += "_" + assessmentPlan.getAssessmentYear();
        }
        return prefix + "_" + System.currentTimeMillis();
    }

    private void setDefaultScoringConfig(PmAssessmentPlan assessmentPlan) {
        if (assessmentPlan.getTotalScore() == null) {
            assessmentPlan.setTotalScore(new BigDecimal("100"));
        }
        if (assessmentPlan.getPassScore() == null) {
            assessmentPlan.setPassScore(new BigDecimal("60"));
        }
        if (assessmentPlan.getExcellentScore() == null) {
            assessmentPlan.setExcellentScore(new BigDecimal("90"));
        }
        if (assessmentPlan.getScoringMethod() == null) {
            assessmentPlan.setScoringMethod("SCORE");
        }
    }

    private void validateActiveAssessmentPlanUpdate(PmAssessmentPlan newPlan, PmAssessmentPlan existingPlan) {
        // 激活状态下不能修改的关键字段
        List<String> restrictedFields = Arrays.asList("planType", "assessmentMode", "assessmentCycle");
        
        // 这里可以添加具体的字段验证逻辑
        // 例如：检查planType是否被修改
        if (!Objects.equals(newPlan.getPlanType(), existingPlan.getPlanType())) {
            throw new RuntimeException("激活状态的方案不能修改方案类型");
        }
    }

    private String processIndicatorConfig(Map<String, Object> indicatorConfig) {
        // 处理指标配置，转换为JSON字符串
        // 这里应该实现具体的指标配置处理逻辑
        return indicatorConfig.toString();
    }

    private void applyScoringRules(PmAssessmentPlan assessmentPlan, Map<String, Object> scoringRules) {
        if (scoringRules.containsKey("scoringMethod")) {
            assessmentPlan.setScoringMethod(scoringRules.get("scoringMethod").toString());
        }
        if (scoringRules.containsKey("totalScore")) {
            assessmentPlan.setTotalScore(new BigDecimal(scoringRules.get("totalScore").toString()));
        }
        if (scoringRules.containsKey("passScore")) {
            assessmentPlan.setPassScore(new BigDecimal(scoringRules.get("passScore").toString()));
        }
        if (scoringRules.containsKey("excellentScore")) {
            assessmentPlan.setExcellentScore(new BigDecimal(scoringRules.get("excellentScore").toString()));
        }
        if (scoringRules.containsKey("gradeSettings")) {
            assessmentPlan.setGradeSettings(scoringRules.get("gradeSettings").toString());
        }
    }

    private String processProcessConfig(Map<String, Object> processConfig) {
        // 处理流程配置，转换为JSON字符串
        return processConfig.toString();
    }

    private void validateWeightConfig(Map<String, Object> weightConfig) {
        // 验证权重配置的合理性
        BigDecimal totalWeight = BigDecimal.ZERO;
        
        if (weightConfig.containsKey("selfEvaluationWeight")) {
            totalWeight = totalWeight.add(new BigDecimal(weightConfig.get("selfEvaluationWeight").toString()));
        }
        if (weightConfig.containsKey("superiorEvaluationWeight")) {
            totalWeight = totalWeight.add(new BigDecimal(weightConfig.get("superiorEvaluationWeight").toString()));
        }
        if (weightConfig.containsKey("peerEvaluationWeight")) {
            totalWeight = totalWeight.add(new BigDecimal(weightConfig.get("peerEvaluationWeight").toString()));
        }
        if (weightConfig.containsKey("subordinateEvaluationWeight")) {
            totalWeight = totalWeight.add(new BigDecimal(weightConfig.get("subordinateEvaluationWeight").toString()));
        }
        if (weightConfig.containsKey("customerEvaluationWeight")) {
            totalWeight = totalWeight.add(new BigDecimal(weightConfig.get("customerEvaluationWeight").toString()));
        }
        
        if (totalWeight.compareTo(new BigDecimal("100")) != 0) {
            throw new RuntimeException("权重配置总和必须等于100%");
        }
    }

    private void applyWeightConfig(PmAssessmentPlan assessmentPlan, Map<String, Object> weightConfig) {
        if (weightConfig.containsKey("selfEvaluationWeight")) {
            assessmentPlan.setSelfEvaluationWeight(new BigDecimal(weightConfig.get("selfEvaluationWeight").toString()));
        }
        if (weightConfig.containsKey("superiorEvaluationWeight")) {
            assessmentPlan.setSuperiorEvaluationWeight(new BigDecimal(weightConfig.get("superiorEvaluationWeight").toString()));
        }
        if (weightConfig.containsKey("peerEvaluationWeight")) {
            assessmentPlan.setPeerEvaluationWeight(new BigDecimal(weightConfig.get("peerEvaluationWeight").toString()));
        }
        if (weightConfig.containsKey("subordinateEvaluationWeight")) {
            assessmentPlan.setSubordinateEvaluationWeight(new BigDecimal(weightConfig.get("subordinateEvaluationWeight").toString()));
        }
        if (weightConfig.containsKey("customerEvaluationWeight")) {
            assessmentPlan.setCustomerEvaluationWeight(new BigDecimal(weightConfig.get("customerEvaluationWeight").toString()));
        }
        
        // 更新权重配置JSON
        assessmentPlan.setWeightConfig(weightConfig.toString());
    }

    private void validateAssessmentPlanCompleteness(PmAssessmentPlan assessmentPlan) {
        List<String> errors = new ArrayList<>();
        
        if (!StringUtils.hasText(assessmentPlan.getPlanName())) {
            errors.add("方案名称不能为空");
        }
        if (assessmentPlan.getStartTime() == null) {
            errors.add("开始时间不能为空");
        }
        if (assessmentPlan.getEndTime() == null) {
            errors.add("结束时间不能为空");
        }
        if (assessmentPlan.getIndicatorSystemId() == null) {
            errors.add("必须配置指标体系");
        }
        
        if (!errors.isEmpty()) {
            throw new RuntimeException("方案配置不完整：" + String.join(", ", errors));
        }
    }

    // 其他接口方法的简化实现
    @Override
    public List<Map<String, Object>> getAssessmentPlanTemplates(String templateType) {
        return assessmentPlanMapper.selectAssessmentPlanTemplates(templateType);
    }

    @Override
    public Map<String, Object> applyAssessmentPlanTemplate(Map<String, Object> templateParams) {
        return assessmentPlanMapper.applyAssessmentPlanTemplate(templateParams);
    }

    @Override
    public Map<String, Object> getAssessmentPlanStatistics(Long organizationId, String statisticsType, String statisticsPeriod) {
        return assessmentPlanMapper.selectAssessmentPlanStatistics(organizationId, statisticsType, statisticsPeriod);
    }

    @Override
    public Map<String, Object> batchOperateAssessmentPlans(Map<String, Object> batchData) {
        return assessmentPlanMapper.batchOperateAssessmentPlans(batchData);
    }

    @Override
    public Map<String, Object> importAssessmentPlans(Map<String, Object> importData) {
        return assessmentPlanMapper.importAssessmentPlans(importData);
    }

    @Override
    public Map<String, Object> exportAssessmentPlans(Map<String, Object> exportParams) {
        return assessmentPlanMapper.exportAssessmentPlans(exportParams);
    }

    @Override
    public Map<String, Object> validateAssessmentPlan(Long planId) {
        return assessmentPlanMapper.validateAssessmentPlan(planId);
    }

    @Override
    public Map<String, Object> previewAssessmentPlan(Long planId) {
        return assessmentPlanMapper.previewAssessmentPlan(planId);
    }

    @Override
    public boolean refreshAssessmentPlanCache(Long organizationId, String cacheType) {
        return assessmentPlanMapper.refreshAssessmentPlanCache(organizationId, cacheType) > 0;
    }

    @Override
    public Map<String, Object> getIndicatorSystem(Long planId) {
        return assessmentPlanMapper.selectIndicatorSystem(planId);
    }

    @Override
    public boolean setIndicatorSystem(Long planId, Map<String, Object> indicatorSystem) {
        return assessmentPlanMapper.updateIndicatorSystem(planId, indicatorSystem) > 0;
    }

    @Override
    public Map<String, Object> getScoringStandards(Long planId) {
        return assessmentPlanMapper.selectScoringStandards(planId);
    }

    @Override
    public boolean setScoringStandards(Long planId, Map<String, Object> scoringStandards) {
        return assessmentPlanMapper.updateScoringStandards(planId, scoringStandards) > 0;
    }

    @Override
    public Map<String, Object> getAssessmentCycleConfig(Long planId) {
        return assessmentPlanMapper.selectAssessmentCycleConfig(planId);
    }

    @Override
    public boolean setAssessmentCycleConfig(Long planId, Map<String, Object> cycleConfig) {
        return assessmentPlanMapper.updateAssessmentCycleConfig(planId, cycleConfig) > 0;
    }

    @Override
    public Map<String, Object> getApplicableScopeConfig(Long planId) {
        return assessmentPlanMapper.selectApplicableScopeConfig(planId);
    }

    @Override
    public boolean setApplicableScopeConfig(Long planId, Map<String, Object> scopeConfig) {
        return assessmentPlanMapper.updateApplicableScopeConfig(planId, scopeConfig) > 0;
    }

    @Override
    public Map<String, Object> getResultApplicationConfig(Long planId) {
        return assessmentPlanMapper.selectResultApplicationConfig(planId);
    }

    @Override
    public boolean setResultApplicationConfig(Long planId, Map<String, Object> applicationConfig) {
        return assessmentPlanMapper.updateResultApplicationConfig(planId, applicationConfig) > 0;
    }

    @Override
    public Map<String, Object> getAssessmentPlanExecutionStatus(Long planId) {
        return assessmentPlanMapper.selectAssessmentPlanExecutionStatus(planId);
    }

    @Override
    public List<Map<String, Object>> getAssessmentPlanParticipants(Long planId) {
        return assessmentPlanMapper.selectAssessmentPlanParticipants(planId);
    }

    @Override
    public boolean setAssessmentPlanParticipants(Long planId, List<Map<String, Object>> participants) {
        return assessmentPlanMapper.updateAssessmentPlanParticipants(planId, participants) > 0;
    }

    @Override
    public Map<String, Object> getAssessmentPlanProgressReport(Long planId, String reportType) {
        return assessmentPlanMapper.selectAssessmentPlanProgressReport(planId, reportType);
    }

    @Override
    public Map<String, Object> generateAssessmentPlanAnalysisReport(Long planId, Map<String, Object> analysisParams) {
        return assessmentPlanMapper.generateAssessmentPlanAnalysisReport(planId, analysisParams);
    }

    @Override
    public List<Map<String, Object>> getAssessmentPlanSuggestions(Long planId, String suggestionType) {
        return assessmentPlanMapper.selectAssessmentPlanSuggestions(planId, suggestionType);
    }

    @Override
    public Map<String, Object> optimizeAssessmentPlan(Long planId, Map<String, Object> optimizationParams) {
        return assessmentPlanMapper.optimizeAssessmentPlan(planId, optimizationParams);
    }

    @Override
    public Map<String, Object> evaluateAssessmentPlanEffectiveness(Long planId, Map<String, Object> evaluationParams) {
        return assessmentPlanMapper.evaluateAssessmentPlanEffectiveness(planId, evaluationParams);
    }

    @Override
    public Map<String, Object> assessAssessmentPlanRisk(Long planId) {
        return assessmentPlanMapper.assessAssessmentPlanRisk(planId);
    }

    @Override
    public Map<String, Object> checkAssessmentPlanCompliance(Long planId) {
        return assessmentPlanMapper.checkAssessmentPlanCompliance(planId);
    }
}
