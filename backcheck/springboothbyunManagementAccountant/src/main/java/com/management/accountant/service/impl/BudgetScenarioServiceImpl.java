package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetScenario;
import com.management.accountant.oracle.mapper.budget.BudgetScenarioMapper;
import com.management.accountant.service.BudgetScenarioService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class BudgetScenarioServiceImpl implements BudgetScenarioService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetScenarioMapper scenarioMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetScenario create(BudgetScenario s) {
        if (s == null) throw new ServiceException("场景信息不能为空");
        if (s.getDelFlag() == null) s.setDelFlag(0);
        if (s.getIsEnabled() == null) s.setIsEnabled(true);
        s.setCreateTime(new Date()); s.setUpdateTime(new Date());
        scenarioMapper.insert(s);
        return s;
    }

    @Override
    public BudgetScenario getById(String id) {
        QueryWrapper<BudgetScenario> w = new QueryWrapper<>();
        w.eq("SCENARIO_ID", id).eq("DEL_FLAG", 0L);
        return scenarioMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetScenario s) {
        if (s == null || !StringUtils.hasText(s.getScenarioId())) throw new ServiceException("场景ID不能为空");
        s.setUpdateTime(new Date());
        scenarioMapper.updateById(s);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        BudgetScenario u = new BudgetScenario();
        u.setScenarioId(id); u.setDelFlag(1); u.setUpdateTime(new Date());
        scenarioMapper.updateById(u);
    }

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        String scenarioStatusFilter = params.get("scenarioStatus") != null && StringUtils.hasText(params.get("scenarioStatus").toString())
            ? params.get("scenarioStatus").toString()
            : null;

        QueryWrapper<BudgetScenario> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L);
        if (params.get("scenarioName") != null && StringUtils.hasText(params.get("scenarioName").toString())) w.like("SCENARIO_NAME", params.get("scenarioName"));
        if (params.get("scenarioType") != null && StringUtils.hasText(params.get("scenarioType").toString())) w.eq("SCENARIO_TYPE", params.get("scenarioType"));
        if (params.get("creator") != null && StringUtils.hasText(params.get("creator").toString())) w.like("CREATE_BY", params.get("creator"));
        w.orderByDesc("CREATE_TIME");

        List<BudgetScenario> allMatchedRecords = scenarioMapper.selectList(w);

        // 如果有场景状态筛选，先过滤
        if (scenarioStatusFilter != null) {
            allMatchedRecords = allMatchedRecords.stream()
                .filter(item -> scenarioStatusFilter.equals(resolveScenarioStatus(item)))
                .collect(java.util.stream.Collectors.toList());
        }

        // 手动分页
        int total = allMatchedRecords.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<BudgetScenario> pageRecords = fromIndex < total
            ? allMatchedRecords.subList(fromIndex, toIndex)
            : new ArrayList<>();

        // 转换分页记录
        List<Map<String, Object>> convertedPageRecords = new ArrayList<>();
        for (BudgetScenario item : pageRecords) {
            convertedPageRecords.add(convertToPageRecord(item));
        }

        // 统计信息基于全部匹配记录（未分页前）
        long totalScenarios = allMatchedRecords.size();
        long activeScenarios = allMatchedRecords.stream().filter(item -> Boolean.TRUE.equals(item.getIsEnabled())).count();
        long approvedScenarios = allMatchedRecords.stream().filter(item -> "APPROVED".equals(resolveScenarioStatus(item))).count();
        long baselineScenarios = allMatchedRecords.stream().filter(item -> Boolean.TRUE.equals(item.getIsDefault())).count();

        Map<String, Object> result = new HashMap<>();
        result.put("records", convertedPageRecords);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalScenarios", totalScenarios);
        stats.put("activeScenarios", activeScenarios);
        stats.put("approvedScenarios", approvedScenarios);
        stats.put("baselineScenarios", baselineScenarios);
        stats.put("activeRate", totalScenarios > 0 ? Math.round(activeScenarios * 1000D / totalScenarios) / 10D : 0D);
        stats.put("approvedRate", totalScenarios > 0 ? Math.round(approvedScenarios * 1000D / totalScenarios) / 10D : 0D);
        stats.put("baselineRate", totalScenarios > 0 ? Math.round(baselineScenarios * 1000D / totalScenarios) / 10D : 0D);
        result.put("stats", stats);
        return result;
    }

    @Override
    public List<BudgetScenario> getBaselineScenarios() {
        QueryWrapper<BudgetScenario> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L).eq("IS_DEFAULT", 1);
        return scenarioMapper.selectList(w);
    }

    @Override
    public List<Map<String, Object>> getAccountOptions() { return new ArrayList<>(); }

    @Override
    public Map<String, Object> calculateScenario(Map<String, Object> params) {
        String scenarioId = params.get("scenarioId") == null ? null : String.valueOf(params.get("scenarioId"));
        if (!StringUtils.hasText(scenarioId)) {
            throw new ServiceException("场景ID不能为空");
        }
        BudgetScenario scenario = getById(scenarioId);
        if (scenario == null) {
            throw new ServiceException("场景不存在");
        }
        double baseBudgetAmount = calculateBudgetAmount(scenario);
        double totalAdjustment = 0D;
        Object assumptionsObj = params.get("assumptions");
        if (assumptionsObj instanceof List) {
            List<?> assumptions = (List<?>) assumptionsObj;
            for (Object assumptionObj : assumptions) {
                if (!(assumptionObj instanceof Map)) {
                    continue;
                }
                Map<?, ?> assumption = (Map<?, ?>) assumptionObj;
                Object typeObj = assumption.get("assumptionType");
                Object valueObj = assumption.get("assumptionValue");
                double value;
                try {
                    value = Double.parseDouble(String.valueOf(valueObj));
                } catch (Exception ex) {
                    continue;
                }
                String type = typeObj == null ? "" : String.valueOf(typeObj);
                if ("PERCENTAGE".equalsIgnoreCase(type) || "RATIO".equalsIgnoreCase(type)) {
                    totalAdjustment += baseBudgetAmount * value / 100D;
                } else {
                    totalAdjustment += value;
                }
            }
        }
        double varianceAmount = Math.round(totalAdjustment * 100D) / 100D;
        double scenarioBudgetAmount = Math.round((baseBudgetAmount + varianceAmount) * 100D) / 100D;
        double varianceRate = baseBudgetAmount == 0D ? 0D : Math.round(varianceAmount * 1000D / baseBudgetAmount) / 10D;

        BudgetScenario update = new BudgetScenario();
        update.setScenarioId(scenarioId);
        update.setParameters(buildParameters(scenarioBudgetAmount, varianceAmount, varianceRate));
        update.setUpdateTime(new Date());
        scenarioMapper.updateById(update);

        Map<String, Object> r = new HashMap<>();
        r.put("success", true);
        r.put("message", "计算完成");
        r.put("scenarioId", scenarioId);
        r.put("budgetAmount", scenarioBudgetAmount);
        r.put("varianceAmount", varianceAmount);
        r.put("varianceRate", varianceRate);
        return r;
    }

    @Override
    public Map<String, Object> compareScenarios(Map<String, Object> params) {
        Object scenarioIdsObj = params.get("scenarioIds");
        if (!(scenarioIdsObj instanceof List) || ((List<?>) scenarioIdsObj).size() < 2) {
            throw new ServiceException("请至少选择2个场景进行对比");
        }
        List<?> scenarioIds = (List<?>) scenarioIdsObj;
        List<Map<String, Object>> scenarios = new ArrayList<>();
        List<Double> budgets = new ArrayList<>();
        for (Object scenarioId : scenarioIds) {
            BudgetScenario scenario = getById(String.valueOf(scenarioId));
            if (scenario == null) {
                throw new ServiceException("场景不存在：" + scenarioId);
            }
            Map<String, Object> scenarioInfo = new HashMap<>();
            scenarioInfo.put("id", scenario.getScenarioId());
            scenarioInfo.put("scenarioId", scenario.getScenarioId());
            scenarioInfo.put("scenarioName", scenario.getScenarioName());
            scenarioInfo.put("scenarioCode", scenario.getScenarioCode());
            scenarios.add(scenarioInfo);
            budgets.add(calculateBudgetAmount(scenario));
        }
        double maxBudget = budgets.stream().mapToDouble(Double::doubleValue).max().orElse(0D);
        double minBudget = budgets.stream().mapToDouble(Double::doubleValue).min().orElse(0D);
        Map<String, Object> r = new HashMap<>();
        Map<String, Object> compareRow = new HashMap<>();
        compareRow.put("accountName", "预算总额");
        for (int i = 0; i < scenarios.size(); i++) {
            compareRow.put(String.valueOf(scenarios.get(i).get("id")), budgets.get(i));
        }
        compareRow.put("maxVariance", maxBudget - minBudget);
        List<Map<String, Object>> compareData = new ArrayList<>();
        compareData.add(compareRow);
        r.put("success", true);
        r.put("compareData", compareData);
        r.put("scenarios", scenarios);
        return r;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setBaseline(String id) {
        BudgetScenario current = getById(id);
        if (current == null) {
            throw new ServiceException("场景不存在");
        }

        // 清空所有场景的默认标记
        UpdateWrapper<BudgetScenario> clearWrapper = new UpdateWrapper<>();
        clearWrapper.eq("DEL_FLAG", 0L)
                    .set("IS_DEFAULT", false);
        scenarioMapper.update(null, clearWrapper);

        // 设置当前场景为默认
        UpdateWrapper<BudgetScenario> setWrapper = new UpdateWrapper<>();
        setWrapper.eq("SCENARIO_ID", id)
                  .eq("DEL_FLAG", 0L)
                  .set("IS_DEFAULT", true);
        scenarioMapper.update(null, setWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitApproval(String id) {
        BudgetScenario current = getById(id);
        if (current == null) {
            throw new ServiceException("场景不存在");
        }
        BudgetScenario update = new BudgetScenario();
        update.setScenarioId(id);
        update.setRemark("APPROVED");
        update.setIsEnabled(true);
        update.setUpdateTime(new Date());
        scenarioMapper.updateById(update);
    }

    private Map<String, Object> convertToPageRecord(BudgetScenario item) {
        Map<String, Object> record = new HashMap<>();
        double budgetAmount = calculateBudgetAmount(item);
        double varianceAmount = calculateVarianceAmount(item, budgetAmount);
        double varianceRate = budgetAmount == 0D ? 0D : Math.round(Math.abs(varianceAmount) * 1000D / budgetAmount) / 10D;
        record.put("id", item.getScenarioId());
        record.put("scenarioId", item.getScenarioId());
        record.put("scenarioCode", item.getScenarioCode());
        record.put("scenarioName", item.getScenarioName());
        record.put("scenarioType", normalizeScenarioType(item.getScenarioType(), item.getIsDefault()));
        record.put("isBaseline", Boolean.TRUE.equals(item.getIsDefault()) || isBaselineType(item.getScenarioType()));
        record.put("isDefault", item.getIsDefault());
        record.put("budgetAmount", budgetAmount);
        record.put("varianceAmount", varianceAmount);
        record.put("varianceRate", varianceRate);
        record.put("scenarioStatus", resolveScenarioStatus(item));
        record.put("creator", item.getCreateBy());
        record.put("createBy", item.getCreateBy());
        record.put("createTime", item.getCreateTime());
        record.put("scenarioDescription", item.getScenarioDescription());
        record.put("assumptions", item.getAssumptions());
        record.put("parameters", item.getParameters());
        record.put("remark", item.getRemark());
        return record;
    }

    private String resolveScenarioStatus(BudgetScenario item) {
        if ("APPROVED".equalsIgnoreCase(item.getRemark())) {
            return "APPROVED";
        }
        if (Boolean.TRUE.equals(item.getIsEnabled())) {
            return "ACTIVE";
        }
        return "DRAFT";
    }

    private double calculateBudgetAmount(BudgetScenario item) {
        String parameters = item.getParameters();
        if (!StringUtils.hasText(parameters)) {
            return 0D;
        }
        String[] entries = parameters.split("[,;]");
        for (String entry : entries) {
            String[] pair = entry.split("=");
            if (pair.length != 2) {
                continue;
            }
            if ("budgetamount".equalsIgnoreCase(pair[0].trim())) {
                try {
                    return Double.parseDouble(pair[1].trim());
                } catch (NumberFormatException ignored) {
                    log.debug("无法解析预算金额参数: {}", entry);
                }
            }
        }
        double total = 0D;
        for (String entry : entries) {
            String[] pair = entry.split("=");
            if (pair.length != 2) {
                continue;
            }
            String key = pair[0].trim().toLowerCase(Locale.ROOT);
            if ((key.contains("variance") && !key.contains("budget")) || (!key.contains("amount") && !key.contains("budget"))) {
                continue;
            }
            try {
                total += Double.parseDouble(pair[1].trim());
            } catch (NumberFormatException ignored) {
                log.debug("无法解析预算金额参数: {}", entry);
            }
        }
        return total;
    }

    private double calculateVarianceAmount(BudgetScenario item, double budgetAmount) {
        String parameters = item.getParameters();
        if (StringUtils.hasText(parameters)) {
            String[] entries = parameters.split("[,;]");
            for (String entry : entries) {
                String[] pair = entry.split("=");
                if (pair.length != 2) {
                    continue;
                }
                if ("varianceamount".equalsIgnoreCase(pair[0].trim())) {
                    try {
                        return Double.parseDouble(pair[1].trim());
                    } catch (NumberFormatException ignored) {
                        log.debug("无法解析差异金额参数: {}", entry);
                    }
                }
            }
        }
        if (Boolean.TRUE.equals(item.getIsDefault())) {
            return 0D;
        }
        return Math.round(budgetAmount * 0.08D * 100D) / 100D;
    }

    private String normalizeScenarioType(String scenarioType, Boolean isDefault) {
        if (Boolean.TRUE.equals(isDefault)) {
            return "BASELINE";
        }
        if (!StringUtils.hasText(scenarioType)) {
            return "WHAT_IF";
        }
        String normalized = scenarioType.trim().toUpperCase(Locale.ROOT);
        switch (normalized) {
            case "BASE":
            case "BASELINE":
                return "BASELINE";
            case "OPTIMISTIC":
                return "OPTIMISTIC";
            case "PESSIMISTIC":
                return "PESSIMISTIC";
            case "REALISTIC":
                return "REALISTIC";
            case "CUSTOM":
            case "WHAT_IF":
                return "WHAT_IF";
            default:
                return normalized;
        }
    }

    private boolean isBaselineType(String scenarioType) {
        return StringUtils.hasText(scenarioType)
                && ("BASE".equalsIgnoreCase(scenarioType) || "BASELINE".equalsIgnoreCase(scenarioType));
    }

    private String buildParameters(double budgetAmount, double varianceAmount, double varianceRate) {
        return "budgetAmount=" + budgetAmount + ";varianceAmount=" + varianceAmount + ";varianceRate=" + varianceRate;
    }
}

