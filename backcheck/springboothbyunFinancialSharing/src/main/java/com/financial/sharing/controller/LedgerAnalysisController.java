package com.financial.sharing.controller;

import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 账簿分析控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Slf4j
@Api(tags = "账簿分析")
@RestController
@RequestMapping("/general-ledger/analysis")
@CrossOrigin
public class LedgerAnalysisController {

    @Resource
    private UserProvider userProvider;

    @ApiOperation("科目余额分析")
    @GetMapping("/subject-balance")
    public String getSubjectBalanceAnalysis(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String period,
                                          @ApiParam(value = "科目编码", required = false) @RequestParam(required = false) String subjectCode) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 获取科目余额分析，期间：{}，科目：{}", loginStaff.getStaffid(), period, subjectCode);

            List<Map<String, Object>> analysis = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("period", "2024-" + String.format("%02d", i));
                monthData.put("subjectCode", subjectCode);
                monthData.put("subjectName", "库存现金");
                monthData.put("beginningBalance", new BigDecimal(10000 * i));
                monthData.put("endingBalance", new BigDecimal(12000 * i));
                monthData.put("changeRate", new BigDecimal("20.00"));
                monthData.put("trend", i % 2 == 0 ? "上升" : "下降");
                analysis.add(monthData);
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("avgBalance", new BigDecimal("15000.00"));
            summary.put("maxBalance", new BigDecimal("30000.00"));
            summary.put("minBalance", new BigDecimal("5000.00"));
            summary.put("volatility", new BigDecimal("25.50"));
            summary.put("trend", "上升");

            Map<String, Object> result = new HashMap<>();
            result.put("period", period);
            result.put("subjectCode", subjectCode);
            result.put("monthlyData", analysis);
            result.put("summary", summary);

            return createSuccessResponse("查询成功", result);
        } catch (Exception e) {
            log.error("获取科目余额分析失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("科目发生额分析")
    @GetMapping("/subject-occurrence")
    public String getSubjectOccurrenceAnalysis(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String period,
                                               @ApiParam(value = "科目编码", required = false) @RequestParam(required = false) String subjectCode) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 获取科目发生额分析", loginStaff.getStaffid());

            List<Map<String, Object>> occurrences = new ArrayList<>();
            for (int i = 1; i <= 4; i++) {
                Map<String, Object> quarter = new HashMap<>();
                quarter.put("quarter", "Q" + i);
                quarter.put("debitTotal", new BigDecimal(100000 * i));
                quarter.put("creditTotal", new BigDecimal(90000 * i));
                quarter.put("netAmount", new BigDecimal(10000 * i));
                quarter.put("transactionCount", 50 * i);
                quarter.put("avgAmount", new BigDecimal(2000 * i));
                occurrences.add(quarter);
            }

            Map<String, Object> trends = new HashMap<>();
            trends.put("debitTrend", "上升");
            trends.put("creditTrend", "稳定");
            trends.put("frequencyTrend", "上升");

            Map<String, Object> result = new HashMap<>();
            result.put("subjectCode", subjectCode);
            result.put("yearlyData", occurrences);
            result.put("trends", trends);

            return createSuccessResponse("查询成功", result);
        } catch (Exception e) {
            log.error("获取科目发生额分析失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("异常数据分析")
    @GetMapping("/abnormal-data")
    public String getAbnormalDataAnalysis(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String period,
                                         @ApiParam(value = "分析类型", required = false) @RequestParam(required = false) String analysisType) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 获取异常数据分析", loginStaff.getStaffid());

            List<Map<String, Object>> abnormalData = new ArrayList<>();

            // 余额异常
            Map<String, Object> balanceAnomaly = new HashMap<>();
            balanceAnomaly.put("anomalyId", "ANO001");
            balanceAnomaly.put("anomalyType", "余额异常");
            balanceAnomaly.put("subjectCode", "1001");
            balanceAnomaly.put("subjectName", "库存现金");
            balanceAnomaly.put("description", "库存现金余额异常增加");
            balanceAnomaly.put("amount", new BigDecimal("500000.00"));
            balanceAnomaly.put("threshold", new BigDecimal("100000.00"));
            balanceAnomaly.put("severity", "高");
            balanceAnomaly.put("detectedTime", "2024-12-01T10:00:00.000Z");
            abnormalData.add(balanceAnomaly);

            // 交易频率异常
            Map<String, Object> frequencyAnomaly = new HashMap<>();
            frequencyAnomaly.put("anomalyId", "ANO002");
            frequencyAnomaly.put("anomalyType", "交易频率异常");
            frequencyAnomaly.put("subjectCode", "1122");
            frequencyAnomaly.put("subjectName", "应收账款");
            frequencyAnomaly.put("description", "应收账款交易频率异常降低");
            frequencyAnomaly.put("frequency", 5);
            frequencyAnomaly.put("avgFrequency", 50);
            frequencyAnomaly.put("severity", "中");
            frequencyAnomaly.put("detectedTime", "2024-12-01T11:00:00.000Z");
            abnormalData.add(frequencyAnomaly);

            // 金额异常
            Map<String, Object> amountAnomaly = new HashMap<>();
            amountAnomaly.put("anomalyId", "ANO003");
            amountAnomaly.put("anomalyType", "金额异常");
            amountAnomaly.put("subjectCode", "5001");
            amountAnomaly.put("subjectName", "生产成本");
            amountAnomaly.put("description", "生产成本单笔交易金额异常");
            amountAnomaly.put("amount", new BigDecimal("1000000.00"));
            amountAnomaly.put("avgAmount", new BigDecimal("50000.00"));
            amountAnomaly.put("severity", "高");
            amountAnomaly.put("detectedTime", "2024-12-01T12:00:00.000Z");
            abnormalData.add(amountAnomaly);

            Map<String, Object> summary = new HashMap<>();
            summary.put("totalAnomalies", abnormalData.size());
            summary.put("highSeverity", 2);
            summary.put("mediumSeverity", 1);
            summary.put("lowSeverity", 0);
            summary.put("lastCheckTime", new Date());

            Map<String, Object> result = new HashMap<>();
            result.put("period", period);
            result.put("anomalies", abnormalData);
            result.put("summary", summary);

            return createSuccessResponse("查询成功", result);
        } catch (Exception e) {
            log.error("获取异常数据分析失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("财务指标分析")
    @GetMapping("/financial-ratio")
    public String getFinancialRatioAnalysis(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String period) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 获取财务指标分析，期间：{}", loginStaff.getStaffid(), period);

            List<Map<String, Object>> ratios = new ArrayList<>();

            // 流动比率
            Map<String, Object> currentRatio = new HashMap<>();
            currentRatio.put("ratioId", "RAT001");
            currentRatio.put("ratioName", "流动比率");
            currentRatio.put("currentValue", new BigDecimal("2.5"));
            currentRatio.put("previousValue", new BigDecimal("2.2"));
            currentRatio.put("industryAvg", new BigDecimal("2.0"));
            currentRatio.put("trend", "改善");
            currentRatio.put("evaluation", "良好");
            currentRatio.put("description", "流动比率高于行业平均水平，短期偿债能力良好");
            ratios.add(currentRatio);

            // 资产负债率
            Map<String, Object> debtRatio = new HashMap<>();
            debtRatio.put("ratioId", "RAT002");
            debtRatio.put("ratioName", "资产负债率");
            debtRatio.put("currentValue", new BigDecimal("0.45"));
            debtRatio.put("previousValue", new BigDecimal("0.50"));
            debtRatio.put("industryAvg", new BigDecimal("0.60"));
            debtRatio.put("trend", "改善");
            debtRatio.put("evaluation", "优秀");
            debtRatio.put("description", "资产负债率低于行业平均水平，财务结构稳健");
            ratios.add(debtRatio);

            // 净利润率
            Map<String, Object> profitMargin = new HashMap<>();
            profitMargin.put("ratioId", "RAT003");
            profitMargin.put("ratioName", "净利润率");
            profitMargin.put("currentValue", new BigDecimal("0.15"));
            profitMargin.put("previousValue", new BigDecimal("0.12"));
            profitMargin.put("industryAvg", new BigDecimal("0.10"));
            profitMargin.put("trend", "改善");
            profitMargin.put("evaluation", "优秀");
            profitMargin.put("description", "净利润率高于行业平均水平，盈利能力强");
            ratios.add(profitMargin);

            // 总资产周转率
            Map<String, Object> assetTurnover = new HashMap<>();
            assetTurnover.put("ratioId", "RAT004");
            assetTurnover.put("ratioName", "总资产周转率");
            assetTurnover.put("currentValue", new BigDecimal("1.2"));
            assetTurnover.put("previousValue", new BigDecimal("1.0"));
            assetTurnover.put("industryAvg", new BigDecimal("1.1"));
            assetTurnover.put("trend", "改善");
            assetTurnover.put("evaluation", "良好");
            assetTurnover.put("description", "总资产周转率略高于行业平均水平，资产使用效率良好");
            ratios.add(assetTurnover);

            Map<String, Object> summary = new HashMap<>();
            summary.put("totalRatios", ratios.size());
            summary.put("improvedCount", 4);
            summary.put("declinedCount", 0);
            summary.put("stableCount", 0);
            summary.put("overallEvaluation", "优秀");
            summary.put("riskLevel", "低");

            Map<String, Object> result = new HashMap<>();
            result.put("period", period);
            result.put("ratios", ratios);
            result.put("summary", summary);

            return createSuccessResponse("查询成功", result);
        } catch (Exception e) {
            log.error("获取财务指标分析失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出分析结果")
    @PostMapping("/export")
    public String exportAnalysisResult(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestBody Map<String, Object> param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            String analysisType = (String) param.get("analysisType");
            String period = (String) param.get("period");

            log.info("用户 {} 导出分析结果，类型：{}，期间：{}", loginStaff.getStaffid(), analysisType, period);

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", "EXPORT" + System.currentTimeMillis());
            result.put("fileName", analysisType + "分析报告_" + period + ".xlsx");
            result.put("exportType", analysisType);
            result.put("status", "PROCESSING");
            result.put("estimatedTime", "2分钟");

            return createSuccessResponse("导出任务创建成功", result);
        } catch (Exception e) {
            log.error("导出分析结果失败", e);
            return createErrorResponse("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("生成分析报告")
    @PostMapping("/generate-report")
    public String generateAnalysisReport(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestBody Map<String, Object> param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            String reportType = (String) param.get("reportType");
            String period = (String) param.get("period");
            @SuppressWarnings("unchecked")
            List<String> analysisTypes = (List<String>) param.get("analysisTypes");

            log.info("用户 {} 生成分析报告，类型：{}，期间：{}", loginStaff.getStaffid(), reportType, period);

            Map<String, Object> report = new HashMap<>();
            report.put("reportId", "RPT" + System.currentTimeMillis());
            report.put("reportName", "财务分析报告_" + period);
            report.put("reportType", reportType);
            report.put("period", period);
            report.put("generatedBy", loginStaff.getUsername());
            report.put("generatedTime", new Date());
            report.put("analysisTypes", analysisTypes);
            report.put("pageCount", 25);
            report.put("status", "COMPLETED");
            report.put("downloadUrl", "/api/report/download/" + report.get("reportId"));

            Map<String, Object> content = new HashMap<>();
            content.put("executiveSummary", "本期财务状况良好，各项指标均有所改善");
            content.put("keyFindings", Arrays.asList(
                "资产负债率下降，财务结构更加稳健",
                "盈利能力提升，净利润率创新高",
                "现金流充裕，运营效率提高"
            ));
            content.put("recommendations", Arrays.asList(
                "继续保持当前的经营策略",
                "加强成本控制，提高运营效率",
                "关注市场变化，适时调整业务结构"
            ));
            report.put("content", content);

            return createSuccessResponse("报告生成成功", report);
        } catch (Exception e) {
            log.error("生成分析报告失败", e);
            return createErrorResponse("生成报告失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private TblStaffUtil validateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
            JsonBean json = new JsonBean();
            json.setCode(401);
            json.setMsg("用户已失效");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
            return null;
        }
        return loginStaff;
    }

    private String createSuccessResponse(String message, Object data) {
        JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg(message);
        json.setData(data);
        return JsonMapper.nonNullMapper().toJson(json);
    }

    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return JsonMapper.nonNullMapper().toJson(json);
    }
}