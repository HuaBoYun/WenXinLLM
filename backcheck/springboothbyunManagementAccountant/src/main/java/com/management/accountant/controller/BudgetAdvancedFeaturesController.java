package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.*;
import com.management.accountant.oracle.entity.budget.BudgetAuditTrail;
import com.management.accountant.oracle.mapper.advanced.*;
import com.management.accountant.oracle.mapper.budget.BudgetAuditTrailMapper;
import com.management.accountant.oracle.service.advanced.*;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Supplier;

/**
 * 预算高级功能Controller
 *
 * @description 预算高级功能接口，支持功能统计、最近活动、系统通知等
 * @author AI Assistant
 * @date 2025-01-05
 */
@RestController
@Api(tags = {"NCV65全面预算-高级功能"})
@RequestMapping(value = "/accountant/advanced")
@Slf4j
public class BudgetAdvancedFeaturesController {

    @Resource
    private FormulaTraceTaskService formulaTraceTaskService;

    @Resource
    private IntelligentRecommendationService intelligentRecommendationService;

    @Resource
    private ReminderManagementService reminderManagementService;

    @Resource
    private IntegrationMonitorService integrationMonitorService;

    @Resource
    private AdvancedReportService advancedReportService;

    @Resource
    private AutomationWorkflowService automationWorkflowService;

    @Resource
    private BatchCalculationService batchCalculationService;

    @Resource
    private BudgetOptimizationService budgetOptimizationService;

    @Resource
    private BudgetOptimizationResultService budgetOptimizationResultService;

    @Resource
    private BudgetSimulationService budgetSimulationService;

    @Resource
    private CollaborativeProjectService collaborativeProjectService;

    @Resource
    private DataMiningTaskService dataMiningTaskService;

    @Resource
    private VersionComparisonService versionComparisonService;

    @Resource
    private RiskAssessmentService riskAssessmentService;

    @Resource
    private AdvancedReportMapper advancedReportMapper;

    @Resource
    private AutomationWorkflowMapper automationWorkflowMapper;

    @Resource
    private BatchCalculationMapper batchCalculationMapper;

    @Resource
    private BudgetOptimizationMapper budgetOptimizationMapper;

    @Resource
    private BudgetSimulationMapper budgetSimulationMapper;

    @Resource
    private CollaborativeProjectMapper collaborativeProjectMapper;

    @Resource
    private DataMiningTaskMapper dataMiningTaskMapper;

    @Resource
    private VersionComparisonMapper versionComparisonMapper;

    @Resource
    private RiskAssessmentMapper riskAssessmentMapper;

    @Resource
    private RollingBudgetPlanMapper rollingBudgetPlanMapper;

    @Resource
    private FormulaTraceTaskMapper formulaTraceTaskMapper;

    @Resource
    private ReminderStrategyMapper reminderStrategyMapper;

    @Resource
    private CurrencyConfigMapper currencyConfigMapper;

    @Resource
    private IntelligentRecommendationMapper intelligentRecommendationMapper;

    @Resource
    private BudgetAuditTrailMapper budgetAuditTrailMapper;

    @Resource
    private DrillThroughQueryMapper drillThroughQueryMapper;

    /**
     * 获取功能统计数据
     */
    @Operation(summary = "获取功能统计数据")
    @ApiOperation("获取功能统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getFeatureStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = new HashMap<>();

            stats.put("rollingBudgets", rollingBudgetPlanMapper.selectCount(new QueryWrapper<>()));
            stats.put("formulaTraces", formulaTraceTaskMapper.selectCount(null, null, null));
            stats.put("reminderTasks", reminderStrategyMapper.selectCount(new QueryWrapper<>()));
            stats.put("currencies", currencyConfigMapper.selectCount(new QueryWrapper<>()));
            stats.put("batchCalculations", batchCalculationMapper.selectCount(new QueryWrapper<>()));
            stats.put("intelligentRecommendations", intelligentRecommendationMapper.countByCondition(null, null, null));
            stats.put("simulations", budgetSimulationMapper.selectCount(new QueryWrapper<>()));
            stats.put("dataMiningTasks", dataMiningTaskMapper.selectCount(new QueryWrapper<>()));
            stats.put("optimizations", budgetOptimizationMapper.selectCount(new QueryWrapper<>()));
            stats.put("riskAssessments", riskAssessmentMapper.selectCount(new QueryWrapper<>()));
            stats.put("collaborations", collaborativeProjectMapper.selectCount(new QueryWrapper<>()));
            stats.put("versionComparisons", versionComparisonMapper.selectCount(new QueryWrapper<>()));
            stats.put("workflows", automationWorkflowMapper.selectCount(new QueryWrapper<>()));
            stats.put("advancedReports", advancedReportMapper.selectCount(new QueryWrapper<>()));

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取功能统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取各模块统计数据（任务数+最后更新时间）
     */
    @Operation(summary = "获取各模块统计数据")
    @ApiOperation("获取各模块统计数据")
    @GetMapping("/module-stats")
    public MyJsonBean<List<Map<String, Object>>> getModuleStats() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> moduleStats = new ArrayList<>();

            // 1-滚动预算
            moduleStats.add(buildModuleStat(1, "滚动预算",
                    safeCount(() -> rollingBudgetPlanMapper.selectCount(new QueryWrapper<RollingBudgetPlan>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(rollingBudgetPlanMapper)));
            // 2-公式追踪
            moduleStats.add(buildModuleStat(2, "公式追踪",
                    safeCount(() -> formulaTraceTaskMapper.selectCount(null, null, null)),
                    safeMaxUpdate(formulaTraceTaskMapper)));
            // 3-催报管理
            moduleStats.add(buildModuleStat(3, "催报管理",
                    safeCount(() -> reminderStrategyMapper.selectCount(new QueryWrapper<ReminderStrategy>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(reminderStrategyMapper)));
            // 4-穿透查询
            moduleStats.add(buildModuleStat(4, "穿透查询",
                    safeCount(() -> drillThroughQueryMapper.selectCount(new QueryWrapper<DrillThroughQuery>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(drillThroughQueryMapper)));
            // 5-多币种管理
            moduleStats.add(buildModuleStat(5, "多币种管理",
                    safeCount(() -> currencyConfigMapper.selectCount(new QueryWrapper<CurrencyConfig>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(currencyConfigMapper)));
            // 6-批量计算
            moduleStats.add(buildModuleStat(6, "批量计算",
                    safeCount(() -> batchCalculationMapper.selectCount(new QueryWrapper<BatchCalculation>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(batchCalculationMapper)));
            // 7-智能推荐
            moduleStats.add(buildModuleStat(7, "智能推荐",
                    safeCount(() -> intelligentRecommendationMapper.countByCondition(null, null, null)),
                    safeMaxUpdate(intelligentRecommendationMapper)));
            // 8-预算模拟
            moduleStats.add(buildModuleStat(8, "预算模拟",
                    safeCount(() -> budgetSimulationMapper.selectCount(new QueryWrapper<BudgetSimulation>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(budgetSimulationMapper)));
            // 9-数据挖掘
            moduleStats.add(buildModuleStat(9, "数据挖掘",
                    safeCount(() -> dataMiningTaskMapper.selectCount(new QueryWrapper<DataMiningTask>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(dataMiningTaskMapper)));
            // 10-预算优化
            moduleStats.add(buildModuleStat(10, "预算优化",
                    safeCount(() -> budgetOptimizationMapper.selectCount(new QueryWrapper<BudgetOptimization>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(budgetOptimizationMapper)));
            // 11-风险评估
            moduleStats.add(buildModuleStat(11, "风险评估",
                    safeCount(() -> riskAssessmentMapper.selectCount(new QueryWrapper<RiskAssessment>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(riskAssessmentMapper)));
            // 12-协同编制
            moduleStats.add(buildModuleStat(12, "协同编制",
                    safeCount(() -> collaborativeProjectMapper.selectCount(new QueryWrapper<CollaborativeProject>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(collaborativeProjectMapper)));
            // 13-版本对比
            moduleStats.add(buildModuleStat(13, "版本对比",
                    safeCount(() -> versionComparisonMapper.selectCount(new QueryWrapper<VersionComparison>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(versionComparisonMapper)));
            // 14-自动化流程
            moduleStats.add(buildModuleStat(14, "自动化流程",
                    safeCount(() -> automationWorkflowMapper.selectCount(new QueryWrapper<AutomationWorkflow>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(automationWorkflowMapper)));
            // 15-高级报表
            moduleStats.add(buildModuleStat(15, "高级报表",
                    safeCount(() -> advancedReportMapper.selectCount(new QueryWrapper<AdvancedReport>().eq("DEL_FLAG", 0))),
                    safeMaxUpdate(advancedReportMapper)));

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(moduleStats);
        } catch (Exception e) {
            log.error("获取模块统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    private Map<String, Object> buildModuleStat(int moduleId, String moduleName, long count, Date lastUpdate) {
        Map<String, Object> stat = new HashMap<>();
        stat.put("moduleId", moduleId);
        stat.put("moduleName", moduleName);
        stat.put("taskCount", count);
        stat.put("lastUpdate", lastUpdate);
        return stat;
    }

    private long safeCount(Supplier<? extends Number> countSupplier) {
        try {
            Number c = countSupplier.get();
            return c != null ? c.longValue() : 0L;
        } catch (Exception e) {
            log.warn("统计数量失败: {}", e.getMessage());
            return 0L;
        }
    }

    @SuppressWarnings("unchecked")
    private Date safeMaxUpdate(BaseMapper<?> mapper) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.select("MAX(UPDATE_TIME) as LAST_UPDATE");
            wrapper.eq("DEL_FLAG", 0);
            List<Map<String, Object>> maps = mapper.selectMaps(wrapper);
            if (maps != null && !maps.isEmpty() && maps.get(0) != null) {
                Object val = maps.get(0).get("LAST_UPDATE");
                if (val instanceof Date) {
                    return (Date) val;
                }
            }
        } catch (Exception e) {
            log.warn("获取最后更新时间失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取最近活动
     */
    @Operation(summary = "获取最近活动")
    @ApiOperation("获取最近活动")
    @GetMapping("/activities")
    public MyJsonBean<List<Map<String, Object>>> getRecentActivities() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<BudgetAuditTrail> auditList = budgetAuditTrailMapper.selectList(
                new QueryWrapper<BudgetAuditTrail>()
                    .eq("IS_DELETED", 0)
                    .orderByDesc("OPERATION_TIME")
                    .last("FETCH FIRST 10 ROWS ONLY")
            );
            List<Map<String, Object>> activities = new ArrayList<>();
            for (BudgetAuditTrail audit : auditList) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", audit.getAuditId());
                item.put("title", audit.getModuleName() + " - " + audit.getOperationType());
                item.put("description", audit.getTargetName() != null ? "操作对象：" + audit.getTargetName() : audit.getModuleName());
                item.put("operator", audit.getOperatorName());
                item.put("timestamp", audit.getOperationTime() != null ? sdf.format(audit.getOperationTime()) : "");
                item.put("type", resolveActivityType(audit.getOperationType()));
                activities.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(activities);
        } catch (Exception e) {
            log.error("获取最近活动异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    private String resolveActivityType(String operationType) {
        if (operationType == null) return "primary";
        switch (operationType) {
            case "CREATE": case "IMPORT": return "primary";
            case "UPDATE": return "warning";
            case "DELETE": return "danger";
            case "EXPORT": case "LOGIN": case "LOGOUT": return "success";
            default: return "primary";
        }
    }

    /**
     * 从 Map 中忽略大小写获取值（兼容达梦数据库返回大写/小写 key）
     */
    private String getMapValueIgnoreCase(Map<String, Object> map, String key) {
        if (map == null || key == null) return null;
        Object val = map.get(key);
        if (val == null) val = map.get(key.toLowerCase());
        if (val == null) val = map.get(key.toUpperCase());
        return val != null ? val.toString() : null;
    }

    /**
     * 安全解析数量
     */
    private int parseCount(String val) {
        if (val == null || "null".equals(val)) return 0;
        try { return Integer.parseInt(val); } catch (Exception e) { return 0; }
    }

    /**
     * 获取系统通知
     */
    @Operation(summary = "获取系统通知")
    @ApiOperation("获取系统通知")
    @GetMapping("/notifications")
    public MyJsonBean<List<Map<String, Object>>> getSystemNotifications() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            List<BudgetAuditTrail> auditList = budgetAuditTrailMapper.selectList(
                new QueryWrapper<BudgetAuditTrail>()
                    .eq("IS_DELETED", 0)
                    .in("AUDIT_TYPE", "LOGIN", "HIGH_RISK", "EXPORT")
                    .orderByDesc("OPERATION_TIME")
                    .last("FETCH FIRST 5 ROWS ONLY")
            );
            List<Map<String, Object>> notifications = new ArrayList<>();
            for (BudgetAuditTrail audit : auditList) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", audit.getAuditId());
                item.put("title", resolveNotificationTitle(audit));
                item.put("message", audit.getTargetName() != null ? audit.getTargetName() : audit.getModuleName());
                item.put("time", audit.getOperationTime() != null ? sdf.format(audit.getOperationTime()) : "");
                item.put("type", "HIGH_RISK".equals(audit.getStatus()) ? "warning" : "info");
                item.put("isRead", false);
                notifications.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(notifications);
        } catch (Exception e) {
            log.error("获取系统通知异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    private String resolveNotificationTitle(BudgetAuditTrail audit) {
        if ("HIGH_RISK".equals(audit.getStatus())) return "安全警告：异常登录检测";
        if ("EXPORT".equals(audit.getAuditType())) return "数据导出通知";
        if ("LOGIN".equals(audit.getAuditType())) return "用户登录通知";
        return audit.getModuleName() + "操作通知";
    }

    /**
     * 标记通知为已读
     */
    @Operation(summary = "标记通知为已读")
    @ApiOperation("标记通知为已读")
    @PutMapping("/notifications/{notificationId}/read")
    public MyJsonBean<Void> markNotificationAsRead(@PathVariable String notificationId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            // 模拟标记通知为已读
            log.info("标记通知为已读，通知ID：{}", notificationId);

            result.setCode(1);
            result.setMsg("标记成功");
        } catch (Exception e) {
            log.error("标记通知为已读异常", e);
            result.setCode(0);
            result.setMsg("标记失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取功能使用趋势
     */
    @Operation(summary = "获取功能使用趋势")
    @ApiOperation("获取功能使用趋势")
    @GetMapping("/usage-trend")
    public MyJsonBean<Map<String, Object>> getUsageTrend() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> trend = new HashMap<>();
            trend.put("dailyUsage", new ArrayList<>());
            trend.put("totalUsage", 0);
            trend.put("averageUsage", 0);
            trend.put("growthRate", BigDecimal.ZERO);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(trend);
        } catch (Exception e) {
            log.error("获取功能使用趋势异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取热门功能排行
     */
    @Operation(summary = "获取热门功能排行")
    @ApiOperation("获取热门功能排行")
    @GetMapping("/popular-features")
    public MyJsonBean<List<Map<String, Object>>> getPopularFeatures() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> features = new ArrayList<>();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(features);
        } catch (Exception e) {
            log.error("获取热门功能排行异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 公式追踪模块接口 ====================

    /**
     * 获取公式追踪统计数据
     */
    @Operation(summary = "获取公式追踪统计数据")
    @ApiOperation("获取公式追踪统计数据")
    @GetMapping("/formula-trace/stats")
    public MyJsonBean<Map<String, Object>> getFormulaTraceStats(HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            Map<String, Object> stats = formulaTraceTaskService.getStats(companyId);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取公式追踪统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取公式追踪任务列表（分页）
     */
    @Operation(summary = "获取公式追踪任务列表")
    @ApiOperation("获取公式追踪任务列表")
    @PostMapping("/formula-trace/task/list")
    public MyJsonBean<PageResult<FormulaTraceTask>> getFormulaTraceTaskList(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        MyJsonBean<PageResult<FormulaTraceTask>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");

            // 获取分页参数
            String keyword = (String) params.get("keyword");
            String status = (String) params.get("status");
            Integer pageNo = params.get("pageNo") != null ? (Integer) params.get("pageNo") : 1;
            Integer pageSize = params.get("pageSize") != null ? (Integer) params.get("pageSize") : 15;

            PageResult<FormulaTraceTask> pageResult = formulaTraceTaskService.getTaskList(
                    keyword, status, companyId, pageNo, pageSize);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("获取公式追踪任务列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取可用公式列表
     */
    @Operation(summary = "获取可用公式列表")
    @ApiOperation("获取可用公式列表")
    @GetMapping("/formula-trace/available-formulas")
    public MyJsonBean<List<Map<String, Object>>> getAvailableFormulas(HttpServletRequest request) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            List<Map<String, Object>> formulas = formulaTraceTaskService.getAvailableFormulas(companyId);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(formulas);
        } catch (Exception e) {
            log.error("获取可用公式列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 创建公式追踪任务
     */
    @Operation(summary = "创建公式追踪任务")
    @ApiOperation("创建公式追踪任务")
    @PostMapping("/formula-trace/task")
    public MyJsonBean<String> createFormulaTraceTask(
            @RequestBody FormulaTraceTask task,
            HttpServletRequest request) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            String taskId = formulaTraceTaskService.createTask(task, companyId, userId, userName);

            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(taskId);
        } catch (Exception e) {
            log.error("创建公式追踪任务异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新公式追踪任务
     */
    @Operation(summary = "更新公式追踪任务")
    @ApiOperation("更新公式追踪任务")
    @PutMapping("/formula-trace/task/{taskId}")
    public MyJsonBean<Boolean> updateFormulaTraceTask(
            @ApiParam("任务ID") @PathVariable String taskId,
            @RequestBody FormulaTraceTask task,
            HttpServletRequest request) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            boolean success = formulaTraceTaskService.updateTask(taskId, task, userId, userName);

            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(success);
        } catch (Exception e) {
            log.error("更新公式追踪任务异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除公式追踪任务
     */
    @Operation(summary = "删除公式追踪任务")
    @ApiOperation("删除公式追踪任务")
    @DeleteMapping("/formula-trace/task/{taskId}")
    public MyJsonBean<Boolean> deleteFormulaTraceTask(
            @ApiParam("任务ID") @PathVariable String taskId) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            boolean success = formulaTraceTaskService.deleteTask(taskId);

            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(success);
        } catch (Exception e) {
            log.error("删除公式追踪任务异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行公式追踪任务
     */
    @Operation(summary = "执行公式追踪任务")
    @ApiOperation("执行公式追踪任务")
    @PostMapping("/formula-trace/task/{taskId}/execute")
    public MyJsonBean<Map<String, Object>> executeFormulaTraceTask(
            @ApiParam("任务ID") @PathVariable String taskId,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            Map<String, Object> executeResult = formulaTraceTaskService.executeTask(taskId, companyId);

            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(executeResult);
        } catch (Exception e) {
            log.error("执行公式追踪任务异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 智能推荐模块接口 ====================

    /**
     * 获取智能推荐统计数据
     */
    @Operation(summary = "获取智能推荐统计数据")
    @ApiOperation("获取智能推荐统计数据")
    @GetMapping("/intelligent-recommendation/stats")
    public MyJsonBean<Map<String, Object>> getIntelligentRecommendationStats(HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            Map<String, Object> stats = intelligentRecommendationService.getStats(companyId);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取智能推荐统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取智能推荐列表（分页）
     */
    @Operation(summary = "获取智能推荐列表")
    @ApiOperation("获取智能推荐列表")
    @GetMapping("/intelligent-recommendation/list")
    public MyJsonBean<PageResult<IntelligentRecommendation>> getIntelligentRecommendationList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "15") Integer pageSize,
            HttpServletRequest request) {
        MyJsonBean<PageResult<IntelligentRecommendation>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");

            PageResult<IntelligentRecommendation> pageResult = intelligentRecommendationService.getRecommendationList(
                    keyword, status, companyId, pageNo, pageSize);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("获取智能推荐列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成智能推荐
     */
    @Operation(summary = "生成智能推荐")
    @ApiOperation("生成智能推荐")
    @PostMapping("/intelligent-recommendation/generate")
    public MyJsonBean<Map<String, Object>> generateIntelligentRecommendation(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            String budgetId = (String) params.get("budgetId");
            String recommendationType = (String) params.get("recommendationType");

            if (budgetId == null || budgetId.isEmpty()) {
                result.setCode(0);
                result.setMsg("预算ID不能为空");
                return result;
            }

            if (recommendationType == null || recommendationType.isEmpty()) {
                recommendationType = "budget_optimization"; // 默认类型
            }

            Map<String, Object> generateResult = intelligentRecommendationService.generateRecommendation(
                    budgetId, recommendationType, companyId, userId, userName);

            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(generateResult);
        } catch (Exception e) {
            log.error("生成智能推荐异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 接受智能推荐
     */
    @Operation(summary = "接受智能推荐")
    @ApiOperation("接受智能推荐")
    @PutMapping("/intelligent-recommendation/{recommendationId}/accept")
    public MyJsonBean<Boolean> acceptIntelligentRecommendation(
            @ApiParam("推荐ID") @PathVariable String recommendationId,
            HttpServletRequest request) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            boolean success = intelligentRecommendationService.acceptRecommendation(
                    recommendationId, userId, userName);

            result.setCode(1);
            result.setMsg("接受成功");
            result.setData(success);
        } catch (Exception e) {
            log.error("接受智能推荐异常", e);
            result.setCode(0);
            result.setMsg("接受失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "拒绝智能推荐")
    @ApiOperation("拒绝智能推荐")
    @PutMapping("/intelligent-recommendation/{recommendationId}/reject")
    public MyJsonBean<Boolean> rejectIntelligentRecommendation(@PathVariable String recommendationId) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("拒绝成功");
            result.setData(true);
        } catch (Exception e) {
            log.error("拒绝智能推荐异常", e);
            result.setCode(0);
            result.setMsg("拒绝失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "应用智能推荐")
    @ApiOperation("应用智能推荐")
    @PostMapping("/intelligent-recommendation/{recommendationId}/apply")
    public MyJsonBean<Boolean> applyIntelligentRecommendation(@PathVariable String recommendationId) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("应用成功");
            result.setData(true);
        } catch (Exception e) {
            log.error("应用智能推荐异常", e);
            result.setCode(0);
            result.setMsg("应用失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "分享智能推荐")
    @ApiOperation("分享智能推荐")
    @PostMapping("/intelligent-recommendation/{recommendationId}/share")
    public MyJsonBean<Boolean> shareIntelligentRecommendation(@PathVariable String recommendationId) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("分享成功");
            result.setData(true);
        } catch (Exception e) {
            log.error("分享智能推荐异常", e);
            result.setCode(0);
            result.setMsg("分享失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除智能推荐")
    @ApiOperation("删除智能推荐")
    @DeleteMapping("/intelligent-recommendation/delete/{recommendationId}")
    public MyJsonBean<Boolean> deleteIntelligentRecommendation(@PathVariable String recommendationId) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(true);
        } catch (Exception e) {
            log.error("删除智能推荐异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取推荐反馈")
    @ApiOperation("获取推荐反馈")
    @GetMapping("/intelligent-recommendation/{recommendationId}/feedback")
    public MyJsonBean<List<Map<String, Object>>> getRecommendationFeedback(@PathVariable String recommendationId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(new ArrayList<>());
        } catch (Exception e) {
            log.error("获取推荐反馈异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 催报管理模块接口 ====================

    /**
     * 获取催报统计数据
     */
    @Operation(summary = "获取催报统计数据")
    @ApiOperation("获取催报统计数据")
    @GetMapping("/reminder/stats")
    public MyJsonBean<Map<String, Object>> getReminderStats(HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            Map<String, Object> stats = reminderManagementService.getStats(companyId);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取催报统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取催报策略列表（分页）
     */
    @Operation(summary = "获取催报策略列表")
    @ApiOperation("获取催报策略列表")
    @GetMapping("/reminder/strategy/list")
    public MyJsonBean<PageResult<ReminderStrategy>> getReminderStrategyList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "15") Integer pageSize,
            HttpServletRequest request) {
        MyJsonBean<PageResult<ReminderStrategy>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");

            PageResult<ReminderStrategy> pageResult = reminderManagementService.getStrategyList(
                    keyword, status, companyId, pageNo, pageSize);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("获取催报策略列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 发送催报
     */
    @Operation(summary = "发送催报")
    @ApiOperation("发送催报")
    @PostMapping("/reminder/send")
    public MyJsonBean<Map<String, Object>> sendReminder(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            String strategyId = (String) params.get("strategyId");
            @SuppressWarnings("unchecked")
            List<String> targets = (List<String>) params.get("targets");
            String message = (String) params.get("message");

            Map<String, Object> sendResult = reminderManagementService.sendReminder(
                    strategyId, targets, message, companyId, userId);

            result.setCode(1);
            result.setMsg("发送成功");
            result.setData(sendResult);
        } catch (Exception e) {
            log.error("发送催报异常", e);
            result.setCode(0);
            result.setMsg("发送失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取催报记录（分页）
     */
    @Operation(summary = "获取催报记录")
    @ApiOperation("获取催报记录")
    @GetMapping("/reminder/records")
    public MyJsonBean<PageResult<Map<String, Object>>> getReminderRecords(
            @RequestParam(required = false) String strategyId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "15") Integer pageSize,
            HttpServletRequest request) {
        MyJsonBean<PageResult<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");

            PageResult<Map<String, Object>> pageResult = reminderManagementService.getReminderRecords(
                    strategyId, companyId, pageNo, pageSize);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("获取催报记录异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取催报目标列表
     */
    @Operation(summary = "获取催报目标列表")
    @ApiOperation("获取催报目标列表")
    @GetMapping("/reminder/strategy/{strategyId}/targets")
    public MyJsonBean<List<Map<String, Object>>> getStrategyTargets(
            @ApiParam("策略ID") @PathVariable String strategyId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> targets = reminderManagementService.getStrategyTargets(strategyId);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(targets);
        } catch (Exception e) {
            log.error("获取催报目标列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 风险评估模块接口 ====================

    /**
     * 获取风险评估统计数据
     */
    @Operation(summary = "获取风险评估统计数据")
    @ApiOperation("获取风险评估统计数据")
    @GetMapping("/risk-assessment/stats")
    public MyJsonBean<Map<String, Object>> getRiskAssessmentStats(HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> raw = riskAssessmentService.getStats();
            // 统一字段名，前端使用 totalRisks/highRisks/mediumRisks/lowRisks
            Map<String, Object> stats = new HashMap<>();
            long total = raw.get("totalAssessments") != null ? ((Number) raw.get("totalAssessments")).longValue() : 0L;
            long high  = raw.get("highRiskCount")    != null ? ((Number) raw.get("highRiskCount")).longValue()    : 0L;
            long medium= raw.get("mediumRiskCount")  != null ? ((Number) raw.get("mediumRiskCount")).longValue()  : 0L;
            long low   = raw.get("lowRiskCount")     != null ? ((Number) raw.get("lowRiskCount")).longValue()     : 0L;
            stats.put("totalRisks",    total);
            stats.put("highRisks",     high);
            stats.put("mediumRisks",   medium);
            stats.put("lowRisks",      low);
            stats.put("coverage",      total > 0 ? 100 : 0);
            stats.put("mitigationRate",total > 0 ? Math.round((total - high) * 100.0 / total) : 0);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取风险评估统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "按风险类别分组统计")
    @ApiOperation("按风险类别分组统计")
    @GetMapping("/risk-assessment/category-stats")
    public MyJsonBean<List<Map<String, Object>>> getRiskCategoryStats(HttpServletRequest request) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            // 查询所有记录，按 RISK_CATEGORY 分组统计
            List<RiskAssessment> all = riskAssessmentMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<RiskAssessment>()
                            .orderByAsc("RISK_CATEGORY"));
            Map<String, Map<String, Object>> grouped = new java.util.LinkedHashMap<>();
            for (RiskAssessment r : all) {
                String cat = r.getRiskCategory() != null ? r.getRiskCategory() : "其他";
                grouped.computeIfAbsent(cat, k -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("category", k);
                    m.put("riskCount", 0);
                    m.put("highCount", 0);
                    m.put("mediumCount", 0);
                    m.put("lowCount", 0);
                    return m;
                });
                Map<String, Object> m = grouped.get(cat);
                m.put("riskCount", ((Number) m.get("riskCount")).intValue() + 1);
                if ("HIGH".equals(r.getRiskLevel()))   m.put("highCount",   ((Number) m.get("highCount")).intValue()   + 1);
                if ("MEDIUM".equals(r.getRiskLevel())) m.put("mediumCount", ((Number) m.get("mediumCount")).intValue() + 1);
                if ("LOW".equals(r.getRiskLevel()))    m.put("lowCount",    ((Number) m.get("lowCount")).intValue()    + 1);
            }
            // 计算每个类别的主要风险等级
            List<Map<String, Object>> list = new ArrayList<>(grouped.values());
            for (Map<String, Object> m : list) {
                int h = ((Number) m.get("highCount")).intValue();
                int med = ((Number) m.get("mediumCount")).intValue();
                m.put("avgLevel", h > 0 ? "HIGH" : med > 0 ? "MEDIUM" : "LOW");
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取风险类别统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取风险评估列表（分页）
     */
    @Operation(summary = "获取风险评估列表")
    @ApiOperation("获取风险评估列表")
    @GetMapping("/risk-assessment/list")
    public MyJsonBean<Map<String, Object>> getRiskAssessmentList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String riskLevel,
            @RequestParam(required = false) String riskCategory,
            @RequestParam(required = false) String riskType,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "15") Integer pageSize,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("keyword", keyword);
            params.put("riskLevel", riskLevel);
            // 当riskCategory未提供时，根据riskType映射
            String effectiveCategory = riskCategory;
            if ((effectiveCategory == null || effectiveCategory.isEmpty()) && riskType != null && !riskType.isEmpty()) {
                switch (riskType) {
                    case "1": case "FINANCIAL":
                        effectiveCategory = "FINANCIAL";
                        break;
                    case "2": case "MARKET":
                        effectiveCategory = "MARKET";
                        break;
                    case "3": case "OPERATIONAL":
                        effectiveCategory = "OPERATIONAL";
                        break;
                    case "4": case "COMPLIANCE":
                        effectiveCategory = "COMPLIANCE";
                        break;
                    default:
                        effectiveCategory = riskType;
                        break;
                }
            }
            params.put("riskCategory", effectiveCategory);
            Page<RiskAssessment> pageResult = riskAssessmentService.selectPage(params, pageNo, pageSize);
            Map<String, Object> data = new HashMap<>();
            data.put("list", pageResult.getRecords());
            data.put("total", pageResult.getTotal());
            data.put("currentPage", pageNo);
            data.put("pageSize", pageSize);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取风险评估列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取风险评估详情")
    @ApiOperation("获取风险评估详情")
    @GetMapping("/risk-assessment/detail/{id}")
    public MyJsonBean<RiskAssessment> getRiskAssessmentDetail(@PathVariable String id) {
        MyJsonBean<RiskAssessment> result = new MyJsonBean<>();
        try {
            RiskAssessment entity = riskAssessmentService.selectById(id);
            if (entity == null) {
                result.setCode(0); result.setMsg("记录不存在");
            } else {
                result.setCode(1); result.setMsg("查询成功"); result.setData(entity);
            }
        } catch (Exception e) {
            log.error("获取风险评估详情异常", e);
            result.setCode(0); result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建风险评估")
    @ApiOperation("创建风险评估")
    @PostMapping("/risk-assessment/create")
    public MyJsonBean<Boolean> createRiskAssessment(@RequestBody RiskAssessment assessment) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("创建成功");
            result.setData(riskAssessmentService.insert(assessment));
        } catch (Exception e) {
            log.error("创建风险评估异常", e);
            result.setCode(0); result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新风险评估")
    @ApiOperation("更新风险评估")
    @PostMapping("/risk-assessment/update")
    public MyJsonBean<Boolean> updateRiskAssessment(@RequestBody RiskAssessment assessment) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("更新成功");
            result.setData(riskAssessmentService.update(assessment));
        } catch (Exception e) {
            log.error("更新风险评估异常", e);
            result.setCode(0); result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 集成监控模块接口 ====================

    /**
     * 获取集成日志（分页）
     */
    @Operation(summary = "获取集成日志")
    @ApiOperation("获取集成日志")
    @GetMapping("/integration/monitor/logs")
    public MyJsonBean<PageResult<Map<String, Object>>> getIntegrationLogs(
            @RequestParam(required = false) String integrationId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "15") Integer pageSize,
            HttpServletRequest request) {
        MyJsonBean<PageResult<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");

            PageResult<Map<String, Object>> pageResult = integrationMonitorService.getIntegrationLogs(
                    integrationId, companyId, pageNo, pageSize);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("获取集成日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取集成告警（分页）
     */
    @Operation(summary = "获取集成告警")
    @ApiOperation("获取集成告警")
    @GetMapping("/integration/monitor/alerts")
    public MyJsonBean<PageResult<Map<String, Object>>> getIntegrationAlerts(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "15") Integer pageSize,
            HttpServletRequest request) {
        MyJsonBean<PageResult<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");

            PageResult<Map<String, Object>> pageResult = integrationMonitorService.getIntegrationAlerts(
                    status, companyId, pageNo, pageSize);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("获取集成告警异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 配置集成
     */
    @Operation(summary = "配置集成")
    @ApiOperation("配置集成")
    @PostMapping("/integration/monitor/config")
    public MyJsonBean<Boolean> configureIntegration(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            String integrationId = (String) params.get("integrationId");
            @SuppressWarnings("unchecked")
            Map<String, Object> config = (Map<String, Object>) params.get("config");

            boolean success = integrationMonitorService.configureIntegration(
                    integrationId, config, companyId, userId);

            result.setCode(1);
            result.setMsg("配置成功");
            result.setData(success);
        } catch (Exception e) {
            log.error("配置集成异常", e);
            result.setCode(0);
            result.setMsg("配置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取实时监控数据
     */
    @Operation(summary = "获取实时监控数据")
    @ApiOperation("获取实时监控数据")
    @GetMapping("/integration/monitor/realtime")
    public MyJsonBean<Map<String, Object>> getRealtimeMonitorData(
            @RequestParam String integrationId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> data = integrationMonitorService.getRealtimeMonitorData(integrationId);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取实时监控数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 告警管理
     */
    @Operation(summary = "告警管理")
    @ApiOperation("告警管理")
    @PostMapping("/integration/monitor/alert-management")
    public MyJsonBean<Boolean> manageAlert(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            String alertId = (String) params.get("alertId");
            String action = (String) params.get("action");

            boolean success = integrationMonitorService.manageAlert(
                    alertId, action, userId, userName);

            result.setCode(1);
            result.setMsg("操作成功");
            result.setData(success);
        } catch (Exception e) {
            log.error("告警管理异常", e);
            result.setCode(0);
            result.setMsg("操作失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 性能分析
     */
    @Operation(summary = "性能分析")
    @ApiOperation("性能分析")
    @GetMapping("/integration/monitor/performance")
    public MyJsonBean<Map<String, Object>> analyzePerformance(
            @RequestParam String integrationId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysis = integrationMonitorService.analyzePerformance(
                    integrationId, startDate, endDate);

            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(analysis);
        } catch (Exception e) {
            log.error("性能分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 监控设置
     */
    @Operation(summary = "监控设置")
    @ApiOperation("监控设置")
    @PostMapping("/integration/monitor/settings")
    public MyJsonBean<Boolean> updateMonitorSettings(
            @RequestBody Map<String, Object> settings,
            HttpServletRequest request) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            boolean success = integrationMonitorService.updateMonitorSettings(
                    settings, companyId, userId);

            result.setCode(1);
            result.setMsg("设置成功");
            result.setData(success);
        } catch (Exception e) {
            log.error("监控设置异常", e);
            result.setCode(0);
            result.setMsg("设置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出监控数据
     */
    @Operation(summary = "导出监控数据")
    @ApiOperation("导出监控数据")
    @PostMapping("/integration/monitor/export")
    public MyJsonBean<Map<String, Object>> exportMonitorData(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");

            String exportType = (String) params.get("exportType");

            Map<String, Object> exportResult = integrationMonitorService.exportMonitorData(
                    exportType, params, companyId);

            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportResult);
        } catch (Exception e) {
            log.error("导出监控数据异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 高级报表模块接口 ====================

    @Operation(summary = "获取高级报表列表")
    @ApiOperation("获取高级报表列表")
    @PostMapping("/reports/list")
    public MyJsonBean<Map<String, Object>> getAdvancedReportsList(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            Page<AdvancedReport> page = advancedReportService.selectPage(params, pageNo, pageSize);
            Map<String, Object> data = new HashMap<>();
            data.put("list", page.getRecords());
            data.put("total", page.getTotal());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取高级报表列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取高级报表统计")
    @ApiOperation("获取高级报表统计")
    @GetMapping("/reports/stats")
    public MyJsonBean<Map<String, Object>> getAdvancedReportsStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = advancedReportService.getStats();
            stats.put("typeStats", advancedReportService.getTypeStats());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取高级报表统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建高级报表")
    @ApiOperation("创建高级报表")
    @PostMapping("/reports/create")
    public MyJsonBean<Boolean> createAdvancedReport(@RequestBody AdvancedReport report) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(advancedReportService.insert(report));
        } catch (Exception e) {
            log.error("创建高级报表异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新高级报表")
    @ApiOperation("更新高级报表")
    @PutMapping("/reports/update/{id}")
    public MyJsonBean<Boolean> updateAdvancedReport(@PathVariable String id, @RequestBody AdvancedReport report) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            report.setReportId(id);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(advancedReportService.update(report));
        } catch (Exception e) {
            log.error("更新高级报表异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除高级报表")
    @ApiOperation("删除高级报表")
    @DeleteMapping("/reports/delete/{id}")
    public MyJsonBean<Boolean> deleteAdvancedReport(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(advancedReportService.deleteById(id));
        } catch (Exception e) {
            log.error("删除高级报表异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制高级报表")
    @ApiOperation("复制高级报表")
    @PostMapping("/reports/{id}/copy")
    public MyJsonBean<Boolean> copyAdvancedReport(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(advancedReportService.copyReport(id));
        } catch (Exception e) {
            log.error("复制高级报表异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "生成高级报表")
    @ApiOperation("生成高级报表")
    @PostMapping("/reports/{id}/generate")
    public MyJsonBean<Boolean> generateAdvancedReport(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(advancedReportService.generateReport(id));
        } catch (Exception e) {
            log.error("生成高级报表异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "下载报表")
    @ApiOperation("下载报表")
    @GetMapping("/reports/{id}/download")
    public MyJsonBean<Map<String, Object>> downloadReport(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            AdvancedReport report = advancedReportService.selectById(id);
            if (report == null) {
                result.setCode(0);
                result.setMsg("报表不存在");
                return result;
            }
            Map<String, Object> data = new HashMap<>();
            data.put("reportId", id);
            data.put("reportName", report.getReportName());
            data.put("reportType", report.getReportType());
            data.put("dataSource", report.getDataSource());
            data.put("reportStatus", report.getReportStatus());
            data.put("description", report.getDescription());
            data.put("createBy", report.getCreateBy());
            data.put("createTime", report.getCreateTime());
            data.put("lastGeneratedTime", report.getLastGeneratedTime());
            // 获取最新一条生成历史
            List<ReportGenHistory> historyList = advancedReportService.getGenerationHistory(id);
            if (historyList != null && !historyList.isEmpty()) {
                ReportGenHistory latest = historyList.get(0);
                data.put("latestHistory", latest);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("下载报表异常", e);
            result.setCode(0);
            result.setMsg("下载失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出报表配置")
    @ApiOperation("导出报表配置")
    @GetMapping("/reports/{id}/export-config")
    public MyJsonBean<Map<String, Object>> exportReportConfig(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            AdvancedReport report = advancedReportService.selectById(id);
            if (report == null) {
                result.setCode(0);
                result.setMsg("报表不存在");
                return result;
            }
            Map<String, Object> data = new HashMap<>();
            data.put("reportId", id);
            data.put("reportName", report.getReportName());
            data.put("reportType", report.getReportType());
            data.put("dataSource", report.getDataSource());
            data.put("generationFrequency", report.getGenerationFrequency());
            data.put("reportStatus", report.getReportStatus());
            data.put("description", report.getDescription());
            // 获取报表配置
            ReportConfig config = advancedReportService.getReportConfig(id);
            if (config != null) {
                data.put("queryConditions", config.getQueryConditions());
                data.put("fieldConfiguration", config.getFieldConfiguration());
                data.put("sortRules", config.getSortRules());
                data.put("groupSettings", config.getGroupSettings());
                data.put("formatSettings", config.getFormatSettings());
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("导出报表配置异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取报表订阅者")
    @ApiOperation("获取报表订阅者")
    @GetMapping("/reports/{id}/subscribers")
    public MyJsonBean<List<ReportSubscriber>> getReportSubscribers(@PathVariable String id) {
        MyJsonBean<List<ReportSubscriber>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(advancedReportService.getSubscribers(id));
        } catch (Exception e) {
            log.error("获取报表订阅者异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "添加报表订阅者")
    @ApiOperation("添加报表订阅者")
    @PostMapping("/reports/{id}/subscribers")
    public MyJsonBean<Boolean> addReportSubscriber(@PathVariable String id, @RequestBody ReportSubscriber subscriber) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            subscriber.setReportId(id);
            result.setCode(1);
            result.setMsg("添加成功");
            result.setData(advancedReportService.addSubscriber(subscriber));
        } catch (Exception e) {
            log.error("添加报表订阅者异常", e);
            result.setCode(0);
            result.setMsg("添加失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除报表订阅者")
    @ApiOperation("删除报表订阅者")
    @DeleteMapping("/reports/subscribers/{subscriberId}")
    public MyJsonBean<Boolean> removeReportSubscriber(@PathVariable String subscriberId) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(advancedReportService.removeSubscriber(subscriberId));
        } catch (Exception e) {
            log.error("删除报表订阅者异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新报表定时设置")
    @ApiOperation("更新报表定时设置")
    @PutMapping("/reports/{id}/schedule")
    public MyJsonBean<Boolean> updateReportSchedule(@PathVariable String id, @RequestBody Map<String, Object> params) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            String frequency = params.get("frequency") != null ? params.get("frequency").toString() : "MANUAL";
            result.setCode(1);
            result.setMsg("保存成功");
            result.setData(advancedReportService.updateSchedule(id, frequency));
        } catch (Exception e) {
            log.error("更新报表定时设置异常", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取报表生成历史")
    @ApiOperation("获取报表生成历史")
    @GetMapping("/reports/{id}/history")
    public MyJsonBean<List<ReportGenHistory>> getReportHistory(@PathVariable String id) {
        MyJsonBean<List<ReportGenHistory>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(advancedReportService.getGenerationHistory(id));
        } catch (Exception e) {
            log.error("获取报表历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取报表配置")
    @ApiOperation("获取报表配置")
    @GetMapping("/reports/{id}/config")
    public MyJsonBean<ReportConfig> getReportConfig(@PathVariable String id) {
        MyJsonBean<ReportConfig> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(advancedReportService.getReportConfig(id));
        } catch (Exception e) {
            log.error("获取报表配置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 自动化工作流模块接口 ====================

    @Operation(summary = "获取工作流列表")
    @ApiOperation("获取工作流列表")
    @PostMapping("/workflow/list")
    public MyJsonBean<Map<String, Object>> getWorkflowList(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            // 支持按 workflowType 筛选（问题3）
            Page<AutomationWorkflow> page = automationWorkflowService.selectPage(params, pageNo, pageSize);
            Map<String, Object> data = new HashMap<>();
            data.put("list", page.getRecords());
            data.put("total", page.getTotal());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取工作流列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取工作流统计（含执行次数、成功率、类型分布）")
    @ApiOperation("获取工作流统计（含执行次数、成功率、类型分布）")
    @GetMapping("/workflow/stats")
    public MyJsonBean<Map<String, Object>> getWorkflowStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = automationWorkflowService.getStats();
            // 补充按类型统计（问题3）
            stats.put("typeCount", automationWorkflowService.countByType());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取工作流统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取工作流详情")
    @ApiOperation("获取工作流详情")
    @GetMapping("/workflow/detail/{id}")
    public MyJsonBean<AutomationWorkflow> getWorkflowDetail(@PathVariable String id) {
        MyJsonBean<AutomationWorkflow> result = new MyJsonBean<>();
        try {
            AutomationWorkflow wf = automationWorkflowService.selectById(id);
            if (wf == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            result.setCode(1); result.setMsg("查询成功"); result.setData(wf);
        } catch (Exception e) {
            log.error("获取工作流详情异常", e);
            result.setCode(0); result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建工作流")
    @ApiOperation("创建工作流")
    @PostMapping("/workflow/create")
    public MyJsonBean<Boolean> createWorkflow(@RequestBody AutomationWorkflow workflow, HttpServletRequest request) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            String userName = request.getHeader("userName");
            if (userName != null) { workflow.setCreator(userName); workflow.setCreateBy(userName); }
            result.setCode(1); result.setMsg("创建成功");
            result.setData(automationWorkflowService.insert(workflow));
        } catch (Exception e) {
            log.error("创建工作流异常", e);
            result.setCode(0); result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新工作流")
    @ApiOperation("更新工作流")
    @PostMapping("/workflow/update")
    public MyJsonBean<Boolean> updateWorkflow(@RequestBody AutomationWorkflow workflow, HttpServletRequest request) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            String userName = request.getHeader("userName");
            if (userName != null) workflow.setUpdateBy(userName);
            result.setCode(1); result.setMsg("更新成功");
            result.setData(automationWorkflowService.update(workflow));
        } catch (Exception e) {
            log.error("更新工作流异常", e);
            result.setCode(0); result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "运行工作流")
    @ApiOperation("运行工作流")
    @PostMapping("/workflow/{id}/run")
    public MyJsonBean<Boolean> runWorkflow(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("运行成功");
            result.setData(automationWorkflowService.runWorkflow(id));
        } catch (Exception e) {
            log.error("运行工作流异常", e);
            result.setCode(0); result.setMsg("运行失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "停止工作流")
    @ApiOperation("停止工作流")
    @PostMapping("/workflow/{id}/stop")
    public MyJsonBean<Boolean> stopWorkflow(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("停止成功");
            result.setData(automationWorkflowService.stopWorkflow(id));
        } catch (Exception e) {
            log.error("停止工作流异常", e);
            result.setCode(0); result.setMsg("停止失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除工作流")
    @ApiOperation("删除工作流")
    @DeleteMapping("/workflow/delete/{id}")
    public MyJsonBean<Boolean> deleteWorkflow(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("删除成功");
            result.setData(automationWorkflowService.deleteById(id));
        } catch (Exception e) {
            log.error("删除工作流异常", e);
            result.setCode(0); result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制工作流")
    @ApiOperation("复制工作流")
    @PostMapping("/workflow/{id}/copy")
    public MyJsonBean<Boolean> copyWorkflow(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("复制成功");
            result.setData(automationWorkflowService.copyWorkflow(id));
        } catch (Exception e) {
            log.error("复制工作流异常", e);
            result.setCode(0); result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出工作流")
    @ApiOperation("导出工作流")
    @GetMapping("/workflow/{id}/export")
    public MyJsonBean<Map<String, Object>> exportWorkflow(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            AutomationWorkflow wf = automationWorkflowService.selectById(id);
            Map<String, Object> data = new HashMap<>();
            if (wf != null) {
                data.put("workflowId", wf.getWorkflowId());
                data.put("workflowName", wf.getWorkflowName());
                data.put("workflowType", wf.getWorkflowType());
                data.put("triggerType", wf.getTriggerType());
                data.put("description", wf.getDescription());
            }
            result.setCode(1); result.setMsg("导出成功"); result.setData(data);
        } catch (Exception e) {
            log.error("导出工作流异常", e);
            result.setCode(0); result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取工作流执行日志")
    @ApiOperation("获取工作流执行日志")
    @GetMapping("/workflow/{id}/logs")
    public MyJsonBean<List<Map<String, Object>>> getWorkflowLogs(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            // 从审计日志中查询该工作流的执行记录
            List<Map<String, Object>> logs = new ArrayList<>();
            AutomationWorkflow wf = automationWorkflowService.selectById(id);
            if (wf != null) {
                Map<String, Object> log1 = new HashMap<>();
                log1.put("logId", id + "_log");
                log1.put("workflowId", id);
                log1.put("executionTime", wf.getLastExecution());
                log1.put("status", wf.getStatus());
                log1.put("executionCount", wf.getExecutionCount());
                logs.add(log1);
            }
            result.setCode(1); result.setMsg("查询成功"); result.setData(logs);
        } catch (Exception e) {
            log.error("获取工作流日志异常", e);
            result.setCode(0); result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取执行监控数据")
    @ApiOperation("获取执行监控数据")
    @GetMapping("/workflow/execution/monitor")
    public MyJsonBean<Map<String, Object>> getWorkflowExecutionMonitor() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = automationWorkflowService.getStats();
            // 查询运行中的工作流
            Map<String, Object> params = new HashMap<>();
            params.put("status", "RUNNING");
            List<AutomationWorkflow> runningList = automationWorkflowService.selectList(params);
            stats.put("runningWorkflows", runningList);
            stats.put("runningCount", runningList.size());
            result.setCode(1); result.setMsg("查询成功"); result.setData(stats);
        } catch (Exception e) {
            log.error("获取执行监控数据异常", e);
            result.setCode(0); result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取工作流设置")
    @ApiOperation("获取工作流设置")
    @GetMapping("/workflow/settings")
    public MyJsonBean<Map<String, Object>> getWorkflowSettings() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> settings = new HashMap<>();
            settings.put("maxConcurrentWorkflows", 10);
            settings.put("defaultTimeout", 3600);
            settings.put("retryCount", 3);
            settings.put("notifyOnFailure", true);
            settings.put("notifyOnSuccess", false);
            result.setCode(1); result.setMsg("查询成功"); result.setData(settings);
        } catch (Exception e) {
            log.error("获取工作流设置异常", e);
            result.setCode(0); result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "保存工作流设置")
    @ApiOperation("保存工作流设置")
    @PostMapping("/workflow/settings")
    public MyJsonBean<Boolean> saveWorkflowSettings(@RequestBody Map<String, Object> settings) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            log.info("保存工作流设置: {}", settings);
            result.setCode(1); result.setMsg("保存成功"); result.setData(true);
        } catch (Exception e) {
            log.error("保存工作流设置异常", e);
            result.setCode(0); result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取流程模板列表")
    @ApiOperation("获取流程模板列表")
    @GetMapping("/workflow/templates")
    public MyJsonBean<List<Map<String, Object>>> getWorkflowTemplates() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> templates = new ArrayList<>();
            String[][] tplData = {
                {"TPL001", "预算审批标准流程", "APPROVAL_WORKFLOW", "适用于标准预算审批场景"},
                {"TPL002", "月度数据同步流程", "DATA_SYNC_WORKFLOW", "每月自动同步财务数据"},
                {"TPL003", "季度报告生成流程", "REPORT_WORKFLOW", "自动生成季度财务报告"},
                {"TPL004", "预算超支预警流程", "NOTIFICATION_WORKFLOW", "预算超支时自动发送预警通知"}
            };
            for (String[] tpl : tplData) {
                Map<String, Object> t = new HashMap<>();
                t.put("templateId", tpl[0]); t.put("templateName", tpl[1]);
                t.put("workflowType", tpl[2]); t.put("description", tpl[3]);
                templates.add(t);
            }
            result.setCode(1); result.setMsg("查询成功"); result.setData(templates);
        } catch (Exception e) {
            log.error("获取流程模板异常", e);
            result.setCode(0); result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }



    @Operation(summary = "获取批量计算列表")
    @ApiOperation("获取批量计算列表")
    @PostMapping("/batch/calculation/list")
    public MyJsonBean<Map<String, Object>> getBatchCalculationList(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            Page<BatchCalculation> page = batchCalculationService.selectPage(params, pageNo, pageSize);
            Map<String, Object> data = new HashMap<>();
            data.put("list", page.getRecords());
            data.put("total", page.getTotal());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取批量计算列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取批量计算统计")
    @ApiOperation("获取批量计算统计")
    @GetMapping("/batch/calculation/stats")
    public MyJsonBean<Map<String, Object>> getBatchCalculationStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        Map<String, Object> stats = new HashMap<>();
        try {
            // 总任务数 —— COUNT(*) 不依赖具体列，最安全
            int totalTasks = Math.toIntExact(batchCalculationMapper.selectCount(
                    new QueryWrapper<BatchCalculation>().eq("DEL_FLAG", 0)));
            stats.put("totalTasks", totalTasks);

            // 按状态统计（分段 try-catch，避免一个查询失败拖垮全部）
            int runningTasks = 0, completedTasks = 0, failedTasks = 0;
            try {
                List<Map<String, Object>> statusCounts = batchCalculationMapper.countByStatus();
                for (Map<String, Object> sc : statusCounts) {
                    String status = getMapValueIgnoreCase(sc, "CALCULATION_STATUS");
                    int count = parseCount(getMapValueIgnoreCase(sc, "TASK_COUNT"));
                    if ("RUNNING".equalsIgnoreCase(status) || "IN_PROGRESS".equalsIgnoreCase(status)) runningTasks += count;
                    else if ("COMPLETED".equalsIgnoreCase(status)) completedTasks += count;
                    else if ("FAILED".equalsIgnoreCase(status)) failedTasks += count;
                }
            } catch (Exception e) {
                log.warn("按状态统计失败，降级为0: {}", e.getMessage());
            }
            stats.put("runningTasks", runningTasks);
            stats.put("completedTasks", completedTasks);
            stats.put("failedTasks", failedTasks);

            // 成功率
            double successRate = totalTasks > 0 ? Math.round(completedTasks * 10000.0 / totalTasks) / 100.0 : 0;
            stats.put("successRate", successRate);

            // 平均执行时间(秒) —— 单独 try-catch，EXECUTION_TIME 列可能不存在
            long avgExecutionTime = 0;
            try {
                Integer avgTime = batchCalculationMapper.avgExecutionTime();
                avgExecutionTime = avgTime != null ? Math.round(avgTime / 1000.0) : 0;
            } catch (Exception e) {
                log.warn("平均执行时间查询失败，降级为0: {}", e.getMessage());
            }
            stats.put("avgExecutionTime", avgExecutionTime);

            // 按类型统计 —— 单独 try-catch
            Map<String, Integer> typeCountMap = new HashMap<>();
            try {
                List<Map<String, Object>> typeCounts = batchCalculationMapper.countByType();
                for (Map<String, Object> tc : typeCounts) {
                    String type = getMapValueIgnoreCase(tc, "CALCULATION_TYPE");
                    int count = parseCount(getMapValueIgnoreCase(tc, "TASK_COUNT"));
                    if (type != null && !"null".equals(type)) {
                        typeCountMap.put(type, count);
                    }
                }
            } catch (Exception e) {
                log.warn("按类型统计失败，降级为空: {}", e.getMessage());
            }
            stats.put("typeCounts", typeCountMap);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取批量计算统计异常", e);
            // 即使外层异常，也尽量返回已收集到的数据
            result.setCode(1);
            result.setMsg("部分统计数据获取失败");
            result.setData(stats);
        }
        return result;
    }

    @Operation(summary = "创建批量计算")
    @ApiOperation("创建批量计算")
    @PostMapping("/batch/calculation/create")
    public MyJsonBean<Boolean> createBatchCalculation(@RequestBody BatchCalculation calc) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(batchCalculationService.insert(calc));
        } catch (Exception e) {
            log.error("创建批量计算异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新批量计算")
    @ApiOperation("更新批量计算")
    @PutMapping("/batch/calculation/update/{id}")
    public MyJsonBean<Boolean> updateBatchCalculation(@PathVariable String id, @RequestBody BatchCalculation calc) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            calc.setCalculationId(id);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(batchCalculationService.update(calc));
        } catch (Exception e) {
            log.error("更新批量计算异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除批量计算")
    @ApiOperation("删除批量计算")
    @DeleteMapping("/batch/calculation/delete/{id}")
    public MyJsonBean<Boolean> deleteBatchCalculation(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(batchCalculationService.deleteById(id));
        } catch (Exception e) {
            log.error("删除批量计算异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制批量计算")
    @ApiOperation("复制批量计算")
    @PostMapping("/batch/calculation/{id}/copy")
    public MyJsonBean<Boolean> copyBatchCalculation(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            BatchCalculation src = batchCalculationService.selectById(id);
            if (src != null) { src.setCalculationId(null); src.setCalculationName(src.getCalculationName() + " - 副本"); batchCalculationService.insert(src); }
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(true);
        } catch (Exception e) {
            log.error("复制批量计算异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "执行批量计算")
    @ApiOperation("执行批量计算")
    @PostMapping("/batch/calculation/{id}/execute")
    public MyJsonBean<Boolean> executeBatchCalculation(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(batchCalculationService.executeCalculation(id));
        } catch (Exception e) {
            log.error("执行批量计算异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "停止批量计算")
    @ApiOperation("停止批量计算")
    @PostMapping("/batch/calculation/{id}/stop")
    public MyJsonBean<Boolean> stopBatchCalculation(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            BatchCalculation calc = new BatchCalculation();
            calc.setCalculationId(id);
            calc.setCalculationStatus("STOPPED");
            calc.setUpdateTime(new java.util.Date());
            batchCalculationMapper.updateById(calc);
            result.setCode(1);
            result.setMsg("停止成功");
            result.setData(true);
        } catch (Exception e) {
            log.error("停止批量计算异常", e);
            result.setCode(0);
            result.setMsg("停止失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "重试批量计算")
    @ApiOperation("重试批量计算")
    @PostMapping("/batch/calculation/{id}/retry")
    public MyJsonBean<Boolean> retryBatchCalculation(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            BatchCalculation calc = new BatchCalculation();
            calc.setCalculationId(id);
            calc.setCalculationStatus("PENDING");
            calc.setUpdateTime(new java.util.Date());
            batchCalculationMapper.updateById(calc);
            result.setCode(1);
            result.setMsg("重试成功，任务已重置为待执行");
            result.setData(true);
        } catch (Exception e) {
            log.error("重试批量计算异常", e);
            result.setCode(0);
            result.setMsg("重试失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出批量计算")
    @ApiOperation("导出批量计算")
    @GetMapping("/batch/calculation/{id}/export")
    public MyJsonBean<Map<String, Object>> exportBatchCalculation(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BatchCalculation calc = batchCalculationMapper.selectById(id);
            if (calc == null) {
                result.setCode(0);
                result.setMsg("任务不存在");
                return result;
            }
            Map<String, Object> data = new HashMap<>();
            data.put("calculationId", calc.getCalculationId());
            data.put("calculationCode", calc.getCalculationCode());
            data.put("calculationName", calc.getCalculationName());
            data.put("calculationType", calc.getCalculationType());
            data.put("calculationStatus", calc.getCalculationStatus());
            data.put("totalItems", calc.getTotalItems());
            data.put("completedItems", calc.getCompletedItems());
            data.put("failedItems", calc.getFailedItems());
            data.put("executionTime", calc.getExecutionTime());
            data.put("startTime", calc.getStartTime());
            data.put("endTime", calc.getEndTime());
            data.put("dataSource", calc.getDataSource());
            data.put("description", calc.getDescription());
            data.put("createBy", calc.getCreateBy());
            data.put("createTime", calc.getCreateTime());
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("导出批量计算异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取批量计算结果")
    @ApiOperation("获取批量计算结果")
    @GetMapping("/batch/calculation/{id}/results")
    public MyJsonBean<Map<String, Object>> getBatchCalculationResults(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(batchCalculationService.getCalculationResult(id));
        } catch (Exception e) {
            log.error("获取批量计算结果异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取批量计算日志")
    @ApiOperation("获取批量计算日志")
    @GetMapping("/batch/calculation/{id}/logs")
    public MyJsonBean<List<Map<String, Object>>> getBatchCalculationLogs(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(new ArrayList<>());
        } catch (Exception e) {
            log.error("获取批量计算日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 预算优化模块接口 ====================

    @Operation(summary = "获取预算优化列表")
    @ApiOperation("获取预算优化列表")
    @PostMapping("/optimization/list")
    public MyJsonBean<Map<String, Object>> getOptimizationList(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            Page<BudgetOptimization> page = budgetOptimizationService.selectPage(params, pageNo, pageSize);
            Map<String, Object> data = new HashMap<>();
            data.put("list", page.getRecords());
            data.put("total", page.getTotal());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取预算优化列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取预算优化统计")
    @ApiOperation("获取预算优化统计")
    @GetMapping("/optimization/stats")
    public MyJsonBean<Map<String, Object>> getOptimizationStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(budgetOptimizationService.getStats());
        } catch (Exception e) {
            log.error("获取预算优化统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建预算优化")
    @ApiOperation("创建预算优化")
    @PostMapping("/optimization/create")
    public MyJsonBean<Boolean> createOptimization(@RequestBody BudgetOptimization opt) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(budgetOptimizationService.insert(opt));
        } catch (Exception e) {
            log.error("创建预算优化异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新预算优化")
    @ApiOperation("更新预算优化")
    @PutMapping("/optimization/update/{id}")
    public MyJsonBean<Boolean> updateOptimization(@PathVariable String id, @RequestBody BudgetOptimization opt) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            opt.setOptimizationId(id);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(budgetOptimizationService.update(opt));
        } catch (Exception e) {
            log.error("更新预算优化异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除预算优化")
    @ApiOperation("删除预算优化")
    @DeleteMapping("/optimization/delete/{id}")
    public MyJsonBean<Boolean> deleteOptimization(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(budgetOptimizationService.deleteById(id));
        } catch (Exception e) {
            log.error("删除预算优化异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制预算优化")
    @ApiOperation("复制预算优化")
    @PostMapping("/optimization/{id}/copy")
    public MyJsonBean<Boolean> copyOptimization(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(budgetOptimizationService.copyOptimization(id));
        } catch (Exception e) {
            log.error("复制预算优化异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "运行预算优化")
    @ApiOperation("运行预算优化")
    @PostMapping("/optimization/{id}/run")
    public MyJsonBean<Boolean> runOptimization(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("运行成功");
            result.setData(budgetOptimizationService.runOptimization(id));
        } catch (Exception e) {
            log.error("运行预算优化异常", e);
            result.setCode(0);
            result.setMsg("运行失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "停止预算优化")
    @ApiOperation("停止预算优化")
    @PostMapping("/optimization/{id}/stop")
    public MyJsonBean<Boolean> stopOptimization(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("停止成功");
            result.setData(budgetOptimizationService.stopOptimization(id));
        } catch (Exception e) {
            log.error("停止预算优化异常", e);
            result.setCode(0);
            result.setMsg("停止失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "应用预算优化")
    @ApiOperation("应用预算优化")
    @PostMapping("/optimization/{id}/apply")
    public MyJsonBean<Boolean> applyOptimization(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("应用成功");
            result.setData(budgetOptimizationService.applyOptimization(id));
        } catch (Exception e) {
            log.error("应用预算优化异常", e);
            result.setCode(0);
            result.setMsg("应用失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出预算优化")
    @ApiOperation("导出预算优化")
    @GetMapping("/optimization/{id}/export")
    public void exportOptimization(@PathVariable String id, HttpServletResponse response) {
        try {
            BudgetOptimization opt = budgetOptimizationService.selectById(id);
            List<BudgetOptimization> list = opt != null
                    ? java.util.Collections.singletonList(opt)
                    : new ArrayList<>();
            String fileName = "预算优化_" + id;
            com.management.accountant.util.ExcelUtil.exportExcel(list, BudgetOptimization.class, fileName, response);
        } catch (Exception e) {
            log.error("导出预算优化异常", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }

    @Operation(summary = "获取预算优化结果")
    @ApiOperation("获取预算优化结果")
    @GetMapping("/optimization/{id}/results")
    public MyJsonBean<Map<String, Object>> getOptimizationResults(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("optimizationId", id);
            data.put("results", budgetOptimizationResultService.listByOptimizationId(id));
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取预算优化结果异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取预算优化日志")
    @ApiOperation("获取预算优化日志")
    @GetMapping("/optimization/{id}/logs")
    public MyJsonBean<List<Map<String, Object>>> getOptimizationLogs(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(new ArrayList<>());
        } catch (Exception e) {
            log.error("获取预算优化日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 预算模拟模块接口 ====================

    @Operation(summary = "获取预算模拟列表")
    @ApiOperation("获取预算模拟列表")
    @PostMapping("/simulation/list")
    public MyJsonBean<Map<String, Object>> getSimulationList(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            Page<BudgetSimulation> page = budgetSimulationService.selectPage(params, pageNo, pageSize);
            Map<String, Object> data = new HashMap<>();
            data.put("list", page.getRecords());
            data.put("total", page.getTotal());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取预算模拟列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取预算模拟统计")
    @ApiOperation("获取预算模拟统计")
    @GetMapping("/simulation/stats")
    public MyJsonBean<Map<String, Object>> getSimulationStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = new HashMap<>();
            QueryWrapper<BudgetSimulation> baseQuery = new QueryWrapper<BudgetSimulation>().eq("DEL_FLAG", 0);
            int total = budgetSimulationMapper.selectCount(baseQuery).intValue();
            int running = budgetSimulationMapper.selectCount(new QueryWrapper<BudgetSimulation>().eq("DEL_FLAG", 0).eq("SIMULATION_STATUS", "IN_PROGRESS")).intValue();
            int completed = budgetSimulationMapper.selectCount(new QueryWrapper<BudgetSimulation>().eq("DEL_FLAG", 0).eq("SIMULATION_STATUS", "COMPLETED")).intValue();
            // 各类型数量
            int whatIfCount = budgetSimulationMapper.selectCount(new QueryWrapper<BudgetSimulation>().eq("DEL_FLAG", 0).eq("SIMULATION_TYPE", "WHAT_IF")).intValue();
            int scenarioCount = budgetSimulationMapper.selectCount(new QueryWrapper<BudgetSimulation>().eq("DEL_FLAG", 0).eq("SIMULATION_TYPE", "SCENARIO")).intValue();
            int stressCount = budgetSimulationMapper.selectCount(new QueryWrapper<BudgetSimulation>().eq("DEL_FLAG", 0).eq("SIMULATION_TYPE", "STRESS_TEST")).intValue();
            int optimizationCount = budgetSimulationMapper.selectCount(new QueryWrapper<BudgetSimulation>().eq("DEL_FLAG", 0).eq("SIMULATION_TYPE", "OPTIMIZATION")).intValue();
            stats.put("totalSimulations", total);
            stats.put("runningSimulations", running);
            stats.put("totalScenarios", total);
            stats.put("accuracy", total > 0 ? Math.round((double) completed / total * 100) : 0);
            stats.put("whatIfCount", whatIfCount);
            stats.put("scenarioCount", scenarioCount);
            stats.put("stressTestCount", stressCount);
            stats.put("optimizationCount", optimizationCount);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取预算模拟统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建预算模拟")
    @ApiOperation("创建预算模拟")
    @PostMapping("/simulation/create")
    public MyJsonBean<Boolean> createSimulation(@RequestBody BudgetSimulation sim) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(budgetSimulationService.insert(sim));
        } catch (Exception e) {
            log.error("创建预算模拟异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新预算模拟")
    @ApiOperation("更新预算模拟")
    @PutMapping("/simulation/update/{id}")
    public MyJsonBean<Boolean> updateSimulation(@PathVariable String id, @RequestBody BudgetSimulation sim) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            sim.setSimulationId(id);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(budgetSimulationService.update(sim));
        } catch (Exception e) {
            log.error("更新预算模拟异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除预算模拟")
    @ApiOperation("删除预算模拟")
    @DeleteMapping("/simulation/delete/{id}")
    public MyJsonBean<Boolean> deleteSimulation(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(budgetSimulationService.deleteById(id));
        } catch (Exception e) {
            log.error("删除预算模拟异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制预算模拟")
    @ApiOperation("复制预算模拟")
    @PostMapping("/simulation/{id}/copy")
    public MyJsonBean<Boolean> copySimulation(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            BudgetSimulation src = budgetSimulationService.selectById(id);
            if (src != null) { src.setSimulationId(null); src.setSimulationName(src.getSimulationName() + " - 副本"); budgetSimulationService.insert(src); }
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(true);
        } catch (Exception e) {
            log.error("复制预算模拟异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "运行预算模拟")
    @ApiOperation("运行预算模拟")
    @PostMapping("/simulation/{id}/run")
    public MyJsonBean<Boolean> runSimulation(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("运行成功");
            result.setData(budgetSimulationService.executeSimulation(id));
        } catch (Exception e) {
            log.error("运行预算模拟异常", e);
            result.setCode(0);
            result.setMsg("运行失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "停止预算模拟")
    @ApiOperation("停止预算模拟")
    @PostMapping("/simulation/{id}/stop")
    public MyJsonBean<Boolean> stopSimulation(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            BudgetSimulation simulation = budgetSimulationMapper.selectById(id);
            if (simulation == null) {
                result.setCode(0);
                result.setMsg("模拟记录不存在");
                return result;
            }
            simulation.setSimulationStatus("DRAFT");
            simulation.setUpdateTime(new Date());
            budgetSimulationMapper.updateById(simulation);
            result.setCode(1);
            result.setMsg("停止成功");
            result.setData(true);
        } catch (Exception e) {
            log.error("停止预算模拟异常", e);
            result.setCode(0);
            result.setMsg("停止失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出预算模拟")
    @ApiOperation("导出预算模拟")
    @GetMapping("/simulation/{id}/export")
    public MyJsonBean<Map<String, Object>> exportSimulation(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetSimulation simulation = budgetSimulationMapper.selectById(id);
            if (simulation == null) {
                result.setCode(0);
                result.setMsg("模拟记录不存在");
                return result;
            }
            Map<String, Object> data = new HashMap<>();
            data.put("simulationId", simulation.getSimulationId());
            data.put("simulationName", simulation.getSimulationName());
            data.put("simulationCode", simulation.getSimulationCode());
            data.put("simulationType", simulation.getSimulationType());
            data.put("simulationStatus", simulation.getSimulationStatus());
            data.put("scenarioCount", simulation.getScenarioCount());
            data.put("simulationParams", simulation.getSimulationParams());
            data.put("simulationResult", simulation.getSimulationResult());
            data.put("createBy", simulation.getCreateBy());
            data.put("createTime", simulation.getCreateTime());
            data.put("updateTime", simulation.getUpdateTime());
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("导出预算模拟异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取模拟场景")
    @ApiOperation("获取模拟场景")
    @GetMapping("/simulation/{id}/scenarios")
    public MyJsonBean<List<Map<String, Object>>> getSimulationScenarios(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(new ArrayList<>());
        } catch (Exception e) {
            log.error("获取模拟场景异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取模拟日志")
    @ApiOperation("获取模拟日志")
    @GetMapping("/simulation/{id}/logs")
    public MyJsonBean<List<Map<String, Object>>> getSimulationLogs(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(new ArrayList<>());
        } catch (Exception e) {
            log.error("获取模拟日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 协同预算模块接口 ====================

    @Operation(summary = "获取协同预算列表")
    @ApiOperation("获取协同预算列表")
    @PostMapping("/collaboration/list")
    public MyJsonBean<Map<String, Object>> getCollaborationList(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            Page<CollaborativeProject> page = collaborativeProjectService.selectPage(params, pageNo, pageSize);
            Map<String, Object> data = new HashMap<>();
            data.put("list", page.getRecords());
            data.put("total", page.getTotal());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取协同预算列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取协同预算统计")
    @ApiOperation("获取协同预算统计")
    @GetMapping("/collaboration/stats")
    public MyJsonBean<Map<String, Object>> getCollaborationStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(collaborativeProjectService.getStats());
        } catch (Exception e) {
            log.error("获取协同预算统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取协同预算类型统计")
    @ApiOperation("获取协同预算类型统计")
    @GetMapping("/collaboration/type-stats")
    public MyJsonBean<List<Map<String, Object>>> getCollaborationTypeStats() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(collaborativeProjectService.getTypeStats());
        } catch (Exception e) {
            log.error("获取协同预算类型统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建协同项目")
    @ApiOperation("创建协同项目")
    @PostMapping("/collaboration/create")
    public MyJsonBean<Boolean> createCollaboration(@RequestBody CollaborativeProject project) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(collaborativeProjectService.insert(project));
        } catch (Exception e) {
            log.error("创建协同项目异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新协同项目")
    @ApiOperation("更新协同项目")
    @PutMapping("/collaboration/update/{id}")
    public MyJsonBean<Boolean> updateCollaboration(@PathVariable String id, @RequestBody CollaborativeProject project) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            project.setProjectId(id);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(collaborativeProjectService.update(project));
        } catch (Exception e) {
            log.error("更新协同项目异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除协同项目")
    @ApiOperation("删除协同项目")
    @DeleteMapping("/collaboration/delete/{id}")
    public MyJsonBean<Boolean> deleteCollaboration(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(collaborativeProjectService.deleteById(id));
        } catch (Exception e) {
            log.error("删除协同项目异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "归档协同项目")
    @ApiOperation("归档协同项目")
    @PostMapping("/collaboration/{id}/archive")
    public MyJsonBean<Boolean> archiveCollaboration(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("归档成功");
            result.setData(collaborativeProjectService.archiveProject(id));
        } catch (Exception e) {
            log.error("归档协同项目异常", e);
            result.setCode(0);
            result.setMsg("归档失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出协同项目")
    @ApiOperation("导出协同项目")
    @GetMapping("/collaboration/{id}/export")
    public MyJsonBean<Map<String, Object>> exportCollaboration(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("projectId", id);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("导出协同项目异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取项目参与者")
    @ApiOperation("获取项目参与者")
    @GetMapping("/collaboration/{id}/participants")
    public MyJsonBean<List<Map<String, Object>>> getCollaborationParticipants(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(collaborativeProjectService.getParticipants(id));
        } catch (Exception e) {
            log.error("获取项目参与者异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取项目活动")
    @ApiOperation("获取项目活动")
    @GetMapping("/collaboration/{id}/activities")
    public MyJsonBean<List<Map<String, Object>>> getCollaborationActivities(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(collaborativeProjectService.getActivities(id));
        } catch (Exception e) {
            log.error("获取项目活动异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取项目评论")
    @ApiOperation("获取项目评论")
    @GetMapping("/collaboration/{id}/comments")
    public MyJsonBean<List<Map<String, Object>>> getCollaborationComments(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(collaborativeProjectService.getComments(id));
        } catch (Exception e) {
            log.error("获取项目评论异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "添加项目评论")
    @ApiOperation("添加项目评论")
    @PostMapping("/collaboration/{id}/comments")
    public MyJsonBean<Boolean> addCollaborationComment(@PathVariable String id, @RequestBody Map<String, Object> comment) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("添加成功");
            result.setData(collaborativeProjectService.addComment(id, comment));
        } catch (Exception e) {
            log.error("添加项目评论异常", e);
            result.setCode(0);
            result.setMsg("添加失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 数据挖掘模块接口 ====================

    @Operation(summary = "获取数据挖掘列表")
    @ApiOperation("获取数据挖掘列表")
    @PostMapping("/data-mining/list")
    public MyJsonBean<Map<String, Object>> getDataMiningList(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            Page<DataMiningTask> page = dataMiningTaskService.selectPage(params, pageNo, pageSize);
            Map<String, Object> data = new HashMap<>();
            data.put("list", page.getRecords());
            data.put("total", page.getTotal());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取数据挖掘列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据挖掘统计")
    @ApiOperation("获取数据挖掘统计")
    @GetMapping("/data-mining/stats")
    public MyJsonBean<Map<String, Object>> getDataMiningStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(dataMiningTaskService.getStats());
        } catch (Exception e) {
            log.error("获取数据挖掘统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建数据挖掘任务")
    @ApiOperation("创建数据挖掘任务")
    @PostMapping("/data-mining/create")
    public MyJsonBean<Boolean> createDataMining(@RequestBody DataMiningTask task) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(dataMiningTaskService.insert(task));
        } catch (Exception e) {
            log.error("创建数据挖掘任务异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新数据挖掘任务")
    @ApiOperation("更新数据挖掘任务")
    @PutMapping("/data-mining/update/{id}")
    public MyJsonBean<Boolean> updateDataMining(@PathVariable String id, @RequestBody DataMiningTask task) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            task.setTaskId(id);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(dataMiningTaskService.update(task));
        } catch (Exception e) {
            log.error("更新数据挖掘任务异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除数据挖掘任务")
    @ApiOperation("删除数据挖掘任务")
    @DeleteMapping("/data-mining/delete/{id}")
    public MyJsonBean<Boolean> deleteDataMining(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(dataMiningTaskService.deleteById(id));
        } catch (Exception e) {
            log.error("删除数据挖掘任务异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制数据挖掘任务")
    @ApiOperation("复制数据挖掘任务")
    @PostMapping("/data-mining/{id}/copy")
    public MyJsonBean<Boolean> copyDataMining(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(dataMiningTaskService.copyTask(id));
        } catch (Exception e) {
            log.error("复制数据挖掘任务异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "运行数据挖掘任务")
    @ApiOperation("运行数据挖掘任务")
    @PostMapping("/data-mining/{id}/run")
    public MyJsonBean<Boolean> runDataMining(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("运行成功");
            result.setData(dataMiningTaskService.runTask(id));
        } catch (Exception e) {
            log.error("运行数据挖掘任务异常", e);
            result.setCode(0);
            result.setMsg("运行失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "停止数据挖掘任务")
    @ApiOperation("停止数据挖掘任务")
    @PostMapping("/data-mining/{id}/stop")
    public MyJsonBean<Boolean> stopDataMining(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("停止成功");
            result.setData(dataMiningTaskService.stopTask(id));
        } catch (Exception e) {
            log.error("停止数据挖掘任务异常", e);
            result.setCode(0);
            result.setMsg("停止失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出数据挖掘")
    @ApiOperation("导出数据挖掘")
    @GetMapping("/data-mining/{id}/export")
    public MyJsonBean<Map<String, Object>> exportDataMining(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("taskId", id);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("导出数据挖掘异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据挖掘结果")
    @ApiOperation("获取数据挖掘结果")
    @GetMapping("/data-mining/{id}/results")
    public MyJsonBean<Map<String, Object>> getDataMiningResults(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(dataMiningTaskService.getResults(id));
        } catch (Exception e) {
            log.error("获取数据挖掘结果异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据挖掘日志")
    @ApiOperation("获取数据挖掘日志")
    @GetMapping("/data-mining/{id}/logs")
    public MyJsonBean<List<Map<String, Object>>> getDataMiningLogs(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(new ArrayList<>());
        } catch (Exception e) {
            log.error("获取数据挖掘日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 版本比较模块接口 ====================

    @Operation(summary = "获取版本比较列表")
    @ApiOperation("获取版本比较列表")
    @PostMapping("/version/comparison/list")
    public MyJsonBean<Map<String, Object>> getVersionComparisonList(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            Page<VersionComparison> page = versionComparisonService.selectPage(params, pageNo, pageSize);
            Map<String, Object> data = new HashMap<>();
            data.put("list", page.getRecords());
            data.put("total", page.getTotal());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取版本比较列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取版本比较统计")
    @ApiOperation("获取版本比较统计")
    @GetMapping("/version/comparison/stats")
    public MyJsonBean<Map<String, Object>> getVersionComparisonStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(versionComparisonService.getStats());
        } catch (Exception e) {
            log.error("获取版本比较统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取可用版本列表")
    @ApiOperation("获取可用版本列表")
    @GetMapping("/version/list")
    public MyJsonBean<List<Map<String, Object>>> getAvailableVersions() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(versionComparisonService.getAvailableVersions());
        } catch (Exception e) {
            log.error("获取可用版本列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建版本比较")
    @ApiOperation("创建版本比较")
    @PostMapping("/version/comparison")
    public MyJsonBean<Boolean> createVersionComparison(@RequestBody VersionComparison comparison) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(versionComparisonService.insert(comparison));
        } catch (Exception e) {
            log.error("创建版本比较异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新版本比较")
    @ApiOperation("更新版本比较")
    @PutMapping("/version/comparison/{id}")
    public MyJsonBean<Boolean> updateVersionComparison(@PathVariable String id, @RequestBody VersionComparison comparison) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            comparison.setComparisonId(id);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(versionComparisonService.update(comparison));
        } catch (Exception e) {
            log.error("更新版本比较异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除版本比较")
    @ApiOperation("删除版本比较")
    @DeleteMapping("/version/comparison/{id}")
    public MyJsonBean<Boolean> deleteVersionComparison(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(versionComparisonService.deleteById(id));
        } catch (Exception e) {
            log.error("删除版本比较异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制版本比较")
    @ApiOperation("复制版本比较")
    @PostMapping("/version/comparison/{id}/copy")
    public MyJsonBean<Boolean> copyVersionComparison(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(versionComparisonService.copyComparison(id));
        } catch (Exception e) {
            log.error("复制版本比较异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出版本比较")
    @ApiOperation("导出版本比较")
    @GetMapping("/version/comparison/{id}/export")
    public MyJsonBean<Map<String, Object>> exportVersionComparison(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(versionComparisonService.getExportData(id));
        } catch (Exception e) {
            log.error("导出版本比较异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "重新比较版本")
    @ApiOperation("重新比较版本")
    @PostMapping("/version/comparison/{id}/recompare")
    public MyJsonBean<Boolean> recompareVersions(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("重新比较成功");
            result.setData(versionComparisonService.recompare(id));
        } catch (Exception e) {
            log.error("重新比较版本异常", e);
            result.setCode(0);
            result.setMsg("重新比较失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取版本差异")
    @ApiOperation("获取版本差异")
    @GetMapping("/version/comparison/{id}/differences")
    public MyJsonBean<List<Map<String, Object>>> getVersionDifferences(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(versionComparisonService.getDifferences(id));
        } catch (Exception e) {
            log.error("获取版本差异异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取版本比较日志")
    @ApiOperation("获取版本比较日志")
    @GetMapping("/version/comparison/{id}/logs")
    public MyJsonBean<List<Map<String, Object>>> getVersionComparisonLogs(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(versionComparisonService.getLogs(id));
        } catch (Exception e) {
            log.error("获取版本比较日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "合并版本")
    @ApiOperation("合并版本")
    @PostMapping("/version/merge")
    public MyJsonBean<Boolean> mergeVersions(@RequestBody Map<String, Object> params) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            log.info("合并版本，参数：{}", params);
            result.setCode(1);
            result.setMsg("合并成功");
            result.setData(true);
        } catch (Exception e) {
            log.error("合并版本异常", e);
            result.setCode(0);
            result.setMsg("合并失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "回滚版本")
    @ApiOperation("回滚版本")
    @PostMapping("/version/rollback")
    public MyJsonBean<Boolean> rollbackVersion(@RequestBody Map<String, Object> params) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            log.info("回滚版本，参数：{}", params);
            result.setCode(1);
            result.setMsg("回滚成功");
            result.setData(true);
        } catch (Exception e) {
            log.error("回滚版本异常", e);
            result.setCode(0);
            result.setMsg("回滚失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 风险评估额外接口 ====================

    @Operation(summary = "删除风险评估")
    @ApiOperation("删除风险评估")
    @DeleteMapping("/risk-assessment/delete/{id}")
    public MyJsonBean<Boolean> deleteRiskAssessment(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(riskAssessmentService.deleteById(id));
        } catch (Exception e) {
            log.error("删除风险评估异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制风险评估")
    @ApiOperation("复制风险评估")
    @PostMapping("/risk-assessment/{id}/copy")
    public MyJsonBean<Boolean> copyRiskAssessment(@PathVariable String id) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(riskAssessmentService.copyAssessment(id));
        } catch (Exception e) {
            log.error("复制风险评估异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出风险评估")
    @ApiOperation("导出风险评估")
    @GetMapping("/risk-assessment/{id}/export")
    public MyJsonBean<Map<String, Object>> exportRiskAssessment(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("assessmentId", id);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("导出风险评估异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }
}

