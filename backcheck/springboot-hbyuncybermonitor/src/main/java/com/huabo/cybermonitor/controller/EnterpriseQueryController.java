package com.huabo.cybermonitor.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.EnterpriseInfo;
import com.huabo.cybermonitor.service.IEnterpriseQueryService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.EnterpriseQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 企业查询管理 Controller
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="企业查询管理",description="企业查询管理")
@RestController
@RequestMapping("/v1/enterprise/query")
public class EnterpriseQueryController {

	private static final Logger log = LoggerFactory.getLogger(EnterpriseQueryController.class);

    @Autowired
    private IEnterpriseQueryService enterpriseQueryService;

    @Operation(summary = "高级查询企业信息列表")
    @PostMapping("/advanced")
    public R<PageResult<EnterpriseInfo>> getEnterpriseAdvancedQuery(@RequestBody EnterpriseQueryVO queryVO) {
        try {
            IPage<EnterpriseInfo> page = enterpriseQueryService.getEnterpriseAdvancedQuery(queryVO);
            PageResult<EnterpriseInfo> pageResult = new PageResult<>();
            pageResult.setTlist(page.getRecords());
            pageResult.setTotalRecord((int) page.getTotal());
            pageResult.setPageNumber((int) page.getCurrent());
            pageResult.setPageSize((int) page.getSize());
            
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("高级查询企业信息列表失败", e);
            return R.fail("高级查询企业信息列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业综合查询（支持模糊搜索）")
    @PostMapping("/comprehensive")
    public R<PageResult<EnterpriseInfo>> getEnterpriseComprehensiveQuery(@RequestBody EnterpriseQueryVO queryVO) {
        try {
            IPage<EnterpriseInfo> page = enterpriseQueryService.getEnterpriseComprehensiveQuery(queryVO);
            PageResult<EnterpriseInfo> pageResult = new PageResult<>();
            pageResult.setTlist(page.getRecords());
            pageResult.setTotalRecord((int) page.getTotal());
            pageResult.setPageNumber((int) page.getCurrent());
            pageResult.setPageSize((int) page.getSize());
            
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("企业综合查询失败", e);
            return R.fail("企业综合查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据关键词搜索企业")
    @PostMapping("/search")
    public R<List<EnterpriseInfo>> searchEnterprisesByKeyword(@RequestBody Map<String, String> params) {
        try {
            String keyword = params.get("keyword");
            if (keyword == null || keyword.trim().isEmpty()) {
                return R.fail("关键词不能为空");
            }
            
            List<EnterpriseInfo> enterprises = enterpriseQueryService.searchEnterprisesByKeyword(keyword);
            return R.success(enterprises);
        } catch (Exception e) {
            log.error("根据关键词搜索企业失败", e);
            return R.fail("根据关键词搜索企业失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业基础统计分析")
    @PostMapping("/statistics/basic")
    public R<Map<String, Object>> getEnterpriseBasicStatistics() {
        try {
            Map<String, Object> statistics = enterpriseQueryService.getEnterpriseBasicStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("企业基础统计分析失败", e);
            return R.fail("企业基础统计分析失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业类型分布统计")
    @PostMapping("/statistics/type-distribution")
    public R<List<Map<String, Object>>> getEnterpriseTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseQueryService.getEnterpriseTypeDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("企业类型分布统计失败", e);
            return R.fail("企业类型分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业地区分布统计")
    @PostMapping("/statistics/region-distribution")
    public R<List<Map<String, Object>>> getEnterpriseRegionDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseQueryService.getEnterpriseRegionDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("企业地区分布统计失败", e);
            return R.fail("企业地区分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业行业分布统计")
    @PostMapping("/statistics/industry-distribution")
    public R<List<Map<String, Object>>> getEnterpriseIndustryDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseQueryService.getEnterpriseIndustryDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("企业行业分布统计失败", e);
            return R.fail("企业行业分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业状态分布统计")
    @PostMapping("/statistics/status-distribution")
    public R<List<Map<String, Object>>> getEnterpriseStatusDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseQueryService.getEnterpriseStatusDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("企业状态分布统计失败", e);
            return R.fail("企业状态分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业监管层级分布统计")
    @PostMapping("/statistics/supervision-level-distribution")
    public R<List<Map<String, Object>>> getSupervisionLevelDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseQueryService.getSupervisionLevelDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("企业监管层级分布统计失败", e);
            return R.fail("企业监管层级分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业注册资本分布统计")
    @PostMapping("/statistics/capital-distribution")
    public R<List<Map<String, Object>>> getRegisteredCapitalDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseQueryService.getRegisteredCapitalDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("企业注册资本分布统计失败", e);
            return R.fail("企业注册资本分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业成立时间分布统计")
    @PostMapping("/statistics/establish-date-distribution")
    public R<List<Map<String, Object>>> getEstablishDateDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseQueryService.getEstablishDateDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("企业成立时间分布统计失败", e);
            return R.fail("企业成立时间分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业上市状态分布统计")
    @PostMapping("/statistics/listing-status-distribution")
    public R<List<Map<String, Object>>> getListingStatusDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseQueryService.getListingStatusDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("企业上市状态分布统计失败", e);
            return R.fail("企业上市状态分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业风险等级分布统计")
    @PostMapping("/statistics/risk-level-distribution")
    public R<List<Map<String, Object>>> getRiskLevelDistribution() {
        try {
            List<Map<String, Object>> distribution = enterpriseQueryService.getRiskLevelDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("企业风险等级分布统计失败", e);
            return R.fail("企业风险等级分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "国有控股企业统计")
    @PostMapping("/statistics/state-controlled")
    public R<Map<String, Object>> getStateControlledStatistics() {
        try {
            Map<String, Object> statistics = enterpriseQueryService.getStateControlledStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("国有控股企业统计失败", e);
            return R.fail("国有控股企业统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "上市公司统计")
    @PostMapping("/statistics/listed-company")
    public R<Map<String, Object>> getListedCompanyStatistics() {
        try {
            Map<String, Object> statistics = enterpriseQueryService.getListedCompanyStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("上市公司统计失败", e);
            return R.fail("上市公司统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业规模分析（按注册资本）")
    @PostMapping("/analysis/scale")
    public R<List<Map<String, Object>>> getEnterpriseScaleAnalysis() {
        try {
            List<Map<String, Object>> analysis = enterpriseQueryService.getEnterpriseScaleAnalysis();
            return R.success(analysis);
        } catch (Exception e) {
            log.error("企业规模分析失败", e);
            return R.fail("企业规模分析失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业发展趋势分析（按成立时间）")
    @PostMapping("/analysis/development-trend")
    public R<List<Map<String, Object>>> getEnterpriseDevelopmentTrend() {
        try {
            List<Map<String, Object>> trend = enterpriseQueryService.getEnterpriseDevelopmentTrend();
            return R.success(trend);
        } catch (Exception e) {
            log.error("企业发展趋势分析失败", e);
            return R.fail("企业发展趋势分析失败：" + e.getMessage());
        }
    }

    @Operation(summary = "重点监管企业列表")
    @PostMapping("/key-supervision")
    public R<List<EnterpriseInfo>> getKeySupervisionEnterprises() {
        try {
            List<EnterpriseInfo> enterprises = enterpriseQueryService.getKeySupervisionEnterprises();
            return R.success(enterprises);
        } catch (Exception e) {
            log.error("获取重点监管企业列表失败", e);
            return R.fail("获取重点监管企业列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "高风险企业列表")
    @PostMapping("/high-risk")
    public R<List<EnterpriseInfo>> getHighRiskEnterprises() {
        try {
            List<EnterpriseInfo> enterprises = enterpriseQueryService.getHighRiskEnterprises();
            return R.success(enterprises);
        } catch (Exception e) {
            log.error("获取高风险企业列表失败", e);
            return R.fail("获取高风险企业列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新成立企业列表（最近一年）")
    @PostMapping("/newly-established")
    public R<List<EnterpriseInfo>> getNewlyEstablishedEnterprises() {
        try {
            List<EnterpriseInfo> enterprises = enterpriseQueryService.getNewlyEstablishedEnterprises();
            return R.success(enterprises);
        } catch (Exception e) {
            log.error("获取新成立企业列表失败", e);
            return R.fail("获取新成立企业列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "大型企业列表（按注册资本）")
    @PostMapping("/large-enterprises")
    public R<List<EnterpriseInfo>> getLargeEnterprises(@RequestBody Map<String, Double> params) {
        try {
            Double threshold = params.get("threshold");
            if (threshold == null || threshold <= 0) {
                threshold = 100000000.0; // 默认1亿元
            }
            
            List<EnterpriseInfo> enterprises = enterpriseQueryService.getLargeEnterprises(threshold);
            return R.success(enterprises);
        } catch (Exception e) {
            log.error("获取大型企业列表失败", e);
            return R.fail("获取大型企业列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业综合分析报告")
    @PostMapping("/analysis/comprehensive")
    public R<Map<String, Object>> getEnterpriseComprehensiveAnalysis() {
        try {
            Map<String, Object> analysis = enterpriseQueryService.getEnterpriseComprehensiveAnalysis();
            return R.success(analysis);
        } catch (Exception e) {
            log.error("获取企业综合分析报告失败", e);
            return R.fail("获取企业综合分析报告失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业监管概览")
    @PostMapping("/analysis/supervision-overview")
    public R<Map<String, Object>> getEnterpriseSupervisionOverview() {
        try {
            Map<String, Object> overview = enterpriseQueryService.getEnterpriseSupervisionOverview();
            return R.success(overview);
        } catch (Exception e) {
            log.error("获取企业监管概览失败", e);
            return R.fail("获取企业监管概览失败：" + e.getMessage());
        }
    }

    @Operation(summary = "企业风险分析")
    @PostMapping("/analysis/risk")
    public R<Map<String, Object>> getEnterpriseRiskAnalysis() {
        try {
            Map<String, Object> riskAnalysis = enterpriseQueryService.getEnterpriseRiskAnalysis();
            return R.success(riskAnalysis);
        } catch (Exception e) {
            log.error("获取企业风险分析失败", e);
            return R.fail("获取企业风险分析失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出企业查询结果")
    @PostMapping("/export")
    public void exportEnterpriseQuery(@RequestBody EnterpriseQueryVO queryVO, HttpServletResponse response) {
        try {
            enterpriseQueryService.exportEnterpriseQuery(queryVO, response);
        } catch (Exception e) {
            log.error("导出企业查询结果失败", e);
            throw new RuntimeException("导出企业查询结果失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出企业统计报表")
    @PostMapping("/export-statistics")
    public void exportEnterpriseStatistics(@RequestBody Map<String, String> params, HttpServletResponse response) {
        try {
            String reportType = params.get("reportType");
            if (reportType == null || reportType.trim().isEmpty()) {
                reportType = "type"; // 默认类型分布
            }
            
            enterpriseQueryService.exportEnterpriseStatistics(reportType, response);
        } catch (Exception e) {
            log.error("导出企业统计报表失败", e);
            throw new RuntimeException("导出企业统计报表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "生成企业分析报告")
    @PostMapping("/generate-report")
    public void generateEnterpriseAnalysisReport(@RequestBody Map<String, String> params, HttpServletResponse response) {
        try {
            String reportType = params.get("reportType");
            String format = params.get("format");
            
            if (reportType == null || reportType.trim().isEmpty()) {
                reportType = "comprehensive"; // 默认综合分析
            }
            if (format == null || format.trim().isEmpty()) {
                format = "excel"; // 默认Excel格式
            }
            
            enterpriseQueryService.generateEnterpriseAnalysisReport(reportType, format, response);
        } catch (Exception e) {
            log.error("生成企业分析报告失败", e);
            throw new RuntimeException("生成企业分析报告失败：" + e.getMessage());
        }
    }
}
