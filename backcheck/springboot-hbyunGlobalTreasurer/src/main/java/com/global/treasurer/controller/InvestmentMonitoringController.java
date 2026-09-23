package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.InvestmentMonitoringDTO;
import com.global.treasurer.dto.InvestmentMonitoringQueryDTO;
import com.global.treasurer.entity.TblInvestmentMonitoring;
import com.global.treasurer.service.InvestmentMonitoringService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资监控Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Controller
@RequestMapping("/investment/monitoring")
@Api(tags = "投资监控管理")
public class InvestmentMonitoringController {
    private static final Logger log = LoggerFactory.getLogger(InvestmentMonitoringController.class);

    @Resource
    private InvestmentMonitoringService investmentMonitoringService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("获取投资监控列表")
    public String getInvestmentMonitoringList(InvestmentMonitoringQueryDTO queryDTO,
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

            PageInfo<TblInvestmentMonitoring> pageInfo = investmentMonitoringService.getInvestmentMonitoringList(queryDTO);

            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            Map<String, Object> result = new HashMap<>();
            result.put("rows", pageInfo.getList());
            result.put("total", pageInfo.getTotal());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询投资监控列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/getById")
    @ResponseBody
    @ApiOperation("根据ID获取投资监控详情")
    public String getInvestmentMonitoringById(@RequestParam Long monitoringId,
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

            TblInvestmentMonitoring monitoring = investmentMonitoringService.getInvestmentMonitoringById(monitoringId);
            return new JsonBean(1, "成功", monitoring).toJson();
        } catch (Exception e) {
            log.error("获取投资监控详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增投资监控
     */
    @PostMapping
    @ResponseBody
    @ApiOperation("新增投资监控")
    public String addInvestmentMonitoring(@FlexibleRequestBody InvestmentMonitoringDTO dto,
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

            TblInvestmentMonitoring monitoring = investmentMonitoringService.saveInvestmentMonitoring(dto);
            return new JsonBean(1, "新增成功", monitoring).toJson();
        } catch (Exception e) {
            log.error("新增投资监控失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 修改投资监控
     */
    @PutMapping
    @ResponseBody
    @ApiOperation("修改投资监控")
    public String updateInvestmentMonitoring(@FlexibleRequestBody InvestmentMonitoringDTO dto,
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

            TblInvestmentMonitoring monitoring = investmentMonitoringService.saveInvestmentMonitoring(dto);
            return new JsonBean(1, "修改成功", monitoring).toJson();
        } catch (Exception e) {
            log.error("修改投资监控失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/save")
    @ResponseBody
    @ApiOperation("保存投资监控")
    public String saveInvestmentMonitoring(InvestmentMonitoringDTO dto,
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

            TblInvestmentMonitoring monitoring = investmentMonitoringService.saveInvestmentMonitoring(dto);
            return new JsonBean(1, "保存成功", monitoring).toJson();
        } catch (Exception e) {
            log.error("保存投资监控失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    @ApiOperation("删除投资监控")
    public String deleteInvestmentMonitoring(@RequestParam Long monitoringId,
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

            investmentMonitoringService.deleteInvestmentMonitoring(monitoringId);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除投资监控失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batchDelete")
    @ResponseBody
    @ApiOperation("批量删除投资监控")
    public String batchDeleteInvestmentMonitorings(@RequestParam("monitoringIds") List<Long> monitoringIds,
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

            log.info("批量删除投资监控，IDs：{}", monitoringIds);

            if (monitoringIds == null || monitoringIds.isEmpty()) {
                log.warn("批量删除失败：ID列表为空");
                return new JsonBean(0, "请选择要删除的记录", null).toJson();
            }

            investmentMonitoringService.batchDeleteInvestmentMonitorings(monitoringIds);
            log.info("批量删除成功，删除数量：{}", monitoringIds.size());
            return new JsonBean(1, "批量删除成功", null).toJson();
        } catch (Exception e) {
            log.error("批量删除投资监控失败，IDs：{}, 错误：{}", monitoringIds, e.getMessage(), e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/create")
    @ResponseBody
    @ApiOperation("创建投资监控记录")
    public String createMonitoring(@RequestParam Long investmentId,
                                  @RequestParam String monitoringType,
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

            investmentMonitoringService.createMonitoring(investmentId, monitoringType);
            return new JsonBean(1, "创建成功", null).toJson();
        } catch (Exception e) {
            log.error("创建投资监控失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/updateData")
    @ResponseBody
    @ApiOperation("更新监控数据")
    public String updateMonitoringData(@RequestParam Long monitoringId,
                                      @RequestParam BigDecimal currentValue,
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

            investmentMonitoringService.updateMonitoringData(monitoringId, currentValue);
            return new JsonBean(1, "更新成功", null).toJson();
        } catch (Exception e) {
            log.error("更新监控数据失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/calculatePerformance")
    @ResponseBody
    @ApiOperation("计算绩效评分")
    public String calculatePerformanceScore(@RequestParam Long monitoringId,
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

            investmentMonitoringService.calculatePerformanceScore(monitoringId);
            return new JsonBean(1, "计算成功", null).toJson();
        } catch (Exception e) {
            log.error("计算绩效评分失败", e);
            return new JsonBean(0, "计算失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/calculateRisk")
    @ResponseBody
    @ApiOperation("计算风险评分")
    public String calculateRiskScore(@RequestParam Long monitoringId,
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

            investmentMonitoringService.calculateRiskScore(monitoringId);
            return new JsonBean(1, "计算成功", null).toJson();
        } catch (Exception e) {
            log.error("计算风险评分失败", e);
            return new JsonBean(0, "计算失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/alert/{monitoringId}")
    @ResponseBody
    @ApiOperation("处理监控预警")
    public String processAlert(@PathVariable Long monitoringId,
                              @RequestParam String alertType,
                              @RequestParam String alertMessage,
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

            log.info("处理预警，监控ID：{}，预警类型：{}，处理信息：{}", monitoringId, alertType, alertMessage);

            return new JsonBean(1, "预警处理成功", null).toJson();
        } catch (Exception e) {
            log.error("处理预警失败", e);
            return new JsonBean(0, "处理失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/generateAlert")
    @ResponseBody
    @ApiOperation("生成预警")
    public String generateAlert(@RequestParam Long monitoringId,
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

            investmentMonitoringService.generateAlert(monitoringId);
            return new JsonBean(1, "生成成功", null).toJson();
        } catch (Exception e) {
            log.error("生成预警失败", e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null).toJson();
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

            Map<String, Object> dashboard = investmentMonitoringService.getDashboardData();
            return new JsonBean(1, "成功", dashboard).toJson();
        } catch (Exception e) {
            log.error("获取仪表盘数据失败", e);
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

            List<TblInvestmentMonitoring> alerts = investmentMonitoringService.getAlertList(alertLevel);
            return new JsonBean(1, "成功", alerts).toJson();
        } catch (Exception e) {
            log.error("获取预警列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ResponseBody
    @ApiOperation("获取投资监控统计信息")
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

            Map<String, Object> statistics = investmentMonitoringService.getMonitoringStatistics();
            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取监控分析数据
     */
    @GetMapping("/analysis")
    @ResponseBody
    @ApiOperation("获取监控分析数据")
    public String getMonitoringAnalysis(@RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> analysis = investmentMonitoringService.getMonitoringAnalysis();
            return new JsonBean(1, "成功", analysis).toJson();
        } catch (Exception e) {
            log.error("获取监控分析数据失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取风险趋势分析
     */
    @GetMapping("/trend")
    @ResponseBody
    @ApiOperation("获取风险趋势分析")
    public String getRiskTrend(@RequestParam(required = false, defaultValue = "6") Integer months,
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

            List<Map<String, Object>> trend = investmentMonitoringService.getRiskTrend(months);
            return new JsonBean(1, "成功", trend).toJson();
        } catch (Exception e) {
            log.error("获取风险趋势分析失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 生成监控记录
     */
    @PostMapping("/generate")
    @ResponseBody
    @ApiOperation("生成监控记录")
    public String generateMonitoringRecords(@RequestParam String monitoringDate,
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

            log.info("生成监控记录，日期：{}", monitoringDate);

            return new JsonBean(1, "生成成功", null).toJson();
        } catch (Exception e) {
            log.error("生成监控记录失败", e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取优化建议
     */
    @GetMapping("/recommendations")
    @ResponseBody
    @ApiOperation("获取优化建议")
    public String getOptimizationRecommendations(@RequestHeader(value = "token", required = false) String token,
                                                HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<Map<String, Object>> recommendations = investmentMonitoringService.getOptimizationRecommendations();
            return new JsonBean(1, "成功", recommendations).toJson();
        } catch (Exception e) {
            log.error("获取优化建议失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 异常检测
     */
    @GetMapping("/anomaly")
    @ResponseBody
    @ApiOperation("异常检测")
    public String detectAnomalies(@RequestParam(required = false) String investmentType,
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

            List<Map<String, Object>> anomalies = investmentMonitoringService.detectAnomalies(investmentType);
            return new JsonBean(1, "成功", anomalies).toJson();
        } catch (Exception e) {
            log.error("异常检测失败", e);
            return new JsonBean(0, "检测失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出投资监控数据
     */
    @GetMapping("/export")
    @ApiOperation("导出投资监控数据")
    public void exportMonitoring(InvestmentMonitoringQueryDTO queryDTO,
                                 @RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 查询所有符合条件的监控数据（不分页）
            queryDTO.setPageNum(null);
            queryDTO.setPageSize(null);
            PageInfo<TblInvestmentMonitoring> pageInfo = investmentMonitoringService.getInvestmentMonitoringList(queryDTO);
            List<TblInvestmentMonitoring> monitorings = pageInfo.getList();

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = "投资监控列表_" + System.currentTimeMillis() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("投资监控");

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"监控编号", "投资类型", "投资标的", "监控指标", "当前值", "预警阈值", "风险等级", "监控状态", "监控日期"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            for (int i = 0; i < monitorings.size(); i++) {
                TblInvestmentMonitoring monitoring = monitorings.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(monitoring.getMonitoringId() != null ? String.valueOf(monitoring.getMonitoringId()) : "");
                row.createCell(1).setCellValue(monitoring.getInvestmentType() != null ? monitoring.getInvestmentType() : "");
                row.createCell(2).setCellValue(monitoring.getInvestmentTarget() != null ? monitoring.getInvestmentTarget() : "");
                row.createCell(3).setCellValue(monitoring.getMonitoringIndicator() != null ? monitoring.getMonitoringIndicator() : "");
                row.createCell(4).setCellValue(monitoring.getCurrentValue() != null ? monitoring.getCurrentValue().doubleValue() : 0);
                row.createCell(5).setCellValue(monitoring.getAlertThreshold() != null ? monitoring.getAlertThreshold().doubleValue() : 0);
                row.createCell(6).setCellValue(monitoring.getRiskLevel() != null ? monitoring.getRiskLevel() : "");
                row.createCell(7).setCellValue(monitoring.getMonitoringStatus() != null ? monitoring.getMonitoringStatus() : "");
                row.createCell(8).setCellValue(monitoring.getMonitoringDate() != null ? monitoring.getMonitoringDate().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
            workbook.close();

            log.info("导出投资监控列表成功, count: {}", monitorings.size());
        } catch (Exception e) {
            log.error("导出投资监控数据失败", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception ex) {
                log.error("设置错误状态失败", ex);
            }
        }
    }
}
