package com.management.accountant.controller;

import com.alibaba.excel.EasyExcel;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetVarianceAnalysis;
import com.management.accountant.service.BudgetVarianceAnalysisService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 预算差异分析Controller
 * 
 * @description 预算差异分析接口，支持预算与实际的差异分析
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-差异分析"})
@RequestMapping(value = "/accountant/variance")
@Slf4j
public class BudgetVarianceAnalysisController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetVarianceAnalysisService varianceService;

    /**
     * 创建差异分析（接收Map，兼容前端字段名）
     */
    @Operation(summary = "创建差异分析")
    @ApiOperation("创建差异分析")
    @PostMapping("/create")
    public MyJsonBean<BudgetVarianceAnalysis> create(@RequestBody Map<String, Object> params) {
        MyJsonBean<BudgetVarianceAnalysis> result = new MyJsonBean<>();
        try {
            BudgetVarianceAnalysis variance = mapToVariance(params);
            BudgetVarianceAnalysis created = varianceService.create(variance);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建差异分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建差异分析异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询差异分析详情
     */
    @Operation(summary = "查询差异分析详情")
    @ApiOperation("查询差异分析详情")
    @GetMapping("/detail/{id}")
    public MyJsonBean<BudgetVarianceAnalysis> getDetail(
            @ApiParam(value = "分析ID", required = true) @PathVariable String id) {
        MyJsonBean<BudgetVarianceAnalysis> result = new MyJsonBean<>();
        try {
            BudgetVarianceAnalysis variance = varianceService.getById(id);
            if (variance != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(variance);
            } else {
                result.setCode(0);
                result.setMsg("差异分析不存在");
            }
        } catch (Exception e) {
            log.error("查询差异分析详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新差异分析（接收Map，兼容前端字段名）
     */
    @Operation(summary = "更新差异分析")
    @ApiOperation("更新差异分析")
    @PutMapping("/update")
    public MyJsonBean<BudgetVarianceAnalysis> update(@RequestBody Map<String, Object> params) {
        MyJsonBean<BudgetVarianceAnalysis> result = new MyJsonBean<>();
        try {
            BudgetVarianceAnalysis variance = mapToVariance(params);
            BudgetVarianceAnalysis updated = varianceService.update(variance);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updated);
        } catch (ServiceException ex) {
            log.error("更新差异分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新差异分析异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除差异分析
     */
    @Operation(summary = "删除差异分析")
    @ApiOperation("删除差异分析")
    @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "分析ID", required = true) @PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            varianceService.delete(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除差异分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除差异分析异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量删除差异分析
     */
    @Operation(summary = "批量删除差异分析")
    @ApiOperation("批量删除差异分析")
    @PostMapping("/batch-delete")
    public MyJsonBean<Void> batchDelete(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要删除的记录");
                return result;
            }
            varianceService.batchDelete(ids);
            result.setCode(1);
            result.setMsg("批量删除成功");
        } catch (ServiceException ex) {
            log.error("批量删除差异分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量删除差异分析异常", e);
            result.setCode(0);
            result.setMsg("批量删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询差异分析列表
     */
    @Operation(summary = "分页查询差异分析列表")
    @ApiOperation("分页查询差异分析列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetVarianceAnalysis>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetVarianceAnalysis>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetVarianceAnalysis> pageResult = varianceService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询差异分析列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行差异分析
     */
    @Operation(summary = "执行差异分析")
    @ApiOperation("执行差异分析")
    @PostMapping("/analyze")
    public MyJsonBean<Map<String, Object>> analyze(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analyzeResult = varianceService.analyze(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(analyzeResult);
        } catch (ServiceException ex) {
            log.error("执行差异分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行差异分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成差异分析报告
     */
    @Operation(summary = "生成差异分析报告")
    @ApiOperation("生成差异分析报告")
    @PostMapping("/report")
    public MyJsonBean<Map<String, Object>> generateReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> report = varianceService.generateReport(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(report);
        } catch (ServiceException ex) {
            log.error("生成差异分析报告失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("生成差异分析报告异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取差异分析组织列表
     */
    @Operation(summary = "获取差异分析组织列表")
    @ApiOperation("获取差异分析组织列表")
    @GetMapping("/organizations")
    public MyJsonBean<Object> getOrganizations() {
        MyJsonBean<Object> result = new MyJsonBean<>();
        try {
            Object organizations = varianceService.getOrganizations();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(organizations);
        } catch (Exception e) {
            log.error("获取组织列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取差异分析预算科目列表
     */
    @Operation(summary = "获取差异分析预算科目列表")
    @ApiOperation("获取差异分析预算科目列表")
    @GetMapping("/budget-accounts")
    public MyJsonBean<Object> getBudgetAccounts() {
        MyJsonBean<Object> result = new MyJsonBean<>();
        try {
            Object accounts = varianceService.getBudgetAccounts();
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

    /**
     * 获取差异分析用户列表
     */
    @Operation(summary = "获取差异分析用户列表")
    @ApiOperation("获取差异分析用户列表")
    @GetMapping("/users")
    public MyJsonBean<Object> getUsers() {
        MyJsonBean<Object> result = new MyJsonBean<>();
        try {
            Object users = varianceService.getUsers();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(users);
        } catch (Exception e) {
            log.error("获取用户列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新差异分析原因
     */
    @Operation(summary = "更新差异分析原因")
    @ApiOperation("更新差异分析原因")
    @PutMapping("/reason")
    public MyJsonBean<Void> updateReason(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            varianceService.updateReason(params);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新差异分析原因失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新差异分析原因异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出差异分析报告（EasyExcel，支持导出选中行或当前页）
     */
    @Operation(summary = "导出差异分析报告")
    @ApiOperation("导出差异分析报告")
    @PostMapping("/export")
    public void exportReport(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetVarianceAnalysis> dataList = varianceService.exportReport(params);
            if (dataList == null) dataList = new java.util.ArrayList<>();
            String fileName = URLEncoder.encode("预算差异分析报告", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            EasyExcel.write(response.getOutputStream())
                    .head(buildExcelHead())
                    .sheet("差异分析数据")
                    .doWrite(buildExcelData(dataList));
        } catch (Exception e) {
            log.error("导出差异分析报告异常", e);
            response.setStatus(500);
        }
    }

    /**
     * 导出单个差异分析（EasyExcel）
     */
    @Operation(summary = "导出单个差异分析")
    @ApiOperation("导出单个差异分析")
    @GetMapping("/export/{varianceId}")
    public void exportSingle(
            @ApiParam(value = "差异分析ID", required = true) @PathVariable String varianceId,
            HttpServletResponse response) {
        try {
            List<BudgetVarianceAnalysis> dataList = varianceService.exportSingle(varianceId);
            if (dataList == null) dataList = new java.util.ArrayList<>();
            String fileName = URLEncoder.encode("差异分析详情", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            EasyExcel.write(response.getOutputStream())
                    .head(buildExcelHead())
                    .sheet("差异分析数据")
                    .doWrite(buildExcelData(dataList));
        } catch (Exception e) {
            log.error("导出单个差异分析异常", e);
            response.setStatus(500);
        }
    }

    /** 构建Excel表头 */
    private List<List<String>> buildExcelHead() {
        List<List<String>> head = new java.util.ArrayList<>();
        head.add(java.util.Collections.singletonList("组织单元"));
        head.add(java.util.Collections.singletonList("预算科目"));
        head.add(java.util.Collections.singletonList("预算金额"));
        head.add(java.util.Collections.singletonList("实际金额"));
        head.add(java.util.Collections.singletonList("差异金额"));
        head.add(java.util.Collections.singletonList("差异率(%)"));
        head.add(java.util.Collections.singletonList("差异类型"));
        head.add(java.util.Collections.singletonList("差异原因"));
        head.add(java.util.Collections.singletonList("改进措施"));
        head.add(java.util.Collections.singletonList("分析状态"));
        head.add(java.util.Collections.singletonList("分析时间"));
        return head;
    }

    /** 构建Excel数据行 */
    private List<List<Object>> buildExcelData(List<BudgetVarianceAnalysis> list) {
        List<List<Object>> data = new java.util.ArrayList<>();
        for (BudgetVarianceAnalysis v : list) {
            List<Object> row = new java.util.ArrayList<>();
            row.add(v.getOrganizationName());
            row.add(v.getAccountName());
            row.add(v.getBudgetAmount());
            row.add(v.getActualAmount());
            row.add(v.getVarianceAmount());
            row.add(v.getVarianceRate());
            String vType = v.getVarianceType();
            row.add("POSITIVE".equals(vType) ? "正差异" : "NEGATIVE".equals(vType) ? "负差异" : "无差异");
            row.add(v.getVarianceReason());
            row.add(v.getImprovementMeasures());
            row.add(v.getAnalysisStatus());
            row.add(v.getAnalysisDate());
            data.add(row);
        }
        return data;
    }

    /** 将前端Map参数映射到实体类 */
    private BudgetVarianceAnalysis mapToVariance(Map<String, Object> params) {
        BudgetVarianceAnalysis v = new BudgetVarianceAnalysis();
        if (params.get("id") != null) v.setId(params.get("id").toString());
        if (params.get("analysisName") != null) v.setAnalysisName(params.get("analysisName").toString());
        if (params.get("organizationId") != null) v.setOrganizationId(params.get("organizationId").toString());
        // 前端 budgetAccountId 对应 accountId
        Object acctId = params.get("accountId") != null ? params.get("accountId") : params.get("budgetAccountId");
        if (acctId != null) v.setAccountId(acctId.toString());
        if (params.get("budgetYear") != null) {
            try { v.setBudgetYear(Integer.parseInt(params.get("budgetYear").toString())); } catch (Exception ignored) {}
        }
        if (params.get("budgetPeriod") != null) v.setBudgetPeriod(params.get("budgetPeriod").toString());
        if (params.get("budgetAmount") != null) {
            try { v.setBudgetAmount(new BigDecimal(params.get("budgetAmount").toString())); } catch (Exception ignored) {}
        }
        if (params.get("actualAmount") != null) {
            try { v.setActualAmount(new BigDecimal(params.get("actualAmount").toString())); } catch (Exception ignored) {}
        }
        if (params.get("varianceReason") != null) v.setVarianceReason(params.get("varianceReason").toString());
        if (params.get("improvementMeasures") != null) v.setImprovementMeasures(params.get("improvementMeasures").toString());
        // 前端 responsiblePerson 对应 reviewedBy
        Object reviewer = params.get("reviewedBy") != null ? params.get("reviewedBy") : params.get("responsiblePerson");
        if (reviewer != null) v.setReviewedBy(reviewer.toString());
        if (params.get("analysisStatus") != null) v.setAnalysisStatus(params.get("analysisStatus").toString());
        if (params.get("description") != null) v.setAnalysisDescription(params.get("description").toString());
        // 自动计算差异
        if (v.getBudgetAmount() != null && v.getActualAmount() != null) {
            BigDecimal diff = v.getActualAmount().subtract(v.getBudgetAmount());
            v.setVarianceAmount(diff);
            if (v.getBudgetAmount().compareTo(BigDecimal.ZERO) != 0) {
                v.setVarianceRate(diff.divide(v.getBudgetAmount(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
            }
            v.setVarianceType(diff.compareTo(BigDecimal.ZERO) > 0 ? "POSITIVE" : diff.compareTo(BigDecimal.ZERO) < 0 ? "NEGATIVE" : "ZERO");
        }
        return v;
    }

    /**
     * 获取差异分析统计数据
     */
    @Operation(summary = "获取差异分析统计数据")
    @ApiOperation("获取差异分析统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = varianceService.getVarianceStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取差异分析统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取差异分析图表数据
     */
    @Operation(summary = "获取差异分析图表数据")
    @ApiOperation("获取差异分析图表数据")
    @PostMapping("/chart-data")
    public MyJsonBean<Map<String, Object>> getChartData(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = varianceService.getVarianceChartData(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取差异分析图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

