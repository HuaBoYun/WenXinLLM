package com.financial.sharing.oracle.controller;

import com.financial.sharing.oracle.dto.QueryOptimizationDTO;
import com.financial.sharing.oracle.service.SqlOptimizationService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * SQL性能优化Controller
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Slf4j
@RestController
@RequestMapping("/financial/sql/optimization")
@Api(tags = "SQL性能优化管理")
public class SqlOptimizationController {

    @Autowired
    private SqlOptimizationService sqlOptimizationService;

    @Autowired
    private UserProvider userProvider;

    @PostMapping("/slowQueries")
    @ApiOperation("分析慢查询")
    public String analyzeSlowQueries(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "开始日期", example = "2025-12-01") @RequestParam(required = false) String startDate,
            @ApiParam(value = "结束日期", example = "2025-12-07") @RequestParam(required = false) String endDate,
            @ApiParam(value = "最小执行时间(毫秒)", example = "1000") @RequestParam(required = false, defaultValue = "1000") Long minExecuteTime) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.analyzeSlowQueries(startDate, endDate, minExecuteTime).toString();
        } catch (Exception e) {
            log.error("分析慢查询失败", e);
            return JsonBean.error("分析慢查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/tablespace")
    @ApiOperation("获取表空间使用情况")
    public String getTablespaceUsage(
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.getTablespaceUsage().toString();
        } catch (Exception e) {
            log.error("获取表空间使用情况失败", e);
            return JsonBean.error("获取表空间使用情况失败: " + e.getMessage());
        }
    }

    @GetMapping("/indexUsage")
    @ApiOperation("分析索引使用情况")
    public String analyzeIndexUsage(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "表名", required = true) @RequestParam String tableName) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.analyzeIndexUsage(tableName).toString();
        } catch (Exception e) {
            log.error("分析索引使用情况失败", e);
            return JsonBean.error("分析索引使用情况失败: " + e.getMessage());
        }
    }

    @PostMapping("/executionPlan")
    @ApiOperation("获取SQL执行计划")
    public String getExecutionPlan(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "SQL语句", required = true) @RequestBody Map<String, String> params) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            String sql = params.get("sql");
            if (sql == null || sql.trim().isEmpty()) {
                return JsonBean.error("SQL语句不能为空");
            }

            return sqlOptimizationService.getExecutionPlan(sql).toString();
        } catch (Exception e) {
            log.error("获取执行计划失败", e);
            return JsonBean.error("获取执行计划失败: " + e.getMessage());
        }
    }

    @GetMapping("/performanceReport/{reportType}")
    @ApiOperation("生成性能报告")
    public String generatePerformanceReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "报告类型(daily/weekly/monthly)", required = true) @PathVariable String reportType) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.generatePerformanceReport(reportType).toString();
        } catch (Exception e) {
            log.error("生成性能报告失败", e);
            return JsonBean.error("生成性能报告失败: " + e.getMessage());
        }
    }

    @GetMapping("/waitEvents")
    @ApiOperation("获取数据库等待事件")
    public String getWaitEvents(
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.getWaitEvents().toString();
        } catch (Exception e) {
            log.error("获取等待事件失败", e);
            return JsonBean.error("获取等待事件失败: " + e.getMessage());
        }
    }

    @GetMapping("/fragmentation")
    @ApiOperation("分析表碎片")
    public String analyzeTableFragmentation(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "模式名", required = true) @RequestParam String schema) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.analyzeTableFragmentation(schema).toString();
        } catch (Exception e) {
            log.error("分析表碎片失败", e);
            return JsonBean.error("分析表碎片失败: " + e.getMessage());
        }
    }

    @GetMapping("/indexRecommendations")
    @ApiOperation("生成索引建议")
    public String generateIndexRecommendations(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "表名", required = true) @RequestParam String tableName) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.generateIndexRecommendations(tableName).toString();
        } catch (Exception e) {
            log.error("生成索引建议失败", e);
            return JsonBean.error("生成索引建议失败: " + e.getMessage());
        }
    }

    @GetMapping("/longRunningQueries")
    @ApiOperation("监控长时间运行的查询")
    public String monitorLongRunningQueries(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "阈值(秒)", example = "300") @RequestParam(required = false, defaultValue = "300") Long threshold) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.monitorLongRunningQueries(threshold).toString();
        } catch (Exception e) {
            log.error("监控长时间运行查询失败", e);
            return JsonBean.error("监控长时间运行查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchOptimize")
    @ApiOperation("批量优化查询")
    public String batchOptimizeQueries(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "优化配置列表", required = true) @RequestBody List<QueryOptimizationDTO> optimizations) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.batchOptimizeQueries(optimizations).toString();
        } catch (Exception e) {
            log.error("批量优化查询失败", e);
            return JsonBean.error("批量优化查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/tableStatistics")
    @ApiOperation("获取表统计信息")
    public String getTableStatistics(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "表名", required = true) @RequestParam String tableName) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.getTableStatistics(tableName).toString();
        } catch (Exception e) {
            log.error("获取表统计信息失败", e);
            return JsonBean.error("获取表统计信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStatistics")
    @ApiOperation("更新表统计信息")
    public String updateTableStatistics(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "表名", required = true) @RequestParam String tableName,
            @ApiParam(value = "估算百分比", example = "100") @RequestParam(required = false, defaultValue = "100") Integer estimatePercent) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.updateTableStatistics(tableName, estimatePercent).toString();
        } catch (Exception e) {
            log.error("更新表统计信息失败", e);
            return JsonBean.error("更新表统计信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/missingIndexes")
    @ApiOperation("检查缺失的索引")
    public String checkMissingIndexes(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "模式名", required = true) @RequestParam String schema) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.checkMissingIndexes(schema).toString();
        } catch (Exception e) {
            log.error("检查缺失索引失败", e);
            return JsonBean.error("检查缺失索引失败: " + e.getMessage());
        }
    }

    @GetMapping("/sqlHistory")
    @ApiOperation("获取SQL执行历史")
    public String getSqlExecutionHistory(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "SQL ID", required = true) @RequestParam String sqlId,
            @ApiParam(value = "天数", example = "7") @RequestParam(required = false, defaultValue = "7") Integer days) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.getSqlExecutionHistory(sqlId, days).toString();
        } catch (Exception e) {
            log.error("获取SQL执行历史失败", e);
            return JsonBean.error("获取SQL执行历史失败: " + e.getMessage());
        }
    }

    @GetMapping("/dashboard")
    @ApiOperation("生成性能监控看板")
    public String generatePerformanceDashboard(
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            // 权限验证
            if (!checkPermission(request, response)) {
                return null;
            }

            return sqlOptimizationService.generatePerformanceDashboard().toString();
        } catch (Exception e) {
            log.error("生成性能监控看板失败", e);
            return JsonBean.error("生成性能监控看板失败: " + e.getMessage());
        }
    }

    /**
     * 权限验证
     */
    private boolean checkPermission(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || token.trim().isEmpty()) {
                token = request.getParameter("token");
            }

            if (token == null || token.trim().isEmpty()) {
                String json = JsonBean.error("未提供认证令牌");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.setStatus(401);
                response.getWriter().write(json);
                return false;
            }

            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                String json = JsonBean.error("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.setStatus(401);
                response.getWriter().write(json);
                return false;
            }

            // 检查是否有DBA权限
            if (!hasDbaPermission(loginStaff)) {
                String json = JsonBean.error("没有DBA权限");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.setStatus(403);
                response.getWriter().write(json);
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("权限验证失败", e);
            try {
                String json = JsonBean.error("权限验证异常");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.setStatus(500);
                response.getWriter().write(json);
            } catch (Exception ex) {
                log.error("返回错误响应失败", ex);
            }
            return false;
        }
    }

    /**
     * 检查是否有DBA权限
     */
    private boolean hasDbaPermission(Object loginStaff) {
        // TODO: 实现实际的权限检查逻辑
        // 这里应该检查用户是否有DBA或系统管理员权限
        return true; // 暂时返回true
    }
}