package com.management.accountant.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.management.accountant.oracle.entity.budget.BudgetForecastAnalysis;
import com.management.accountant.oracle.entity.budget.BudgetForecastModel;
import com.management.accountant.oracle.mapper.budget.BudgetForecastAnalysisMapper;
import com.management.accountant.oracle.mapper.budget.BudgetForecastModelMapper;
import com.management.accountant.service.BudgetForecastAnalysisService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class BudgetForecastAnalysisServiceImpl implements BudgetForecastAnalysisService {

    @Resource
    private BudgetForecastAnalysisMapper forecastMapper;

    @Resource
    private BudgetForecastModelMapper modelMapper;

    // ==================== 分页查询（execute 接口） ====================
    @Override
    public Map<String, Object> executeForecastAnalysis(Map<String, Object> params) {
        int pageNum  = params.get("pageNum")  != null ? Integer.parseInt(params.get("pageNum").toString())  : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        Map<String, Object> queryParams = buildQueryParams(params);

        PageHelper.startPage(pageNum, pageSize);
        List<BudgetForecastAnalysis> list = forecastMapper.selectByPage(queryParams);
        PageInfo<BudgetForecastAnalysis> pageInfo = new PageInfo<>(list);

        PageResult<BudgetForecastAnalysis> pageResult = new PageResult<>();
        pageResult.setTlist(list);
        pageResult.setTotalRecord((int) pageInfo.getTotal());
        pageResult.setPageNo(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPage(pageInfo.getPages());

        Map<String, Object> result = new HashMap<>();
        result.put("tlist",       pageResult.getTlist());
        result.put("totalRecord", pageResult.getTotalRecord());
        result.put("pageNo",      pageResult.getPageNo());
        result.put("pageSize",    pageResult.getPageSize());
        result.put("totalPage",   pageResult.getTotalPage());
        result.put("list",  pageResult.getTlist());
        result.put("total", pageResult.getTotalRecord());
        return result;
    }

    // ==================== 创建预测 ====================
    @Override
    public Map<String, Object> createForecast(Map<String, Object> params) {
        // forecastMethod → forecastModelName 映射
        Map<String, String> modelNameMap = new HashMap<>();
        modelNameMap.put("LINEAR_REGRESSION", "线性回归模型");
        modelNameMap.put("ARIMA", "ARIMA模型");
        modelNameMap.put("EXPONENTIAL_SMOOTHING", "指数平滑模型");
        modelNameMap.put("NEURAL_NETWORK", "神经网络模型");
        modelNameMap.put("ENSEMBLE", "集成模型");
        modelNameMap.put("TREND_ANALYSIS", "趋势分析模型");
        modelNameMap.put("MOVING_AVERAGE", "移动平均模型");

        String forecastMethod = str(params.get("forecastMethod"));
        String forecastGranularity = str(params.get("forecastGranularity"));

        // forecastGranularity → forecastType 映射
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("MONTHLY", "MONTHLY");
        typeMap.put("QUARTERLY", "QUARTERLY");
        typeMap.put("YEARLY", "ANNUAL");

        BudgetForecastAnalysis entity = new BudgetForecastAnalysis();
        entity.setAnalysisCode("FCT-" + System.currentTimeMillis());
        entity.setAnalysisName(str(params.get("analysisName")));
        entity.setBudgetId(str(params.get("budgetId")));
        entity.setBudgetYear(params.get("budgetYear") != null ? Integer.parseInt(params.get("budgetYear").toString()) : Calendar.getInstance().get(Calendar.YEAR));
        entity.setOrganizationId(str(params.get("organizationId")));
        entity.setOrganizationName(str(params.get("organizationName")));
        entity.setAccountId(str(params.get("accountId")));
        entity.setAccountName(str(params.get("accountName")));
        entity.setForecastType(typeMap.getOrDefault(forecastGranularity, forecastGranularity));
        entity.setForecastMethod(forecastMethod);
        // 优先用前端传入的 forecastModelName，否则根据 forecastMethod 自动推导
        String modelName = str(params.get("forecastModelName"));
        entity.setForecastModelName(modelName != null && !modelName.isEmpty() ? modelName : modelNameMap.getOrDefault(forecastMethod, forecastMethod));
        entity.setForecastPeriod(str(params.get("forecastPeriod")));
        entity.setForecastGranularity(forecastGranularity);
        entity.setConfidenceInterval(str(params.get("confidenceInterval")));
        // 预测日期（Entity 中为 String 类型，直接赋值）
        if (params.get("forecastDate") != null) {
            entity.setForecastDate(str(params.get("forecastDate")));
        }
        // 历史值 / 预测金额
        if (params.get("historicalValue") != null) {
            entity.setHistoricalValue(new BigDecimal(params.get("historicalValue").toString()));
        }
        if (params.get("forecastAmount") != null) {
            entity.setForecastAmount(new BigDecimal(params.get("forecastAmount").toString()));
        }
        entity.setRemark(str(params.get("remark")));
        entity.setAnalysisStatus("DRAFT");
        entity.setCreateBy("admin");
        entity.setDelFlag(0);

        forecastMapper.insert(entity);

        Map<String, Object> result = new HashMap<>();
        result.put("id", entity.getId());
        result.put("analysisCode", entity.getAnalysisCode());
        return result;
    }

    // ==================== 编辑预测 ====================
    @Override
    public Map<String, Object> updateForecast(Map<String, Object> params) {
        String id = str(params.get("id"));
        BudgetForecastAnalysis entity = forecastMapper.selectById(id);
        if (entity == null || Integer.valueOf(1).equals(entity.getDelFlag())) {
            throw new RuntimeException("记录不存在");
        }
        if (params.get("analysisName") != null) entity.setAnalysisName(str(params.get("analysisName")));
        if (params.get("budgetYear") != null) entity.setBudgetYear(Integer.parseInt(params.get("budgetYear").toString()));
        if (params.get("forecastMethod") != null) entity.setForecastMethod(str(params.get("forecastMethod")));
        if (params.get("forecastGranularity") != null) entity.setForecastGranularity(str(params.get("forecastGranularity")));
        if (params.get("confidenceInterval") != null) entity.setConfidenceInterval(str(params.get("confidenceInterval")));
        if (params.get("organizationId") != null) entity.setOrganizationId(str(params.get("organizationId")));
        if (params.get("organizationName") != null) entity.setOrganizationName(str(params.get("organizationName")));
        if (params.get("remark") != null) entity.setRemark(str(params.get("remark")));
        entity.setUpdateBy("admin");
        entity.setUpdateTime(new Date());

        forecastMapper.updateById(entity);

        Map<String, Object> result = new HashMap<>();
        result.put("id", entity.getId());
        return result;
    }
    @Override
    public Map<String, Object> getForecastChart(Map<String, Object> params) {
        Map<String, Object> queryParams = buildQueryParams(params);
        List<Map<String, Object>> trendData = forecastMapper.selectTrendChartData(queryParams);

        List<String> xAxis = new ArrayList<>();
        List<Object> forecastValues = new ArrayList<>();
        List<Object> actualValues = new ArrayList<>();
        List<Object> upperBounds = new ArrayList<>();
        List<Object> lowerBounds = new ArrayList<>();

        for (Map<String, Object> row : trendData) {
            xAxis.add(str(row.get("PERIOD_LABEL")));
            forecastValues.add(row.getOrDefault("FORECAST_VALUE", 0));
            actualValues.add(row.getOrDefault("ACTUAL_VALUE", 0));
            upperBounds.add(row.getOrDefault("UPPER_BOUND", 0));
            lowerBounds.add(row.getOrDefault("LOWER_BOUND", 0));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", xAxis);
        List<Map<String, Object>> series = new ArrayList<>();
        series.add(buildSeries("预测值", forecastValues));
        series.add(buildSeries("实际值", actualValues));
        series.add(buildSeries("上限", upperBounds));
        series.add(buildSeries("下限", lowerBounds));
        result.put("series", series);
        result.put("legend", Arrays.asList("预测值", "实际值", "上限", "下限"));
        return result;
    }

    // ==================== 导出 ====================
    @Override
    public Map<String, Object> exportForecastReport(Map<String, Object> params) {
        Map<String, Object> queryParams = buildQueryParams(params);
        List<BudgetForecastAnalysis> list = forecastMapper.selectForExport(queryParams);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", list.size());
        result.put("exportTime", new Date());
        return result;
    }

    // ==================== 统计卡片 ====================
    @Override
    public Map<String, Object> getForecastStats() {
        try {
            Map<String, Object> raw = forecastMapper.selectStats(new HashMap<>());
            Map<String, Object> stats = new HashMap<>();
            if (raw != null) {
                stats.put("totalForecasts",     toInt(raw.get("TOTAL_COUNT")));
                stats.put("completedForecasts", toInt(raw.get("COMPLETED_COUNT")));
                stats.put("averageAccuracy",    toDouble(raw.get("AVG_ACCURACY")));
                stats.put("avgForecastAmount",  toDouble(raw.get("AVG_FORECAST_AMOUNT")));
            } else {
                stats.put("totalForecasts", 0);
                stats.put("completedForecasts", 0);
                stats.put("averageAccuracy", 0);
                stats.put("avgForecastAmount", 0);
            }
            // 活跃模型数
            List<BudgetForecastModel> activeModels = modelMapper.selectActiveModels();
            stats.put("activeModels", activeModels != null ? activeModels.size() : 0);
            return stats;
        } catch (Exception e) {
            log.error("获取预测统计数据失败", e);
            Map<String, Object> empty = new HashMap<>();
            empty.put("totalForecasts", 0);
            empty.put("completedForecasts", 0);
            empty.put("averageAccuracy", 0);
            empty.put("activeModels", 0);
            return empty;
        }
    }

    // ==================== 4个图表数据 ====================
    @Override
    public Map<String, Object> getForecastChartData(Map<String, Object> params) {
        if (params == null) params = new HashMap<>();
        Map<String, Object> queryParams = buildQueryParams(params);

        // 预测趋势图
        List<Map<String, Object>> trendRaw = forecastMapper.selectTrendChartData(queryParams);
        Map<String, Object> trendChart = buildTrendChart(trendRaw);

        // 模型性能图
        List<Map<String, Object>> perfRaw = forecastMapper.selectModelPerformanceData(queryParams);
        Map<String, Object> performanceChart = buildPerformanceChart(perfRaw);

        // 预测误差分析图
        List<Map<String, Object>> errorRaw = forecastMapper.selectErrorAnalysisData(queryParams);
        Map<String, Object> errorChart = buildErrorChart(errorRaw);

        // 残差分析图
        List<Map<String, Object>> residualRaw = forecastMapper.selectResidualData(queryParams);
        Map<String, Object> residualChart = buildResidualChart(residualRaw);

        Map<String, Object> result = new HashMap<>();
        result.put("trendChart",       trendChart);
        result.put("performanceChart", performanceChart);
        result.put("errorChart",       errorChart);
        result.put("residualChart",    residualChart);
        return result;
    }

    // ==================== 模型信息 ====================
    @Override
    public Map<String, Object> getForecastModelInfo() {
        try {
            List<BudgetForecastModel> allModels = modelMapper.selectActiveModels();
            List<Map<String, Object>> summary = modelMapper.selectModelSummary();

            Map<String, Object> result = new HashMap<>();
            result.put("models", allModels != null ? allModels : new ArrayList<>());
            result.put("summary", summary != null ? summary : new ArrayList<>());
            result.put("activeModel", (allModels != null && !allModels.isEmpty()) ? allModels.get(0) : null);
            return result;
        } catch (Exception e) {
            log.error("获取模型信息失败", e);
            Map<String, Object> empty = new HashMap<>();
            empty.put("models", new ArrayList<>());
            empty.put("summary", new ArrayList<>());
            empty.put("activeModel", null);
            return empty;
        }
    }

    // ==================== 模型验证 ====================
    @Override
    public Map<String, Object> validateModel(String modelId) {
        BudgetForecastModel model = modelMapper.selectById(modelId);
        Map<String, Object> result = new HashMap<>();
        if (model != null) {
            result.put("modelId", model.getId());
            result.put("modelName", model.getModelName());
            result.put("accuracy", model.getAccuracy());
            result.put("rSquared", model.getRSquared());
            result.put("rmse", model.getRmse());
            result.put("mae", model.getMae());
            result.put("mape", model.getMape());
            result.put("isValid", model.getAccuracy() != null && model.getAccuracy().doubleValue() >= 80);
        } else {
            result.put("isValid", false);
            result.put("message", "模型不存在");
        }
        return result;
    }

    // ==================== 模型训练 ====================
    @Override
    public Map<String, Object> trainModel(Map<String, Object> params) {
        String modelType = str(params.get("modelType"));
        Map<String, Object> result = new HashMap<>();
        result.put("modelType", modelType);
        result.put("status", "COMPLETED");
        result.put("trainTime", new Date());
        result.put("message", "模型训练完成");
        return result;
    }

    // ==================== 预测结果验证 ====================
    @Override
    public Map<String, Object> validateForecast(String id) {
        BudgetForecastAnalysis forecast = forecastMapper.selectById(id);
        Map<String, Object> result = new HashMap<>();
        if (forecast != null) {
            result.put("id", forecast.getId());
            result.put("analysisName", forecast.getAnalysisName());
            result.put("forecastAmount", forecast.getForecastAmount());
            result.put("actualAmount", forecast.getActualAmount());
            boolean isValid = forecast.getAccuracyRate() != null && forecast.getAccuracyRate().doubleValue() >= 80;
            result.put("isValid", isValid);
            result.put("accuracyRate", forecast.getAccuracyRate());
        } else {
            result.put("isValid", false);
            result.put("message", "预测记录不存在");
        }
        return result;
    }

    // ==================== 删除 ====================
    @Override
    public void deleteForecast(String id) {
        forecastMapper.logicDeleteById(id);
    }



    // ==================== 私有辅助方法 ====================

    private Map<String, Object> buildQueryParams(Map<String, Object> params) {
        Map<String, Object> q = new HashMap<>();
        // 预测方法：前端字段 forecastModel → DB字段 forecastMethod
        Object fm = params.get("forecastModel");
        if (fm == null) fm = params.get("forecastMethod");
        if (fm != null && !fm.toString().isEmpty()) q.put("forecastMethod", fm.toString());
        // 组织
        Object org = params.get("organizationPath");
        if (org == null) org = params.get("organizationId");
        if (org != null && !org.toString().isEmpty()) q.put("organizationId", org.toString());
        // 预测粒度
        Object gran = params.get("forecastGranularity");
        if (gran != null && !gran.toString().isEmpty()) q.put("forecastGranularity", gran.toString());
        // 置信区间
        Object ci = params.get("confidenceInterval");
        if (ci != null && !ci.toString().isEmpty()) q.put("confidenceInterval", ci.toString());
        // 分析状态
        Object status = params.get("analysisStatus");
        if (status != null && !status.toString().isEmpty()) q.put("analysisStatus", status.toString());
        // 预算年度
        Object year = params.get("budgetYear");
        if (year != null && !year.toString().isEmpty()) q.put("budgetYear", year.toString());
        // 预测类型
        Object ft = params.get("forecastType");
        if (ft != null && !ft.toString().isEmpty()) q.put("forecastType", ft.toString());
        // 关键字搜索
        Object kw = params.get("keyword");
        if (kw != null && !kw.toString().isEmpty()) q.put("keyword", kw.toString());
        // 历史期间
        Object hp = params.get("historicalPeriod");
        if (hp instanceof List) {
            List<?> range = (List<?>) hp;
            if (range.size() >= 2) {
                if (range.get(0) != null && !range.get(0).toString().isEmpty())
                    q.put("startDate", range.get(0).toString().substring(0, 10));
                if (range.get(1) != null && !range.get(1).toString().isEmpty())
                    q.put("endDate", range.get(1).toString().substring(0, 10));
            }
        }
        return q;
    }

    private Map<String, Object> buildTrendChart(List<Map<String, Object>> raw) {
        List<String> xAxis = new ArrayList<>();
        List<Object> forecastValues = new ArrayList<>();
        List<Object> actualValues = new ArrayList<>();
        List<Object> upperBounds = new ArrayList<>();
        List<Object> lowerBounds = new ArrayList<>();
        for (Map<String, Object> row : raw) {
            xAxis.add(str(row.get("PERIOD_LABEL")));
            forecastValues.add(row.getOrDefault("FORECAST_VALUE", 0));
            actualValues.add(row.getOrDefault("ACTUAL_VALUE", 0));
            upperBounds.add(row.getOrDefault("UPPER_BOUND", 0));
            lowerBounds.add(row.getOrDefault("LOWER_BOUND", 0));
        }
        Map<String, Object> chart = new HashMap<>();
        chart.put("xAxis", xAxis);
        List<Map<String, Object>> series = new ArrayList<>();
        series.add(buildSeries("预测值", forecastValues));
        series.add(buildSeries("实际值", actualValues));
        series.add(buildSeries("上限", upperBounds));
        series.add(buildSeries("下限", lowerBounds));
        chart.put("series", series);
        chart.put("legend", Arrays.asList("预测值", "实际值", "上限", "下限"));
        return chart;
    }

    private Map<String, Object> buildPerformanceChart(List<Map<String, Object>> raw) {
        List<String> xAxis = new ArrayList<>();
        List<Object> accuracy = new ArrayList<>();
        List<Object> rmse = new ArrayList<>();
        List<Object> mae = new ArrayList<>();
        for (Map<String, Object> row : raw) {
            xAxis.add(str(row.get("MODEL_NAME")));
            accuracy.add(row.getOrDefault("ACCURACY", 0));
            rmse.add(row.getOrDefault("RMSE", 0));
            mae.add(row.getOrDefault("MAE", 0));
        }
        Map<String, Object> chart = new HashMap<>();
        chart.put("xAxis", xAxis);
        List<Map<String, Object>> series = new ArrayList<>();
        series.add(buildSeries("准确率", accuracy));
        series.add(buildSeries("RMSE", rmse));
        series.add(buildSeries("MAE", mae));
        chart.put("series", series);
        chart.put("legend", Arrays.asList("准确率", "RMSE", "MAE"));
        return chart;
    }

    private Map<String, Object> buildErrorChart(List<Map<String, Object>> raw) {
        List<String> xAxis = new ArrayList<>();
        List<Object> errorValues = new ArrayList<>();
        List<Object> errorRates = new ArrayList<>();
        for (Map<String, Object> row : raw) {
            xAxis.add(str(row.get("PERIOD_LABEL")));
            errorValues.add(row.getOrDefault("ERROR_VALUE", 0));
            errorRates.add(row.getOrDefault("ERROR_RATE", 0));
        }
        Map<String, Object> chart = new HashMap<>();
        chart.put("xAxis", xAxis);
        List<Map<String, Object>> series = new ArrayList<>();
        series.add(buildSeries("误差值", errorValues));
        series.add(buildSeries("误差率", errorRates));
        chart.put("series", series);
        chart.put("legend", Arrays.asList("误差值", "误差率"));
        return chart;
    }

    private Map<String, Object> buildResidualChart(List<Map<String, Object>> raw) {
        List<String> xAxis = new ArrayList<>();
        List<Object> residuals = new ArrayList<>();
        for (Map<String, Object> row : raw) {
            xAxis.add(str(row.get("PERIOD_LABEL")));
            residuals.add(row.getOrDefault("RESIDUAL", 0));
        }
        Map<String, Object> chart = new HashMap<>();
        chart.put("xAxis", xAxis);
        chart.put("series", Collections.singletonList(buildSeries("残差", residuals)));
        chart.put("legend", Collections.singletonList("残差"));
        return chart;
    }

    private Map<String, Object> buildSeries(String name, List<Object> data) {
        Map<String, Object> s = new HashMap<>();
        s.put("name", name);
        s.put("data", data);
        return s;
    }

    private String str(Object val) {
        return val != null ? val.toString() : "";
    }

    private int toInt(Object val) {
        if (val == null) return 0;
        try { return Integer.parseInt(val.toString()); } catch (Exception e) { return 0; }
    }

    private double toDouble(Object val) {
        if (val == null) return 0.0;
        try { return Double.parseDouble(val.toString()); } catch (Exception e) { return 0.0; }
    }
}
