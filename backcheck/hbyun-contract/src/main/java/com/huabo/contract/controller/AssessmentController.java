package com.huabo.contract.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.AssessmentIndicator;
import com.huabo.contract.entity.ProjectAssessment;
import com.huabo.contract.service.AssessmentService;
import com.huabo.contract.vo.AssessmentQueryParam;
import com.huabo.contract.vo.AssessmentRequest;
import com.huabo.contract.vo.AssessmentReviewRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目考核管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@RestController
@RequestMapping("/assessment")
@Tag(name="项目考核管理",description="项目考核管理")
@Validated
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    /**
     * 启动项目考核
     */
    @PostMapping("/start")
    @Operation(summary = "启动项目考核", description = "启动项目考核流程，支持定期考核和临时考核")
    public String startAssessment(@Valid @RequestBody AssessmentRequest request) {
        try {
            log.info("启动项目考核，参数：{}", request);

            ProjectAssessment result = assessmentService.startAssessment(request);

            return JsonBean.success("考核启动成功", result);
        } catch (Exception e) {
            log.error("启动项目考核失败", e);
            return JsonBean.error("考核启动失败：" + e.getMessage());
        }
    }

    /**
     * 查询考核结果列表
     */
    @PostMapping("/list")
    @Operation(summary = "查询考核结果列表", description = "分页查询项目考核结果列表")
    public String getAssessmentList(@RequestBody AssessmentQueryParam param) {
        try {
            log.info("查询考核结果列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNum() == null || param.getPageNum() <= 0) {
                param.setPageNum(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<ProjectAssessment> pageInfo = assessmentService.getAssessmentList(param);

            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询考核结果列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询指定项目的考核结果
     */
    @GetMapping("/result/{projectId}")
    @Operation(summary = "查询考核结果", description = "查询指定项目的考核结果列表")
    public String getAssessmentResults(
            @Parameter(description = "项目ID", required = true) @PathVariable String projectId,
            @Parameter(description = "考核期间") @RequestParam(required = false) String period) {
        try {
            log.info("查询项目考核结果，项目ID：{}，期间：{}", projectId, period);

            List<ProjectAssessment> results = assessmentService.getAssessmentResultsByProject(projectId, period);

            return JsonBean.success("查询成功", results);
        } catch (Exception e) {
            log.error("查询项目考核结果失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取考核详情
     */
    @GetMapping("/detail/{assessmentId}")
    @Operation(summary = "获取考核详情", description = "获取指定考核记录的详细信息")
    public String getAssessmentDetail(
            @Parameter(description = "考核记录ID", required = true) @PathVariable Long assessmentId) {
        try {
            log.info("获取考核详情，考核ID：{}", assessmentId);

            Map<String, Object> detail = assessmentService.getAssessmentDetail(assessmentId);

            return JsonBean.success("查询成功", detail);
        } catch (Exception e) {
            log.error("获取考核详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 生成考核报告
     */
    @PostMapping("/report/{assessmentId}")
    @Operation(summary = "生成考核报告", description = "生成指定考核记录的详细报告")
    public String generateReport(
            @Parameter(description = "考核记录ID", required = true) @PathVariable Long assessmentId,
            @RequestParam(defaultValue = "detailed") String reportType,
            @RequestParam(defaultValue = "true") Boolean includeChart,
            @RequestParam(defaultValue = "true") Boolean includeSuggestions) {
        try {
            log.info("生成考核报告，考核ID：{}，报告类型：{}", assessmentId, reportType);

            Map<String, Object> report = assessmentService.generateAssessmentReport(
                    assessmentId, reportType, includeChart, includeSuggestions);

            return JsonBean.success("报告生成成功", report);
        } catch (Exception e) {
            log.error("生成考核报告失败", e);
            return JsonBean.error("报告生成失败：" + e.getMessage());
        }
    }

    /**
     * 审核考核结果
     */
    @PutMapping("/review/{assessmentId}")
    @Operation(summary = "审核考核结果", description = "审核考核结果，确认或驳回")
    public String reviewAssessment(
            @Parameter(description = "考核记录ID", required = true) @PathVariable Long assessmentId,
            @Valid @RequestBody AssessmentReviewRequest request) {
        try {
            log.info("审核考核结果，考核ID：{}，审核结果：{}", assessmentId, request.getReviewResult());

            ProjectAssessment result = assessmentService.reviewAssessment(assessmentId, request);

            return JsonBean.success("审核完成", result);
        } catch (Exception e) {
            log.error("审核考核结果失败", e);
            return JsonBean.error("审核失败：" + e.getMessage());
        }
    }

    /**
     * 获取指标体系
     */
    @GetMapping("/indicators")
    @Operation(summary = "获取指标体系", description = "获取考核指标体系配置")
    public String getIndicators(
            @RequestParam(defaultValue = "true") Boolean isActive,
            @RequestParam(required = false) Long parentId) {
        try {
            log.info("获取指标体系，是否启用：{}，父指标ID：{}", isActive, parentId);

            List<AssessmentIndicator> indicators = assessmentService.getIndicators(isActive, parentId);

            return JsonBean.success("查询成功", indicators);
        } catch (Exception e) {
            log.error("获取指标体系失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取指标树
     */
    @PostMapping("/indicator/tree")
    @Operation(summary = "获取指标树", description = "获取层级化的考核指标树")
    public String getIndicatorTree() {
        try {
            log.info("获取指标树");
            List<AssessmentIndicator> tree = assessmentService.getIndicatorTree();
            return JsonBean.success("查询成功", tree);
        } catch (Exception e) {
            log.error("获取指标树失败", e);
            return JsonBean.error("获取指标树失败：" + e.getMessage());
        }
    }

    /**
     * 保存指标配置
     */
    @PostMapping("/indicator/save")
    @Operation(summary = "保存指标配置", description = "新增或更新考核指标配置")
    public String saveIndicator(@Valid @RequestBody AssessmentIndicator indicator) {
        try {
            log.info("保存指标配置，指标编码：{}", indicator.getIndicatorCode());

            assessmentService.saveIndicator(indicator);

            return JsonBean.success("保存成功");
        } catch (Exception e) {
            log.error("保存指标配置失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 更新指标
     */
    @PostMapping("/indicator/update")
    @Operation(summary = "更新指标", description = "更新考核指标信息")
    public String updateIndicator(@Valid @RequestBody AssessmentIndicator indicator) {
        try {
            log.info("更新指标，参数：{}", indicator);
            assessmentService.updateIndicator(indicator);
            return JsonBean.success("指标更新成功");
        } catch (Exception e) {
            log.error("更新指标失败", e);
            return JsonBean.error("更新指标失败：" + e.getMessage());
        }
    }

    /**
     * 删除指标
     */
    @PostMapping("/indicator/delete/{indicatorId}")
    @Operation(summary = "删除指标", description = "删除考核指标（逻辑删除）")
    public String deleteIndicator(
            @Parameter(description = "指标ID", required = true) @PathVariable Long indicatorId) {
        try {
            log.info("删除指标，指标ID：{}", indicatorId);

            assessmentService.deleteIndicator(indicatorId);

            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除指标失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 更新指标状态
     */
    @PostMapping("/indicator/status")
    @Operation(summary = "更新指标状态", description = "启用或禁用考核指标")
    public String updateIndicatorStatus(@RequestBody Map<String, Object> params) {
        try {
            Long id = Long.valueOf(params.get("id").toString());
            Integer status = Integer.valueOf(params.get("status").toString());
            log.info("更新指标状态，ID：{}，状态：{}", id, status);
            assessmentService.updateIndicatorStatus(id, status);
            return JsonBean.success("状态更新成功");
        } catch (Exception e) {
            log.error("更新指标状态失败", e);
            return JsonBean.error("更新指标状态失败：" + e.getMessage());
        }
    }

    /**
     * 考核统计概览
     */
    @GetMapping("/statistics/overview")
    @Operation(summary = "考核统计概览", description = "获取考核统计概览数据")
    public String getStatisticsOverview(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long deptId) {
        try {
            log.info("获取考核统计概览，开始日期：{}，结束日期：{}，部门ID：{}", startDate, endDate, deptId);

            Map<String, Object> statistics = assessmentService.getAssessmentStatistics(startDate, endDate, deptId);

            return JsonBean.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取考核统计概览失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 项目考核排名
     */
    @GetMapping("/statistics/ranking")
    @Operation(summary = "项目考核排名", description = "获取项目考核排名列表")
    public String getAssessmentRanking(
            @Parameter(description = "考核期间", required = true) @RequestParam String period,
            @RequestParam(required = false) Integer assessmentType,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("获取项目考核排名，期间：{}，类型：{}，数量：{}", period, assessmentType, limit);

            List<Map<String, Object>> ranking = assessmentService.getAssessmentRanking(period, assessmentType, limit);

            return JsonBean.success("查询成功", ranking);
        } catch (Exception e) {
            log.error("获取项目考核排名失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 指标分析报告
     */
    @GetMapping("/statistics/indicators")
    @Operation(summary = "指标分析报告", description = "获取各指标的统计分析数据")
    public String getIndicatorAnalysis(
            @Parameter(description = "考核期间", required = true) @RequestParam String period,
            @RequestParam(required = false) Long indicatorId) {
        try {
            log.info("获取指标分析报告，期间：{}，指标ID：{}", period, indicatorId);

            List<Map<String, Object>> analysis = assessmentService.getIndicatorAnalysis(period, indicatorId);

            return JsonBean.success("查询成功", analysis);
        } catch (Exception e) {
            log.error("获取指标分析报告失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 同步考核数据
     */
    @PostMapping("/sync/batch")
    @Operation(summary = "批量数据同步", description = "批量同步所有模块数据")
    public String syncAssessmentData(
            @RequestParam String projectId,
            @RequestParam String period,
            @RequestParam(defaultValue = "cost,quality,safety,progress") String modules,
            @RequestParam(defaultValue = "false") Boolean forceSync) {
        try {
            log.info("同步考核数据，项目ID：{}，期间：{}，模块：{}", projectId, period, modules);

            List<String> moduleList = Arrays.asList(modules.split(","));
            Map<String, Object> result = assessmentService.syncAssessmentData(projectId, period, moduleList, forceSync);

            return JsonBean.success("同步成功", result);
        } catch (Exception e) {
            log.error("同步考核数据失败", e);
            return JsonBean.error("同步失败：" + e.getMessage());
        }
    }

    /**
     * 导出考核结果
     */
    @PostMapping("/export/results")
    @Operation(summary = "导出考核结果", description = "导出考核结果数据")
    public String exportAssessmentResults(
            @RequestParam(required = false) String projectIds,
            @Parameter(description = "开始日期", required = true) @RequestParam String startDate,
            @Parameter(description = "结束日期", required = true) @RequestParam String endDate,
            @RequestParam(defaultValue = "excel") String exportFormat,
            @RequestParam(defaultValue = "false") Boolean includeDetails) {
        try {
            log.info("导出考核结果，项目IDs：{}，开始日期：{}，结束日期：{}", projectIds, startDate, endDate);

            List<String> projectIdList = null;
            if (StringUtils.hasText(projectIds)) {
                projectIdList = Arrays.asList(projectIds.split(","));
            }

            Map<String, Object> result = assessmentService.exportAssessmentResults(
                    projectIdList, startDate, endDate, exportFormat, includeDetails);

            return JsonBean.success("导出成功", result);
        } catch (Exception e) {
            log.error("导出考核结果失败", e);
            return JsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 下载导出文件
     */
    @GetMapping("/download/{fileId}")
    @Operation(summary = "下载导出文件", description = "下载导出的文件")
    public String downloadExportFile(
            @Parameter(description = "文件ID", required = true) @PathVariable String fileId) {
        try {
            log.info("下载导出文件，文件ID：{}", fileId);

            // TODO: 实际项目中需要实现文件下载逻辑
            // 这里返回文件信息
            Map<String, Object> fileInfo = new HashMap<>();
            fileInfo.put("fileId", fileId);
            fileInfo.put("fileName", "项目考核结果.xlsx");
            fileInfo.put("downloadUrl", "/files/download/" + fileId);

            return JsonBean.success("文件信息获取成功", fileInfo);
        } catch (Exception e) {
            log.error("下载导出文件失败", e);
            return JsonBean.error("下载失败：" + e.getMessage());
        }
    }

}
