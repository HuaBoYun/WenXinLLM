package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.global.treasurer.entity.*;
import com.global.treasurer.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Claude
 * @date 2026-01-20
 * @description 监管分析管理Controller
 */
@RestController
@RequestMapping("/regulatory-analysis")
@Api(tags = "监管分析管理")
public class RegulatoryAnalysisController {

    private static final Logger log = LoggerFactory.getLogger(RegulatoryAnalysisController.class);

    @Resource
    private TblRegulatoryAnalysisService tblRegulatoryAnalysisService;

    /**
     * 分页查询监管分析
     */
    @GetMapping("/page")
    @ApiOperation("分页查询监管分析")
    public String getAnalysisPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("分析名称") @RequestParam(required = false) String analysisName,
            @ApiParam("分析类型") @RequestParam(required = false) String analysisType,
            @ApiParam("合规状态") @RequestParam(required = false) String complianceStatus,
            HttpServletResponse response) throws IOException {

        try {
            PageInfo<TblRegulatoryAnalysis> pageInfo = tblRegulatoryAnalysisService.getAnalysisPage(pageNo, pageSize, analysisName, analysisType, complianceStatus, null, null);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询监管分析分页数据失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询监管分析
     */
    @GetMapping("/{analysisId}")
    @ApiOperation("根据ID查询监管分析")
    public String getAnalysisById(
            @ApiParam("分析ID") @PathVariable String analysisId,
            HttpServletResponse response) throws IOException {

        try {
            TblRegulatoryAnalysis analysis = tblRegulatoryAnalysisService.getAnalysisById(analysisId);
            return JsonBean.success(analysis);
        } catch (Exception e) {
            log.error("查询监管分析详情失败, analysisId={}", analysisId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建监管分析
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("创建监管分析")
    public String createAnalysis(
            @ApiParam("监管分析信息") @FlexibleRequestBody TblRegulatoryAnalysis analysis,
            HttpServletResponse response) throws IOException {

        try {
            TblRegulatoryAnalysis saved = tblRegulatoryAnalysisService.saveAnalysis(analysis);
            return JsonBean.success("创建成功", saved);
        } catch (Exception e) {
            log.error("创建监管分析失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新监管分析
     */
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("更新监管分析")
    public String updateAnalysis(
            @ApiParam("监管分析信息") @FlexibleRequestBody TblRegulatoryAnalysis analysis,
            HttpServletResponse response) throws IOException {

        try {
            tblRegulatoryAnalysisService.updateAnalysis(analysis);
            return JsonBean.success("更新成功");
        } catch (Exception e) {
            log.error("更新监管分析失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除监管分析
     */
    @DeleteMapping("/{analysisId}")
    @ApiOperation("删除监管分析")
    public String deleteAnalysis(
            @ApiParam("分析ID") @PathVariable String analysisId,
            HttpServletResponse response) throws IOException {

        try {
            tblRegulatoryAnalysisService.deleteAnalysis(analysisId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除监管分析失败, analysisId={}", analysisId, e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 执行监管分析
     */
    @PostMapping("/execute")
    @ApiOperation("执行监管分析")
    public String executeAnalysis(
            @ApiParam("分析类型") @RequestParam String analysisType,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate,
            HttpServletResponse response) throws IOException {

        try {
            TblRegulatoryAnalysis analysis = tblRegulatoryAnalysisService.executeAnalysis(analysisType, startDate, endDate);
            return JsonBean.success("执行成功", analysis);
        } catch (Exception e) {
            log.error("执行监管分析失败", e);
            return new JsonBean(0, "执行失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 生成监管报告
     */
    @PostMapping("/{analysisId}/generate-report")
    @ApiOperation("生成监管报告")
    public String generateReport(
            @ApiParam("分析ID") @PathVariable String analysisId,
            @ApiParam("报告格式") @RequestParam(defaultValue = "PDF") String reportFormat,
            HttpServletResponse response) throws IOException {

        try {
            byte[] reportData = tblRegulatoryAnalysisService.generateReport(analysisId, reportFormat);
            return JsonBean.success("生成成功");
        } catch (Exception e) {
            log.error("生成监管报告失败, analysisId={}", analysisId, e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取合规统计
     */
    @GetMapping("/compliance-statistics")
    @ApiOperation("获取合规统计")
    public String getComplianceStatistics(HttpServletResponse response) throws IOException {

        try {
            Map<String, Object> statistics = tblRegulatoryAnalysisService.getComplianceStatistics();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("查询合规统计失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取风险等级分布
     */
    @GetMapping("/risk-distribution")
    @ApiOperation("获取风险等级分布")
    public String getRiskDistribution(HttpServletResponse response) throws IOException {

        try {
            Map<String, Object> distribution = tblRegulatoryAnalysisService.getRiskLevelDistribution();
            return JsonBean.success(distribution);
        } catch (Exception e) {
            log.error("查询风险等级分布失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取分析类型列表
     */
    @GetMapping("/types")
    @ApiOperation("获取分析类型列表")
    public String getAnalysisTypes(HttpServletResponse response) throws IOException {

        try {
            List<Map<String, Object>> types = tblRegulatoryAnalysisService.getAnalysisTypes();
            return JsonBean.success(types);
        } catch (Exception e) {
            log.error("查询分析类型列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}

