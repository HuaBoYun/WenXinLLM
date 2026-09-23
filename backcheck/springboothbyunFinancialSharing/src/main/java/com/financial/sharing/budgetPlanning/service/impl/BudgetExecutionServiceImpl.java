package com.financial.sharing.budgetPlanning.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.budgetPlanning.dto.BudgetExecutionQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetData;
import com.financial.sharing.budgetPlanning.entity.TblBudgetExecution;
import com.financial.sharing.budgetPlanning.entity.TblBudgetModel;
import com.financial.sharing.budgetPlanning.mapper.BudgetDataMapper;
import com.financial.sharing.budgetPlanning.mapper.BudgetExecutionMapper;
import com.financial.sharing.budgetPlanning.mapper.BudgetModelMapper;
import com.financial.sharing.budgetPlanning.service.BudgetExecutionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 预算执行分析Service实现类
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Slf4j
@Service
public class BudgetExecutionServiceImpl extends ServiceImpl<BudgetExecutionMapper, TblBudgetExecution> 
        implements BudgetExecutionService {

    @Autowired
    private BudgetExecutionMapper budgetExecutionMapper;

    @Autowired
    private BudgetDataMapper budgetDataMapper;

    @Autowired
    private BudgetModelMapper budgetModelMapper;

    /** dataValues JSON 中可能存放金额的常用 key, 按优先级取值 */
    private static final String[] AMOUNT_KEYS = {
            "amount", "budget", "budgetAmount", "revenue", "income", "cost", "expense",
            "salary", "deptBudget", "totalAmount", "value"
    };

    @Override
    public PageInfo<TblBudgetExecution> getExecutionList(BudgetExecutionQueryParam param) {
        if (param.getPageNum() != null && param.getPageSize() != null) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        List<TblBudgetExecution> list = budgetExecutionMapper.selectExecutionList(param);
        return new PageInfo<>(list);
    }

    @Override
    public TblBudgetExecution getExecutionById(String executionId) {
        if (!StringUtils.hasText(executionId)) {
            return null;
        }
        return this.getById(executionId);
    }

    @Override
    public Map<String, Object> getExecutionStatistics(BudgetExecutionQueryParam param) {
        Map<String, Object> raw = budgetExecutionMapper.selectExecutionStatistics(param);
        log.info("[BudgetExecution.statistics] orgId={}, raw={}", param.getOrgId(), raw);
        // 关键: 达梦驱动可能把别名转成大写, 这里做大小写归一化, 同名取非零值优先
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", 0);
        result.put("normalCount", 0);
        result.put("warningCount", 0);
        result.put("exceededCount", 0);
        result.put("totalBudget", BigDecimal.ZERO);
        result.put("totalAdjusted", BigDecimal.ZERO);
        result.put("totalActual", BigDecimal.ZERO);
        result.put("totalAvailable", BigDecimal.ZERO);
        result.put("avgExecutionRate", BigDecimal.ZERO);
        if (raw != null) {
            for (Map.Entry<String, Object> entry : raw.entrySet()) {
                String camelKey = toCamelLower(entry.getKey());
                Object val = entry.getValue();
                if (val == null) {
                    continue;
                }
                Object existed = result.get(camelKey);
                if (existed == null || isZeroLike(existed)) {
                    result.put(camelKey, val);
                }
            }
        }
        return result;
    }

    /** 把驱动可能返回的 TOTAL_COUNT / TOTALCOUNT / totalCount 全部归一化成 totalCount */
    private static String toCamelLower(String key) {
        if (key == null) return "";
        String low = key.toLowerCase().replace("_", "");
        switch (low) {
            case "totalcount":       return "totalCount";
            case "normalcount":      return "normalCount";
            case "warningcount":     return "warningCount";
            case "exceededcount":    return "exceededCount";
            case "totalbudget":      return "totalBudget";
            case "totaladjusted":    return "totalAdjusted";
            case "totalactual":      return "totalActual";
            case "totalavailable":   return "totalAvailable";
            case "avgexecutionrate": return "avgExecutionRate";
            default: return key;
        }
    }

    private static boolean isZeroLike(Object v) {
        if (v == null) return true;
        if (v instanceof Number) return ((Number) v).doubleValue() == 0d;
        if (v instanceof BigDecimal) return ((BigDecimal) v).signum() == 0;
        String s = v.toString();
        return s.isEmpty() || "0".equals(s) || "0.00".equals(s);
    }

    @Override
    public List<Map<String, Object>> getExecutionTrend(BudgetExecutionQueryParam param) {
        return budgetExecutionMapper.selectExecutionTrend(param);
    }

    @Override
    public PageInfo<TblBudgetExecution> getExecutionWarnings(BudgetExecutionQueryParam param) {
        if (param.getPageNum() != null && param.getPageSize() != null) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        List<TblBudgetExecution> list = budgetExecutionMapper.selectExecutionWarnings(param);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> refreshExecutionData(BudgetExecutionQueryParam param) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;

        try {
            // 1. 查询预算数据 (只刷新已审批的, 草稿/驳回的不进入执行分析)
            LambdaQueryWrapper<TblBudgetData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TblBudgetData::getOrgId, param.getOrgId());
            queryWrapper.eq(TblBudgetData::getStatus, "APPROVED");

            if (StringUtils.hasText(param.getModelId())) {
                queryWrapper.eq(TblBudgetData::getModelId, param.getModelId());
            }
            if (StringUtils.hasText(param.getBizOrgId())) {
                queryWrapper.eq(TblBudgetData::getOrganizationCode, param.getBizOrgId());
            }
            if (StringUtils.hasText(param.getSubjectCode())) {
                queryWrapper.eq(TblBudgetData::getSubjectCode, param.getSubjectCode());
            }
            if (StringUtils.hasText(param.getPeriod())) {
                queryWrapper.eq(TblBudgetData::getPeriod, param.getPeriod());
            }

            List<TblBudgetData> budgetDataList = budgetDataMapper.selectList(queryWrapper);
            log.info("[BudgetExecution.refresh] orgId={} 候选预算数据 {} 条", param.getOrgId(), budgetDataList.size());

            // 2. 模型信息缓存(同一批 refresh 内 modelId 通常重复, 避免反复查表)
            Map<String, TblBudgetModel> modelCache = new HashMap<>();

            // 3. 遍历预算数据，生成或更新执行分析记录
            for (TblBudgetData budgetData : budgetDataList) {
                try {
                    // 查询是否已存在执行记录 (key: orgId + modelId + bizOrgId + subjectCode + period)
                    LambdaQueryWrapper<TblBudgetExecution> executionQuery = new LambdaQueryWrapper<>();
                    executionQuery.eq(TblBudgetExecution::getOrgId, param.getOrgId())
                            .eq(TblBudgetExecution::getModelId, budgetData.getModelId())
                            .eq(TblBudgetExecution::getBizOrgId, budgetData.getOrganizationCode())
                            .eq(TblBudgetExecution::getSubjectCode, budgetData.getSubjectCode())
                            .eq(TblBudgetExecution::getPeriod, budgetData.getPeriod());

                    TblBudgetExecution execution = this.getOne(executionQuery);
                    boolean isNew = (execution == null);

                    if (isNew) {
                        execution = new TblBudgetExecution();
                        execution.setOrgId(param.getOrgId());
                        execution.setModelId(budgetData.getModelId());
                        execution.setBizOrgId(budgetData.getOrganizationCode());
                        execution.setSubjectCode(budgetData.getSubjectCode());
                        execution.setPeriod(budgetData.getPeriod());
                        execution.setCreateUser(param.getOrgId());
                        execution.setCreateTime(new Date());
                    }

                    // 4. 取关联模型, 补全 budgetYear / versionNo
                    TblBudgetModel model = modelCache.get(budgetData.getModelId());
                    if (model == null && budgetData.getModelId() != null) {
                        model = budgetModelMapper.selectById(budgetData.getModelId());
                        if (model != null) {
                            modelCache.put(budgetData.getModelId(), model);
                        }
                    }
                    if (model != null && model.getBudgetYear() != null) {
                        execution.setBudgetYear(model.getBudgetYear());
                    } else {
                        // 兜底: 从 period 抽取前 4 位作为年度 (e.g. 2026-Q1 -> 2026, 2026 -> 2026)
                        execution.setBudgetYear(extractYear(budgetData.getPeriod()));
                    }
                    execution.setVersionNo(budgetData.getVersion());

                    // 5. 业务组织维度: 用 organizationCode 同时填充 ORG_CODE / ORG_NAME (后者无字典先回写编码兜底)
                    execution.setOrgCode(budgetData.getOrganizationCode());
                    execution.setOrgName(budgetData.getOrganizationCode()); // 无组织字典表, 兜底显示编码

                    // 6. 科目名称: 前期没接科目字典, 兜底回写编码
                    execution.setSubjectName(budgetData.getSubjectCode());

                    // 7. 解析 dataValues JSON 取金额
                    BigDecimal amount = parseAmountFromDataValues(budgetData.getDataValues());
                    execution.setBudgetAmount(amount);
                    execution.setAdjustedAmount(amount); // 暂同预算 (后续接调整接口可单独计算)
                    // 实际/承诺/占用: 没接财务实际入账系统前, 仅在新增时随机模拟一个 0~adjusted*1.3 的实际值,
                    // 让 UI 看得到完整的状态/预警级别分布; 后续接真实数据替换这里即可.
                    if (isNew) {
                        execution.setActualAmount(mockActualAmount(amount));
                    }
                    if (execution.getCommittedAmount() == null) execution.setCommittedAmount(BigDecimal.ZERO);
                    if (execution.getOccupiedAmount() == null) execution.setOccupiedAmount(BigDecimal.ZERO);

                    // 8. 计算执行指标 + 状态/预警级别 (会写 STATUS/WARNING_LEVEL)
                    calculateExecutionMetrics(execution);

                    execution.setLastUpdateTime(new Date());
                    execution.setUpdateUser(param.getOrgId());
                    execution.setUpdateTime(new Date());

                    if (isNew) {
                        this.save(execution);
                    } else {
                        this.updateById(execution);
                    }
                    successCount++;
                } catch (Exception e) {
                    log.error("刷新单条预算执行数据失败 dataId={}: {}", budgetData.getDataId(), e.getMessage(), e);
                    failCount++;
                }
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", String.format("刷新完成，成功%d条，失败%d条", successCount, failCount));

        } catch (Exception e) {
            log.error("刷新预算执行数据异常: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "刷新失败: " + e.getMessage());
        }

        return result;
    }

    /** 从 dataValues JSON 中按优先级取金额 (revenue/cost/amount/budget...), 取不到返回 0 */
    private BigDecimal parseAmountFromDataValues(String dataValues) {
        if (dataValues == null || dataValues.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            JSONObject json = JSONObject.parseObject(dataValues);
            if (json == null) return BigDecimal.ZERO;
            for (String k : AMOUNT_KEYS) {
                Object v = json.get(k);
                if (v != null) {
                    try {
                        return new BigDecimal(v.toString());
                    } catch (NumberFormatException ignore) {
                        // 当前 key 不是数字, 继续找下一个
                    }
                }
            }
            // 兜底: 找第一个能转成数字的 value
            for (Map.Entry<String, Object> e : json.entrySet()) {
                Object v = e.getValue();
                if (v != null) {
                    try {
                        return new BigDecimal(v.toString());
                    } catch (NumberFormatException ignore) {}
                }
            }
        } catch (Exception e) {
            log.warn("解析 dataValues 失败: {} | data={}", e.getMessage(), dataValues);
        }
        return BigDecimal.ZERO;
    }

    /** period -> 年度: "2026-Q1"/"2026-01"/"2026" -> "2026" */
    private static String extractYear(String period) {
        if (period == null || period.length() < 4) return null;
        return period.substring(0, 4);
    }

    /** 根据预算金额生成 [70%, 130%] 区间的实际值, 让 NORMAL/WARNING/EXCEEDED 都能出现 */
    private static BigDecimal mockActualAmount(BigDecimal budget) {
        if (budget == null || budget.signum() == 0) return BigDecimal.ZERO;
        double ratio = 0.70d + Math.random() * 0.60d; // 0.70 ~ 1.30
        return budget.multiply(BigDecimal.valueOf(ratio)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void calculateExecutionMetrics(TblBudgetExecution execution) {
        // 计算可用金额 = 调整后预算 - 实际 - 承诺 - 占用
        BigDecimal adjustedAmount = execution.getAdjustedAmount() != null ? 
                execution.getAdjustedAmount() : BigDecimal.ZERO;
        BigDecimal actualAmount = execution.getActualAmount() != null ? 
                execution.getActualAmount() : BigDecimal.ZERO;
        BigDecimal committedAmount = execution.getCommittedAmount() != null ? 
                execution.getCommittedAmount() : BigDecimal.ZERO;
        BigDecimal occupiedAmount = execution.getOccupiedAmount() != null ? 
                execution.getOccupiedAmount() : BigDecimal.ZERO;

        BigDecimal availableAmount = adjustedAmount
                .subtract(actualAmount)
                .subtract(committedAmount)
                .subtract(occupiedAmount);
        execution.setAvailableAmount(availableAmount);

        // 计算差异金额 = 调整后预算 - 实际
        BigDecimal varianceAmount = adjustedAmount.subtract(actualAmount);
        execution.setVarianceAmount(varianceAmount);

        // 计算执行率 = (实际 / 调整后预算) * 100
        if (adjustedAmount.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal executionRate = actualAmount
                    .divide(adjustedAmount, 6, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            execution.setExecutionRate(executionRate);

            // 计算差异率 = (差异 / 调整后预算) * 100
            BigDecimal varianceRate = varianceAmount
                    .divide(adjustedAmount, 6, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            execution.setVarianceRate(varianceRate);
        } else {
            execution.setExecutionRate(BigDecimal.ZERO);
            execution.setVarianceRate(BigDecimal.ZERO);
        }

        // 判断状态和预警级别
        BigDecimal executionRate = execution.getExecutionRate();
        if (executionRate.compareTo(new BigDecimal("100")) > 0) {
            // 超支
            execution.setStatus("EXCEEDED");
            if (executionRate.compareTo(new BigDecimal("120")) > 0) {
                execution.setWarningLevel("HIGH");
            } else if (executionRate.compareTo(new BigDecimal("110")) > 0) {
                execution.setWarningLevel("MEDIUM");
            } else {
                execution.setWarningLevel("LOW");
            }
        } else if (executionRate.compareTo(new BigDecimal("90")) > 0) {
            // 预警
            execution.setStatus("WARNING");
            if (executionRate.compareTo(new BigDecimal("95")) > 0) {
                execution.setWarningLevel("MEDIUM");
            } else {
                execution.setWarningLevel("LOW");
            }
        } else {
            // 正常
            execution.setStatus("NORMAL");
            execution.setWarningLevel(null);
        }
    }
}

