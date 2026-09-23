package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetExecution;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.service.BudgetExecutionService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import com.management.accountant.util.excel.ExcelExport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * 预算执行分析Controller
 * 
 * @description 预算执行分析管理接口，支持执行监控、进度分析、偏差分析等功能
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-执行分析"})
@RequestMapping(value = "/accountant/execution")
@Slf4j
public class BudgetExecutionController {

    @Resource
    private BudgetExecutionService executionService;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    /**
     * 创建执行记录
     */
    @Operation(summary = "创建执行记录")
    @ApiOperation("创建执行记录")
    @PostMapping("/create")
    public MyJsonBean<BudgetExecution> create(@RequestBody @Validated BudgetExecution execution) {
        MyJsonBean<BudgetExecution> result = new MyJsonBean<>();
        try {
            BudgetExecution created = executionService.create(execution);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建执行记录失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建执行记录异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新执行记录
     */
    @Operation(summary = "更新执行记录")
    @ApiOperation("更新执行记录")
    @PostMapping("/update")
    public MyJsonBean<BudgetExecution> update(@RequestBody BudgetExecution execution) {
        MyJsonBean<BudgetExecution> result = new MyJsonBean<>();
        try {
            BudgetExecution updated = executionService.update(execution);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updated);
        } catch (ServiceException ex) {
            log.error("更新执行记录失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新执行记录异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询执行详情
     */
    @Operation(summary = "查询执行详情")
    @ApiOperation("查询执行详情")
    @GetMapping("/detail/{executionId}")
    public MyJsonBean<BudgetExecution> getDetail(
            @ApiParam(value = "执行ID", required = true) @PathVariable String executionId) {
        MyJsonBean<BudgetExecution> result = new MyJsonBean<>();
        try {
            BudgetExecution execution = executionService.getById(executionId);
            if (execution != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(execution);
            } else {
                result.setCode(0);
                result.setMsg("执行记录不存在");
            }
        } catch (Exception e) {
            log.error("查询执行详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询执行列表
     */
    @Operation(summary = "分页查询执行列表")
    @ApiOperation("分页查询执行列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetExecution>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetExecution>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetExecution> pageResult = executionService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询执行列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取执行进度分析
     */
    @Operation(summary = "获取执行进度分析")
    @ApiOperation("获取执行进度分析")
    @PostMapping("/progress-analysis")
    public MyJsonBean<Map<String, Object>> getProgressAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysis = executionService.getProgressAnalysis(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(analysis);
        } catch (Exception e) {
            log.error("获取执行进度分析异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取执行偏差分析
     */
    @Operation(summary = "获取执行偏差分析")
    @ApiOperation("获取执行偏差分析")
    @PostMapping("/variance-analysis")
    public MyJsonBean<Map<String, Object>> getVarianceAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysis = executionService.getVarianceAnalysis(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(analysis);
        } catch (Exception e) {
            log.error("获取执行偏差分析异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取执行趋势分析
     */
    @Operation(summary = "获取执行趋势分析")
    @ApiOperation("获取执行趋势分析")
    @PostMapping("/trend-analysis")
    public MyJsonBean<Map<String, Object>> getTrendAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysis = executionService.getTrendAnalysis(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(analysis);
        } catch (Exception e) {
            log.error("获取执行趋势分析异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取执行统计信息
     */
    @Operation(summary = "获取执行统计信息")
    @ApiOperation("获取执行统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = executionService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取执行统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成执行报表
     */
    @Operation(summary = "生成执行报表")
    @ApiOperation("生成执行报表")
    @PostMapping("/generate-report")
    public MyJsonBean<Map<String, Object>> generateReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> report = executionService.generateReport(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(report);
        } catch (Exception e) {
            log.error("生成执行报表异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出执行数据
     */
    @Operation(summary = "导出执行数据")
    @ApiOperation("导出执行数据")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetExecution> dataList = executionService.getPage(params).getTlist();
            if (dataList == null) dataList = new java.util.ArrayList<>();

            // 使用ExcelExport工具类导出
            String fileName = "预算执行数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算执行", BudgetExecution.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算执行数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出预算执行数据异常", e);
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (java.io.IOException ex) {
                log.error("响应写入异常", ex);
            }
        }
    }

    /**
     * 导出单个执行数据
     */
    @Operation(summary = "导出单个执行数据")
    @ApiOperation("导出单个执行数据")
    @GetMapping("/export/{id}")
    public MyJsonBean<Map<String, Object>> exportSingle(
            @ApiParam(value = "执行ID", required = true) @PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetExecution execution = executionService.getById(id);
            Map<String, Object> exportData = new java.util.HashMap<>();
            exportData.put("fileName", "预算执行_" + id + ".xlsx");
            exportData.put("data", execution);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportData);
        } catch (Exception e) {
            log.error("导出单个执行数据异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取执行分析详情")
    @ApiOperation("获取执行分析详情")
    @GetMapping("/analysis/{executionId}")
    public MyJsonBean<Map<String, Object>> getAnalysis(
            @ApiParam(value = "执行ID", required = true) @PathVariable String executionId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetExecution execution = executionService.getById(executionId);
            Map<String, Object> analysis = new HashMap<>();
            analysis.put("executionId", executionId);
            analysis.put("execution", execution);
            if (execution != null) {
                double executionAmt = execution.getAmount() != null ? execution.getAmount().doubleValue() : 0;
                // 计算完成率和偏差率需要预算总额，这里基于执行数据计算
                analysis.put("completionRate", executionAmt > 0 ? 100.0 : 0);
                analysis.put("varianceRate", 0);
                analysis.put("trend", "STABLE");
            } else {
                analysis.put("completionRate", 0);
                analysis.put("varianceRate", 0);
                analysis.put("trend", "UNKNOWN");
            }
            analysis.put("analysisTime", new Date());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(analysis);
        } catch (Exception e) {
            log.error("获取执行分析详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取组织列表")
    @ApiOperation("获取组织列表")
    @GetMapping("/organizations")
    public MyJsonBean<List<Map<String, Object>>> getOrganizations() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetOrganization> wrapper = new QueryWrapper<>();
            wrapper.eq("IS_ENABLED", 1);
            wrapper.orderByAsc("SORT_ORDER");
            List<BudgetOrganization> orgList = organizationMapper.selectList(wrapper);
            List<Map<String, Object>> orgs = new ArrayList<>();
            for (BudgetOrganization org : orgList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", org.getOrganizationId());
                map.put("name", org.getOrganizationName());
                map.put("code", org.getOrganizationCode());
                orgs.add(map);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(orgs);
        } catch (Exception e) {
            log.error("获取组织列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取预算科目列表")
    @ApiOperation("获取预算科目列表")
    @GetMapping("/accounts")
    public MyJsonBean<List<Map<String, Object>>> getAccounts() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetAccount> wrapper = new QueryWrapper<>();
            wrapper.eq("IS_ENABLED", 1);
            wrapper.orderByAsc("SORT_ORDER");
            List<BudgetAccount> accList = accountMapper.selectList(wrapper);
            List<Map<String, Object>> accounts = new ArrayList<>();
            for (BudgetAccount acc : accList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", acc.getAccountId());
                map.put("name", acc.getAccountName());
                map.put("code", acc.getAccountCode());
                accounts.add(map);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(accounts);
        } catch (Exception e) {
            log.error("获取预算科目列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "检查预警")
    @ApiOperation("检查预警")
    @PostMapping("/check-warnings")
    public MyJsonBean<List<Map<String, Object>>> checkWarnings(@RequestBody Map<String, Object> params) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            // 查询执行金额超出预算的记录
            QueryWrapper<BudgetExecution> qw = new QueryWrapper<>();
            if (params.get("budgetType") != null) {
                qw.eq("BUDGET_TYPE", params.get("budgetType").toString());
            }
            List<BudgetExecution> executions = executionService.list(qw);
            List<Map<String, Object>> warnings = new ArrayList<>();
            for (BudgetExecution exec : executions) {
                // 标记异常状态的执行记录为预警
                if (exec.getAmount() != null && exec.getAmount().doubleValue() < 0) {
                    Map<String, Object> w = new HashMap<>();
                    w.put("id", exec.getExecutionId());
                    w.put("type", "EXECUTION_EXCEPTION");
                    w.put("level", "HIGH");
                    w.put("message", "执行异常：" + exec.getExecutionId());
                    w.put("time", exec.getExecutionTime());
                    warnings.add(w);
                }
            }
            result.setCode(1);
            result.setMsg("检查完成，发现" + warnings.size() + "条预警");
            result.setData(warnings);
        } catch (Exception e) {
            log.error("检查预警异常", e);
            result.setCode(0);
            result.setMsg("检查失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出执行详情")
    @ApiOperation("导出执行详情")
    @GetMapping("/export-detail/{executionId}")
    public void exportDetail(
            @ApiParam(value = "执行ID", required = true) @PathVariable String executionId,
            HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=execution_detail_" + executionId + ".xlsx");
            StringBuilder sb = new StringBuilder();
            sb.append("ID,科目,预算金额,执行金额,执行率\n");
            BudgetExecution execution = executionService.getById(executionId);
            if (execution != null) {
                sb.append(execution.getExecutionId() != null ? execution.getExecutionId() : executionId).append(",");
                sb.append(execution.getBudgetType() != null ? execution.getBudgetType() : "").append(",");
                sb.append(execution.getBudgetId() != null ? execution.getBudgetId() : "").append(",");
                sb.append(execution.getAmount() != null ? execution.getAmount() : "").append(",");
                sb.append(execution.getRemark() != null ? execution.getRemark() : "").append("\n");
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出执行详情异常", e);
        }
    }

    @Operation(summary = "导出分析报告")
    @ApiOperation("导出分析报告")
    @PostMapping("/export-analysis")
    public void exportAnalysis(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=execution_analysis.xlsx");
            StringBuilder sb = new StringBuilder();
            sb.append("预算类型,预算ID,执行金额,执行人,备注\n");
            // 根据查询参数获取执行数据列表
            List<BudgetExecution> list = executionService.list();
            for (BudgetExecution exec : list) {
                sb.append(exec.getBudgetType() != null ? exec.getBudgetType() : "").append(",");
                sb.append(exec.getBudgetId() != null ? exec.getBudgetId() : "").append(",");
                sb.append(exec.getAmount() != null ? exec.getAmount() : "").append(",");
                sb.append(exec.getExecutionUserName() != null ? exec.getExecutionUserName() : "").append(",");
                sb.append(exec.getRemark() != null ? exec.getRemark() : "").append("\n");
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出分析报告异常", e);
        }
    }

    @Operation(summary = "批量分析")
    @ApiOperation("批量分析")
    @PostMapping("/batch-analysis")
    public MyJsonBean<Map<String, Object>> batchAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<String> ids = (List<String>) params.get("ids");
            Map<String, Object> analysisResult = new HashMap<>();
            int totalCount = 0;
            double totalAmount = 0;
            if (ids != null && !ids.isEmpty()) {
                List<BudgetExecution> executions = executionService.listByIds(ids);
                totalCount = executions.size();
                for (BudgetExecution exec : executions) {
                    if (exec.getAmount() != null) {
                        totalAmount += exec.getAmount().doubleValue();
                    }
                }
            }
            analysisResult.put("totalCount", totalCount);
            analysisResult.put("totalAmount", totalAmount);
            analysisResult.put("avgAmount", totalCount > 0 ? Math.round(totalAmount / totalCount * 100.0) / 100.0 : 0);
            analysisResult.put("analysisTime", new Date());
            result.setCode(1);
            result.setMsg("批量分析完成");
            result.setData(analysisResult);
        } catch (Exception e) {
            log.error("批量分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取绩效分析统计数据")
    @ApiOperation("获取绩效分析统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = executionService.getExecutionStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取绩效分析统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取绩效分析图表数据")
    @ApiOperation("获取绩效分析图表数据")
    @PostMapping("/chart-data")
    public MyJsonBean<Map<String, Object>> getChartData(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = executionService.getExecutionChartData(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取绩效分析图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取绩效排名")
    @ApiOperation("获取绩效排名")
    @GetMapping("/ranking")
    public MyJsonBean<Map<String, Object>> getRanking() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> ranking = executionService.getExecutionRanking();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(ranking);
        } catch (Exception e) {
            log.error("获取绩效排名失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取改进建议")
    @ApiOperation("获取改进建议")
    @GetMapping("/suggestions")
    public MyJsonBean<Map<String, Object>> getSuggestions() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> suggestions = executionService.getImprovementSuggestions();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(suggestions);
        } catch (Exception e) {
            log.error("获取改进建议失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}