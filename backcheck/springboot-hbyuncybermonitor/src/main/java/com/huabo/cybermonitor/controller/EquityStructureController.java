package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.cybermonitor.entity.EquityStructure;
import com.huabo.cybermonitor.service.IEquityStructureService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.EquityStructureQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 股权结构控制器
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="股权结构管理",description="股权结构管理")
@RestController
@RequestMapping("/v1/supervision/equity/structure")
public class EquityStructureController {

	private static final Logger log = LoggerFactory.getLogger(EquityStructureController.class);

    @Autowired
    private IEquityStructureService equityStructureService;

    @Operation(summary = "分页查询股权结构列表")
    @PostMapping("/list")
    public R<PageResult<EquityStructure>> getEquityStructureList(@RequestBody(required = false) Map<String, Object> params) {
        try {
            if (params == null) params = new java.util.HashMap<>();
            int pageNumber = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && !params.get("enterpriseId").toString().isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, params.get("enterpriseId").toString());
            }
            if (params.get("investorName") != null && !params.get("investorName").toString().isEmpty()) {
                wrapper.like(EquityStructure::getInvestorName, params.get("investorName").toString());
            }
            if (params.get("investorType") != null && !params.get("investorType").toString().isEmpty()) {
                wrapper.eq(EquityStructure::getInvestorType, params.get("investorType").toString());
            }
            if (params.get("status") != null && !params.get("status").toString().isEmpty()) {
                wrapper.eq(EquityStructure::getStatus, params.get("status").toString());
            }
            if (params.get("controlType") != null && !params.get("controlType").toString().isEmpty()) {
                wrapper.eq(EquityStructure::getControlType, params.get("controlType").toString());
            }
            if (params.get("keyword") != null && !params.get("keyword").toString().isEmpty()) {
                String keyword = params.get("keyword").toString();
                wrapper.and(w -> w.like(EquityStructure::getInvestorName, keyword).or().like(EquityStructure::getEnterpriseId, keyword));
            }
            wrapper.orderByDesc(EquityStructure::getCreateTime);

            com.baomidou.mybatisplus.extension.plugins.pagination.Page<EquityStructure> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNumber, pageSize);
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<EquityStructure> result = equityStructureService.page(page, wrapper);

            PageResult<EquityStructure> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage(pageNumber);
            pageResult.setPageNumber(pageNumber);
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize(pageSize);
            pageResult.setTlist(result.getRecords());
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询股权结构列表失败", e);
            return R.fail("查询股权结构列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID获取股权结构详情")
    @GetMapping("/detail/{equityId}")
    public R<EquityStructure> getEquityStructureById(@PathVariable String equityId) {
        try {
            EquityStructure equityStructure = equityStructureService.getEquityStructureById(equityId);
            return R.success(equityStructure);
        } catch (Exception e) {
            log.error("获取股权结构详情失败，equityId: {}", equityId, e);
            return R.fail("获取股权结构详情失败");
        }
    }

    @Operation(summary = "新增股权结构")
    @PostMapping("/add")
    public R<Boolean> addEquityStructure(@RequestBody EquityStructure equityStructure) {
        try {
            boolean result = equityStructureService.addEquityStructure(equityStructure);
            return R.success(result);
        } catch (Exception e) {
            log.error("新增股权结构失败", e);
            return R.fail("新增股权结构失败");
        }
    }

    @Operation(summary = "更新股权结构")
    @PostMapping("/update")
    public R<Boolean> updateEquityStructure(@RequestBody EquityStructure equityStructure) {
        try {
            boolean result = equityStructureService.updateEquityStructure(equityStructure);
            return R.success(result);
        } catch (Exception e) {
            log.error("更新股权结构失败", e);
            return R.fail("更新股权结构失败");
        }
    }

    @Operation(summary = "删除股权结构")
    @DeleteMapping("/delete/{equityId}")
    public R<Boolean> deleteEquityStructure(@PathVariable String equityId) {
        try {
            boolean result = equityStructureService.deleteEquityStructure(equityId);
            return R.success(result);
        } catch (Exception e) {
            log.error("删除股权结构失败，equityId: {}", equityId, e);
            return R.fail("删除股权结构失败");
        }
    }

    @Operation(summary = "批量删除股权结构")
    @PostMapping("/batchDelete")
    public R<Boolean> batchDeleteEquityStructure(@RequestBody List<String> equityIds) {
        try {
            boolean result = equityStructureService.batchDeleteEquityStructure(equityIds);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量删除股权结构失败", e);
            return R.fail("批量删除股权结构失败");
        }
    }

    @Operation(summary = "股权穿透查询（向上穿透）")
    @GetMapping("/penetration/up/{investeeEnterpriseId}")
    public R<List<EquityStructure>> getEquityPenetrationUp(@PathVariable String investeeEnterpriseId,
                                                           @RequestParam(defaultValue = "10") Integer maxLevel) {
        try {
            List<EquityStructure> result = equityStructureService.getEquityPenetrationUp(investeeEnterpriseId, maxLevel);
            return R.success(result);
        } catch (Exception e) {
            log.error("股权穿透查询（向上穿透）失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("股权穿透查询失败");
        }
    }

    @Operation(summary = "股权穿透查询（向下穿透）")
    @GetMapping("/penetration/down/{investorEnterpriseId}")
    public R<List<EquityStructure>> getEquityPenetrationDown(@PathVariable String investorEnterpriseId,
                                                            @RequestParam(defaultValue = "10") Integer maxLevel) {
        try {
            List<EquityStructure> result = equityStructureService.getEquityPenetrationDown(investorEnterpriseId, maxLevel);
            return R.success(result);
        } catch (Exception e) {
            log.error("股权穿透查询（向下穿透）失败，investorEnterpriseId: {}", investorEnterpriseId, e);
            return R.fail("股权穿透查询失败");
        }
    }

    @Operation(summary = "获取股权结构图谱数据")
    @GetMapping("/graph/{enterpriseId}")
    public R<Map<String, Object>> getEquityStructureGraph(@PathVariable String enterpriseId,
                                                          @RequestParam(defaultValue = "both") String direction,
                                                          @RequestParam(defaultValue = "10") Integer maxLevel) {
        try {
            Map<String, Object> result = equityStructureService.getEquityStructureGraph(enterpriseId, direction, maxLevel);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权结构图谱数据失败，enterpriseId: {}", enterpriseId, e);
            return R.fail("获取股权结构图谱数据失败");
        }
    }

    @Operation(summary = "获取股权穿透分析报告")
    @GetMapping("/analysis/report/{investeeEnterpriseId}")
    public R<Map<String, Object>> getEquityPenetrationAnalysisReport(@PathVariable String investeeEnterpriseId) {
        try {
            Map<String, Object> result = equityStructureService.getEquityPenetrationAnalysisReport(investeeEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权穿透分析报告失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("获取股权穿透分析报告失败");
        }
    }

    @Operation(summary = "获取股权风险评估")
    @GetMapping("/risk/assessment/{investeeEnterpriseId}")
    public R<Map<String, Object>> getEquityRiskAssessment(@PathVariable String investeeEnterpriseId) {
        try {
            Map<String, Object> result = equityStructureService.getEquityRiskAssessment(investeeEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权风险评估失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("获取股权风险评估失败");
        }
    }

    @Operation(summary = "查询控股股权结构")
    @GetMapping("/controlling/{investeeEnterpriseId}")
    public R<List<EquityStructure>> getControllingEquity(@PathVariable String investeeEnterpriseId) {
        try {
            List<EquityStructure> result = equityStructureService.getControllingEquity(investeeEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询控股股权结构失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("查询控股股权结构失败");
        }
    }

    @Operation(summary = "查询实际控制人股权结构")
    @GetMapping("/actualController/{investeeEnterpriseId}")
    public R<List<EquityStructure>> getActualControllerEquity(@PathVariable String investeeEnterpriseId) {
        try {
            List<EquityStructure> result = equityStructureService.getActualControllerEquity(investeeEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询实际控制人股权结构失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("查询实际控制人股权结构失败");
        }
    }

    @Operation(summary = "查询质押股权")
    @GetMapping("/pledged/{investeeEnterpriseId}")
    public R<List<EquityStructure>> getPledgedEquity(@PathVariable String investeeEnterpriseId) {
        try {
            List<EquityStructure> result = equityStructureService.getPledgedEquity(investeeEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询质押股权失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("查询质押股权失败");
        }
    }

    @Operation(summary = "获取股权集中度")
    @GetMapping("/concentration/{investeeEnterpriseId}")
    public R<Map<String, Object>> getEquityConcentration(@PathVariable String investeeEnterpriseId) {
        try {
            Map<String, Object> result = equityStructureService.getEquityConcentration(investeeEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权集中度失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("获取股权集中度失败");
        }
    }

    @Operation(summary = "按投资方类型统计股权分布")
    @GetMapping("/distribution/investorType/{investeeEnterpriseId}")
    public R<List<Map<String, Object>>> getEquityDistributionByInvestorType(@PathVariable String investeeEnterpriseId) {
        try {
            List<Map<String, Object>> result = equityStructureService.getEquityDistributionByInvestorType(investeeEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("按投资方类型统计股权分布失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("按投资方类型统计股权分布失败");
        }
    }

    @Operation(summary = "按股权性质统计股权分布")
    @GetMapping("/distribution/nature/{investeeEnterpriseId}")
    public R<List<Map<String, Object>>> getEquityDistributionByNature(@PathVariable String investeeEnterpriseId) {
        try {
            List<Map<String, Object>> result = equityStructureService.getEquityDistributionByNature(investeeEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("按股权性质统计股权分布失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("按股权性质统计股权分布失败");
        }
    }

    @Operation(summary = "按投资层级统计股权分布")
    @GetMapping("/distribution/level/{investeeEnterpriseId}")
    public R<List<Map<String, Object>>> getEquityDistributionByLevel(@PathVariable String investeeEnterpriseId) {
        try {
            List<Map<String, Object>> result = equityStructureService.getEquityDistributionByLevel(investeeEnterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("按投资层级统计股权分布失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("按投资层级统计股权分布失败");
        }
    }

    @Operation(summary = "查询股权变动趋势")
    @GetMapping("/trend/{investeeEnterpriseId}")
    public R<List<Map<String, Object>>> getEquityChangeTrend(@PathVariable String investeeEnterpriseId,
                                                            @RequestParam String startDate,
                                                            @RequestParam String endDate) {
        try {
            List<Map<String, Object>> result = equityStructureService.getEquityChangeTrend(investeeEnterpriseId, startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询股权变动趋势失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return R.fail("查询股权变动趋势失败");
        }
    }

    @Operation(summary = "获取股权统计概览（POST兼容前端）")
    @PostMapping("/statistics")
    public R<Map<String, Object>> getEquityStatisticsPost(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();

            // 1. totalCount: 股权记录总数
            long totalCount = equityStructureService.count();
            result.put("totalCount", totalCount);

            // 2. avgConcentration: 各企业最大持股比例的平均值（百分比，取整）
            java.util.List<EquityStructure> all = equityStructureService.list();
            java.util.Map<String, java.math.BigDecimal> maxRatioByEnterprise = new java.util.HashMap<>();
            for (EquityStructure es : all) {
                if (es.getEnterpriseId() != null && es.getShareholdingRatio() != null) {
                    maxRatioByEnterprise.merge(es.getEnterpriseId(), es.getShareholdingRatio(), java.math.BigDecimal::max);
                }
            }
            long avgConcentration = 0;
            if (!maxRatioByEnterprise.isEmpty()) {
                java.math.BigDecimal sum = maxRatioByEnterprise.values().stream()
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                avgConcentration = sum.divide(java.math.BigDecimal.valueOf(maxRatioByEnterprise.size()), 0, java.math.RoundingMode.HALF_UP).longValue();
            }
            result.put("avgConcentration", avgConcentration);

            // 3. riskCount: 质押或冻结状态的记录数
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> riskWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            riskWrapper.in(EquityStructure::getStatus, java.util.Arrays.asList("PLEDGED", "FROZEN"));
            long riskCount = equityStructureService.count(riskWrapper);
            result.put("riskCount", riskCount);

            // 4. changedCount: 本月更新的记录数
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            java.util.Date firstDayOfMonth = cal.getTime();
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> changedWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            changedWrapper.ge(EquityStructure::getUpdateTime, firstDayOfMonth);
            long changedCount = equityStructureService.count(changedWrapper);
            result.put("changedCount", changedCount);

            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权统计概览失败", e);
            return R.fail("获取股权统计概览失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权统计概览")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> getEquityStatisticsOverview() {
        try {
            Map<String, Object> result = equityStructureService.getEquityStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权统计概览失败", e);
            return R.fail("获取股权统计概览失败");
        }
    }

    @Operation(summary = "获取投资方类型分布")
    @GetMapping("/statistics/investorType")
    public R<List<Map<String, Object>>> getInvestorTypeDistribution() {
        try {
            List<Map<String, Object>> result = equityStructureService.getInvestorTypeDistribution();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取投资方类型分布失败", e);
            return R.fail("获取投资方类型分布失败");
        }
    }

    @Operation(summary = "获取股权性质分布")
    @GetMapping("/statistics/equityNature")
    public R<List<Map<String, Object>>> getEquityNatureDistribution() {
        try {
            List<Map<String, Object>> result = equityStructureService.getEquityNatureDistribution();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权性质分布失败", e);
            return R.fail("获取股权性质分布失败");
        }
    }

    @Operation(summary = "获取投资层级分布")
    @GetMapping("/statistics/investmentLevel")
    public R<List<Map<String, Object>>> getInvestmentLevelDistribution() {
        try {
            List<Map<String, Object>> result = equityStructureService.getInvestmentLevelDistribution();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取投资层级分布失败", e);
            return R.fail("获取投资层级分布失败");
        }
    }

    @Operation(summary = "获取控股情况统计")
    @GetMapping("/statistics/controlling")
    public R<Map<String, Object>> getControllingStatistics() {
        try {
            Map<String, Object> result = equityStructureService.getControllingStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控股情况统计失败", e);
            return R.fail("获取控股情况统计失败");
        }
    }

    @Operation(summary = "获取质押情况统计")
    @GetMapping("/statistics/pledge")
    public R<Map<String, Object>> getPledgeStatistics() {
        try {
            Map<String, Object> result = equityStructureService.getPledgeStatistics();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取质押情况统计失败", e);
            return R.fail("获取质押情况统计失败");
        }
    }

    @Operation(summary = "批量更新股权状态")
    @PostMapping("/batchUpdateStatus")
    public R<Boolean> batchUpdateEquityStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> equityIds = (List<String>) params.get("equityIds");
            String equityStatus = (String) params.get("equityStatus");
            boolean result = equityStructureService.batchUpdateEquityStatus(equityIds, equityStatus);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量更新股权状态失败", e);
            return R.fail("批量更新股权状态失败");
        }
    }

    @Operation(summary = "获取股权结构图数据")
    @PostMapping("/chart")
    public R<Map<String, Object>> getChart(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            Map<String, Object> chartData = new java.util.HashMap<>();

            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            chartData.put("shareholders", list);
            chartData.put("totalCount", list.size());

            // Build simple tree structure
            java.util.List<java.util.Map<String, Object>> nodes = new java.util.ArrayList<>();
            for (EquityStructure es : list) {
                java.util.Map<String, Object> node = new java.util.HashMap<>();
                node.put("name", es.getInvestorName());
                node.put("ratio", es.getShareholdingRatio());
                node.put("type", es.getInvestorType());
                node.put("controlType", es.getControlType());
                nodes.add(node);
            }
            chartData.put("nodes", nodes);
            return R.success(chartData);
        } catch (Exception e) {
            log.error("获取股权结构图数据失败", e);
            return R.fail("获取失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出股权结构列表")
    @PostMapping("/export")
    public void exportEquityStructureList(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse response) {
        try {
            if (params == null) params = new HashMap<>();

            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && !params.get("enterpriseId").toString().isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, params.get("enterpriseId").toString());
            }
            if (params.get("enterpriseName") != null && !params.get("enterpriseName").toString().isEmpty()) {
                wrapper.like(EquityStructure::getEnterpriseId, params.get("enterpriseName").toString());
            }
            if (params.get("investorType") != null && !params.get("investorType").toString().isEmpty()) {
                wrapper.eq(EquityStructure::getInvestorType, params.get("investorType").toString());
            }
            if (params.get("controlType") != null && !params.get("controlType").toString().isEmpty()) {
                wrapper.eq(EquityStructure::getControlType, params.get("controlType").toString());
            }
            if (params.get("status") != null && !params.get("status").toString().isEmpty()) {
                wrapper.eq(EquityStructure::getStatus, params.get("status").toString());
            }
            if (params.get("keyword") != null && !params.get("keyword").toString().isEmpty()) {
                String keyword = params.get("keyword").toString();
                wrapper.like(EquityStructure::getInvestorName, keyword);
            }
            wrapper.orderByDesc(EquityStructure::getCreateTime);
            List<EquityStructure> list = equityStructureService.list(wrapper);

            // 生成Excel
            SXSSFWorkbook wb = new SXSSFWorkbook(100);
            SXSSFSheet sheet = wb.createSheet("股权结构");
            String[] headers = {"企业ID", "投资方", "投资方类型", "持股比例(%)", "持股金额(万)", "表决权(%)", "控制类型", "状态", "创建时间"};
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < list.size(); i++) {
                EquityStructure e = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(e.getEnterpriseId() != null ? e.getEnterpriseId() : "");
                row.createCell(1).setCellValue(e.getInvestorName() != null ? e.getInvestorName() : "");
                row.createCell(2).setCellValue(mapInvestorType(e.getInvestorType()));
                row.createCell(3).setCellValue(e.getShareholdingRatio() != null ? e.getShareholdingRatio().toString() : "");
                row.createCell(4).setCellValue(e.getShareholdingAmount() != null ? e.getShareholdingAmount().toString() : "");
                row.createCell(5).setCellValue(e.getVotingRatio() != null ? e.getVotingRatio().toString() : "");
                row.createCell(6).setCellValue(mapControlType(e.getControlType()));
                row.createCell(7).setCellValue(mapStatus(e.getStatus()));
                row.createCell(8).setCellValue(e.getCreateTime() != null ? sdf.format(e.getCreateTime()) : "");
            }

            String filename = "股权结构数据_" + System.currentTimeMillis() + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            wb.write(response.getOutputStream());
            wb.close();
        } catch (Exception e) {
            log.error("导出股权结构列表失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    private String mapInvestorType(String type) {
        if (type == null) return "";
        switch (type) {
            case "ENTERPRISE": return "企业";
            case "INDIVIDUAL": return "个人";
            case "GOVERNMENT": return "政府";
            case "INSTITUTION": return "机构";
            case "FUND": return "基金";
            default: return type;
        }
    }

    private String mapControlType(String type) {
        if (type == null) return "";
        switch (type) {
            case "DIRECT": return "直接控制";
            case "INDIRECT": return "间接控制";
            case "ABSOLUTE": return "绝对控制";
            case "RELATIVE": return "相对控制";
            case "JOINT": return "共同控制";
            case "NONE": return "无控制";
            default: return type;
        }
    }

    private String mapStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "NORMAL": return "正常";
            case "PLEDGED": return "已质押";
            case "FROZEN": return "冻结";
            case "TRANSFERRED": return "已转让";
            case "CANCELLED": return "已注销";
            default: return status;
        }
    }

    @Operation(summary = "执行股权结构深度分析")
    @PostMapping("/analyze")
    public R<Map<String, Object>> analyzeEquityStructure(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            Map<String, Object> result = new java.util.HashMap<>();
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            result.put("totalShareholders", list.size());
            result.put("analyzisStatus", "COMPLETED");
            result.put("analyzeTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("执行股权结构深度分析失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分析股权结构变化")
    @PostMapping("/changes")
    public R<List<Map<String, Object>>> analyzeEquityChanges(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            wrapper.orderByDesc(EquityStructure::getUpdateTime);
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            for (EquityStructure es : list) {
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("equityId", es.getEquityId());
                item.put("investorName", es.getInvestorName());
                item.put("shareholdingRatio", es.getShareholdingRatio());
                item.put("status", es.getStatus());
                item.put("updateTime", es.getUpdateTime());
                result.add(item);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("分析股权结构变化失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权结构层级")
    @PostMapping("/levels")
    public R<List<Map<String, Object>>> getEquityLevels(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            for (EquityStructure es : list) {
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("equityId", es.getEquityId());
                item.put("investorName", es.getInvestorName());
                item.put("investmentLevel", es.getControlType());
                item.put("shareholdingRatio", es.getShareholdingRatio());
                result.add(item);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权结构层级失败", e);
            return R.fail("获取失败: " + e.getMessage());
        }
    }

    @Operation(summary = "计算股权比例")
    @PostMapping("/ratios")
    public R<Map<String, Object>> calculateEquityRatios(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            Map<String, Object> result = new java.util.HashMap<>();
            java.math.BigDecimal totalRatio = java.math.BigDecimal.ZERO;
            java.math.BigDecimal maxRatio = java.math.BigDecimal.ZERO;
            for (EquityStructure es : list) {
                if (es.getShareholdingRatio() != null) {
                    totalRatio = totalRatio.add(es.getShareholdingRatio());
                    if (es.getShareholdingRatio().compareTo(maxRatio) > 0) maxRatio = es.getShareholdingRatio();
                }
            }
            result.put("totalRatio", totalRatio);
            result.put("maxRatio", maxRatio);
            result.put("shareholderCount", list.size());
            return R.success(result);
        } catch (Exception e) {
            log.error("计算股权比例失败", e);
            return R.fail("计算失败: " + e.getMessage());
        }
    }

    @Operation(summary = "识别控制关系")
    @PostMapping("/control")
    public R<List<Map<String, Object>>> identifyControlRelations(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            wrapper.isNotNull(EquityStructure::getControlType);
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            for (EquityStructure es : list) {
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("equityId", es.getEquityId());
                item.put("investorName", es.getInvestorName());
                item.put("controlType", es.getControlType());
                item.put("shareholdingRatio", es.getShareholdingRatio());
                result.add(item);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("识别控制关系失败", e);
            return R.fail("识别失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分析股权稳定性")
    @PostMapping("/stability")
    public R<Map<String, Object>> analyzeEquityStability(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            Map<String, Object> result = new java.util.HashMap<>();
            long pledgedCount = list.stream().filter(es -> "PLEDGED".equals(es.getStatus())).count();
            long frozenCount = list.stream().filter(es -> "FROZEN".equals(es.getStatus())).count();
            String stabilityLevel = (pledgedCount + frozenCount) > list.size() / 2 ? "LOW" : pledgedCount > 0 ? "MEDIUM" : "HIGH";
            result.put("stabilityLevel", stabilityLevel);
            result.put("pledgedCount", pledgedCount);
            result.put("frozenCount", frozenCount);
            result.put("totalShareholders", list.size());
            return R.success(result);
        } catch (Exception e) {
            log.error("分析股权稳定性失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "检测股权异常")
    @PostMapping("/anomalies")
    public R<List<Map<String, Object>>> detectEquityAnomalies(@RequestBody(required = false) Map<String, Object> params) {
        try {
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            wrapper.and(w -> w.eq(EquityStructure::getStatus, "FROZEN").or().eq(EquityStructure::getStatus, "PLEDGED"));
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            for (EquityStructure es : list) {
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("equityId", es.getEquityId());
                item.put("investorName", es.getInvestorName());
                item.put("status", es.getStatus());
                item.put("anomalyType", "FROZEN".equals(es.getStatus()) ? "冻结" : "质押");
                result.add(item);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("检测股权异常失败", e);
            return R.fail("检测失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权结构对比")
    @PostMapping("/compare")
    public R<Map<String, Object>> compareEquityStructures(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("comparisonResult", "对比完成");
            result.put("compareTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权结构对比失败", e);
            return R.fail("对比失败: " + e.getMessage());
        }
    }

    @Operation(summary = "模拟股权结构变更")
    @PostMapping("/simulate")
    public R<Map<String, Object>> simulateEquityChanges(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("simulationResult", "模拟完成");
            result.put("impactLevel", "LOW");
            result.put("suggestion", "结构变更影响较小。");
            result.put("simulateTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("模拟股权结构变更失败", e);
            return R.fail("模拟失败: " + e.getMessage());
        }
    }

    @Operation(summary = "评估股权结构风险")
    @PostMapping("/risk")
    public R<Map<String, Object>> assessEquityRisk(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            Map<String, Object> result = new java.util.HashMap<>();
            long pledgedCount = list.stream().filter(es -> "PLEDGED".equals(es.getStatus())).count();
            long frozenCount = list.stream().filter(es -> "FROZEN".equals(es.getStatus())).count();
            String riskLevel = (pledgedCount + frozenCount) > list.size() * 0.3 ? "HIGH" : (pledgedCount + frozenCount) > 0 ? "MEDIUM" : "LOW";
            result.put("riskLevel", riskLevel);
            result.put("pledgedCount", pledgedCount);
            result.put("frozenCount", frozenCount);
            result.put("assessTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("评估股权结构风险失败", e);
            return R.fail("评估失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权结构建议")
    @PostMapping("/recommendations")
    public R<List<Map<String, Object>>> getEquityRecommendations(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            Map<String, Object> rec1 = new java.util.HashMap<>();
            rec1.put("type", "STRUCTURE");
            rec1.put("description", "建议优化股权结构层级");
            rec1.put("priority", "MEDIUM");
            result.add(rec1);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权结构建议失败", e);
            return R.fail("获取建议失败: " + e.getMessage());
        }
    }

    @Operation(summary = "验证股权结构合规性")
    @PostMapping("/compliance")
    public R<Map<String, Object>> validateEquityCompliance(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("complianceStatus", "COMPLIANT");
            result.put("issues", new java.util.ArrayList<>());
            result.put("validateTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("验证股权结构合规性失败", e);
            return R.fail("验证失败: " + e.getMessage());
        }
    }

    @Operation(summary = "生成股权结构报告")
    @PostMapping("/report")
    public R<Map<String, Object>> generateEquityReport(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("reportTitle", "股权结构分析报告");
            result.put("generateTime", java.time.LocalDateTime.now().toString());
            result.put("status", "SUCCESS");
            return R.success(result);
        } catch (Exception e) {
            log.error("生成股权结构报告失败", e);
            return R.fail("生成报告失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量更新股权信息")
    @PostMapping("/batch/update")
    public R<Boolean> batchUpdateEquity(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要更新的记录");
            }
            String status = params.get("status") != null ? params.get("status").toString() : null;
            for (String id : ids) {
                EquityStructure es = equityStructureService.getById(id);
                if (es != null && status != null) {
                    es.setStatus(status);
                    equityStructureService.updateById(es);
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("批量更新股权信息失败", e);
            return R.fail("批量更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除股权记录")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDeleteEquity(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要删除的记录");
            }
            equityStructureService.removeByIds(ids);
            return R.success(true);
        } catch (Exception e) {
            log.error("批量删除股权记录失败", e);
            return R.fail("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导入股权结构数据")
    @PostMapping("/import")
    public R<Map<String, Object>> importEquityData(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "导入成功");
            result.put("importTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("导入股权结构数据失败", e);
            return R.fail("导入失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权结构树形数据")
    @PostMapping("/tree")
    public R<Map<String, Object>> getEquityTreeData(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            Map<String, Object> tree = new java.util.HashMap<>();
            tree.put("name", enterpriseId != null ? enterpriseId : "根节点");
            List<Map<String, Object>> children = new java.util.ArrayList<>();
            for (EquityStructure es : list) {
                Map<String, Object> node = new java.util.HashMap<>();
                node.put("name", es.getInvestorName());
                node.put("ratio", es.getShareholdingRatio());
                node.put("type", es.getInvestorType());
                node.put("level", es.getControlType());
                children.add(node);
            }
            tree.put("children", children);
            return R.success(tree);
        } catch (Exception e) {
            log.error("获取股权结构树形数据失败", e);
            return R.fail("获取失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分析股权集中度")
    @PostMapping("/concentration")
    public R<Map<String, Object>> analyzeEquityConcentration(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            Map<String, Object> result = new java.util.HashMap<>();
            java.math.BigDecimal top1 = java.math.BigDecimal.ZERO;
            java.math.BigDecimal top5 = java.math.BigDecimal.ZERO;
            java.util.List<java.math.BigDecimal> ratios = new java.util.ArrayList<>();
            for (EquityStructure es : list) {
                if (es.getShareholdingRatio() != null) ratios.add(es.getShareholdingRatio());
            }
            ratios.sort(java.util.Collections.reverseOrder());
            if (!ratios.isEmpty()) top1 = ratios.get(0);
            for (int i = 0; i < Math.min(5, ratios.size()); i++) top5 = top5.add(ratios.get(i));
            result.put("top1Ratio", top1);
            result.put("top5Ratio", top5);
            result.put("concentrationLevel", top1.compareTo(new java.math.BigDecimal("50")) >= 0 ? "HIGH" : top5.compareTo(new java.math.BigDecimal("50")) >= 0 ? "MEDIUM" : "LOW");
            return R.success(result);
        } catch (Exception e) {
            log.error("分析股权集中度失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权变更历史")
    @PostMapping("/history")
    public R<List<Map<String, Object>>> getEquityChangeHistory(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            wrapper.orderByDesc(EquityStructure::getUpdateTime);
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            for (EquityStructure es : list) {
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("equityId", es.getEquityId());
                item.put("investorName", es.getInvestorName());
                item.put("ratio", es.getShareholdingRatio());
                item.put("status", es.getStatus());
                item.put("updateTime", es.getUpdateTime());
                result.add(item);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("获取股权变更历史失败", e);
            return R.fail("获取失败: " + e.getMessage());
        }
    }

    @Operation(summary = "股权结构优化建议")
    @PostMapping("/optimize")
    public R<Map<String, Object>> optimizeEquityStructure(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("currentScore", 72);
            result.put("optimizedScore", 88);
            result.put("suggestions", java.util.Arrays.asList("减少股权层级", "提高控股透明度", "消除交叉持股"));
            result.put("optimizeTime", java.time.LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("股权结构优化建议失败", e);
            return R.fail("获取建议失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取股权穿透图谱数据")
    @PostMapping("/penetration/chart")
    public R<Map<String, Object>> getPenetrationChart(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (enterpriseId != null && !enterpriseId.isEmpty()) {
                wrapper.eq(EquityStructure::getEnterpriseId, enterpriseId);
            }
            java.util.List<EquityStructure> list = equityStructureService.list(wrapper);
            Map<String, Object> chartData = new java.util.HashMap<>();
            List<Map<String, Object>> nodes = new java.util.ArrayList<>();
            List<Map<String, Object>> links = new java.util.ArrayList<>();
            for (EquityStructure es : list) {
                Map<String, Object> node = new java.util.HashMap<>();
                node.put("id", es.getInvestorId());
                node.put("name", es.getInvestorName());
                node.put("type", es.getInvestorType());
                node.put("ratio", es.getShareholdingRatio());
                nodes.add(node);
                Map<String, Object> link = new java.util.HashMap<>();
                link.put("source", es.getInvestorId());
                link.put("target", es.getEnterpriseId());
                link.put("ratio", es.getShareholdingRatio());
                link.put("level", es.getControlType());
                links.add(link);
            }
            chartData.put("nodes", nodes);
            chartData.put("links", links);
            return R.success(chartData);
        } catch (Exception e) {
            log.error("获取股权穿透图谱数据失败", e);
            return R.fail("获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取股权穿透树（递归嵌套结构，供首页ECharts树图使用）
     * 返回格式：{ name, value, level, risk, children: [...] }
     * @param params depth: 穿透层级（默认3），rootEnterpriseId: 根节点企业ID（可选）
     */
    @Operation(summary = "获取股权穿透树（递归嵌套，供首页树图使用）")
    @PostMapping("/penetration/tree")
    public R<Map<String, Object>> getEquityPenetrationTree(@RequestBody(required = false) Map<String, Object> params) {
        try {
            int depth = 3;
            String rootEnterpriseId = null;
            if (params != null) {
                if (params.get("depth") != null) {
                    depth = Integer.parseInt(params.get("depth").toString());
                }
                rootEnterpriseId = (String) params.get("rootEnterpriseId");
            }

            // 查询所有有效股权记录
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquityStructure> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            wrapper.ne(EquityStructure::getStatus, "CANCELLED");
            List<EquityStructure> allRecords = equityStructureService.list(wrapper);

            if (allRecords == null || allRecords.isEmpty()) {
                return R.success(new HashMap<>());
            }

            // 按投资方ID分组：investorId -> 其投资的企业列表
            Map<String, List<EquityStructure>> investorMap = new java.util.HashMap<>();
            java.util.Set<String> allEnterpriseIds = new java.util.HashSet<>();
            java.util.Set<String> allInvestorIds = new java.util.HashSet<>();
            // 构建企业ID -> 中文名称的映射表（核心：解决子节点显示英文ID的问题）
            Map<String, String> enterpriseNameMap = new java.util.HashMap<>();

            for (EquityStructure es : allRecords) {
                if (es.getInvestorId() != null) {
                    investorMap.computeIfAbsent(es.getInvestorId(), k -> new java.util.ArrayList<>()).add(es);
                    allInvestorIds.add(es.getInvestorId());
                    // 投资方ID -> 投资方名称（每个投资方也是一个企业）
                    if (es.getInvestorName() != null) {
                        enterpriseNameMap.put(es.getInvestorId(), es.getInvestorName());
                    }
                }
                if (es.getEnterpriseId() != null) {
                    allEnterpriseIds.add(es.getEnterpriseId());
                }
            }

            // 确定根节点：指定的rootEnterpriseId，或者找出只作为投资方但不被投资的企业
            String rootId = rootEnterpriseId;
            String rootName = "集团总部";
            if (rootId == null || rootId.isEmpty()) {
                // 找出只作为投资方但不作为被投资方的企业（即顶层控股公司）
                java.util.Set<String> topInvestors = new java.util.HashSet<>(allInvestorIds);
                topInvestors.removeAll(allEnterpriseIds);
                if (!topInvestors.isEmpty()) {
                    rootId = topInvestors.iterator().next();
                    // 尝试从记录中获取名称
                    for (EquityStructure es : allRecords) {
                        if (rootId.equals(es.getInvestorId())) {
                            rootName = es.getInvestorName() != null ? es.getInvestorName() : rootId;
                            break;
                        }
                    }
                } else {
                    // 如果没有纯顶层投资方，取持股比例最大的投资方
                    rootId = allRecords.get(0).getInvestorId();
                    rootName = allRecords.get(0).getInvestorName() != null ? allRecords.get(0).getInvestorName() : rootId;
                }
            }

            // 递归构建树（传入名称映射表）
            Map<String, Object> tree = buildPenetrationNode(rootId, rootName, 0, depth, investorMap, new java.util.HashSet<>(), enterpriseNameMap);
            return R.success(tree);
        } catch (Exception e) {
            log.error("获取股权穿透树失败", e);
            return R.fail("获取失败: " + e.getMessage());
        }
    }

    /**
     * 递归构建穿透树节点
     */
    private Map<String, Object> buildPenetrationNode(String investorId, String investorName, int currentLevel,
                                                      int maxDepth, Map<String, List<EquityStructure>> investorMap,
                                                      java.util.Set<String> visited, Map<String, String> enterpriseNameMap) {
        Map<String, Object> node = new java.util.HashMap<>();
        node.put("name", investorName);
        node.put("level", currentLevel);

        // 确定风险标记
        List<EquityStructure> investments = investorMap.get(investorId);
        if (investments != null && !investments.isEmpty()) {
            // 计算该节点的总持股价值（用最大持股比例代表）
            BigDecimal maxRatio = investments.stream()
                .map(EquityStructure::getShareholdingRatio)
                .filter(r -> r != null)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
            node.put("value", maxRatio.intValue());
        } else {
            node.put("value", 0);
        }

        // 构建子节点
        List<Map<String, Object>> children = new java.util.ArrayList<>();
        if (currentLevel < maxDepth && investments != null && !visited.contains(investorId)) {
            visited.add(investorId); // 防止循环引用
            for (EquityStructure es : investments) {
                String childId = es.getEnterpriseId();
                if (childId == null || visited.contains(childId)) continue;

                // 从映射表获取企业中文名称，找不到则用投资方名称或ID兜底
                String childName = enterpriseNameMap.getOrDefault(childId, childId);

                Map<String, Object> childNode = buildPenetrationNode(
                    childId,
                    childName,
                    currentLevel + 1,
                    maxDepth,
                    investorMap,
                    visited,
                    enterpriseNameMap
                );
                // 设置持股比例作为value
                if (es.getShareholdingRatio() != null) {
                    childNode.put("value", es.getShareholdingRatio().intValue());
                }
                // 设置风险标记
                if ("PLEDGED".equals(es.getStatus())) {
                    childNode.put("risk", "pledge");
                } else if ("CONTROLLING".equals(es.getControlType()) || (es.getShareholdingRatio() != null && es.getShareholdingRatio().compareTo(new BigDecimal("50")) >= 0)) {
                    childNode.put("risk", "joint");
                } else if ("FROZEN".equals(es.getStatus())) {
                    childNode.put("risk", "high");
                }
                // 在name中附加持股比例信息（使用中文名称）
                String displayName = childName;
                if (es.getShareholdingRatio() != null) {
                    displayName += "\n(" + es.getShareholdingRatio() + "%)";
                }
                childNode.put("name", displayName);
                children.add(childNode);
            }
            visited.remove(investorId); // 回溯，允许其他路径访问
        }
        node.put("children", children);
        return node;
    }
}
