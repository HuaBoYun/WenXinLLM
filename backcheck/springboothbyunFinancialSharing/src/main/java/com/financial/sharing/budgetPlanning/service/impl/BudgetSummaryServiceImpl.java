package com.financial.sharing.budgetPlanning.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.budgetPlanning.dto.BudgetSummaryQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetData;
import com.financial.sharing.budgetPlanning.entity.TblBudgetSummary;
import com.financial.sharing.budgetPlanning.mapper.BudgetDataMapper;
import com.financial.sharing.budgetPlanning.mapper.BudgetSummaryMapper;
import com.financial.sharing.budgetPlanning.service.BudgetSummaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预算数据汇总Service实现类
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Service
public class BudgetSummaryServiceImpl implements BudgetSummaryService {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(BudgetSummaryServiceImpl.class);

    @Autowired
    private BudgetSummaryMapper summaryMapper;

    @Autowired
    private BudgetDataMapper dataMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeSummary(String modelId, String period, String version, String summaryType, String summaryMethod) {
        String orgId = UserUtils.requireOrgId();
        String userId = UserUtils.requireUserId();
        Date now = new Date();

        try {
            // 删除已存在的汇总记录
            QueryWrapper<TblBudgetSummary> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("MODEL_ID", modelId);
            deleteWrapper.eq("PERIOD", period);
            deleteWrapper.eq("VERSION", version);
            deleteWrapper.eq("SUMMARY_TYPE", summaryType);
            deleteWrapper.eq("ORG_ID", orgId);
            summaryMapper.delete(deleteWrapper);

            // 查询已审批的预算数据
            QueryWrapper<TblBudgetData> dataWrapper = new QueryWrapper<>();
            dataWrapper.eq("MODEL_ID", modelId);
            dataWrapper.eq("PERIOD", period);
            dataWrapper.eq("VERSION", version);
            dataWrapper.eq("STATUS", "APPROVED");
            dataWrapper.eq("ORG_ID", orgId);
            List<TblBudgetData> dataList = dataMapper.selectList(dataWrapper);

            if (dataList.isEmpty()) {
                throw new RuntimeException("没有已审批的预算数据可供汇总");
            }

            // 根据汇总类型进行汇总
            Map<String, List<TblBudgetData>> groupedData = new HashMap<>();
            
            if ("SUBJECT".equals(summaryType)) {
                // 按科目汇总
                groupedData = dataList.stream()
                    .collect(Collectors.groupingBy(TblBudgetData::getSubjectCode));
            } else if ("ORGANIZATION".equals(summaryType)) {
                // 按组织汇总
                groupedData = dataList.stream()
                    .collect(Collectors.groupingBy(TblBudgetData::getOrganizationCode));
            } else if ("PERIOD".equals(summaryType)) {
                // 按期间汇总
                groupedData = dataList.stream()
                    .collect(Collectors.groupingBy(TblBudgetData::getPeriod));
            } else {
                // 自定义汇总 - 按科目+组织汇总
                groupedData = dataList.stream()
                    .collect(Collectors.groupingBy(data -> 
                        data.getSubjectCode() + "_" + data.getOrganizationCode()));
            }

            // 计算汇总值并保存
            for (Map.Entry<String, List<TblBudgetData>> entry : groupedData.entrySet()) {
                String dimensionCode = entry.getKey();
                List<TblBudgetData> group = entry.getValue();

                BigDecimal summaryValue = calculateSummaryValue(group, summaryMethod);

                TblBudgetSummary summary = new TblBudgetSummary();
                summary.setSummaryId(UUID.randomUUID().toString().replace("-", ""));
                summary.setModelId(modelId);
                summary.setPeriod(period);
                summary.setVersion(version);
                summary.setSummaryType(summaryType);
                summary.setDimensionCode(dimensionCode);
                summary.setDimensionName(getDimensionName(dimensionCode, summaryType, group));
                summary.setSummaryValue(summaryValue);
                summary.setSummaryMethod(summaryMethod);
                summary.setDataCount(group.size());
                summary.setStatus("COMPLETED");
                summary.setOrgId(orgId);
                summary.setCreateUser(userId);
                summary.setCreateTime(now);
                summary.setUpdateUser(userId);
                summary.setUpdateTime(now);

                summaryMapper.insert(summary);
            }

        } catch (Exception e) {
            // 记录失败信息
            TblBudgetSummary failedSummary = new TblBudgetSummary();
            failedSummary.setSummaryId(UUID.randomUUID().toString().replace("-", ""));
            failedSummary.setModelId(modelId);
            failedSummary.setPeriod(period);
            failedSummary.setVersion(version);
            failedSummary.setSummaryType(summaryType);
            failedSummary.setSummaryMethod(summaryMethod);
            failedSummary.setStatus("FAILED");
            failedSummary.setErrorMessage(e.getMessage());
            failedSummary.setOrgId(orgId);
            failedSummary.setCreateUser(userId);
            failedSummary.setCreateTime(now);
            failedSummary.setUpdateUser(userId);
            failedSummary.setUpdateTime(now);
            summaryMapper.insert(failedSummary);

            // 不再加"汇总执行失败:"前缀, 由 controller 层统一处理, 避免双前缀
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 计算汇总值
     */
    private BigDecimal calculateSummaryValue(List<TblBudgetData> dataList, String summaryMethod) {
        if (dataList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 从dataValues JSON中提取数值进行汇总
        // 这里简化处理,假设每条数据有一个amount字段
        List<BigDecimal> values = new ArrayList<>();
        for (TblBudgetData data : dataList) {
            // TODO: 实际应该解析dataValues JSON,提取具体的数值字段
            // 这里暂时使用模拟数据
            values.add(new BigDecimal(Math.random() * 10000));
        }

        if ("SUM".equals(summaryMethod)) {
            return values.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else if ("AVG".equals(summaryMethod)) {
            BigDecimal sum = values.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            return sum.divide(new BigDecimal(values.size()), 2, RoundingMode.HALF_UP);
        } else if ("MAX".equals(summaryMethod)) {
            return values.stream()
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        } else if ("MIN".equals(summaryMethod)) {
            return values.stream()
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        } else if ("COUNT".equals(summaryMethod)) {
            return new BigDecimal(values.size());
        }

        return BigDecimal.ZERO;
    }

    /**
     * 获取维度名称
     */
    private String getDimensionName(String dimensionCode, String summaryType, List<TblBudgetData> dataList) {
        if (dataList.isEmpty()) {
            return dimensionCode;
        }

        TblBudgetData first = dataList.get(0);
        if ("SUBJECT".equals(summaryType)) {
            return first.getSubjectCode();
        } else if ("ORGANIZATION".equals(summaryType)) {
            return first.getOrganizationCode();
        } else if ("PERIOD".equals(summaryType)) {
            return first.getPeriod();
        }

        return dimensionCode;
    }

    @Override
    public PageInfo<TblBudgetSummary> getSummaryList(BudgetSummaryQueryParam param) {
        param.setOrgId(UserUtils.requireOrgId());
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblBudgetSummary> list = summaryMapper.selectSummaryList(param);
        return new PageInfo<>(list);
    }

    @Override
    public TblBudgetSummary getSummaryById(String summaryId) {
        return summaryMapper.selectById(summaryId);
    }

    @Override
    public List<TblBudgetSummary> getSummaryByCondition(String modelId, String period, String version, String summaryType) {
        String orgId = UserUtils.requireOrgId();
        return summaryMapper.selectByCondition(modelId, period, version, summaryType, orgId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSummary(String summaryId) {
        TblBudgetSummary summary = summaryMapper.selectById(summaryId);
        if (summary == null) {
            throw new RuntimeException("汇总记录不存在");
        }

        String orgId = UserUtils.requireOrgId();
        if (!orgId.equals(summary.getOrgId())) {
            throw new RuntimeException("无权限删除该汇总记录");
        }

        summaryMapper.deleteById(summaryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteSummary(List<String> summaryIds) {
        for (String summaryId : summaryIds) {
            deleteSummary(summaryId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reExecuteSummary(String summaryId) {
        TblBudgetSummary summary = summaryMapper.selectById(summaryId);
        if (summary == null) {
            throw new RuntimeException("汇总记录不存在");
        }

        executeSummary(
            summary.getModelId(),
            summary.getPeriod(),
            summary.getVersion(),
            summary.getSummaryType(),
            summary.getSummaryMethod()
        );
    }

    @Override
    public Map<String, Object> getSummaryStatistics() {
        String orgId = UserUtils.requireOrgId();
        Map<String, Object> raw = summaryMapper.selectSummaryStatistics(orgId);
        log.info("[BudgetSummary.statistics] orgId={}, raw={}", orgId, raw);
        // 关键: 达梦驱动可能把别名转成大写, 这里做大小写归一化, 同名取非空值优先
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("totalCount", 0);
        result.put("completedCount", 0);
        result.put("processingCount", 0);
        result.put("failedCount", 0);
        result.put("totalDataCount", 0);
        if (raw != null) {
            for (Map.Entry<String, Object> entry : raw.entrySet()) {
                String camelKey = toCamelLower(entry.getKey());
                Object val = entry.getValue();
                if (val == null) {
                    continue;
                }
                // 已经存在非零值的 key 不要被 0 覆盖
                Object existed = result.get(camelKey);
                if (existed == null || isZeroLike(existed)) {
                    result.put(camelKey, val);
                }
            }
        }
        return result;
    }

    /** "TOTALCOUNT" / "totalCount" / "totalcount" 全部归一化成 "totalCount" 等已知 key */
    private static String toCamelLower(String key) {
        if (key == null) return "";
        // 已知 5 个 key, 直接做大小写不敏感映射, 简单可靠
        String low = key.toLowerCase();
        switch (low) {
            case "totalcount":     return "totalCount";
            case "completedcount": return "completedCount";
            case "processingcount":return "processingCount";
            case "failedcount":    return "failedCount";
            case "totaldatacount": return "totalDataCount";
            default: return key; // 其他 key 原样保留
        }
    }

    private static boolean isZeroLike(Object v) {
        if (v == null) return true;
        if (v instanceof Number) return ((Number) v).longValue() == 0L;
        String s = v.toString();
        return s.isEmpty() || "0".equals(s);
    }

    @Override
    public void exportSummary(String summaryId, javax.servlet.http.HttpServletResponse response) {
        TblBudgetSummary anchor = summaryMapper.selectById(summaryId);
        if (anchor == null) {
            throw new RuntimeException("汇总记录不存在");
        }
        // 同一批次(模型/期间/版本/类型)的全部维度数据
        String orgId = UserUtils.requireOrgId();
        List<TblBudgetSummary> list = summaryMapper.selectByCondition(
                anchor.getModelId(), anchor.getPeriod(), anchor.getVersion(),
                anchor.getSummaryType(), orgId
        );

        // 中文映射, 让导出可读
        java.util.Map<String, String> typeMap = new java.util.HashMap<>();
        typeMap.put("SUBJECT", "科目汇总");
        typeMap.put("ORGANIZATION", "组织汇总");
        typeMap.put("PERIOD", "期间汇总");
        typeMap.put("CUSTOM", "自定义汇总");
        java.util.Map<String, String> methodMap = new java.util.HashMap<>();
        methodMap.put("SUM", "求和");
        methodMap.put("AVG", "平均");
        methodMap.put("MAX", "最大");
        methodMap.put("MIN", "最小");
        methodMap.put("COUNT", "计数");
        java.util.Map<String, String> statusMap = new java.util.HashMap<>();
        statusMap.put("PROCESSING", "处理中");
        statusMap.put("COMPLETED", "已完成");
        statusMap.put("FAILED", "失败");

        // 动态表头
        java.util.List<String> headers = java.util.Arrays.asList(
                "预算期间", "预算版本", "汇总类型", "维度编码", "维度名称",
                "汇总方法", "汇总值", "数据条数", "状态", "创建时间"
        );
        // 数据行(Map 形式, 与 ExcelExportUtil.exportDynamicHeaders 对齐)
        java.util.List<java.util.Map<String, Object>> rows = new java.util.ArrayList<>();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (TblBudgetSummary s : list) {
            java.util.Map<String, Object> row = new java.util.LinkedHashMap<>();
            row.put("预算期间", s.getPeriod());
            row.put("预算版本", s.getVersion());
            row.put("汇总类型", typeMap.getOrDefault(s.getSummaryType(), s.getSummaryType()));
            row.put("维度编码", s.getDimensionCode());
            row.put("维度名称", s.getDimensionName());
            row.put("汇总方法", methodMap.getOrDefault(s.getSummaryMethod(), s.getSummaryMethod()));
            row.put("汇总值", s.getSummaryValue());
            row.put("数据条数", s.getDataCount());
            row.put("状态", statusMap.getOrDefault(s.getStatus(), s.getStatus()));
            row.put("创建时间", s.getCreateTime() == null ? "" : sdf.format(s.getCreateTime()));
            rows.add(row);
        }

        String fileName = "预算汇总_" + anchor.getPeriod() + "_" + anchor.getVersion();
        com.financial.sharing.util.ExcelExportUtil.exportDynamicHeaders(
                response, rows, headers, fileName, "汇总明细"
        );
    }
}

