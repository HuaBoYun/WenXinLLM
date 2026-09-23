package com.huabo.cybermonitor.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.EnterpriseDashboard;
import com.huabo.cybermonitor.service.IEnterpriseDashboardService;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.EnterpriseDashboardQueryVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 企业管理驾驶舱控制器
 *
 * @author huabo
 * @since 2024-12-12
 */
@Tag(name="企业管理驾驶舱管理",description="企业管理驾驶舱管理")
@RestController
@RequestMapping("/v1/enterprise/dashboard")
public class EnterpriseDashboardController {

	private static final Logger log = LoggerFactory.getLogger(EnterpriseDashboardController.class);

    @Autowired
    private IEnterpriseDashboardService enterpriseDashboardService;

    @Operation(summary = "分页查询企业管理驾驶舱数据")
    @PostMapping("/list")
    public R<IPage<EnterpriseDashboard>> list(@RequestBody EnterpriseDashboardQueryVo queryVo) {
        try {
            IPage<EnterpriseDashboard> page = enterpriseDashboardService.selectEnterpriseDashboardPage(queryVo);
            return R.success(page);
        } catch (Exception e) {
            log.error("分页查询企业管理驾驶舱数据失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业管理驾驶舱详情")
    @GetMapping("/{dashboardId}")
    public R<EnterpriseDashboard> getById(@Parameter(description="驾驶舱ID") @PathVariable String dashboardId) {
        try {
            EnterpriseDashboard enterpriseDashboard = enterpriseDashboardService.getById(dashboardId);
            return R.success(enterpriseDashboard);
        } catch (Exception e) {
            log.error("获取企业管理驾驶舱详情失败，驾驶舱ID：{}", dashboardId, e);
            return R.fail("获取详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业驾驶舱完整数据")
    @GetMapping("/enterprise/{enterpriseId}")
    public R<Map<String, Object>> getEnterpriseDashboard(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> dashboardData = enterpriseDashboardService.getEnterpriseDashboard(enterpriseId);
            return R.success(dashboardData);
        } catch (Exception e) {
            log.error("获取企业驾驶舱完整数据失败，企业ID：{}", enterpriseId, e);
            return R.fail("获取驾驶舱数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业概览数据")
    @PostMapping("/overview")
    public R<Map<String, Object>> getEnterpriseOverview(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            Map<String, Object> overview = enterpriseDashboardService.getEnterpriseOverview(enterpriseId);
            return R.success(overview);
        } catch (Exception e) {
            log.error("获取企业概览数据失败", e);
            return R.fail("获取概览数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业关键指标数据")
    @PostMapping("/indicators")
    public R<Map<String, Object>> getKeyIndicators(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            Map<String, Object> indicators = enterpriseDashboardService.getKeyIndicators(enterpriseId);
            return R.success(indicators);
        } catch (Exception e) {
            log.error("获取企业关键指标数据失败", e);
            return R.fail("获取关键指标失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业业务状态监控数据")
    @PostMapping("/business-status")
    public R<Map<String, Object>> getBusinessStatus(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            Map<String, Object> businessStatus = enterpriseDashboardService.getBusinessStatus(enterpriseId);
            return R.success(businessStatus);
        } catch (Exception e) {
            log.error("获取企业业务状态监控数据失败", e);
            return R.fail("获取业务状态失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业风险监控数据")
    @PostMapping("/risk-monitoring")
    public R<Map<String, Object>> getRiskMonitoring(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            Map<String, Object> riskMonitoring = enterpriseDashboardService.getRiskMonitoring(enterpriseId);
            return R.success(riskMonitoring);
        } catch (Exception e) {
            log.error("获取企业风险监控数据失败", e);
            return R.fail("获取风险监控数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业数据报送状态")
    @PostMapping("/submission-status")
    public R<Map<String, Object>> getDataSubmissionStatus(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            Map<String, Object> submissionStatus = enterpriseDashboardService.getDataSubmissionStatus(enterpriseId);
            return R.success(submissionStatus);
        } catch (Exception e) {
            log.error("获取企业数据报送状态失败", e);
            return R.fail("获取数据报送状态失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业财务指标趋势数据")
    @PostMapping("/financial-trend")
    public R<List<Map<String, Object>>> getFinancialTrend(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            Integer months = (Integer) params.get("months");
            List<Map<String, Object>> trendData = enterpriseDashboardService.getFinancialTrend(enterpriseId, months);
            return R.success(trendData);
        } catch (Exception e) {
            log.error("获取企业财务指标趋势数据失败", e);
            return R.fail("获取财务趋势数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业业务分布数据")
    @PostMapping("/business-distribution")
    public R<List<Map<String, Object>>> getBusinessDistribution(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            List<Map<String, Object>> distributionData = enterpriseDashboardService.getBusinessDistribution(enterpriseId);
            return R.success(distributionData);
        } catch (Exception e) {
            log.error("获取企业业务分布数据失败", e);
            return R.fail("获取业务分布数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业经营计划执行进度")
    @PostMapping("/plan-execution")
    public R<List<Map<String, Object>>> getOperatingPlanExecution(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            List<Map<String, Object>> planExecution = enterpriseDashboardService.getOperatingPlanExecution(enterpriseId);
            return R.success(planExecution);
        } catch (Exception e) {
            log.error("获取企业经营计划执行进度失败", e);
            return R.fail("获取计划执行进度失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取企业预算执行进度")
    @PostMapping("/budget-execution")
    public R<List<Map<String, Object>>> getBudgetExecution(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            List<Map<String, Object>> budgetExecution = enterpriseDashboardService.getBudgetExecution(enterpriseId);
            return R.success(budgetExecution);
        } catch (Exception e) {
            log.error("获取企业预算执行进度失败", e);
            return R.fail("获取预算执行进度失败：" + e.getMessage());
        }
    }

    @Operation(summary = "刷新企业驾驶舱数据")
    @PostMapping("/refresh/{enterpriseId}")
    public R<Boolean> refreshDashboardData(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            boolean result = enterpriseDashboardService.refreshDashboardData(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("刷新企业驾驶舱数据失败，企业ID：{}", enterpriseId, e);
            return R.fail("刷新数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出企业驾驶舱报告")
    @PostMapping("/export")
    public void exportDashboardReport(@RequestBody Map<String, Object> params, javax.servlet.http.HttpServletResponse response) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            String enterpriseName = (String) params.get("enterpriseName");
            String reportType = (String) params.get("reportType");

            // 获取报告文件字节数组
            byte[] fileBytes = enterpriseDashboardService.exportDashboardReportAsBytes(enterpriseId, reportType);

            // 设置响应头
            String fileName = enterpriseName + "管理驾驶舱报告.xlsx";
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLength(fileBytes.length);

            // 写入响应流
            javax.servlet.ServletOutputStream out = response.getOutputStream();
            out.write(fileBytes);
            out.flush();
            out.close();

        } catch (Exception e) {
            log.error("导出企业驾驶舱报告失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出报告失败：" + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    @Operation(summary = "获取企业驾驶舱配置")
    @GetMapping("/config/{enterpriseId}")
    public R<Map<String, Object>> getDashboardConfig(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> config = enterpriseDashboardService.getDashboardConfig(enterpriseId);
            return R.success(config);
        } catch (Exception e) {
            log.error("获取企业驾驶舱配置失败，企业ID：{}", enterpriseId, e);
            return R.fail("获取配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "保存企业驾驶舱配置")
    @PostMapping("/config")
    public R<Boolean> saveDashboardConfig(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            @SuppressWarnings("unchecked")
            Map<String, Object> configData = (Map<String, Object>) params.get("configData");
            boolean result = enterpriseDashboardService.saveDashboardConfig(enterpriseId, configData);
            return R.success(result);
        } catch (Exception e) {
            log.error("保存企业驾驶舱配置失败", e);
            return R.fail("保存配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增企业管理驾驶舱数据")
    @PostMapping
    public R<Boolean> add(@RequestBody EnterpriseDashboard enterpriseDashboard) {
        try {
            boolean result = enterpriseDashboardService.addEnterpriseDashboard(enterpriseDashboard);
            return R.success(result);
        } catch (Exception e) {
            log.error("新增企业管理驾驶舱数据失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新企业管理驾驶舱数据")
    @PutMapping
    public R<Boolean> update(@RequestBody EnterpriseDashboard enterpriseDashboard) {
        try {
            boolean result = enterpriseDashboardService.updateEnterpriseDashboard(enterpriseDashboard);
            return R.success(result);
        } catch (Exception e) {
            log.error("更新企业管理驾驶舱数据失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除企业管理驾驶舱数据")
    @DeleteMapping("/{dashboardId}")
    public R<Boolean> delete(@Parameter(description="驾驶舱ID") @PathVariable String dashboardId) {
        try {
            boolean result = enterpriseDashboardService.deleteEnterpriseDashboard(dashboardId);
            return R.success(result);
        } catch (Exception e) {
            log.error("删除企业管理驾驶舱数据失败，驾驶舱ID：{}", dashboardId, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除企业管理驾驶舱数据")
    @DeleteMapping("/batch")
    public R<Boolean> deleteBatch(@RequestBody List<String> dashboardIds) {
        try {
            boolean result = enterpriseDashboardService.deleteBatchEnterpriseDashboard(dashboardIds);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量删除企业管理驾驶舱数据失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "统计企业驾驶舱数据")
    @PostMapping("/statistics")
    public R<Map<String, Object>> getStatistics(@RequestBody EnterpriseDashboardQueryVo queryVo) {
        try {
            Map<String, Object> statistics = enterpriseDashboardService.getStatistics(queryVo);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("统计企业驾驶舱数据失败", e);
            return R.fail("统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证企业驾驶舱数据")
    @PostMapping("/validate")
    public R<Map<String, Object>> validateDashboardData(@RequestBody EnterpriseDashboard enterpriseDashboard) {
        try {
            Map<String, Object> result = enterpriseDashboardService.validateDashboardData(enterpriseDashboard);
            return R.success(result);
        } catch (Exception e) {
            log.error("验证企业驾驶舱数据失败", e);
            return R.fail("验证失败：" + e.getMessage());
        }
    }

    @Operation(summary = "生成企业驾驶舱数据")
    @PostMapping("/generate")
    public R<Boolean> generateDashboardData(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            String statisticsPeriod = (String) params.get("statisticsPeriod");
            boolean result = enterpriseDashboardService.generateDashboardData(enterpriseId, statisticsPeriod);
            return R.success(result);
        } catch (Exception e) {
            log.error("生成企业驾驶舱数据失败", e);
            return R.fail("生成数据失败：" + e.getMessage());
        }
    }
}
