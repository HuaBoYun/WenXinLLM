package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 战略管理控制器
 * 包含：战略规划、执行监控、绩效评价、战略调整、环境分析、竞争分析
 *
 * @author AI Assistant
 * @date 2026-05-08
 */
@Slf4j
@RestController
@RequestMapping("/v1/enterprise/strategy")
@Tag(name = "战略管理", description = "战略管理六大模块接口")
public class StrategyManagementController {

    @Autowired
    private GzctStrategyPlanningMapper planningMapper;

    @Autowired
    private GzctStrategyExecutionMapper executionMapper;

    @Autowired
    private GzctStrategyPerformanceMapper performanceMapper;

    @Autowired
    private GzctStrategyAdjustmentMapper adjustmentMapper;

    @Autowired
    private GzctStrategyEnvironmentMapper environmentMapper;

    @Autowired
    private GzctStrategyCompetitionMapper competitionMapper;

    // ==================== 总体统计 ====================

    @Operation(summary = "")
    @GetMapping("/statistics")
    public R<Map<String, Object>> getOverallStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();

            // 活跃计划数 - 状态为执行中的规划
            LambdaQueryWrapper<GzctStrategyPlanning> activeWrapper = new LambdaQueryWrapper<>();
            activeWrapper.eq(GzctStrategyPlanning::getStatus, "执行中");
            int activePlans = planningMapper.selectCount(activeWrapper).intValue();
            stats.put("activePlans", activePlans);

            // 达成率 - 规划表平均进度
            List<GzctStrategyPlanning> allPlans = planningMapper.selectList(null);
            double avgProgress = allPlans.stream()
                    .filter(p -> p.getProgress() != null)
                    .mapToDouble(p -> p.getProgress().doubleValue())
                    .average().orElse(0.0);
            stats.put("achievementRate", Math.round(avgProgress * 100.0) / 100.0);

            // 已完成里程碑
            List<GzctStrategyExecution> allExec = executionMapper.selectList(null);
            int completedMilestones = allExec.stream()
                    .filter(e -> e.getCompletedMilestones() != null)
                    .mapToInt(GzctStrategyExecution::getCompletedMilestones)
                    .sum();
            stats.put("completedMilestones", completedMilestones);

            // 风险项
            int riskItems = allExec.stream()
                    .filter(e -> e.getRiskCount() != null)
                    .mapToInt(GzctStrategyExecution::getRiskCount)
                    .sum();
            stats.put("riskItems", riskItems);

            return R.success(stats);
        } catch (Exception e) {
            log.error("获取战略管理总体统计失败", e);
            return R.fail("获取统计数据失败: " + e.getMessage());
        }
    }

    // ==================== 战略规划模块 ====================

    @Operation(summary = "查询列表")
    @PostMapping("/planning/list")
    public R<PageResult<GzctStrategyPlanning>> getPlanningList(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = getPageNumber(params);
            int pageSize = getPageSize(params);

            LambdaQueryWrapper<GzctStrategyPlanning> wrapper = new LambdaQueryWrapper<>();
            String status = getStringParam(params, "status");
            String planName = getStringParam(params, "planName");
            String planNo = getStringParam(params, "planNo");
            String planType = getStringParam(params, "planType");
            String manager = getStringParam(params, "manager");

            if (StringUtils.hasText(status)) {
                wrapper.eq(GzctStrategyPlanning::getStatus, status);
            }
            if (StringUtils.hasText(planName)) {
                wrapper.like(GzctStrategyPlanning::getPlanName, planName);
            }
            if (StringUtils.hasText(planNo)) {
                wrapper.like(GzctStrategyPlanning::getPlanNo, planNo);
            }
            if (StringUtils.hasText(planType)) {
                wrapper.eq(GzctStrategyPlanning::getPlanType, planType);
            }
            if (StringUtils.hasText(manager)) {
                wrapper.like(GzctStrategyPlanning::getManager, manager);
            }
            wrapper.orderByDesc(GzctStrategyPlanning::getCreateTime);

            List<GzctStrategyPlanning> all = planningMapper.selectList(wrapper);
            PageResult<GzctStrategyPlanning> pageResult = buildPageResult(all, currentPage, pageSize);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询战略规划列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询详情")
    @GetMapping("/planning/{id}")
    public R<GzctStrategyPlanning> getPlanningDetail(@PathVariable String id) {
        try {
            GzctStrategyPlanning entity = planningMapper.selectById(id);
            if (entity == null) {
                return R.fail("数据不存在");
            }
            return R.success(entity);
        } catch (Exception e) {
            log.error("查询战略规划详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增")
    @PostMapping("/planning/add")
    public R<String> addPlanning(@RequestBody GzctStrategyPlanning entity) {
        try {
            entity.setId(UUID.randomUUID().toString().replace("-", ""));
            entity.setCreateTime(LocalDateTime.now());
            // 自动生成计划编号
            if (!StringUtils.hasText(entity.getPlanNo())) {
                entity.setPlanNo(generateNo("SP"));
            }
            planningMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增战略规划失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PostMapping("/planning/update")
    public R<String> updatePlanning(@RequestBody GzctStrategyPlanning entity) {
        try {
            entity.setUpdateTime(LocalDateTime.now());
            planningMapper.updateById(entity);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新战略规划失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/planning/{id}")
    public R<String> deletePlanning(@PathVariable String id) {
        try {
            planningMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除战略规划失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量操作")
    @PostMapping("/planning/batch/delete")
    public R<String> batchDeletePlanning(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要删除的数据");
            }
            planningMapper.deleteBatchIds(ids);
            return R.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除战略规划失败", e);
            return R.fail("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "")
    @GetMapping("/planning/statistics")
    public R<Map<String, Object>> getPlanningStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<GzctStrategyPlanning> all = planningMapper.selectList(null);
            stats.put("total", all.size());

            long executing = all.stream().filter(e -> "执行中".equals(e.getStatus())).count();
            long completed = all.stream().filter(e -> "已完成".equals(e.getStatus())).count();
            long paused = all.stream().filter(e -> "已暂停".equals(e.getStatus())).count();
            stats.put("executing", executing);
            stats.put("completed", completed);
            stats.put("paused", paused);

            return R.success(stats);
        } catch (Exception e) {
            log.error("获取战略规划统计失败", e);
            return R.fail("获取统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出战略规划数据")
    @GetMapping("/planning/export")
    public void exportPlanning(HttpServletResponse response,
                               @RequestParam(required = false) String planNo,
                               @RequestParam(required = false) String planName,
                               @RequestParam(required = false) String planType,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) String manager) {
        try {
            LambdaQueryWrapper<GzctStrategyPlanning> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasText(planNo)) wrapper.like(GzctStrategyPlanning::getPlanNo, planNo);
            if (StringUtils.hasText(planName)) wrapper.like(GzctStrategyPlanning::getPlanName, planName);
            if (StringUtils.hasText(planType)) wrapper.eq(GzctStrategyPlanning::getPlanType, planType);
            if (StringUtils.hasText(status)) wrapper.eq(GzctStrategyPlanning::getStatus, status);
            if (StringUtils.hasText(manager)) wrapper.like(GzctStrategyPlanning::getManager, manager);
            wrapper.orderByDesc(GzctStrategyPlanning::getCreateTime);

            List<GzctStrategyPlanning> list = planningMapper.selectList(wrapper);

            String[] headers = {"计划编号", "计划名称", "计划类型", "负责人", "负责部门", "开始日期", "结束日期", "完成进度(%)", "预算(万元)", "优先级", "风险等级", "状态"};
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            SXSSFSheet sheet = workbook.createSheet("战略规划");
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            for (int i = 0; i < list.size(); i++) {
                GzctStrategyPlanning p = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(p.getPlanNo() != null ? p.getPlanNo() : "");
                row.createCell(1).setCellValue(p.getPlanName() != null ? p.getPlanName() : "");
                row.createCell(2).setCellValue(p.getPlanType() != null ? p.getPlanType() : "");
                row.createCell(3).setCellValue(p.getManager() != null ? p.getManager() : "");
                row.createCell(4).setCellValue(p.getDepartment() != null ? p.getDepartment() : "");
                row.createCell(5).setCellValue(p.getStartDate() != null ? p.getStartDate() : "");
                row.createCell(6).setCellValue(p.getEndDate() != null ? p.getEndDate() : "");
                row.createCell(7).setCellValue(p.getProgress() != null ? p.getProgress() : 0);
                row.createCell(8).setCellValue(p.getBudget() != null ? p.getBudget().doubleValue() : 0);
                row.createCell(9).setCellValue(p.getPriority() != null ? p.getPriority() : "");
                row.createCell(10).setCellValue(p.getRiskLevel() != null ? p.getRiskLevel() : "");
                row.createCell(11).setCellValue(p.getStatus() != null ? p.getStatus() : "");
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("战略规划数据.xlsx", "UTF-8"));
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            log.error("导出战略规划数据失败", e);
        }
    }

    // ==================== 执行监控模块 ====================

    @Operation(summary = "查询列表")
    @PostMapping("/execution/list")
    public R<PageResult<GzctStrategyExecution>> getExecutionList(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = getPageNumber(params);
            int pageSize = getPageSize(params);

            LambdaQueryWrapper<GzctStrategyExecution> wrapper = new LambdaQueryWrapper<>();
            String status = getStringParam(params, "status");
            String planName = getStringParam(params, "planName");
            String executionNo = getStringParam(params, "executionNo");
            String executionPhase = getStringParam(params, "executionPhase");
            String executor = getStringParam(params, "executor");

            if (StringUtils.hasText(status)) {
                wrapper.eq(GzctStrategyExecution::getStatus, status);
            }
            if (StringUtils.hasText(planName)) {
                wrapper.like(GzctStrategyExecution::getPlanName, planName);
            }
            if (StringUtils.hasText(executionNo)) {
                wrapper.like(GzctStrategyExecution::getExecutionNo, executionNo);
            }
            if (StringUtils.hasText(executionPhase)) {
                wrapper.eq(GzctStrategyExecution::getExecutionPhase, executionPhase);
            }
            if (StringUtils.hasText(executor)) {
                wrapper.like(GzctStrategyExecution::getExecutor, executor);
            }
            wrapper.orderByDesc(GzctStrategyExecution::getCreateTime);

            List<GzctStrategyExecution> all = executionMapper.selectList(wrapper);
            PageResult<GzctStrategyExecution> pageResult = buildPageResult(all, currentPage, pageSize);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询执行监控列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询详情")
    @GetMapping("/execution/{id}")
    public R<GzctStrategyExecution> getExecutionDetail(@PathVariable String id) {
        try {
            GzctStrategyExecution entity = executionMapper.selectById(id);
            if (entity == null) {
                return R.fail("数据不存在");
            }
            return R.success(entity);
        } catch (Exception e) {
            log.error("查询执行监控详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增")
    @PostMapping("/execution/add")
    public R<String> addExecution(@RequestBody GzctStrategyExecution entity) {
        try {
            entity.setId(UUID.randomUUID().toString().replace("-", ""));
            entity.setCreateTime(LocalDateTime.now());
            // 自动生成执行编号
            if (!StringUtils.hasText(entity.getExecutionNo())) {
                entity.setExecutionNo(generateNo("EX"));
            }
            executionMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增执行监控失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PostMapping("/execution/update")
    public R<String> updateExecution(@RequestBody GzctStrategyExecution entity) {
        try {
            entity.setUpdateTime(LocalDateTime.now());
            executionMapper.updateById(entity);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新执行监控失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/execution/{id}")
    public R<String> deleteExecution(@PathVariable String id) {
        try {
            executionMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除执行监控失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量操作")
    @PostMapping("/execution/batch/delete")
    public R<String> batchDeleteExecution(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要删除的数据");
            }
            executionMapper.deleteBatchIds(ids);
            return R.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除执行监控失败", e);
            return R.fail("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "")
    @GetMapping("/execution/statistics")
    public R<Map<String, Object>> getExecutionStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<GzctStrategyExecution> all = executionMapper.selectList(null);
            stats.put("total", all.size());

            long normalCount = all.stream().filter(e -> "正常执行".equals(e.getStatus())).count();
            long delayedCount = all.stream().filter(e -> "延期执行".equals(e.getStatus())).count();
            stats.put("normalCount", normalCount);
            stats.put("delayedCount", delayedCount);

            double avgProgress = all.stream()
                    .filter(e -> e.getActualProgress() != null)
                    .mapToDouble(e -> e.getActualProgress().doubleValue())
                    .average().orElse(0.0);
            stats.put("avgProgress", Math.round(avgProgress * 100.0) / 100.0);

            return R.success(stats);
        } catch (Exception e) {
            log.error("获取执行监控统计失败", e);
            return R.fail("获取统计失败: " + e.getMessage());
        }
    }


    // ==================== 绩效评价模块 ====================

    @Operation(summary = "查询列表")
    @PostMapping("/performance/list")
    public R<PageResult<GzctStrategyPerformance>> getPerformanceList(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = getPageNumber(params);
            int pageSize = getPageSize(params);

            LambdaQueryWrapper<GzctStrategyPerformance> wrapper = new LambdaQueryWrapper<>();
            String status = getStringParam(params, "status");
            String planName = getStringParam(params, "planName");
            String evaluationNo = getStringParam(params, "evaluationNo");
            String evaluationType = getStringParam(params, "evaluationType");
            String evaluator = getStringParam(params, "evaluator");

            if (StringUtils.hasText(status)) {
                wrapper.eq(GzctStrategyPerformance::getStatus, status);
            }
            if (StringUtils.hasText(planName)) {
                wrapper.like(GzctStrategyPerformance::getPlanName, planName);
            }
            if (StringUtils.hasText(evaluationNo)) {
                wrapper.like(GzctStrategyPerformance::getEvaluationNo, evaluationNo);
            }
            if (StringUtils.hasText(evaluationType)) {
                wrapper.eq(GzctStrategyPerformance::getEvaluationType, evaluationType);
            }
            if (StringUtils.hasText(evaluator)) {
                wrapper.like(GzctStrategyPerformance::getEvaluator, evaluator);
            }
            wrapper.orderByDesc(GzctStrategyPerformance::getCreateTime);

            List<GzctStrategyPerformance> all = performanceMapper.selectList(wrapper);
            PageResult<GzctStrategyPerformance> pageResult = buildPageResult(all, currentPage, pageSize);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询绩效评价列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询详情")
    @GetMapping("/performance/{id}")
    public R<GzctStrategyPerformance> getPerformanceDetail(@PathVariable String id) {
        try {
            GzctStrategyPerformance entity = performanceMapper.selectById(id);
            if (entity == null) {
                return R.fail("数据不存在");
            }
            return R.success(entity);
        } catch (Exception e) {
            log.error("查询绩效评价详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增")
    @PostMapping("/performance/add")
    public R<String> addPerformance(@RequestBody GzctStrategyPerformance entity) {
        try {
            entity.setId(UUID.randomUUID().toString().replace("-", ""));
            entity.setCreateTime(LocalDateTime.now());
            // 自动生成评估编号
            if (!StringUtils.hasText(entity.getEvaluationNo())) {
                entity.setEvaluationNo(generateNo("PF"));
            }
            performanceMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增绩效评价失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PostMapping("/performance/update")
    public R<String> updatePerformance(@RequestBody GzctStrategyPerformance entity) {
        try {
            entity.setUpdateTime(LocalDateTime.now());
            performanceMapper.updateById(entity);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新绩效评价失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/performance/{id}")
    public R<String> deletePerformance(@PathVariable String id) {
        try {
            performanceMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除绩效评价失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量操作")
    @PostMapping("/performance/batch/delete")
    public R<String> batchDeletePerformance(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要删除的数据");
            }
            performanceMapper.deleteBatchIds(ids);
            return R.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除绩效评价失败", e);
            return R.fail("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "")
    @GetMapping("/performance/statistics")
    public R<Map<String, Object>> getPerformanceStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<GzctStrategyPerformance> all = performanceMapper.selectList(null);
            stats.put("total", all.size());

            double avgKpi = all.stream()
                    .filter(e -> e.getKpiScore() != null)
                    .mapToDouble(e -> e.getKpiScore().doubleValue())
                    .average().orElse(0.0);
            stats.put("avgKpiScore", Math.round(avgKpi * 100.0) / 100.0);

            long excellentCount = all.stream().filter(e -> "优秀".equals(e.getOverallRating())).count();
            long pendingCount = all.stream().filter(e -> "待评估".equals(e.getStatus())).count();
            stats.put("excellentCount", excellentCount);
            stats.put("pendingCount", pendingCount);

            return R.success(stats);
        } catch (Exception e) {
            log.error("获取绩效评价统计失败", e);
            return R.fail("获取统计失败: " + e.getMessage());
        }
    }


    // ==================== 战略调整模块 ====================

    @Operation(summary = "查询列表")
    @PostMapping("/adjustment/list")
    public R<PageResult<GzctStrategyAdjustment>> getAdjustmentList(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = getPageNumber(params);
            int pageSize = getPageSize(params);

            LambdaQueryWrapper<GzctStrategyAdjustment> wrapper = new LambdaQueryWrapper<>();
            String status = getStringParam(params, "status");
            String planName = getStringParam(params, "planName");
            String adjustmentNo = getStringParam(params, "adjustmentNo");
            String adjustmentType = getStringParam(params, "adjustmentType");
            String applicant = getStringParam(params, "applicant");

            if (StringUtils.hasText(status)) {
                wrapper.eq(GzctStrategyAdjustment::getStatus, status);
            }
            if (StringUtils.hasText(planName)) {
                wrapper.like(GzctStrategyAdjustment::getPlanName, planName);
            }
            if (StringUtils.hasText(adjustmentNo)) {
                wrapper.like(GzctStrategyAdjustment::getAdjustmentNo, adjustmentNo);
            }
            if (StringUtils.hasText(adjustmentType)) {
                wrapper.eq(GzctStrategyAdjustment::getAdjustmentType, adjustmentType);
            }
            if (StringUtils.hasText(applicant)) {
                wrapper.like(GzctStrategyAdjustment::getApplicant, applicant);
            }
            wrapper.orderByDesc(GzctStrategyAdjustment::getCreateTime);

            List<GzctStrategyAdjustment> all = adjustmentMapper.selectList(wrapper);
            PageResult<GzctStrategyAdjustment> pageResult = buildPageResult(all, currentPage, pageSize);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询战略调整列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询详情")
    @GetMapping("/adjustment/{id}")
    public R<GzctStrategyAdjustment> getAdjustmentDetail(@PathVariable String id) {
        try {
            GzctStrategyAdjustment entity = adjustmentMapper.selectById(id);
            if (entity == null) {
                return R.fail("数据不存在");
            }
            return R.success(entity);
        } catch (Exception e) {
            log.error("查询战略调整详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增")
    @PostMapping("/adjustment/add")
    public R<String> addAdjustment(@RequestBody GzctStrategyAdjustment entity) {
        try {
            entity.setId(UUID.randomUUID().toString().replace("-", ""));
            entity.setCreateTime(LocalDateTime.now());
            // 自动生成调整编号
            if (!StringUtils.hasText(entity.getAdjustmentNo())) {
                entity.setAdjustmentNo(generateNo("AD"));
            }
            adjustmentMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增战略调整失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PostMapping("/adjustment/update")
    public R<String> updateAdjustment(@RequestBody GzctStrategyAdjustment entity) {
        try {
            entity.setUpdateTime(LocalDateTime.now());
            adjustmentMapper.updateById(entity);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新战略调整失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/adjustment/{id}")
    public R<String> deleteAdjustment(@PathVariable String id) {
        try {
            adjustmentMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除战略调整失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量操作")
    @PostMapping("/adjustment/batch/delete")
    public R<String> batchDeleteAdjustment(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要删除的数据");
            }
            adjustmentMapper.deleteBatchIds(ids);
            return R.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除战略调整失败", e);
            return R.fail("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "")
    @GetMapping("/adjustment/statistics")
    public R<Map<String, Object>> getAdjustmentStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<GzctStrategyAdjustment> all = adjustmentMapper.selectList(null);
            stats.put("total", all.size());

            long pendingCount = all.stream().filter(e -> "待审批".equals(e.getStatus())).count();
            long approvedCount = all.stream().filter(e -> "已批准".equals(e.getStatus())).count();
            long rejectedCount = all.stream().filter(e -> "已拒绝".equals(e.getStatus())).count();
            stats.put("pendingCount", pendingCount);
            stats.put("approvedCount", approvedCount);
            stats.put("rejectedCount", rejectedCount);

            Map<String, Long> byType = new HashMap<>();
            for (GzctStrategyAdjustment item : all) {
                String t = item.getAdjustmentType() != null ? item.getAdjustmentType() : "未知";
                byType.put(t, byType.getOrDefault(t, 0L) + 1);
            }
            stats.put("byType", byType);

            return R.success(stats);
        } catch (Exception e) {
            log.error("获取战略调整统计失败", e);
            return R.fail("获取统计失败: " + e.getMessage());
        }
    }


    // ==================== 环境分析模块 ====================

    @Operation(summary = "查询列表")
    @PostMapping("/environment/list")
    public R<PageResult<GzctStrategyEnvironment>> getEnvironmentList(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = getPageNumber(params);
            int pageSize = getPageSize(params);

            LambdaQueryWrapper<GzctStrategyEnvironment> wrapper = new LambdaQueryWrapper<>();
            String status = getStringParam(params, "status");
            String analysisTheme = getStringParam(params, "analysisTheme");
            String analysisNo = getStringParam(params, "analysisNo");
            String analysisType = getStringParam(params, "analysisType");
            String analyst = getStringParam(params, "analyst");

            if (StringUtils.hasText(status)) {
                wrapper.eq(GzctStrategyEnvironment::getStatus, status);
            }
            if (StringUtils.hasText(analysisTheme)) {
                wrapper.like(GzctStrategyEnvironment::getAnalysisTheme, analysisTheme);
            }
            if (StringUtils.hasText(analysisNo)) {
                wrapper.like(GzctStrategyEnvironment::getAnalysisNo, analysisNo);
            }
            if (StringUtils.hasText(analysisType)) {
                wrapper.eq(GzctStrategyEnvironment::getAnalysisType, analysisType);
            }
            if (StringUtils.hasText(analyst)) {
                wrapper.like(GzctStrategyEnvironment::getAnalyst, analyst);
            }
            wrapper.orderByDesc(GzctStrategyEnvironment::getCreateTime);

            List<GzctStrategyEnvironment> all = environmentMapper.selectList(wrapper);
            PageResult<GzctStrategyEnvironment> pageResult = buildPageResult(all, currentPage, pageSize);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询环境分析列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询详情")
    @GetMapping("/environment/{id}")
    public R<GzctStrategyEnvironment> getEnvironmentDetail(@PathVariable String id) {
        try {
            GzctStrategyEnvironment entity = environmentMapper.selectById(id);
            if (entity == null) {
                return R.fail("数据不存在");
            }
            return R.success(entity);
        } catch (Exception e) {
            log.error("查询环境分析详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增")
    @PostMapping("/environment/add")
    public R<String> addEnvironment(@RequestBody GzctStrategyEnvironment entity) {
        try {
            entity.setId(UUID.randomUUID().toString().replace("-", ""));
            entity.setCreateTime(LocalDateTime.now());
            // 自动生成分析编号
            if (!StringUtils.hasText(entity.getAnalysisNo())) {
                entity.setAnalysisNo(generateNo("EN"));
            }
            environmentMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增环境分析失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PostMapping("/environment/update")
    public R<String> updateEnvironment(@RequestBody GzctStrategyEnvironment entity) {
        try {
            entity.setUpdateTime(LocalDateTime.now());
            environmentMapper.updateById(entity);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新环境分析失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/environment/{id}")
    public R<String> deleteEnvironment(@PathVariable String id) {
        try {
            environmentMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除环境分析失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量操作")
    @PostMapping("/environment/batch/delete")
    public R<String> batchDeleteEnvironment(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要删除的数据");
            }
            environmentMapper.deleteBatchIds(ids);
            return R.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除环境分析失败", e);
            return R.fail("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "")
    @GetMapping("/environment/statistics")
    public R<Map<String, Object>> getEnvironmentStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<GzctStrategyEnvironment> all = environmentMapper.selectList(null);
            stats.put("total", all.size());

            long inProgressCount = all.stream().filter(e -> "进行中".equals(e.getStatus())).count();
            long completedCount = all.stream().filter(e -> "已完成".equals(e.getStatus())).count();
            long needUpdateCount = all.stream().filter(e -> "需更新".equals(e.getStatus())).count();
            stats.put("inProgressCount", inProgressCount);
            stats.put("completedCount", completedCount);
            stats.put("needUpdateCount", needUpdateCount);

            double avgOpportunity = all.stream()
                    .filter(e -> e.getOpportunityIndex() != null)
                    .mapToDouble(e -> e.getOpportunityIndex().doubleValue())
                    .average().orElse(0.0);
            stats.put("avgOpportunityIndex", Math.round(avgOpportunity * 100.0) / 100.0);

            double avgThreat = all.stream()
                    .filter(e -> e.getThreatIndex() != null)
                    .mapToDouble(e -> e.getThreatIndex().doubleValue())
                    .average().orElse(0.0);
            stats.put("avgThreatIndex", Math.round(avgThreat * 100.0) / 100.0);

            return R.success(stats);
        } catch (Exception e) {
            log.error("获取环境分析统计失败", e);
            return R.fail("获取统计失败: " + e.getMessage());
        }
    }


    // ==================== 竞争分析模块 ====================

    @Operation(summary = "查询列表")
    @PostMapping("/competition/list")
    public R<PageResult<GzctStrategyCompetition>> getCompetitionList(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = getPageNumber(params);
            int pageSize = getPageSize(params);

            LambdaQueryWrapper<GzctStrategyCompetition> wrapper = new LambdaQueryWrapper<>();
            String status = getStringParam(params, "status");
            String competitorName = getStringParam(params, "competitorName");
            String analysisNo = getStringParam(params, "analysisNo");
            String analysisDimension = getStringParam(params, "analysisDimension");
            String analyst = getStringParam(params, "analyst");

            if (StringUtils.hasText(status)) {
                wrapper.eq(GzctStrategyCompetition::getStatus, status);
            }
            if (StringUtils.hasText(competitorName)) {
                wrapper.like(GzctStrategyCompetition::getCompetitorName, competitorName);
            }
            if (StringUtils.hasText(analysisNo)) {
                wrapper.like(GzctStrategyCompetition::getAnalysisNo, analysisNo);
            }
            if (StringUtils.hasText(analysisDimension)) {
                wrapper.eq(GzctStrategyCompetition::getAnalysisDimension, analysisDimension);
            }
            if (StringUtils.hasText(analyst)) {
                wrapper.like(GzctStrategyCompetition::getAnalyst, analyst);
            }
            wrapper.orderByDesc(GzctStrategyCompetition::getCreateTime);

            List<GzctStrategyCompetition> all = competitionMapper.selectList(wrapper);
            PageResult<GzctStrategyCompetition> pageResult = buildPageResult(all, currentPage, pageSize);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询竞争分析列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询详情")
    @GetMapping("/competition/{id}")
    public R<GzctStrategyCompetition> getCompetitionDetail(@PathVariable String id) {
        try {
            GzctStrategyCompetition entity = competitionMapper.selectById(id);
            if (entity == null) {
                return R.fail("数据不存在");
            }
            return R.success(entity);
        } catch (Exception e) {
            log.error("查询竞争分析详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增")
    @PostMapping("/competition/add")
    public R<String> addCompetition(@RequestBody GzctStrategyCompetition entity) {
        try {
            entity.setId(UUID.randomUUID().toString().replace("-", ""));
            entity.setCreateTime(LocalDateTime.now());
            // 自动生成分析编号
            if (!StringUtils.hasText(entity.getAnalysisNo())) {
                entity.setAnalysisNo(generateNo("CP"));
            }
            competitionMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增竞争分析失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PostMapping("/competition/update")
    public R<String> updateCompetition(@RequestBody GzctStrategyCompetition entity) {
        try {
            entity.setUpdateTime(LocalDateTime.now());
            competitionMapper.updateById(entity);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新竞争分析失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/competition/{id}")
    public R<String> deleteCompetition(@PathVariable String id) {
        try {
            competitionMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除竞争分析失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量操作")
    @PostMapping("/competition/batch/delete")
    public R<String> batchDeleteCompetition(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要删除的数据");
            }
            competitionMapper.deleteBatchIds(ids);
            return R.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除竞争分析失败", e);
            return R.fail("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "")
    @GetMapping("/competition/statistics")
    public R<Map<String, Object>> getCompetitionStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<GzctStrategyCompetition> all = competitionMapper.selectList(null);
            stats.put("total", all.size());

            long inProgressCount = all.stream().filter(e -> "分析中".equals(e.getStatus())).count();
            long completedCount = all.stream().filter(e -> "已完成".equals(e.getStatus())).count();
            stats.put("inProgressCount", inProgressCount);
            stats.put("completedCount", completedCount);

            double avgMarketShare = all.stream()
                    .filter(e -> e.getMarketShare() != null)
                    .mapToDouble(e -> e.getMarketShare().doubleValue())
                    .average().orElse(0.0);
            stats.put("avgMarketShare", Math.round(avgMarketShare * 100.0) / 100.0);

            return R.success(stats);
        } catch (Exception e) {
            log.error("获取竞争分析统计失败", e);
            return R.fail("获取统计失败: " + e.getMessage());
        }
    }


    // ==================== 通用接口 ====================

    /**
     * 获取所有规划名称（下拉选择用）
     */
    @Operation(summary = "获取所有规划名称")
    @GetMapping("/planning/all")
    public R<List<Map<String, Object>>> getAllPlanningNames() {
        try {
            LambdaQueryWrapper<GzctStrategyPlanning> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(GzctStrategyPlanning::getId, GzctStrategyPlanning::getPlanName);
            wrapper.orderByDesc(GzctStrategyPlanning::getCreateTime);
            List<GzctStrategyPlanning> list = planningMapper.selectList(wrapper);

            List<Map<String, Object>> result = new ArrayList<>();
            for (GzctStrategyPlanning item : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", item.getId());
                map.put("planName", item.getPlanName());
                result.add(map);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("获取规划名称列表失败", e);
            return R.fail("获取规划名称列表失败: " + e.getMessage());
        }
    }

    /**
     * 批量审批规划（将状态改为"已批准"）
     */
    @Operation(summary = "批量审批规划")
    @PostMapping("/planning/batch/approve")
    public R<String> batchApprovePlanning(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择需要审批的规划");
            }

            int successCount = 0;
            for (String id : ids) {
                GzctStrategyPlanning planning = planningMapper.selectById(id);
                if (planning != null) {
                    planning.setStatus("已批准");
                    planning.setUpdateTime(LocalDateTime.now());
                    planningMapper.updateById(planning);
                    successCount++;
                }
            }
            return R.success("成功审批 " + successCount + " 条规划");
        } catch (Exception e) {
            log.error("批量审批规划失败", e);
            return R.fail("批量审批失败: " + e.getMessage());
        }
    }

    /**
     * 评估绩效记录
     * 根据综合评级(overallRating)判断状态：优秀/良好 -> "已完成"，一般/较差/空 -> "评估中"
     */
    @Operation(summary = "评估绩效记录")
    @PostMapping("/performance/evaluate")
    public R<GzctStrategyPerformance> evaluatePerformance(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            if (!StringUtils.hasText(id)) {
                return R.fail("绩效记录ID不能为空");
            }

            GzctStrategyPerformance performance = performanceMapper.selectById(id);
            if (performance == null) {
                return R.fail("绩效记录不存在");
            }
            if ("已完成".equals(performance.getStatus())) {
                return R.fail("该记录已完成评估，无需重复操作");
            }

            // 根据综合评级决定目标状态
            String rating = performance.getOverallRating();
            if ("优秀".equals(rating) || "良好".equals(rating)) {
                performance.setStatus("已完成");
            } else {
                performance.setStatus("评估中");
            }
            performance.setUpdateTime(LocalDateTime.now());
            performanceMapper.updateById(performance);

            return R.success(performance);
        } catch (Exception e) {
            log.error("评估绩效记录失败", e);
            return R.fail("评估失败: " + e.getMessage());
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 获取分页页码，兼容 pageNumber 和 currentPage 两种参数名
     */
    private int getPageNumber(Map<String, Object> params) {
        Object pageNumber = params.get("pageNumber");
        if (pageNumber == null) {
            pageNumber = params.get("currentPage");
        }
        if (pageNumber == null) {
            return 1;
        }
        return Integer.parseInt(pageNumber.toString());
    }

    /**
     * 获取每页大小
     */
    private int getPageSize(Map<String, Object> params) {
        Object pageSize = params.get("pageSize");
        if (pageSize == null) {
            return 10;
        }
        return Integer.parseInt(pageSize.toString());
    }

    /**
     * 安全获取字符串参数
     */
    private String getStringParam(Map<String, Object> params, String key) {
        Object value = params.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * 构建分页结果
     */
    private <T> PageResult<T> buildPageResult(List<T> all, int currentPage, int pageSize) {
        int total = all.size();
        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<T> records = fromIndex < total ? all.subList(fromIndex, toIndex) : new ArrayList<>();

        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotalRecord(total);
        pageResult.setCurrentPage(currentPage);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPage((total + pageSize - 1) / pageSize);
        pageResult.setPageNumber(currentPage);
        pageResult.setTlist(records);
        return pageResult;
    }

    /**
     * 自动生成业务编号
     * 格式：前缀 + 年月日 + 4位随机数，如 SP20250610-3847
     */
    private String generateNo(String prefix) {
        String datePart = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        int random = (int) (Math.random() * 9000) + 1000;
        return prefix + datePart + "-" + random;
    }
}
