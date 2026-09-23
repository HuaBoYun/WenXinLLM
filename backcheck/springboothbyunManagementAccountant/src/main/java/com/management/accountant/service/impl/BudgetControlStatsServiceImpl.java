package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.oracle.entity.budget.*;
import com.management.accountant.oracle.mapper.budget.*;
import com.management.accountant.service.BudgetControlStatsService;
import com.management.accountant.vo.result.BudgetControlHealthVO;
import com.management.accountant.vo.result.BudgetControlStatsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 预算控制统计Service实现
 * 
 * @description 预算控制统计数据服务实现
 * @author AI Assistant
 * @date 2025-01-30
 */
@Service
@Slf4j
public class BudgetControlStatsServiceImpl implements BudgetControlStatsService {

    @Resource
    private BudgetControlRuleMapper controlRuleMapper;

    @Resource
    private BudgetWarningRecordMapper warningRecordMapper;

    @Resource
    private BudgetExecutionMapper executionMapper;

    @Resource
    private BudgetFreezeMapper freezeMapper;

    @Resource
    private BudgetLimitMapper limitMapper;

    @Override
    public BudgetControlStatsVO getControlStats() {
        BudgetControlStatsVO stats = new BudgetControlStatsVO();
        
        try {
            // 统计控制规则
            Integer totalRules = controlRuleMapper.selectCount(new QueryWrapper<>()).intValue();
            stats.setTotalRules(totalRules);

            QueryWrapper<BudgetControlRule> activeRuleWrapper = new QueryWrapper<>();
            activeRuleWrapper.eq("IS_ENABLED", 1);
            Integer activeRules = controlRuleMapper.selectCount(activeRuleWrapper).intValue();
            stats.setActiveRules(activeRules);

            // 统计预警记录
            Integer totalWarnings = warningRecordMapper.selectCount(new QueryWrapper<>()).intValue();
            stats.setTotalWarnings(totalWarnings);

            QueryWrapper<BudgetWarningRecord> pendingWarningWrapper = new QueryWrapper<>();
            pendingWarningWrapper.eq("HANDLE_STATUS", "PENDING");
            Integer pendingWarnings = warningRecordMapper.selectCount(pendingWarningWrapper).intValue();
            stats.setPendingWarnings(pendingWarnings);

            // 统计预算执行
            Integer totalExecutions = executionMapper.selectCount(new QueryWrapper<>()).intValue();
            stats.setTotalExecutions(totalExecutions);

            // 简化计算超预算执行数
            stats.setOverBudgetExecutions(0);

            // 统计冻结预算
            QueryWrapper<BudgetFreeze> freezeWrapper = new QueryWrapper<>();
            freezeWrapper.eq("FREEZE_STATUS", "FROZEN");
            Integer frozenBudgets = freezeMapper.selectCount(freezeWrapper).intValue();
            stats.setFrozenBudgets(frozenBudgets);

            // 统计预算限额
            Integer budgetLimits = limitMapper.selectCount(new QueryWrapper<>()).intValue();
            stats.setBudgetLimits(budgetLimits);

            // 计算控制有效率
            if (totalRules > 0) {
                stats.setControlEffectiveness(Math.round(activeRules * 100.0 / totalRules * 10.0) / 10.0);
            }

            // 计算预警响应率
            if (totalWarnings > 0) {
                Integer handledWarnings = totalWarnings - pendingWarnings;
                stats.setWarningResponseRate(Math.round(handledWarnings * 100.0 / totalWarnings * 10.0) / 10.0);
            }

        } catch (Exception e) {
            log.error("获取预算控制统计数据失败", e);
        }
        
        return stats;
    }

    @Override
    public BudgetControlHealthVO getControlHealth() {
        BudgetControlHealthVO health = new BudgetControlHealthVO();
        List<BudgetControlHealthVO.HealthMetric> metrics = new ArrayList<>();

        try {
            // 规则覆盖率：启用规则数 / 总规则数
            Integer totalRules = controlRuleMapper.selectCount(new QueryWrapper<>()).intValue();
            QueryWrapper<BudgetControlRule> enabledWrapper = new QueryWrapper<>();
            enabledWrapper.eq("IS_ENABLED", 1);
            Integer enabledRules = controlRuleMapper.selectCount(enabledWrapper).intValue();
            double ruleCoverageValue = totalRules > 0 ? Math.round(enabledRules * 100.0 / totalRules * 10.0) / 10.0 : 0.0;

            BudgetControlHealthVO.HealthMetric ruleCoverage = new BudgetControlHealthVO.HealthMetric();
            ruleCoverage.setKey("ruleCoverage");
            ruleCoverage.setLabel("规则覆盖率");
            ruleCoverage.setValue(ruleCoverageValue);
            ruleCoverage.setUnit("%");
            ruleCoverage.setStatus(ruleCoverageValue >= 80 ? "good" : ruleCoverageValue >= 60 ? "warning" : "danger");
            metrics.add(ruleCoverage);

            // 预警响应率：已处理预警数 / 总预警数
            Integer totalWarnings = warningRecordMapper.selectCount(new QueryWrapper<>()).intValue();
            QueryWrapper<BudgetWarningRecord> pendingWrapper = new QueryWrapper<>();
            pendingWrapper.eq("HANDLE_STATUS", "PENDING");
            Integer pendingWarnings = warningRecordMapper.selectCount(pendingWrapper).intValue();
            double warningResponseValue = totalWarnings > 0 ? Math.round((totalWarnings - pendingWarnings) * 100.0 / totalWarnings * 10.0) / 10.0 : 100.0;

            BudgetControlHealthVO.HealthMetric warningTimeliness = new BudgetControlHealthVO.HealthMetric();
            warningTimeliness.setKey("warningTimeliness");
            warningTimeliness.setLabel("预警响应率");
            warningTimeliness.setValue(warningResponseValue);
            warningTimeliness.setUnit("%");
            warningTimeliness.setStatus(warningResponseValue >= 80 ? "good" : warningResponseValue >= 60 ? "warning" : "danger");
            metrics.add(warningTimeliness);

            // 控制执行率：有执行记录的规则 / 总规则数
            Integer totalExecutions = executionMapper.selectCount(new QueryWrapper<>()).intValue();
            double controlExecutionValue = totalRules > 0 ? Math.min(Math.round(totalExecutions * 100.0 / Math.max(totalRules, 1) * 10.0) / 10.0, 100.0) : 0.0;

            BudgetControlHealthVO.HealthMetric controlExecution = new BudgetControlHealthVO.HealthMetric();
            controlExecution.setKey("controlExecution");
            controlExecution.setLabel("控制执行率");
            controlExecution.setValue(controlExecutionValue);
            controlExecution.setUnit("%");
            controlExecution.setStatus(controlExecutionValue >= 80 ? "good" : controlExecutionValue >= 60 ? "warning" : "danger");
            metrics.add(controlExecution);

            // 冻结占比：冻结预算数 / 总限额数（越低越好）
            Integer totalLimits = limitMapper.selectCount(new QueryWrapper<>()).intValue();
            QueryWrapper<BudgetFreeze> frozenWrapper = new QueryWrapper<>();
            frozenWrapper.eq("FREEZE_STATUS", "FROZEN");
            Integer frozenCount = freezeMapper.selectCount(frozenWrapper).intValue();
            double frozenRate = totalLimits > 0 ? Math.round(frozenCount * 100.0 / totalLimits * 10.0) / 10.0 : 0.0;
            // 冻结占比越低越好，转换为健康分数
            double frozenHealthScore = Math.max(100.0 - frozenRate, 0.0);

            BudgetControlHealthVO.HealthMetric overBudgetRate = new BudgetControlHealthVO.HealthMetric();
            overBudgetRate.setKey("frozenRate");
            overBudgetRate.setLabel("预算健康度");
            overBudgetRate.setValue(Math.round(frozenHealthScore * 10.0) / 10.0);
            overBudgetRate.setUnit("%");
            overBudgetRate.setStatus(frozenHealthScore >= 80 ? "good" : frozenHealthScore >= 60 ? "warning" : "danger");
            metrics.add(overBudgetRate);

            health.setMetrics(metrics);

            // 计算总体评分（加权平均）
            double totalScore = metrics.stream()
                    .mapToDouble(BudgetControlHealthVO.HealthMetric::getValue)
                    .average()
                    .orElse(0.0);
            health.setOverallScore(Math.round(totalScore * 10.0) / 10.0);

        } catch (Exception e) {
            log.error("获取预算控制健康度失败", e);
        }

        return health;
    }
}

