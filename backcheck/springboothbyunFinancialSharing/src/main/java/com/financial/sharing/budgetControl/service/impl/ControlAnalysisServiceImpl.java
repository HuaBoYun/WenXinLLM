package com.financial.sharing.budgetControl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.budgetControl.dto.ControlAnalysisQueryParam;
import com.financial.sharing.budgetControl.entity.TblExecutionRecord;
import com.financial.sharing.budgetControl.mapper.ExecutionRecordMapper;
import com.financial.sharing.budgetControl.service.ControlAnalysisService;
import com.financial.sharing.util.MyJsonBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 控制分析Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class ControlAnalysisServiceImpl implements ControlAnalysisService {

    @Autowired
    private ExecutionRecordMapper executionRecordMapper;

    @Override
    public MyJsonBean getControlEffectAnalysis(ControlAnalysisQueryParam param) {
        try {
            LambdaQueryWrapper<TblExecutionRecord> wrapper = buildQueryWrapper(param);
            List<TblExecutionRecord> records = executionRecordMapper.selectList(wrapper);

            // 统计各种控制结果的数量
            long totalCount = records.size();
            long passCount = records.stream().filter(r -> "PASS".equals(r.getControlResult())).count();
            long blockCount = records.stream().filter(r -> "BLOCK".equals(r.getControlResult())).count();
            // 注意: entity 注释和业务全链路都用 "WARN", 不是 "WARNING", 这里曾经是个 bug
            long warningCount = records.stream().filter(r -> "WARN".equals(r.getControlResult())).count();

            // 计算比率
            BigDecimal passRate = calculateRate(passCount, totalCount);
            BigDecimal blockRate = calculateRate(blockCount, totalCount);
            BigDecimal warningRate = calculateRate(warningCount, totalCount);

            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", totalCount);
            result.put("passCount", passCount);
            result.put("blockCount", blockCount);
            result.put("warningCount", warningCount);
            result.put("passRate", passRate);
            result.put("blockRate", blockRate);
            result.put("warningRate", warningRate);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取控制效果分析失败", e);
            return MyJsonBean.errorData("获取控制效果分析失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getBudgetUsageTrend(ControlAnalysisQueryParam param) {
        try {
            LambdaQueryWrapper<TblExecutionRecord> wrapper = buildQueryWrapper(param);
            wrapper.orderByAsc(TblExecutionRecord::getPeriod);
            List<TblExecutionRecord> records = executionRecordMapper.selectList(wrapper);

            // 按期间分组统计
            Map<String, Map<String, Object>> periodStats = new LinkedHashMap<>();
            
            for (TblExecutionRecord record : records) {
                String period = record.getPeriod();
                if (!periodStats.containsKey(period)) {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("period", period);
                    stats.put("occupyAmount", BigDecimal.ZERO);
                    stats.put("releaseAmount", BigDecimal.ZERO);
                    stats.put("transferAmount", BigDecimal.ZERO);
                    stats.put("controlCount", 0);
                    periodStats.put(period, stats);
                }

                Map<String, Object> stats = periodStats.get(period);
                BigDecimal amount = record.getApplyAmount() != null ? record.getApplyAmount() : BigDecimal.ZERO;
                
                // 根据控制结果分类统计
                String controlResult = record.getControlResult();
                if ("OCCUPY".equals(controlResult)) {
                    stats.put("occupyAmount", ((BigDecimal) stats.get("occupyAmount")).add(amount));
                } else if ("RELEASE".equals(controlResult)) {
                    stats.put("releaseAmount", ((BigDecimal) stats.get("releaseAmount")).add(amount));
                } else if ("TRANSFER".equals(controlResult)) {
                    stats.put("transferAmount", ((BigDecimal) stats.get("transferAmount")).add(amount));
                }
                
                stats.put("controlCount", (Integer) stats.get("controlCount") + 1);
            }

            List<Map<String, Object>> trendData = new ArrayList<>(periodStats.values());
            return MyJsonBean.successData(trendData);
        } catch (Exception e) {
            log.error("获取预算使用趋势分析失败", e);
            return MyJsonBean.errorData("获取预算使用趋势分析失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getAbnormalControlAnalysis(ControlAnalysisQueryParam param) {
        try {
            LambdaQueryWrapper<TblExecutionRecord> wrapper = buildQueryWrapper(param);
            wrapper.eq(TblExecutionRecord::getControlResult, "BLOCK");
            wrapper.orderByDesc(TblExecutionRecord::getExecuteTime);
            
            List<TblExecutionRecord> records = executionRecordMapper.selectList(wrapper);

            // 统计异常模式
            Map<String, Integer> orgBlockCount = new HashMap<>();
            Map<String, Integer> subjectBlockCount = new HashMap<>();
            Map<String, Integer> sourceSystemBlockCount = new HashMap<>();
            BigDecimal totalBlockAmount = BigDecimal.ZERO;

            for (TblExecutionRecord record : records) {
                // 按业务组织统计 (注意: ORG_ID 是租户隔离, 已在 wrapper 里过滤为同一值; 真正用来分组的是 BIZ_ORG_ID)
                String bizOrgId = record.getBizOrgId();
                if (StringUtils.isNotBlank(bizOrgId)) {
                    orgBlockCount.put(bizOrgId, orgBlockCount.getOrDefault(bizOrgId, 0) + 1);
                }

                // 按科目统计
                String subjectCode = record.getSubjectCode();
                if (StringUtils.isNotBlank(subjectCode)) {
                    subjectBlockCount.put(subjectCode, subjectBlockCount.getOrDefault(subjectCode, 0) + 1);
                }

                // 按来源系统统计
                String sourceSystem = record.getSourceSystem();
                if (StringUtils.isNotBlank(sourceSystem)) {
                    sourceSystemBlockCount.put(sourceSystem, sourceSystemBlockCount.getOrDefault(sourceSystem, 0) + 1);
                }

                // 累计阻止金额
                if (record.getApplyAmount() != null) {
                    totalBlockAmount = totalBlockAmount.add(record.getApplyAmount());
                }
            }

            // 转换为排名列表（取前10）
            List<Map<String, Object>> topOrgBlocks = convertToRankingList(orgBlockCount, "orgId", 10);
            List<Map<String, Object>> topSubjectBlocks = convertToRankingList(subjectBlockCount, "subjectCode", 10);
            List<Map<String, Object>> topSystemBlocks = convertToRankingList(sourceSystemBlockCount, "sourceSystem", 10);

            Map<String, Object> result = new HashMap<>();
            result.put("totalBlockCount", records.size());
            result.put("totalBlockAmount", totalBlockAmount);
            result.put("topOrgBlocks", topOrgBlocks);
            result.put("topSubjectBlocks", topSubjectBlocks);
            result.put("topSystemBlocks", topSystemBlocks);
            result.put("recentBlocks", records.stream().limit(20).collect(Collectors.toList()));

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取异常控制分析失败", e);
            return MyJsonBean.errorData("获取异常控制分析失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getControlResultDistribution(ControlAnalysisQueryParam param) {
        try {
            LambdaQueryWrapper<TblExecutionRecord> wrapper = buildQueryWrapper(param);
            List<TblExecutionRecord> records = executionRecordMapper.selectList(wrapper);

            // 统计各种控制结果
            Map<String, Integer> resultCount = new HashMap<>();
            for (TblExecutionRecord record : records) {
                String result = record.getControlResult();
                if (StringUtils.isNotBlank(result)) {
                    resultCount.put(result, resultCount.getOrDefault(result, 0) + 1);
                }
            }

            // 转换为列表格式
            List<Map<String, Object>> distribution = new ArrayList<>();
            int total = records.size();
            for (Map.Entry<String, Integer> entry : resultCount.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("result", entry.getKey());
                item.put("count", entry.getValue());
                item.put("percentage", calculateRate(entry.getValue(), total));
                distribution.add(item);
            }

            // 按数量降序排列
            distribution.sort((a, b) -> ((Integer) b.get("count")).compareTo((Integer) a.get("count")));

            return MyJsonBean.successData(distribution);
        } catch (Exception e) {
            log.error("获取控制结果分布失败", e);
            return MyJsonBean.errorData("获取控制结果分布失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getOrgControlRanking(ControlAnalysisQueryParam param) {
        try {
            LambdaQueryWrapper<TblExecutionRecord> wrapper = buildQueryWrapper(param);
            List<TblExecutionRecord> records = executionRecordMapper.selectList(wrapper);

            // 按业务组织统计 (key 仍用 "orgId" 保持前端零改动, 实际承载 BIZ_ORG_ID)
            Map<String, Map<String, Object>> orgStats = new HashMap<>();
            for (TblExecutionRecord record : records) {
                String orgId = record.getBizOrgId();
                if (StringUtils.isBlank(orgId)) continue;

                if (!orgStats.containsKey(orgId)) {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("orgId", orgId);
                    stats.put("totalCount", 0);
                    stats.put("passCount", 0);
                    stats.put("blockCount", 0);
                    stats.put("warningCount", 0);
                    orgStats.put(orgId, stats);
                }

                Map<String, Object> stats = orgStats.get(orgId);
                stats.put("totalCount", (Integer) stats.get("totalCount") + 1);

                String result = record.getControlResult();
                if ("PASS".equals(result)) {
                    stats.put("passCount", (Integer) stats.get("passCount") + 1);
                } else if ("BLOCK".equals(result)) {
                    stats.put("blockCount", (Integer) stats.get("blockCount") + 1);
                } else if ("WARN".equals(result)) {
                    stats.put("warningCount", (Integer) stats.get("warningCount") + 1);
                }
            }

            // 计算阻止率并排序
            List<Map<String, Object>> ranking = new ArrayList<>(orgStats.values());
            for (Map<String, Object> stats : ranking) {
                int totalCount = (Integer) stats.get("totalCount");
                int blockCount = (Integer) stats.get("blockCount");
                stats.put("blockRate", calculateRate(blockCount, totalCount));
            }
            ranking.sort((a, b) -> ((Integer) b.get("totalCount")).compareTo((Integer) a.get("totalCount")));

            return MyJsonBean.successData(ranking);
        } catch (Exception e) {
            log.error("获取组织控制排名失败", e);
            return MyJsonBean.errorData("获取组织控制排名失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getSubjectControlRanking(ControlAnalysisQueryParam param) {
        try {
            LambdaQueryWrapper<TblExecutionRecord> wrapper = buildQueryWrapper(param);
            List<TblExecutionRecord> records = executionRecordMapper.selectList(wrapper);

            // 按科目统计
            Map<String, Map<String, Object>> subjectStats = new HashMap<>();
            for (TblExecutionRecord record : records) {
                String subjectCode = record.getSubjectCode();
                if (StringUtils.isBlank(subjectCode)) continue;

                if (!subjectStats.containsKey(subjectCode)) {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("subjectCode", subjectCode);
                    stats.put("totalCount", 0);
                    stats.put("passCount", 0);
                    stats.put("blockCount", 0);
                    stats.put("warningCount", 0);
                    subjectStats.put(subjectCode, stats);
                }

                Map<String, Object> stats = subjectStats.get(subjectCode);
                stats.put("totalCount", (Integer) stats.get("totalCount") + 1);

                String result = record.getControlResult();
                if ("PASS".equals(result)) {
                    stats.put("passCount", (Integer) stats.get("passCount") + 1);
                } else if ("BLOCK".equals(result)) {
                    stats.put("blockCount", (Integer) stats.get("blockCount") + 1);
                } else if ("WARN".equals(result)) {
                    stats.put("warningCount", (Integer) stats.get("warningCount") + 1);
                }
            }

            // 计算阻止率并排序
            List<Map<String, Object>> ranking = new ArrayList<>(subjectStats.values());
            for (Map<String, Object> stats : ranking) {
                int totalCount = (Integer) stats.get("totalCount");
                int blockCount = (Integer) stats.get("blockCount");
                stats.put("blockRate", calculateRate(blockCount, totalCount));
            }
            ranking.sort((a, b) -> ((Integer) b.get("totalCount")).compareTo((Integer) a.get("totalCount")));

            return MyJsonBean.successData(ranking);
        } catch (Exception e) {
            log.error("获取科目控制排名失败", e);
            return MyJsonBean.errorData("获取科目控制排名失败：" + e.getMessage());
        }
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<TblExecutionRecord> buildQueryWrapper(ControlAnalysisQueryParam param) {
        LambdaQueryWrapper<TblExecutionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblExecutionRecord::getOrgId, param.getOrgId());

        if (StringUtils.isNotBlank(param.getBizOrgId())) {
            wrapper.eq(TblExecutionRecord::getBizOrgId, param.getBizOrgId());
        }
        if (StringUtils.isNotBlank(param.getSubjectCode())) {
            wrapper.eq(TblExecutionRecord::getSubjectCode, param.getSubjectCode());
        }
        if (StringUtils.isNotBlank(param.getPeriod())) {
            wrapper.eq(TblExecutionRecord::getPeriod, param.getPeriod());
        }
        if (StringUtils.isNotBlank(param.getStartPeriod())) {
            wrapper.ge(TblExecutionRecord::getPeriod, param.getStartPeriod());
        }
        if (StringUtils.isNotBlank(param.getEndPeriod())) {
            wrapper.le(TblExecutionRecord::getPeriod, param.getEndPeriod());
        }
        if (StringUtils.isNotBlank(param.getSourceSystem())) {
            wrapper.eq(TblExecutionRecord::getSourceSystem, param.getSourceSystem());
        }
        if (StringUtils.isNotBlank(param.getControlResult())) {
            wrapper.eq(TblExecutionRecord::getControlResult, param.getControlResult());
        }

        return wrapper;
    }

    /**
     * 计算比率
     */
    private BigDecimal calculateRate(long count, long total) {
        if (total == 0) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(count)
                .multiply(new BigDecimal(100))
                .divide(new BigDecimal(total), 2, RoundingMode.HALF_UP);
    }

    /**
     * 转换为排名列表
     */
    private List<Map<String, Object>> convertToRankingList(Map<String, Integer> countMap, String keyName, int topN) {
        List<Map<String, Object>> ranking = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put(keyName, entry.getKey());
            item.put("count", entry.getValue());
            ranking.add(item);
        }
        ranking.sort((a, b) -> ((Integer) b.get("count")).compareTo((Integer) a.get("count")));
        return ranking.stream().limit(topN).collect(Collectors.toList());
    }
}

