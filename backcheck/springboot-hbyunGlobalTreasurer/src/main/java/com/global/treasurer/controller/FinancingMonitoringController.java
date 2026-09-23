package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingMonitoringDTO;
import com.global.treasurer.dto.FinancingMonitoringQueryDTO;
import com.global.treasurer.entity.TblFinancingMonitoring;
import com.global.treasurer.service.FinancingMonitoringService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资监控Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
@RestController
@RequestMapping({"/rzgl/financing-monitoring", "/financial/rzgl/financing-monitoring", "/centralaudit/rzgl/financing-monitoring",
                "/rzgl/monitoring", "/financial/rzgl/monitoring", "/centralaudit/rzgl/monitoring",
                "/rzgl", "/financial/rzgl", "/centralaudit/rzgl"
                })
@Api(tags = "融资监控管理")
public class FinancingMonitoringController {
    private static final Logger log = LoggerFactory.getLogger(FinancingMonitoringController.class);

    @Resource
    private FinancingMonitoringService financingMonitoringService;

    @Resource
    private UserProvider userProvider;

    @PostMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询监控列表")
    public String getFinancingMonitoringList(FinancingMonitoringQueryDTO queryDTO,
                                             @RequestHeader(value = "token", required = false) String token,
                                             HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 如果queryDTO为空，创建默认对象
            if (queryDTO == null) {
                queryDTO = new FinancingMonitoringQueryDTO();
            }

            log.info("查询融资监控列表, 参数: alertType={}, alertLevel={}, alertStatus={}, keyword={}, startDate={}, endDate={}",
                    queryDTO.getAlertType(), queryDTO.getAlertLevel(), queryDTO.getAlertStatus(),
                    queryDTO.getKeyword(), queryDTO.getStartDate(), queryDTO.getEndDate());

            PageInfo<TblFinancingMonitoring> pageInfo = financingMonitoringService.getFinancingMonitoringList(queryDTO);
            return new JsonBean(1, "成功", pageInfo).toJson();
        } catch (Exception e) {
            log.error("查询融资监控列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/overview")
    @ResponseBody
    @ApiOperation("获取监控概览")
    public String getMonitoringOverview(@RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> overview = financingMonitoringService.getMonitoringOverview();
            return new JsonBean(1, "成功", overview).toJson();
        } catch (Exception e) {
            log.error("获取监控概览失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/update")
    @ResponseBody
    @ApiOperation("更新监控数据")
    public String updateMonitoringData(FinancingMonitoringDTO dto,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            financingMonitoringService.updateMonitoringData(dto);
            return new JsonBean(1, "更新成功", null).toJson();
        } catch (Exception e) {
            log.error("更新监控数据失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/processAlert")
    @ResponseBody
    @ApiOperation("处理风险预警")
    public String processAlert(@RequestParam Long monitoringId,
                              @RequestParam String handleOpinion,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            financingMonitoringService.processAlert(
                monitoringId,
                handleOpinion,
                loginStaff.getStaffid().longValue(),
                loginStaff.getRealname()
            );
            return new JsonBean(1, "处理成功", null).toJson();
        } catch (Exception e) {
            log.error("处理风险预警失败", e);
            return new JsonBean(0, "处理失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ResponseBody
    @ApiOperation("导出监控报告")
    public String exportMonitoringReport(FinancingMonitoringQueryDTO queryDTO,
                                       @RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<Map<String, Object>> report = financingMonitoringService.exportMonitoringReport(queryDTO);
            return new JsonBean(1, "导出成功", report).toJson();
        } catch (Exception e) {
            log.error("导出监控报告失败", e);
            return new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/dashboard")
    @ResponseBody
    @ApiOperation("获取仪表盘数据")
    public String getDashboardData(@RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> dashboard = financingMonitoringService.getDashboardData();
            return new JsonBean(1, "成功", dashboard).toJson();
        } catch (Exception e) {
            log.error("获取仪表盘数据失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/trends")
    @ResponseBody
    @ApiOperation("获取趋势分析")
    public String getTrendsAnalysis(@RequestParam(required = false, defaultValue = "week") String period,
                                    @RequestHeader(value = "token", required = false) String token,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> trends = financingMonitoringService.getTrendsAnalysis(period);
            return new JsonBean(1, "成功", trends).toJson();
        } catch (Exception e) {
            log.error("获取趋势分析失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/refresh")
    @ResponseBody
    @ApiOperation("刷新监控数据")
    public String refreshMonitoringData(@RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            financingMonitoringService.refreshMonitoringData();
            return new JsonBean(1, "刷新成功", null).toJson();
        } catch (Exception e) {
            log.error("刷新监控数据失败", e);
            return new JsonBean(0, "刷新失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/getById")
    @ResponseBody
    @ApiOperation("根据ID获取融资监控详情")
    public String getFinancingMonitoringById(@RequestParam Long monitoringId,
                                             @RequestHeader(value = "token", required = false) String token,
                                             HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblFinancingMonitoring monitoring = financingMonitoringService.getFinancingMonitoringById(monitoringId);
            return new JsonBean(1, "成功", monitoring).toJson();
        } catch (Exception e) {
            log.error("获取融资监控详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/alerts")
    @ResponseBody
    @ApiOperation("获取预警列表")
    public String getAlertList(@RequestParam(required = false) String alertLevel,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblFinancingMonitoring> alerts = financingMonitoringService.getAlertList(alertLevel);
            return new JsonBean(1, "成功", alerts).toJson();
        } catch (Exception e) {
            log.error("获取预警列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/alerts/page")
    @ResponseBody
    @ApiOperation("分页获取预警列表")
    public String getAlertListPage(@FlexibleRequestBody FinancingMonitoringQueryDTO queryDTO,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            log.info("查询预警列表, alertStatus: {}, pageNum: {}, pageSize: {}",
                    queryDTO.getAlertStatus(), queryDTO.getPageNum(), queryDTO.getPageSize());

            PageInfo<TblFinancingMonitoring> pageInfo = financingMonitoringService.getFinancingMonitoringList(queryDTO);
            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            Map<String, Object> result = new HashMap<>();
            result.put("rows", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("分页获取预警列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ResponseBody
    @ApiOperation("获取统计信息")
    public String getMonitoringStatistics(@RequestHeader(value = "token", required = false) String token,
                                         HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> statistics = financingMonitoringService.getMonitoringStatistics();
            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/chart/type-distribution")
    @ResponseBody
    @ApiOperation("获取融资类型分布图表数据")
    public String getFinancingTypeDistribution(@RequestHeader(value = "token", required = false) String token,
                                               HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<Map<String, Object>> distribution = financingMonitoringService.getFinancingTypeDistribution();
            return new JsonBean(1, "成功", distribution).toJson();
        } catch (Exception e) {
            log.error("获取融资类型分布数据失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/chart/risk-level")
    @ResponseBody
    @ApiOperation("获取风险等级分布图表数据")
    public String getRiskLevelDistribution(@RequestHeader(value = "token", required = false) String token,
                                           HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<Map<String, Object>> distribution = financingMonitoringService.getRiskLevelDistribution();
            return new JsonBean(1, "成功", distribution).toJson();
        } catch (Exception e) {
            log.error("获取风险等级分布数据失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/chart/compliance")
    @ResponseBody
    @ApiOperation("获取合规状态分布图表数据")
    public String getComplianceDistribution(@RequestHeader(value = "token", required = false) String token,
                                            HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<Map<String, Object>> distribution = financingMonitoringService.getComplianceDistribution();
            return new JsonBean(1, "成功", distribution).toJson();
        } catch (Exception e) {
            log.error("获取合规状态分布数据失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/analysis/trend")
    @ResponseBody
    @ApiOperation("获取融资规模趋势数据")
    public String getFinancingScaleTrend(@RequestParam(required = false) Map<String, Object> params,
                                         @RequestHeader(value = "token", required = false) String token,
                                         HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Integer months = 6; // 默认6个月
            if (params != null && params.get("months") != null) {
                months = Integer.parseInt(params.get("months").toString());
            }

            log.info("查询融资规模趋势数据, months: {}", months);
            List<Map<String, Object>> trendData = financingMonitoringService.getFinancingScaleTrend(months);
            return new JsonBean(1, "成功", trendData).toJson();
        } catch (Exception e) {
            log.error("获取融资规模趋势数据失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/analysis/structure")
    @ResponseBody
    @ApiOperation("获取融资结构分析数据")
    public String getFinancingStructureAnalysis(@RequestParam(required = false) Map<String, Object> params,
                                                @RequestHeader(value = "token", required = false) String token,
                                                HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            log.info("查询融资结构分析数据");
            List<Map<String, Object>> structureData = financingMonitoringService.getFinancingStructureAnalysis();
            return new JsonBean(1, "成功", structureData).toJson();
        } catch (Exception e) {
            log.error("获取融资结构分析数据失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }
}
